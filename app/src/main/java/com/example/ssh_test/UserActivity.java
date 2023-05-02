package com.example.ssh_test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.ThemedSpinnerAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToDoubleBiFunction;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        String _user="root";
        String _password="huahuadan1234";
        String _host="47.102.114.76";

        Intent intent=getIntent();
        String user=intent.getStringExtra("user");
        String password=intent.getStringExtra("password");
        String host=intent.getStringExtra("host");

        Button showUsergroupButton=findViewById(R.id.show_usergroup_btn);//查看所有用户组
        Button addGroupButton=findViewById(R.id.add_group_btn);//添加用户组按钮
        Button deleteGroupButton=findViewById(R.id.del_group_btn);//添加用户组按钮
        Button showUserButton=findViewById(R.id.show_user_btn);//查看所有用户组
        Button addUserButton=findViewById(R.id.add_user_btn);//添加用户
        Button changeUserPasswordButton=findViewById(R.id.change_user_passwd_btn);//修改用户密码
        Button changeUserGroupButton=findViewById(R.id.change_user_group_btn);//修改用户所在用户组
        Button lockUserButton=findViewById(R.id.lock_user_btn);//锁定用户
        Button unlockUserButton=findViewById(R.id.unlock_user_btn);//启用用户
        Button deleteUserButton=findViewById(R.id.del_user_btn);//删除用户
        Button rootUserButton=findViewById(R.id.root_user_btn);//赋予用户管理员权限
        Button passwordRuleButton=findViewById(R.id.passwd_rule_btn);//普通用户修改密码规范
        Button showUserLimitButton=findViewById(R.id.show_user_limit_btn);//查看用户限制
        Button limitButton=findViewById(R.id.limit_btn);//修改用户限制

        //查看所有用户组监听事件
        showUsergroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler mhandler=new mHandler(UserActivity.this,R.id.user_group_tv);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(UserActivity.this);
                        sshUtils.connectSSH(user,password,host,"cat /etc/group",mhandler);
                    }
                }).start();
            }
        });
        //添加用户组按钮监听事件
        addGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler mhandler=new mHandler(UserActivity.this,0);
                EditText addGroupEditText=findViewById(R.id.add_group_et);
                String userGroup=addGroupEditText.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(UserActivity.this);
                        sshUtils.connectSSH(user,password,host,"groupadd "+userGroup,mhandler);
                    }
                }).start();
            }
        });
        //删除用户组按钮监听事件
        deleteGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler mhandler=new mHandler(UserActivity.this,0);
                EditText deleteGroupEditText=findViewById(R.id.del_group_et);
                String userGroup=deleteGroupEditText.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(UserActivity.this);
                        sshUtils.connectSSH(user,password,host,"groupdel "+userGroup,mhandler);
                    }
                }).start();
            }
        });
        //查看所有用户监听事件
        showUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler mhandler=new mHandler(UserActivity.this,R.id.user_tv);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(UserActivity.this);
                        sshUtils.connectSSH(user,password,host,"cat /etc/passwd",mhandler);
                    }
                }).start();
            }
        });
        //添加用户监听事件
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText addUserEditText=findViewById(R.id.add_user_et);
                EditText addUserGroupEditText=findViewById(R.id.add_user_group_et);
                EditText addUserDirEditText=findViewById(R.id.add_user_pwd_et);

                String userName=addUserEditText.getText().toString();
                String userGroup=addUserGroupEditText.getText().toString();
                String userDir=addUserDirEditText.getText().toString();
                Handler mhandler=new mHandler(UserActivity.this,0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(UserActivity.this);
                        sshUtils.connectSSH(user,password,host,"useradd --gid "+userGroup+" --home-dir "+userDir+" "+userName,mhandler);
                    }
                }).start();
            }
        });
        //修改用户密码
        changeUserPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userNameEditText=findViewById(R.id.change_user_passwd_name);
                EditText userPasswordEditText=findViewById(R.id.new_passwd);

                String userName=userNameEditText.getText().toString();
                String userPassword=userPasswordEditText.getText().toString();
                Handler mhandler=new mHandler(UserActivity.this,0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(UserActivity.this);
                        sshUtils.connectSSH(user,password,host,"echo \""+userPassword+"\" | passwd --stdin "+userName,mhandler);
                    }
                }).start();
            }
        });
        //修改用户所在用户组
        changeUserGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userNameEditText=findViewById(R.id.change_user_group_name);
                EditText userGroupEditText=findViewById(R.id.new_user_group);

                String userName=userNameEditText.getText().toString();
                String userGroup=userGroupEditText.getText().toString();
                Handler mhandler=new mHandler(UserActivity.this,0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(UserActivity.this);
                        sshUtils.connectSSH(user,password,host,"usermod -G "+userGroup+" "+userName,mhandler);
                    }
                }).start();
            }
        });
        //锁定用户事件监听
        lockUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText lockUserName=findViewById(R.id.lock_user_name);

                String userName=lockUserName.getText().toString();
                Handler mhandler=new mHandler(UserActivity.this,0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(UserActivity.this);
                        sshUtils.connectSSH(user,password,host,"passwd -l "+userName,mhandler);
                    }
                }).start();
            }
        });
        //启用用户事件监听
        unlockUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText lockUserName=findViewById(R.id.unlock_user_name);

                String userName=lockUserName.getText().toString();
                Handler mhandler=new mHandler(UserActivity.this,0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(UserActivity.this);
                        sshUtils.connectSSH(user,password,host,"passwd -u "+userName,mhandler);
                    }
                }).start();
            }
        });
        //删除用户事件监听
        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText deleteUserName=findViewById(R.id.del_user_name);

                String userName=deleteUserName.getText().toString();
                Handler mhandler=new mHandler(UserActivity.this,0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(UserActivity.this);
                        sshUtils.connectSSH(user,password,host,"userdel -r "+userName,mhandler);
                    }
                }).start();
            }
        });
        //赋予用户管理员权限
        rootUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText userNameEditText=findViewById(R.id.root_user_name);

                String userName=userNameEditText.getText().toString();
                //构造map
                Map<String,String> stringStringMap=new HashMap<>();
                stringStringMap.put("name",userName);
                Handler mhandler=new mHandler(UserActivity.this,0);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(UserActivity.this);
                        sshUtils.sftpSSH(user,password,host,"/etc/passwd",stringStringMap,8);
                        //sshUtils.connectSSH(user,password,host,"userdel -r "+userName,mhandler);
                    }
                }).start();
            }
        });
        //普通用户修改密码规范
        passwordRuleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText minPasswordLengthEditText=findViewById(R.id.min_passwd_len);
                EditText minPasswordDLengthEditText=findViewById(R.id.min_passwd_D_len);
                EditText minPasswordULengthEditText=findViewById(R.id.min_passwd_U_len);
                EditText minPasswordLLengthEditText=findViewById(R.id.min_passwd_L_len);
                EditText minPasswordOLengthEditText=findViewById(R.id.min_passwd_O_len);
                EditText minPasswordDiLengthEditText=findViewById(R.id.min_passwd_di_len);
                EditText minPasswordRLengthEditText=findViewById(R.id.min_passwd_R_len);

                String minPasswordLength=minPasswordLengthEditText.getText().toString();
                String minPasswordDLength=minPasswordDLengthEditText.getText().toString();
                String minPasswordULength=minPasswordULengthEditText.getText().toString();
                String minPasswordLLength=minPasswordLLengthEditText.getText().toString();
                String minPasswordOLength=minPasswordOLengthEditText.getText().toString();
                String minPasswordDiLength=minPasswordDiLengthEditText.getText().toString();
                String minPasswordRLength=minPasswordRLengthEditText.getText().toString();

                 Map<String, String> passwdRule = new HashMap<String, String>();
                 passwdRule.put("minlen", minPasswordLength);
                 passwdRule.put("dcredit", minPasswordDLength);
                 passwdRule.put("ucredit", minPasswordULength);
                 passwdRule.put("lcredit", minPasswordLLength);
                 passwdRule.put("ocredit", minPasswordOLength);
                 passwdRule.put("difork", minPasswordDiLength);
                 passwdRule.put("remember", minPasswordRLength);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(UserActivity.this);
                        sshUtils.sftpSSH(user,password,host,"/etc/pam.d/system-auth",passwdRule,7);
                        //sshUtils.connectSSH(user,password,host,"last",mhandler);
                    }
                }).start();
            }
        });
        //查看用户限制
        showUserLimitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler mhandler=new mHandler(UserActivity.this,R.id.user_limit_tv);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(UserActivity.this);
                        sshUtils.connectSSH(user,password,host,"ulimit -a",mhandler);
                    }
                }).start();
            }
        });

        //设置用户限制
        limitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isGroup=false;
                RadioButton isUserRadioButton=findViewById(R.id.is_user_rb);
                RadioButton isGroupRadioButton=findViewById(R.id.is_group_rb);
                EditText limitUserNameEditText=findViewById(R.id.limit_user_name);
                EditText limitAsEditText=findViewById(R.id.limit_as);
                EditText limitNprocEditText=findViewById(R.id.limit_nproc);
                EditText limitNofileEditText=findViewById(R.id.limit_nofile);
                EditText limitCpuEditText=findViewById(R.id.limit_cpu);

                String limitUserName=limitUserNameEditText.getText().toString();
                String limitAs=limitAsEditText.getText().toString();
                String limitNproc=limitNprocEditText.getText().toString();
                String limitNofile=limitNofileEditText.getText().toString();
                String limitCpu=limitCpuEditText.getText().toString();
                if(isUserRadioButton.isChecked())
                {
                    isGroup=true;
                }
                else if(isGroupRadioButton.isChecked())
                {
                    isGroup=false;
                }

                Map<String, String> userLimitDict = new HashMap<String, String>();
                userLimitDict.put("name", limitUserName);// 用户名
                userLimitDict.put("maxlogins", limitAs);// 最大登陆数
                userLimitDict.put("as", limitAs);// 最大内存数
                userLimitDict.put("nproc", limitNproc);// 最大进程数
                userLimitDict.put("nofile", limitNofile);// 最大文件句柄数量
                userLimitDict.put("cpu", limitCpu);// 最大 CPU 使用时间，分钟为单位

                boolean finalIsGroup = isGroup;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(UserActivity.this);
                        sshUtils.sftpSSH(user,password,host,"/etc/pam.d/sshd",userLimitDict,6);
                    }
                });
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SSHUtils sshUtils=new SSHUtils(UserActivity.this);
                        //sshUtils.connectSSH(user,password,host,"ulimit -a",mhandler);
                        if(finalIsGroup)
                        {
                            sshUtils.sftpSSH(user,password,host,"/etc/security/limits.conf",userLimitDict,4);
                        }
                        else
                        {
                            sshUtils.sftpSSH(user,password,host,"/etc/security/limits.conf",userLimitDict,5);
                        }
                    }
                }).start();
            }
        });

    }
}