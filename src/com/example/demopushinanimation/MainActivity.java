package com.example.demopushinanimation;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final String TAG = "PushInAnimation";

    private TextView mPoemTv;
    private Button mOpenBtn;

    private static final int PLAY_PUSH_IN_ANIMATION = 0;
    private static final int DEFAULT_HEIGHT = 300; //300dp
    private int actualHeight = DEFAULT_HEIGHT;
    private int iterator = 0;
    private ViewGroup.LayoutParams mParams = null;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case PLAY_PUSH_IN_ANIMATION: {
                    if(mPoemTv.getLineCount() > 0 &&
                            mPoemTv.getLineCount() * mPoemTv.getLineHeight() != actualHeight) {
                        //calculate the actual height again, because I use WRAP_CONTENT property for
                        //this TextView
                        actualHeight = mPoemTv.getLineCount() * mPoemTv.getLineHeight();
                    }

                    if(mParams.height >= actualHeight) {
                        mParams.height = actualHeight;
                        mPoemTv.setLayoutParams(mParams);
                        mOpenBtn.setClickable(true);
                        mOpenBtn.setText(R.string.close_btn);

                        iterator = 0;

                        removeMessages(PLAY_PUSH_IN_ANIMATION);

                        return;
                    }

                    //mParams.height += (iterator >= 10 ? iterator -= 12 : iterator++);//accelerate and decelerate
                    mParams.height += iterator++;    //accelerate
                    mPoemTv.setLayoutParams(mParams);

                    sendMessageDelayed(obtainMessage(PLAY_PUSH_IN_ANIMATION), 20);
                }break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPoemTv = (TextView)findViewById(R.id.poem_tv);
        mOpenBtn = (Button)findViewById(R.id.open_btn);
        mOpenBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(mPoemTv.getVisibility() != View.VISIBLE) {
                    mOpenBtn.setClickable(false);
                    mPoemTv.setVisibility(View.VISIBLE);
                    mParams = mPoemTv.getLayoutParams();
                    mParams.height = 0;
                    mPoemTv.setLayoutParams(mParams);

                    //calculate the actual height
                    final float density = getResources().getDisplayMetrics().density;
                    actualHeight = (int)(DEFAULT_HEIGHT * density + 0.5f);

                    mHandler.sendEmptyMessage(PLAY_PUSH_IN_ANIMATION);
                } else {
                    mPoemTv.setVisibility(View.GONE);
                    mOpenBtn.setText(R.string.open_btn);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void Log(String msg) {
        android.util.Log.i("Young Lee", msg);
    }
}
