package com.example.lamlv.login_mvp.Presenter;

import com.example.lamlv.login_mvp.Model.Data.Account;

public interface IPresenterCallBack {
    void onLoginSuccess(String message);
    void onLoginFailed(String message);
}
