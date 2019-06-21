<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%!
int counter = 1;//注意：多用户将共享此变量，此进度条只适合单用户
%>
<%
String task = request.getParameter("task");
String res = "";
 
if (task.equals("create")) {
res = "1";
counter = 1;
}
else {
String percent = "";
switch (counter) {
case 1: percent = "10"; break;
case 2: percent = "23"; break;
case 3: percent = "35"; break;
case 4: percent = "51"; break;
case 5: percent = "64"; break;
case 6: percent = "73"; break;
case 7: percent = "89"; break;
case 8: percent = "100"; break;
}
counter++;
 
res = "<percent>" + percent + "</percent>";
}
response.setContentType("text/xml");
response.setHeader("Cache-Control", "no-cache");
out.println("<response>");
out.println(res);
out.println("</response>");
out.close();     
%>
    