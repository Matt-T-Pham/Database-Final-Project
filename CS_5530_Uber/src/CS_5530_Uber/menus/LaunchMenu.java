// **** CS 5530 Uber final Project ****
// **** Greg Rosich & Matthew Pham ****

package CS_5530_Uber.menus;

import CS_5530_Uber.Program;
import CS_5530_Uber.Connector;
import CS_5530_Uber.sql_classes.UberUser;

import java.io.BufferedReader;

public class LaunchMenu {

    public static void displayMenu()
    {
        System.out.println("\n------- Welcome to UUber System -------");
        System.out.println("1. Register a new Uber User");
        System.out.println("2. Login");
        System.out.println("3. Browse");
        System.out.println("0. Quit");
        System.out.println("for trouble shooting");
    }

    public static boolean getChoice(Connector con, BufferedReader in) throws Exception
    {
        int c = 0;
        try {
            c = Program.readNum(in);
        } catch (Exception e) {
            System.out.println("Invalid entry");
            return true;
        }

        if(c < 0 | c > 6) {
            System.out.println("Invalid entry");
            return true;
        }

        switch (c)
        {
            // quit
            case 0:
                return false;

            // register a new user
            case 1:
            {
                String login;
                String password;
                String name;
                String address;
                String phone;

                System.out.println("Enter a username:");
                while ((login = in.readLine()) == null && login.length() == 0);
                System.out.println("Enter a password:");
                while ((password = in.readLine()) == null && password.length() == 0);
                System.out.println("Enter your name:");
                while ((name = in.readLine()) == null && name.length() == 0);
                System.out.println("Enter your address:");
                while ((address = in.readLine()) == null && address.length() == 0);
                System.out.println("Enter your phone number (digits only):");
                while ((phone = in.readLine()) == null && phone.length() == 0);
                UberUser.InsertUser(login, password, name, address, phone, con.stmt);
            }
            break;

            // login
            case 2:
            {
                String login;
                String password;
                System.out.println("Enter your username:");
                while ((login = in.readLine()) == null && login.length() == 0);
                System.out.println("Enter your password:");
                while ((password = in.readLine()) == null && password.length() == 0);

                if(UberUser.Login(login, password, con.stmt))
                {
                    LoggedinMenu menu = new LoggedinMenu(login, con, in);
                    menu.runMenu();
                }
                else
                {
                    System.out.println("Invalid login");
                }
            }
            break;

            // browse
            case 3:
            {
                BrowseMenu menu = new BrowseMenu(con, in);
                menu.runMenu();
            }
            break;
        }

        return true;
    }
}
