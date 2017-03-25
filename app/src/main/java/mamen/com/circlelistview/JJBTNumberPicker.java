package mamen.com.circlelistview;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class JJBTNumberPicker extends LinearLayout implements View.OnTouchListener{

    public static final float DIFFERENT = 1;
    public static final float DEFAULT_CURRENT_Y = 0;
    public static final float DEFAULT_PREV_Y = -240;
    public static final float DEFAULT_NEXT_Y = 240;

    private float oldY;

    private TextView prev;
    private TextView current;
    private TextView next;

    private List<String> data;

    private int indexOfCurrent;

    public JJBTNumberPicker(Context context) {
        super(context);
        setUpDefaultValue();
    }

    public JJBTNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpDefaultValue();
    }

    public JJBTNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setUpDefaultValue();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public JJBTNumberPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setUpDefaultValue();
    }


    private void setUpDefaultValue() {
        oldY = 0;

        indexOfCurrent = 0;
        data = new ArrayList<>();

        prev = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item,this,false);
        addView(prev);
        current = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item,this,false);
        addView(current);
        next = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item,this,false);
        addView(next);
    }

    public void setValue(int from, int until, int startFromIndex){
        indexOfCurrent = startFromIndex;

        for(int datum = from ; datum <= until ; datum++)data.add(String.valueOf(datum));

        if(indexOfCurrent==0)prev.setText(data.get(data.size()-1));
        else prev.setText(data.get(indexOfCurrent-1));

        current.setText(data.get(indexOfCurrent));

        if(indexOfCurrent==data.size()-1)next.setText(data.get(0));
        else next.setText(data.get(indexOfCurrent+1));

        setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            //user first touch
            oldY = event.getY();
        } else if(event.getAction() == MotionEvent.ACTION_MOVE) {

            float different = oldY - event.getY();

            if(different > 0.1){ // keatas itemnya
                setScrollUp(different);
            }else { //kebawah
                setScrollDown(-different);
            }

            oldY = event.getY();

        }else if(event.getAction() == MotionEvent.ACTION_UP){

            if(current.getY() < DEFAULT_CURRENT_Y) {
                //current and next
                float differentInCurrent = -current.getY();
                float differentInNext = next.getY();
                if (differentInCurrent <= differentInNext) {
                    //visible for current
                    while (differentInCurrent > DEFAULT_CURRENT_Y) {
                        setScrollDown(DIFFERENT);
                        differentInCurrent -= DIFFERENT;
                    }
                } else {
                    //visible for next
                    while (differentInNext > DEFAULT_CURRENT_Y) {
                        setScrollUp(DIFFERENT);
                        differentInNext -= DIFFERENT;
                    }
                }
            }else if (current.getY() > DEFAULT_CURRENT_Y){
                //current and next
                float differentInCurrent = current.getY();
                float differentInPrev = -prev.getY();
                if (differentInCurrent <= differentInPrev) {
                    //visible for current
                    while (differentInCurrent > DEFAULT_CURRENT_Y) {
                        setScrollUp(DIFFERENT);
                        differentInCurrent -= DIFFERENT;
                    }
                } else {
                    //visible for next
                    while (differentInPrev > DEFAULT_CURRENT_Y) {
                        setScrollDown(DIFFERENT);
                        differentInPrev -= DIFFERENT;
                    }
                }
            }
        }

        return true;
    }

    private void setScrollDown(float different) {
        next.setY(next.getY() + different);
        current.setY(current.getY() + different);
        prev.setY(prev.getY() + different);

        if(prev.getY() >= DEFAULT_CURRENT_Y){
            next.setY(DEFAULT_PREV_Y);

            TextView temp = current;
            current = prev;
            prev = next;
            next = temp;

            indexOfCurrent--;

            if(indexOfCurrent == -1){
                indexOfCurrent = data.size()-1;
            }

            if(indexOfCurrent-1 == -1){
                prev.setText(data.get(data.size()-1));
            }else {
                prev.setText(data.get(indexOfCurrent-1));
            }

        }
    }

    private void setScrollUp(float different) {
        prev.setY(prev.getY() - different);
        current.setY(current.getY() - different);
        next.setY(next.getY() - different);

        if(next.getY() <= DEFAULT_CURRENT_Y){

            prev.setY(DEFAULT_NEXT_Y);

            TextView temp = prev;
            prev = current;
            current = next;
            next = temp;

            indexOfCurrent++;

            if(indexOfCurrent == data.size()){
                indexOfCurrent = 0;
            }

            if(indexOfCurrent+1 == data.size()){
                next.setText(data.get(0));
            }else{
                next.setText(data.get(indexOfCurrent+1));
            }

        }
    }
}
