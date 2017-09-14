package fiuba.ubreapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        info = (TextView)findViewById(R.id.info);

        Bundle bundle = getIntent().getExtras();
        String text=bundle.getString("Result");

        info.setText(text);
    }
}
