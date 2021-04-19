package com.wgl.configuration;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CommandConfig implements Runnable{
    public enum CommandName{Agriculture_Control_light, Agriculture_Control_Motor};
    public enum Command{ON, OFF};
    private String X_Auth_Token;
    private URL url;
    private Command command;
    private CommandName commandName;
    private boolean isSuccess;

    public CommandConfig(URL url, CommandName commandName, Command command, String X_Auth_Token){
        this.url = url;
        this.commandName = commandName;
        this.command = command;
        this.X_Auth_Token = X_Auth_Token;
    }

    public void postCommand() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) this.url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("X-Auth-Token", X_Auth_Token);
        connection.setRequestProperty("Connection", "keep-alive");
        String requestBody =
                "{" +
                "\"service_id\":" + "\"Agriculture\"," +
                "\"command_name\":" + "\"" + commandName.toString() + "\"" +
                        "\"paras\":{ \"value\":\"" + command.toString() + "\"}}";
        OutputStream os = connection.getOutputStream();
        os.write(requestBody.getBytes());
        os.flush();
        os.close();

        connection.connect();

        int resultCode = connection.getResponseCode();
        if(resultCode == HttpURLConnection.HTTP_OK){
            isSuccess = true;
        }else{
            isSuccess = false;
        }
    }

    @Override
    public void run(){
        try {
            postCommand();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean checkSuccess(){
        return isSuccess;
    }
}
