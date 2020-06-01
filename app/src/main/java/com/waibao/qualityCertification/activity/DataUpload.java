package com.waibao.qualityCertification.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.waibao.qualityCertification.R;
import com.waibao.qualityCertification.base.BaseActivity;
import com.waibao.qualityCertification.base.BaseAsyTask;
import com.waibao.qualityCertification.interfaceMy.PermissionListener;
import com.waibao.qualityCertification.util.FileUtils;
import com.waibao.qualityCertification.util.SHAUtil;
import com.waibao.qualityCertification.util.UiUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

// 数据上传
public class DataUpload extends BaseActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private Spinner data_upload_spinner1;
    private Spinner data_upload_spinner2;
    private String[][] data = {{"证书申请资料上传", "文件审核资料上传", "现场审核资料上传", "证书数据上传"}, {"检测检验上传", "试运行数据上传"}};
    private ArrayAdapter<String> dataAdapter = null;
    // 检测数据上传相关
    private LinearLayout data_upload_jiance_line = null;
    private EditText data_upload_jiance_certificateID = null;
    private EditText data_upload_jiance_unitID = null;
    private EditText data_upload_jiance_unitName = null;
    private EditText data_upload_jiance_platformName = null;
    private EditText data_upload_jiance_edition = null;
    private EditText data_upload_jiance_testUnitID = null;
    private EditText data_upload_jiance_testUnitName = null;
    private EditText data_upload_jiance_conclusion = null;
    private EditText data_upload_jiance_testTime = null;
    private TextView data_upload_jiance_testTime_text = null;
    private EditText data_upload_jiance_testingPersonID = null;
    private EditText data_upload_jiance_testingPersonName = null;
    private TextView data_upload_jiance_postPersonID = null;
    private TextView data_upload_jiance_postPersonName = null;
    private String data_upload_jiance_certificateIDStr = "";
    private String data_upload_jiance_unitIDStr = "";
    private String data_upload_jiance_unitNameStr = "";
    private String data_upload_jiance_platformNameStr = "";
    private String data_upload_jiance_editionStr = "";
    private String data_upload_jiance_testUnitIDStr = "";
    private String data_upload_jiance_testUnitNameStr = "";
    private String data_upload_jiance_conclusionStr = "";
    private String data_upload_jiance_testTimeStr = "";
    private String data_upload_jiance_testingPersonIDStr = "";
    private String data_upload_jiance_testingPersonNameStr = "";
    private Button data_upload_jiance_btn = null;
    // 证书申请资料上传
    private LinearLayout data_upload_certificate_application_line;
    private EditText data_upload_certificate_application_unitID;
    private Button data_upload_certificate_application_printFileID;
    private Button data_upload_certificate_application_verificationPrintFileID;
    private EditText data_upload_certificate_application_unitName;
    private EditText data_upload_certificate_application_platformName;
    private EditText data_upload_certificate_application_edition;
    private EditText data_upload_certificate_application_deployPlace;
    private EditText data_upload_certificate_application_runPlace;
    private EditText data_upload_certificate_application_runState;
    private EditText data_upload_certificate_application_applyTime;
    private TextView data_upload_certificate_application_operatorID;
    private TextView data_upload_certificate_application_operatorName;
    private Button data_upload_certificate_application_btn;
    private String data_upload_certificate_application_unitIDStr = "";
    private String data_upload_certificate_application_unitNameStr = "";
    private String data_upload_certificate_application_platformNameStr = "";
    private String data_upload_certificate_application_editionStr = "";
    private String data_upload_certificate_application_deployPlaceStr = "";
    private String data_upload_certificate_application_runPlaceStr = "";
    private String data_upload_certificate_application_runStateStr = "";
    private String data_upload_certificate_application_applyTimeStr = "";
    private String data_upload_certificate_application_applicationHash;
    private String data_upload_certificate_application_applicationImageURL;
    private String data_upload_certificate_application_legalFileHash;
    private String data_upload_certificate_application_LegalFileImageURL;
    // 文件审核资料上传
    private LinearLayout data_upload_document_audit_line;
    private EditText data_upload_document_audit_unitID;
    private EditText data_upload_document_audit_unitName;
    private EditText data_upload_document_audit_platformName;
    private EditText data_upload_document_audit_edition;
    private EditText data_upload_document_audit_certificationUnitID;
    private EditText data_upload_document_audit_certificationUnitName;
    private EditText data_upload_document_audit_conclusion;
    private EditText data_upload_document_audit_auditTime;
    private EditText data_upload_document_audit_auditorID;
    private EditText data_upload_document_audit_auditorName;
    private TextView data_upload_document_audit_postPersonID;
    private TextView data_upload_document_audit_postPersonName;
    private Button data_upload_document_audit_btn;
    private String data_upload_document_audit_unitIDStr = "";
    private String data_upload_document_audit_unitNameStr = "";
    private String data_upload_document_audit_platformNameStr = "";
    private String data_upload_document_audit_editionStr = "";
    private String data_upload_document_audit_certificationUnitIDStr = "";
    private String data_upload_document_audit_certificationUnitNameStr = "";
    private String data_upload_document_audit_conclusionStr = "";
    private String data_upload_document_audit_auditTimeStr = "";
    private String data_upload_document_audit_auditorIDStr = "";
    private String data_upload_document_audit_auditorNameStr = "";
    // 现场审核资料上传
    private LinearLayout data_upload_site_audit_line;
    private EditText data_upload_site_audit_unitID;
    private EditText data_upload_site_audit_unitName;
    private EditText data_upload_site_audit_platformName;
    private EditText data_upload_site_audit_edition;
    private EditText data_upload_site_audit_certificationUnitID;
    private EditText data_upload_site_audit_certificationUnitName;
    private EditText data_upload_site_audit_conclusion;
    private EditText data_upload_site_audit_auditTime;
    private EditText data_upload_site_audit_auditEndTime;
    private EditText data_upload_site_audit_auditorID;
    private EditText data_upload_site_audit_auditorName;
    private TextView data_upload_site_audit_postPersonID;
    private TextView data_upload_site_audit_postPersonName;
    private Button data_upload_site_audit_btn;
    private String data_upload_site_audit_unitIDStr = "";
    private String data_upload_site_audit_unitNameStr = "";
    private String data_upload_site_audit_platformNameStr = "";
    private String data_upload_site_audit_editionStr = "";
    private String data_upload_site_audit_certificationUnitIDStr = "";
    private String data_upload_site_audit_certificationUnitNameStr = "";
    private String data_upload_site_audit_conclusionStr = "";
    private String data_upload_site_audit_auditTimeStr = "";
    private String data_upload_site_audit_auditEndTimeStr = "";
    private String data_upload_site_audit_auditorIDStr = "";
    private String data_upload_site_audit_auditorNameStr = "";
    // 证书数据上传
    private LinearLayout data_upload_certificate_data_line;
    private EditText data_upload_certificate_data_certificateID;
    private EditText data_upload_certificate_data_unitID;
    private EditText data_upload_certificate_data_unitName;
    private EditText data_upload_certificate_data_registerAddr;
    private EditText data_upload_certificate_data_platformName;
    private EditText data_upload_certificate_data_edition;
    private EditText data_upload_certificate_data_website;
    private EditText data_upload_certificate_data_auditAddr;
    private EditText data_upload_certificate_data_authenticationStandard;
    private EditText data_upload_certificate_data_certificationMode;
    private EditText data_upload_certificate_data_certificationConclusion;
    private EditText data_upload_certificate_data_certificationClass;
    private EditText data_upload_certificate_data_awardDate;
    private EditText data_upload_certificate_data_replaceDate;
    private EditText data_upload_certificate_data_validityTerm;
    private EditText data_upload_certificate_data_certificationID;
    private EditText data_upload_certificate_datacertificationName;
    private Button data_upload_certificate_data_filename;
    private TextView data_upload_certificate_data_postPersonID;
    private TextView data_upload_certificate_data_postPersonName;
    private Button data_upload_certificate_data_btn;
    private String data_upload_certificate_data_certificateIDStr = "";
    private String data_upload_certificate_data_unitIDStr = "";
    private String data_upload_certificate_data_unitNameStr = "";
    private String data_upload_certificate_data_registerAddrStr = "";
    private String data_upload_certificate_data_platformNameStr = "";
    private String data_upload_certificate_data_editionStr = "";
    private String data_upload_certificate_data_websiteStr = "";
    private String data_upload_certificate_data_auditAddrStr = "";
    private String data_upload_certificate_data_authenticationStandardStr = "";
    private String data_upload_certificate_data_certificationModeStr = "";
    private String data_upload_certificate_data_certificationConclusionStr = "";
    private String data_upload_certificate_data_certificationClassStr;
    private String data_upload_certificate_data_awardDateStr;
    private String data_upload_certificate_data_replaceDateStr;
    private String data_upload_certificate_data_validityTermStr;
    private String data_upload_certificate_data_certificationIDStr;
    private String data_upload_certificate_datacertificationNameStr;
    private String data_upload_certificateImagePath;
    private String data_upload_certificate_applicationHash;
    private String data_upload_certificateImageURL;

    private String postPersonID;
    private String postPersonName;
    private String unittype;
    private boolean permission = false;
    private int tempPos1 = 0, tempPos2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_upload);
        initToolbar();
        if (sharedPreference != null) {
            String userid = sharedPreference.getString("userid", "0");
            if (!TextUtils.equals(userid, "0")) {
                postPersonID = userid.split(";")[0];
                postPersonName = userid.split(";")[1];
            }
            unittype = sharedPreference.getString("unittype", "0");
        }
        initView();
    }

    private void initView() {
        data_upload_spinner1 = (Spinner) findViewById(R.id.data_upload_spinner1);
        data_upload_spinner2 = (Spinner) findViewById(R.id.data_upload_spinner2);
        initJianceView();
        initCertificateApplicationView();
        initDocumentAuditView();
        initSiteAuditView();
        initCertificateDataView();
        data_upload_spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int pos1, long l) {
                data_upload_jiance_line.setVisibility(View.GONE);
                data_upload_certificate_application_line.setVisibility(View.GONE);
                data_upload_document_audit_line.setVisibility(View.GONE);
                data_upload_certificate_data_line.setVisibility(View.GONE);
                if (pos1 == 0) {
                    dataAdapter = new ArrayAdapter<String>(DataUpload.this, android.R.layout.simple_spinner_item, data[0]);
                    if (TextUtils.equals(unittype, "1")) {
                        permission = true;
                    } else {
                        UiUtils.show("对不起，您无权限进行此项操作。请切换到其他项。");
                    }
                } else {
                    dataAdapter = new ArrayAdapter<String>(DataUpload.this, android.R.layout.simple_spinner_item, data[1]);
                    if (TextUtils.equals(unittype, "2")) {
                        data_upload_jiance_line.setVisibility(View.VISIBLE);
                        data_upload_jiance_postPersonID.setText(postPersonID);
                        data_upload_jiance_postPersonName.setText(postPersonName);
                        permission = true;
                    } else {
                        UiUtils.show("对不起，您无权限进行此项操作。请切换到其他项。");
                    }
                }
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                data_upload_spinner2.setAdapter(dataAdapter);
                if (permission == true) {
                    data_upload_spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, final int pos2, long l) {
                            data_upload_certificate_application_line.setVisibility(View.GONE);
                            data_upload_document_audit_line.setVisibility(View.GONE);
                            data_upload_site_audit_line.setVisibility(View.GONE);
                            data_upload_certificate_data_line.setVisibility(View.GONE);
                            tempPos1 = pos1;
                            tempPos2 = pos2;
                            if (pos1 == 0) {
                                if (pos2 == 0) {
                                    data_upload_certificate_application_line.setVisibility(View.VISIBLE);
                                    data_upload_certificate_application_operatorID.setText(postPersonID);
                                    data_upload_certificate_application_operatorName.setText(postPersonName);
                                } else if (pos2 == 1) {
                                    data_upload_document_audit_line.setVisibility(View.VISIBLE);
                                    data_upload_document_audit_postPersonID.setText(postPersonID);
                                    data_upload_document_audit_postPersonName.setText(postPersonName);
                                } else if (pos2 == 2) {
                                    data_upload_site_audit_line.setVisibility(View.VISIBLE);
                                    data_upload_site_audit_postPersonID.setText(postPersonID);
                                    data_upload_site_audit_postPersonName.setText(postPersonName);
                                } else {
                                    data_upload_certificate_data_line.setVisibility(View.VISIBLE);
                                    data_upload_certificate_data_postPersonID.setText(postPersonID);
                                    data_upload_certificate_data_postPersonName.setText(postPersonName);
                                }
                            } else {
                                if (pos2 == 0) {
                                    data_upload_jiance_testTime_text.setText("检测时间:");
                                } else {
                                    data_upload_jiance_testTime_text.setText("试运行时间:");
                                }
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

    //证书数据上传
    private void initCertificateDataView() {
        data_upload_certificate_data_line = (LinearLayout) findViewById(R.id.data_upload_certificate_data_line);
        data_upload_certificate_data_certificateID = (EditText) findViewById(R.id.data_upload_certificate_data_certificateID);
        data_upload_certificate_data_unitID = (EditText) findViewById(R.id.data_upload_certificate_data_unitID);
        data_upload_certificate_data_unitName = (EditText) findViewById(R.id.data_upload_certificate_data_unitName);
        data_upload_certificate_data_registerAddr = (EditText) findViewById(R.id.data_upload_certificate_data_registerAddr);
        data_upload_certificate_data_platformName = (EditText) findViewById(R.id.data_upload_certificate_data_platformName);
        data_upload_certificate_data_edition = (EditText) findViewById(R.id.data_upload_certificate_data_edition);
        data_upload_certificate_data_website = (EditText) findViewById(R.id.data_upload_certificate_data_website);
        data_upload_certificate_data_auditAddr = (EditText) findViewById(R.id.data_upload_certificate_data_auditAddr);
        data_upload_certificate_data_authenticationStandard = (EditText) findViewById(R.id.data_upload_certificate_data_authenticationStandard);
        data_upload_certificate_data_certificationMode = (EditText) findViewById(R.id.data_upload_certificate_data_certificationMode);
        data_upload_certificate_data_certificationConclusion = (EditText) findViewById(R.id.data_upload_certificate_data_certificationConclusion);
        data_upload_certificate_data_certificationClass = (EditText) findViewById(R.id.data_upload_certificate_data_certificationClass);
        data_upload_certificate_data_awardDate = (EditText) findViewById(R.id.data_upload_certificate_data_awardDate);
        data_upload_certificate_data_replaceDate = (EditText) findViewById(R.id.data_upload_certificate_data_replaceDate);
        data_upload_certificate_data_validityTerm = (EditText) findViewById(R.id.data_upload_certificate_data_validityTerm);
        data_upload_certificate_data_certificationID = (EditText) findViewById(R.id.data_upload_certificate_data_certificationID);
        data_upload_certificate_datacertificationName = (EditText) findViewById(R.id.data_upload_certificate_datacertificationName);
        data_upload_certificate_data_filename = (Button) findViewById(R.id.data_upload_certificate_data_filename);
        data_upload_certificate_data_postPersonID = (TextView) findViewById(R.id.data_upload_certificate_data_postPersonID);
        data_upload_certificate_data_postPersonName = (TextView) findViewById(R.id.data_upload_certificate_data_postPersonName);
        data_upload_certificate_data_btn = (Button) findViewById(R.id.data_upload_certificate_data_btn);
        data_upload_certificate_data_btn.setOnClickListener(this);
        data_upload_certificate_data_filename.setOnClickListener(this);
    }

    //现场审核上传
    private void initSiteAuditView() {
        data_upload_site_audit_line = (LinearLayout) findViewById(R.id.data_upload_site_audit_line);
        data_upload_site_audit_unitID = (EditText) findViewById(R.id.data_upload_site_audit_unitID);
        data_upload_site_audit_unitName = (EditText) findViewById(R.id.data_upload_site_audit_unitName);
        data_upload_site_audit_platformName = (EditText) findViewById(R.id.data_upload_site_audit_platformName);
        data_upload_site_audit_edition = (EditText) findViewById(R.id.data_upload_site_audit_edition);
        data_upload_site_audit_certificationUnitID = (EditText) findViewById(R.id.data_upload_site_audit_certificationUnitID);
        data_upload_site_audit_certificationUnitName = (EditText) findViewById(R.id.data_upload_site_audit_certificationUnitName);
        data_upload_site_audit_conclusion = (EditText) findViewById(R.id.data_upload_site_audit_conclusion);
        data_upload_site_audit_auditTime = (EditText) findViewById(R.id.data_upload_site_audit_auditTime);
        data_upload_site_audit_auditEndTime = (EditText) findViewById(R.id.data_upload_site_audit_auditEndTime);
        data_upload_site_audit_auditorID = (EditText) findViewById(R.id.data_upload_site_audit_auditorID);
        data_upload_site_audit_auditorName = (EditText) findViewById(R.id.data_upload_site_audit_auditorName);
        data_upload_site_audit_postPersonID = (TextView) findViewById(R.id.data_upload_site_audit_postPersonID);
        data_upload_site_audit_postPersonName = (TextView) findViewById(R.id.data_upload_site_audit_postPersonName);
        data_upload_site_audit_btn = (Button) findViewById(R.id.data_upload_site_audit_btn);
        data_upload_site_audit_btn.setOnClickListener(this);
    }

    //文件审核上传
    private void initDocumentAuditView() {
        data_upload_document_audit_line = (LinearLayout) findViewById(R.id.data_upload_document_audit_line);
        data_upload_document_audit_unitID = (EditText) findViewById(R.id.data_upload_document_audit_unitID);
        data_upload_document_audit_unitName = (EditText) findViewById(R.id.data_upload_document_audit_unitName);
        data_upload_document_audit_platformName = (EditText) findViewById(R.id.data_upload_document_audit_platformName);
        data_upload_document_audit_edition = (EditText) findViewById(R.id.data_upload_document_audit_edition);
        data_upload_document_audit_certificationUnitID = (EditText) findViewById(R.id.data_upload_document_audit_certificationUnitID);
        data_upload_document_audit_certificationUnitName = (EditText) findViewById(R.id.data_upload_document_audit_certificationUnitName);
        data_upload_document_audit_conclusion = (EditText) findViewById(R.id.data_upload_document_audit_conclusion);
        data_upload_document_audit_auditTime = (EditText) findViewById(R.id.data_upload_document_audit_auditTime);
        data_upload_document_audit_auditorID = (EditText) findViewById(R.id.data_upload_document_audit_auditorID);
        data_upload_document_audit_auditorName = (EditText) findViewById(R.id.data_upload_document_audit_auditorName);
        data_upload_document_audit_postPersonID = (TextView) findViewById(R.id.data_upload_document_audit_postPersonID);
        data_upload_document_audit_postPersonName = (TextView) findViewById(R.id.data_upload_document_audit_postPersonName);
        data_upload_document_audit_btn = (Button) findViewById(R.id.data_upload_document_audit_btn);
        data_upload_document_audit_btn.setOnClickListener(this);
    }

    // 证书申请上传
    private void initCertificateApplicationView() {
        data_upload_certificate_application_line = (LinearLayout) findViewById(R.id.data_upload_certificate_application_line);
        data_upload_certificate_application_unitID = (EditText) findViewById(R.id.data_upload_certificate_application_unitID);
        data_upload_certificate_application_printFileID = (Button) findViewById(R.id.data_upload_certificate_application_printFileID);
        data_upload_certificate_application_verificationPrintFileID = (Button) findViewById(R.id.data_upload_certificate_application_verificationPrintFileID);
        data_upload_certificate_application_unitName = (EditText) findViewById(R.id.data_upload_certificate_application_unitName);
        data_upload_certificate_application_platformName = (EditText) findViewById(R.id.data_upload_certificate_application_platformName);
        data_upload_certificate_application_edition = (EditText) findViewById(R.id.data_upload_certificate_application_edition);
        data_upload_certificate_application_deployPlace = (EditText) findViewById(R.id.data_upload_certificate_application_deployPlace);
        data_upload_certificate_application_runPlace = (EditText) findViewById(R.id.data_upload_certificate_application_runPlace);
        data_upload_certificate_application_runState = (EditText) findViewById(R.id.data_upload_certificate_application_runState);
        data_upload_certificate_application_applyTime = (EditText) findViewById(R.id.data_upload_certificate_application_applyTime);
        data_upload_certificate_application_operatorID = (TextView) findViewById(R.id.data_upload_certificate_application_operatorID);
        data_upload_certificate_application_operatorName = (TextView) findViewById(R.id.data_upload_certificate_application_operatorName);
        data_upload_certificate_application_btn = (Button) findViewById(R.id.data_upload_certificate_application_btn);
        data_upload_certificate_application_printFileID.setOnClickListener(this);
        data_upload_certificate_application_verificationPrintFileID.setOnClickListener(this);
        data_upload_certificate_application_btn.setOnClickListener(this);
    }

    // 检测检验上传
    private void initJianceView() {
        data_upload_jiance_line = (LinearLayout) findViewById(R.id.data_upload_jiance_line);
        data_upload_jiance_certificateID = (EditText) findViewById(R.id.data_upload_jiance_certificateID);
        data_upload_jiance_unitID = (EditText) findViewById(R.id.data_upload_jiance_unitID);
        data_upload_jiance_unitName = (EditText) findViewById(R.id.data_upload_jiance_unitName);
        data_upload_jiance_platformName = (EditText) findViewById(R.id.data_upload_jiance_platformName);
        data_upload_jiance_edition = (EditText) findViewById(R.id.data_upload_jiance_edition);
        data_upload_jiance_testUnitID = (EditText) findViewById(R.id.data_upload_jiance_testUnitID);
        data_upload_jiance_testUnitName = (EditText) findViewById(R.id.data_upload_jiance_testUnitName);
        data_upload_jiance_conclusion = (EditText) findViewById(R.id.data_upload_jiance_conclusion);
        data_upload_jiance_testTime_text = (TextView) findViewById(R.id.data_upload_jiance_testTime_text);
        data_upload_jiance_testTime = (EditText) findViewById(R.id.data_upload_jiance_testTime);
        data_upload_jiance_testingPersonID = (EditText) findViewById(R.id.data_upload_jiance_testingPersonID);
        data_upload_jiance_testingPersonName = (EditText) findViewById(R.id.data_upload_jiance_testingPersonName);
        data_upload_jiance_postPersonID = (TextView) findViewById(R.id.data_upload_jiance_postPersonID);
        data_upload_jiance_postPersonName = (TextView) findViewById(R.id.data_upload_jiance_postPersonName);
        data_upload_jiance_btn = (Button) findViewById(R.id.data_upload_jiance_btn);
        data_upload_jiance_btn.setOnClickListener(DataUpload.this);
    }

    private boolean isValidInput(int cnt, String... args) {
        for (int i = 0; i < cnt; ++i) {
            if (args[i].length() != 18) {
                return false;
            }
        }
        for (int i = 0; i < args.length; ++i) {
            if (TextUtils.isEmpty(args[i])) {
                return false;
            }
        }
        return true;
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_data_upload);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 检测检验上传
            case R.id.data_upload_jiance_btn:
                data_upload_jiance_certificateIDStr = data_upload_jiance_certificateID.getText().toString().trim();
                data_upload_jiance_unitIDStr = data_upload_jiance_unitID.getText().toString().trim();
                data_upload_jiance_unitNameStr = data_upload_jiance_unitName.getText().toString().trim();
                data_upload_jiance_platformNameStr = data_upload_jiance_platformName.getText().toString().trim();
                data_upload_jiance_editionStr = data_upload_jiance_edition.getText().toString().trim();
                data_upload_jiance_testUnitIDStr = data_upload_jiance_testUnitID.getText().toString().trim();
                data_upload_jiance_testUnitNameStr = data_upload_jiance_testUnitName.getText().toString().trim();
                data_upload_jiance_conclusionStr = data_upload_jiance_conclusion.getText().toString().trim();
                data_upload_jiance_testTimeStr = data_upload_jiance_testTime.getText().toString().trim();
                data_upload_jiance_testingPersonIDStr = data_upload_jiance_testingPersonID.getText().toString().trim();
                data_upload_jiance_testingPersonNameStr = data_upload_jiance_testingPersonName.getText().toString().trim();
                if (!isValidInput(1, data_upload_jiance_testingPersonIDStr, data_upload_jiance_certificateIDStr, data_upload_jiance_unitIDStr,
                        data_upload_jiance_unitNameStr, data_upload_jiance_platformNameStr, data_upload_jiance_editionStr,
                        data_upload_jiance_testUnitIDStr, data_upload_jiance_testUnitNameStr, data_upload_jiance_conclusionStr,
                        data_upload_jiance_testTimeStr, data_upload_jiance_testingPersonNameStr)) {
                    UiUtils.show("对不起，您的输入不能为空。");
                } else {
                    JSONObject basedata = new JSONObject();
                    JSONObject data = new JSONObject();
                    String url;
                    String fcnUrl;
                    try {
                        basedata.put("unitName", data_upload_jiance_unitNameStr);
                        basedata.put("platformName", data_upload_jiance_platformNameStr);
                        basedata.put("edition", data_upload_jiance_editionStr);
                        basedata.put("testUnitID", data_upload_jiance_testUnitIDStr);
                        basedata.put("testUnitName", data_upload_jiance_testUnitNameStr);
                        basedata.put("conclusion", data_upload_jiance_conclusionStr);
                        if (tempPos2 == 0) {
                            basedata.put("testTime", data_upload_jiance_testTimeStr);
                            url = "/detectionDataUpdate";
                            fcnUrl = "testDataUpload";
                        } else {
                            basedata.put("testRunTime", data_upload_jiance_testTimeStr);
                            url = "/testRunDataUpdate";
                            fcnUrl = "trialRunDataUpload";
                        }
                        basedata.put("testingPersonID", data_upload_jiance_testingPersonIDStr);
                        basedata.put("testingPersonName", data_upload_jiance_testingPersonNameStr);
                        basedata.put("postPersonID", postPersonID);
                        basedata.put("postPersonName", postPersonName);
                        JSONArray peersJSONArray = new JSONArray();
                        peersJSONArray.put("peer0.org1.example.com").put("peer0.org2.example.com").put("peer0.org3.example.com");
                        data.put("peers", peersJSONArray);
                        data.put("fcn", fcnUrl);
                        JSONArray argsJSONArray = new JSONArray();
                        argsJSONArray.put(data_upload_jiance_certificateIDStr).put(data_upload_jiance_unitIDStr).put(basedata).put("");
                        data.put("args", argsJSONArray);
                        data.put("certificateID", data_upload_jiance_certificateIDStr);
                        data.put("unitID", data_upload_jiance_unitIDStr);
                        data.put("postPersonName", postPersonName);
                        data.put("postPersonID", postPersonID);
                        new DataUpdateTask(DataUpload.this,
                                "DataUpdateTask", url, data.toString()).execute();
                    } catch (JSONException e) {
                        UiUtils.show("出现错误");
                        e.printStackTrace();
                    }
                }
                break;
            // 证书申请资料上传
            case R.id.data_upload_certificate_application_printFileID:
                BaseActivity.requestRuntimePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
                    @Override
                    public void onGranted() {//获取动态权限
                        Intent imageIntent = new Intent();
                        imageIntent.setType("image/*");
                        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(imageIntent, 2);
                    }

                    @Override
                    public void onDenied(List<String> deniedPermission) {
                        dialog(DataUpload.this, "浏览文件需要该权限，拒绝后将不能正常使用，是否重新开启此权限？");
                    }
                });
                break;
            case R.id.data_upload_certificate_application_verificationPrintFileID:
                BaseActivity.requestRuntimePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
                    @Override
                    public void onGranted() {//获取动态权限
                        Intent imageIntent = new Intent();
                        imageIntent.setType("image/*");
                        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(imageIntent, 3);
                    }

                    @Override
                    public void onDenied(List<String> deniedPermission) {
                        dialog(DataUpload.this, "浏览文件需要该权限，拒绝后将不能正常使用，是否重新开启此权限？");
                    }
                });
                break;
            case R.id.data_upload_certificate_application_btn:
                data_upload_certificate_application_unitIDStr = data_upload_certificate_application_unitID.getText().toString().trim();
                data_upload_certificate_application_unitNameStr = data_upload_certificate_application_unitName.getText().toString().trim();
                data_upload_certificate_application_platformNameStr = data_upload_certificate_application_platformName.getText().toString().trim();
                data_upload_certificate_application_editionStr = data_upload_certificate_application_edition.getText().toString().trim();
                data_upload_certificate_application_deployPlaceStr = data_upload_certificate_application_deployPlace.getText().toString().trim();
                data_upload_certificate_application_runPlaceStr = data_upload_certificate_application_runPlace.getText().toString().trim();
                data_upload_certificate_application_runStateStr = data_upload_certificate_application_runState.getText().toString().trim();
                data_upload_certificate_application_applyTimeStr = data_upload_certificate_application_applyTime.getText().toString().trim();
                if (!isValidInput(1, postPersonID, postPersonName, data_upload_certificate_application_unitIDStr, data_upload_certificate_application_unitNameStr,
                        data_upload_certificate_application_platformNameStr, data_upload_certificate_application_editionStr, data_upload_certificate_application_deployPlaceStr,
                        data_upload_certificate_application_runPlaceStr, data_upload_certificate_application_runStateStr, data_upload_certificate_application_applyTimeStr)) {
                    UiUtils.show("对不起，您的输入不能为空。");
                } else {
                    JSONObject basedata = new JSONObject();
                    JSONObject data = new JSONObject();
                    try {
                        basedata.put("unitID", data_upload_certificate_application_unitIDStr);
                        basedata.put("unitName", data_upload_certificate_application_unitNameStr);
                        basedata.put("platformName", data_upload_certificate_application_platformNameStr);
                        basedata.put("edition", data_upload_certificate_application_editionStr);
                        basedata.put("deployPlace", data_upload_certificate_application_deployPlaceStr);
                        basedata.put("runPlace", data_upload_certificate_application_runPlaceStr);
                        basedata.put("runState", data_upload_certificate_application_runStateStr);
                        basedata.put("applyTime", data_upload_certificate_application_applyTimeStr);
                        basedata.put("operatorID", postPersonID);
                        basedata.put("operatorName", postPersonName);
                        JSONArray peersJSONArray = new JSONArray();
                        peersJSONArray.put("peer0.org1.example.com").put("peer0.org2.example.com").put("peer0.org3.example.com");
                        data.put("peers", peersJSONArray);
                        data.put("fcn", "certApplication");
                        JSONArray argsJSONArray = new JSONArray();
                        argsJSONArray.put(basedata).put(data_upload_certificate_application_applicationHash).put(data_upload_certificate_application_legalFileHash).put(SHAUtil.getSHA256StrJava("")).put("");
                        data.put("args", argsJSONArray);
                        data.put("image1", data_upload_certificate_application_applicationImageURL);
                        data.put("image2", data_upload_certificate_application_LegalFileImageURL);
                        data.put("applyTime", data_upload_certificate_application_applyTimeStr);
                        data.put("operatorID", postPersonID);
                        data.put("operatorName", postPersonName);
                        data.put("unitID", data_upload_certificate_application_unitIDStr);
                        new DataUpdateTask(DataUpload.this,
                                "DataUpdateTask", "/applyCertificateUpdate", data.toString()).execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            // 文件审核资料上传
            case R.id.data_upload_document_audit_btn:
                data_upload_document_audit_unitIDStr = data_upload_document_audit_unitID.getText().toString().trim();
                data_upload_document_audit_unitNameStr = data_upload_document_audit_unitName.getText().toString().trim();
                data_upload_document_audit_platformNameStr = data_upload_document_audit_platformName.getText().toString().trim();
                data_upload_document_audit_editionStr = data_upload_document_audit_edition.getText().toString().trim();
                data_upload_document_audit_certificationUnitIDStr = data_upload_document_audit_certificationUnitID.getText().toString().trim();
                data_upload_document_audit_certificationUnitNameStr = data_upload_document_audit_certificationUnitName.getText().toString().trim();
                data_upload_document_audit_conclusionStr = data_upload_document_audit_conclusion.getText().toString().trim();
                data_upload_document_audit_auditTimeStr = data_upload_document_audit_auditTime.getText().toString().trim();
                data_upload_document_audit_auditorIDStr = data_upload_document_audit_auditorID.getText().toString().trim();
                data_upload_document_audit_auditorNameStr = data_upload_document_audit_auditorName.getText().toString().trim();
                if (!isValidInput(2, postPersonID, data_upload_document_audit_auditorIDStr, data_upload_document_audit_unitIDStr, data_upload_document_audit_unitNameStr,
                        data_upload_document_audit_platformNameStr, data_upload_document_audit_editionStr, data_upload_document_audit_certificationUnitIDStr,
                        data_upload_document_audit_certificationUnitNameStr, data_upload_document_audit_conclusionStr, data_upload_document_audit_auditTimeStr,
                        data_upload_document_audit_auditorNameStr, postPersonName)) {
                    UiUtils.show("对不起，您的输入不能为空。");
                } else {
                    JSONObject basedata = new JSONObject();
                    JSONObject data = new JSONObject();
                    try {
                        basedata.put("unitID", data_upload_document_audit_unitIDStr);
                        basedata.put("unitName", data_upload_document_audit_unitNameStr);
                        basedata.put("platformName", data_upload_document_audit_platformNameStr);
                        basedata.put("edition", data_upload_document_audit_editionStr);
                        basedata.put("certificationUnitID", data_upload_document_audit_certificationUnitIDStr);
                        basedata.put("certificationUnitName", data_upload_document_audit_certificationUnitNameStr);
                        basedata.put("conclusion", data_upload_document_audit_conclusionStr);
                        basedata.put("auditTime", data_upload_document_audit_auditTimeStr);
                        basedata.put("auditorID", data_upload_document_audit_auditorIDStr);
                        basedata.put("auditorName", data_upload_document_audit_auditorNameStr);
                        basedata.put("postPersonID", postPersonID);
                        basedata.put("postPersonName", postPersonName);
                        JSONArray peersJSONArray = new JSONArray();
                        peersJSONArray.put("peer0.org1.example.com").put("peer0.org2.example.com").put("peer0.org3.example.com");
                        data.put("peers", peersJSONArray);
                        data.put("fcn", "docAudit");
                        JSONArray argsJSONArray = new JSONArray();
                        argsJSONArray.put(basedata.toString()).put("");
                        data.put("args", argsJSONArray);
                        data.put("certificateID", data_upload_document_audit_certificationUnitIDStr);
                        data.put("unitID", data_upload_document_audit_unitIDStr);
                        data.put("postPersonName", postPersonName);
                        data.put("postPersonID", postPersonID);
                        new DataUpdateTask(DataUpload.this,
                                "DataUpdateTask", "/fileUpdate", data.toString()).execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            // 现场审核资料上传
            case R.id.data_upload_site_audit_btn:
                data_upload_site_audit_unitIDStr = data_upload_site_audit_unitID.getText().toString().trim();
                data_upload_site_audit_unitNameStr = data_upload_site_audit_unitName.getText().toString().trim();
                data_upload_site_audit_platformNameStr = data_upload_site_audit_platformName.getText().toString().trim();
                data_upload_site_audit_editionStr = data_upload_site_audit_edition.getText().toString().trim();
                data_upload_site_audit_certificationUnitIDStr = data_upload_site_audit_certificationUnitID.getText().toString().trim();
                data_upload_site_audit_certificationUnitNameStr = data_upload_site_audit_certificationUnitName.getText().toString().trim();
                data_upload_site_audit_conclusionStr = data_upload_site_audit_conclusion.getText().toString().trim();
                data_upload_site_audit_auditTimeStr = data_upload_site_audit_auditTime.getText().toString().trim();
                data_upload_site_audit_auditEndTimeStr = data_upload_site_audit_auditEndTime.getText().toString().trim();
                data_upload_site_audit_auditorIDStr = data_upload_site_audit_auditorID.getText().toString().trim();
                data_upload_site_audit_auditorNameStr = data_upload_site_audit_auditorName.getText().toString().trim();
                if (!isValidInput(2, postPersonID, data_upload_site_audit_auditorIDStr, data_upload_site_audit_unitIDStr, data_upload_site_audit_unitNameStr,
                        data_upload_site_audit_platformNameStr, data_upload_site_audit_editionStr, data_upload_site_audit_certificationUnitIDStr,
                        data_upload_site_audit_certificationUnitNameStr, data_upload_site_audit_conclusionStr, data_upload_site_audit_auditTimeStr,
                        data_upload_site_audit_auditEndTimeStr, data_upload_site_audit_auditorIDStr, data_upload_site_audit_auditorNameStr, postPersonName)) {
                    UiUtils.show("对不起，您的输入不能为空。");
                } else {
                    JSONObject basedata = new JSONObject();
                    JSONObject data = new JSONObject();
                    try {
                        basedata.put("unitID", data_upload_site_audit_unitIDStr);
                        basedata.put("unitName", data_upload_site_audit_unitNameStr);
                        basedata.put("platformName", data_upload_site_audit_platformNameStr);
                        basedata.put("edition", data_upload_site_audit_editionStr);
                        basedata.put("certificationUnitID", data_upload_site_audit_certificationUnitIDStr);
                        basedata.put("certificationUnitName", data_upload_site_audit_certificationUnitNameStr);
                        basedata.put("conclusion", data_upload_site_audit_conclusionStr);
                        basedata.put("auditTime", data_upload_site_audit_auditTimeStr);
                        basedata.put("auditorID", data_upload_site_audit_auditorIDStr);
                        basedata.put("auditorName", data_upload_site_audit_auditorNameStr);
                        basedata.put("postPersonID", postPersonID);
                        basedata.put("postPersonName", postPersonName);
                        JSONArray peersJSONArray = new JSONArray();
                        peersJSONArray.put("peer0.org1.example.com").put("peer0.org2.example.com").put("peer0.org3.example.com");
                        data.put("peers", peersJSONArray);
                        data.put("fcn", "onsiteAudit");
                        JSONArray argsJSONArray = new JSONArray();
                        argsJSONArray.put(basedata.toString()).put("");
                        data.put("args", argsJSONArray);
                        data.put("unitID", data_upload_site_audit_unitIDStr);
                        data.put("postPersonName", postPersonName);
                        data.put("postPersonID", postPersonID);
                        new DataUpdateTask(DataUpload.this,
                                "DataUpdateTask", "/OnsiteUpdate", data.toString()).execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            // 证书数据上传
            case R.id.data_upload_certificate_data_filename:
                BaseActivity.requestRuntimePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
                    @Override
                    public void onGranted() {//获取动态权限
                        Intent imageIntent = new Intent();
                        imageIntent.setType("image/*");
                        imageIntent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(imageIntent, 1);
                    }

                    @Override
                    public void onDenied(List<String> deniedPermission) {
                        dialog(DataUpload.this, "浏览文件需要该权限，拒绝后将不能正常使用，是否重新开启此权限？");
                    }
                });
                break;
            case R.id.data_upload_certificate_data_btn:
                data_upload_certificate_data_certificateIDStr = data_upload_certificate_data_certificateID.getText().toString().trim();
                data_upload_certificate_data_unitIDStr = data_upload_certificate_data_unitID.getText().toString().trim();
                data_upload_certificate_data_unitNameStr = data_upload_certificate_data_unitName.getText().toString().trim();
                data_upload_certificate_data_registerAddrStr = data_upload_certificate_data_registerAddr.getText().toString().trim();
                data_upload_certificate_data_platformNameStr = data_upload_certificate_data_platformName.getText().toString().trim();
                data_upload_certificate_data_editionStr = data_upload_certificate_data_edition.getText().toString().trim();
                data_upload_certificate_data_websiteStr = data_upload_certificate_data_website.getText().toString().trim();
                data_upload_certificate_data_auditAddrStr = data_upload_certificate_data_auditAddr.getText().toString().trim();
                data_upload_certificate_data_authenticationStandardStr = data_upload_certificate_data_authenticationStandard.getText().toString().trim();
                data_upload_certificate_data_certificationModeStr = data_upload_certificate_data_certificationMode.getText().toString().trim();
                data_upload_certificate_data_certificationConclusionStr = data_upload_certificate_data_certificationConclusion.getText().toString().trim();
                data_upload_certificate_data_certificationClassStr = data_upload_certificate_data_certificationClass.getText().toString().trim();
                data_upload_certificate_data_awardDateStr = data_upload_certificate_data_awardDate.getText().toString().trim();
                data_upload_certificate_data_replaceDateStr = data_upload_certificate_data_replaceDate.getText().toString().trim();
                data_upload_certificate_data_validityTermStr = data_upload_certificate_data_validityTerm.getText().toString().trim();
                data_upload_certificate_data_certificationIDStr = data_upload_certificate_data_certificationID.getText().toString().trim();
                data_upload_certificate_datacertificationNameStr = data_upload_certificate_datacertificationName.getText().toString().trim();
                if (!isValidInput(1, postPersonID, postPersonName, data_upload_certificate_data_certificateIDStr, data_upload_certificate_data_unitIDStr,
                        data_upload_certificate_data_unitNameStr, data_upload_certificate_data_registerAddrStr, data_upload_certificate_data_platformNameStr,
                        data_upload_certificate_data_editionStr, data_upload_certificate_data_websiteStr, data_upload_certificate_data_auditAddrStr,
                        data_upload_certificate_data_authenticationStandardStr, data_upload_certificate_data_certificationModeStr, data_upload_certificate_data_certificationConclusionStr,
                        data_upload_certificate_data_certificationClassStr, data_upload_certificate_data_awardDateStr, data_upload_certificate_data_replaceDateStr, data_upload_certificate_data_validityTermStr,
                        data_upload_certificate_data_certificationIDStr, data_upload_certificate_datacertificationNameStr, data_upload_certificateImageURL)) {
                    UiUtils.show("对不起，您的输入不能为空。");
                } else {
                    JSONObject basedata = new JSONObject();
                    JSONObject data = new JSONObject();
                    try {
                        basedata.put("certificateID", data_upload_certificate_data_certificateIDStr);
                        basedata.put("unitName", data_upload_certificate_data_unitNameStr);
                        basedata.put("registerAddr", data_upload_certificate_data_registerAddrStr);
                        basedata.put("platformName", data_upload_certificate_data_platformNameStr);
                        basedata.put("edition", data_upload_certificate_data_editionStr);
                        basedata.put("website", data_upload_certificate_data_websiteStr);
                        basedata.put("auditAddr", data_upload_certificate_data_auditAddrStr);
                        basedata.put("authenticationStandard", data_upload_certificate_data_authenticationStandardStr);
                        basedata.put("certificationMode", data_upload_certificate_data_certificationModeStr);
                        basedata.put("certificationConclusion", data_upload_certificate_data_certificationConclusionStr);
                        basedata.put("certificationClass", data_upload_certificate_data_certificationClassStr);
                        basedata.put("awardDate", data_upload_certificate_data_awardDateStr);
                        basedata.put("replaceDate", data_upload_certificate_data_replaceDateStr);
                        basedata.put("validityTerm", data_upload_certificate_data_validityTermStr);
                        basedata.put("certificationID", data_upload_certificate_data_certificationIDStr);
                        basedata.put("certificationName", data_upload_certificate_datacertificationNameStr);
                        basedata.put("certificateScanHASH", data_upload_certificate_applicationHash);
                        basedata.put("postPersonID", postPersonID);
                        basedata.put("postPersonName", postPersonName);
                        JSONArray peersJSONArray = new JSONArray();
                        peersJSONArray.put("peer0.org1.example.com").put("peer0.org2.example.com").put("peer0.org3.example.com");
                        data.put("peers", peersJSONArray);
                        data.put("fcn", "certUpload");
                        JSONArray argsJSONArray = new JSONArray();
                        argsJSONArray.put(data_upload_certificate_data_certificateIDStr).put(data_upload_certificate_data_unitIDStr).put(basedata.toString()).put("");
                        data.put("args", argsJSONArray);
                        data.put("image1", data_upload_certificateImageURL);
                        data.put("certificateID", data_upload_certificate_data_certificateIDStr);
                        data.put("unitID", data_upload_certificate_data_unitIDStr);
                        data.put("postPersonName", postPersonName);
                        data.put("postPersonID", postPersonID);
                        new DataUpdateTask(DataUpload.this,
                                "DataUpdateTask", "/certificateUpdate", data.toString()).execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                data_upload_certificateImagePath = FileUtils.getPath(DataUpload.this, uri);
                data_upload_certificate_data_filename.setText("已上传");
                data_upload_certificateImageURL = new StringBuffer("data:image/jpeg;base64,").append(FileUtils.imageToBase64(data_upload_certificateImagePath)).toString();
                data_upload_certificate_applicationHash = SHAUtil.getSHA256StrJava(data_upload_certificateImageURL);
            } else {
                UiUtils.show("您没有选择任何文件");
            }
        } else if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                data_upload_certificateImagePath = FileUtils.getPath(DataUpload.this, uri);
                data_upload_certificate_application_printFileID.setText("已上传");
                data_upload_certificate_application_applicationImageURL = new StringBuffer("data:image/jpeg;base64,").append(FileUtils.imageToBase64(data_upload_certificateImagePath)).toString();
                data_upload_certificate_application_applicationHash = SHAUtil.getSHA256StrJava(data_upload_certificate_application_applicationImageURL);
            } else {
                UiUtils.show("您没有选择任何文件");
            }
        } else {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                data_upload_certificateImagePath = FileUtils.getPath(DataUpload.this, uri);
                data_upload_certificate_application_verificationPrintFileID.setText("已上传");
                data_upload_certificate_application_LegalFileImageURL = new StringBuffer("data:image/jpeg;base64,").append(FileUtils.imageToBase64(data_upload_certificateImagePath)).toString();
                data_upload_certificate_application_legalFileHash = SHAUtil.getSHA256StrJava(data_upload_certificate_application_LegalFileImageURL);
            } else {
                UiUtils.show("您没有选择任何文件");
            }
        }
    }

    private class DataUpdateTask extends BaseAsyTask {
        private String status = "-200";
        private String msg = "未知错误";

        public DataUpdateTask(Context context, String string, String... params) {
            super(context, string, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "0")) {
                UiUtils.show(msg);
                startActivity(new Intent(DataUpload.this, MainActivity.class));
                finish();
            } else if (TextUtils.equals(s, "401")) {
                UiUtils.show("登陆信息已过期，请重新登陆。");
                if (editor != null) {
                    editor.clear();
                    editor.apply();
                }
                startActivity(new Intent(DataUpload.this, LoginActivity.class));
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
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return status;
        }
    }
}