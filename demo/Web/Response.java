package Web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * 用于记录一些相应信息 如状态码，cookie等
 *
 * @author Ma
 */
public class Response {

    /**
     * 返回消息实体
     */
    public static String Content = "";
    /**
     * 如果是文件就直接些进去
     */
    public static byte[] FileBytes;
    /**
     * 标记这次是不是请求文件的
     */
    public static boolean IsFile = false;
    public static ArrayList<String> Header = new ArrayList<String>();
    public static String Status = "HTTP/1.1 200 OK";
    public static ArrayList<String> Cookies = new ArrayList<String>();
    public static String ContentType = "text/html;charset=UTF-8;";
    /**
     * 服务器的名字
     */
    public static String Server = Options.ServerName;
    /**
     * 只包含Contest的长度不包含header的长度
     */
    public static int ContentLength;

    /**
     * //禁用页面缓存 <br/>
     * header('Cache-Control: no-cache, no-store, max-age=0,must-revalidate');
     * <br/>
     * header('Expires: Mon, 26 Jul 1997 05:00:00 GMT');<br/>
     * header('Pragma: no-cache');<br/>
     *
     * public static String X-Powered-By;
     *
     */
    /**
     * 设置状态码 HTTP/1.1 200 OK <br/>
     * HTTP/1.1 303 See Other \r\nLocation:http://www.maiyoule.com/ 结尾没有分号 <br/>
     * HTTP/1.1 304 Not Modified 缓存 <br/>
     * HTTP/1.1 403 Forbidden<br/>
     * HTTP/1.1 404 Not Found <br/>
     * HTTP/1.1 500 Internal Server Error<br/>
     *
     * @param code
     */
    public static void setStatus(int code) {
        String string;
        switch (code) {
            case 200:
                string = "HTTP/1.1 200 OK";
                break;
            case 303:
                string = "HTTP/1.1 303 See Other";
                break;
            case 304:
                string = "HTTP/1.1 304 Not Modified";
                break;
            case 403:
                string = "HTTP/1.1 403 Forbidden";
                break;
            case 404:
                string = "HTTP/1.1 404 Not Found";
                break;
            case 500:
                string = "HTTP/1.1 500 Internal Server Error";
                break;
            default:
                string = "HTTP/1.1 200 OK";
        }
        Response.Status = string;

    }

    /**
     * 设置content-length长度
     *
     * @param length
     */
    public static void setContentLength(int length) {
        Response.ContentLength = length;
    }

    /**
     * 设置ContentType application/json; charset=utf-8; <br/>
     * text/html;charset=UTF-8;<br/>
     * application/xml;charset=UTF-8;<br/>
     */
    public static void setContentType(String type) {
        Response.ContentType = type;
    }

    /**
     * 查找ContentType类型 不能识别的文件，默认当作zip处理
     *
     * @todo 是否允许自己配置
     */
    public static String findContentType(String type) {
        HashMap<String, String> contentType = new HashMap<String, String>();
        contentType.put("html", "text/html;charset=UTF-8;");
        contentType.put("json", "application/json; charset=utf-8;");
        contentType.put("xml", "application/xml;charset=UTF-8;");
        contentType.put("zip", "application/x-zip-compressed");
        contentType.put("ico", "image/x-icon");
        String returnType = "";
        if (contentType.containsKey(type)) {
            returnType = contentType.get(type);
        } else {
            returnType = "application/x-zip-compressed";
        }
        return returnType;
    }

    /**
     * 设置返回实体
     *
     * @param content
     */
    public static void setContent(String content) {
        Response.Content = content;
    }

    /**
     * 追加返回实体
     *
     * @param content
     */
    public static void addContent(String content) {
        Response.Content = Response.Content + content;
    }

    /**
     * 添加自定义Header
     *
     * @param header
     * @return
     */
    public static boolean header(String header) {
        return Response.Header.add(header);
    }

    /**
     * 获取此次响应的header
     *
     * @return
     */
    public static String getHeader() {
        String header;
        header = Response.Status + "\r\n";
        //检查cookie
        for (int i = 0; i < Response.Cookies.size(); i++) {
            header += Response.Cookies.get(i) + "\r\n";
        }
        header += "Content-type: " + Response.ContentType + "\r\n";
        header += "Content-ength: " + Response.ContentLength + "\r\n";
        //检查自定义Header
        for (int i = 0; i < Response.Header.size(); i++) {
            header += Response.Header.get(i) + "\r\n";
        }

        header += "Server: " + Response.Server + "\r\n\r\n";

        return header;
    }

    public static byte[] getContentBytes() throws UnsupportedEncodingException {

        return Response.Content.getBytes("UTF-8");
    }

    /**
     * 请求结束，清理信息
     */
    public static void finish() {

        Response.Status = "HTTP/1.1 200 OK";
        Response.Content = "";
        Response.Header = new ArrayList<String>();
        Response.Cookies = new ArrayList<String>();
        Response.ContentLength = 0;
        Response.ContentType = "text/html;charset=UTF-8;";
        Response.FileBytes = new byte[0];
        //取消是文件标识
        Response.IsFile = false;
    }
}
