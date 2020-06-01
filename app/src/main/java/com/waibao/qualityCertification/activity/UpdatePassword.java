package com.waibao.qualityCertification.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.waibao.qualityCertification.R;
import com.waibao.qualityCertification.base.BaseActivity;
import com.waibao.qualityCertification.base.BaseAsyTask;
import com.waibao.qualityCertification.util.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class UpdatePassword extends BaseActivity {
    private Toolbar toolbar;
    private EditText passwd;
    private EditText passwd1;
    private String passwdStr;
    private String passwd1Str;
    private Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        initToolbar();
        passwd = (EditText) findViewById(R.id.passwd);
        passwd1 = (EditText) findViewById(R.id.passwd1);
        updateButton = (Button) findViewById(R.id.update);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwdStr = passwd.getText().toString().trim();
                passwd1Str = passwd1.getText().toString().trim();
                if (TextUtils.isEmpty(passwdStr) || TextUtils.isEmpty(passwd1Str)) {
                    UiUtils.show("不能为空");
                } else {
                    new UpdatePasswordTask(UpdatePassword.this,
                            "UpdatePasswordTask", passwdStr, passwd1Str).execute();
                }
            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_update_pass);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    public class UpdatePasswordTask extends BaseAsyTask {
        private String status = "-500";
        private String msg = "success";

        public UpdatePasswordTask(Context context, String string, String... params) {
            super(context, string, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "0")) {
                UiUtils.show("密码修改成功");
                Intent intent = new Intent(UpdatePassword.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else if (TextUtils.equals(s, "500")) {
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