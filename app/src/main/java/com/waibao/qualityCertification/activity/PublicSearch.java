package com.waibao.qualityCertification.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waibao.qualityCertification.R;
import com.waibao.qualityCertification.base.BaseActivity;
import com.waibao.qualityCertification.base.BaseAsyTask;
import com.waibao.qualityCertification.util.UiUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

// 公众查询
public class PublicSearch extends BaseActivity {
    private EditText certificateId;
    private EditText unitName;
    private EditText platformName;
    private Button queryCerts;
    private TextView certificationClass;
    private TextView validityTerm;
    private LinearLayout certificationClassLine;
    private LinearLayout validityTermLine;
    private String certificateIdStr;
    private String unitNameStr;
    private String platformNameStr;
    private String publicQuery = "";
    private String certificationClassStr;
    private String validityTermStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_search);
        certificateId = (EditText) findViewById(R.id.certificateId);
        unitName = (EditText) findViewById(R.id.unitName);
        platformName = (EditText) findViewById(R.id.platformName);
        queryCerts = (Button) findViewById(R.id.queryCerts);
        certificationClass = (TextView) findViewById(R.id.certificationClass);
        validityTerm = (TextView) findViewById(R.id.validityTerm);
        certificationClassLine = (LinearLayout) findViewById(R.id.certificationClassLine);
        validityTermLine = (LinearLayout) findViewById(R.id.validityTermLine);
        queryCerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                certificateIdStr = certificateId.getText().toString().trim();
                unitNameStr = unitName.getText().toString().trim();
                platformNameStr = platformName.getText().toString().trim();
                publicQuery = "?peer=peer0.org1.example.com&fcn=publicQuery&args=[\"" + certificateIdStr + "\",\"" + unitNameStr + "\",\"" + platformNameStr + "\"]";
                if (TextUtils.isEmpty(certificateIdStr) || TextUtils.isEmpty(unitNameStr) || TextUtils.isEmpty(platformNameStr)) {
                    UiUtils.show("字段不能为空");
                } else {
                    new UserTokenTask(PublicSearch.this,
                            "UserTokenTask", "Admin", "Org1").execute();
                }
            }
        });
    }

    public class UserTokenTask extends BaseAsyTask {
        private String status = "false";

        public UserTokenTask(Context context, String string, String... params) {
            super(context, string, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "true")) {
                new PublicSearchTask(PublicSearch.this,
                        "PublicSearchTask", publicQuery, token).execute();
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
                    status = jsonObject.optString("success");
                    token = jsonObject.optString("token");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return status;
        }
    }

    public class PublicSearchTask extends BaseAsyTask {
        private String status = "true";

        public PublicSearchTask(Context context, String string, String... params) {
            super(context, string, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "true")) {
                UiUtils.show("查询成功");
                certificationClassLine.setVisibility(View.VISIBLE);
                validityTermLine.setVisibility(View.VISIBLE);
                certificationClass.setText(certificationClassStr);
                validityTerm.setText(validityTermStr);
            } else {
                UiUtils.show("查询不到该条信息：" + string);
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                if (okHttpClient != null) {
                    response = okHttpClient.newCall(request).execute();
                    string = response.body().string();
                    jsonObject = new JSONObject(string);
                    certificationClassStr = jsonObject.optString("certificationClass");
                    validityTermStr = jsonObject.optString("validityTerm");
                }
            } catch (Exception e) {
                e.printStackTrace();
                status = "false";
            }
            return status;
        }
    }
}