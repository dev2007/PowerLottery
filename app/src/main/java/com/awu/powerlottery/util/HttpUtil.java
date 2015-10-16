package com.awu.powerlottery.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Http request util.
 * Created by awu on 2015-10-14.
 */
public class HttpUtil {
    /**
     * request time-out millisecond.
     */
    private static final int TIMEOUT = 8 * 1000;
    /**
     * request method GET.
     */
    private static final String GET_METHOD = "GET";

    /**
     * send one request and get the response.
     * @param urlAddress Web link address.
     * @param listener Request callback listener.The request result will be handle in it.
     */
    public static void sendHttpRequest(final String urlAddress,final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL(urlAddress);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod(GET_METHOD);
                    connection.setConnectTimeout(TIMEOUT);
                    connection.setReadTimeout(TIMEOUT);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        response.append(line);
                    }

                    if(listener != null){
                        listener.onFinish(response.toString());
                    }
                }catch (Exception e){
                    if(listener != null){
                        listener.onError(e);
                    }
                }finally {
                    if(connection != null)
                        connection.disconnect();
                }
            }
        }).start();
    }

    /**
     * Http request handle listener.
     */
    public interface  HttpCallbackListener{
        /**
         * request finish.
         * @param response The server response.
         */
        void onFinish(String response);

        /**
         * request exception.
         * @param e The exception object.
         */
        void onError(Exception e);
    }
}
