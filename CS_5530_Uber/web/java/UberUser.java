// **** CS 5530 Uber final Project ****
// **** Greg Rosich & Matthew Pham ****

package cs5530;

import java.sql.*;
import java.util.HashMap;

public class UberUser {

    public static void InsertUser(String login, String password, String name,
                                    String address, String phone, Statement stmt)
    {
        String sql = "INSERT INTO UU (login, password, name, address, phone) VALUES('"+login+"','"+password+"','"+
                name+"','"+address+"','"+phone+"')";
//        System.out.println("executing "+sql);

        int rc = 0;
        try{
            rc = stmt.executeUpdate(sql);
        }
        catch(Exception e)
        {
            System.out.println("cannot execute the query: \n" + e);
        }

        if(rc == 1) {
            System.out.println("User '" + login + "' created");
        } else {
            System.out.println("unexpected result count: " + rc);
        }
    }

    public static boolean Login(String login, String password, Statement stmt)
    {
        String sql="SELECT COUNT(*) AS count FROM UU WHERE login = '"+login+"' AND password = '"+password+"'";
        ResultSet rs = null;
//        System.out.println("executing "+sql);
        try{
            rs = stmt.executeQuery(sql);
            while (rs.next())
            {
                int count = rs.getInt("count");
                return count == 1;
            }

            rs.close();
        }
        catch(SQLException e)
        {
            System.out.println("cannot execute the query: \n" + e);
        }
        finally
        {
            try
            {
                if (rs!=null && !rs.isClosed())
                    rs.close();
            }
            catch(Exception e)
            {
                System.out.println("cannot close resultset");
            }
        }
        return false;
    }

    public static void becomeUberDriver(String login, Statement stmt) {
        String sql = "INSERT INTO UD (login) VALUES('"+login+"')";
//        System.out.println("executing "+sql);

        int rc = 0;
        try{
            rc = stmt.executeUpdate(sql);
        }
        catch(Exception e)
        {
//            System.out.println("cannot execute the query: \n" + e);
        }

        if(rc == 1) {
            System.out.println("You are now a driver!");
        } else if(rc == 0) {
            System.out.println("You were already a driver!");
        } else {
            System.out.println("unexpected result count: " + rc);
        }
    }

    public static void rateFeedback(String login, int fid, int rating, Statement stmt) {
        String sql = "INSERT INTO Rates (login, fid, rating) VALUES('"+login+"','"+fid+"','"+rating+"')";
//       System.out.println("executing "+sql);

        int rc = 0;
        try{
            rc = stmt.executeUpdate(sql);
        }
        catch(SQLException e)
        {

            if(e.getErrorCode() == MYSQLErrorCodes.DUPLICATE) {
                System.out.println("You have already rated this feedback");
            } else if(e.getErrorCode() == MYSQLErrorCodes.FOREIGN_KEY){
                System.out.println("Ivalid Feedback ID");
            } else if(e.getErrorCode() == MYSQLErrorCodes.USER) {
                System.out.println("You cannot rate your own feedback");
            } else {
                System.out.println("cannot execute the query: \n" + e);
            }
        }

        if(rc == 1) {
            System.out.println("Feedback rated");
        } else if(rc == 0) {
            // nothing happened
        } else {
            System.out.println("unexpected result count: " + rc);
        }
    }

    public static void rateUser(String login, String trustee, boolean isTrusted, Statement stmt) {

        String sqlCheckExist = "SELECT trustee FROM Trust WHERE trustee = '"+trustee+"'";
        String output = "";
        ResultSet rs = null;
        try{
            rs = stmt.executeQuery(sqlCheckExist);
            while (rs.next())
            {
                output += rs.getString("trustee");
            }
            rs.close();

            if(output.length() == 0 )
            {
                String sql = "INSERT INTO Trust (truster, trustee, isTrusted) VALUES('"+login+"','"+trustee+"','"+(isTrusted ? 1 : 0)+"')";

                //        System.out.println("executing "+sql);

                int rc = 0;
                try{
                    rc = stmt.executeUpdate(sql);
                }
                catch(SQLException e)
                {
                    if(e.getErrorCode() == MYSQLErrorCodes.FOREIGN_KEY){
                        System.out.println("Ivalid Feedback ID");
                    }
                    else {
                        System.out.println("cannot execute the query: \n" + e);
                    }
                }

                if(rc == 1) {
                    System.out.println("User rated");
                } else if(rc == 0) {
                    // nothing happened
                } else {
                    System.out.println("unexpected result count: " + rc);
                }
            }
            else{
                String sqlTrust = "UPDATE Trust SET isTrusted = '"+(isTrusted ? 1 : 0)+"' where trustee = '"+trustee+"'";

                int rcTrust = 0;

                try{
                    rcTrust = stmt.executeUpdate(sqlTrust);
                }
                catch(SQLException e)
                {
                        System.out.println("cannot execute the query: \n" + e);
                }

                if(rcTrust == 1) {
                    System.out.println("User rated");
                } else if(rcTrust == 0) {
                    // nothing happened
                } else {
                    System.out.println("unexpected result count: " + rcTrust);
                }
            }
        }
        catch(Exception e)
        {
            SQLException sqle = (SQLException) e;
            System.out.println("cannot execute the query: \n" + sqle);
        }
    }

