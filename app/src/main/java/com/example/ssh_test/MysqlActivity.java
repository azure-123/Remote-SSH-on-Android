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

public class MysqlActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysql);

        String _user="root";
        String _password="huahuadan1234";
        String _host="47.102.114.76";

        Intent intent=getIntent();
        String user=intent.getStringExtra("user");
        String password=intent.getStringExtra("password");
        String host=intent.getStringExtra("host");

        Button mysqlQuaTypeButton=findViewById(R.id.mysql_qua_type_btn);
        Button mysqlBufferButton=findViewById(R.id.mysql_buffer_btn);
        Button mysqlLogBufferButton=findViewById(R.id.mysql_log_buffer_btn);
        Button mysqlTableNumButton=findViewById(R.id.mysql_table_num_btn);
        Button mysqlCharacterButton=findViewById(R.id.mysql_character_btn);

        mysqlQuaTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取radio及选中的文字
                RadioGroup radioGroup=findViewById(R.id.mysql_qua_type);
                RadioButton radioButton=(RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                String isolation=radioButton.getText().toString();
                //构造map
                Map<String,String> stringStringMap=new HashMap<String,String>();
                stringStringMap.put("transaction_isolation",isolation);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(MysqlActivity.this);
                        sshUtils.sftpSSH(user,password,host,"/etc/my.cnf",stringStringMap,1);
                        //sshUtils.connectSSH(user,password,host,"Timeout "+apacheTimeout,mhandler);
                    }
                }).start();

            }
        });

        mysqlBufferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mysqlBufferEditText=findViewById(R.id.mysql_buffer);
                String mysqlBuffer=mysqlBufferEditText.getText().toString()+"M";
                //构造map
                Map<String,String> stringStringMap=new HashMap<String,String>();
                stringStringMap.put("query_cache_size",mysqlBuffer);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(MysqlActivity.this);
                        sshUtils.sftpSSH(user,password,host,"/etc/my.cnf",stringStringMap,1);
                        //sshUtils.connectSSH(user,password,host,"Timeout "+apacheTimeout,mhandler);
                    }
                }).start();
            }
        });
        mysqlLogBufferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mysqlLogBufferEditText=findViewById(R.id.mysql_log_buffer);
                String mysqlLogBuffer=mysqlLogBufferEditText.getText().toString();
                //构造map
                Map<String,String> stringStringMap=new HashMap<String,String>();
                stringStringMap.put("innodb_log_buffer_size",mysqlLogBuffer);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(MysqlActivity.this);
                        sshUtils.sftpSSH(user,password,host,"/etc/my.cnf",stringStringMap,1);
                        //sshUtils.connectSSH(user,password,host,"Timeout "+apacheTimeout,mhandler);
                    }
                }).start();
            }
        });
        mysqlTableNumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mysqlTableNumEditText=findViewById(R.id.mysql_table_num);
                String mysqlTableNum=mysqlTableNumEditText.getText().toString();
                //构造map
                Map<String,String> stringStringMap=new HashMap<String,String>();
                stringStringMap.put("open_files_limit",mysqlTableNum);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(MysqlActivity.this);
                        sshUtils.sftpSSH(user,password,host,"/etc/my.cnf",stringStringMap,1);
                        //sshUtils.connectSSH(user,password,host,"Timeout "+apacheTimeout,mhandler);
                    }
                }).start();
            }
        });
        mysqlCharacterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mysqlCharacterEditText=findViewById(R.id.mysql_character);
                String mysqlCharacter=mysqlCharacterEditText.getText().toString();
                //构造map
                Map<String,String> stringStringMap=new HashMap<String,String>();
                stringStringMap.put("default-character-set",mysqlCharacter);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(MysqlActivity.this);
                        sshUtils.sftpSSH(user,password,host,"/etc/my.cnf",stringStringMap,1);
                        //sshUtils.connectSSH(user,password,host,"Timeout "+apacheTimeout,mhandler);
                    }
                }).start();
            }
        });
    }
}