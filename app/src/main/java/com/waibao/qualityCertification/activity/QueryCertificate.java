package com.waibao.qualityCertification.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.waibao.qualityCertification.R;
import com.waibao.qualityCertification.base.BaseActivity;
import com.waibao.qualityCertification.base.BaseAsyTask;
import com.waibao.qualityCertification.util.GridViewUtils;
import com.waibao.qualityCertification.util.UiUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

// 证书查询
public class QueryCertificate extends BaseActivity {
    private Toolbar toolbar;
    private Spinner query_certificate_spinner;
    private GridView query_certificate_grid1;
    private GridView query_certificate_grid2;
    private LinearLayout query_certificate_linear;
    // 综合查询
    private ArrayList<String> mulQueryListData = new ArrayList<String>();
    // 条件查询
    private ArrayList<String> conListData = new ArrayList<String>();
    private ArrayAdapter<String> mulQueryListAdapter;
    private ArrayAdapter<String> conQueryListAdapter;
    private int position = 0;
    private String userToken = "";
    private String userSession = "";
    private ArrayList<String> nameArr = new ArrayList<>();
    private JSONArray tempJsonArray = null;
    private JSONObject tempJSONObject = null;
    private Button query_certificate_Btn;
    private EditText query_certificate_num_Edt;
    private EditText query_certificate_unitID_Edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_certificate);
        initToolbar();
        initView();
    }

    private void initView() {
        query_certificate_spinner = (Spinner) findViewById(R.id.query_certificate_spinner);
        query_certificate_grid1 = (GridView) findViewById(R.id.query_certificate_grid1);
        query_certificate_grid2 = (GridView) findViewById(R.id.query_certificate_grid2);
        query_certificate_linear = (LinearLayout) findViewById(R.id.query_certificate_linear);
        query_certificate_Btn = (Button) findViewById(R.id.query_certificate_Btn);
        query_certificate_num_Edt = (EditText) findViewById(R.id.query_certificate_num_Edt);
        query_certificate_unitID_Edt = (EditText) findViewById(R.id.query_certificate_unitID_Edt);
        if (sharedPreference != null) {
            userSession = sharedPreference.getString("session", "");
        }
        mulQueryListAdapter = new ArrayAdapter<String>(QueryCertificate.this, android.R.layout.simple_list_item_1, mulQueryListData) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // Return the GridView current item as a View
                View view = super.getView(position, convertView, parent);
                return GridViewUtils.getView(QueryCertificate.this, view, mulQueryListData, position);
            }
        };
        conQueryListAdapter = new ArrayAdapter<String>(QueryCertificate.this, android.R.layout.simple_list_item_1, conListData) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // Return the GridView current item as a View
                View view = super.getView(position, convertView, parent);
                return GridViewUtils.getView(QueryCertificate.this, view, conListData, position);
            }
        };
        query_certificate_grid1.setAdapter(mulQueryListAdapter);
        query_certificate_grid2.setAdapter(conQueryListAdapter);
        query_certificate_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                query_certificate_grid1.setVisibility(View.GONE);
                query_certificate_linear.setVerticalGravity(View.GONE);
                position = i;

                new UserTokenTask(QueryCertificate.this,
                        "UserTokenTask", "Admin", "Org1").execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_query_certificate);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void initArrayList(ArrayList<String> temp, String... parmas) {
        temp.clear();
        for (int i = 0; i < parmas.length; ++i) {
            temp.add(parmas[i]);
        }
    }

    public class QueryAllCertsTask extends BaseAsyTask {
        private String status = "true";

        public QueryAllCertsTask(Context context, String string, String... params) {
            super(context, string, params);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                if (okHttpClient != null) {
                    response = okHttpClient.newCall(request).execute();
                    string = response.body().string();
                    if (position == 0) {
                        jsonArray = new JSONArray(string);
                        tempJsonArray = jsonArray;
                    } else {
                        jsonObject = new JSONObject(string);
                        tempJSONObject = jsonObject;
                    }
                }
            } catch (Exception e) {
                status = "false";
                e.printStackTrace();
            }
            return status;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "true")) {
                if (!TextUtils.isEmpty(userToken) && !TextUtils.isEmpty(userSession)) {
                    new GetUnitUserTask(QueryCertificate.this,
                            "GetUnitUserTask", userSession, userToken).execute();
                } else {
                    UiUtils.show("出现未知错误");
                }
            } else {
                UiUtils.show(getString(R.string.netWorkError));
            }
        }
    }

    public class GetUnitUserTask extends BaseAsyTask {
        private String status = "-200";
        private String msg = "";
        String type = "-1";
        String loginname = "";

        public GetUnitUserTask(Context context, String string, String... params) {
            super(context, string, params);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                if (okHttpClient != null) {
                    response = okHttpClient.newCall(request).execute();
                    string = response.body().string();
                    if (string.contains("\"status\":500")) {
                        status = "401";
                        return status;
                    }
                    jsonObject = new JSONObject(string);
                    status = jsonObject.optString("code");
                    msg = jsonObject.optString("msg");
                    jsonArray = jsonObject.getJSONArray("nameList");
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        nameArr.add(jsonArray.getJSONObject(i).optString("idCard").split(";")[1]);
                    }
                    if (sharedPreference != null) {
                        type = sharedPreference.getString("unittype", "-1");
                        loginname = sharedPreference.getString("loginname", "");
                    }
                    if (position == 0) {
                        // 综合查询
                        for (int i = 0; i < tempJsonArray.length(); ++i) {
                            JSONObject tempJsonObject = null;
                            tempJsonObject = tempJsonArray.getJSONObject(i);
                            String xdata = "";
                            String pID = "";
                            String pName = "";
                            JSONObject vobj = null;
                            JSONObject recordJsonObject = tempJsonObject.getJSONObject("Record");
                            String vobjJson = null;
                            if (!TextUtils.equals(recordJsonObject.optString("testDataUpload"), "null") && TextUtils.equals(type, "2")) {
                                vobjJson = recordJsonObject.getJSONObject("testDataUpload").getString("baseData");
                            }
                            if (!TextUtils.equals(recordJsonObject.optString("trialRunDataUpload"), "null") && TextUtils.equals(type, "2")) {
                                vobjJson = recordJsonObject.getJSONObject("trialRunDataUpload").getString("baseData");

                            }
                            if (!TextUtils.equals(recordJsonObject.optString("certUpload"), "null") && TextUtils.equals(type, "1")) {
                                vobjJson = recordJsonObject.getJSONObject("certUpload").getString("baseData");
                            }
                            if (vobjJson != null) {
                                vobj = new JSONObject(vobjJson);
                                xdata = vobj.optString("unitName");
                                pID = vobj.optString("postPersonID");
                                pName = vobj.optString("postPersonName");
                            }
                            int sign = 0;
                            for (int j = 0; j < nameArr.size(); ++j) {
                                if (TextUtils.equals(nameArr.get(j), pName)) {
                                    sign = 1;
                                    break;
                                }
                            }
                            if (sign == 1 || TextUtils.equals(loginname, "admin1") || TextUtils.equals(loginname, "admin2") || TextUtils.equals(loginname, "admin3")) {
                                mulQueryListData.add(tempJsonObject.optString("Key").split(",")[0]);
                                mulQueryListData.add(tempJsonObject.optString("Key").split(",")[1]);
                                mulQueryListData.add(TextUtils.isEmpty(xdata) ? "未找到" : xdata);
                                mulQueryListData.add(TextUtils.isEmpty(pID) ? "未找到" : pID);
                                mulQueryListData.add(TextUtils.isEmpty(pName) ? "未找到" : pName);
                            }
                        }
                    } else {
                        conListData.clear();
                        //条件查询
                        String pName = "";
                        JSONObject vobj = null;
                        String vobjJson = null;
                        // 代码中根本没检验type
//                        if (!TextUtils.equals(tempJSONObject.optString("testDataUpload"), "null") && TextUtils.equals(type, "2")) {
                        if (!TextUtils.equals(tempJSONObject.optString("testDataUpload"), "null")) {
                            vobjJson = tempJSONObject.getJSONObject("testDataUpload").getString("baseData");
                        }
                        if (!TextUtils.equals(tempJSONObject.optString("trialRunDataUpload"), "null")) {
                            vobjJson = tempJSONObject.getJSONObject("trialRunDataUpload").getString("baseData");

                        }
                        if (!TextUtils.equals(tempJSONObject.optString("certUpload"), "null")) {
                            vobjJson = tempJSONObject.getJSONObject("certUpload").getString("baseData");
                        }
                        if (vobjJson != null) {
                            vobj = new JSONObject(vobjJson);
                            pName = vobj.optString("postPersonName");
                        }
                        int sign = 0;
                        for (int j = 0; j < nameArr.size(); ++j) {
                            if (TextUtils.equals(nameArr.get(j), pName)) {
                                sign = 1;
                                break;
                            }
                        }
                        if (sign == 1 || TextUtils.equals(loginname, "admin1") || TextUtils.equals(loginname, "admin2") || TextUtils.equals(loginname, "admin3")) {
                            if (!TextUtils.equals(tempJSONObject.optString("certUpload"), "null")) {
                                conListData.add("证书编号");
                                conListData.add(tempJSONObject.optString("certificateID"));
                                conListData.add("获证企业单位编号");
                                conListData.add(tempJSONObject.optString("unitID"));
                                conListData.add("获证企业单位名称");
                                conListData.add(TextUtils.isEmpty(vobj.optString("unitName")) ? "未找到" : vobj.optString("unitName"));
                                conListData.add("注册地址");
                                conListData.add(TextUtils.isEmpty(vobj.optString("registerAddr")) ? "未找到" : vobj.optString("registerAddr"));
                                conListData.add("交易平台名称");
                                conListData.add(TextUtils.isEmpty(vobj.optString("platformName")) ? "未找到" : vobj.optString("platformName"));
                                conListData.add("交易平台版本");
                                conListData.add(TextUtils.isEmpty(vobj.optString("edition")) ? "未找到" : vobj.optString("edition"));
                                conListData.add("网址");
                                conListData.add(TextUtils.isEmpty(vobj.optString("website")) ? "未找到" : vobj.optString("website"));
                                conListData.add("受审核地址");
                                conListData.add(TextUtils.isEmpty(vobj.optString("auditAddr")) ? "未找到" : vobj.optString("auditAddr"));
                                conListData.add("标准和技术要求");
                                conListData.add(TextUtils.isEmpty(vobj.optString("authenticationStandard")) ? "未找到" : vobj.optString("authenticationStandard"));
                                conListData.add("认证模式");
                                conListData.add(TextUtils.isEmpty(vobj.optString("certificationMode")) ? "未找到" : vobj.optString("certificationMode"));
                                conListData.add("认证级别");
                                conListData.add(TextUtils.isEmpty(vobj.optString("certificationClass")) ? "未找到" : vobj.optString("certificationClass"));
                                conListData.add("认证结论");
                                conListData.add(TextUtils.isEmpty(vobj.optString("certificationConclusion")) ? "未找到" : vobj.optString("certificationConclusion"));
                                conListData.add("颁证日期");
                                conListData.add(TextUtils.isEmpty(vobj.optString("awardDate")) ? "未找到" : vobj.optString("awardDate"));
                                conListData.add("换证日期");
                                conListData.add(TextUtils.isEmpty(vobj.optString("replaceDate")) ? "未找到" : vobj.optString("replaceDate"));
                                conListData.add("有效期至");
                                conListData.add(TextUtils.isEmpty(vobj.optString("validityTerm")) ? "未找到" : vobj.optString("validityTerm"));
                                conListData.add("认证机构编号");
                                conListData.add(TextUtils.isEmpty(vobj.optString("certificationID")) ? "未找到" : vobj.optString("certificationID"));
                                conListData.add("认证机构名称");
                                conListData.add(TextUtils.isEmpty(vobj.optString("certificationName")) ? "未找到" : vobj.optString("certificationName"));
                                conListData.add("提交人");
                                conListData.add(TextUtils.isEmpty(vobj.optString("postPersonName")) ? "未找到" : vobj.optString("postPersonName"));
                            } else if (!TextUtils.equals(tempJSONObject.optString("testDataUpload"), "null")) {
                                conListData.add("委托人单位编号");
                                conListData.add(tempJSONObject.optString("unitID"));
                                conListData.add("委托人单位名称");
                                conListData.add(TextUtils.isEmpty(vobj.optString("unitName")) ? "未找到" : vobj.optString("unitName"));
                                conListData.add("交易平台名称");
                                conListData.add(TextUtils.isEmpty(vobj.optString("platformName")) ? "未找到" : vobj.optString("platformName"));
                                conListData.add("交易平台版本");
                                conListData.add(TextUtils.isEmpty(vobj.optString("edition")) ? "未找到" : vobj.optString("edition"));
                                conListData.add("检测机构名称");
                                conListData.add(TextUtils.isEmpty(vobj.optString("testUnitName")) ? "未找到" : vobj.optString("testUnitName"));
                                conListData.add("检测报告结论");
                                conListData.add(TextUtils.isEmpty(vobj.optString("conclusion")) ? "未找到" : vobj.optString("conclusion"));
                                conListData.add("检测时间");
                                conListData.add(TextUtils.isEmpty(vobj.optString("testTime")) ? "未找到" : vobj.optString("testTime"));
                                conListData.add("检测人证件号码");
                                conListData.add(TextUtils.isEmpty(vobj.optString("testingPersonID")) ? "未找到" : vobj.optString("testingPersonID"));
                                conListData.add("检测人姓名");
                                conListData.add(TextUtils.isEmpty(vobj.optString("testingPersonName")) ? "未找到" : vobj.optString("testingPersonName"));
                                conListData.add("数据上传人证件号码");
                                conListData.add(TextUtils.isEmpty(vobj.optString("postPersonID")) ? "未找到" : vobj.optString("postPersonID"));
                                conListData.add("数据上传人证件姓名");
                                conListData.add(TextUtils.isEmpty(vobj.optString("postPersonName")) ? "未找到" : vobj.optString("postPersonName"));
                            } else if (!TextUtils.equals(tempJSONObject.optString("trialRunDataUpload"), "null")) {
                                conListData.add("证书编号");
                                conListData.add(tempJSONObject.optString("certificateID"));
                                conListData.add("委托人单位编号");
                                conListData.add(tempJSONObject.optString("unitID"));
                                conListData.add("委托人单位名称");
                                conListData.add(TextUtils.isEmpty(vobj.optString("unitName")) ? "未找到" : vobj.optString("unitName"));
                                conListData.add("交易平台名称");
                                conListData.add(TextUtils.isEmpty(vobj.optString("platformName")) ? "未找到" : vobj.optString("platformName"));
                                conListData.add("交易平台版本");
                                conListData.add(TextUtils.isEmpty(vobj.optString("edition")) ? "未找到" : vobj.optString("edition"));
                                conListData.add("检测机构编号");
                                conListData.add(TextUtils.isEmpty(vobj.optString("testUnitName")) ? "未找到" : vobj.optString("testUnitName"));
                                conListData.add("检测机构名称");
                                conListData.add(TextUtils.isEmpty(vobj.optString("testUnitName")) ? "未找到" : vobj.optString("testUnitName"));
                                conListData.add("检测报告结论");
                                conListData.add(TextUtils.isEmpty(vobj.optString("conclusion")) ? "未找到" : vobj.optString("conclusion"));
                                conListData.add("试运行时间");
                                conListData.add(TextUtils.isEmpty(vobj.optString("testTime")) ? "未找到" : vobj.optString("testTime"));
                                conListData.add("检测人证件号码");
                                conListData.add(TextUtils.isEmpty(vobj.optString("testingPersonID")) ? "未找到" : vobj.optString("testingPersonID"));
                                conListData.add("检测人姓名");
                                conListData.add(TextUtils.isEmpty(vobj.optString("testingPersonName")) ? "未找到" : vobj.optString("testingPersonName"));
                                conListData.add("数据上传人证件号码");
                                conListData.add(TextUtils.isEmpty(vobj.optString("postPersonID")) ? "未找到" : vobj.optString("postPersonID"));
                                conListData.add("数据上传人证件姓名");
                                conListData.add(TextUtils.isEmpty(vobj.optString("postPersonName")) ? "未找到" : vobj.optString("postPersonName"));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return status;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "0")) {
                if (position == 0) {
                    mulQueryListAdapter.notifyDataSetChanged();
                } else {
                    conQueryListAdapter.notifyDataSetChanged();
                }
            } else if (TextUtils.equals(s, "500")) {
                UiUtils.show(msg);
            } else if (TextUtils.equals(s, "401")) {
                UiUtils.show("登陆信息已过期，请重新登陆。");
                if (editor != null) {
                    editor.clear();
                    editor.apply();
                }
                startActivity(new Intent(QueryCertificate.this, LoginActivity.class));
                finish();
            } else {
                UiUtils.show(getString(R.string.netWorkError));
            }
        }
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
                if (!TextUtils.isEmpty(token)) {
                    if (position == 0) {
                        initArrayList(mulQueryListData, "证书编号", "营业执照编号", "获证企业名称", "提交人身份证号", "提交人姓名");
                        query_certificate_grid1.setVisibility(View.VISIBLE);
                        new QueryAllCertsTask(QueryCertificate.this,
                                "QueryAllCertsTask", "?peer=peer0.org1.example.com&fcn=queryAllCerts&args=['']", token).execute();
                    } else {
                        query_certificate_linear.setVisibility(View.VISIBLE);
                        query_certificate_Btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String query_certificate_num_Str = query_certificate_num_Edt.getText().toString().trim();
                                String query_certificate_unitID_Str = query_certificate_unitID_Edt.getText().toString().trim();
                                if (!TextUtils.isEmpty(query_certificate_num_Str) && !TextUtils.isEmpty(query_certificate_unitID_Str)) {
                                    new QueryAllCertsTask(QueryCertificate.this,
                                            "QueryAllCertsTask",
                                            "?peer=peer0.org1.example.com&fcn=queryCert&args=[\"" + query_certificate_num_Str + "\",\"" + query_certificate_unitID_Str + "\"]", token).execute();
                                } else {
                                    UiUtils.show("输入不能为空");

                                }
                            }
                        });
                    }
                } else {
                    UiUtils.show("出现未知错误");
                }
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
                    userToken = token;
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