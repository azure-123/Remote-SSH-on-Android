package com.example.ssh_test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jcraft.jsch.Buffer;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Hashtable;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Properties;

import com.example.ssh_test.FileUtils;

public class MainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);




        Button connectButton=findViewById(R.id.connect_btn);
        Button rootButton=findViewById(R.id.root_btn);
        //Button disconnectButton=findViewById(R.id.disconnect_btn);
        mhandler=new mHandler();

        connectButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                if (false) {
                    Toast.makeText(MainActivity.this, "已连接！", Toast.LENGTH_LONG).show();
                } else {
                    EditText hostEditText = findViewById(R.id.host_et);
                    EditText usernameEditText = findViewById(R.id.username_et);
                    EditText passwordEditText = findViewById(R.id.password_et);
                    EditText commandEditText=findViewById(R.id.command_et);

                    String host = hostEditText.getText().toString();
                    String username = usernameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    String command=commandEditText.getText().toString();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            connectSSH(username, password, host,command);
                        }
                    }).start();
                }
            }
        });

        rootButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText hostEditText = findViewById(R.id.host_et);
                EditText usernameEditText = findViewById(R.id.username_et);
                EditText passwordEditText = findViewById(R.id.password_et);
                EditText commandEditText=findViewById(R.id.command_et);

                String host = hostEditText.getText().toString();
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String command=commandEditText.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        rootSSH(username, password, host,command);
                    }
                }).start();
            }
        });

    }

    public void connectSSH(String user,String password,String host,String command) {
        String userCorrect = "root";
        String passwordCorrect = "huahuadan1234";
        String hostCorrect = "47.102.114.76";
        int port = 22;
//user.equals(userCorrect) && password.equals(passwordCorrect) && host.equals(hostCorrect)
        if (user.equals(userCorrect) && password.equals(passwordCorrect) && host.equals(hostCorrect)) {

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
                session.connect();
                connectState = session.isConnected();
                if (connectState) {
                    Log.e("connect", "result success");
//                    Looper.prepare();
//                    Toast.makeText(MainActivity.this, "连接成功！", Toast.LENGTH_LONG).show();
//                    Looper.loop();
                } else {
                    Log.e("connect", "result failure");
//                    Looper.prepare();
//                    Toast.makeText(MainActivity.this, "连接失败！", Toast.LENGTH_LONG).show();
//                    Looper.loop();
                }

                // 2. 通过 exec 方式执行 shell 命令
                ChannelExec channel=(ChannelExec) session.openChannel("exec");
                channel.setCommand(command);
                channel.connect();

                // 3. 获取标准输入流

                inputStreamReader = new BufferedReader(new InputStreamReader(channel.getInputStream()));
                // 4. 获取标准错误输入流
                errInputStreamReader = new BufferedReader(new InputStreamReader(channel.getErrStream()));
                // 5. 记录命令执行 log
                String line = null;
                Button stop=findViewById(R.id.stop_btn);
                final boolean[] flag = {true};

                while ((line = inputStreamReader.readLine()) != null&& flag[0]) {
                    runLog.append(line).append("\n");
                    System.out.println(line);
                    stop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(flag[0])
                                flag[0] =false;
                        }
                    });

                    Message msg = Message.obtain();
                    msg.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putString("run_log",runLog.toString());
                    bundle.putString("err_log",errLog.toString());
                    msg.setData(bundle);//mes利用Bundle传递数据
                    mhandler.sendMessage(msg);
                }

                // 6. 记录命令执行错误 log
                String errLine = null;
                while ((errLine = errInputStreamReader.readLine()) != null) {
                    errLog.append(errLine).append("\n");
                }
                //无法直接通过子线程对组件内容进行修改，需要handler传值


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
        }
        else
        {
            Looper.prepare();
            Toast.makeText(MainActivity.this, "输入错误！", Toast.LENGTH_LONG).show();
            Looper.loop();
        }
    }

    public void rootSSH(String user,String password,String host,String command)
    {
        String userCorrect = "root";
        String passwordCorrect = "huahuadan1234";
        String hostCorrect = "47.102.114.76";
        int port = 22;
//user.equals(userCorrect) && password.equals(passwordCorrect) && host.equals(hostCorrect)
        if (user.equals(userCorrect) && password.equals(passwordCorrect) && host.equals(hostCorrect)) {

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
                session.connect();
                connectState = session.isConnected();
                if (connectState) {
                    Log.e("connect", "result success");
                } else {
                    Log.e("connect", "result failure");
                }

                // 2. 通过 sftp 方式执行 shell 命令
                Channel channel= session.openChannel("sftp");
                //channel.setCommand(command);
                channel.connect();


                File newfile=new File("/storage/emulated/0/test.txt");
                try {
                    if(!newfile.exists()) {
                        newfile.createNewFile();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String saveFile="/storage/emulated/0/test.txt";
                String downloadFile="test.txt";
                //使用sftp的channel，到达路径
                String directory="/root";
                ChannelSftp sftp=(ChannelSftp) channel;
                sftp.cd(directory);

                File file=new File(saveFile);
                FileUtils fileUtils=new FileUtils();
                //将主机上的文件远程下载到手机本地
                sftp.get(downloadFile,new FileOutputStream(file));
                //读取本地文件的内容
                String content=fileUtils.read("/storage/emulated/0/test.txt");
                System.out.println(content);
                //修改本地文件内容
                fileUtils.write("/storage/emulated/0/test.txt");
                //将本地文件上传到服务器上
                sftp.put("/storage/emulated/0/test.txt","/root/test1.txt");

                //无法直接通过子线程对组件内容进行修改，需要handler传值
                Message msg = Message.obtain();
                msg.what = 1;
                Bundle bundle = new Bundle();
                bundle.putString("run_log","下载成功");
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
        }
        else
        {
            Looper.prepare();
            Toast.makeText(MainActivity.this, "输入错误！", Toast.LENGTH_LONG).show();
            Looper.loop();
        }
    }

//    public String read(String filename) throws IOException {
//        String str="";
//        try {
//            //打开文件输入流
//            File urlFile = new File(filename);
//            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(urlFile));
//            BufferedReader br = new BufferedReader(inputStreamReader);
//            String line = null;
//            //定义字符串变量
//            //读取文件内容，当文件内容长度大于0时，
//            while((line=br.readLine())!=null){
//                str=str+line+"\n";
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        return str;
//    }
//
//    public void write(String filename) {
//// add-write text into file
//        try {
//            File urlFile = new File(filename);
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(urlFile));
//            BufferedWriter br = new BufferedWriter(outputStreamWriter);
////            String line = null;
//            br.write("huahuadan1234");
//            br.close();
//
////display file saved message
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    class mHandler extends Handler{
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            String runLog=msg.getData().getString("run_log");
            TextView consoleResult=findViewById(R.id.console_result);
            consoleResult.setText(runLog);
        }
    }

}