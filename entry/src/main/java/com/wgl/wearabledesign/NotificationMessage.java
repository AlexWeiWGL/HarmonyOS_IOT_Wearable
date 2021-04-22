package com.wgl.wearabledesign;

import ohos.aafwk.content.Intent;
import ohos.aafwk.content.Operation;
import ohos.app.Context;
import ohos.event.intentagent.IntentAgent;
import ohos.event.intentagent.IntentAgentConstant;
import ohos.event.intentagent.IntentAgentHelper;
import ohos.event.intentagent.IntentAgentInfo;
import ohos.event.notification.NotificationHelper;
import ohos.event.notification.NotificationRequest;
import ohos.event.notification.NotificationSlot;
import ohos.media.image.PixelMap;
import ohos.rpc.RemoteException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotificationMessage{
    private HashMap<String, Integer> data;
    private NotificationSlot notificationSlot;
    private NotificationRequest request;
    private String title;
    private String contentText;
    private PixelMap pixelMap;
    private Context context;
    private boolean isFirst;

    public NotificationMessage(HashMap<String, Integer> data, PixelMap pixelMap, Context context, boolean isFirst) {
        this.data = data;
        this.title = "Agriculture";
        this.pixelMap = pixelMap;
        this.context = context;
        this.isFirst = isFirst;
    }

    public void checkNotification(){
        if(data.get("Luminance") < 100 && !isFirst){
            contentText = "Light condition is Weak !";
            initNotificationSlot();
            publishNotification();
            initAgent();
            isFirst = true;
        }
    }

    private void initAgent(){
        Intent intent = new Intent();
        Operation operation = new Intent.OperationBuilder()
                .withDeviceId("")
                .withBundleName("com.wgl.wearabledesign")
                .withAbilityName("com.wgl.wearabledesign.MainAbility")
                .build();
        intent.setOperation(operation);

        List<Intent> intentList = new ArrayList<>();
        intentList.add(intent);

        IntentAgentInfo intentAgentInfo = new IntentAgentInfo(request.getNotificationId(), IntentAgentConstant.OperationType.START_ABILITY,
                IntentAgentConstant.Flags.UPDATE_PRESENT_FLAG, intentList, null);

        IntentAgent intentAgent = IntentAgentHelper.getIntentAgent(context, intentAgentInfo);
        request.setIntentAgent(intentAgent);
        request.setTapDismissed(true);

        try {
            NotificationHelper.publishNotification(request);
        } catch (RemoteException exception) {
            exception.printStackTrace();
        }
    }

    private void initNotificationSlot(){
        notificationSlot = new NotificationSlot("slot_001", "slot_default", NotificationSlot.LEVEL_DEFAULT);
        notificationSlot.setDescription("Light Condition is Weak!");
        try{
            NotificationHelper.addNotificationSlot(notificationSlot);
        } catch (RemoteException exception) {
            exception.printStackTrace();
        }
    }

    private void publishNotification(){
        request = new NotificationRequest(1);
        request.setSlotId(notificationSlot.getId());

        NotificationRequest.NotificationNormalContent content = new NotificationRequest.NotificationNormalContent();
        content.setTitle(title).setText(contentText);

        NotificationRequest.NotificationContent notificationContent = new NotificationRequest.NotificationContent(content);
        request.setLittleIcon(pixelMap);
        request.setContent(notificationContent);
    }

    private void cancelNotification(){
        try{
            NotificationHelper.cancelNotification(1);
        } catch (RemoteException exception) {
            exception.printStackTrace();
        }
    }

    public void setIsFirst(boolean isFirst){
        this.isFirst = isFirst;
    }

    public boolean getIsFirst(){
        return isFirst;
    }

    public void setData(HashMap<String, Integer> data){
        this.data = data;
    }
}
