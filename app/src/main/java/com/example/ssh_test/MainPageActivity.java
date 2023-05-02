package com.example.ssh_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        Button commandButton=findViewById(R.id.command_btn);//命令行及文件管理按钮
        Button bootButton=findViewById(R.id.boot_btn);//电源管理按钮
        Button userButton=findViewById(R.id.user_btn);//用户管理按钮
        Button runButton=findViewById(R.id.run_btn);//运行状态按钮
        Button logButton=findViewById(R.id.log_btn);//日志管理按钮
        Button firewallButton=findViewById(R.id.firewall_btn);//防火墙管理按钮
        Button selinuxButton=findViewById(R.id.selinux_btn);//SELinux管理按钮
        Button mysqlButton=findViewById(R.id.mysql_btn);//mysql管理按钮
        Button apacheButton=findViewById(R.id.apache_btn);//apache管理按钮

        Intent intent=getIntent();
        String user=intent.getStringExtra("user");
        String password=intent.getStringExtra("password");
        String host=intent.getStringExtra("host");

        //命令行及文件管理按钮绑定
        commandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainPageActivity.this,CommandActivity.class);
                intent.putExtra("host",host);
                intent.putExtra("user",user);
                intent.putExtra("password",password);
                startActivity(intent);
            }
        });

        //电源管理按钮绑定
        bootButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainPageActivity.this,BootActivity.class);
                intent.putExtra("host",host);
                intent.putExtra("user",user);
                intent.putExtra("password",password);
                startActivity(intent);
            }
        });

        //用户管理按钮绑定
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainPageActivity.this,UserActivity.class);
                intent.putExtra("host",host);
                intent.putExtra("user",user);
                intent.putExtra("password",password);
                startActivity(intent);
            }
        });

        //命令行及文件管理按钮绑定
        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainPageActivity.this,StatusActivity.class);
                intent.putExtra("host",host);
                intent.putExtra("user",user);
                intent.putExtra("password",password);
                startActivity(intent);
            }
        });

        //日志管理按钮绑定
        logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainPageActivity.this,SyslogActivity.class);
                intent.putExtra("host",host);
                intent.putExtra("user",user);
                intent.putExtra("password",password);
                startActivity(intent);
            }
        });

        //防火墙管理按钮绑定
        firewallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainPageActivity.this,FirewallActivity.class);
                intent.putExtra("host",host);
                intent.putExtra("user",user);
                intent.putExtra("password",password);
                startActivity(intent);
            }
        });

        //SELinux管理按钮绑定
        selinuxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainPageActivity.this,SelinuxActivity.class);
                intent.putExtra("host",host);
                intent.putExtra("user",user);
                intent.putExtra("password",password);
                startActivity(intent);
            }
        });

        //Mysql管理按钮绑定
        mysqlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainPageActivity.this,MysqlActivity.class);
                intent.putExtra("host",host);
                intent.putExtra("user",user);
                intent.putExtra("password",password);
                startActivity(intent);
            }
        });

        //Apache管理按钮绑定
        apacheButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainPageActivity.this,ApacheActivity.class);
                intent.putExtra("host",host);
                intent.putExtra("user",user);
                intent.putExtra("password",password);
                startActivity(intent);
            }
        });


    }


}