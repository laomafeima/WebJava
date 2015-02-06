package Template;
public class loginTemplate extends baseTemplate{






public String body (){ String str = ""+
"<div>"+
"<form method=\"post\" action=\"/Login\">"+
"<input name=\"name\" type=\"text\">"+
"<input name =\"password\" type=\"password\">"+
"<input type=\"submit\" value=\"Submit\">"+
"</form>"+
"</div>";return str;}
}