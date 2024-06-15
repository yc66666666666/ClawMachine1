package com.doll.controller;

import com.doll.entity.Component;
import com.doll.service.impl.ControllerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/controller")
public class ControllerController {

//    @Autowired
//    private MqttSender mqttSender;

    @Autowired
    private ControllerServiceImpl controllerService;

    @Value("${mymqtt.brokerUrl}")
    private String brokerUrl ;

    @Value("${mymqtt.topic}")
    String topic;



    int count11=0;

    @PostMapping("/move/{action}")
    public void sendTemperatureUpdate(@PathVariable Integer action, @RequestBody Component component) {
        try {
            String topic = "/sys/k1fjo6CPtMr/app_dev_1/thing/event/property/post";

            String payload = String.format("{\"id\":\"1\",\"version\":\"1.0\",\"params\":{\"controlDoll\":%d},\"method\":\"thing.event.property.post\"}", action);
//            String payload = String.format("{\"id\":\"1\",\"params\":{\"control\":%d},\"version\":\"1.0\",:\"thing.event.property.post\"}", action);
//            {"id":1718179085647,"params":{"control":1},"version":"1.0","method":"thing.event.property.post"}
            System.out.println(payload);
            if (count11==0){
                controllerService.connect(component.getClientId(), component.getName(),component.getPasswd(),brokerUrl);
                count11=1;
            }
            controllerService.publish(topic, payload,component.getClientId(), component.getName(),component.getPasswd(),brokerUrl);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
