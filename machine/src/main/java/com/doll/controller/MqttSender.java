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

//    private String brokerUrl="tcp://k15kaQk0k1P.iot-as-mqtt.cn-shanghai.aliyuncs.com:1883";
//
//
//    private String clientId="k15kaQk0k1P.app_dev|securemode=2,signmethod=hmacsha256,timestamp=1713947416488|";
//
//
//    private String username="app_dev&k15kaQk0k1P";
//
//
//    private String password="b2075d52c53d782fe6cc003dc4356225f4ca41dae283af29704adacb988046e3";

    //k1fjo6CPtMr为ProductKey
//    private String brokerUrl="tcp://k1fjo6CPtMr.iot-as-mqtt.cn-shanghai.aliyuncs.com:1883";



//    private String brokerUrl="tcp://iot-06z00htcl0whfr4.mqtt.iothub.aliyuncs.com:1883";
//
//    //MQTT连接参数里面的clientId
//    private String clientId="k1fjo6CPtMr.app_dev_1|securemode=2,signmethod=hmacsha256,timestamp=1718181222523|";
//
//    //MQTT连接参数里面的username
//    private String username="app_dev_1&k1fjo6CPtMr";
//
//    //MQTT连接参数里面的passwd
//    private String password="c03994e75c583441f10cc6d1c09176e553c7bbec06394cae6258f3aef3eee865";



//    //MQTT连接参数里面的clientId
//    private String clientId="k1fjo6CPtMr.app_dev_1|securemode=2,signmethod=hmacsha256,timestamp=1718181222523|";
//
//    //MQTT连接参数里面的username
//    private String username="app_dev_1&k1fjo6CPtMr";
//
//    //MQTT连接参数里面的passwd
//    private String password="c03994e75c583441f10cc6d1c09176e553c7bbec06394cae6258f3aef3eee865";

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
