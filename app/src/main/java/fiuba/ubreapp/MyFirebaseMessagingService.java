package fiuba.ubreapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    Intent intent;
    Context context;
    Singleton singleton;
    String type;
    String str;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        singleton = Singleton.getInstance();

        type = singleton.getType();

        Log.e(TAG, "Notification Received.");
        Log.e(TAG, remoteMessage.getNotification().getBody());
        Log.e(TAG, "Type: "+ type);

        if(type.equals("passenger")){
            str = remoteMessage.getNotification().getBody();
            str = str.substring(41);
            intent = new Intent(MyFirebaseMessagingService.this,MapDoTripActivity.class);
            intent.putExtra("idtrip",singleton.getIdtrip());
            intent.putExtra("Route",singleton.getRoute());
            intent.putExtra("OtherUser",str);

        } else {
            intent = new Intent(MyFirebaseMessagingService.this,MapActivity.class);
            intent.putExtra("Car",singleton.getCar());
        }

        intent.putExtra("User",singleton.getUser());
        intent.putExtra("Card",singleton.getCard());
        intent.putExtra("Type",type);
        intent.putExtra("URL",singleton.getUrl());


        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

}
