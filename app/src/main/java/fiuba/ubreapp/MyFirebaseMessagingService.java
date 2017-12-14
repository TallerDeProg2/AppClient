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
        Log.e(TAG, "Notification Received.");
        singleton = Singleton.getInstance();

        type = singleton.getType();

        str = remoteMessage.getNotification().getBody();
        str = str.substring(41);

        if(type.equals("passenger")){
            intent = new Intent(MyFirebaseMessagingService.this,MapDoTripActivity.class);
            intent.putExtra("User",singleton.getUser());
            intent.putExtra("Card",singleton.getCard());
            intent.putExtra("Type",type);
            intent.putExtra("idtrip",singleton.getIdtrip());
            intent.putExtra("URL",singleton.getUrl());
            intent.putExtra("Route",singleton.getRoute());
            intent.putExtra("OtherUser",str);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent = new Intent(MyFirebaseMessagingService.this,MapActivity.class);
            intent.putExtra("User",singleton.getUser());
            intent.putExtra("Card",singleton.getCard());
            intent.putExtra("Car",singleton.getCar());
            intent.putExtra("Type",type);
            intent.putExtra("URL",singleton.getUrl());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        startActivity(intent);

    }

}
