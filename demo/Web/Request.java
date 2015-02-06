package Web;

import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * 处理接受到的数据
 *
 * @author Ma
 */
public class Request {

    public HashMap Header;
    public HashMap Arguments;
    public HashMap Cookies;
    public String Path;
    public String Method;
    public String RemoteIP;

    /**
     * 处理接收数据
     * @param receive 接受
     */
    public Request(String receive) {

        StringTokenizer stringTokenizer = new StringTokenizer(receive, "\n");
        String[] receiveArray = new String[stringTokenizer.countTokens()];
        HashMap<String, String> hashMap = new HashMap<String, String>();
        this.Header = new HashMap();
        this.Arguments = new HashMap();
        this.Cookies = new HashMap();
        String[] URLStrings = stringTokenizer.nextToken().split(" ");
        this.Method = URLStrings[0];//请求方式
        String href = URLStrings[1]; //URL
        String[] hrefSplit = href.split("\\?", 2);
        this.Path = hrefSplit[0];
        //分析URL传值
        if (hrefSplit.length > 1) {
            String query = hrefSplit[1];
            this.Arguments = ResolveQuery.query(query);
        }

        while (stringTokenizer.hasMoreTokens()) {
            String temp = stringTokenizer.nextToken().trim();
            //长度小于1说明是个空白行，说明header可以结束了，如果是POST请求下面的就是参数了
            if (temp.length() < 1) {
                break;
            }
            String[] split = temp.split(":", 2);
            if (split.length < 2) {
                ;
            }
            if (split[0].toLowerCase().equals("cookie")) {
                //解析Cookie，不记录到Header中
                Cookie.analysis(split[1].trim());
                continue;
            }
            this.Header.put(split[0], split[1].trim());
        }
        while (stringTokenizer.hasMoreTokens()) {
            String temp = stringTokenizer.nextToken().trim();
            //长度小于1说明是个空白行，说明header可以结束了，如果是POST请求下面的就是参数了
            if (temp.length() < 1) {
                continue;
            }
            this.Arguments.putAll(ResolveQuery.query(temp));

        }

    }

    /**
     * 获取用户远程IP
     * @return 远程IP
     */
    public String getRemoteIP() {
        if(this.Header.containsKey("True-ip")){
            this.RemoteIP = (String) this.Header.get("True-ip");
        }
        return this.RemoteIP;
    }

    /**
     * 设置远程IP
     * @param RemoteIP
     */
    public void setRemoteIP(String RemoteIP) {
        this.RemoteIP = RemoteIP;
    }
    
    

}
