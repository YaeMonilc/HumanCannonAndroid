package cc.kocho.humancannonandroid.util;

import okhttp3.*;
import okhttp3.internal.platform.Platform;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class HttpRequest {

    public static String get(String url, String pattern) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(2000, TimeUnit.MILLISECONDS)
                .readTimeout(2000, TimeUnit.MILLISECONDS)
                .writeTimeout(2000, TimeUnit.MILLISECONDS)
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(),Platform.get().platformTrustManager())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .build();
        url += "?" + pattern;
        return Objects.requireNonNull(okHttpClient.newCall(new Request.Builder().url(url).get()
                .addHeader("User-Agent", "UnityPlayer/2017.4.30f1 (UnityWebRequest/1.0, libcurl/7.51.0-DEV)")
                .addHeader("X-Unity-Version", "2017.4.30f1").build()).execute().body()).string();
    }

    public static String post(String url, RequestBody pattern) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(2000, TimeUnit.MILLISECONDS)
                .readTimeout(2000, TimeUnit.MILLISECONDS)
                .writeTimeout(2000, TimeUnit.MILLISECONDS)
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory(),Platform.get().platformTrustManager())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .build();
        return Objects.requireNonNull(okHttpClient.newCall(new Request.Builder().url(url).post(pattern)
                .addHeader("User-Agent", "UnityPlayer/2017.4.30f1 (UnityWebRequest/1.0, libcurl/7.51.0-DEV)")
                .addHeader("X-Unity-Version", "2017.4.30f1")
                .build()).execute().body()).string();
    }



}
