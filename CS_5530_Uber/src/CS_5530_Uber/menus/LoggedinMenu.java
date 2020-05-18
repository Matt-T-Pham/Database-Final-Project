// **** CS 5530 Uber final Project ****
// **** Greg Rosich & Matthew Pham ****

package CS_5530_Uber.menus;

import CS_5530_Uber.Connector;
import CS_5530_Uber.Program;
import CS_5530_Uber.sql_classes.UberCar;
import CS_5530_Uber.sql_classes.UberUser;

import java.io.BufferedReader;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

public class LoggedinMenu {

    String login;
    Connector con;
    BufferedReader in;

    public LoggedinMenu(String login, Connector con, BufferedReader in)
    {
        this.login = login;
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
        System.out.println("\n------- Welcome " + login + " -------");
        System.out.println("1. Ride");
        System.out.println("2. Drive");
        System.out.println("3. Browse");
        // TODO: rate menu
        System.out.println("0. Logout");
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

        if(c < 0 | c > 3) {
            System.out.println("Invalid entry");
            return true;
        }

        switch (c)
        {
            // logout
            case 0:
                return false;

            // rides menu
            case 1:
            {
                runRideMenu();
            }
            break;

            // drive
            case 2:
            {
                runDriveMenu();
            }
            break;

            // browse
            case 3:
            {
                BrowseMenu menu = new BrowseMenu(con, in);
                menu.runMenu();
            }
        }

        return true;
    }

    private void runRideMenu() throws Exception
    {
        boolean isRunning = true;
        while (isRunning)
        {
            displayRideMenu();
            isRunning = getRideMenuChoice();
        }
    }

    private void displayRideMenu()
    {
        System.out.println("\n---------- Ride Menu ----------");
        System.out.println("1. Reserve ride");
        System.out.println("2. Record ride");
        System.out.println("3. Favorite a car");
        System.out.println("4. Rate a car and give feedback");
        System.out.println("5. Rate other users feedback");
        System.out.println("6. Rate other users");
        System.out.println("0. Back");
        System.out.println("please enter your choice:");
    }

    public boolean getRideMenuChoice() throws Exception
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

            // reserve a ride
            case 1:
            {
                int vin = 0;
                int pid = 0;
                while (true) {
                    while (vin < 1) {
                        System.out.println("Enter the vin of the car you would like to reserve:");
                        try {
                            vin = Program.readNum(in);
                        } catch (Exception e) {
                            System.out.println("Invalid vin");
                            return true;
                        }
                    }
                    HashMap<Integer, String> periods = UberCar.getPeriodsAvailable(vin, con.stmt);
                    if(periods.isEmpty()){
                        System.out.println("car "+vin+"'s driver has no availablility");
                        return true;
                    }
                    while (!periods.containsKey(pid)) {
                        System.out.println("Select the id of the period you would like to reserve the ride for:");
                        for (HashMap.Entry<Integer, String> entry : periods.entrySet()) {
                            System.out.println(entry.getKey() + ". " + entry.getValue());
                        }
                        try {
                            pid = Program.readNum(in);
                        } catch (Exception e) {
                            System.out.println("Invalid period id");
                            return true;
                        }
                    }

                    String entry = "";

                    System.out.println();
                    System.out.println("The Vin of the car you are reserving is: " + vin + "\n" + "From: " + periods.get(pid) + "\n");
                    System.out.println("Is the information correct? y/n");

                    while ((entry = in.readLine()) == null && entry.length() == 0) ;
                    if (entry.toLowerCase().equals("y")) {
                        UberCar.reserveRide(login, vin, pid, con.stmt);
                        break;
                    }
                    if (entry.toLowerCase().equals("n")) {
                        vin = 0;
                        pid = 0;
                        continue;
                    }
                    System.out.println("invalid entry");
                }
            }
            break;

