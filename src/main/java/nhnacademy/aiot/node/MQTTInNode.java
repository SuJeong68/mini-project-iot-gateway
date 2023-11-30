package nhnacademy.aiot.node;

import java.util.Optional;
import nhnacademy.aiot.message.MQTTMessage;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.json.JSONObject;

public final class MQTTInNode extends InNode {

    private final String serverURI;
    private final String topicFilter;

    private IMqttClient client;
    private MqttConnectOptions options;

    public MQTTInNode(String name, String serverURI, String topicFilter, int count) {
        super(name, count);
        this.serverURI = serverURI;
        this.topicFilter = topicFilter;
    }

    @Override
    public void preprocess() {
        logger.debug("MQTTInNode {} - preprocess()", getName());

        try {
            client = new MqttClient(serverURI, getUuid());

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
