package com.doll.controller;

import com.alibaba.druid.util.StringUtils;
import com.doll.entity.Component;
import com.doll.service.ComponentService;
import com.doll.service.impl.ControllerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/controller")
public class ControllerController {

//    @Autowired
//    private MqttSender mqttSender;

    @Autowired
    private ControllerServiceImpl controllerService;

    @Autowired
    private ComponentService componentService;

    @Value("${mymqtt.brokerUrl}")
    private String brokerUrl ;

//    @Value("${mymqtt.topic}")
//    String topic;



    @Autowired
    private RedisTemplate redisTemplate;

    int count11=0;

    @PostMapping("/move/{action}")
    public void sendTemperatureUpdate(@PathVariable Integer action,Long clawMachineControllerId) {


        Component component=(Component) redisTemplate.opsForValue().get("tomovie"+clawMachineControllerId);
        String topic =  String.format("/sys/k1fjo6CPtMr/%s/thing/event/property/post",component.getName().split("&")[0]);

        if (Objects.isNull(component)){
            component=componentService.getById(clawMachineControllerId);
            redisTemplate.opsForValue().set("tomovie"+clawMachineControllerId,component);
        }

        try {
//            String topic = "/sys/k1fjo6CPtMr/app_dev_3/thing/event/property/post";
//            String topic =  String.format("/sys/k1fjo6CPtMr/%s/thing/event/property/post",component.getName().split("&")[0]);
            System.out.println(topic);
            String payload = String.format("{\"id\":\"1\",\"version\":\"1.0\",\"params\":{\"controlDoll\":%d},\"method\":\"thing.event.property.post\"}", action);
//            String payload = String.format("{\"id\":\"1\",\"params\":{\"control\":%d},\"version\":\"1.0\",:\"thing.event.property.post\"}", action);
//            {"id":1718179085647,"params":{"control":1},"version":"1.0","method":"thing.event.property.post"}
            System.out.println(payload);
//            if (count11==0){
                controllerService.connect(component.getClientId(), component.getName(),component.getPasswd(),brokerUrl);
//                count11=1;
//            }
            controllerService.publish(topic, payload,component.getClientId(), component.getName(),component.getPasswd(),brokerUrl);

            if (action==5){   //这个是新填的
                String topic1="/sys/k1fjo6CPtMr/app_dev_1/thing/service/property/set";
                controllerService.subscribe(topic1,component.getClientId(), component.getName(),component.getPasswd(),brokerUrl);
            }


        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

   @PostMapping("/disconnect")
    public void disconnect(Long clawMachineControllerId){
       Component component=(Component) redisTemplate.opsForValue().get("tomovie"+clawMachineControllerId);
       try {
           controllerService.disconnect(component.getName());
           System.out.println("断开连接");
       } catch (MqttException e) {
           throw new RuntimeException(e);
       }
   }



}
