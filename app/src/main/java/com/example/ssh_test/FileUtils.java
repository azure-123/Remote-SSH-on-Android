package com.example.ssh_test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Set;

public class FileUtils {
    public void write(String filename) {
        try {
            File urlFile = new File(filename);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(urlFile));
            BufferedWriter br = new BufferedWriter(outputStreamWriter);
            br.write("huahuadan1234");
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void write(String filename,String content) {
        try {
            File urlFile = new File(filename);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(urlFile));
            BufferedWriter br = new BufferedWriter(outputStreamWriter);
            br.write(content);
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String read(String filename) throws IOException {
        String str="";
        try {
            //打开文件输入流
            File urlFile = new File(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(urlFile));
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line = null;
            //定义字符串变量
            //读取文件内容，当文件内容长度大于0时，
            while((line=br.readLine())!=null){
                str=str+line+"\n";
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return str;
    }

    public String setSelinux(String content, String sType) {// 设置selinux防护的类型
        // /etc/selinux/config
        content = content.replaceAll("SELINUX=\\w*", "SELINUX=" + sType);
        return content;
    }

    public String setMysqlConf(String content, Map<String, String> mysqlConf) {// 设置messgaes日志文件保存等级,保存的大于等于level
        // /etc/my.cnf
        Set<String> keys = mysqlConf.keySet();
        for (String key : keys) {
            String value = mysqlConf.get(key); // 获取键的值
            // System.out.println(key + "..." + value);
            if (content.indexOf(key) != -1)// 原本就有该设置
            {
                content = content.replaceAll(key + "\\s*=\\s*\\w", key + " = " + value);
            } else {
                content = content.replaceAll("\\[mysqld\\]\n", "[mysqld]\n" + key + " = " + value + "\n");
                System.out.println("+++++\n");
            }
        }
        return content;
    }

    public String setLogSaveLevel(String content, String level) {// 设置messgaes日志文件保存等级,保存的大于等于level
        // /etc/rsyslog.conf
        content = content.replaceAll("\\*\\..*?;mail.none;authpriv.none;cron.none",
                "*." + level + ";mail.none;authpriv.none;cron.none");
        return content;
    }

    public String setLogSaveTime(String content, String time) {// 设置日志文件保存时间,周为单位,还需要systemctl restart rsyslog重启服务
        // /etc/logrotate.conf
        content = content.replaceAll("rotate \\d\n", "rotate " + time + "\n");
        return content;
    }

    public String setUserLimit(String content, Map<String, String> userLimitDict,
                                     boolean isGroup) {// 设置用户的配置文件 /etccuritymits.conf
        String temp;
        if (!isGroup) {
            Set<String> keys = userLimitDict.keySet();
            for (String key : keys) {
                String value = userLimitDict.get(key); // 获取键的值
                // System.out.println(key + "..." + value);
                if (userLimitDict.get(key) != "" && userLimitDict.get(key) != "name") {
                    temp = userLimitDict.get("name") + "\\s*\\w*?\\s*" + key + "\\d*";// 去除之前的
                    content = content.replaceAll(temp, "");
                    content = content + "\n" + userLimitDict.get("name") + " hard " + key + " " + value;
                }
            }
        } else {
            Set<String> keys = userLimitDict.keySet();
            for (String key : keys) {
                String value = userLimitDict.get(key); // 获取键的值
                // System.out.println(key + "..." + value);
                if (userLimitDict.get(key) != "" && userLimitDict.get(key) != "name") {
                    temp = userLimitDict.get("name") + "\\s*" + key + "\\d*";
                    content = content.replaceAll(temp, "");
                    content = content + "\n@" + userLimitDict.get("name") + " hard " + key + " " + value;
                }
            }
        }
        return content;
    }

    public String setNeedUserLimit(String content) {// 设置是否需要配置限制文件 /etc/pam.d/sshd
        content = content.replace("\nsession    required     pam_limits.so", "");
        content = content + "\nsession    required     pam_limits.so";
        return content;
    }

    public String setUserPasswdRule(String content, Map<String, String> passwdRule) {// 设置用户设置密码的基本规则
        // /etc/pam.d/system-auth
        Set<String> keys = passwdRule.keySet();
        for (String key : keys) {
            String value = passwdRule.get(key); // 获取键的值
            // System.out.println(key + "..." + value);
            if (content.indexOf(key) != -1)// 原本就有该设置
            {
                content = content.replaceAll(key + "\\s*=\\s*\\w", key + " = " + value);
            } else {
                content = content.replaceAll("password    sufficient    pam_unix.so ",
                        "password    sufficient    pam_unix.so " + key + "=" + value + " ");
            }
        }
        return content;
    }

    public String changUsertoRoot(String content, String name) {// 给予用户root权限 /etc/passwd
        String temp = name + ":x:" + "\\d*:\\d*";
        content = content.replaceFirst(temp, name + ":x:0:0");
        return content;
    }

}
