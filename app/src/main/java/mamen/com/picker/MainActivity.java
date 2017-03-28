package mamen.com.picker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    CustomNumberPicker fromHourPicker;
    CustomNumberPicker fromMinutePicker;
    CustomNumberPicker untilHourPicker;
    CustomNumberPicker untilMinutePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fromHourPicker = (CustomNumberPicker) findViewById(R.id.fromHourPicker);
        fromMinutePicker = (CustomNumberPicker) findViewById(R.id.fromMinutePicker);
        untilHourPicker = (CustomNumberPicker) findViewById(R.id.untilHourPicker);
        untilMinutePicker = (CustomNumberPicker) findViewById(R.id.untilMinutePicker);

        fromHourPicker.setValue(0,24,0);
        fromMinutePicker.setValue(0,60,0);
        untilHourPicker.setValue(0,24,0);
        untilMinutePicker.setValue(0,60,0);

    }


    public void onClickShowTime(View view) {
        Toast.makeText(this,"From "+fromHourPicker.getNumber()+":"+fromMinutePicker.getNumber()+" , Until "+untilHourPicker.getNumber()+":"+untilMinutePicker.getNumber(),Toast.LENGTH_SHORT).show();
    }
}
