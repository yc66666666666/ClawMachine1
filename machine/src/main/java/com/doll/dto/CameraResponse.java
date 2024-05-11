package com.doll.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CameraResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private String accessToken;

    private String url;


}
