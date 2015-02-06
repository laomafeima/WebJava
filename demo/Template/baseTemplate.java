package Template;
public class baseTemplate extends Web.BaseTemplate{
	public String display(){
		String str= 
"<!DOCTYPE html>"+
"<html>"+
"<head>"+
"<title>"+this.get("title")+"</title>"+
"<link rel=\"shortcut icon\" href=\"/favicon.ico\">"+
"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"+
""+this.head()+"</head>"+
"<body>"+
"<a href=\"/Create\" >添加文章</a>"+
""+this.body()+
"</body>"+
"</html>"+
"";return str;
}public String head (){ String str = "";return str;}public String body (){ String str = ""+
"欢迎归来";return str;}
}