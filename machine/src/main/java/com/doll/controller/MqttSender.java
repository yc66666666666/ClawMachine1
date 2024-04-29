package com.doll.controller;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
//@Bean
public class MqttSender {

    private String brokerUrl="tcp://k15kaQk0k1P.iot-as-mqtt.cn-shanghai.aliyuncs.com:1883";


    private String clientId="k15kaQk0k1P.app_dev|securemode=2,signmethod=hmacsha256,timestamp=1713947416488|";


    private String username="app_dev&k15kaQk0k1P";


    private String password="b2075d52c53d782fe6cc003dc4356225f4ca41dae283af29704adacb988046e3";

    private MqttClient client;

    public void connect() throws MqttException {
        if (client == null) {
            client = new MqttClient(brokerUrl, clientId);
        }
        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        client.connect(options);
    }

    public void publish(String topic, String payload) throws MqttException {
        if (!client.isConnected()) {
            connect();
        }
        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setQos(1);
        client.publish(topic, message);
    }
}
