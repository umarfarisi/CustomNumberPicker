package mamen.com.circlelistview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (LinearLayout) findViewById(R.id.layout);

        layout.setOnTouchListener(new View.OnTouchListener() {

            final float DEFAULT_PREV_Y = -240;

            float eventY = 0;
            float prevYCounter = -240;
            float currentYCounter = 0;
            float nextYCounter = 240;
            View prev = null, current = null, next = null;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_MOVE || event.getY() == MotionEvent.ACTION_BUTTON_RELEASE) {

                    if (prev == null || current == null || next == null){
                        prev = layout.getChildAt(0);
                        current = layout.getChildAt(1);
                        next = layout.getChildAt(2);
                    }

                    float different = eventY - event.getY();

                    if(different > 0){ // keatas

                        prev.setY(prev.getY() - 10);
                        current.setY(current.getY() - 10);
                        next.setY(next.getY() - 10);

                        if(next.getY() == 0){
                            prev.setY(240);

                            View temp = prev;
                            current = next;
                            prev = current;
                            next = temp;
                        }

                    }else { //kebawah

                        next.setY(next.getY() + 10);
                        current.setY(current.getY() + 10);
                        prev.setY(prev.getY() + 10);

                        if(prev.getY() == 0){
                            next.setY(-240);

                            View temp = current;
                            current = prev;
                            prev = next;
                            next = temp;
                        }

                    }

                    eventY = event.getY();

                }

                return true;
            }
        });

    }

}
