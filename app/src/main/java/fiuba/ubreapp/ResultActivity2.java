package fiuba.ubreapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;


public class ResultActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result2);

        TextView text1,text2,text3,text4,text5,text6,text7,text8,
                text9,text10,text11,text12,text13,text14,text15,text16;

        text1 = (TextView)findViewById(R.id.textView23);
        text2 = (TextView)findViewById(R.id.textView24);
        text3 = (TextView)findViewById(R.id.textView25);
        text4 = (TextView)findViewById(R.id.textView26);
        text5 = (TextView)findViewById(R.id.textView27);
        text6 = (TextView)findViewById(R.id.textView28);
        text7 = (TextView)findViewById(R.id.textView29);
        text8 = (TextView)findViewById(R.id.textView30);
        text9 = (TextView)findViewById(R.id.textView31);
        text10 = (TextView)findViewById(R.id.textView32);
        text11 = (TextView)findViewById(R.id.textView33);
        text12 = (TextView)findViewById(R.id.textView34);
        text13 = (TextView)findViewById(R.id.textView35);
        text14 = (TextView)findViewById(R.id.textView36);
        text15 = (TextView)findViewById(R.id.textView37);
        text16= (TextView)findViewById(R.id.textView38);

        Bundle bundle = getIntent().getExtras();
        String text=bundle.getString("User");
        Gson gson = new Gson();
        User user = gson.fromJson(text,User.class);

        text = "Name: " + user.getName();
        text1.setText(text);
        text = "LastName: " + user.getLastName();
        text2.setText(text);
        text = "Email: " + user.getEmail();
        text3.setText(text);
        text = "Password: " + user.getPassword();
        text4.setText(text);
        text = "Card Name: " + user.getNameCard();
        text5.setText(text);
        text = "Card Number: " + user.getNumberCard();
        text6.setText(text);
        text = "Expire Month: " + user.getExpireMonthCard();
        text7.setText(text);
        text = "Expire Year: " + user.getExpireYearCard();
        text8.setText(text);
        text = "Type Card: " + user.getTypeCard();
        text9.setText(text);
        text = "Model Car: " + user.getModelCar();
        text10.setText(text);
        text = "Colour: " + user.getColourCar();
        text11.setText(text);
        text = "Plate: " + user.getPlateCar();
        text12.setText(text);
        text = "Year Car: " + user.getYearCar();
        text13.setText(text);
        text = "State: " + user.getStateCar();
        text14.setText(text);
        text = "Music: " + user.getMusicCar();
        text15.setText(text);
        text = "Air Conditioner: " + user.getAirConditioner().toString();
        text16.setText(text);
    }
}
