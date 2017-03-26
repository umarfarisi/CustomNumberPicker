package mamen.com.circlelistview;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class JJBTNumberPicker extends LinearLayout implements View.OnTouchListener{

    public static final float DIFFERENT = 1;
    public static final float DEFAULT_CURRENT_Y = 0;
    private static final float MINIMUM_VELOCITY = 10;

    private float oldY;

    private TextView prev;
    private TextView current;
    private TextView next;

    private List<String> data;

    private int indexOfCurrent;

    private boolean isTouchAgain;

    private VelocityTracker velocityTracker;

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

        prev = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.number,this,false);
        addView(prev);
        current = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.number,this,false);
        addView(current);
        next = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.number,this,false);
        addView(next);
    }

    public void setValue(int from, int until, int startFromIndex){
        indexOfCurrent = startFromIndex;

        for(int datum = from ; datum <= until ; datum++){
            if(datum < 10){
                data.add("0"+datum);
            }else {
                data.add(String.valueOf(datum));
            }
        }

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

            if(velocityTracker == null){
                velocityTracker = VelocityTracker.obtain();
            }else{
                velocityTracker.clear();
            }

            velocityTracker.addMovement(event);

            isTouchAgain = true;

        } else if(event.getAction() == MotionEvent.ACTION_MOVE) {

            velocityTracker.addMovement(event);

            float different = oldY - event.getY();

            if(different > 0.1){ // keatas itemnya
                setScrollUp(different);
            }else { //kebawah
                setScrollDown(-different);
            }

            oldY = event.getY();

        }else if(event.getAction() == MotionEvent.ACTION_UP){

            isTouchAgain = false;

            velocityTracker.computeCurrentVelocity(1000);

            final float velocity = Math.abs(velocityTracker.getYVelocity());

            if(velocity > MINIMUM_VELOCITY){
                if(velocityTracker.getYVelocity() > 0){
                    //kebawah
                    new AsyncTask<Void, Void, Void>(){

                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                float velocityFromBackgroundThread = velocity;
                                while (velocityFromBackgroundThread > 0 && !isTouchAgain) {
                                    velocityFromBackgroundThread -= MINIMUM_VELOCITY;
                                    publishProgress();
                                    Thread.sleep(1);
                                }
                            } catch (InterruptedException e) {

                            }
                            return null;
                        }

                        @Override
                        protected void onProgressUpdate(Void... values) {
                            if(!isTouchAgain)setScrollDown(DIFFERENT);
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            scrollToVisibleItem();
                        }

                    }.execute();

                }else{
                    //keatas
                    new AsyncTask<Void, Void, Void>(){

                        @Override
                        protected Void doInBackground(Void... params) {
                            try {
                                float velocityFromBackgroundThread = velocity;
                                while (velocityFromBackgroundThread > 0 && !isTouchAgain) {
                                    velocityFromBackgroundThread -= MINIMUM_VELOCITY;
                                    publishProgress();
                                    Thread.sleep(1);
                                }
                            } catch (InterruptedException e) {

                            }
                            return null;
                        }

                        @Override
                        protected void onProgressUpdate(Void... values) {
                            setScrollUp(DIFFERENT);
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            if(!isTouchAgain)scrollToVisibleItem();
                        }
                    }.execute();

                }
            }else{
                if(!isTouchAgain)scrollToVisibleItem();
            }

        } else if(event.getAction() == MotionEvent.ACTION_CANCEL){
            velocityTracker.clear();
        }

        return true;
    }

    private void scrollToVisibleItem() {
        Log.d("IMMMM","INN");
        if(current.getY() < DEFAULT_CURRENT_Y) {
            //current and next
            final float differentInCurrent = -current.getY();
            final float differentInNext = next.getY();
            if (differentInCurrent <= differentInNext) {
                //visible for current

                new AsyncTask<Void, Void, Void>(){

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            float differentInCurrentFromBackgroundThread = differentInCurrent;
                            while (differentInCurrentFromBackgroundThread > DEFAULT_CURRENT_Y) {
                                publishProgress();
                                differentInCurrentFromBackgroundThread -= DIFFERENT;
                                Thread.sleep(5);
                            }
                        } catch (InterruptedException e) {

                        }
                        return null;
                    }

                    @Override
                    protected void onProgressUpdate(Void... values) {
                        setScrollDown(DIFFERENT);
                    }
                }.execute();

            } else {
                //visible for next

                new AsyncTask<Void, Void, Void>(){

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            float differentInNextFromBackgroundThread = differentInNext;
                            while (differentInNextFromBackgroundThread > DEFAULT_CURRENT_Y) {
                                publishProgress();
                                differentInNextFromBackgroundThread -= DIFFERENT;
                                Thread.sleep(5);
                            }
                        } catch (InterruptedException e) {

                        }
                        return null;
                    }

                    @Override
                    protected void onProgressUpdate(Void... values) {
                        setScrollUp(DIFFERENT);
                    }
                }.execute();

            }
        }else if (current.getY() > DEFAULT_CURRENT_Y){
            //current and prev
            final float differentInCurrent = current.getY();
            final float differentInPrev = -prev.getY();
            if (differentInCurrent <= differentInPrev) {
                //visible for current
                new AsyncTask<Void, Void, Void>(){

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            float differentInCurrentFromBackgroundThread = differentInCurrent;
                            while (differentInCurrentFromBackgroundThread > DEFAULT_CURRENT_Y) {
                                publishProgress();
                                differentInCurrentFromBackgroundThread -= DIFFERENT;
                                Thread.sleep(5);
                            }
                        } catch (InterruptedException e) {

                        }
                        return null;
                    }

                    @Override
                    protected void onProgressUpdate(Void... values) {
                        setScrollUp(DIFFERENT);
                    }
                }.execute();

            } else {
                //visible for next

                new AsyncTask<Void, Void, Void>(){

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            float differentInPrevFromBackgroundThread = differentInPrev;
                            while (differentInPrevFromBackgroundThread > DEFAULT_CURRENT_Y) {
                                publishProgress();
                                differentInPrevFromBackgroundThread -= DIFFERENT;
                                Thread.sleep(5);
                            }
                        } catch (InterruptedException e) {

                        }
                        return null;
                    }

                    @Override
                    protected void onProgressUpdate(Void... values) {
                        setScrollDown(DIFFERENT);
                    }
                }.execute();

            }
        }
    }

    private void setScrollDown(float different) {
        next.setY(next.getY() + different);
        current.setY(current.getY() + different);
        prev.setY(prev.getY() + different);

        if(prev.getY() >= DEFAULT_CURRENT_Y){


            TextView temp = current;
            current = prev;
            prev = next;
            next = temp;

            prev.setY(current.getY()-prev.getHeight());

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

            TextView temp = prev;
            prev = current;
            current = next;
            next = temp;

            next.setY(current.getY()+current.getHeight());

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

    public int getNumber(){
        return Integer.valueOf(data.get(indexOfCurrent));
    }
}
