package nhnacademy.aiot.message;

import org.json.JSONObject;

public class MQTTMessage implements Message {

    private String topic;
    private JSONObject payload;

    public MQTTMessage(String topic, JSONObject payload) {
        this.topic = topic;
        this.payload = payload;
    }

    public String getTopic() {
        return topic;
    }

    public JSONObject getPayload() {
        return payload;
    }

    public void changeTopic(String topic) {
        this.topic = topic;
    }
}
