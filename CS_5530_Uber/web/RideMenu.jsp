<%@ page language="java" import="cs5530.*" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="java.util.Date" %>
<%--
  Created by IntelliJ IDEA.
  User: mtp8
  Date: 4/22/2018
  Time: 3:01 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Ride Menu</title>

</head>
<body>

<h1 align="center" style="font-size: x-large">
    Ride Menu
</h1>
<form align="center" name="reserveRide" action="ReserveRide.jsp">
    <input type=hidden name="searchAttribute" value="reserve" >
    <input type="submit" value="Reserve Ride">
</form>
<BR>
<form align="center" name="recordRide">
    <input type=hidden name="searchAttribute" value="record">
    <p align="center">Enter the vin of the car you rode in:</p>
    <input type=number name="vin">
    <br/>
    Cost:
    <input type=number name="cost">
    <br/>
    Date (yyyy-mm-dd hh:mm:ss):
    <input type="text" name="date">
    <br/>
    <input type="submit" value="Record Ride">
</form>
<BR>
<form align="center" name="favoriteCar">
    <input type=hidden name="searchAttribute" value="favorite">
    <p align="center">Enter the vin of the car you would like to favorite:</p>
    <input type=number name="vin">
    <input type="submit" value="Favorite a Car">
</form>
<BR>
<form align="center" name="rateCarGiveFeedback">
    <input type=hidden name="searchAttribute" value="feedback">
    <p align="center">Enter the vin of the car you would like to rate:</p>
    <input type=number name="vin">
    <br/>
    Rating:
    <select name="rating">
        <option value=0>0</option>
        <option value=1>1</option>
        <option value=2>2</option>
        <option value=3>3</option>
        <option value=4>4</option>
        <option value=5>5</option>
        <option value=6>6</option>
        <option value=7>7</option>
        <option value=8>8</option>
        <option value=9>9</option>
        <option value=10>10</option>
    </select>
    <br/>
    Feedback:
    <input type=text name="feedback">
    <br/>
    <input type="submit" value="Rate a car and give feedback">
</form>
<BR>
<form align="center" name="rateOtherFeedback">
    <input type=hidden name="searchAttribute" value="rateFeedback">
    <p align="center">Enter the id of the feedback you would like to rate:</p>
    <input type=number name="fid">
    <br/>
    Rating:
    <select name="rating">
        <option value=0>Useless</option>
        <option value=1>Useful</option>
        <option value=2>Very Useful</option>
    </select>
    <br/>
    <input type="submit" value="Rate other users feedback">
</form>
<BR>
<form align="center" name="rateUser">
    <input type=hidden name="searchAttribute" value="rateUser">
    <p align="center">Enter the login of the user you would like to rate:</p>
    <input type=text name="trustee">
    <br/>
    Rating:
    <select name="rating">
        <option value=false>No</option>
        <option value=true>Yes</option>
    </select>
    <br/>
    <input type="submit" value="Rate other user">
</form>
<BR>
<form align="center" name="rateOtherUsers">
    <input type="submit" value="Logout">
</form>
<BR>
<form align="center" name="back" action="WelcomeLogin.jsp">
    <input type="submit" value="Back">
</form>
<%
    String login = (String)session.getAttribute("login");
    String searchAttribute = request.getParameter("searchAttribute");
    if(searchAttribute !=null)
    {
        Connector con = null;
        try {
            con = new Connector();
            if(searchAttribute !=null && searchAttribute.equals("record")) {
                int vin = Integer.parseInt(request.getParameter("vin"));
                int cost = Integer.parseInt(request.getParameter("cost"));
                String sdate = request.getParameter("date");
%>
<script type="text/javascript">
    if (confirm("Are you sure you want to record a ride in the car with vin '<%=vin%>' for <%=cost%> on <%=sdate%>?")) {
        <%
            Timestamp rdate = java.sql.Timestamp.valueOf(sdate);
                        UberCar.recordRide(login, vin, cost, rdate, con.stmt);
        %>
    }
</script>
<%

    }

                if(searchAttribute.equals("favorite")) {
                    int vin = Integer.parseInt(request.getParameter("vin"));
                    UberCar.favoriteCar(login, vin, con.stmt);
    %>

<script type="text/javascript">
    alert("Car with vin '<%=vin%>' favorited");

</script>
<%
    }
    if(searchAttribute.equals("feedback")) {
        int vin = Integer.parseInt(request.getParameter("vin"));
        int rating = Integer.parseInt(request.getParameter("rating"));
        String fbtext = request.getParameter("feedback");
        Timestamp fbdate = new Timestamp(new Date().getTime());
        UberCar.rateCar(login, vin, rating, fbtext, fbdate, con.stmt);
    }
    if(searchAttribute.equals("rateFeedback")) {
        int fid = Integer.parseInt(request.getParameter("fid"));
        int rating = Integer.parseInt(request.getParameter("rating"));
        UberUser.rateFeedback(login, fid, rating, con.stmt);
    }
    if(searchAttribute.equals("rateUser")) {
        String trustee = request.getParameter("trustee");
        boolean rating = Boolean.parseBoolean(request.getParameter("rating"));
        UberUser.rateUser(login, trustee, rating, con.stmt);
    }

%>
<script type="text/javascript">
    // window.location = "RideMenu.jsp";
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
                } catch (Exception e) { /* ignore close errors */ }
            }
        }
    }
%>
</body>
</html>
