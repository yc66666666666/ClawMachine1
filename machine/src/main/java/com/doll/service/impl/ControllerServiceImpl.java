package com.doll.service.impl;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ControllerServiceImpl {

//    private MqttClient client;

    private Map<String, MqttClient> clientMap=new HashMap<>();

    public void connect(String clientId,String name ,String password,String brokerUrl) throws MqttException {
        MqttClient client1=clientMap.get(name);
        if (client1 == null) {
            client1 = new MqttClient(brokerUrl, clientId,new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setUserName(name);
            options.setPassword(password.toCharArray());
            client1.connect(options);
            clientMap.put(name,client1);
        }

    }

    public void publish(String topic, String payload,String clientId,String name ,String password,String brokerUrl) throws MqttException {
        if (!clientMap.get(name).isConnected() || clientMap.get(name) == null) {
            connect(clientId,name ,password, brokerUrl);
        }
        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setQos(1);
        clientMap.get(name).publish(topic, message);
//        client.close();
    }


    //接受来自设备的信号
    public void subscribe(String topic1 ,String clientId,String name ,String password,String brokerUrl) throws MqttException {
        if (!clientMap.get(name).isConnected() || clientMap.get(name) == null) {
            connect(clientId,name ,password, brokerUrl);
        }

        clientMap.get(name).subscribe(
                topic1, (receivedTopic, message) -> {
                    System.out.println(String.format("1111111111111111111111topic:{},message:{}",receivedTopic,new String(message.getPayload())));
                }
        );

    }






    public String disconnect(String name) throws MqttException {
        MqttClient client1=clientMap.get(name);
        if (client1!=null){
            client1.disconnect();
            clientMap.remove(name);
        }
        return "断开成功";
    }




}
