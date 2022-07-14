package Matching.SouP.config;

public class MyOkHttpClient {
//    private static final String BASE_URL = "https://kauth.kakao.com";
//    private static final String APP_KEY = OauthConfig.getClientId();
//    private static final String REDIRECT_URI = "http://localhost:8080/oauth";
//    private static String template;
    private MyOkHttpClient() {}

//    public static String getAppKey() {
//        return APP_KEY;
//    }
//
//    public static String getAccessToken(String authorizationCode) throws IOException {
//        OkHttpClient client = new OkHttpClient();
//        HttpUrl urlWithParameters = makeHttpUrlWithParameters(authorizationCode);
//        Request request= makeRequest(urlWithParameters);
//        return client.newCall(request).execute().body().string();
//    }
//
//    public static boolean sendTalk(String token){
//        try{
//            OkHttpClient client = new OkHttpClient();
//            System.out.println(template);
//            String strURL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
//            String strBody = "template_object={\n" +
//                    "                    \"object_type\" : \"feed\",\n" +
//                    "                            \"content\" : {\n" +
//                    "                        \"title\":\"지원 템플릿\",\n" +
//                    "                                \"image_url\":\"https://user-images.githubusercontent.com/94730032/174986851-a0a8755a-9087-47a9-8722-cb47dcfa1352.jpg\",\n" +
//                    "                                \"link\" : { \"web_url\" : \"http://localhost:8080\" },\n" +
//                    "                        \"description\" : \""+template+"\"" +
//                    "                    },\n" +
//                    "                    \"item_content\" : {\n" +
//                    "                        \"profile_text\" :\"SouP 서포터\",\n" +
//                    "                                \"profile_image_url\" :\"https://mud-kage.kakao.com/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png\"\n" +
//                    "                    },\n" +
//                    "                    \"link\" : { \"web_url\" : \"www.naver.com\" },\n" +
//                    "                    \"buttons\": [\n" +
//                    "                    {\n" +
//                    "                        \"title\": \"맞춤 양식 보기\",\n" +
//                    "                            \"link\": {\n" +
//                    "                        \"web_url\": \"http://www.daum.net\",\n" +
//                    "                                \"mobile_web_url\": \"http://m.daum.net\"\n" +
//                    "                    }\n" +
//                    "                    }\n" +
//                    "                            ]\n" +
//                    "                }";
//            System.out.println(strBody);
//            RequestBody requestBody = RequestBody.create(MediaType.parse("text; charset=utf-8"),strBody);
//            Request.Builder builder = new Request.Builder().url(strURL).post(requestBody);
//            builder.addHeader("Content-type","application/x-www-form-urlencoded");
//            builder.addHeader("Authorization","Bearer "+ token);
//            Request request = builder.build();
//            Response response = client.newCall(request).execute();
//
//            if(response.isSuccessful()){
//                ResponseBody body = response.body();
//                body.close();
//                log.info("전송 성공!");
//                return true;
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public static String makeTemplate(Source source, String link, String stack, String email, String talk){
//        String start = "안녕하세요 "+ source +"의 "+link+ "  보고 연락 드렸습니다.";
//        String middle = "모집중이신 "+stack+"을 이용한 프로젝트/스터디에 관심이 있고 [           ] 정도 다뤄봤으며, [          ]와 같은 구현 경험이 있습니다.";
//        String end = "자세한 내용은 ['깃허브주소']를 참고해 주시거나 "+email+" 로 연락주세요. 카톡도 가능합니다 :) !! 지원 오픈카톡 --> "+ talk;
//        template = start+middle+end;
//        return template;
//    }
//
//    private static Request makeRequest(HttpUrl url) {
//        return new Request.Builder()
//                .url(url)
//                .build();
//    }
//
//    private static HttpUrl makeHttpUrlWithParameters(String authorizationCode) throws MalformedURLException {
//        HttpUrl.Builder httpBuilder = HttpUrl
//                .get(new URL(BASE_URL + "/oauth/token"))
//                .newBuilder();
//        httpBuilder.addQueryParameter("grant_type", "authorization_code");
//        httpBuilder.addQueryParameter("client_id", APP_KEY);
//        httpBuilder.addQueryParameter("redirect_uri", REDIRECT_URI);
//        httpBuilder.addQueryParameter("code", authorizationCode);
//        return httpBuilder.build();
//    }

}