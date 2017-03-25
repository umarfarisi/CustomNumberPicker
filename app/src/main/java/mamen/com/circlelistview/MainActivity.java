package mamen.com.circlelistview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (LinearLayout) findViewById(R.id.layout);

        layout.setOnTouchListener(new JJBTNumberPicker(0,24));

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

        private View prev;
        private View current;
        private View next;

        private List<Integer> data;

        public JJBTNumberPicker(int from, int until) {
            eventY = 0;
            prev = null;
            current = null;
            next = null;
            data = new ArrayList<>();
            for(int datum = from ; datum <= until ; datum++)data.add(datum);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if(event.getAction() == MotionEvent.ACTION_MOVE || event.getY() == MotionEvent.ACTION_BUTTON_RELEASE) {

                LinearLayout layout = (LinearLayout) v;

                if (prev == null || current == null || next == null){
                    prev = layout.getChildAt(PREV_INDEX);
                    current = layout.getChildAt(CURRENT_INDEX);
                    next = layout.getChildAt(NEXT_INDEX);
                }

                boolean isScrollUp = eventY - event.getY() > 0;

                if(isScrollUp){ // keatas
                    prev.setY(prev.getY() - DIFFERENT);
                    current.setY(current.getY() - DIFFERENT);
                    next.setY(next.getY() - DIFFERENT);

                    if(next.getY() == DEFAULT_CURRENT_Y){
                        prev.setY(DEFAULT_NEXT_Y);

                        View temp = prev;
                        prev = current;
                        current = next;
                        next = temp;
                    }
                }else { //kebawah
                    next.setY(next.getY() + DIFFERENT);
                    current.setY(current.getY() + DIFFERENT);
                    prev.setY(prev.getY() + DIFFERENT);

                    if(prev.getY() == DEFAULT_CURRENT_Y){
                        next.setY(DEFAULT_PREV_Y);

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

        private void setScrollDown() {
            next.setY(next.getY() + DIFFERENT);
            current.setY(current.getY() + DIFFERENT);
            prev.setY(prev.getY() + DIFFERENT);

            if(prev.getY() == DEFAULT_CURRENT_Y){
                next.setY(DEFAULT_PREV_Y);

                View temp = current;
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

                View temp = prev;
                prev = current;
                current = next;
                next = temp;
            }
        }
    }

}
