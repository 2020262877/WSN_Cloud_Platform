package com.gong.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gong.pojo.*;
import com.gong.service.GateDeviceService;
import com.gong.service.GateService;
import com.gong.service.HistoryDataService;
import com.gong.utils.CodeExchange;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/historydata")
public class HistoryDataController {

    @Autowired
    private HistoryDataService historyDataService;

    @Autowired
    private GateDeviceService gateDeviceService;

    @Autowired
    private GateService gateService;

    private static String appKey = "6532364baea54d15abaa927450f68172";
    private static String appSecret="b043a0082a7f73176f66e185ef99347c";

    @RequestMapping("/toreal")
    public String toRealJsp(HttpServletRequest request){
        int id=1;
        VideoToken videoToken = historyDataService.getVideoToken(id);
        String accesstoken = videoToken.getAccesstoken();
        List<GateDevice> gateDevices = gateDeviceService.queryGateDeviceList();
        request.setAttribute("gateDeviceList",gateDevices);
        request.setAttribute("accesstoken",accesstoken);
        return "real";
    }

    @RequestMapping("/last")
    public void queryHistoryDataLast(HttpServletResponse response) throws IOException {
        int gdid = 2;
        HistoryData dataLast = historyDataService.queryHistoryDataLast(gdid);
        int sendHumi;
        int sendTemp;
        JSONObject e = (JSONObject) JSONObject.parse(((HistoryData)dataLast).getRecorddata());

        // 以下需要查表了解有那些数据
        sendHumi=Integer.parseInt(e.getString("humi"));
        sendTemp=Integer.parseInt(e.getString("temp"));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("humi", sendHumi);
        map.put("temp", sendTemp);
        String jsonStr = JSON.toJSONString(map);
        PrintWriter out = response.getWriter();
        out.println(jsonStr);
        return;
    }

    @RequestMapping("/add")
    public void addHistoryData(HttpServletResponse response, HttpServletRequest request) throws IOException {
        HistoryData historyData = new HistoryData();
        InputStream is= request.getInputStream();
        String bodyInfo = IOUtils.toString(is, "UTF-8");
        System.out.println("Recv Data:" + bodyInfo);
        // 获取数据，存放到值 JavaBean user
        historyData.setGdid(2);
        historyData.setRecorddata(CodeExchange.chinese(bodyInfo));
        historyData.setRecordtime(new Timestamp((new Date()).getTime()));
        PrintWriter out = response.getWriter();
        int i = historyDataService.addHistoryData(historyData);
        if (i>0) {
            System.out.println("添加成功！!!!");
            out.println("1");
        } else {
            System.out.println("添加失败！");
		    out.println("0");
        }
        return;
    }

