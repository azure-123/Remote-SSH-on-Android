package com.example.ssh_test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

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
import java.util.Map;
import java.util.Properties;

import com.example.ssh_test.LoginActivity;

public class SSHUtils {
    public Activity activity;
    SSHUtils(Activity activity)
    {
        this.activity=activity;
    }

    public void sftpSSH(String user, String password, String host, String directory, Map<String,String> alternative, int num) {
        int port = 22;
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
            String downloadFile="etc/passwd";
            //使用sftp的channel，到达路径
            String storage="/storage/emulated/0/test.txt";
            ChannelSftp sftp=(ChannelSftp) channel;

            File file=new File(saveFile);
            FileUtils fileUtils=new FileUtils();
            //将主机上的文件远程下载到手机本地
            sftp.get(directory,new FileOutputStream(file));
            //读取本地文件的内容
            String content=fileUtils.read(storage);
            System.out.println(content);
            //修改本地文件内容
            String newContent="";

            switch (num)
            {
                case 0:// 设置selinux防护的类型
                    String temp=alternative.get("sType");
                    newContent=fileUtils.setSelinux(content,temp);
                    break;
                case 1:// 设置messgaes日志文件保存等级,保存的大于等于level
                    newContent=fileUtils.setMysqlConf(content,alternative);
                    break;
                case 2:// 设置messgaes日志文件保存等级,保存的大于等于level
                    temp=alternative.get("level");
                    newContent=fileUtils.setLogSaveLevel(content,temp);
                    break;
                case 3:// 设置日志文件保存时间,周为单位,还需要systemctl restart rsyslog重启服务
                    temp=alternative.get("time");
                    newContent=fileUtils.setLogSaveTime(content,temp);
                    break;
                case 4:// 设置用户的配置文件 /etc/security/limits.conf
                    newContent=fileUtils.setUserLimit(content,alternative,true);
                    break;
                case 5:// 设置用户的配置文件 /etc/security/limits.conf
                    newContent=fileUtils.setUserLimit(content,alternative,false);
                    break;
                case 6:// 设置是否需要配置限制文件 /etc/pam.d/sshd
                    newContent=fileUtils.setNeedUserLimit(content);
                    break;
                case 7:// 设置用户设置密码的基本规则
                    newContent=fileUtils.setUserPasswdRule(content,alternative);
                    break;
                case 8:
                    temp=alternative.get("name");
                    newContent=fileUtils.changUsertoRoot(content,temp);
                    break;
                default:
                    break;
            }


            fileUtils.write(storage,newContent);
            //将本地文件上传到服务器上
            sftp.put(storage,directory);

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
            Looper.prepare();
            new AlertDialog.Builder(
                    activity)
                    .setTitle("提示")
                    .setMessage("操作成功！")
                    .setPositiveButton("确定", null)
                    .show();
            Looper.loop();
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
    public void connectSSH(String user,String password,String host,String command) {
        int port = 22;
//user.equals(userCorrect) && password.equals(passwordCorrect) && host.equals(hostCorrect)
        JSch jSch = new JSch();
        Session session = null;
        ChannelExec channelExec = null;
        BufferedReader inputStreamReader = null;
        BufferedReader errInputStreamReader = null;
        StringBuilder runLog = new StringBuilder("");
        StringBuilder errLog = new StringBuilder("");
        try {
//                // 1. 获取 ssh session
            session = jSch.getSession(user, host, port);
            session.setPassword(password);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setTimeout(3000);
            session.connect();

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
    public void connectSSH(String user, String password, String host, String command, Handler mhandler) {
        int port = 22;
//user.equals(userCorrect) && password.equals(passwordCorrect) && host.equals(hostCorrect)
        JSch jSch = new JSch();
        Session session = null;
        ChannelExec channelExec = null;
        BufferedReader inputStreamReader = null;
        BufferedReader errInputStreamReader = null;
        StringBuilder runLog = new StringBuilder("");
        StringBuilder errLog = new StringBuilder("");
        try {
//                // 1. 获取 ssh session
            session = jSch.getSession(user, host, port);
            session.setPassword(password);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setTimeout(3000);
            session.connect();

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


            Looper.prepare();
            new AlertDialog.Builder(
                    activity)
                    .setTitle("提示")
                    .setMessage("操作成功！")
                    .setPositiveButton("确定", null)
                    .show();
            Looper.loop();


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
    public void topSSH(String user,String password,String host,String command,Handler mhandler) {
        int port = 22;
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
            session.connect();
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
            Button stop=activity.findViewById(R.id.stop_btn);
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
    public void downloadSSH(String user, String password, String host, String directory,String storage) {
        int port = 22;
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
            session.connect();
            // 2. 通过 sftp 方式执行 shell 命令
            Channel channel= session.openChannel("sftp");
            //channel.setCommand(command);
            channel.connect();
            ChannelSftp sftp=(ChannelSftp) channel;
            File file=new File(storage);
            //将主机上的文件远程下载到手机本地
            sftp.get(directory,new FileOutputStream(file));

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
    public void uploadSSH(String user, String password, String host,String directory, String storage) {
        int port = 22;
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
            session.connect();
            // 2. 通过 sftp 方式执行 shell 命令
            Channel channel= session.openChannel("sftp");
            //channel.setCommand(command);
            channel.connect();
            //使用sftp的channel，到达路径
            //String directory="/root";
            //String storage="/storage/emulated/0/test.txt";
            ChannelSftp sftp=(ChannelSftp) channel;
            //将本地文件上传到服务器上
            sftp.put(storage,directory);

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
}
