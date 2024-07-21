//package com.liuyang1.luascript;
//
//import com.google.gson.Gson;
//import com.xiaojukeji.davinci.nlp.didilog.utils.HttpUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.util.DigestUtils;
//
//import java.util.HashMap;
//
//@SpringBootTest
//@Slf4j
//public class CustomHttpAuthApiTest {
//    private final Gson gson = new Gson();
//
//    @Test
//    public void testApi() {
//        String url = "http://10.169.166.81:8001/musicTsp/message/new";
//
//        HashMap<String, String> header = new HashMap<>();
//        final String myAppKey = "E2I3F1E2T1E2D5O8J0A3S6N5O6F0P5E4", myAppSecret = "K2R9V4L7Z3D1S4G7H5B7P1Q1T6Y5I9C2";
//        String timestamp = String.valueOf(System.currentTimeMillis());
//        header.put("timestamp", timestamp);
//        header.put("appKey", myAppKey);
//        header.put("version", "1.0");
//
//        String signStr = "timestamp" + timestamp + "version" + "1.0" + myAppSecret;
//        String sign = DigestUtils.md5DigestAsHex(signStr.getBytes());
//        log.info("sign: {}", sign);
//
//        header.put("sign", sign);
//        header.put("env", "stable");
//
//        HashMap<String, String> messageBody = new HashMap<>();
//        messageBody.put("ddmqTopicName", "iov-cloud-music-player-topic");
//        messageBody.put("messageBody", "{\"songId\":1111}");
//
//        String response = HttpUtils.post(url, null, header, gson.toJson(messageBody));
//        log.info("response: {}", response);
//    }
//}
