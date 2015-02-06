package Handler;

import Web.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 *
 * @author Ma
 */
public class ArticleHandler extends Handler {

    public void get(String id) {
        BaseTemplate index = this.render("article");
        Object[] r = DB.find("select * from article where id=?", id);
        if (r.length < 1) {
            NotFound404.set404("文章不存在");
            return;
        }
        long time = Integer.parseInt((String) r[3]);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp now = new Timestamp(time * 1000);
        r[3] = df.format(now);
        index.assign("article", r);
        index.assign("title", r[1].toString());
        this.writer(index.display());
    }
}
