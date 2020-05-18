<%@ page language="java" import="cs5530.*" %>
<%--
  Created by IntelliJ IDEA.
  User: mtp8
  Date: 4/22/2018
  Time: 2:26 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Browse</title>
</head>
<body>

<%
    String searchAttribute = request.getParameter("searchAttribute");
    if( searchAttribute == null ) {
%>
<form align="center" name="findCars">
    <input type=hidden name="searchAttribute" value="findCars">
    <b align="center">To search for cars, enter the make or model of the car</b>
    <br />
    <input type=text name="car" length=10>
    <input type="submit" value="Find Cars">
</form>
<BR>
<form align="center" name="driverFeedback">
    <input type=hidden name="searchAttribute" value="driverFeedback">
    <b align="center">Get feedback for driver:</b>
    <br />
    Driver username: <input type=text name="username">
    <br />
    Feedback count: <input type=number name="count" length=10>
    <br />
    <input type="submit" value="Get Feedback for Drivers">
</form>
<BR>
<form align="center" name="stats" action="StatisticsMenu.jsp">
    <input type="submit" value="Statistics">
</form>
<BR>
<form align="center" name="awards" action="Awards.jsp">
    <input type="submit" value="Awards">
</form>
<BR>
<form align="center" name="Back" action="index.jsp">
    <input type="submit" value="Back">
</form>

<%
    } else {

    Connector con = null;
    try {
        con = new Connector();
        if(searchAttribute.equals("findCars")) {
            String car = request.getParameter("car");
            out.println(UberCar.FindCars(car, con.stmt));
        }
        if(searchAttribute.equals("driverFeedback")) {
            String username = request.getParameter("username");
            int count = Integer.parseInt(request.getParameter("count"));
            out.println(UberUser.getUsefulDriverFeedback(username, count, con.stmt));
        }
%>
        <form action="BrowseMenu.jsp">
            <input type="submit" value="Back">
        </form>
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
                } catch (Exception e) { /* ignore close errors */ }
            }
        }
    }
%>
</body>
</html>
