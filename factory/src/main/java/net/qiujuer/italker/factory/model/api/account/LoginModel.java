package net.qiujuer.italker.factory.model.api.account;

/**
 * @author qiujuer Email:qiujuer@live.cn
 * @version 1.0.0
 */
public class LoginModel {
    private String account;
    private String password;
    private String pushId;

    public LoginModel(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public LoginModel(String account, String password, String pushId) {
        this.account = account;
        this.password = password;
        this.pushId = pushId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }
}
