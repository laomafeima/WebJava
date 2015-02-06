
import Handler.*;
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

        //关于路径说明：在IDE环境下user.dir和jar包中的user.dir不一样，这里手动指定
        Options.StaticPath = System.getProperty("user.dir") + "/src/Static/";//静态文件目录
        Options.TemplatePath = System.getProperty("user.dir") + "/src/Template/";//模版文件目录

        Options.DEBUG = true;
        HttpServer.setPATH("/", new IndexHandler());
        HttpServer.setPATH("\\/Article\\/(\\d+)", new ArticleHandler());
        HttpServer.setPATH("\\/Admin", new AdminHandler());
        HttpServer.setPATH("\\/Login", new LoginHandler());
        HttpServer.setPATH("\\/Create", new CreateHandler());
        System.out.println("Welcome BLOG Listen port 8080");
        HttpServer.init(8080);
    }
}
