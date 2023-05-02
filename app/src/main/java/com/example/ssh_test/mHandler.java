package com.example.ssh_test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

class mHandler extends Handler {
    public Activity context;
    public int textViewId=0;
    mHandler(Activity context,int id)
    {
        this.context=context;
        this.textViewId=id;
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void handleMessage(Message msg) {
        String runLog=msg.getData().getString("run_log");
        String errLog=msg.getData().getString("err_log");
        if(textViewId!=0) {
            TextView consoleResult = (TextView) context.findViewById(textViewId);
            consoleResult.setText(runLog+"\n"+errLog);
        }
    }
}
