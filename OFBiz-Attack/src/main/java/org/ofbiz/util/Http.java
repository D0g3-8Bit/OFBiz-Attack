package org.ofbiz.util;

import okhttp3.*;

import javax.net.ssl.*;
import java.net.ConnectException;
import java.net.SocketException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class Http {
    private static final OkHttpClient client = getUnsafeOkHttpClient(); // 获取信任所有证书的OkHttpClient

    /**
     * 创建一个信任所有证书的OkHttpClient
     */
    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // 创建一个信任所有证书的TrustManager
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            // 创建一个使用信任所有证书的SSLContext
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // 获取使用信任所有证书的SSLSocketFactory
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            // 创建一个OkHttpClient，并设置使用信任所有证书的SSLSocketFactory
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);
            return builder.build();

        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    public static Response sendHttpsGetRequest(String url){
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            return response;

        }catch (ConnectException e){
            System.err.println("Connect Error");
            return null;
        }
        catch (SocketException e){
            System.err.println("Timeout Error");
            return null;
        }
        catch (Exception e1) {
            System.err.println("Connect Error");
            return null;
        }
    }

    public static Response sendHttpsPostRequest(String url, String body, String ContentType) {
        try {
            RequestBody requestBody = RequestBody.create(MediaType.parse(ContentType), body);
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            return client.newCall(request).execute();

        } catch (ConnectException e) {
            System.err.println("Connect Error");
            return null;
        } catch (SocketException e) {
            System.err.println("Timeout Error");
            return null;
        } catch (Exception e1) {
            System.err.println("Connect Error");
            return null;
        }
    }
}
