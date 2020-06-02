package com.waibao.qualityCertification.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.waibao.qualityCertification.R;
import com.waibao.qualityCertification.base.BaseActivity;
import com.waibao.qualityCertification.base.BaseAsyTask;
import com.waibao.qualityCertification.util.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * 用于实现用户登录的功能
 */
public class LoginActivity extends BaseActivity {
    String username;
    String password;
    TextView registerbutton;
    TextView reset;
    TextView publicSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText loginusername = (EditText) findViewById(R.id.username);
        final EditText loginpassword = (EditText) findViewById(R.id.password);
        final Button loginbutton = (Button) findViewById(R.id.loginbutton);
        registerbutton = (TextView) findViewById(R.id.registerbutton);
        reset = (TextView) findViewById(R.id.reset);
        publicSearch = (TextView) findViewById(R.id.publicSearch);
        //用户登录
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = loginusername.getText().toString();
                password = loginpassword.getText().toString();
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                    // 登陆
                    new LoginTask(LoginActivity.this,
                            "LoginTask", username, password).execute();
                } else {
                    UiUtils.show("用户名或者密码不能为空");
                }
            }
        });
        //跳转到用户注册界面
        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        //跳转到重置密码界面
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RecoverPasswordActivity.class);
                startActivity(intent);
            }
        });
        publicSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, PublicSearch.class);
                startActivity(intent);
            }
        });
    }

    public class LoginTask extends BaseAsyTask {
        private String status = "-500";
        private String msg = "success";
        private String session;

        public LoginTask(Context context, String string, String... params) {
            super(context, string, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "0")) {
                UiUtils.show(getString(R.string.loginSuccess));
                String[] msgStr = msg.split(",");
                editor.putString("loginname", username);
                editor.putString("usertype", msgStr[0]);
                if (msgStr[1] != null) {
                    editor.putString("token", msgStr[1]);
                }
                editor.putString("unittype", msgStr[2]);
//                editor.putString("usertype", msgStr[3]);
                editor.putString("transaction", msgStr[4]);
                editor.putString("username", msgStr[5]);
                editor.putString("userid", msgStr[6]);
                editor.putString("session", session);
                editor.apply();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else if (TextUtils.equals(s, "500")) {
                UiUtils.show(getString(R.string.userNotFound));
            } else if (TextUtils.equals(s, "-200")) {
                UiUtils.show(getString(R.string.userOrPassError));
            } else if (TextUtils.equals(s, "-2")) {
                UiUtils.show(msg);
            } else if (TextUtils.equals(s, "-1")) {
                UiUtils.show(msg);
            } else {
                UiUtils.show(getString(R.string.netWorkError));
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                if (okHttpClient != null) {
                    response = okHttpClient.newCall(request).execute();
                    string = response.body().string();
                    jsonObject = new JSONObject(string);
                    status = jsonObject.optString("code");
                    msg = jsonObject.optString("msg");
                    if (TextUtils.equals(status, "0")) {
                        String sessionTemp = response.headers().values("Set-Cookie").get(0);
                        session = sessionTemp.substring(0, sessionTemp.indexOf(";"));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return status;
        }
    }
}