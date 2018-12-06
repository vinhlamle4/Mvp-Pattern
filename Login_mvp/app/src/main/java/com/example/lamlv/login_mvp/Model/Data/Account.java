package com.example.lamlv.login_mvp.Model.Data;

public class Account {

    private static Account account;
    private String useName, password;

    private Account() {
    }

    public static Account getInstance()
    {
        if(account == null)
        {
            account = new Account();
        }
        return account;
    }

    public String getUseName() {
        return useName;
    }

    public void setUseName(String useName) {
        this.useName = useName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Account{" +
                "useName='" + useName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
