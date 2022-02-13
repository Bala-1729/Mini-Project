package com.project.magicofbooks;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Wrapper;
import java.util.HashMap;
import java.util.Scanner;

public class User implements Runnable{
    private static int user_id;
    private static String user_type;
    private static Scanner sc = new Scanner(System.in);

    User(int user_id, String user_type){
        User.user_id =user_id;
        User.user_type =user_type;
    }

    private static int bookOp(String type) throws Exception {
        long option = -1;
        System.out.println("0.Main menu\n1.Add book\n2.Delete book\n3.Display books");
        System.out.print("Enter option: ");
        option = Main.getOption();

        if(option==0){
            System.out.println("Redirecting to main menu.");
            return 0;
        }
        int temp = (int)option;
        try {
            if(option==1 || option == 2){
                System.out.print("Enter Book ID: ");
                option=Main.getOption();
                DataDAO.bookOperations(user_id, (int)option, temp, type);
            }
            else if(option==3){
                System.out.println("Available books in your log.");
                DataDAO.bookOperations(user_id, (int)option, temp, type);
            }
            else{
                System.out.println("Invalid option try again.");
            }
        }
        catch (Exception e){
            System.out.println("Invalid Book ID, try again.");
        }
        return 1;
    }

    public static void userApplication(int user_id) throws Exception {
        System.out.println("Login successful as user.");
        long option = -1;
        while (true) {
            System.out.println("Main menu");
            System.out.println("1. Display books.");
            System.out.println("2. New Books.");
            System.out.println("3. Favourite Books.");
            System.out.println("4. Completed Books.");
            System.out.println("5. Logout");
            System.out.print("Enter option: ");
            try {
                option = Main.getOption();
            } catch (NegativeEntryException e) {
                System.out.println("Invalid option, negative value please try again.");
                continue;
            }

            if (option == 1) {
                while (true) {
                    System.out.println("Available books.");
                    DataDAO.displayBooks();
                    System.out.println("Enter Book ID to display details.\n0.Main menu");
                    System.out.print("Enter option: ");
                    option = Main.getOption();

                    if(option==0){
                        System.out.println("Redirecting to main menu.");
                        break;
                    }

                    try {
                        DataDAO.getBookDetails((int) option);
                    }
                    catch (Exception e){
                        System.out.println("Invalid Book ID, try again.");
                    }
                }
            }
            else if(option == 2){
                while (true){
                    int status =  bookOp("new");
                    if(status==0){
                        break;
                    }
                }
            }
            else if(option == 3){
                while (true){
                   int status =  bookOp("favourite");
                   if(status==0){
                       break;
                   }
                }
            }
            else if(option == 4){
                while (true){
                    int status =  bookOp("completed");
                    if(status==0){
                        break;
                    }
                }
            }
            else if(option == 5){
                System.out.println("Logging out, thank you!");
                return;
            }
            else{
                System.out.println("Invalid option, try again.");
            }
        }
    }

    public static void adminApplication() throws Exception {
        System.out.println("Login successful as admin.");
        long option = -1;
        while (true) {
            System.out.println("Main menu");
            System.out.println("1. Add a book");
            System.out.println("2. Delete a book");
            System.out.println("3. Update a book");
            System.out.println("4. Display books");
            System.out.println("5. Display books in Autobiography genre.");
            System.out.println("6. Arrange books.");
            System.out.println("7. Logout");
            System.out.print("Enter option: ");

            try {
                option = Main.getOption();
            } catch (NegativeEntryException e) {
                System.out.println("Invalid option, negative value please try again.");
                continue;
            }

            if(option==1){
                System.out.println("Enter book details.");
                System.out.print("Book name : ");
                String book_name = sc.nextLine();
                System.out.print("Author name: ");
                String author_name = sc.nextLine();
                System.out.print("Description: ");
                String desc = sc.nextLine();
                System.out.print("Genre: ");
                String genre = sc.nextLine();
                System.out.print("Price: ");
                int price = sc.nextInt();
                try {
                    DataDAO.addUpdateBook(-1, book_name, author_name, desc, genre, price);
                    System.out.println("Book added successfully");
                }
                catch (Exception e){
                    System.out.println(e);
                }
            }
            else if(option==3){
                System.out.println("Enter book id and details to update");
                System.out.print("Enter book id: ");
                int book_id = sc.nextInt();
                sc.nextLine();
                System.out.print("Book name : ");
                String book_name = sc.nextLine();
                System.out.print("Author name: ");
                String author_name = sc.nextLine();
                System.out.print("Description: ");
                String desc = sc.nextLine();
                System.out.print("Genre: ");
                String genre = sc.nextLine();
                System.out.print("Price: ");
                int price = sc.nextInt();
                try {
                    DataDAO.addUpdateBook(book_id,book_name, author_name, desc, genre, price);
                    System.out.println("Book updated successfully");
                }
                catch (Exception e){
                    System.out.println(e);
                }
            }
            else if(option==2){
                System.out.print("Enter book id to delete: ");
                option = Main.getOption();
                try {
                    DataDAO.deleteBook((int)option);
                    System.out.println("Delete book successful");
                }
                catch (Exception e){
                    System.out.println(e);
                }
            }
            else if(option==4){
                System.out.println("Available books.");
                DataDAO.displayBooks();
            }
            else if (option==5){
                System.out.println("Available books in auto-biography");
                DataDAO.displayGenreBooks();
            }
            else if(option==6){
                System.out.println("Order by price:\n1.Low to high\n2.High to low\n3.Best selling");
                System.out.print("Enter option: ");
                option=Main.getOption();
                DataDAO.displayBooksOrder((int)option);
            }
            else if(option==7){
                return;
            }
            else{
                System.out.println("Invalid option try again.");
            }
        }
    }

    @Override
    public void run() {
        try {
            if(user_type.equals("user"))
                userApplication(user_id);
            else
                adminApplication();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
