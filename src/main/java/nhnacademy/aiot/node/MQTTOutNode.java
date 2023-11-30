package nhnacademy.aiot.node;

import nhnacademy.aiot.message.MQTTMessage;
import nhnacademy.aiot.message.Message;
import nhnacademy.aiot.wire.Wire;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTOutNode extends OutNode {

    private final String serverURI;

    private IMqttClient client;

    public MQTTOutNode(String name, String serverURI, int count) {
        super(name, count);
        this.serverURI = serverURI;
    }

    @Override
    public void preprocess() {
        logger.debug("MQTTOutNode {} - preprocess()", getName());

        try {
            client = new MqttClient(serverURI, getUuid());

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
            options.setKeepAliveInterval(1000);
            options.setExecutorServiceTimeout(0);

            client.connect(options);
        } catch (MqttException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void process() {
        try {
            for (int i = 0; i < getInWireCount(); i++) {
                Wire wire = getInWire(i);
                if (wire.hasMessage()) {
                    Message message = wire.get();
                    if (message instanceof MQTTMessage) {
                        MQTTMessage mqttMessage = (MQTTMessage) message;
                        client.publish(mqttMessage.getTopic(), new MqttMessage(mqttMessage.getPayload().toString().getBytes()));
                    }
                }
            }
        } catch (MqttException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void postprocess() {
        logger.debug("MQTTOutNode {} - postprocess()", getName());

        try {
            client.disconnect();
        } catch (MqttException e) {
            logger.error(e.getMessage());
        }
    }
}
