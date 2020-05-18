<%@ page language="java" import="cs5530.*" %>
<%--
  Created by IntelliJ IDEA.
  User: mtp8
  Date: 4/22/2018
  Time: 3:07 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Driver Menu</title>
</head>
    <body>

        <%
            String searchAttributes = request.getParameter("searchAttributes");
            if( searchAttributes == null ) {
        %>

        <h1 align="center" style="font-size: x-large">
            Driver Menu
        </h1>
        <form align="center" name="newCar" action="NewCar.jsp">
            <input type="submit" value="New Car">
        </form>
            <BR>
        <form align="center" name="updateCar" action="UpdateCar.jsp">
            <input type=hidden name="searchAttributes" value="updateCar">
            <input type="submit" value="Update Car">
        </form>
            <BR>
        <form align="center" name="becomeDriver">
            <input type=hidden name="searchAttributes" value="becomeDriver">
            <input type="submit" value="Become a driver">
        </form>
            <BR>
        <form align="center" name="back" action="WelcomeLogin.jsp">
            <input type="submit" value="Back">
        </form>

    <%  }  else {

        Connector con = null;
        String login = (String)session.getAttribute("login");

        try {

            con = new Connector();


            if(searchAttributes.equals("becomeDriver"))
            {
                UberUser.becomeUberDriver(login, con.stmt);
                %>
                    <script LANGUAGE="JavaScript">
                        alert("Success you are a driver")
                    </script>
                    <script type="text/javascript">
                        window.location = "WelcomeLogin.jsp";
                    </script>
                <%
            }
        }catch(Exception e){

        }
    }
    %>
    </body>
</html>