package com.example.ssh_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jcraft.jsch.*;
import com.example.ssh_test.SSHUtils;

public class BootActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot);

        Button shutdownButton=findViewById(R.id.shutdown_btn);//关机按钮
        Button rebootButton=findViewById(R.id.reboot_btn);//重启按钮
        Button bootTimeButton=findViewById(R.id.boot_time_btn);//定时重启按钮
        Button bootCancelButton=findViewById(R.id.cancel_boot_btn);//取消定时计划

        String _user="root";
        String _password="huahuadan1234";
        String _host="47.102.114.76";

        Intent intent=getIntent();
        String user=intent.getStringExtra("user");
        String password=intent.getStringExtra("password");
        String host=intent.getStringExtra("host");

        Handler mhandler=new mHandler(this,R.id.console_tv);
        //立即关机按钮绑定
        shutdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(BootActivity.this);
                        sshUtils.connectSSH(user,password,host,"shutdown -h now",mhandler);
                    }
                }).start();
            }
        });

        //定时关机按钮绑定
        bootTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText timeEditText=findViewById(R.id.shutdown_time_et);
                String time=timeEditText.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(BootActivity.this);
                        sshUtils.connectSSH(user,password,host,"shutdown +"+time,mhandler);
                    }
                }).start();
            }
        });

        //远程重启按钮绑定
        rebootButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(BootActivity.this);
                        sshUtils.connectSSH(user,password,host,"shutdown -r now",mhandler);
                    }
                }).start();
            }
        });

        //取消关机计划
        bootCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(BootActivity.this);
                        sshUtils.connectSSH(user,password,host,"shutdown -c",mhandler);
                    }
                }).start();
            }
        });

    }
}