package com.doll.service;

import com.doll.common.R;
import com.doll.dto.CameraResponse;

public interface CameraService {
    public R<CameraResponse> getVideoUrlAndAccessToken(String ApplicationType, String deviceSerial, int channelNo, String videoType, String startTime, String endTime);
}
