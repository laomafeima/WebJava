package Template;
public class indexTemplate extends baseTemplate{













public String body (){ String str = ""+
"<div>"+
"<table border=\"1\">"+
"<tr>"+
"<th>标题</th>"+
"<th>发表时间</th>"+
"</tr>"+
"";for(int i = 0; i < this.getArray2("articles").length; i++){str=str
+
"<tr>"+
"<td><a href=\"/Article/"+this.getArray2("articles")[i][0]+"\">"+
""+this.getArray2("articles")[i][1]+"</a>"+
"</td>"+
"<td>"+this.getArray2("articles")[i][2]+"</td>"+
"</tr>"+
"";}str=str
+
"</table>"+
"</div>";return str;}
}