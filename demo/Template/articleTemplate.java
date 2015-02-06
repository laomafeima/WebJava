package Template;
public class articleTemplate extends baseTemplate{




public String body (){ String str = ""+
"<div>"+
"<h1>"+this.getArray("article")[1]+"</h1>"+
"<h3>发表时间："+this.getArray("article")[3]+"</h3>"+
"<div>"+this.getArray("article")[2]+"</div>"+
"</div>";return str;}
}