            // record a ride
            case 2:
            {

                int vin = 0;
                int cost = 0;
                String sdate = "";
                Timestamp rdate = null;
                while (true) {
                    while (vin < 1) {
                        System.out.println("Enter the vin of the car you rode in:");
                        try {
                            vin = Program.readNum(in);
                        } catch (Exception e) {
                            System.out.println("Invalid vin");
                            return true;
                        }
                    }
                    while (cost < 1) {
                        System.out.println("Enter the cost of the ride:");
                        try {
                            cost = Program.readNum(in);
                        } catch (Exception e) {
                            System.out.println("Invalid number");
                            return true;
                        }
                    }

                    String dateFormat = "yyyy-mm-dd hh:mm:ss";
                    while (rdate == null) {
                        System.out.println("Enter the date and time of the ride (" + dateFormat + "):");
                        while ((sdate = in.readLine()) == null && sdate.length() == 0) ;
                        try {
                            rdate = java.sql.Timestamp.valueOf(sdate);
                        } catch (Exception e) {
                            System.out.println("Please enter a date in the format: " + dateFormat);
                        }
                    }

                    String entry = "";

                    System.out.println();
                    System.out.println("The Vin of the car you rode in is: " + vin + "\n" + "Cost of the Ride: " + cost + "\n" + "The date: " + sdate + "\n");
                    System.out.println("Is the information correct? y/n");

                    while ((entry = in.readLine()) == null && entry.length() == 0) ;
                    if (entry.toLowerCase().equals("y")) {
                        UberCar.recordRide(login, vin, cost, rdate, con.stmt);
                        break;
                    }
                    if (entry.toLowerCase().equals("n")) {
                         vin = 0;
                         cost = 0;
                         sdate = "";
                         rdate = null;
                      continue;
                    }
                    System.out.println("invalid entry");

                }
            }
            break;

            // favorite a car
            case 3:
            {
                int vin = -1;
                while (vin < 1) {
                    System.out.println("Enter the vin of the car you would like to favorite:");
                    try {
                        vin = Program.readNum(in);
                    } catch (Exception e) {
                        System.out.println("Invalid vin");
                        return true;
                    }
                }
                UberCar.favoriteCar(login, vin, con.stmt);
            }
            break;

            // rate a car and give feedback
            case 4:
            {
                int vin = -1;
                int rating = -1;
                String fbtext;
                while (vin < 1) {
                    System.out.println("Enter the vin of the car you would like to rate:");
                    try {
                        vin = Program.readNum(in);
                    } catch (Exception e) {
                        System.out.println("Invalid vin");
                        return true;
                    }
                }
                System.out.println("Enter your rating (0-10):");
                while (rating < 0 || rating > 10) {
                    try {
                        rating = Program.readNum(in);
                    } catch (Exception e) {
                        System.out.println("Please enter a number from 0 to 10");
                        return true;
                    }
                    if(rating < 0 || rating > 10) {
                        System.out.println("Please enter a number from 0 to 10");
                    }
                }
                System.out.println("Enter some feedback for the car (optional):");
                while ((fbtext = in.readLine()) == null && fbtext.length() == 0);
                Timestamp fbdate = new Timestamp(new Date().getTime());
                UberCar.rateCar(login, vin, rating, fbtext, fbdate, con.stmt);
            }
            break;

            // rate other users feedback
            case 5:
            {
                int fid = -1;
                int rating = -1;
                System.out.println("Enter the id of the feedback you would like to rate:");
                while (fid < 1) {
                    try {
                        fid = Program.readNum(in);
                    } catch (Exception e) {
                        System.out.println("Please enter a number from 0 to 10");
                        return true;
                    }
                }
                while (rating < 0 || rating > 2) {
                    System.out.println("Select a rating:");
                    System.out.println("0. useless");
                    System.out.println("1. useful");
                    System.out.println("2. very useful");
                    try {
                        rating = Program.readNum(in);
                    } catch (Exception e) {
                        System.out.println("Invalid selection");
                        return true;
                    }
                }
                UberUser.rateFeedback(login, fid, rating, con.stmt);
            }
            break;

