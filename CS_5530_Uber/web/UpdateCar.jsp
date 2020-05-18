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
            if( form_obj.tid.value == "" || form_obj.vin.value == "" || form_obj.category.value == "" || form_obj.make.value == "" ){
                alert("Fields should be nonempty");
                return false;
            }
            return true;
        }

    </script>
</head>
<body>

<h1 align="center" style="font-size: x-large">
    Submit TID to update car
</h1>
<form align="center" method="post" onsubmit="return check_all_fields(this)" >
    <input type=hidden name="searchAttribute1" value="register">
    <table align="center" >
        <tr>
            <td>TID</td>
            <td><input type="text" name="tid" /></td>
        </tr>

        <tr>
            <td>VIN</td>
            <td><input type="text" name="vin" /></td>
        </tr>

        <tr>
            <td>Category</td>
            <td><input type="text" name="category" /></td>
        </tr>

        <tr>
            <td>Make</td>
            <td><input type="address" name="make" /></td>
        </tr>


        <tr>
            <td>Model</td>
            <td><input type="address" name="model" /></td>
        </tr>
    </table>
    <input type="submit" value=" Update Car"/>
</form>

<BR>
<form align="center" name="Cancel" action="DriverMenu.jsp">
    <input type="submit" value="Cancel">
</form>

<%
    String x = request.getParameter("searchAttribute1");

    if(x!=null && x.equals("register"))
    {
        String category = request.getParameter("category");
        String make = request.getParameter("make");
        String model = request.getParameter("model");
        String login = (String)session.getAttribute("login");

        Connector con = null;

        try {
            int tid =Integer.parseInt(request.getParameter("tid"));
            int vin =Integer.parseInt(request.getParameter("vin"));
            con = new Connector();
            UberCar.UpdateCar(tid, vin, category, make, model, login, con.stmt);

            %>
                <script LANGUAGE="JavaScript">
                    alert("Success updating")
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
                        window.location = "DriverMenu.jsp";
                    </script>
                <%
                } catch (Exception e) { /* ignore close errors */ }
            }
        }
    }
%>
</body>
</html>