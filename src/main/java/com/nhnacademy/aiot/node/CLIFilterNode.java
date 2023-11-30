package com.nhnacademy.aiot.node;

import java.util.Arrays;
import java.util.Iterator;
import com.nhnacademy.aiot.message.MQTTMessage;
import com.nhnacademy.aiot.message.Message;
import com.nhnacademy.aiot.wire.Wire;
import org.apache.commons.cli.CommandLine;
import org.json.JSONObject;

public final class CLIFilterNode extends InOutNode {

    private final CommandLine commandLine;

    public CLIFilterNode(String id, String name, CommandLine commandLine) {
        super(id, name);
        this.commandLine = commandLine;
    }

    @Override
    public void preprocess() {
        logger.debug("CLINode {} - preprocess()", getName());
    }

    @Override
    public void process() {
        for (int i = 0; i < getInWireCount(); i++) {
            Wire wire = getInWire(i);
            if (wire.hasMessage()) {
                Message message = wire.get();
                if (message instanceof MQTTMessage) {
                    MQTTMessage mqttMessage = (MQTTMessage) message;
                    sendFilteredMqttMessage(mqttMessage);
                }
            }
        }
    }

    private void sendFilteredMqttMessage(MQTTMessage mqttMessage) {
        JSONObject payload = mqttMessage.getPayload();
        if (payload.has("object") && isPermittedApplication(payload)) {
            JSONObject object = payload.getJSONObject("object");
            Iterator<String> sensors = getSensorIterator(object);

            Object time = payload.get("time");
            while (sensors.hasNext()) {
                String sensor = sensors.next();
                if (object.has(sensor)) {
                    JSONObject newPayload = new JSONObject()
                                            .put("time", time)
                                            .put(sensor, object.get(sensor));
                    output(new MQTTMessage(mqttMessage.getTopic() + "/s/" + sensor, newPayload));
                }
            }
        }
    }

    private boolean isPermittedApplication(JSONObject payload) {
        return !commandLine.hasOption("an")
            || commandLine.getOptionValue("an")
            .equals(payload.getJSONObject("deviceInfo").get("applicationId"));
    }

    private Iterator<String> getSensorIterator(JSONObject object) {
        if (commandLine.hasOption("s")) {
            return Arrays.stream(commandLine.getOptionValue("s").split(",")).iterator();
        }
        return object.keys();
    }

    @Override
    public void postprocess() {
        logger.debug("CLINode {} - postprocess()", getName());
    }
}