            // rate other users
            case 6:
            {
                String trustee;
                System.out.println("Enter the login of the user you would like to rate:");
                while ((trustee = in.readLine()) == null && trustee.length() == 0);
                String entry = "";
                while (true) {
                    System.out.println("Do you trust this user? (y/n)");
                    while ((entry = in.readLine()) == null && entry.length() == 0);
                    if(entry.toLowerCase().equals("y")) {
                        UberUser.rateUser(login, trustee, true, con.stmt);
                        break;
                    }
                    if (entry.toLowerCase().equals("n")) {
                        UberUser.rateUser(login, trustee, false, con.stmt);
                        break;
                    }
                    System.out.println("invalid entry");
                }
            }
            break;

        }

        return true;
    }

    private void runDriveMenu() throws Exception
    {
        boolean isRunning = true;
        while (isRunning)
        {
            displayDriveMenu();
            isRunning = getDriveMenuChoice();
        }
    }

    private void displayDriveMenu()
    {
        System.out.println("\n---------- Driver Menu ----------");
        System.out.println("1. New Car");
        System.out.println("2. Update Car");
        System.out.println("3. Become a driver");
        System.out.println("0. Back");
        System.out.println("please enter your choice:");
    }

    private boolean getDriveMenuChoice() throws Exception
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

        switch (c)
        {
            // quit
            case 0:
                return false;

            // new car
            case 1:
            {
                int vin = 0;
                String make;
                String model;
                String category; // TODO: category selector and move category's to own table

                System.out.println("Enter your car's vin number:");
                while (vin < 1) {
                    try {
                        vin = Program.readNum(in);
                    } catch (Exception e) {
                        System.out.println("Please enter a number greater than 0");
                        return true;
                    }
                }
                System.out.println("Enter your car's make:");
                while ((make = in.readLine()) == null && make.length() == 0);
                System.out.println("Enter your car's model:");
                while ((model = in.readLine()) == null && model.length() == 0);
                System.out.println("Enter your car's category:");
                while ((category = in.readLine()) == null && category.length() == 0);
                UberCar.newUberCar(vin, category, make, model, login, con.stmt);UberCar.newUberCar(vin, category, make, model, login, con.stmt);
            }
            break;

            // update a car
            case 2:
            {
                int vin = 0;
                String make;
                String model;
                String category; // TODO: category selector and move category's to own table
                UberCar ub = new UberCar();
                System.out.println("Enter your car's vin number that you want to update:");
                while (vin < 1) {
                    try {
                        vin = Program.readNum(in);
                        String stid = ub.checkIfCarExist(vin,con.stmt);

                        if (stid.length() != 0){

                            int tid = Integer.parseInt(stid);

                            System.out.println("Enter your car's make:");
                            while ((make = in.readLine()) == null && make.length() == 0);
                            System.out.println("Enter your car's model:");
                            while ((model = in.readLine()) == null && model.length() == 0);
                            System.out.println("Enter your car's category:");
                            while ((category = in.readLine()) == null && category.length() == 0);
                            ub.UpdateCar(tid,vin, category, make, model, login, con.stmt);
                        }else {
                            System.out.println("No recorded Vin");
                        }
                    } catch (Exception e) {
                        System.out.println("No VIN number found");
                        return true;
                    }
                }

            }
            break;

            // become a driver
            case 3:
            {
                String entry = "";
                while (true) {
                    System.out.println("Are you sure you would like to become a driver? (y/n)");
                    while ((entry = in.readLine()) == null && entry.length() == 0);
                    if(entry.toLowerCase().equals("y")) {
                        UberUser.becomeUberDriver(login, con.stmt);
                        return true;
                    }
                    if (entry.toLowerCase().equals("n")) {
                        return true;
                    }
                    System.out.println("invalid entry");
                }
            }

        }
        return true;
    }
}
