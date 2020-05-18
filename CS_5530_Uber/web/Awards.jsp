<%@ page language="java" import="cs5530.*" %>
<%--
  Created by IntelliJ IDEA.
  User: mtp8
  Date: 4/22/2018
  Time: 2:41 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Awards</title>
</head>
<body>
<%
    String searchAttribute = request.getParameter("searchAttribute");
    if( searchAttribute == null ) {
%>
<form align="center" name="trustedUser">
    <input type=hidden name="searchAttribute" value="trusted">
    <p align="center">Enter the number of trusted users you would like to see:</p>
    <input type=number name="count">
    <input type="submit" value="Top Trusted Users">
</form>
<BR>
<form align="center" name="usefulUser">
    <input type=hidden name="searchAttribute" value="useful">
    <p align="center">Enter the number of useful users you would like to see:</p>
    <input type=number name="count">
    <input type="submit" value="Top Useful Users">
</form>
<BR>
<form align="center" name="Back" action="BrowseMenu.jsp">
    <input type="submit" value="Back">
</form>
<%
} else {
    Connector con = null;
    try {
        con = new Connector();
        int count = Integer.parseInt(request.getParameter("count"));
        if(searchAttribute.equals("trusted")) {
            out.println(UberUser.getTopTrustedUsers(count, con.stmt));
        } else if (searchAttribute.equals("useful")){
            out.println(UberUser.getTopUsefulUsers(count, con.stmt));
        }
%>
<form action="Awards.jsp">
    <button type="submit">Go Back</button>
    <form/>
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
