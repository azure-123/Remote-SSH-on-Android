package com.example.ssh_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class ApacheActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apache);

        String _user="root";
        String _password="huahuadan1234";
        String _host="47.102.114.76";

        Intent intent=getIntent();
        String user=intent.getStringExtra("user");
        String password=intent.getStringExtra("password");
        String host=intent.getStringExtra("host");

        Button apacheHomeHtmlButton=findViewById(R.id.apache_home_html_btn);
        Button apacheTimeoutButton=findViewById(R.id.apache_timeout_btn);
        Button apacheCharsetButton=findViewById(R.id.apache_charset_btn);
        Button apacheCloseHtmlButton=findViewById(R.id.apache_close_html_btn);
        Button apacheOpenHtmlButton=findViewById(R.id.apache_open_html_btn);
        Button apacheLogLevelButton=findViewById(R.id.apache_log_level_btn);

        //TODO
        apacheHomeHtmlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText apacheHomeHtmlTextView=findViewById(R.id.apache_home_html);
                String apacheHomeHtml=apacheHomeHtmlTextView.getText().toString();
                Handler mhandler=new mHandler(ApacheActivity.this,0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(ApacheActivity.this);
                        sshUtils.connectSSH(user,password,host,"cat /etc/group",mhandler);
                    }
                }).start();
            }
        });
        //TODO
        apacheTimeoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText apacheTimeoutTextView=findViewById(R.id.apache_timeout);
                String apacheTimeout=apacheTimeoutTextView.getText().toString();
                Handler mhandler=new mHandler(ApacheActivity.this,0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(ApacheActivity.this);
                        //sshUtils.connectSSH(user,password,host,"Timeout "+apacheTimeout,mhandler);
                    }
                }).start();
            }
        });
        //TODO
        apacheCharsetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText apacheCharsetTextView=findViewById(R.id.apache_charset);
                String apacheCharset=apacheCharsetTextView.getText().toString();
                Handler mhandler=new mHandler(ApacheActivity.this,0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(ApacheActivity.this);
                        //sshUtils.connectSSH(user,password,host,"Timeout "+apacheTimeout,mhandler);
                    }
                }).start();
            }
        });
        //TODO
        apacheCloseHtmlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText apacheCloseHtmlTextView=findViewById(R.id.apache_close_html);
                String apacheCloseHtml=apacheCloseHtmlTextView.getText().toString();
                Handler mhandler=new mHandler(ApacheActivity.this,0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(ApacheActivity.this);
                        //sshUtils.connectSSH(user,password,host,"Timeout "+apacheTimeout,mhandler);
                    }
                }).start();
            }
        });
        //TODO
        apacheOpenHtmlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText apacheOpenHtmlTextView=findViewById(R.id.apache_open_html);
                String apacheOpenHtml=apacheOpenHtmlTextView.getText().toString();
                Handler mhandler=new mHandler(ApacheActivity.this,0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(ApacheActivity.this);
                        //sshUtils.connectSSH(user,password,host,"Timeout "+apacheTimeout,mhandler);
                    }
                }).start();
            }
        });
        //TODO
        apacheLogLevelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton warnRadioButton=findViewById(R.id.warn_rb);
                RadioButton errorRadioButton=findViewById(R.id.error_rb);
                RadioButton fatalRadioButton=findViewById(R.id.fatal_rb);
                RadioButton allRadioButton=findViewById(R.id.all_rb);
                RadioButton offRadioButton=findViewById(R.id.off_rb);
                Handler mhandler=new mHandler(ApacheActivity.this,0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String tempString="";

                        if(warnRadioButton.isChecked())
                        {
                            tempString="warn";
                        }
                        if(errorRadioButton.isChecked())
                        {
                            tempString="error";
                        }
                        if(fatalRadioButton.isChecked())
                        {
                            tempString="fatal";
                        }
                        if(allRadioButton.isChecked())
                        {
                            tempString="all";
                        }
                        if(offRadioButton.isChecked())
                        {
                            tempString="off";
                        }
                        SSHUtils sshUtils=new SSHUtils(ApacheActivity.this);
                        //sshUtils.connectSSH(user,password,host,"Timeout "+apacheTimeout,mhandler);
                    }
                }).start();
            }
        });




    }
}