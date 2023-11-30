package com.nhnacademy.aiot.node;

import com.nhnacademy.aiot.message.MQTTMessage;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONObject;

public final class MQTTInNode extends InNode {

    private final String serverURI;
    private final String topicFilter;

    private IMqttClient client;
    private MqttConnectOptions options;

    public MQTTInNode(String id, String name, String serverURI, String topicFilter) {
        super(id, name);
        this.serverURI = serverURI;
        this.topicFilter = topicFilter;
    }

    @Override
    public void preprocess() {
        logger.debug("MQTTInNode {} - preprocess()", getName());

        try {
            client = new MqttClient(serverURI, getId());

            options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(1000);
            options.setExecutorServiceTimeout(0);

            client.connect(options);

            client.subscribe(topicFilter, (topic, msg) -> output(new MQTTMessage(topic, new JSONObject(msg.toString()))));
        } catch (MqttException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void process() {
        try {
            if (!client.isConnected()) {
                client.connect(options);
            }
        } catch (MqttException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void postprocess() {
        logger.debug("MQTTInNode {} - postprocess()", getName());

        try {
            client.disconnect();
        } catch (MqttException e) {
            logger.error(e.getMessage());
        }
    }
}
