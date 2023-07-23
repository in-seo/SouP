package Matching.SouP.common;

import org.json.simple.JSONObject;

public class ResponseJSONObject extends JSONObject {
    private static final String success = "success";

    public ResponseJSONObject(boolean bl) {
        super();
        put(success, bl);
    }
}
