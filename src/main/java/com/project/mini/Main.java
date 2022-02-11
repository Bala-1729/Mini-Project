package com.project.mini;

import java.util.*;

class NegativeEntryException extends Exception{
    public NegativeEntryException(String msg){
        super(msg);
    }
}

public class Main {
    private static Scanner sc = new Scanner(System.in);
    public static long getOption() throws Exception{
        long option = sc.nextLong();
        if(option<0){
            throw new NegativeEntryException("Please enter a Positive Integer value.\n");
        }
        return option;
    }

    public static void main(String[] args) throws Exception {
        long option;
        String username;
        String password;
        while (true){
            System.out.print("Main menu\n1.Register\n2.User login\n3.Admin login\n4.Exit\nEnter any option: ");

            try {
                option = getOption();
                sc.nextLine();
            }
            catch (NegativeEntryException e){
                System.out.println("Invalid option, negative value please try again.");
                continue;
            }

            if(option==1){
                System.out.print("Enter username: ");
                username=sc.nextLine();
                System.out.print("Enter password: ");
                password=sc.nextLine();
                int status = DataDAO.register(username, password);
                if(status==-1){
                    System.out.println("Exception registering user, try again.");
                    continue;
                }
                else {
                    System.out.println("Registration success. Please login.");
                }

            }
            else if(option==2){
                System.out.print("Enter username: ");
                username=sc.nextLine();
                System.out.print("Enter password: ");
                password=sc.nextLine();
                int user_id=DataDAO.login(username, password);
                if(user_id==-1){
                    System.out.println("Exception logging in user, try again.");
                    continue;
                }
                User user = new User(user_id, "user");

                Thread t1 = new Thread(user);
                t1.start();
                t1.join();
            }
            else if(option==3){
                System.out.print("Enter username: ");
                username=sc.nextLine();
                System.out.print("Enter password: ");
                password=sc.nextLine();

                if(username.equals("admin") && password.equals("admin")){
                    System.out.println("Log in successful as admin");
                }
                else{
                    System.out.println("Exception logging in user, try again.");
                    continue;
                }

                User user = new User(-1, "admin");

                Thread t1 = new Thread(user);
                t1.start();
                t1.join();
            }
            else if(option==4){
                System.out.println("Exiting application, thank you.");
                System.exit(0);
            }
            else{
                System.out.println("Invalid option please select properly.");
            }
        }
    }
}
