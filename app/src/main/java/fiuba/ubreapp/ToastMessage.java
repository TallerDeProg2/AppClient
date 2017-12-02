package fiuba.ubreapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

/**
 * Created by alan on 20/11/17.
 */

class ToastMessage {

    private Context context;
    private int duration;
    private Toast toast;
    private Gson gson;
    private ErrorResponse er;

    ToastMessage(Context context){
        this.context = context;
        this.duration = Toast.LENGTH_SHORT;
        this.gson = new Gson();
        this.er = new ErrorResponse();
    }

    public void show(String message){
        er = gson.fromJson(message,ErrorResponse.class);
        toast = Toast.makeText(context, er.getMessage(), duration);
        toast.show();
    }
}
