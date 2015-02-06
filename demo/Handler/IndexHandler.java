package Handler;

import Web.*;

/**
 * 博客首页
 * @author Ma
 */
public class IndexHandler extends Handler {

    /**
     * 博客首页
     */
    public void get() {
        
        BaseTemplate index = this.render("index");
        Object[][] r = DB.get("select id,title,creat_date from article");
        index.assign("articles", r);
        index.assign("title", "HM Blog");
        this.writer(index.display());
        
    }
}
