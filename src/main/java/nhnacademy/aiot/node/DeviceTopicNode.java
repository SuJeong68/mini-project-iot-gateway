package nhnacademy.aiot.node;

import nhnacademy.aiot.exception.NotDeviceException;
import nhnacademy.aiot.message.MQTTMessage;
import nhnacademy.aiot.message.Message;
import nhnacademy.aiot.wire.Wire;
import org.json.JSONObject;

public final class DeviceTopicNode extends InOutNode {

    public DeviceTopicNode(String name, int inCount, int outCount) {
        super(name, inCount, outCount);
    }

    @Override
    public void preprocess() {
        logger.debug("DeviceTopicNode {} - preprocess()", getName());
    }

    @Override
    public void process() {
        for (int i = 0; i < getInWireCount(); i++) {
            Wire wire = getInWire(i);
            if (wire.hasMessage()) {
                Message message = wire.get();
                if (message instanceof MQTTMessage) {
                    MQTTMessage mqttMessage = (MQTTMessage) message;
                    JSONObject payload = mqttMessage.getPayload();
                    try {
                        mqttMessage.changeTopic(getDeviceTopic(payload));
                        output(mqttMessage);
                    } catch (NotDeviceException e) {
                        //
                    }
                }
            }
        }
    }

    private String getDeviceTopic(JSONObject payload) {
        if (payload.has("deviceInfo")) {
            JSONObject deviceInfo = payload.getJSONObject("deviceInfo");
            if (deviceInfo.has("devEui") && deviceInfo.has("tags")) {
                JSONObject tags = deviceInfo.getJSONObject("tags");
                if (tags.has("branch") && tags.has("place")) {
                    return String.format("data/b/%s/p/%s/d/%s", tags.getString("branch"), tags.getString("place"), deviceInfo.getString("devEui"));
                }
            }
        }
        throw new NotDeviceException();
    }

    @Override
    public void postprocess() {
        logger.debug("DeviceTopicNode {} - postprocess()", getName());
    }
}