    @RequestMapping("/query")
    public void queryHistoryDataList(HttpServletRequest request,HttpServletResponse response) throws IOException {
        int gdid = Integer.parseInt(request.getParameter("gdid"));
        String starttime = CodeExchange.chinese(request.getParameter("starttime"));
        String endtime = CodeExchange.chinese(request.getParameter("endtime"));
        List<HistoryData> queryDatas = historyDataService.queryHistoryDataList(gdid,starttime,endtime);
        ArrayList<Integer> sendHumi = new ArrayList<Integer>();
        ArrayList<Integer> sendTemp = new ArrayList<Integer>();
        ArrayList<String> sendTime = new ArrayList<String>();
        if (queryDatas != null && queryDatas.size() > 0) {
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for(int i = 0;i<queryDatas.size();i++) {
                JSONObject e = (JSONObject) JSONObject.parse(((HistoryData)queryDatas.get(i)).getRecorddata());
                sendTime.add(sdf.format(((HistoryData)queryDatas.get(i)).getRecordtime()));
                // 以下需要查表了解有那些数据
                sendHumi.add(Integer.parseInt(e.getString("humi")));
                sendTemp.add(Integer.parseInt(e.getString("temp")));
            }
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("time", sendTime);
        map.put("humi", sendHumi);
        map.put("temp", sendTemp);
        String jsonStr = JSON.toJSONString(map);
        PrintWriter out = response.getWriter();
        out.println(jsonStr);
        return;
    }

    @RequestMapping("/show")
    public String showHistoryDataList(HttpServletRequest request){
        Map<Integer,String> gataMap = new HashMap();
        List<Gate> gates = gateService.queryGateList();
        for(Gate gate:gates){
            gataMap.put(gate.getId(),gate.getGatename());
        }
        HttpSession session=request.getSession();
        String deviceForm = "<!-- 传感器面板实例 -->	<div class='col-md-6'>		<div class='panel panel-primary'>			<div class='panel-heading'>				<h3 class='panel-title text-center'>%s监控</h3>			</div>			<div class='panel-body'>				<div class='row'>					<div class='col-sm-5'>						<div class='form-group'>							<label>选择开始时间：</label>							<!--指定 date标记-->							<div class='input-group date' id='%s-dtp1'>								<input type='text' class='form-control' /> <span									class='input-group-addon'> <span									class='glyphicon glyphicon-calendar'></span>								</span>							</div>						</div>					</div>					<div class='col-sm-5'>						<div class='form-group'>							<label>选择结束时间：</label>							<!--指定 date标记-->							<div class='input-group date' id='%s-dtp2'>								<input type='text' class='form-control' /> <span									class='input-group-addon'> <span									class='glyphicon glyphicon-calendar'></span>								</span>							</div>						</div>					</div>					<div class='col-sm-2'>						<div class='form-group'>							<div class='btn-group-vertical'>								<button id='chongzhi' type='submit' class='btn btn-default' onclick='chongzhi()'>重置</button>								<button id='%s-que' type='submit' class='btn btn-primary'>查询</button>							</div>						</div>					</div>				</div>				<div class='row'>					<div id='%s-chart'	style='min-width: 400px; height: 350px; margin: 0 auto;overflow:auto;'></div>				</div>			</div>		</div>	</div>";
        String newForm = "";
        List<ScriptNeedInfo> infoList = new ArrayList<ScriptNeedInfo>();
        if(request.getParameter("gid")!=null) {
            List<String> newForms = new ArrayList<String>();
            int gid = Integer.parseInt(request.getParameter("gid"));
            List<GateDevice> gateDevices = gateDeviceService.queryGateDeviceByGId(gid);
            for (GateDevice gateDevice : gateDevices) {
                ScriptNeedInfo tmpInfo = new ScriptNeedInfo();
                String formName = gateDevice.getClientdeviceid();
                tmpInfo.setId(gateDevice.getId());
                tmpInfo.setCode(formName);
                tmpInfo.setDescript(gateDevice.getClientdevicename());
                newForm = String.format(deviceForm, gateDevice.getClientdevicename(), formName, formName, formName, formName, formName);
                newForms.add(newForm);
                infoList.add(tmpInfo);
            }
            request.setAttribute("newForms", newForms);
            request.setAttribute("gid", gid); // 回传上传查询内容
        }
        session.setAttribute("infoList", infoList);
        request.setAttribute("gateMap",gataMap);
        return "history";
    }

    @RequestMapping("/simulation")
    public String toSimulation(){
        return "simulation";
    }

    @Scheduled(cron = "0 0 13 ? * MON")
    public void updateVideoToken() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        StringBuffer params = new StringBuffer();
        params.append("appKey="+appKey);
        params.append("&");
        params.append("appSecret="+appSecret);
        // 创建Post请求
        HttpPost httpPost = new HttpPost("https://open.ys7.com/api/lapp/token/get" + "?" + params);
        httpPost.setHeader("Content-Type","application/x-www-form-urlencoded");
        // 响应模型
        CloseableHttpResponse response = null;
        // 由客户端执行(发送)Post请求
        response = httpClient.execute(httpPost);
        // 从响应模型中获取响应实体
        HttpEntity responseEntity = response.getEntity();
        if (responseEntity != null) {
            //返回数据
            String responseString = EntityUtils.toString(responseEntity);
            //生成实体类（responseString就可以看到数据的）
            JSONObject jsonObject = JSON.parseObject(responseString);
            String msg = jsonObject.getString("msg");
            String code = jsonObject.getString("code");
            JSONObject dataJson = jsonObject.getJSONObject("data");
            Data data = JSONObject.toJavaObject(dataJson, Data.class);
            VideoToken videoToken = new VideoToken();
            videoToken.setId(1);
            videoToken.setAccesstoken(data.getAccessToken());
            if(code.equals("200")){
                historyDataService.updateAccessToken(videoToken);
            }
        }
    }

    @RequestMapping("/tomap")
    public String toMap(HttpServletRequest request){
        List<GateDevice> gatedeviceList = gateDeviceService.queryGateDeviceList();
        request.setAttribute("gatedevices",JSON.toJSON(gatedeviceList));
        System.out.println(JSON.toJSON(gatedeviceList));
        return "map";
    }
}
