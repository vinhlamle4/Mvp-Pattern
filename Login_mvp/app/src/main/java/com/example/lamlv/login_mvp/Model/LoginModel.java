package com.example.lamlv.login_mvp.Model;

import android.text.TextUtils;

import com.example.lamlv.login_mvp.Model.Data.Account;
import com.example.lamlv.login_mvp.Presenter.IPresenterCallBack;

public class LoginModel implements ILoginModelOps {

    private IPresenterCallBack iPresenterCallBack;

    public LoginModel(IPresenterCallBack iPresenterCallBack) {
        this.iPresenterCallBack = iPresenterCallBack;
    }

    @Override
    public void login(String userName, String password)
    {
        Account account = Account.getInstance();
        account.setUseName("admin");
        account.setPassword("admin123");
        if (account.getUseName().trim().equals(userName)
                && !TextUtils.isEmpty(userName)
                && account.getPassword().trim().equals(password)
                && !TextUtils.isEmpty(password)) {
            iPresenterCallBack.onLoginSuccess("Đăng Nhập Thành Công!!! ");
        } else {
            iPresenterCallBack.onLoginFailed("Đăng nhập thất bại!!!");
        }
    }
}
