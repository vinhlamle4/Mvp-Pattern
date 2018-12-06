package com.example.lamlv.login_mvp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lamlv.login_mvp.Presenter.ILoginPresenterOps;
import com.example.lamlv.login_mvp.Presenter.LoginPresenterPresenter;
import com.example.lamlv.login_mvp.View.IViewCallBack;

public class MainActivity extends AppCompatActivity  implements IViewCallBack {

    private EditText edtUserName, edtPassword;
    private Button btnLogin, btnExit;
    private ILoginPresenterOps iLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        onClickbtnEvent();
    }

    public void initView()
    {
        edtUserName = findViewById(R.id.edt_user_name);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        btnExit = findViewById(R.id.btn_exit);
        iLoginPresenter = new LoginPresenterPresenter(this);
    }

    private void onClickbtnEvent()
    {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iLoginPresenter.getAccount(edtUserName.getText().toString().trim(), edtPassword.getText().toString().trim());
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void loginSuccessed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginFailed(String message) {
        Toast.makeText(this, message  , Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy()
    {
        iLoginPresenter.destroyModel();
        iLoginPresenter = null;
        super.onDestroy();
    }
}
