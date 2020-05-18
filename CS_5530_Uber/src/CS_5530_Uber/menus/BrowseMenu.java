// **** CS 5530 Uber final Project ****
// **** Greg Rosich & Matthew Pham ****

package CS_5530_Uber.menus;

import CS_5530_Uber.Connector;
import CS_5530_Uber.Program;
import CS_5530_Uber.sql_classes.UberCar;
import CS_5530_Uber.sql_classes.UberUser;

import java.io.BufferedReader;

public class BrowseMenu {

    Connector con;
    BufferedReader in;

    public BrowseMenu(Connector con, BufferedReader in)
    {
        this.con = con;
        this.in = in;
    }

    public void runMenu() throws Exception
    {
        boolean isRunning = true;
        while (isRunning)
        {
            displayMenu();
            isRunning = getChoice();
        }
    }

    private void displayMenu()
    {
        System.out.println("\n---------- Browse Menu ----------");
        System.out.println("1. Find cars");
        System.out.println("2. Get feedback for driver");
        System.out.println("3. Two degrees of separation");
        System.out.println("4. Statistics");
        System.out.println("5. Awards");
        System.out.println("0. Back");
        System.out.println("please enter your choice:");
    }

    private boolean getChoice() throws Exception
    {
        int c = 0;
        try {
            c = Program.readNum(in);
        } catch (Exception e) {
            System.out.println("Invalid entry");
            return true;
        }

        if(c < 0 | c > 5) {
            System.out.println("Invalid entry");
            return true;
        }

        switch (c) {
            // back
            case 0:
                return false;

            // find cars
            case 1:
            {

                String keyword;

                String output;
                System.out.println("please enter the make or model of the car");

                while ((keyword = in.readLine()) == null && keyword.length() == 0);

                UberCar uc = new UberCar();

                output = uc.FindCars(keyword,con.stmt);
                System.out.println(output);
            }
            break;

            // get feedback for driver
            case 2:
            {
                String driverLogin;
                System.out.println("Enter the username of the driver you would like feedback for:");
                while ((driverLogin = in.readLine()) == null && driverLogin.length() == 0);

                int count = 0;
                System.out.println("Enter the number of feedbacks:");
                while (count < 1) {
                    try {
                        count = Program.readNum(in);
                    } catch (Exception e) {
                        System.out.println("Please enter a number greater than 0");
                        return true;
                    }
                }
                System.out.println(UberUser.getUsefulDriverFeedback(driverLogin, count, con.stmt));
            }
            break;

            // two degrees of separation
            case 3:
            {
                String login1;
                String login2;
                System.out.println("Enter the username of first user:");
                while ((login1 = in.readLine()) == null && login1.length() == 0);
                System.out.println("Enter the username of second user:");
                while ((login2 = in.readLine()) == null && login2.length() == 0);
                UberUser.getDegreesOfSeparation(login1, login2, con.stmt);
            }
            break;

            // statistics
            case 4:
            {
                runStatsMenu();
            }
            break;

            // awards
            case 5:
            {
                runAwardsMenu();
            }
            break;
        }

        return true;
    }

    private void runStatsMenu() throws Exception
    {
        boolean isRunning = true;
        while (isRunning)
        {
            displayStatsMenu();
            isRunning = getStatsMenuChoice();
        }
    }

    private void displayStatsMenu()
    {
        System.out.println("\n---------- Statistics Menu ----------");
        System.out.println("1. Most popular cars");
        System.out.println("2. Most expensive cars");
        System.out.println("3. Highest rated drivers");
        System.out.println("0. Back");
        System.out.println("please enter your choice:");
    }

    private boolean getStatsMenuChoice() throws Exception
    {
        int c = 0;
        try {
            c = Program.readNum(in);
        } catch (Exception e) {
            System.out.println("Invalid entry");
            return true;
        }

        if(c < 0 | c > 3) {
            System.out.println("Invalid entry");
            return true;
        }

        switch (c) {
            // back
            case 0:
                return false;

            // most popular cars
            case 1:
            {
                int count = 0;
                System.out.println("Enter the number of cars:");
                while (count < 1) {
                    try {
                        count = Program.readNum(in);
                    } catch (Exception e) {
                        System.out.println("Please enter a number greater than 0");
                        return true;
                    }
                }
                System.out.println(UberCar.getMostPopularCars(count, con.stmt));
            }
            break;

            // most expensive cars
            case 2:
            {
                int count = 0;
                System.out.println("Enter the number of cars:");
                while (count < 1) {
                    try {
                        count = Program.readNum(in);
                    } catch (Exception e) {
                        System.out.println("Please enter a number greater than 0");
                        return true;
                    }
                }

                System.out.println(UberCar.getMostExpensiveCars(count, con.stmt));
            }
            break;

            // highest rated drivers
            case 3:
            {
                int count = 0;
                System.out.println("Enter the number of drivers:");
                while (count < 1) {
                    try {
                        count = Program.readNum(in);
                    } catch (Exception e) {
                        System.out.println("Please enter a number greater than 0");
                        return true;
                    }
                }
                System.out.println(UberUser.getHighestRatedDrivers(count, con.stmt));
            }
            break;
        }

        return true;
    }



    private void runAwardsMenu() throws Exception
    {
        boolean isRunning = true;
        while (isRunning)
        {
            displayAwardsMenu();
            isRunning = getAwardsMenuChoice();
        }
    }

    private void displayAwardsMenu()
    {
        System.out.println("\n---------- Awards Menu ----------");
        System.out.println("1. Top trusted users");
        System.out.println("2. Top useful users");
        System.out.println("0. Back");
        System.out.println("please enter your choice:");
    }

    private boolean getAwardsMenuChoice() throws Exception
    {
        int c = 0;
        try {
            c = Program.readNum(in);
        } catch (Exception e) {
            System.out.println("Invalid entry");
            return true;
        }

        if(c < 0 | c > 2) {
            System.out.println("Invalid entry");
            return true;
        }

        switch (c) {
            // back
            case 0:
                return false;

            // top trusted users
            case 1:
            {
                int count = 0;
                System.out.println("Enter the number of users:");
                while (count < 1) {
                    try {
                        count = Program.readNum(in);
                    } catch (Exception e) {
                        System.out.println("Please enter a number greater than 0");
                        return true;
                    }
                }
                System.out.println(UberUser.getTopTrustedUsers(count, con.stmt));
            }
            break;

            // top useful users
            case 2:
            {
                int count = 0;
                System.out.println("Enter the number of users:");
                while (count < 1) {
                    try {
                        count = Program.readNum(in);
                    } catch (Exception e) {
                        System.out.println("Please enter a number greater than 0");
                        return true;
                    }
                }
                System.out.println(UberUser.getTopUsefulUsers(count, con.stmt));
            }
            break;
        }

        return true;
    }
}
