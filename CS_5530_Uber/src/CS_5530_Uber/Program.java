// **** CS 5530 Uber final Project ****
// **** Greg Rosich & Matthew Pham ****

package CS_5530_Uber;

import CS_5530_Uber.menus.LaunchMenu;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Program {

    public static void main(String[] args)
    {
        Connector con = null;

        try {
            con = new Connector();
            System.out.println ("Database connection established");

            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            boolean isRunning = true;
            while (isRunning)
            {
                LaunchMenu.displayMenu();
                isRunning = LaunchMenu.getChoice(con, in);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println ("Either connection error or query execution error!");
        }
        finally
        {
            if (con != null)
            {
                try
                {
                    con.closeConnection();
                    System.out.println ("Database connection terminated");
                }

                catch (Exception e) { /* ignore close errors */ }
            }
        }
    }



    public static int readNum(BufferedReader in) throws Exception {
        String choice;
        while ((choice = in.readLine()) == null && choice.length() == 0);
        return Integer.parseInt(choice);
    }
}
