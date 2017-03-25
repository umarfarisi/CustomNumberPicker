package mamen.com.circlelistview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (LinearLayout) findViewById(R.id.layout);

        layout.setOnTouchListener(new JJBTNumberPicker(0,5,0));

    }

    public static class JJBTNumberPicker implements View.OnTouchListener{

        public static final int PREV_INDEX = 0;
        public static final int CURRENT_INDEX = 1;
        public static final int NEXT_INDEX = 2;

        public static final float DIFFERENT = 10;
        public static final float DEFAULT_CURRENT_Y = 0;
        public static final float DEFAULT_PREV_Y = -240;
        public static final float DEFAULT_NEXT_Y = 240;

        private float eventY;

        private TextView prev;
        private TextView current;
        private TextView next;

        private List<String> data;

        private int index;

        public JJBTNumberPicker(int from, int until, int startFromIndex) {
            eventY = 0;

            index = startFromIndex;

            data = new ArrayList<>();
            for(int datum = from ; datum <= until ; datum++)data.add(String.valueOf(datum));

        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if(event.getAction() == MotionEvent.ACTION_MOVE || event.getY() == MotionEvent.ACTION_BUTTON_RELEASE) {

                if(prev == null || current == null || next == null){

                    LinearLayout layout = (LinearLayout) v;

                    prev = (TextView) layout.getChildAt(PREV_INDEX);
                    current = (TextView) layout.getChildAt(CURRENT_INDEX);
                    next = (TextView) layout.getChildAt(NEXT_INDEX);

                    if(index==0)prev.setText(data.get(data.size()-1));
                    else prev.setText(data.get(index-1));
                    current.setText(data.get(index));
                    if(index==data.size()-1)next.setText(data.get(0));
                    else next.setText(data.get(index+1));
                }

                boolean isScrollUp = eventY - event.getY() > 0;

                if(isScrollUp){ // keatas
                    prev.setY(prev.getY() - DIFFERENT);
                    current.setY(current.getY() - DIFFERENT);
                    next.setY(next.getY() - DIFFERENT);

                    if(next.getY() == DEFAULT_CURRENT_Y){

                        prev.setY(DEFAULT_NEXT_Y);

                        prev.setText(data.get(index));

                        TextView temp = prev;
                        prev = current;
                        current = next;
                        next = temp;

                        index++;

                        if(index == data.size()){
                            index = 0;
                        }

                    }
                }else { //kebawah
                    next.setY(next.getY() + DIFFERENT);
                    current.setY(current.getY() + DIFFERENT);
                    prev.setY(prev.getY() + DIFFERENT);

                    if(prev.getY() == DEFAULT_CURRENT_Y){
                        next.setY(DEFAULT_PREV_Y);

                        next.setText(data.get(index));

                        TextView temp = current;
                        current = prev;
                        prev = next;
                        next = temp;

                        index--;

                        if(index == -1){
                            index = data.size()-1;
                        }

                    }
                }

                eventY = event.getY();

            }

            return true;
        }

        private void setScrollDown() {
            next.setY(next.getY() + DIFFERENT);
            current.setY(current.getY() + DIFFERENT);
            prev.setY(prev.getY() + DIFFERENT);

            if(prev.getY() == DEFAULT_CURRENT_Y){
                next.setY(DEFAULT_PREV_Y);

                TextView temp = current;
                current = prev;
                prev = next;
                next = temp;
            }
        }

        private void setScrollUp() {
            prev.setY(prev.getY() - DIFFERENT);
            current.setY(current.getY() - DIFFERENT);
            next.setY(next.getY() - DIFFERENT);

            if(next.getY() == DEFAULT_CURRENT_Y){
                prev.setY(DEFAULT_NEXT_Y);

                TextView temp = prev;
                prev = current;
                current = next;
                next = temp;
            }
        }
    }

}
