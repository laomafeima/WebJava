/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Web;

/**
 * 运行时的配置
 *
 * @author 马
 */
public class Options {

    /**
     * 是不是debug模式
     */
    public static boolean DEBUG = false;
    /**
     * 是否打印信息
     */
    public static boolean ParseLine = false;
    public static String DBURL = null;//地址
    public static String DBDriver = null;//驱动
    public static String DBUser = null;//用户
    public static String DBPassword = null;//密码
    public static int DBIdleTime = 25200;//数据库最大闲置时间

    public static String ServerName = "XiaoMaServer 1.0";//服务器名字

    public static String StaticPath = "/src/Web/Static/";//静态文件目录
    public static String TemplatePath = "/src/Web/Template/";//模版文件目录


}
