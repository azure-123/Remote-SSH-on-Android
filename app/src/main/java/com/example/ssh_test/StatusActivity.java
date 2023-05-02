package com.example.ssh_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;

public class StatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        String _user="root";
        String _password="huahuadan1234";
        String _host="47.102.114.76";

        Intent intent=getIntent();
        String user=intent.getStringExtra("user");
        String password=intent.getStringExtra("password");
        String host=intent.getStringExtra("host");

        Button topButton=findViewById(R.id.top_btn);//top查看各项资源使用情况
        Button psButton=findViewById(R.id.ps_btn);//查看所有进程
        Button killButton=findViewById(R.id.kill_btn);//停止进程
        Button dfButton=findViewById(R.id.df_btn);//查看磁盘使用情况
        Button freeButton=findViewById(R.id.free_btn);//查看内存使用情况

        //top查看各项资源使用情况
        topButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler mhandler=new mHandler(StatusActivity.this,R.id.top_show_tv);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(StatusActivity.this);
                        sshUtils.topSSH(user,password,host,"top -b",mhandler);
                    }
                }).start();
            }
        });
        //查看所有进程
        psButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler mhandler=new mHandler(StatusActivity.this,R.id.ps_show_tv);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(StatusActivity.this);
                        sshUtils.connectSSH(user,password,host,"ps",mhandler);
                    }
                }).start();
            }
        });
        //停止进程
        killButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText killEditText=findViewById(R.id.kill_name);

                String kill=killEditText.getTransitionName().toString();
                Handler mhandler=new mHandler(StatusActivity.this,0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(StatusActivity.this);
                        sshUtils.connectSSH(user,password,host,"kill -9 "+kill,mhandler);
                    }
                }).start();
            }
        });
        //查看磁盘使用情况
        dfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler mhandler=new mHandler(StatusActivity.this,R.id.df_show_tv);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(StatusActivity.this);
                        sshUtils.connectSSH(user,password,host,"df",mhandler);
                    }
                }).start();
            }
        });
        //查看内存使用情况
        freeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler mhandler=new mHandler(StatusActivity.this,R.id.free_show_tv);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(StatusActivity.this);
                        sshUtils.connectSSH(user,password,host,"free",mhandler);
                    }
                }).start();
            }
        });
    }
}