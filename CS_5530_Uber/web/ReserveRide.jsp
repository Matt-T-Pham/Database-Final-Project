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
    Reserve a Ride
</h1>
<form align="center" method="post" onsubmit="return check_all_fields(this)" >
    <input type=hidden name="searchAttribute2" value="register">
    <table align="center" >
        <tr>
            <td>VIN</td>
            <td><input type="text" name="vin" /></td>
        </tr>

        <tr>
            <td>PID</td>
            <td><input type="test" name="pid" /></td>
        </tr>
    </table>
    <input type="submit" value="Register User"/>
</form>

<BR>
<form align="center" name="Cancel" action="RideMenu.jsp">
    <input type="submit" value="Cancel">
</form>
<%
    String x = request.getParameter("searchAttribute2");
    String login = (String)session.getAttribute("login");


    if(x!=null && x.equals("register"))
    {
        Connector con = null;

        try {
            int pid = Integer.parseInt(request.getParameter("pid"));
            int vin = Integer.parseInt(request.getParameter("vin"));
            con = new Connector();
            UberCar.reserveRide(login,vin,pid ,con.stmt);

        %>
            <script LANGUAGE="JavaScript">
                alert("Success reserving ride")
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
                window.location = "RideMenu.jsp";
            </script>
        <%
                        } catch (Exception e) { /* ignore close errors */ }
                    }
                }
            }
        %>
</body>
</html>