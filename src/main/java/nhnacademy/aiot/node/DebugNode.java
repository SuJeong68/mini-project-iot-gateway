package nhnacademy.aiot.node;

import nhnacademy.aiot.message.MQTTMessage;
import nhnacademy.aiot.message.Message;
import nhnacademy.aiot.wire.Wire;

public final class DebugNode extends OutNode {

    public DebugNode(String name, int count) {
        super(name, count);
    }

    @Override
    public void preprocess() {
        logger.debug("DebugNode {} - preprocess()", getName());
    }

    @Override
    public void process() {
        for (int i = 0; i < getInWireCount(); i++) {
            Wire wire = getInWire(i);
            if (wire.hasMessage()) {
                Message message = wire.get();
                if (message instanceof MQTTMessage) {
                    MQTTMessage mqttMessage = (MQTTMessage) message;
                    logger.info("topic = {}, payload = {}", mqttMessage.getTopic(), mqttMessage.getPayload().toString());
                }
            }
        }
    }

    @Override
    public void postprocess() {
        logger.debug("DebugNode {} - postprocess()", getName());
    }
}