    public static String getTopTrustedUsers(int count, Statement stmt)
    {
        String sql="SELECT trustee, yes-no AS delta \n" +
                "FROM (\n" +
                "  SELECT trustee, SUM(isTrusted = 0) AS no, SUM(isTrusted = 1) AS yes \n" +
                "  FROM Trust \n" +
                "  GROUP BY trustee) t\n" +
                "ORDER BY delta DESC\n"+
                "LIMIT " + count;
        String output="\n<BR>Most Trusted Users:<BR>\n";
        ResultSet rs=null;
//        System.out.println("executing "+sql);
        try{
            rs = stmt.executeQuery(sql);
            while (rs.next())
            {
                output+="   '"+rs.getString("trustee")+"' - trust score: "+rs.getInt("delta")+"<BR>\n";
            }

            rs.close();
        }
        catch(Exception e)
        {
            System.out.println("cannot execute the query");
        }
        finally
        {
            try{
                if (rs!=null && !rs.isClosed())
                    rs.close();
            }
            catch(Exception e)
            {
                System.out.println("cannot close resultset");
            }
        }
        return output;
    }

    public static String getTopUsefulUsers(int count, Statement stmt)
    {
        String sql="SELECT f.login, AVG(r.rating) r\n" +
                "FROM Rates r, Feedback f\n" +
                "WHERE r.fid = f.fid\n" +
                "GROUP BY f.login\n" +
                "ORDER BY r DESC\n" +
                "LIMIT " + count;
        String output="\n<BR>Most Useful Users:<BR>\n";
        ResultSet rs=null;
//        System.out.println("executing "+sql);
        try{
            rs = stmt.executeQuery(sql);
            while (rs.next())
            {
                output+="   "+rs.getString("login")+"' - ";
                String use = "";
                switch (rs.getInt("r")){
                    case 0:
                    {
                        use = "useless";
                    }
                    break;
                    case 1:
                    {
                        use = "useful";
                    }
                    break;
                    case 2:
                    {
                        use = "very userful";
                    }
                    break;
                }
                output += use + "<BR>\n";
            }

            rs.close();
        }
        catch(Exception e)
        {
            System.out.println("cannot execute the query");
        }
        finally
        {
            try{
                if (rs!=null && !rs.isClosed())
                    rs.close();
            }
            catch(Exception e)
            {
                System.out.println("cannot close resultset");
            }
        }
        return output;
    }

    public static String getHighestRatedDrivers(int count, Statement stmt) {

        String sql = "SELECT u.login, AVG(f.rating), f.rating FROM Feedback f,UC u WHERE u.vin = f.vin GROUP BY u.login,f.rating ORDER BY 2 DESC LIMIT "+count+"";

        ResultSet rs = null;
        System.out.println("executing "+ sql);

        String output = "\n<BR>Highest Rated Drivers: <BR>\n";
        try{
            rs = stmt.executeQuery(sql);

            while (rs.next())
            {
                output += "<BR>   '"+rs.getString("login")+"' - score: "+rs.getString("rating")+"<BR>\n";
            }
            rs.close();
        }
        catch(Exception e)
        {
            System.out.println(sql);
            SQLException sqle = (SQLException) e;
            System.out.println("cannot execute the query: \n" + e);
        }
        finally
        {
            try
            {
                if (rs!=null && !rs.isClosed())
                    rs.close();
            }
            catch(Exception e)
            {
                System.out.println("cannot close resultset");
            }
        }
        return output;
    }

    public static String getUsefulDriverFeedback(String driverLogin, int count, Statement stmt) {
        // TODO: stub

        String sql = "Select r.login, AVG(r.rating),r.rating FROM Rates r WHERE login = '"+driverLogin+"' GROUP BY r.login,r.rating ORDER BY 2 DESC LIMIT "+count+"";
        ResultSet rs = null;
        String output= "";

        try{
            rs = stmt.executeQuery(sql);
            while (rs.next())
            {
                output +="Login Name: " +rs.getString("login")+"     Average Rating: "+rs.getString("rating")+"\n";
            }
            rs.close();
        }
        catch(SQLException e)
        {
            System.out.println("cannot execute the query: \n" + e);
        }
        finally
        {
            try
            {
                if (rs!=null && !rs.isClosed())
                    rs.close();
            }
            catch(Exception e)
            {
                System.out.println("cannot close resultset");
            }
        }
        return output;
    }

    public static void getDegreesOfSeparation(String login1, String login2, Statement stmt)
    {
        String sql = "SELECT f1.login login1, f2.login login2\n" +
                "FROM Favorites f1\n" +
                "LEFT JOIN Favorites f2 ON f1.vin = f2.vin\n" +
                "  AND f1.login != f2.login";
        ResultSet rs = null;
        System.out.println("executing "+ sql);
        boolean oneDeg = false;
        boolean twoDeg = false;
        try{
            rs = stmt.executeQuery(sql);
            while (rs.next())
            {
                if(rs.getString("login1").equals(login1) && rs.getString("login2").equals(login2)) {
                    System.out.println("One degree of separation");
                    return;
                }
            }
            rs.close();
        }
        catch(SQLException e)
        {
            System.out.println("cannot execute the query: \n" + e);
        }
        finally
        {
            try
            {
                if (rs!=null && !rs.isClosed())
                    rs.close();
            }
            catch(Exception e)
            {
                System.out.println("cannot close resultset");
            }
        }
    }
}
