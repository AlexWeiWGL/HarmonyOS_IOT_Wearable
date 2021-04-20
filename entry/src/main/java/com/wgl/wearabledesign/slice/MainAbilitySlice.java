package com.wgl.wearabledesign.slice;

import com.wgl.configuration.CommandConfig;
import com.wgl.configuration.HttpConfig;
import com.wgl.wearabledesign.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.AbsButton;
import ohos.agp.components.Switch;
import ohos.agp.components.Text;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.eventhandler.InnerEvent;
import ohos.global.resource.NotExistException;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.Size;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MainAbilitySlice extends AbilitySlice {
    private HttpConfig config;
    private static final int HTTP_UPDATE = 1;
    private Timer timer;
    private TimerTask timerTask;
    private CommandConfig lightCommand;
    private CommandConfig motorCommand;

    private EventHandler eventHandler = new EventHandler(EventRunner.current()){
        protected void processEvent(InnerEvent event){
            switch (event.eventId){
                case HTTP_UPDATE:
                    getUITaskDispatcher().delayDispatch(new Runnable() {
                        @Override
                        public void run() {
                            initView(config.getData());
                        }
                    }, 0);
            }
        }
    };

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        init();
    }

    @Override
    public void onActive() {
        super.onActive();
        updateHttpData();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    public void init(){
        try {
            URL url = new URL("https://iotda.cn-north-4.myhuaweicloud.com/v5/iot/0c1907218a80f2952fa4c015c7ae499c/devices/607abeccba4b2702c056fe37_BearPiDemo/shadow");
            URL commandURL = new URL("https://iotda.cn-north-4.myhuaweicloud.com/v5/iot/0c1907218a80f2952fa4c015c7ae499c/devices/607abeccba4b2702c056fe37_BearPiDemo/commands");
            String X_Auth_Token = "MIIVWAYJKoZIhvcNAQcCoIIVSTCCFUUCAQExDTALBglghkgBZQMEAgEwghNqBgkqhkiG9w0BBwGgghNbBIITV3sidG9rZW4iOnsiZXhwaXJlc19hdCI6IjIwMjEtMDQtMjFUMDI6MTg6MzUuMDkwMDAwWiIsIm1ldGhvZHMiOlsicGFzc3dvcmQiXSwiY2F0YWxvZyI6W10sInJvbGVzIjpbeyJuYW1lIjoidGVfYWRtaW4iLCJpZCI6IjAifSx7Im5hbWUiOiJ0ZV9hZ2VuY3kiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9jc2UyLjBfY2ZnIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZGNzX21zX3J3cyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2Vjc19zcG90X2luc3RhbmNlIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfaXZhc192Y3JfdmNhIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZWNzX2FzY2VuZF9rYWkxIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZWNzX2thZTEiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9kYnNfcmkiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9ldnNfZXNzZCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2Vjc19ncHVfdjEwMCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2NzZTIuMF9ndm4iLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9kd3NfcG9jIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfY2JyX2ZpbGUiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9lY3Nfa2MxX3VzZXJfZGVmaW5lZCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX21lZXRpbmdfZW5kcG9pbnRfYnV5IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfbWFwX25scCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3Npc19zYXNyX2VuIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfc2FkX2JldGEiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9zZXJ2aWNlc3RhZ2VfbWdyX2R0bV9lbiIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX1ZJU19JbnRsIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZWNzX2dwdV9wMnMiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9ldnNfdm9sdW1lX3JlY3ljbGVfYmluIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZGNzX2RjczItZW50ZXJwcmlzZSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2NzZTIuMCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3ZjcCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2N2ciIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2Vjc19jNm5lIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfYXBwY3ViZSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX211bHRpX2JpbmQiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9vcF9nYXRlZF9pb3RzdGFnZSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2VpcF9wb29sIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfcm9tYWV4Y2hhbmdlIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfYV9hcC1zb3V0aGVhc3QtM2QiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9wcm9qZWN0X2RlbCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3NoYXJlQmFuZHdpZHRoX3FvcyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2NjaV9vY2VhbiIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2V2c19yZXR5cGUiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9vbmVhY2Nlc3MiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9lY3NfaXIzeCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2VsYl9ndWFyYW50ZWVkIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfYV9jbi1zb3V0aHdlc3QtMmIiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9jaWUiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9zZnN0dXJibyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3ZwY19uYXQiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF92cG5fdmd3X2ludGwiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9odl92ZW5kb3IiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9hX2NuLW5vcnRoLTRlIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfYV9jbi1ub3J0aC00ZCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2Vjc19ncHVfZzZ2IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfSUVDIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZGF5dV9kbG1fY2x1c3RlciIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2ludGxfY29uZmlndXJhdGlvbiIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2FfY24tbm9ydGgtNGYiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9jcGgiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9tdGQiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9lY3NfZ3B1X2c1ciIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3drc19rcCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2NjaV9rdW5wZW5nIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfcmlfZHdzIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfYV9jbi1zb3V0aHdlc3QtMmQiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF92cGNfZmxvd19sb2ciLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9hYWRfYmV0YV9pZGMiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9jc2JzX3JlcF9hY2NlbGVyYXRpb24iLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9kc3NfbW9udGgiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9jc2ciLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9kZWNfbW9udGhfdXNlciIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2llZl9lZGdlYXV0b25vbXkiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF92aXBfYmFuZHdpZHRoIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfb3NjIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZWNzX29sZF9yZW91cmNlIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfd2VsaW5rYnJpZGdlX2VuZHBvaW50X2J1eSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2Rjc19yaSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2llZi1pbnRsIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfcHN0bl9lbmRwb2ludF9idXkiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9tYXBfb2NyIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZGx2X29wZW5fYmV0YSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2llcyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX29ic19kdWFsc3RhY2siLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9lZGNtIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfY3Nic19yZXN0b3JlIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfaXZzY3MiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9lY3NfYzZhIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfdnBuX3ZndyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3Ntbl9jYWxsbm90aWZ5IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZG1zX3JlbGlhYmlsaXR5IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfcGNhIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfY2NlX2FzbV9oayIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2NzYnNfcHJvZ3Jlc3NiYXIiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9ldnNfcG9vbF9jYSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2JjZSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2Vjc19vZmZsaW5lX2Rpc2tfNCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2VwcyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2NzYnNfcmVzdG9yZV9hbGwiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9sMmNnIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfaW50bF92cGNfbmF0IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZmNzX3BheSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2wyY2dfaW50bCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2FfYXAtc291dGhlYXN0LTFlIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfYV9ydS1tb3Njb3ctMWIiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9hX2FwLXNvdXRoZWFzdC0xZCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2FfYXAtc291dGhlYXN0LTFmIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZWNzX3ZncHVfZzUiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9vcF9nYXRlZF9tZXNzYWdlb3ZlcjVnIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfdGljc19vcGVuX2JldGEiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9tYXBfdmlzaW9uIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZWNzX3JpIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfYV9hcC1zb3V0aGVhc3QtMWMiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9hX3J1LW5vcnRod2VzdC0yYyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2llZl9wbGF0aW51bSIsImlkIjoiMCJ9XSwicHJvamVjdCI6eyJkb21haW4iOnsibmFtZSI6Imh3Mjg5ODQxMzIiLCJpZCI6IjA4YWM2NzU3NWE4MGYzODgwZjY0YzAxNTJlNmE1YzgwIn0sIm5hbWUiOiJjbi1ub3J0aC00IiwiaWQiOiIwYzE5MDcyMThhODBmMjk1MmZhNGMwMTVjN2FlNDk5YyJ9LCJpc3N1ZWRfYXQiOiIyMDIxLTA0LTIwVDAyOjE4OjM1LjA5MDAwMFoiLCJ1c2VyIjp7ImRvbWFpbiI6eyJuYW1lIjoiaHcyODk4NDEzMiIsImlkIjoiMDhhYzY3NTc1YTgwZjM4ODBmNjRjMDE1MmU2YTVjODAifSwibmFtZSI6Imh3Mjg5ODQxMzIiLCJwYXNzd29yZF9leHBpcmVzX2F0IjoiIiwiaWQiOiIwOGFjNjc1ODQyODAwZjQxMWY3ZmMwMTUxZDJhYWY3NSJ9fX0xggHBMIIBvQIBATCBlzCBiTELMAkGA1UEBhMCQ04xEjAQBgNVBAgMCUd1YW5nRG9uZzERMA8GA1UEBwwIU2hlblpoZW4xLjAsBgNVBAoMJUh1YXdlaSBTb2Z0d2FyZSBUZWNobm9sb2dpZXMgQ28uLCBMdGQxDjAMBgNVBAsMBUNsb3VkMRMwEQYDVQQDDApjYS5pYW0ucGtpAgkA3LMrXRBhahAwCwYJYIZIAWUDBAIBMA0GCSqGSIb3DQEBAQUABIIBAGb6QrwAbW9sctrjqUsduPF8TbA3gsPCA9bCE8V-GiFnEtGGynDaFG+LQkjkKZJntjDQ14bgvGWO1LUX9RXHddMeCRJJVlLawo60uOHK2BnOVmDSsUALwO+jtXNBeyZrFxR9HuedvnqKdtMbBZxHvBvYoGs17-qklMR3-xk6GnNSnrvWdTnoqqhD6tQ-Hye5JJOtnNH8vnaBCHbKg76fKTLGROOmI9QApUA33AAHcVCf1LFhaljrfcfJ7NUEbhUo7LqRxx4q+cxCiL5ElIBfNtZNyhhFDOwNx1tsKvbKS2YjgSawVmINySInOykllX36spYAML+6oCy6BpRus0YcRkc=";
            config = new HttpConfig(url, X_Auth_Token, getPixelMapFromResource(ResourceTable.Media_icon), getContext());
            lightCommand = new CommandConfig(commandURL, CommandConfig.CommandName.Agriculture_Control_light, CommandConfig.CommandType.Light, X_Auth_Token);
            motorCommand = new CommandConfig(commandURL, CommandConfig.CommandName.Agriculture_Control_Motor, CommandConfig.CommandType.Motor, X_Auth_Token);

            System.out.println("Enter connect");
            Thread httpConfigThread = new Thread(config);
            httpConfigThread.start();
            httpConfigThread.join(5000);

            //data = httpConfig.getData();
            initView(config.getData());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void initView(HashMap<String, Integer> data){
        Text temperature = (Text) findComponentById(ResourceTable.Id_temperature);
        Text humidity = (Text) findComponentById(ResourceTable.Id_humidity);
        Text luminance = (Text) findComponentById(ResourceTable.Id_luminance);

        temperature.setText(data.get("Temperature").toString());
        humidity.setText(data.get("Humidity").toString());
        luminance.setText(data.get("Luminance").toString());

        Switch lightSwitch = (Switch) findComponentById(ResourceTable.Id_lightSwitch);
        Switch motorSwitch = (Switch) findComponentById(ResourceTable.Id_motorSwitch);

        lightSwitch.setCheckedStateChangedListener(new AbsButton.CheckedStateChangedListener() {
            @Override
            public void onCheckedChanged(AbsButton absButton, boolean b) {
                if(absButton.isChecked()){
                    System.out.println("LightCheck");
                    lightCommand.setCommand(CommandConfig.Command.ON);
                    Thread lightCommandThread = new Thread(lightCommand);
                    lightCommandThread.start();
                    try {
                        lightCommandThread.join(5000);

                        lightSwitch.setChecked(true);
                        absButton.setChecked(true);
                        System.out.println("LightOnSuccess!");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else if (!absButton.isChecked()){
                    System.out.println("LightOFFCheck");
                    lightCommand.setCommand(CommandConfig.Command.OFF);
                    Thread lightCommandThread = new Thread(lightCommand);
                    lightCommandThread.start();
                    try {
                        lightCommandThread.join(5000);
                        lightSwitch.setChecked(false);
                        absButton.setChecked(false);
                        System.out.println("LightOFFSuccess!");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        motorSwitch.setCheckedStateChangedListener(new AbsButton.CheckedStateChangedListener() {
            @Override
            public void onCheckedChanged(AbsButton absButton, boolean b) {
                if(absButton.isChecked()){
                    System.out.println("MotorCheck");
                    motorCommand.setCommand(CommandConfig.Command.ON);
                    Thread motorCommandThread = new Thread(motorCommand);
                    motorCommandThread.start();
                    try {
                        motorCommandThread.join(5000);
                        motorSwitch.setChecked(true);
                        absButton.setChecked(true);
                        System.out.println("MotorOnSuccess!");

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else if(!absButton.isChecked()){
                    System.out.println("MotorCheck");
                    motorCommand.setCommand(CommandConfig.Command.OFF);
                    Thread motorCommandThread = new Thread(motorCommand);
                    motorCommandThread.start();
                    try {
                        motorCommandThread.join(5000);

                        motorSwitch.setChecked(false);
                        absButton.setChecked(false);
                        System.out.println("MotorOffSuccess");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void updateHttpData(){
        if(timer == null && timerTask == null){
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    Thread configThread = new Thread(config);
                    configThread.start();
                    try {
                        configThread.join(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    eventHandler.sendEvent(HTTP_UPDATE);
                    System.out.println("Update!");
                }
            };
            timer.schedule(timerTask, 0, 1000);
        }
    }

    private PixelMap getPixelMapFromResource(int resourceId){
        InputStream inputStream = null;
        try{
            inputStream = getContext().getResourceManager().getResource(resourceId);
            ImageSource.SourceOptions srcOpts = new ImageSource.SourceOptions();
            srcOpts.formatHint = "image/png";
            ImageSource imageSource = ImageSource.create(inputStream, srcOpts);

            ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
            decodingOptions.desiredSize = new Size(57, 57);

            return imageSource.createPixelmap(decodingOptions);
        } catch (NotExistException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
