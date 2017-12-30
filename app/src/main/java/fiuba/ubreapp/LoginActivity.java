package fiuba.ubreapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
    private Info url,loginjson,userjson,tokeninfo,answerinfo;
    private UserLogIn userlogin;

    private String URL = "https://ubre-app.herokuapp.com";
    private String endpoint;

    private String cardjson,carjson;

    private User user;
    private Card card;
    private Car car;

    ProgressDialog progressDialog;

    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseMessaging.getInstance().subscribeToTopic("MiAplicacion");
        setContentView(R.layout.activity_login);

        Button acceptButton = findViewById(R.id.button2);
        acceptButton.setOnClickListener(this);

        TextView textView = findViewById(R.id.textView);

        textView.setOnClickListener(this);

        post = new PostRestApi();
        get = new GetRestApi();
        url = new Info();
        loginjson = new Info();
        userjson = new Info();
        tokeninfo = new Info();
        answerinfo = new Info();
        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
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
                String token,extradata;

                intent = new Intent(LoginActivity.this, MapActivity.class);

                userlogin = new UserLogIn("","",loginResult.getAccessToken().getUserId(),
                        loginResult.getAccessToken().getToken());

                loginjson.setInfo(gson.toJson(userlogin));

                url.setInfo(URL + endpoint);

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
                        user = gson.fromJson(userjson.getInfo(),User.class);
                        typeuser = user.getType();

                        url.setInfo(URL+user.getId());
                        token = user.getToken();
                        tokeninfo.setInfo(token);

//                        obtainData(url,answerinfo);

                        intent.putExtra("Type", typeuser);

                        intent.putExtra("User", userjson.getInfo());
                        intent.putExtra("URL",URL);
//                        if(typeuser.equals("Passenger"))
//                            intent.putExtra("Card",extradata);
//                        else
//                            intent.putExtra("Car",extradata);
                        Log.i(TAG, "LogIn As " + typeuser + ".");
                        progressDialog.dismiss();
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

        EditText username = findViewById(R.id.editText);
        EditText password = findViewById(R.id.editText2);

        String susername, spassword, extradata,token;
        Boolean busername, bpassword;

        if (v.getId() == R.id.button2) {

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

                endpoint = "/validate";

                url.setInfo(URL + endpoint);
                try {
                    post.execute(url,loginjson, userjson).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                status = userjson.getStatus();

                switch (status) {
                    case 201:
                        user = gson.fromJson(userjson.getInfo(),User.class);
                        typeuser = user.getType();

                        if(typeuser.equals("passenger"))
                            endpoint = "/passengers/"+user.getId()+"/card";
                        else
                            endpoint = "/drivers/"+user.getId()+"/card";

                        intent.putExtra("Type", typeuser);
                        intent.putExtra("User", userjson.getInfo());
                        intent.putExtra("URL",URL);

                        requestCard(URL+endpoint,user.getToken(),user.getId());

                        Log.i(TAG, "LogIn As " + typeuser + ".");
                        progressDialog.hide();
                        startActivity(intent);
                        break;
                    case 400:
                        break; //Incumplimiento de precondiciones (parámetros faltantes) o validación fallida
                    case 403:
                        password.setError("Incorrect password");
                        break; //Unauthorized
                    case 404:
                        username.setError("Non-existent user");
                        break;
                    case 500:
                        break; //Unexpected Error
                    default:
                        break;
                }
                progressDialog.dismiss();
            } else {
                progressDialog.dismiss();
                if(busername){
                    username.setError("User can't be blank");
                } else {
                    password.setError("Password can't be blank");
                }
            }


        }

        if (v.getId() == R.id.textView){
            intent = new Intent(LoginActivity.this, RegisterActivity.class);
            intent.putExtra("URL",URL);
            Log.i(TAG,"Go to Register");
            startActivity(intent);
        }
    }

    private void requestCard(String url, final String token, final String id){
        final Info info = new Info();
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        switch (info.getStatus()){
                            case 200:
                                intent.putExtra("Card",info.getInfo());
                                if(typeuser.equals("passenger")){
                                    Log.i(TAG, "LogIn As " + typeuser + ".");
                                    progressDialog.hide();
                                    startActivity(intent);
                                } else {
                                    requestCar(URL,token,id);
                                }
                                break;
                            case 400:
                                Log.i(TAG,"Error Card: " + String.valueOf(info.getStatus()));
                                break; //Incumplimiento de precondiciones (parámetros faltantes) o validación fallida
                            case 403:
                                Log.i(TAG,"Error Card: " + String.valueOf(info.getStatus()));
                                break; //Unauthorized
                            case 404:
                                Log.i(TAG,"Error Card: " + String.valueOf(info.getStatus()));
                                break;
                            case 500:
                                Log.i(TAG,"Error Card: " + String.valueOf(info.getStatus()));
                                break; //Unexpected Error
                            default:
                                Log.i(TAG,"Error Card: " + String.valueOf(info.getStatus()));
                                break;
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.i(TAG,error.getMessage());

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("token", token);

                return params;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                info.setStatus(response.statusCode);
                info.setInfo(new String(response.data));
                return super.parseNetworkResponse(response);
            }

        };
        queue.add(jsObjRequest);
    }

    private void requestCar(String url, final String token,final String id){
        final Info info = new Info();
        String endpoint = "/drivers/"+id+"/cars";
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url+endpoint, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        switch (info.getStatus()){
                            case 200:
                            intent.putExtra("Car",info.getInfo());
                                Log.i(TAG, "LogIn As " + typeuser + ".");
                                progressDialog.hide();
                                startActivity(intent);
                                break;
                            case 400:
                                Log.i(TAG,"Error Car: " + String.valueOf(info.getStatus()));
                                break; //Incumplimiento de precondiciones (parámetros faltantes) o validación fallida
                            case 403:
                                Log.i(TAG,"Error Car: " + String.valueOf(info.getStatus()));
                                break; //Unauthorized
                            case 404:
                                Log.i(TAG,"Error Car: " + String.valueOf(info.getStatus()));
                                break;
                            case 500:
                                Log.i(TAG,"Error Car: " + String.valueOf(info.getStatus()));
                                break; //Unexpected Error
                            default:
                                Log.i(TAG,"Error Car: " + String.valueOf(info.getStatus()));
                                break;
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.i(TAG,error.getMessage());

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("token", token);

                return params;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                info.setStatus(response.statusCode);
                info.setInfo(new String(response.data));
                return super.parseNetworkResponse(response);
            }

        };
        queue.add(jsObjRequest);
    }



}