package com.gong.mapper;

import com.gong.pojo.HistoryData;
import com.gong.pojo.VideoToken;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HistoryDataMapper {

    int addHistoryData(HistoryData historyData);

    int deleteHistoryData(int id);

    List<HistoryData> queryHistoryDataList(@Param("gdid") int gdid, @Param("startTime") String startTime, @Param("endTime") String endTime);

    HistoryData queryHistoryDataLast(int gdid);

    int updateAccessToken(VideoToken videoToken);

    VideoToken getVideoToken(int id);
}
