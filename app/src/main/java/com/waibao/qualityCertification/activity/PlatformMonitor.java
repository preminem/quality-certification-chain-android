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
import android.widget.TextView;

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

// 平台监控
public class PlatformMonitor extends BaseActivity {
    private Toolbar toolbar;
    private Spinner platform_monitor = null;
    private int position = 0;
    private LinearLayout status_monitor;
    private TextView status_monitor_num;
    private TextView status_monitor_hash;
    private TextView status_monitor_hash_len;
    private TextView status_monitor_pre_hash;
    private TextView status_monitor_tran;
    private String status_monitor_num_str;
    private String status_monitor_hash_str;
    private String status_monitor_pre_hash_str;
    private int status_monitor_hash_len_str = 0;
    private int status_monitor_tran_str = 0;

    private LinearLayout block_monitor;
    private EditText blockIDEdt;
    private Button blockIDBtn;
    private TextView block_monitor_num;
    private TextView block_monitor_hash;
    private TextView block_monitor_hash_len;
    private TextView block_monitor_pre_hash;
    private TextView block_monitor_tran;
    private String blockIDStr;
    private String block_monitor_num_str;
    private String block_monitor_hash_str;
    private String block_monitor_pre_hash_str;

    private GridView transaction_monitor_grid_view;
    private ArrayList<String> transactionMonitorData = new ArrayList<String>();
    private ArrayAdapter<String> transactionMonitorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_platform_monitor);
        initToolbar();
        initView();
    }

    private void initView() {
        platform_monitor = (Spinner) findViewById(R.id.platform_monitor);
        status_monitor = (LinearLayout) findViewById(R.id.status_monitor);
        block_monitor = (LinearLayout) findViewById(R.id.block_monitor);
        status_monitor_num = (TextView) findViewById(R.id.status_monitor_hight);
        status_monitor_hash = (TextView) findViewById(R.id.status_monitor_cur_hash);
        status_monitor_hash_len = (TextView) findViewById(R.id.status_monitor_cur_hash_len);
        status_monitor_pre_hash = (TextView) findViewById(R.id.status_monitor_pre_hash);
        status_monitor_tran = (TextView) findViewById(R.id.status_monitor_pre_hash_len);

        blockIDEdt = (EditText) findViewById(R.id.blockIDEdt);
        blockIDBtn = (Button) findViewById(R.id.blockIDBtn);
        block_monitor_num = (TextView) findViewById(R.id.block_monitor_num);
        block_monitor_hash = (TextView) findViewById(R.id.block_monitor_hash);
        block_monitor_hash_len = (TextView) findViewById(R.id.block_monitor_hash_len);
        block_monitor_pre_hash = (TextView) findViewById(R.id.block_monitor_pre_hash);
        block_monitor_tran = (TextView) findViewById(R.id.block_monitor_tran);

        transaction_monitor_grid_view = (GridView) findViewById(R.id.transaction_monitor_grid_view);
        transactionMonitorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, transactionMonitorData) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // Return the GridView current item as a View
                View view = super.getView(position, convertView, parent);
                return GridViewUtils.getView(PlatformMonitor.this, view, transactionMonitorData, position);
            }
        };
        transaction_monitor_grid_view.setAdapter(transactionMonitorAdapter);
        platform_monitor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                status_monitor.setVisibility(View.GONE);
                status_monitor.setVisibility(View.GONE);
                transaction_monitor_grid_view.setVisibility(View.GONE);
                position = pos;
                initArrayList(transactionMonitorData, "Tx id", "数据类型", "上传人姓名", "长传人ID", "是否审核", "审核人姓名", "审核人ID");
                new UserTokenTask(PlatformMonitor.this,
                        "UserTokenTask", "Admin", "Org1").execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_platform_monitor);
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

    // 区块链状态监控
    public class StatusMonitorTask extends BaseAsyTask {
        private String status = "true";

        public StatusMonitorTask(Context context, String string, String... params) {
            super(context, string, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "true")) {
                status_monitor.setVisibility(View.VISIBLE);
                status_monitor_num.setText(status_monitor_num_str);
                status_monitor_hash.setText(status_monitor_hash_str);
                status_monitor_hash_len.setText(String.valueOf(status_monitor_hash_len_str));
                status_monitor_pre_hash.setText(status_monitor_pre_hash_str);
                status_monitor_tran.setText(String.valueOf(status_monitor_tran_str));
            } else if (TextUtils.equals(s, "401")) {
                UiUtils.show("登陆信息已过期，请重新登陆。");
                if (editor != null) {
                    editor.clear();
                    editor.apply();
                }
                startActivity(new Intent(PlatformMonitor.this, LoginActivity.class));
                finish();
            } else {
                UiUtils.show("查询失败请稍后再试");
            }
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
                    status_monitor_num_str = jsonObject.getJSONObject("height").getString("low");
                    status_monitor_hash_str = jsonObject.getJSONObject("currentBlockHash").getJSONObject("buffer").getString("data");
                    status_monitor_hash_len_str = jsonObject.getJSONObject("currentBlockHash").getJSONObject("buffer").getJSONArray("data").length();
                    status_monitor_pre_hash_str = jsonObject.getJSONObject("previousBlockHash").getJSONObject("buffer").getString("data");
                    status_monitor_tran_str = jsonObject.getJSONObject("previousBlockHash").getJSONObject("buffer").getJSONArray("data").length();
                }
            } catch (Exception e) {
                status = "false";
                e.printStackTrace();
            }
            return status;
        }
    }

    // 区块信息监控
    public class BlockMonitorTask extends BaseAsyTask {
        private String status = "true";

        public BlockMonitorTask(Context context, String string, String... params) {
            super(context, string, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "true")) {
                UiUtils.show("查询成功");
                block_monitor_num.setText(block_monitor_num_str);
                block_monitor_hash.setText(block_monitor_hash_str);
                block_monitor_hash_len.setText(String.valueOf(block_monitor_hash_str.length()));
                block_monitor_pre_hash.setText(block_monitor_pre_hash_str);
                block_monitor_tran.setText("null");
            } else if (TextUtils.equals(s, "401")) {
                UiUtils.show("登陆信息已过期，请重新登陆。");
                if (editor != null) {
                    editor.clear();
                    editor.apply();
                }
                startActivity(new Intent(PlatformMonitor.this, LoginActivity.class));
                finish();
            } else {
                UiUtils.show("查询失败请稍后再试");
            }
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
                    block_monitor_num_str = jsonObject.getJSONObject("header").getString("number");
                    block_monitor_hash_str = jsonObject.getJSONObject("header").getString("data_hash");
                    block_monitor_pre_hash_str = jsonObject.getJSONObject("header").getString("previous_hash");
                }
            } catch (Exception e) {
                status = "false";
                e.printStackTrace();
            }
            return status;
        }
    }

    // 交易监控
    public class TransactionMonitorTask extends BaseAsyTask {
        private String status = "true";

        public TransactionMonitorTask(Context context, String string, String... params) {
            super(context, string, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "true")) {
                UiUtils.show("查询成功");
                transactionMonitorAdapter.notifyDataSetChanged();
            } else if (TextUtils.equals(s, "401")) {
                UiUtils.show("登陆信息已过期，请重新登陆。");
                if (editor != null) {
                    editor.clear();
                    editor.apply();
                }
                startActivity(new Intent(PlatformMonitor.this, LoginActivity.class));
                finish();
            } else {
                UiUtils.show("查询失败请稍后再试");
            }
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
                    jsonArray = new JSONArray(string);
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        transactionMonitorData.add(jsonObject.getString("trancid"));
                        transactionMonitorData.add(jsonObject.getString("temp3"));
                        transactionMonitorData.add(jsonObject.getString("uploadman"));
                        transactionMonitorData.add(jsonObject.getString("uploadmanid"));
                        transactionMonitorData.add(jsonObject.getString("ischecked"));
                        transactionMonitorData.add(jsonObject.getString("checkman"));
                        transactionMonitorData.add(jsonObject.getString("checkmanid"));
                    }
                }
            } catch (Exception e) {
                status = "false";
                e.printStackTrace();
            }
            return status;
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
                        block_monitor.setVisibility(View.GONE);
                        new StatusMonitorTask(PlatformMonitor.this,
                                "StatusMonitorTask", "?peer=peer0.org1.example.com", token).execute();
                    } else if (position == 1) {
                        block_monitor.setVisibility(View.VISIBLE);
                        blockIDBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                blockIDStr = blockIDEdt.getText().toString().trim();
                                if (!TextUtils.isEmpty(blockIDStr)) {
                                    new BlockMonitorTask(PlatformMonitor.this,
                                            "BlockMonitorTask", "?peer=peer0.org2.example.com", token, blockIDStr).execute();
                                } else {
                                    UiUtils.show("输入不能为空。");
                                }
                            }
                        });
                    } else {
                        block_monitor.setVisibility(View.GONE);
                        transaction_monitor_grid_view.setVisibility(View.VISIBLE);
                        new TransactionMonitorTask(PlatformMonitor.this,
                                "TransactionMonitorTask", token).execute();
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