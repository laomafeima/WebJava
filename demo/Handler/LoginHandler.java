package Handler;

import Web.*;

/**
 * 登陆
 *
 * @author Ma
 */
public class LoginHandler extends Handler {

    /**
     * 获取登录页面
     */
    public void get() {
        BaseTemplate login = this.render("login");
        login.assign("title", "Login");
        this.writer(login.display());
    }

    /**
     * 登录操作
     */
    public void post() {
        String name = (String) this.getArgument("name");
        String password = (String) this.getArgument("password");
        Object id = DB.getField("SELECT id FROM user WHERE `name`=? AND password=?", name, password);
        if (id == null) {
            this.success("失败");
        } else {
            Cookie.setCookie("id", (String) id);
            this.success("成功", "/");
        }

    }
}
