package com.gong.service;

import com.gong.pojo.HistoryData;
import com.gong.pojo.VideoToken;

import java.util.List;

public interface HistoryDataService {

    int addHistoryData(HistoryData historyData);

    int deleteHistoryData(int id);

    List<HistoryData> queryHistoryDataList(int gdid, String startTime, String endTime);

    HistoryData queryHistoryDataLast(int gdid);

    int updateAccessToken(VideoToken videoToken);

    VideoToken getVideoToken(int id);
}
