package io.github.lucasduete.eventbus.cepapp;

import org.json.JSONObject;

public class Evento {

    private JSONObject jsonObject;

    public Evento(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Evento{");
        sb.append("jsonObject=").append(jsonObject.toString());
        sb.append('}');
        return sb.toString();
    }
}
