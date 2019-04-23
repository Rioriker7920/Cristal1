package com.rioriker.myapplication;


import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private String code;

    private static String  TAG = "TimerDemo";

    private TextView mTextView = null;
    private Button mButton_start = null;
    private Button mButton_pause = null;

    private Timer mTimer = null;
    private TimerTask mTimerTask = null;

    private Handler mHandler = null;

    private static int count = 0;
    private boolean isPause = false;
    private boolean isStop = true;

    private static int delay = 1;  //1s
    private static int period = 1;  //1s

    private static final int UPDATE_TEXTVIEW = 0;


    Button b1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.mytextview);
        mButton_start = findViewById(R.id.mybutton_start);
        mButton_pause = findViewById(R.id.mybutton_pause);


        mButton_start.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (isStop) {
                    Log.i(TAG, "开始");
                } else {
                    Log.i(TAG, "停止");
                }

                isStop = !isStop;

                if (!isStop) {
                    startTimer();
                }else {
                    stopTimer();
                }

                if (isStop) {
                    mButton_start.setText("开始");
                } else {
                    mButton_start.setText("停止");
                }
            }

        });

        mButton_pause.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (isPause) {
                    Log.i(TAG, "继续");
                } else {
                    Log.i(TAG, "暂停");
                }

                isPause = !isPause;

                if (isPause) {
                    mButton_pause.setText("继续");
                } else {
                    mButton_pause.setText("暂停");
                }
            }
        });

        mHandler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_TEXTVIEW:
                        updateTextView();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void updateTextView(){
        code = createRandom(true,4);

        Toast.makeText(getApplicationContext(),""+code,Toast.LENGTH_SHORT);

        mTextView.setText(String.valueOf(code));

    }

    private void startTimer(){
        if (mTimer == null) {
            mTimer = new Timer();
        }

        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    Log.i(TAG, "count: "+String.valueOf(count));
                    sendMessage(UPDATE_TEXTVIEW);

                    do {
                        try {
                            Log.i(TAG, "sleep(1000)...");
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                    } while (isPause);

                    count ++;
                }
            };
        }

        if(mTimer != null && mTimerTask != null )
            mTimer.schedule(mTimerTask, delay, period);

    }

    private void stopTimer(){

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }

        count = 0;

    }

    public void sendMessage(int id){
        if (mHandler != null) {
            Message message = Message.obtain(mHandler, id);
            mHandler.sendMessage(message);
        }
    }




    public static String createRandom(boolean numberFlag, int length){
        String retStr;
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);

        return retStr;
    }
}
