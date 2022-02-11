package com.project.mini;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Wrapper;
import java.util.HashMap;
import java.util.Scanner;

class Item {
    public int id;
    public String name;
    public int cost;

    Item(int id, String name, int cost){
        this.id=id;
        this.name=name;
        this.cost=cost;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name=" + name +
                ", cost=" + cost +
                '}';
    }
}

public class User implements Runnable{
    private static final HashMap<Integer,Integer> my_orders= new HashMap<>();
    public static HashMap<Integer, Item> items = new HashMap<>();
    private static Scanner sc = new Scanner(System.in);
    private static int user_id;
    private static String user_type;
    User(int user_id, String user_type){
        User.user_id =user_id;
        User.user_type =user_type;
    }

    public static void userApplication(int user_id) throws Exception {
        System.out.println("Login successful as user.");
        long option = -1;
        while (true) {
            System.out.println("Main menu\n1.Display food menu\n2.Bill\n3.Logout");
            System.out.print("Enter option: ");
            try {
                option = Main.getOption();
            }
            catch (NegativeEntryException e){
                System.out.println("Invalid option, negative value please try again.");
                continue;
            }

            if(option==1){
                DataDAO.getMenu();
                System.out.println("Order food\n0.Main menu\n1.Yes\n2.No");
                System.out.print("Enter option: ");
                try {
                    option = Main.getOption();
                }
                catch (NegativeEntryException e){
                    System.out.println("Invalid option, negative value please try again.");
                    continue;
                }

                if(option!=1){
                    System.out.println("Redirecting to main menu.");
                    continue;
                }

                long item_id = -1;
                long count = -1;
                while (true) {
                    System.out.print("Enter item id: ");
                    item_id = Main.getOption();
                    System.out.print("Enter count: ");
                    count = Main.getOption();
                    if (my_orders.containsKey((int) item_id))
                        my_orders.put((int) item_id, my_orders.get((int) item_id) + (int)count);
                    else
                        my_orders.put((int) item_id, 1);
                    System.out.println("Continue ordering?\n1.Yes\n2.No");
                    System.out.print("Enter option: ");
                    option = Main.getOption();

                    if (option == 1)
                        continue;
                    break;
                }
            }
            else if(option==2){
                System.out.println("Final Bill");
                for (HashMap.Entry<Integer,Integer> entry : my_orders.entrySet()){
                    System.out.println("Item: "+items.get(entry.getKey())+" || Count: "+entry.getValue());
                }
                System.out.println("Place order?\n1.Yes\n2.No");
                System.out.print("Enter option: ");
                option=Main.getOption();

                if(option==1){
                    DataDAO.addBill(user_id, my_orders);
                    System.out.println("Order placed, thank you!");
                    break;
                }
                else{
                    System.out.println("Redirecting to main menu.");
                }
            }
            else if(option==3){
                System.out.println("Logging out, thank you!");
                return;
            }
            else{
                System.out.println("Invalid option, try again");
            }
        }
    }

    public static void adminApplication() throws Exception {
        long option = -1;

        while (true){
            System.out.println("Main menu\n1.Show today bills\n2.Month total sale\n3.Edit food menu\n4.Log out");
            System.out.print("Enter option: ");
            try {
                option = Main.getOption();
            }
            catch (NegativeEntryException e){
                System.out.println("Invalid option, negative value please try again.");
                continue;
            }

            if(option==1){
                System.out.println("\nToday's bill.");
                DataDAO.getTodayBills();
            }
            else if(option==2){
                System.out.println("\nMonthly sale.");
                DataDAO.getMonthlySale();
            }
            else if(option==3){
                while (true) {
                    System.out.println("Menu");
                    DataDAO.getMenu();
                    System.out.println("0.Main menu\n1.Create new item\n2.Display an item\n3.Update an item\n4.Delete an item");
                    System.out.print("Enter option: ");
                    option=Main.getOption();

                    if(option==0){
                        break;
                    }
                    else if(option==1){
                        System.out.print("Enter item name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter item cost: ");
                        long cost = Main.getOption();
                        DataDAO.createItem(new Item(-1, name, (int)cost));
                        System.out.println("Item created in menu.");
                    }
                    else if(option==2){
                        System.out.print("Enter item id: ");
                        long item_id = Main.getOption();
                        DataDAO.getItem((int)item_id);
                    }
                    else if(option==3){
                        System.out.print("Enter item id: ");
                        long item_id = Main.getOption();
                        System.out.print("Enter item name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter item cost: ");
                        long cost = Main.getOption();
                        DataDAO.updateItem(new Item((int)item_id, name, (int)cost));
                        System.out.println("Item updated in menu.");
                    }
                    else if(option==4){
                        System.out.print("Enter item id: ");
                        long item_id = Main.getOption();
                        DataDAO.deleteItem((int)item_id);
                        System.out.println("Item deleted from menu,");
                    }
                    else{
                        System.out.println("Invalid option, try again");
                    }
                }
            }
            else if(option==4){
                System.out.println("Logging out, thank you!");
                return;
            }
        }
    }

    @Override
    public void run() {
        try {
            DataDAO.initMenu();
            if(user_type.equals("user"))
                userApplication(user_id);
            else
                adminApplication();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
