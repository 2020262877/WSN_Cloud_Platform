package com.gong.service.impl;

import com.gong.mapper.HistoryDataMapper;
import com.gong.pojo.HistoryData;
import com.gong.pojo.VideoToken;
import com.gong.service.HistoryDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryDataServiceImpl implements HistoryDataService {

    @Autowired
    private HistoryDataMapper historyDataMapper;

    public void setHistoryDataMapper(HistoryDataMapper historyDataMapper) {
        this.historyDataMapper = historyDataMapper;
    }

    @Override
    public int addHistoryData(HistoryData historyData) {
        return historyDataMapper.addHistoryData(historyData);
    }

    @Override
    public int deleteHistoryData(int id) {
        return historyDataMapper.deleteHistoryData(id);
    }

    @Override
    public List<HistoryData> queryHistoryDataList(int gdid, String startTime, String endTime) {
        return historyDataMapper.queryHistoryDataList(gdid,startTime,endTime);
    }

    @Override
    public HistoryData queryHistoryDataLast(int gdid) {
        return historyDataMapper.queryHistoryDataLast(gdid);
    }

    @Override
    public int updateAccessToken(VideoToken videoToken) {
        return historyDataMapper.updateAccessToken(videoToken);
    }

    @Override
    public VideoToken getVideoToken(int id) {
        return historyDataMapper.getVideoToken(id);
    }
}
