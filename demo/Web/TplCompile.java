package Web;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

/**
 * 动态编译Java文件
 *
 * @author Ma
 */
public class TplCompile {

    public String TemplatePath = Options.TemplatePath;

    /*
     public static void main(String[] args) {
     TplCompile Compiler = new TplCompile();
     Compiler.setTemplatePath();
     Compiler.run("dd/test.html");
     }*/
    public void setTemplatePath() {
        this.TemplatePath = Options.TemplatePath;
    }

    /**
     * 开始编译
     */
    public Object run(String fileName) {
        this.setTemplatePath();//先配置路径
        TemplateCompile templateCompile = new TemplateCompile();
        int r = templateCompile.start(fileName);
        if (r > 0 && r == 1) {
            //1表示说明文件不存在
            //0表示解析正常
            return null;
        }
        String javaFile = this.TemplatePath + templateCompile.getFilePath(fileName) + "/" + templateCompile.getfileName(fileName) + "Template.java";
        String classPath = this.getClass().getResource("/").getPath();
        //动态编译
        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
        int status = javac.run(null, null, null, "-d", classPath, javaFile);
        if (status != 0) {
            System.out.println("没有编译成功！");
        }

        //动态执行
        try {
            //返回与带有给定字符串名的类 或接口相关联的 Class 对象。

            Class cls = Class.forName("Template." + templateCompile.getFilePath(fileName).replace("/", ".") + templateCompile.getfileName(fileName) + "Template");
            BaseTemplate object = (BaseTemplate) cls.newInstance();
            return object;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TplCompile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(TplCompile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(TplCompile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
