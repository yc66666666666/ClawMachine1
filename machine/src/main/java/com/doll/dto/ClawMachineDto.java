package com.doll.dto;

import com.doll.entity.ClawMachine;
import lombok.Data;

@Data
public class ClawMachineDto extends ClawMachine {
    private String sensorName;
    private String controllerName;
    private String cameraName;
    private String commodityName;
    private Integer copies;
}
