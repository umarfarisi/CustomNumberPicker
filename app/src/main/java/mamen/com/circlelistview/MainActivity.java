package mamen.com.circlelistview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    JJBTNumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        numberPicker = (JJBTNumberPicker) findViewById(R.id.numberPicker);
        numberPicker.setValue(0,24,0);

    }


}
