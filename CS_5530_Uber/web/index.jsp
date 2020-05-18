<%@ page language="java" import="cs5530.*" %>
<%--
  Created by IntelliJ IDEA.
  User: mtp8
  Date: 4/20/2018
  Time: 8:00 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<html>
<head>
    <script LANGUAGE="javascript">
        function check_all_fields(form_obj){
            alert(form_obj.searchAttribute.value+"='"+form_obj.attributeValue.value+"'");
            if( form_obj.name.value == "" && form_obj.password.value){
                alert("Search field should be nonempty");
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
<BR><BR><BR><BR>
<h1 align="center" style="font-size: x-large">
    Welcome to U of U Uber Database
</h1>

<form align="center" name="Login" method="get" onsubmit="return check_all_fields(this)" action="index.jsp">
    <input type=hidden name="searchAttribute" value="login">
    Username:
    <input name="login" type="text">
    Password:
    <input name="password" type="password">
    <input type="submit" value="Login">
</form>
<BR>
<form align="center" name="Register User" action="Registration.jsp">
    <input type="submit" value="New User">
</form>
<BR>
<form align="center" name="Browse" action="BrowseMenu.jsp">
    <input type="submit" value="Browse">
</form>

<%
    String x = request.getParameter("searchAttribute");
    if(x!=null && x.equals("login"))
    {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        Connector con = null;
        try {
            con = new Connector();
            if(UberUser.Login(login, password, con.stmt)) {
                session.setAttribute("login", login);
%>
<script type="text/javascript">
    window.location = "WelcomeLogin.jsp";
</script>
<%
    }
        }
        catch (Exception e)
        {
            e.printStackTrace();
%>
    <script LANGUAGE="javascript">
        alert("Either connection error or query execution error!");
    </script>
<%
        }
        finally {
            if (con != null) {
                try {
                    con.closeConnection();
                } catch (Exception e) { /* ignore close errors */ }
            }
        }
    }
%>

</body>
</html>
