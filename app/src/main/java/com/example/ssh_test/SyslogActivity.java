package com.example.ssh_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.HashMap;
import java.util.Map;

public class SyslogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syslog);

        String _user="root";
        String _password="huahuadan1234";
        String _host="47.102.114.76";

        Intent intent=getIntent();
        String user=intent.getStringExtra("user");
        String password=intent.getStringExtra("password");
        String host=intent.getStringExtra("host");

        Button messageLogButton=findViewById(R.id.message_log_btn);//查看最新message日志
        Button secureLogButton=findViewById(R.id.secure_log_btn);//查看最新secure日志
        Button cronLogButton=findViewById(R.id.cron_log_btn);//查看最新cron日志
        Button dmesgLogButton=findViewById(R.id.dmesg_log_btn);//查看最新dmesg日志
        Button emailLogButton=findViewById(R.id.email_log_btn);//查看最新email日志
        Button loginLogButton=findViewById(R.id.login_log_btn);//查看最新登陆日志
        Button logSaveTimeButton=findViewById(R.id.log_save_time_btn);//设置系统日志保存时间
        Button logSaveLevelButton=findViewById(R.id.log_save_level_btn);//设置系统日志保存等级

        //查看最新message日志
        messageLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler mhandler=new mHandler(SyslogActivity.this,R.id.message_log_tv);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(SyslogActivity.this);
                        sshUtils.connectSSH(user,password,host,"tail -n 50 /var/log/messages",mhandler);
                    }
                }).start();
            }
        });
        //查看最新secure日志
        secureLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler mhandler=new mHandler(SyslogActivity.this,R.id.secure_log_tv);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(SyslogActivity.this);
                        sshUtils.connectSSH(user,password,host,"tail -n 50 /var/log/secure",mhandler);
                    }
                }).start();
            }
        });
        //查看最新cron日志
        cronLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler mhandler=new mHandler(SyslogActivity.this,R.id.cron_log_tv);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(SyslogActivity.this);
                        sshUtils.connectSSH(user,password,host,"tail -n 50 /var/log/cron",mhandler);
                    }
                }).start();
            }
        });
        //查看最新dmesg日志
        dmesgLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler mhandler=new mHandler(SyslogActivity.this,R.id.dmesg_log_tv);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(SyslogActivity.this);
                        sshUtils.connectSSH(user,password,host,"tail -n 50 /var/log/dmesg",mhandler);
                    }
                }).start();
            }
        });
        //查看最新email日志
        emailLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler mhandler=new mHandler(SyslogActivity.this,R.id.email_log_tv);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(SyslogActivity.this);
                        sshUtils.connectSSH(user,password,host,"tail -n 50 /var/log/mail.log",mhandler);
                    }
                }).start();
            }
        });
        //查看最新登陆日志
        loginLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler mhandler=new mHandler(SyslogActivity.this,R.id.login_log_tv);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(SyslogActivity.this);
                        sshUtils.connectSSH(user,password,host,"last",mhandler);
                    }
                }).start();
            }
        });
        //设置系统日志保存时间
        logSaveTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText logSavetimeEditText=findViewById(R.id.log_save_time);
                String logSavetime=logSavetimeEditText.getText().toString();
                //构造map
                Map<String,String> stringStringMap=new HashMap<String,String>();
                stringStringMap.put("time",logSavetime);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(SyslogActivity.this);
                        sshUtils.sftpSSH(user,password,host,"/etc/logrotate.conf",stringStringMap,3);
                        //sshUtils.connectSSH(user,password,host,"last",mhandler);
                    }
                }).start();
            }
        });

        //设置系统日志保存等级
        logSaveLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取radio及选中的文字
                RadioGroup radioGroup=findViewById(R.id.limit_user_type);
                RadioButton radioButton=(RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                String level=radioButton.getText().toString();
                //构造map
                Map<String,String> stringStringMap=new HashMap<String,String>();
                stringStringMap.put("level",level);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(SyslogActivity.this);
                        sshUtils.sftpSSH(user,password,host,"/etc/rsyslog.conf",stringStringMap,2);
                        //sshUtils.connectSSH(user,password,host,"last",mhandler);
                    }
                }).start();
            }
        });

    }

}