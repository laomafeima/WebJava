
模版说明
使用继承
顶顶顶顶顶
{% block body %}
XXXX
hkud
{% end %}
开始和end标记之间是默认内容，如果没有被继承就显示默认内容否则显示

文件的开始处生命继承的模版只允许单一继承（一层继承关系 待定）

一个文件可以包含多个文本块。在继承的文件只实现多个

{%block de%}
得到

dddd{%if(true)%}
<div class="de">dsssssssssssssssss</div>
{%endif%}sss
{% end %}
语法
测试语法开始
<br>
<br>
测试For
{%for(int i = 0; i < $for:array.length; i++)%}
城市是：{{for:i}}
{%endfor%}
<br>
<br>
{{frist}}
dddd{%if(true)%}
<div class="de">dddddddddddddddddddddd</div>
{%elseif($name:boolean&&true)%}
<div id="s">fsssssssssssssssfffff</div>
{%else%}
<div id="s">fffffffffffff</div>
{%endif%}sss

{{name}}

{%for(int i=0;i<5;i++)%}
for循环的测试点点滴滴点点滴滴的的的的的的
{%endfor%}

{#de = de+"fr"#}
测试执行任意Java语句

{{}} 表示 输出变量
{%%}if for 语句
{##}执行的语句

######
以上部分是文档说明
