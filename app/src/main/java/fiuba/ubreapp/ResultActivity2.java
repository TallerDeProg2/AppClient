package fiuba.ubreapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;


public class ResultActivity2 extends AppCompatActivity {
    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        info = (TextView)findViewById(R.id.info);

        Bundle bundle = getIntent().getExtras();
        String text=bundle.getString("User");
        Gson gson = new Gson();
        User user = gson.fromJson(text,User.class);

        text = "Name: " + user.getName() + " LastName: " + user.getLastName() + " Email: " + user.getEmail() +
                " Password: " + user.getPassword();

        info.setText(text);
    }
}
