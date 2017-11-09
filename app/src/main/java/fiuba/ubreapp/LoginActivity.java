package fiuba.ubreapp;

import android.app.ProgressDialog;
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
    private GetRestApi get;
    private Info url,loginjson,userjson;
    private UserLogIn userlogin;

    private String URL = "http://demo1144105.mockable.io";
    private String parameters;

    private User user;
    private Card card;
    private Car car;

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
        get = new GetRestApi();
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

                parameters = "/Card/";
                typeuser = "Card";

                userlogin = new UserLogIn("","",loginResult.getAccessToken().getUserId(),
                        loginResult.getAccessToken().getToken());

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
                        Log.i(TAG, "LogIn As " + typeuser + ".");
                        Log.i(TAG, userjson.getInfo());
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

        EditText username = (EditText) findViewById(R.id.editText);
        EditText password = (EditText) findViewById(R.id.editText2);

        String susername, spassword;
        Boolean busername, bpassword;

        if (v.getId() == R.id.button2) {

            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                    R.style.Theme_AppCompat_DayNight_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Authenticating...");
            progressDialog.show();

            susername = username.getText().toString();
            spassword = password.getText().toString();

            busername = susername.isEmpty();
            bpassword = spassword.isEmpty();

            if(!busername && !bpassword){
                intent = new Intent(LoginActivity.this, MapActivity.class);

                userlogin = new UserLogIn(susername,spassword,"","");
                loginjson.setInfo(gson.toJson(userlogin));

//                url.setInfo(URL + parameters);
//                try {
//                    post.execute(url,loginjson, userjson).get();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }

//                status = userjson.getStatus();

                status = 200;

                switch (status) {
                    case 200:
//                        user = gson.fromJson(userjson.getInfo(),User.class);
//                        typeuser = user.getType();

//                        try {
//                            //corregir
//                            post.execute(url,loginjson, userjson).get();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        } catch (ExecutionException e) {
//                            e.printStackTrace();
//                        }

//                        intent.putExtra(typeuser, user.getType());

                        String data = "{'username':'alanrinaldi','password':'1234','fb':{'userID':'rinaldia118','authToken':'12345'},'firstname':'Alan','lastname':'Rinaldi','country':'Argentina','email':'alan.rinaldi@live.com','birthdate':'30/01/1992','type':'Passenger','id':'1'}";
                        intent.putExtra("Type", "Passenger");
                        intent.putExtra("User", data);
//                        Log.i(TAG, "LogIn As " + typeuser + ".");
                        progressDialog.hide();
                        startActivity(intent);
                        break;
                    case 400:
                        break; //Incumplimiento de precondiciones (parámetros faltantes) o validación fallida
                    case 401:
                        break; //Unauthorized
                    case 500:
                        break; //Unexpected Error
                }
            } else {
                progressDialog.hide();
                if(busername){
                    username.setError("User can't be blank");
                } else {
                    password.setError("Password can't be blank");
                }
            }


        }

        if (v.getId() == R.id.textView){
            intent = new Intent(LoginActivity.this, RegisterActivity.class);
            Log.i(TAG,"Go to Register");
            startActivity(intent);
        }
    }

}