package Handler;

import Web.BaseTemplate;
import Web.DB;

/**
 * 添加文章
 *
 * @author Ma
 */
public class CreateHandler extends AdminHandler {

    /**
     * 添加文章
     */
    public void get() {

        if (this.getUserID() < 1) {
            this.error("请登录！", "/Login", 5);
            return;
        }
        BaseTemplate create = this.render("create");
        create.assign("title", "Create");
        this.writer(create.display());
    }

    /**
     * 添加文章
     */
    public void post() {
        if (this.getUserID() < 1) {
            this.error("请登录！", "/Login", 5);
            return;
        }
        String title = (String) this.getArgument("title");
        String content = (String) this.getArgument("content");
        int date = (int) (System.currentTimeMillis() / 1000);
        String dateString = date + "";
        int r = DB.insert("INSERT INTO article (title,content,creat_date,user_id) VALUE (?,?,?,?)", title.trim(), content.trim(), dateString, this.getUserID() + "");
        if (r < 1) {
            this.success("失败");
        } else {
            this.success("成功", "/");
        }
    }
}
