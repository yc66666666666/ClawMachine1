package com.doll.controller;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/publish")
public class DataPublisher {

    @Autowired
    private MqttSender mqttSender;

    int count11=0;

    @GetMapping("/move/{action}")
    public void sendTemperatureUpdate(@PathVariable Integer action) {
        try {
            String topic = "/sys/k15kaQk0k1P/app_dev/thing/event/property/post";
//            String payload = "{\"id\":\"1\",\"version\":\"1.0\",\"params\":{\"controlDoll\":2},\"method\":\"thing.event.property.post\"}";
            String payload = String.format("{\"id\":\"1\",\"version\":\"1.0\",\"params\":{\"controlDoll\":%d},\"method\":\"thing.event.property.post\"}", action);
            System.out.println(payload);
            if (count11==0){
                mqttSender.connect();
                count11=1;
            }
            mqttSender.publish(topic, payload);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
