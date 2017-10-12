package fiuba.ubreapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

/**Screen para loguearse o realizar un registro nuevo.*/
public class LoginActivity extends AppCompatActivity implements OnClickListener{

    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private static final String TAG = "LoginActivity";
    private Gson gson = new Gson();
    private String typeuser;

    private Intent intent;

    private PostRestApi post;
    private Info url,loginjson,userjson;
    private UserLogIn userlogin;

    private String URL = "http://demo1144105.mockable.io";
    private String parameters;

    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button acceptButton = (Button) findViewById(R.id.button2);
        acceptButton.setOnClickListener(this);

        TextView textView = (TextView) findViewById(R.id.textView);

        textView.setOnClickListener(this);

        post = new PostRestApi();
        url = new Info();
        loginjson = new Info();
        userjson = new Info();

        callbackManager = CallbackManager.Factory.create();

        addLoginButton();
    }

    //Agrego boton Log In de Facebook
    private void addLoginButton() {
        loginButton = (LoginButton) this.findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("email", "user_friends",
                "public_profile", "user_birthday"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            private ProfileTracker mProfileTracker;

            //Success Log In Facebook
            @Override
            public void onSuccess(LoginResult loginResult) {
                intent = new Intent(LoginActivity.this, MapActivity.class);

//                if (!ischecked) {
//                    parameters = "/Passenger/";
//                    typeuser = "Passenger";
//                } else {
//                    parameters = "/Driver/";
//                    typeuser = "Driver";
//                }

                parameters = "/Passenger/";
                typeuser = "Passenger";

                userlogin = new UserLogIn(loginResult.getAccessToken().getUserId()
                        ,"",loginResult.getAccessToken().getToken());

                loginjson.setInfo(gson.toJson(userlogin));

                url.setInfo(URL + parameters);
                try {
                    post.execute(url,loginjson, userjson).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                status = userjson.getStatus();

                switch (status) {
                    case 200:
                        intent.putExtra("AsDriver", false);
                        intent.putExtra(typeuser, userjson.getInfo());
                        Log.i(TAG, "LogIn As " + typeuser + " .");
                        startActivity(intent);
                        break;
                    case 400:
                        break; //Incumplimiento de precondiciones (parámetros faltantes) o validación fallida
                    case 401:
                        break; //Unauthorized
                    case 500:
                        break; //Unexpected Error
                }

                Log.i(TAG,"Success Log In");

                startActivity(intent);
            }

            //Cancel Log In Facebook
            @Override
            public void onCancel() {
                intent = new Intent(LoginActivity.this, LoginActivity.class);
                Log.i(TAG,"Success Cancel Log In");
                startActivity(intent);
            }

            //Error Log In Facebook
            @Override
            public void onError(FacebookException error) {

                intent = new Intent(LoginActivity.this, LoginActivity.class);
                Log.e(TAG,"Error");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //Acciones al cliquear boton de Log In o registro de nuevo usuario
    @Override
    public void onClick (View v) {

        EditText user = (EditText) findViewById(R.id.editText);
        EditText password = (EditText) findViewById(R.id.editText2);
        CheckBox checkbox = (CheckBox) findViewById(R.id.checkBox);

        Boolean ischecked = checkbox.isChecked();

        if (v.getId() == R.id.button2) {

            intent = new Intent(LoginActivity.this, MapActivity.class);

            if (!ischecked) {
                parameters = "/Passenger/";
                typeuser = "Passenger";
            } else {
                parameters = "/Driver/";
                typeuser = "Driver";
            }

            userlogin = new UserLogIn(user.getText().toString(),password.getText().toString(),"");
            loginjson.setInfo(gson.toJson(userlogin));

            url.setInfo(URL + parameters);
            try {
                post.execute(url,loginjson, userjson).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            status = userjson.getStatus();

            switch (status) {
                case 200:
                    intent.putExtra("AsDriver", ischecked);
                    intent.putExtra(typeuser, userjson.getInfo());
                    Log.i(TAG, "LogIn As " + typeuser + " .");
                    startActivity(intent);
                    break;
                case 400:
                    break; //Incumplimiento de precondiciones (parámetros faltantes) o validación fallida
                case 401:
                    break; //Unauthorized
                case 500:
                    break; //Unexpected Error
            }
        }

        if (v.getId() == R.id.textView){
            intent = new Intent(LoginActivity.this, RegisterActivity.class);
            Log.i(TAG,"Go to Register");
            startActivity(intent);
        }
    }

}