package com.doll.service.impl;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

@Service
public class ControllerServiceImpl {

    private MqttClient client;

    public void connect(String clientId,String name ,String password,String brokerUrl) throws MqttException {
        if (client == null) {
            client = new MqttClient(brokerUrl, clientId);
        }
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setUserName(name);
        options.setPassword(password.toCharArray());
        client.connect(options);
    }

    public void publish(String topic, String payload,String clientId,String name ,String password,String brokerUrl) throws MqttException {
        if (!client.isConnected()) {
            connect(clientId,name ,password, brokerUrl);
        }
        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setQos(1);
        client.publish(topic, message);
    }


}
