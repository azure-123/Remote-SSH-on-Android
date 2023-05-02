package com.example.ssh_test;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class LoginActivity extends AppCompatActivity {

    private boolean connectState=false;
    public Handler mhandler;

    private  final int REQUEST_EXTERNAL_STORAGE = 1;
    private  String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    public  void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        verifyStoragePermissions(this);

        final boolean[] flag = {false};
        EditText hostEditText = findViewById(R.id.host_et);
        EditText usernameEditText = findViewById(R.id.username_et);
        EditText passwordEditText = findViewById(R.id.password_et);
        EditText portEditText=findViewById(R.id.port_et);




        Button connectButton=findViewById(R.id.connect_btn);
        //Button rootButton=findViewById(R.id.root_btn);
        //Button disconnectButton=findViewById(R.id.disconnect_btn);
        mhandler=new mHandler();
        connectButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                String host = hostEditText.getText().toString();
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                int port;
                if (portEditText.getText().toString().equals("")) {
                    port = 0;
                } else {
                    port = Integer.parseInt(portEditText.getText().toString());
                }
//                int port=22;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        flag[0] = connectSSH(username, password, host, port);
                    }
                }).start();
                Toast.makeText(LoginActivity.this,"请稍等！", Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (flag[0]) {
                            new AlertDialog.Builder(
                                    LoginActivity.this)
                                    .setTitle("提示")
                                    .setMessage("The authenticity of host '" + host + " (" + host + ")' can't be established.\n" +
                                            "ECDSA key fingerprint is SHA256:ckDktQw16lOAnl+bKIWVo+J3kE/HaeNoiutDv2cFy7c.\n" +
                                            "Are you sure you want to continue connecting (yes/no)?\n")
                                    .setPositiveButton("确定连接", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(LoginActivity.this, MainPageActivity.class);
                                            intent.putExtra("host", host);
                                            intent.putExtra("user", username);
                                            intent.putExtra("password", password);
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("取消连接", null)
                                    .show();

                        }
                        else{
                            Toast.makeText(LoginActivity.this,"输入存在错误！", Toast.LENGTH_LONG).show();
                        }
                    }
                },1000);
                if(flag[0]) {
                    Toast.makeText(LoginActivity.this, "网络不佳，请重试！", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public boolean connectSSH(String user,String password,String host,int port) {
        int portCorrect = 22;
//user.equals(userCorrect) && password.equals(passwordCorrect) && host.equals(hostCorrect)
        JSch jSch = new JSch();
        Session session = null;
        ChannelExec channelExec = null;
        BufferedReader inputStreamReader = null;
        BufferedReader errInputStreamReader = null;
        StringBuilder runLog = new StringBuilder("");
        StringBuilder errLog = new StringBuilder("");
        try {
            // 1. 获取 ssh session
            session = jSch.getSession(user, host, port);
            session.setPassword(password);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setTimeout(3000);
            session.setServerAliveInterval(10000);
            session.connect();
            connectState = session.isConnected();
            if (connectState) {
                Log.e("connect", "result success");
            } else {
                Log.e("connect", "result failure");
            }

            // 2. 通过 exec 方式执行 shell 命令
            ChannelExec channel=(ChannelExec) session.openChannel("exec");
            //channel.setCommand(command);
            channel.connect();

            // 3. 获取标准输入流
            inputStreamReader = new BufferedReader(new InputStreamReader(channel.getInputStream(), StandardCharsets.UTF_8));
            // 4. 获取标准错误输入流
            errInputStreamReader = new BufferedReader(new InputStreamReader(channel.getErrStream(), StandardCharsets.UTF_8));
            // 5. 记录命令执行 log
            String line = null;
            while ((line = inputStreamReader.readLine()) != null) {
                runLog.append(line).append("\n");
            }
            // 6. 记录命令执行错误 log
            String errLine = null;
            while ((errLine = errInputStreamReader.readLine()) != null) {
                errLog.append(errLine).append("\n");
            }
            //无法直接通过子线程对组件内容进行修改，需要handler传值
            Message msg = Message.obtain();
            msg.what = 1;
            Bundle bundle = new Bundle();
            bundle.putString("run_log",runLog.toString());
            bundle.putString("err_log",errLog.toString());
            msg.setData(bundle);//mes利用Bundle传递数据
            mhandler.sendMessage(msg);

            // 7. 输出 shell 命令执行日志
//                System.out.println("exitStatus=" + channelExec.getExitStatus() + ", openChannel.isClosed="
//                        + channelExec.isClosed());

            System.out.println("命令执行完成，执行日志如下:");
            System.out.println(runLog.toString());
            System.out.println("命令执行完成，执行错误日志如下:");
            System.out.println(errLog.toString());
            Log.e("runlog",runLog.toString());
            Log.e("errlog",errLog.toString());
//                Looper.prepare();
//                Looper.loop();

            //channel.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        finally {
            try {
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (errInputStreamReader != null) {
                    errInputStreamReader.close();
                }

                if (channelExec != null) {
                    channelExec.disconnect();
                }
                if (session != null) {
                    session.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
    class mHandler extends Handler{
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {

//            String runLog=msg.getData().getString("run_log");
//            TextView consoleResult=findViewById(R.id.console_result);
//            consoleResult.setText(runLog);
        }
    }
}