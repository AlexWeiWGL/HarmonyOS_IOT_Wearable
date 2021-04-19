package com.wgl.configuration;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CommandConfig implements Runnable{
    public enum CommandName{Agriculture_Control_light, Agriculture_Control_Motor};
    public enum Command{ON, OFF};
    public enum CommandType{Light, Motor};
    private String X_Auth_Token;
    private URL url;
    private Command command;
    private CommandName commandName;
    private boolean isSuccess;
    private CommandType commandType;

    public CommandConfig(URL url, CommandName commandName, CommandType commandType, String X_Auth_Token){
        this.url = url;
        this.commandName = commandName;
        this.X_Auth_Token = X_Auth_Token;
        this.commandType = commandType;
        isSuccess = false;
    }

    public void postCommand() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) this.url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("X-Auth-Token", X_Auth_Token);
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Content-Type", "text/plain");
        String requestBody =
                "{" +
                "\"service_id\":" + "\"Agriculture\"," +
                "\"command_name\":" + "\"" + commandName.toString() + "\"," +
                        "\"paras\":{\""+commandType.toString()+"\":\"" + command.toString() + "\"}}";

        connection.setRequestProperty("Content-Length", requestBody.getBytes().length+"");

        OutputStream os = connection.getOutputStream();
        os.write(requestBody.getBytes());
        os.flush();
        os.close();

        connection.connect();

        int resultCode = connection.getResponseCode();
        System.out.println(resultCode);
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

    public synchronized void setCommand(Command command){
        this.command = command;
    }
}
