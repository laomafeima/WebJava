package Template;
public class createTemplate extends baseTemplate{










public String body (){ String str = ""+
"<div>"+
"<form method=\"post\" action=\"/Create\">"+
"<div>"+
"<input name=\"title\" type=\"text\">"+
"</div>"+
"<div>"+
"<textarea name=\"content\"></textarea>"+
"</div>"+
"<input type=\"submit\" value=\"Submit\">"+
"</form>"+
"</div>";return str;}
}