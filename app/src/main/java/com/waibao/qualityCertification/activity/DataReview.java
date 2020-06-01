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
import android.widget.GridView;
import android.widget.Spinner;

import com.waibao.qualityCertification.R;
import com.waibao.qualityCertification.base.BaseActivity;
import com.waibao.qualityCertification.base.BaseAsyTask;
import com.waibao.qualityCertification.util.GridViewUtils;
import com.waibao.qualityCertification.util.UiUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

// 数据审核
public class DataReview extends BaseActivity {
    private Toolbar toolbar;
    private Spinner data_review_spinner1;
    private Spinner data_review_spinner2;
    private String[][] data = {{"证书申请资料审核", "文件审核资料审核", "现场审核资料审核", "证书数据审核"}, {"检测检验审核", "试运行数据审核"}};
    private ArrayAdapter<String> dataAdapter = null;
    private ArrayAdapter<String> dataGridViewAdapter = null;
    private ArrayList<String> dataList = new ArrayList<>();
    // dataURLList存放所有的链接，用于查看详情
    private ArrayList<Integer> dataURLList = new ArrayList<>();
    private GridView data_review_grid = null;
    private int type = -1;
    private int tempPos1 = 0;
    private int tempPos2 = 0;
    private String unittype;
    private String session;
    private boolean permission = false;
    private String checkUploadURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_review);
        initToolbar();
        if (sharedPreference != null) {
            unittype = sharedPreference.getString("unittype", "0");
            session = sharedPreference.getString("session", "0");
        }
        initView();
    }

    private void initView() {
        data_review_spinner1 = (Spinner) findViewById(R.id.data_review_spinner1);
        data_review_spinner2 = (Spinner) findViewById(R.id.data_review_spinner2);
        data_review_grid = (GridView) findViewById(R.id.data_review_grid);

        dataGridViewAdapter = new ArrayAdapter<String>(DataReview.this, android.R.layout.simple_list_item_1, dataList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                // Return the GridView current item as a View
                View view = super.getView(position, convertView, parent);
                return GridViewUtils.getView(DataReview.this, view, dataList, position);
            }
        };
        data_review_grid.setAdapter(dataGridViewAdapter);
        data_review_spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int pos1, long l) {
                data_review_grid.setVisibility(View.GONE);
                if (pos1 == 0) {
                    dataAdapter = new ArrayAdapter<String>(DataReview.this, android.R.layout.simple_spinner_item, data[0]);
                    if (TextUtils.equals(unittype, "1")) {
                        permission = true;
                    } else {
                        UiUtils.show("对不起，您无权限进行此项操作。请切换到其他项。");
                    }
                } else {
                    dataAdapter = new ArrayAdapter<String>(DataReview.this, android.R.layout.simple_spinner_item, data[1]);
                    if (TextUtils.equals(unittype, "2")) {
                        permission = true;
                    } else {
                        UiUtils.show("对不起，您无权限进行此项操作。请切换到其他项。");
                    }
                }
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                data_review_spinner2.setAdapter(dataAdapter);
                if (permission == true) {
                    data_review_spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int pos2, long l) {
                            data_review_grid.setVisibility(View.GONE);
                            initArrayList(dataList, "证书编号", "机构编号", "提交人编号", "提交人姓名", "操作");
                            dataURLList.clear();
                            dataURLList.add(0);
                            tempPos1 = pos1;
                            tempPos2 = pos2;
                            if (pos1 == 0) {
                                data_review_grid.setVisibility(View.VISIBLE);
                                if (pos2 == 0) {
                                    checkUploadURL = "/getApplyData";
                                } else if (pos2 == 1) {
                                    checkUploadURL = "/getUpFileData";
                                } else if (pos2 == 2) {
                                    checkUploadURL = "/getOnSiteData";
                                } else {
                                    checkUploadURL = "/getUpData";
                                }
                                new GetUploadListTask(DataReview.this,
                                        "GetUploadListTask", checkUploadURL, session).execute();
                            } else {
                                // 检测数据审核
                                data_review_grid.setVisibility(View.VISIBLE);
                                type = (pos2 == 0 ? 5 : 6);
                                new GetCheckListTask(DataReview.this,
                                        "GetCheckListTask", "?type=" + type).execute();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_data_review);
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

    // 认证数据审核
    private class GetUploadListTask extends BaseAsyTask {
        private String status = "0";
        private JSONArray tempJSONArray;

        public GetUploadListTask(Context context, String string, String... params) {
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
                    tempJSONArray = new JSONArray(string);

                    for (int i = 0; i < tempJSONArray.length(); ++i) {
                        dataList.add(tempJSONArray.getJSONObject(i).optString("temp2"));
                        dataList.add(tempJSONArray.getJSONObject(i).optString("temp1"));
                        dataList.add(tempJSONArray.getJSONObject(i).optString("uploadmanid"));
                        dataList.add(tempJSONArray.getJSONObject(i).optString("uploadman"));
                        dataList.add("查看详情");
                        dataURLList.add(tempJSONArray.getJSONObject(i).optInt("id"));
                    }
                }
            } catch (Exception e) {
                status = "500";
                e.printStackTrace();
            }
            return status;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "0")) {
                dataGridViewAdapter.notifyDataSetChanged();
                data_review_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        // 点击的是操作的那一行
                        if (i > 5 && i % 5 == 4) {
                            Intent intent = new Intent(DataReview.this, DataReviewDetail.class);
                            intent.putExtra("id", dataURLList.get(i / 5));
                            intent.putExtra("pos1", tempPos1);
                            intent.putExtra("pos2", tempPos2);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            } else if (TextUtils.equals(s, "401")) {
                UiUtils.show("登陆信息已过期，请重新登陆。");
                if (editor != null) {
                    editor.clear();
                    editor.apply();
                }
                startActivity(new Intent(DataReview.this, LoginActivity.class));
                finish();
            } else {
                UiUtils.show("未知错误");
            }
        }
    }

    // 检测检验数据审核
    private class GetCheckListTask extends BaseAsyTask {
        private String status = "-200";
        private String msg = "未知错误";
        private String data;
        private JSONArray tempJSONArray;

        public GetCheckListTask(Context context, String string, String... params) {
            super(context, string, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "0")) {
                UiUtils.show(msg);
                dataGridViewAdapter.notifyDataSetChanged();
                data_review_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        // 点击的是操作的那一行
                        if (i > 5 && i % 5 == 4) {
                            Intent intent = new Intent(DataReview.this, DataReviewDetail.class);
                            intent.putExtra("id", dataURLList.get(i / 5));
                            intent.putExtra("pos1", tempPos1);
                            intent.putExtra("pos2", tempPos2);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            } else if (TextUtils.equals(s, "401")) {
                UiUtils.show("登陆信息已过期，请重新登陆。");
                if (editor != null) {
                    editor.clear();
                    editor.apply();
                }
                startActivity(new Intent(DataReview.this, LoginActivity.class));
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
                    if (string.contains("\"status\":500")) {
                        status = "401";
                        return status;
                    }
                    jsonObject = new JSONObject(string);
                    status = jsonObject.optString("code");
                    msg = jsonObject.optString("msg");
                    if (TextUtils.equals(status, "0")) {
                        data = jsonObject.optString("data");
                        tempJSONArray = new JSONArray(data);
                        for (int i = 0; i < tempJSONArray.length(); ++i) {
                            dataList.add(tempJSONArray.getJSONObject(i).optString("temp2"));
                            dataList.add(tempJSONArray.getJSONObject(i).optString("temp1"));
                            dataList.add(tempJSONArray.getJSONObject(i).optString("uploadmanid"));
                            dataList.add(tempJSONArray.getJSONObject(i).optString("uploadman"));
                            dataList.add("查看详情");
                            dataURLList.add(tempJSONArray.getJSONObject(i).optInt("id"));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return status;
        }
    }
}