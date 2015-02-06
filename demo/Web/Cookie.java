/*
 * To change this template, choose Cookie | Templates
 * and open the template in the editor.
 */
package Web;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Cookie操作类 maxAge 说明 0表示立即删除，负数表示关闭页面删除，正数表示生存时间
 *
 * @author Ma
 */
public class Cookie {

    public static HashMap RequestCookie;

    public static void analysis(String request) {
        String[] temp = request.split(";");
        Cookie.RequestCookie = new HashMap();
        for (int i = 0; i < temp.length; i++) {
            String[] item = temp[i].trim().split("\\=");
            Cookie.RequestCookie.put(item[0].trim(), item[1].trim());
        }

    }

    /**
     * 设置cookie
     */
    public static void setCookie(String name, String value, int maxAge, String path, String domain) {
        String cookieString = "Set-Cookie: " + name + "=" + Cookie.encode(value) + "; Max-Age=" + maxAge + ";path=" + path + ";domian=" + domain + ";";
        Response.Cookies.add(cookieString);

    }

    /**
     * 设置cookie
     */
    public static void setCookie(String name, String value, int maxAge, String path) {
        String cookieString = "Set-Cookie: " + name + "=" + Cookie.encode(value) + "; Max-Age=" + maxAge + ";path=" + path + ";";
        Response.Cookies.add(cookieString);

    }

    /**
     * 设置cookie
     */
    public static void setCookie(String name, String value, int maxAge) {
        String cookieString = "Set-Cookie: " + name + "=" + Cookie.encode(value) + "; Max-Age=" + maxAge + ";";
        Response.Cookies.add(cookieString);

    }

    /**
     * 设置cookie
     */
    public static void setCookie(String name, String value) {

        String cookieString = "Set-Cookie: " + name + "=" + Cookie.encode(value) + ";";
        Response.Cookies.add(cookieString);
    }

    /**
     * 获取Cookie值
     *
     * @param name
     * @return
     */
    public static String getCookie(String name) {
        if (Cookie.RequestCookie !=null && Cookie.RequestCookie.containsKey(name)) {
            String value = (String) Cookie.RequestCookie.get(name);
            return Cookie.decode(value);
        }else{
            return null;
        }
    }

    /**
     * 编码字符串
     *
     * @param encode
     * @return
     */
    private static String encode(String encode) {
        try {
            encode = java.net.URLEncoder.encode(encode, "UTF-8");
            return encode;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Cookie.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * 解码字符串
     *
     * @param decode
     * @return
     */
    private static String decode(String decode) {

        try {
            decode = java.net.URLDecoder.decode(decode, "UTF-8");
            return decode;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Cookie.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
