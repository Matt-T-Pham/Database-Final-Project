// **** CS 5530 Uber final Project ****
// **** Greg Rosich & Matthew Pham ****

package cs5530;


import java.sql.*;
import java.util.HashMap;

public class UberCar {

    public static void newUberCar(int vin, String category, String make, String model, String login, Statement stmt)
    {
        boolean didSucc = true;

        String sql = "START TRANSACTION;\n" +
                //"INSERT INTO Ctypes SET make='"+make+"',model='"+model+"';"; //+
                //"ON DUPLICATE KEY UPDATE tid=LAST_INSERT_ID(tid);\n" +
                //"INSERT INTO UC (tid,vin, category, login) VALUES(LAST_INSERT_ID(),'"+vin+
               // "','"+category+"','"+login+"');\n";
                //"COMMIT;";

       // String sql = "INSERT INTO Ctypes VALUES('"+vin+"','"+make+"','"+model+"');";

        //String sql = "INSERT INTO Ctypes (tid,make,model) VALUES('"+vin+"','"+make+"','"+model+"';";
        try
        {
            stmt.executeUpdate(sql);
        }
        catch(SQLException e)
        {
            didSucc = false;
            if(e.getErrorCode() == MYSQLErrorCodes.DUPLICATE) {
                System.out.println("There is already a car with that vin");
            } else {
                System.out.println("cannot execute the query: \n" + e + " code: " +e.getErrorCode());
            }
        }
        if(didSucc){
            System.out.println("Car '" + vin + "' added");
        }
    }

