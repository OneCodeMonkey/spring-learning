package com.liuyang1.spring_learning.dynamic_rate_limit.services;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class HttpService {
    private final Gson gson = new Gson();

    public String get(String url) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .header("Content-Type", "application/json")
                .url(url)
                .build();

        String responseBody = "";
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                responseBody = response.body().string();
                log.info("HttpService Response: {} | code={}", responseBody, response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseBody;
    }

    public String post(String url, Map<String, Object> params) {
        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String json = gson.toJson(params);
        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        String responseBody = "";
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                responseBody = response.body().string();
                log.info("HttpService Response: {} | code={}", responseBody, response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseBody;
    }

    public String post(String url, Map<String, String> headers, Map<String, Object> params) {
        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String json = gson.toJson(params);
        RequestBody body = RequestBody.create(json, JSON);

        Headers headers1 = Headers.of(headers).newBuilder().build();

        Request request = new Request.Builder()
                .headers(headers1)
                .url(url)
                .post(body)
                .build();

        String responseBody = "";
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                responseBody = response.body().string();
                log.info("HttpService Response: {} | code={}", responseBody, response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseBody;
    }

    public static String staticGet(String url) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .header("Content-Type", "application/json")
                .url(url)
                .build();

        String responseBody = "";
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                responseBody = response.body().string();
                log.info("HttpService Response: {} | code={}", responseBody, response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseBody;
    }

    public static String staticPost(String url, Map<String, Object> params) {
        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String json = new Gson().toJson(params);
        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        String responseBody = "";
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                responseBody = response.body().string();
                log.info("HttpService Response: {} | code={}", responseBody, response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseBody;
    }
}
