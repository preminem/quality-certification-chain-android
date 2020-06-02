package com.waibao.qualityCertification.fragement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waibao.qualityCertification.R;
import com.waibao.qualityCertification.activity.AboutUsActivity;
import com.waibao.qualityCertification.activity.AdvicesActivity;
import com.waibao.qualityCertification.activity.LoginActivity;
import com.waibao.qualityCertification.activity.UpdatePassword;
import com.waibao.qualityCertification.activity.UserHelp;
import com.waibao.qualityCertification.base.BaseFragement;
import com.waibao.qualityCertification.util.UiUtils;

/**
 * 实现清除缓存，意见反馈，关于我们，帮助和退出登陆的功能
 */
public class MyFragement extends BaseFragement implements View.OnClickListener {

    private RelativeLayout tv_advice;
    private RelativeLayout contact_our;
    private RelativeLayout helpRelative;
    private RelativeLayout update_password;
    private Button logOut;
    private TextView username;
    private StringBuffer usernameStr = new StringBuffer();
    private String unittype;
    private String usertype;
    private AlertDialog.Builder dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragement_my, null, false);
        initView(mView);

        return mView;
    }

    private void initView(View mView) {
        tv_advice = (RelativeLayout) mView.findViewById(R.id.tv_advice);
        tv_advice.setOnClickListener(this);
        contact_our = (RelativeLayout) mView.findViewById(R.id.contact_our);
        contact_our.setOnClickListener(this);
        helpRelative = (RelativeLayout) mView.findViewById(R.id.help);
        helpRelative.setOnClickListener(this);
        username = (TextView) mView.findViewById(R.id.username);
        logOut = (Button) mView.findViewById(R.id.logOut);
        logOut.setOnClickListener(this);
        update_password = (RelativeLayout) mView.findViewById(R.id.update_password);
        update_password.setOnClickListener(this);
        if (sharedPreference != null) {
            unittype = sharedPreference.getString("unittype", "0");
            usertype = sharedPreference.getString("usertype", "0");
            usernameStr.append("您好，");
            if (TextUtils.equals(unittype, "1")) {
                usernameStr.append("认证机构的");
            } else if (TextUtils.equals(unittype, "2")) {
                usernameStr.append("检测检验机构的");
            } else if (TextUtils.equals(unittype, "3")) {
                usernameStr.append("政府监管机构的");
            }
            if (TextUtils.equals(usertype, "1")) {
                usernameStr.append("业务员");
            } else if (TextUtils.equals(usertype, "2")) {
                usernameStr.append("审核员");
            } else if (TextUtils.equals(usertype, "3")) {
                usernameStr.append("管理员");
            }
            username.setText(usernameStr);
            username.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_advice:
                startActivity(new Intent(getActivity(), AdvicesActivity.class));
                break;
            case R.id.contact_our:
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                break;
            case R.id.help:
                startActivity(new Intent(getActivity(), UserHelp.class));
                break;
            case R.id.logOut:
                dialog = new AlertDialog.Builder(getActivity());
                dialog.setMessage("确定要退出吗？");
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        // 删除保存的数据
                        if (editor != null) {
                            editor.clear().commit();
                        }
                        UiUtils.show("退出成功，请重新登录。");
                        dialog.dismiss();
                        getActivity().finish();
                    }
                });
                dialog.show();
                break;
            case R.id.update_password:
                startActivity(new Intent(getActivity(), UpdatePassword.class));
                getActivity().finish();
                break;
            default:
                break;
        }
    }
}