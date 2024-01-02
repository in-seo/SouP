package Matching.SouP.common;

import org.json.simple.JSONObject;

public class SoupResponse {

    public static JSONObject success() {
        return new ResponseJSONObject(true);
    }

    public static JSONObject fail() {
        return new ResponseJSONObject(false);
    }
}
