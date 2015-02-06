package Web;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.HashMap;

/**
 *
 * @author Ma
 */
public class Handler {

    public Request request;

    /**
     * 初始化Handler
     */
    public void init(Request request) {
        this.request = request;

    }

    /**
     * 用于实现一些类似构造方法的作用
     * @todo 装饰器实现
     */
    public void construct() {
        ;
    }

    /**
     * 获取参数
     * @param key
     * @return 
     */
    public Object getArgument(String key){
        return this.request.Arguments.get(key);
    }
    /**
     * 把模版或者字符串写入
     * @param s 
     */
    public void writer(String s) {
        Response.setContent(s);
    }

    /**
     * 读取模版
     */
    public BaseTemplate render(String fileName) {

        try {
            BaseTemplate object;
            if (Options.DEBUG) {
                fileName = fileName.replace(".", "/");//把点替换成路径
                TplCompile tplCompile = new TplCompile();
                object = (BaseTemplate) tplCompile.run(fileName + ".html");
                if (object == null) {
                    InternalServerError500.set500(fileName + "模版不存在!");
                }
            } else {
                Class TemplateClass = Class.forName("Template." + fileName + "Template");
                object = (BaseTemplate) TemplateClass.newInstance();
            }
            return object;
        } catch (ClassNotFoundException ex) {
            //模版不存在
            InternalServerError500.set500(fileName + "模版不存在!");
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * 重定向到URL，发送303
     *
     * @param url
     */
    public void redirect(String url) {
        Response.setStatus(303);
        if (url.toLowerCase().indexOf("http://") < 0) {
            url = "http://" + this.request.Header.get("Host") + url;
        }
        Response.header("Location: " + url);
    }

    /**
     * 页面跳转到URL中，通过HTML
     *
     * @param url
     * @param time
     */
    public void jump(String message, String url, int time, boolean status) {

        boolean isSetUrl = false;

        if (message == null) {
            message = "Message!";
        }
        if (time < 1) {
            time = 3;
        }
        if ("XMLHttpRequest".equals(this.request.Header.get("X-Requested-With"))) {
            //判断Ajax请求
            HashMap map = new HashMap();
            map.put("static", status);
            map.put("data", message);
            Response.setContentType(Response.findContentType("json"));
            this.writer(Json.encode(map));
        } else {
            BaseTemplate jump = this.render("jump");
            jump.assign("status", status);
            jump.assign("message", message);
            if (url != null) {
                isSetUrl = true;
            }
            jump.assign("isSetUrl", isSetUrl);
            jump.assign("url", url);
            jump.assign("wait", time);
            this.writer(jump.display());
        }

    }

    /**
     * 操作成功
     */
    public void success(String message, String url, int time) {
        if (message == null) {
            message = "Success!";
        }
        this.jump(message, url, time, true);
    }

    /**
     * 操作成功
     */
    public void success(String message, String url) {
        this.success(message, url, 0);
    }

    /**
     * 操作成功
     */
    public void success(String message) {
        this.success(message, null, 0);
    }

    /**
     * 操作错误
     */
    public void error(String message, String url, int time) {
        if (message == null) {
            message = "Error!";
        }
        this.jump(message, url, time, false);
    }

    /**
     * 操作错误
     */
    public void error(String message, String url) {
        this.error(message, url, 0);
    }

    /**
     * 操作错误
     */
    public void error(String message) {
        this.error(message, null, 0);
    }
}
