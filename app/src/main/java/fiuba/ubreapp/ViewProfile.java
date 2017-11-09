package fiuba.ubreapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

public class ViewProfile extends AppCompatActivity {

    private static final String TAG = "ViewProfile";

    private Bundle bundle;
    private User user;
    private String userjson;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        bundle = getIntent().getExtras();

        gson = new Gson();

        userjson = bundle.getString("User");
        user = gson.fromJson(userjson,User.class);

        EditText name = (EditText) findViewById(R.id.editText4);
        EditText lastname = (EditText) findViewById(R.id.editText5);
        EditText country = (EditText) findViewById(R.id.editText6);
        EditText email = (EditText) findViewById(R.id.editText24);
        EditText date = (EditText) findViewById(R.id.editText25);

        name.setText(user.getFirstname());
        lastname.setText(user.getLastName());
        country.setText(user.getCountry());
        email.setText(user.getEmail());
        date.setText(user.getBirthdate());

        name.setFocusable(false);
        lastname.setFocusable(false);
        country.setFocusable(false);
        email.setFocusable(false);
        date.setFocusable(false);
    }
}
