<%@ page language="java" import="cs5530.*" %>
<%--
  Created by IntelliJ IDEA.
  User: mtp8
  Date: 4/22/2018
  Time: 2:35 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Statistics</title>
</head>
    <body>
    <%
        String searchAttribute = request.getParameter("searchAttribute");
        if( searchAttribute == null ) {
    %>
             <p align="center">Enter number of popular cars to list</p>
        <form align="center" name="popularCars">
            <input type=hidden name="searchAttribute" value="popCars">
            <input type="number" name="count">
            <input type="submit" value="Most Popular Cars">
        </form>
            <BR>
         <p align="center">Enter number of expensive cars to list</p>
        <form align="center" name="expensiveCars">
            <input type=hidden name="searchAttribute" value="expensiveCars">
            <input type="number" name="count">
            <input type="submit" value="Most Expensive Cars">
        </form>
            <BR>
            <p align="center">Enter number of highest rated drivers to list</p>
        <form align="center" name="highestRatedDriver">
            <input type=hidden name="searchAttribute" value="highestRatedDriver">
            <input type="number" name="count">
            <input type="submit" value="Highest Rated Drivers">
        </form>
            <BR>
        <form align="center" name="back" action="BrowseMenu.jsp">
            <input type="submit" value="Back">
        </form>

    <%
        } else {
            Connector con = null;
            try {

                con = new Connector();

                int count = Integer.parseInt(request.getParameter("count"));

                if(searchAttribute.equals("popCars"))
                {
                    out.println(UberCar.getMostPopularCars(count,con.stmt));
                    %>
                    <form name="back" action="StatisticsMenu.jsp">
                        <input type="submit" value="Back">
                    </form>
                    <%
                }
                if(searchAttribute.equals("expensiveCars"))
                {
                    out.println(UberCar.getMostExpensiveCars(count,con.stmt));
                    %>
                        <form name="back" action="StatisticsMenu.jsp">
                            <input type="submit" value="Back">
                        </form>
                     <%
                }
                if(searchAttribute.equals("highestRatedDriver"))
                {
                    out.println(UberUser.getHighestRatedDrivers(count,con.stmt));

                     %>
                        <form name="back" action="StatisticsMenu.jsp">
                            <input type="submit" value="Back">
                        </form>
                    <%
                }
            }
            catch(Exception e){
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