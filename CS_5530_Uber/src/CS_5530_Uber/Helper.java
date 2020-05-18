package CS_5530_Uber;

import java.sql.*;


public class Helper {


    public void ClearAll( String tableName ,Statement stmt){

        ResultSet rs = null;
        String sql = "TRUNCATE TABLE '%"+tableName+"%'";
        System.out.println("executing "+sql);
        try {
            rs = stmt.executeQuery(sql);

            rs.close();
        }
        catch(Exception e){
            System.out.println("cannot execute the query");
        }

    };

    public String PrintAll(String tableName,Statement stmt)
    {
        ResultSet rs = null;
        String sql = "SELECT * FROM "+tableName;
        String output= "";
        System.out.println("executing "+sql);
        try {
            rs = stmt.executeQuery(sql);
                while(rs.next())
                    output+= rs.getString("login");
            rs.close();
        }
        catch(Exception e){
            System.out.println("cannot execute the query" + e);
        }
        return output;
    }


}
