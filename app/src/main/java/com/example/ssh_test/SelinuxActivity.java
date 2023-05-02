package com.example.ssh_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.HashMap;
import java.util.Map;

public class SelinuxActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selinux);

        String _user="root";
        String _password="huahuadan1234";
        String _host="47.102.114.76";

        Intent intent=getIntent();
        String user=intent.getStringExtra("user");
        String password=intent.getStringExtra("password");
        String host=intent.getStringExtra("host");

        Button selinuxTypeButton=findViewById(R.id.selinux_type_btn);

        //TODO:相关函数未修改
        selinuxTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler mhandler=new mHandler(SelinuxActivity.this,0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(SelinuxActivity.this);
                        //获取radio及选中的文字
                        RadioGroup radioGroup=findViewById(R.id.selinux_type);
                        RadioButton radioButton=(RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                        String sType=radioButton.getText().toString();
                        //构造map
                        Map<String,String> stringStringMap=new HashMap<String,String>();
                        stringStringMap.put("sType",sType);
                        sshUtils.sftpSSH(user,password,host,"/etc/selinux/config",stringStringMap,0);
                    }
                }).start();
            }
        });
    }
}