    public static String checkIfCarExist(int vin,Statement stmt){
        String sqlCheckExist = "Select tid FROM UC where vin = '"+vin+"'";
        String output ="";
        ResultSet rs = null;

        try{
            rs = stmt.executeQuery(sqlCheckExist);
            while(rs.next()){
                output += rs.getString("tid");
            }
            rs.close();
            if (output.length() != 0)
            {
                return output;
            }
            else{
                return null;
            }
        }catch(SQLException e){
            System.out.println("cannot execute the query to get valid VIN \n" + e);
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
        return null;

    }
    public static void UpdateCar(int tid, int vin, String category, String make, String model, String login, Statement stmt){

        int rcCar = 0;
        int rcCtype = 0;

        String sqlUpdateCarType = "Update Ctypes Set make = '"+make+"', model = '"+model+"' Where tid = "+tid+"";

        String UpdateCar = "UPDATE " +
                "UC " +
                "SET " +
                "category = '"+category+"'"+
                " WHERE " +
                "  vin = '"+vin+"'";
        try
        {
            rcCtype = stmt.executeUpdate(sqlUpdateCarType);

        }
        catch(SQLException e)
        {
            System.out.print(e);
            System.out.print(sqlUpdateCarType);
        }

        try
        {
            rcCar = stmt.executeUpdate(UpdateCar);
        }
        catch(SQLException e)
        {
            System.out.print(e);
        }
        if(rcCar == 1 && rcCtype == 1) {
            System.out.println("Car Updated");
        }
    }
    public static void favoriteCar(String login, int vin, Statement stmt) {
        String sql = "INSERT INTO Favorites (login, vin) VALUES('"+login+"','"+vin+"')";
//        System.out.println("executing "+sql);

        int rc = 0;

        try{
            rc = stmt.executeUpdate(sql);
        }
        catch(SQLException e)
        {
            if(e.getErrorCode() == MYSQLErrorCodes.DUPLICATE) {
                System.out.println("You have already favorited this car");
            } else {
                System.out.println("cannot execute the query: \n" + e + " code: " +e.getErrorCode());
            }
        }

        if(rc == 1) {
            System.out.println("Car '" + vin + "' favorited");
        } else {
            System.out.println("unexpected result count: " + rc);
        }
    }

    public static void reserveRide(String login, int vin, int pid, Statement stmt) {
        String sql = "INSERT INTO Reserve (login, vin, pid) VALUES('"+login+"','"+vin+"','"+
                pid+"')";
//        System.out.println("executing "+sql);

        int rc = 0;

        try{
            rc = stmt.executeUpdate(sql);
        }
        catch(SQLException e)
        {
            if(e.getErrorCode() == MYSQLErrorCodes.DUPLICATE) {
                System.out.println("You have already reserved this ride");
                getCarSuggestions(vin, stmt);
            } else
            if(e.getErrorCode() == MYSQLErrorCodes.FOREIGN_KEY){
                System.out.println("vin is not a car");
            } else {
                System.out.println("cannot execute the query: \n" + e + " code: " +e.getErrorCode());
            }
        }

        if(rc == 1) {
            System.out.println("Ride reserved");
            getCarSuggestions(vin, stmt);
        } else if(rc > 1){
            System.out.println("unexpected result count: " + rc);
        }
    }

    public static void recordRide(String login, int vin, int cost, Timestamp rdate, Statement stmt) {
        String sql = "INSERT INTO Ride (cost, rdate, login, vin) VALUES('"+cost+"','"+rdate+"','"+
                login+"','"+vin+"')";
//        System.out.println("executing "+sql);

        int rc = 0;

        try{
            rc = stmt.executeUpdate(sql);
        }
        catch(SQLException e)
        {
            if(e.getErrorCode() == MYSQLErrorCodes.USER) {
                System.out.println("ERROR: driver is not available at specified time");
            } else {
                System.out.println("cannot execute the query: \n" + e + " code: " +e.getErrorCode());
            }
        }

        if(rc == 1) {
            System.out.println("Ride recorded");
        } else if(rc > 1){
            System.out.println("unexpected result count: " + rc);
        }
    }

    public static void getCarSuggestions(int vin, Statement stmt){
        String sql = "SELECT DISTINCT vin, COUNT(*) count\n" +
                "FROM Ride r,\n" +
                "  (SELECT DISTINCT login\n" +
                "  FROM Ride\n" +
                "  WHERE vin = "+vin+") l\n" +
                "WHERE r.login = l.login\n" +
                "  AND r.vin <> "+vin+"\n" +
                "GROUP BY r.vin\n" +
                "ORDER BY count DESC";
//        System.out.println("executing "+ sql);
        String output="Suggested rides:\n";
        ResultSet rs=null;
        try{
            rs=stmt.executeQuery(sql);
            while (rs.next())
            {
                output+="   vin: '"+rs.getInt("vin")+"', popularity: "+rs.getInt("count")+"\n";
            }
            System.out.println(output);
            rs.close();
        }
        catch(Exception e)
        {
            System.out.println("cannot execute the query "+e);
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
    }

    public static void rateCar(String login, int vin, int rating, String fbtext, Timestamp fbdate, Statement stmt) {
        String sql = "INSERT INTO Feedback (login, vin, rating, fbtext, fbdate) VALUES('"+login+"','"+vin+"','"+rating+"','"+fbtext+"','"+fbdate+"')";
//        System.out.println("executing "+sql);

        int rc = 0;

        try{
            rc = stmt.executeUpdate(sql);
        }
        catch(SQLException e)
        {
            if(e.getErrorCode() == MYSQLErrorCodes.FOREIGN_KEY){
                System.out.println("vin is not a car");
            } else if(e.getErrorCode() == MYSQLErrorCodes.USER) {
                System.out.println("You already rated this car");
            } else if(e.getErrorCode() == MYSQLErrorCodes.DUPLICATE) {
                System.out.println("You have already rated car '" +vin+ "'!");
            } else {
                System.out.println("cannot execute the query: \n" + e);
            }
        }

        if(rc == 1) {
            System.out.println("Car '" + vin + "' rated");
        } else if(rc > 1){
            System.out.println("unexpected result count: " + rc);
        }
    }

    public static String getMostPopularCars(int count, Statement stmt) {

        String sql = "SELECT uc.vin, uc.category, r.count\n" +
                "FROM UC uc,\n" +
                "  (SELECT vin, COUNT(vin) count \n" +
                "  FROM Ride \n" +
                "  GROUP BY vin) r\n" +
                "WHERE uc.vin = r.vin\n" +
                "GROUP BY category, vin, count\n" +
                "ORDER BY category, count DESC\n" +
                "LIMIT " + count;

        System.out.println("executing "+ sql);
        ResultSet rs = null;
        String currentCat = "";

        String output = "\nMost Popular Cars:\n";
        try{
            rs = stmt.executeQuery(sql);

            while (rs.next())
            {
                String newCat = rs.getString("category");
                if(!currentCat.equals(newCat)) {
                    currentCat = newCat;
                    output += " Category: "+ currentCat + "\n";
                }
                output += "<BR>   VIN: "+rs.getString("vin")+" - Number of Rides: "+rs.getString("count")+"<BR>\n";
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

    public static String getMostExpensiveCars(int count, Statement stmt) {

        String sql = "SELECT r.vin, AVG(r.cost),r.cost FROM Ride r GROUP BY r.vin,r.cost ORDER BY 2 DESC LIMIT "+count+"";

        System.out.println("executing "+ sql);
        ResultSet rs = null;

        String output = "\n Most Expensive Cars:\n";
        try{
            rs = stmt.executeQuery(sql);

            while (rs.next())
            {
                output += " <BR>  VIN: "+rs.getString("vin")+" - Cost: "+rs.getString("cost")+"<BR>\n";
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

    public static String FindCars(String keyword, Statement stmt) {

        String sql = "SELECT * FROM Ctypes as c, UC as u WHERE model LIKE '%"+keyword+"%' or make LIKE '%"+keyword+"%' AND u.tid = c.tid ";

        String output="";
        ResultSet rs=null;
        try{
            rs=stmt.executeQuery(sql);
            while (rs.next())
            {
                output+="Make: "+rs.getString("make")+"   Model: "+rs.getString("model")+"   Category: "+rs.getString("category")+"   Driver Login: "+rs.getString("login")+"   VIN: "+rs.getString("vin")+"<BR>\n";
            }

            rs.close();
        }
        catch(Exception e)
        {
            System.out.println("cannot execute the query "+e);
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

        return  output;
    }


    public static HashMap<Integer,String> getPeriodsAvailable(int vin, Statement stmt) {
        HashMap<Integer,String> periods = new HashMap<Integer, String>();
        String sql="SELECT p.pid, p.fromHour, p.toHour\n" +
                "FROM Available a, Period p, UC uc\n" +
                "WHERE a.pid = p.pid AND uc.vin = " + vin+ " AND uc.login = a.login";
        String output="";
        ResultSet rs=null;
//        System.out.println("executing "+sql);
        try{
            rs = stmt.executeQuery(sql);
            while (rs.next())
            {
                periods.put(rs.getInt("pid"), rs.getInt("fromHour") + ":00 - " + rs.getInt("toHour") + ":00");
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
        return periods;
    }
}
