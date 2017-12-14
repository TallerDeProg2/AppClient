package fiuba.ubreapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class InfoHistorialActivity extends AppCompatActivity {

    Bundle bundle;
    EditText text;
    String info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_historial);

        bundle = getIntent().getExtras();

        info = bundle.getString("Data");

        text = findViewById(R.id.editText30);

        text.setFocusable(false);

        text.setText(info);
    }
}
