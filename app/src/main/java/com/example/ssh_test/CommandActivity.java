package com.example.ssh_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CommandActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command);

        String user="root";
        String password="huahuadan1234";
        String host="47.102.114.76";

        Button commandButton=findViewById(R.id.command_btn);//命令行执行按钮
        Button uploadButton=findViewById(R.id.upload_btn);//上传文件按钮
        Button downloadButton=findViewById(R.id.download_btn);//上传文件按钮
        Handler mhandler=new mHandler(this,R.id.command_tv);

        Intent intent=getIntent();
        user=intent.getStringExtra("user");
        password=intent.getStringExtra("password");
        host=intent.getStringExtra("host");

        //命令行按钮绑定
        String finalUser = user;
        String finalPassword = password;
        String finalHost = host;
        commandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText commandEditText=findViewById(R.id.command_et);
                String command=commandEditText.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(CommandActivity.this);
                        sshUtils.connectSSH(finalUser, finalPassword, finalHost,command,mhandler);
                    }
                }).start();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText serverEditText=findViewById(R.id.server_et);
                EditText storageEditText=findViewById(R.id.storage_et);
                String server=serverEditText.getText().toString();
                String storage=storageEditText.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(CommandActivity.this);
                        sshUtils.uploadSSH(finalUser, finalPassword, finalHost,server,storage);
                    }
                }).start();
            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText serverEditText=findViewById(R.id.server_et);
                EditText storageEditText=findViewById(R.id.storage_et);
                String server=serverEditText.getText().toString();
                String storage=storageEditText.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(CommandActivity.this);
                        sshUtils.downloadSSH(finalUser, finalPassword, finalHost,server,storage);
                    }
                }).start();
            }
        });
    }
}