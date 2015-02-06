package Template;
public class testTemplate extends Web.BaseTemplate{
	public String display(){
		String str= 
"模版说明"+
"使用继承"+
"顶顶顶顶顶"+
""+this.body()+

"开始和end标记之间是默认内容，如果没有被继承就显示默认内容否则显示"+
"文件的开始处生命继承的模版只允许单一继承（一层继承关系 待定）"+
"一个文件可以包含多个文本块。在继承的文件只实现多个"+
""+this.de()+

"语法"+
"测试语法开始"+
"<br>"+
"<br>"+
"测试For"+
"";for(int i = 0; i < this.getArray("for").length; i++){str=str
+"城市是："+this.getArray("for")[i]+""+
"";}str=str
+"<br>"+
"<br>"+
""+this.get("frist")+""+
"dddd";if(true){str=str
+"<div class=\"de\">dddddddddddddddddddddd</div>"+
"";}else if(this.getBoolean("name")&&true){str=str
+"<div id=\"s\">fsssssssssssssssfffff</div>"+
"";}else{str=str
+"<div id=\"s\">fffffffffffff</div>"+
"";}str=str+"sss"
+""+this.get("name")+""+
"";for(int i=0;i<5;i++){str=str
+"for循环的测试点点滴滴点点滴滴的的的的的的"+
"";}str=str
+"{#de = de+\"fr\"#}"+
"测试执行任意Java语句"+
"{{}} 表示 输出变量"+
"{%%}if for 语句"+
"{##}执行的语句"+
"######"+
"以上部分是测试文档"+
"下面是文档说明"+
"这个是一个"+
"";return str;
}public String body (){ String str = ""+
"XXXX"+
"hkud";return str;}public String de (){ String str = ""+
"得到"+
"dddd";if(true){str=str
+
"<div class=\"de\">dsssssssssssssssss</div>"+
"";}str=str+"sss"
;return str;}
}