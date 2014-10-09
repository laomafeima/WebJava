/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Web;

import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.regex.Pattern;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;

/**
 * 编译模版成Java代码
 *
 * @author 马
 */
public class TemplateCompile {

    /**
     * 模版目录
     */
    String TemplatePath = Options.TemplatePath;
    protected ArrayList<String> block;
    protected ArrayList<String> blockContent;
    protected boolean isBlock = false;
    protected boolean isExtends = false;//标记是否是有继承关系
    private BufferedWriter writer;

    public TemplateCompile() {
        this.blockContent = new ArrayList<String>();
        this.block = new ArrayList<String>();
        String path = System.getProperty("user.dir").replace("\\", "/");
        this.TemplatePath = path + Options.TemplatePath;
    }

    /**
     * public static void main(String[] args) { TemplateCompile self = new
     * TemplateCompile(); File tplDir = new File(self.TemplatePath); //判断目录是否存在
     * tplDir.isDirectory();
     *
     * String[] fileList = tplDir.list();
     *
     * for (int i = 0; i < fileList.length; i++) { String string = fileList[i];
     * if (string.matches(".+\\.java")) {//不处理Java文件 continue; }
     * self.start(string); System.out.println(string);//@todo 后期处理子目录的问题 } }
     *
     */
    public int start(String fileName) {
        File tplFile = new File(this.TemplatePath + fileName);
        if (tplFile.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(tplFile));
                String line = null;
                //编译文件Start

                File compilerFile = this.getFile(fileName);//获取Java文件
                if (compilerFile.exists()) {
                    //如果文件存在就比较时间
                    if (compilerFile.lastModified() > tplFile.lastModified()) {
                        //如果最后修改的时候小于编译后的Java文件的最后日期
                        //直接返回
                        //return 0;注释掉，每次都重新编译
                    }
                } else {
                    //如果文件不存在就创建
                    compilerFile.createNewFile();
                }

                this.writer = new BufferedWriter(new FileWriter(compilerFile));
                if (!compilerFile.isFile()) {
                    ;//说明创建失败
                }
                //编译文件END
                String fatherTpl = "Web.BaseTemplate";//默认继承
                line = reader.readLine();//分析第一行是否继承
                Pattern pat = Pattern.compile("\\{\\%\\s*extends\\s*\\s(\\w+)\\s*\\%\\}");
                Matcher mat = pat.matcher(line);
                if (mat.find()) {
                    fatherTpl = mat.group(1) + "Template";
                    line = line.replace(mat.group(), "");
                    this.isExtends = true;
                    //遇到继承，就去解析父模版
                    TemplateCompile templateCompile = new TemplateCompile();
                    int r = templateCompile.start(mat.group(1) + ".html");
                }
                //识别继承标识Start
                Pattern blockPat = Pattern.compile("\\{\\%\\s*block\\s*(\\w+)\\s*\\%\\}");
                //识别继承标识END


                //writer.write("package Web.Template" + this.getPackage(fileName) + ";\npublic class "
                writer.write("package Template" + this.getPackage(fileName) + ";\npublic class "
                        + this.getfileName(fileName)
                        + "Template extends " + fatherTpl + "{");
                if (!this.isExtends) {
                    writer.write("\n\tpublic String display(){\n\t\tString str= \n");
                }
                while (line != null) {
                    line = line.trim();
                    if (line.length() < 1) {
                        line = reader.readLine();//读取下一行
                        continue;
                    }
                    //检查是否是block Start
                    Matcher blockMat = blockPat.matcher(line);
                    if (blockMat.find()) {
                        this.block.add(blockMat.group(1));
                        this.blockContent.add("");
                        this.isBlock = true;
                        line = line.replace(blockMat.group(), "\"\"+this." + blockMat.group(1) + "()");
                        this.writerFile(line, true);
                        line = reader.readLine();//读取下一行
                        continue;
                        //System.out.println(blockMat.group(1));
                    }

                    //检查是否是block End
                    if (this.isBlock && line.matches("\\{\\%\\s*end\\s*\\%\\}")) {
                        this.isBlock = false;
                        line = reader.readLine();//读取下一行
                        continue;

                    }



                    //检查if 语句
                    if (line.matches(".*\\{\\%\\s*if\\s*\\(.+\\)\\s*\\%\\}.*")) {

                        line = line.replace("\"", "\\\"");
                        line = line.replace("{%", "\";");
                        line = line.replace("%}", "{str=str");
                        line = "\"" + line + "\n";

                        if (this.isBlock) {
                            this.writerBlock(line, true);
                        } else {
                            this.writerFile(line, true);
                        }
                        line = reader.readLine();//读取下一行
                        continue;
                    }
                    //检查endif 语句
                    if (line.matches(".*\\{\\%\\s*endif\\s*\\%\\}.*")) {
                        line = line.replaceAll("\\{\\%\\s*endif\\s*\\%\\}", "\";}str=str+\"");
                        line = "\"" + line + "\"\n";
                        if (this.isBlock) {
                            this.writerBlock(line, true);
                        } else {
                            this.writerFile(line, true);
                        }
                        line = reader.readLine();//读取下一行
                        continue;
                    }
                    //检查elseif 语句
                    if (line.matches(".*\\{\\%\\s*elseif\\s*\\(.+\\)\\s*\\%\\}.*")) {
                        line = line.replace("{%", "\";}");
                        line = line.replace("%}", "{str=str");
                        line = line.replace("elseif", "else if");
                        line = "\"" + line + "\n";

                        if (this.isBlock) {
                            this.writerBlock(line, true);
                        } else {
                            this.writerFile(line, true);
                        }
                        line = reader.readLine();//读取下一行
                        continue;
                    }
                    //检查else 语句
                    if (line.matches(".*\\{\\%\\s*else\\s*\\%\\}.*")) {
                        line = line.replaceAll("\\{\\%\\s*else\\s*\\%\\}", "\";}else{str=str");

                        line = "\"" + line + "\n";
                        if (this.isBlock) {
                            this.writerBlock(line, true);
                        } else {
                            this.writerFile(line, true);
                        }
                        line = reader.readLine();//读取下一行
                        continue;
                    }
                    //检查for 语句
                    if (line.matches(".*\\{\\%\\s*for\\s*\\(.+\\)\\s*\\%\\}.*")) {
                        line = line.replace("{%", "\";");
                        line = line.replace("%}", "{str=str");
                        line = "\"" + line + "\n";
                        if (this.isBlock) {
                            this.writerBlock(line, true);
                        } else {
                            this.writerFile(line, true);
                        }
                        line = reader.readLine();//读取下一行
                        continue;
                    }

                    //检查endfor 语句
                    if (line.matches(".*\\{\\%\\s*endfor\\s*\\%\\}.*")) {
                        line = line.replaceAll("\\{\\%\\s*endfor\\s*\\%\\}", "\";}str=str");
                        line = "\"" + line + "\n";
                        if (this.isBlock) {
                            this.writerBlock(line, true);
                        } else {
                            this.writerFile(line, true);
                        }
                        line = reader.readLine();//读取下一行
                        continue;
                    }


                    if (this.isBlock) {
                        this.writerBlock(line);
                    } else {
                        this.writerFile(line);
                    }


                    writer.newLine();
                    //writer.newLine();
                    line = reader.readLine();//读取下一行
                }

                reader.close();
                this.flush();
                this.writer.flush();
                this.writer.close();
                return 0;

            } catch (FileNotFoundException ex) {
                Logger.getLogger(TemplateCompile.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(TemplateCompile.class.getName()).log(Level.SEVERE, null, ex);
            }


        } else {
            return 1;
        }
        return 0;
    }

    /**
     * 创建编译后的文件
     *
     * @param fileName
     * @return
     */
    protected File getFile(String fileName) {
        fileName = this.getFilePath(fileName) + "/" + this.getfileName(fileName);
        String javaTplName = fileName + "Template.java";
        File JavaFile = new File(this.TemplatePath + javaTplName);

        return JavaFile;
    }

    /**
     * 获取文件名字，不包括扩展名字
     *
     * @param fileName
     * @return
     */
    protected String getfileName(String fileName) {
        //获取文件名字，不包含扩展名
        int index = fileName.lastIndexOf(".html");
        fileName = fileName.substring(0, index);
        index = fileName.lastIndexOf("/");
        fileName = fileName.substring(index + 1);
        return fileName;
    }

    /**
     * 获取文件的路径
     *
     * @param fileName
     * @return
     */
    protected String getFilePath(String fileName) {
        int index = fileName.lastIndexOf(".html");
        fileName = fileName.substring(0, index);
        index = fileName.lastIndexOf("/");
        String path = "";
        if (index > 0) {
            path = fileName.substring(0, index);
        }
        return path;
    }

    protected String getPackage(String fileName) {
        String path = this.getFilePath(fileName);
        if (path.equals("")) {
            path = "";
        } else {
            path = "." + path.replace("/", ".");
        }
        return path;
    }

    /**
     * 写入bolck，自动添加引号
     *
     * @param line
     * @return
     */
    protected String writerBlock(String line) {
        String r = "";
        //首先把所有变量修改{{}}
        if (line.matches(".*\\{\\{.+\\}\\}.*")) {
            Pattern pattern = Pattern.compile(".*\\{\\{(\\w+):(\\w+):?(\\w+)?\\}\\}.*");
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                line = line.replace("\"", "\\\"");
                if (matcher.group(3) == null) {
                    line = line.replaceFirst("\\{\\{(.+):(.+)\\}\\}", "\"+this.getArray(\""
                            + matcher.group(1) + "\")["
                            + matcher.group(2) + "]+\"");
                } else {//说明是引用2维数组
                    line = line.replaceFirst("\\{\\{(.+):(.+):(.+)\\}\\}", "\"+this.getArray2(\""
                            + matcher.group(1) + "\")["
                            + matcher.group(2) + "][" + matcher.group(3) + "]+\"");
                }
            } else {
                line = line.replace("{{", "\"+this.get(\"");
                line = line.replace("}}", "\")+\"");
                line = line.replace("\"", "\\\"");
            }
            r = this.blockContent.set(this.block.size() - 1, this.blockContent.get(this.block.size() - 1) + "+\n" + "\"" + line + "\"");
        } else {
            r = this.blockContent.set(this.block.size() - 1, this.blockContent.get(this.block.size() - 1) + "+\n" + "\"" + line.replace("\"", "\\\"") + "\"");
        }
        return r;
    }

    /**
     * 写入 bolck，不写入引号
     *
     * @param line
     * @param eval
     * @return
     */
    protected String writerBlock(String line, boolean eval) {

        //首先把所有变量修改{{}}
        Pattern pat = Pattern.compile("\\(.*\\$([_a-zA-Z][_a-zA-Z0-9]*):?(\\w*).*\\)");
        Matcher mat = pat.matcher(line);

        if (mat.find()) {
            String param = mat.group(1);
            String valueType = "";
            if (mat.group(2).length() > 0) {
                valueType = mat.group(2).replaceFirst(mat.group(2).substring(0, 1), mat.group(2).substring(0, 1).toUpperCase());
            }
            line = line.replaceAll("\\$([_a-zA-Z][_a-zA-Z0-9]*):?(\\w*)", "this.get"
                    + valueType
                    + "(\"" + param + "\")");
        }
        String r = this.blockContent.set(this.block.size() - 1, this.blockContent.get(this.block.size() - 1) + "+\n" + line);
        return r;
    }

    /**
     * 写入引号
     *
     * @param line
     */
    protected void writerFile(String line) {
        line = line.replace("\"", "\\\"");
        //首先把所有变量修改{{}}
        if (line.matches(".*\\{\\{.+\\}\\}.*")) {
            Pattern pattern = Pattern.compile(".*\\{\\{(\\w+):(\\w+)\\}\\}.*");
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {

                line = line.replaceAll("\\{\\{(.+):(.+)\\}\\}", "\"+this.getArray(\""
                        + matcher.group(1) + "\")["
                        + matcher.group(2) + "]+\"");
            } else {
                line = line.replace("{{", "\"+this.get(\"");
                line = line.replace("}}", "\")+\"");
            }
        }
        try {
            writer.write("\"" + line + "\"+");

        } catch (IOException ex) {
            Logger.getLogger(TemplateCompile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 不写入引号
     *
     * @param line
     * @param eval
     */
    protected void writerFile(String line, boolean eval) {

        //首先把所有变量修改{{}}
        Pattern pat = Pattern.compile("\\(.*\\$([_a-zA-Z][_a-zA-Z0-9]*):?(\\w*).*\\)");
        Matcher mat = pat.matcher(line);

        if (mat.find()) {
            String param = mat.group(1);
            String valueType = "";
            if (mat.group(2).length() > 0) {
                valueType = mat.group(2).replaceFirst(mat.group(2).substring(0, 1), mat.group(2).substring(0, 1).toUpperCase());
            }
            line = line.replaceAll("\\$([_a-zA-Z][_a-zA-Z0-9]*):?(\\w*)", "this.get"
                    + valueType
                    + "(\"" + param + "\")");
        }
        try {
            if (!this.isExtends) {
                writer.write("" + line + "+");
            }
        } catch (IOException ex) {
            Logger.getLogger(TemplateCompile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 处理结尾，把block写在里面
     */
    protected void flush() {
        try {
            if (!this.isExtends) {
                this.writer.write("\"\";return str;\n}");
            }
            for (int i = 0; i < this.block.size(); i++) {
                try {
                    this.writer.write("public String " + this.block.get(i) + " (){ String str = \"\"" + this.blockContent.get(i) + ";return str;}");

                } catch (IOException ex) {
                    Logger.getLogger(TemplateCompile.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            this.writer.write("\n}");
        } catch (IOException ex) {
            Logger.getLogger(TemplateCompile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
