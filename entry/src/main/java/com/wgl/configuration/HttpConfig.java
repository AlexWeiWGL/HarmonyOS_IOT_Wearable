package com.wgl.configuration;

import net.sf.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class HttpConfig implements Runnable{

    private final String X_AUTH_TOKEN;
    private final URL url;
    private HashMap<String, Integer> data;


    public HttpConfig(URL url, String X_AUTH_TOKEN){

        this.X_AUTH_TOKEN = X_AUTH_TOKEN;

        this.url = url;
        data = new HashMap<>();
    }

    private void doConnect() throws IOException {
        //headerMap.put("X-Auth-Token", X_AUTH_TOKEN);
        HttpURLConnection connection = (HttpURLConnection) this.url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Auth-Token", X_AUTH_TOKEN);
        connection.setRequestProperty("Connection", "keep-alive");

        connection.connect();

        int resultCode = connection.getResponseCode();
        if(resultCode == HttpURLConnection.HTTP_OK){
            String response = getStringFromInputStream(connection.getInputStream());
            JSONObject jsonObject = JSONObject.fromObject(response);
            data = MessageAnalyzer.getData(jsonObject);
        }else{
            doConnect();
        }
    }

    @Override
    public void run(){
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized HashMap<String, Integer> getData(){
        return data;
    }

    private String getStringFromInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len = -1;
        while((len = inputStream.read(buffer)) != -1){
            baos.write(buffer, 0, len);
        }
        inputStream.close();
        String output = baos.toString();
        baos.close();
        return output;
    }
}
