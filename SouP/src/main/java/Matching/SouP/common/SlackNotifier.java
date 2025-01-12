package Matching.SouP.common;

import Matching.SouP.service.PropertyUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;


@Slf4j
public class SlackNotifier {
    private static final OkHttpClient client = new OkHttpClient();

    public void sendMessageToSlack(String errorMessage) {
        String webHookURL = PropertyUtil.getProperty("webhook.url");
        String message = "OKKY 파싱 에러";

        RequestBody body = RequestBody.create(
            MediaType.parse("application/json; charset=utf-8"),
            "{\"text\":\"" + message + " " + errorMessage + "\"}"
        );

        Request request = new Request.Builder()
            .url(webHookURL)
            .post(body)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("Unexpected code " + response);
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
    }
}
