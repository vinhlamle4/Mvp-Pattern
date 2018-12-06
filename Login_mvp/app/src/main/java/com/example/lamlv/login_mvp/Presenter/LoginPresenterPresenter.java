package com.example.lamlv.login_mvp.Presenter;

import com.example.lamlv.login_mvp.Model.ILoginModelOps;
import com.example.lamlv.login_mvp.Model.LoginModel;
import com.example.lamlv.login_mvp.View.IViewCallBack;

public class LoginPresenterPresenter implements ILoginPresenterOps, IPresenterCallBack {

    private IViewCallBack iViewCallBack;
    private ILoginModelOps iLoginModel;

    public LoginPresenterPresenter(IViewCallBack iViewCallBack) {
        this.iViewCallBack = iViewCallBack;
        iLoginModel = new LoginModel(this);
    }

    @Override
    public void getAccount(String userName, String password)
    {
        iLoginModel.login(userName, password);

    }

    @Override
    public void destroyModel() {
        iLoginModel = null;
    }

    @Override
    public void onLoginSuccess(String message) {
        iViewCallBack.LoginSuccessed(message);
    }

    @Override
    public void onLoginFailed(String message) {
        iViewCallBack.LoginFailed(message);
    }
}
