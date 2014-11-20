#Web.Java 简介
=========
轻量级，无依赖
----
你是不是受够了J2EE的庞大臃肿？让Tomcat，GlassFlish等等应用服务器去死吧。Web.Java没有任何依赖，只要安装JDK7及以上版本就可以独立运行。不需要安装其他任何的东西。
当然，如果你要使用数据库需要需要相应的JDBC。如果你想，你可以使用任何你想使用的扩展。
非阻塞，高性能
----
Web.Java是一个高性能，轻量级的非阻塞式服务器，而且速度相当快。  
为了能更好的提高性能，Web.Java把HTTP服务区分应用和文件服务两种。
  - Web.Java 整体采用Reactor模式用来接收或响应HTTP请求（原理同Nginx）。
  - 应用服务采用了Reactor来响应请求。
  - 文件服务采用了Proactor模式，并搭配304状态使用，能极大的提高静态文件的相应速度，且不影响整体。

模板引擎
----
 - 简单的标签：只需要记住{{}}标签用于输出变量，{%%}标签用于if,for 等操作。
 - 可以继承的模版：页面的布局，HTML文件的复用等问题，通过模版继承机制可以得到解决。用{%extends xx%}关键字实现继承。  
 - 高性能：Web.Java会自动把Html文件，编译成Java文件。应用到生产环境的时候，速度等同于Java的硬输出。且在DEBUG模式下，会动态的加载模板文件，而不需要重启进程。



灵活漂亮的URL
----
随便打开几个J2EE的网站，我就不想吐槽那个URL了。Web.Java使用正则表达式来配置URL，这样做可以提供足够强大和灵活的URL模式。比如像用“/Article/23”想获取文章的ID可以这样来配置URL  
```
HttpServer.setPATH("/Article/(\\d+)",new ArticleHandler());  

//ArticleHandler.java文件 get方法 
public void get(String id){  
  //获取ID进行其他操作
  ……  
}  
```  
这样在ArticleHandler中对应的get或者post方法中就会获取相应的参数。当然，你可以任意的使用正则表达式来配置你的URL  

简单易用的数据库操作
----
```
Options.DBURL = "jdbc:mysql://localhost:3306/test";
Options.DBDriver = "com.mysql.jdbc.Driver";
Options.DBUser = "root";
Options.DBPassword = "123456";
```
配置好数据库信息后，可以直接在Handler中使用DB中的静态方法进行操作。  
具体的操作在DB中有说明。当然如果你想，可以使用任何你想用的ORM。当然，希望你能直接使用SQL，不想解释为什么。

Hello World!
----
看名字就知道了。让我们开始使用Web.Java吧！  
把源码包放到你的项目目录下面。
然后在main方法中加入
```
HttpServer.setPATH("/", new IndexHandler());
System.out.println("Listen 8080");
HttpServer.init(8080);
```
IndexHandler.java
```
public void get() {
    this.writer("Hello World!");
}
```
现在Run it 浏览器打开就会看到你Writer的内容了。  
