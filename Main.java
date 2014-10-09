import Web.*;

/**
 * @author Ma
 */
public class Main {

    /**
     * @param args Main 方法
     */
    public static void main(String[] args) {

        Options.DBURL = "jdbc:mysql://localhost:3306/blog";
        Options.DBDriver = "com.mysql.jdbc.Driver";         // 驱动程序名
        Options.DBUser = "root";
        Options.DBPassword = "123456";

        Options.DEBUG = true;//开启debug模式
        Options.ParseLine = true;//打印每一次请求
        if (Options.DEBUG) {
            //关于路径说明：在IDE环境下user.dir和jar包中的user.dir不一样，这里手动指定
            Options.StaticPath = "/src/Static/";//静态文件目录
            Options.TemplatePath = "/src/Template/";//模版文件目录
        } else {
            Options.StaticPath = "/Static/";//静态文件目录
            Options.TemplatePath = "/Template/";//模版文件目录
        }
        HttpServer.setPATH("/", new IndexHandler());
        System.out.println("Welcome Web.Java. Listen port 8080");
        HttpServer.init(8080);
    }
}
