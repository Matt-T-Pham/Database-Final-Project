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
            //alert(form_obj.username.value +" | "+ form_obj.password.value +" | "+ form_obj.name.value +" | "+ form_obj.address.value +" | "+ form_obj.phone.value)
            if( form_obj.username.value == "" || form_obj.password.value == "" || form_obj.name.value == "" || form_obj.address.value == "" || form_obj.phone.value == ""){
                alert("Fields should be nonempty");
                return false;
            }
            return true;
        }

    </script>
</head>
<body>
<h1 align="center" style="font-size: x-large">
    Register a new user
</h1>
<form align="center" method="post" onsubmit="return check_all_fields(this)" >
    <input type=hidden name="searchAttribute" value="register">
    <table align="center" >
        <tr>
            <td>Username</td>
            <td><input type="text" name="username" /></td>
        </tr>

        <tr>
            <td>Password</td>
            <td><input type="password" name="password" /></td>
        </tr>

        <tr>
            <td>Name</td>
            <td><input type="text" name="name" /></td>
        </tr>

        <tr>
            <td>Address</td>
            <td><input type="address" name="address" /></td>
        </tr>

        <tr>
            <td>Phone</td>
            <td><input type="text" name="phone" maxlength="10"/></td>
        </tr>
    </table>
    <input type="submit" value="Register User"/>
</form>

<BR>
<form align="center" name="Cancel" action="index.jsp">
    <input type="submit" value="Cancel">
</form>
<%
    String x = request.getParameter("searchAttribute");
    if(x!=null && x.equals("register"))
    {
        String login = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        Connector con = null;

        try {
            con = new Connector();
            UberUser.InsertUser(login, password, name, address, phone, con.stmt);

            %>
            <script LANGUAGE="JavaScript">
                alert("Success creating new user")
            </script>
            <%

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
                    %>
                        <script type="text/javascript">
                            window.location = "index.jsp";
                        </script>
                    <%
                } catch (Exception e) { /* ignore close errors */ }
            }
        }
    }
%>
</body>
</html>