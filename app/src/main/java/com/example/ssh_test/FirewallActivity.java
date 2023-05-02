package com.example.ssh_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FirewallActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firewall);

        String _user="root";
        String _password="huahuadan1234";
        String _host="47.102.114.76";

        Intent intent=getIntent();
        String user=intent.getStringExtra("user");
        String password=intent.getStringExtra("password");
        String host=intent.getStringExtra("host");

        Button showFirewallStatusButton=findViewById(R.id.show_firewall_status_btn);
        Button openFirewallButton=findViewById(R.id.open_firewall_btn);
        Button closeFirewallButton=findViewById(R.id.close_firewall_btn);
        Button restartFirewallButton=findViewById(R.id.restart_firewall_btn);
        Button enableFirewallButton=findViewById(R.id.enable_firewall_btn);
        Button disableFirewallButton=findViewById(R.id.disable_firewall_btn);
        Button showFirewallPortButton=findViewById(R.id.show_firewall_port_btn);
        Button firewallOpenPortButton=findViewById(R.id.firewall_open_port_btn);
        Button firewallClosePortButton=findViewById(R.id.firewall_close_port_btn);

        showFirewallStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler mhandler=new mHandler(FirewallActivity.this,R.id.show_firewall_status_tv);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(FirewallActivity.this);
                        sshUtils.connectSSH(user,password,host,"systemctl status firewalld",mhandler);
                    }
                }).start();
            }
        });

        openFirewallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler mhandler=new mHandler(FirewallActivity.this,0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(FirewallActivity.this);
                        sshUtils.connectSSH(user,password,host,"systemctl start firewalld",mhandler);
                    }
                }).start();
            }
        });
        closeFirewallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler mhandler=new mHandler(FirewallActivity.this,0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(FirewallActivity.this);
                        sshUtils.connectSSH(user,password,host,"systemctl stop firewalld",mhandler);
                    }
                }).start();
            }
        });
        restartFirewallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler mhandler=new mHandler(FirewallActivity.this,0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(FirewallActivity.this);
                        sshUtils.connectSSH(user,password,host,"firewall-cmd --reload",mhandler);
                    }
                }).start();
            }
        });
        enableFirewallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler mhandler=new mHandler(FirewallActivity.this,0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(FirewallActivity.this);
                        sshUtils.connectSSH(user,password,host,"systemctl enable firewalld",mhandler);
                    }
                }).start();
            }
        });
        disableFirewallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler mhandler=new mHandler(FirewallActivity.this,0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(FirewallActivity.this);
                        sshUtils.connectSSH(user,password,host,"systemctl disable firewalld",mhandler);
                    }
                }).start();
            }
        });
        showFirewallPortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler mhandler=new mHandler(FirewallActivity.this,R.id.show_firewall_port_tv);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(FirewallActivity.this);
                        sshUtils.connectSSH(user,password,host,"firewall-cmd --list-ports",mhandler);
                    }
                }).start();
            }
        });
        firewallOpenPortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText firewallOpenPortTextView=findViewById(R.id.firewall_open_port);

                String firewallOpenPort=firewallOpenPortTextView.getText().toString();
                Handler mhandler=new mHandler(FirewallActivity.this,0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(FirewallActivity.this);
                        sshUtils.connectSSH(user,password,host,"firewall-cmd --zone=public --add-port="+firewallOpenPort+"/tcp --permanent",mhandler);
                    }
                }).start();
            }
        });
        firewallClosePortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText firewallClosePortTextView=findViewById(R.id.firewall_close_port);

                String firewallClosePort=firewallClosePortTextView.getText().toString();
                Handler mhandler=new mHandler(FirewallActivity.this,0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(FirewallActivity.this);
                        sshUtils.connectSSH(user,password,host,"firewall-cmd --zone=public --remove-port="+firewallClosePort+"/tcp --permanent",mhandler);
                    }
                }).start();
            }
        });



    }
}