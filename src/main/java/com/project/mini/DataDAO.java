package com.project.mini;
import java.sql.*;
import java.util.HashMap;
import java.time.LocalDate;

class UserTypeMismatchException extends Exception{
    public UserTypeMismatchException(String msg){
        super(msg);
    }
}

public class DataDAO {
    public static int register(String user_name, String password) {
        int status=-1;
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "bala-11786", "password");
            PreparedStatement stmt = con.prepareStatement("insert into person (user_name, password) values (?,?)");
            stmt.setString(1, user_name);
            stmt.setString(2, password);

            status = stmt.executeUpdate();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return status;
    }

    public static int login(String user_name, String password) {
        int user_id=-1;

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "bala-11786", "password");
            PreparedStatement stmt = con.prepareStatement("select password,user_id from person where user_name=?");
            stmt.setString(1, user_name);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                if(rs.getString(1).equals(password)){
                    user_id=rs.getInt(2);
                }
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return user_id;
    }

    public static void getMenu(){
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "bala-11786", "password");
            PreparedStatement stmt = con.prepareStatement("select item_id, item_name, item_cost from menu");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                System.out.println("Item id: "+rs.getInt(1)+" || Item name: "+rs.getString(2)+" || Item cost: Rs."+rs.getInt(3));
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void addBill(int user_id, HashMap<Integer, Integer> orders){
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "bala-11786", "password");
            PreparedStatement stmt = con.prepareStatement("insert into billing (item_id, item_count) values (?,?)");
            for (HashMap.Entry<Integer,Integer> entry : orders.entrySet()){
                stmt.setInt(1, entry.getKey());
                stmt.setInt(2, entry.getValue());
                stmt.executeUpdate();
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void getTodayBills(){
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "bala-11786", "password");
            PreparedStatement stmt = con.prepareStatement("select item_id, item_count from billing;");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                System.out.println("Item Id: "+rs.getInt(1)+" || Item name: "+User.items.get(rs.getInt(1))+" || Item count: "+rs.getInt(2));
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void getMonthlySale(){
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "bala-11786", "password");
            LocalDate myObj = LocalDate.now();
            PreparedStatement stmt = con.prepareStatement("select bill_id, item_id, item_count from billing where created_date like '"+Date.valueOf(myObj).toString()+" %'");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                System.out.println("Bill id: "+rs.getInt(1)+" || Item Id: "+rs.getInt(2)+ "|| Item name: "+User.items.get(rs.getInt(2))+" || Item count: "+rs.getInt(3));
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void createItem(Item item){
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "bala-11786", "password");
            PreparedStatement stmt = con.prepareStatement("insert into menu (item_name, item_cost) values (?,?)");
            stmt.setString(1,item.name);
            stmt.setInt(2, item.cost);
            stmt.executeUpdate();
            con.close();
            initMenu();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void getItem(int id){
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "bala-11786", "password");
            PreparedStatement stmt = con.prepareStatement("select item_id, item_name, item_cost from menu where item_id = ?");
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                System.out.println("Item Id: "+rs.getInt(1)+" || Item name: "+rs.getString(2)+" || Item count: "+rs.getInt(3));
            }
            con.close();
            initMenu();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void updateItem(Item item){
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "bala-11786", "password");
            PreparedStatement stmt = con.prepareStatement("update menu set item_name=?, item_cost=? where item_id=?");
            stmt.setString(1, item.name);
            stmt.setInt(2, item.cost);
            stmt.setInt(3, item.id);
            stmt.executeUpdate();
            con.close();
            initMenu();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void deleteItem(int item_id){
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "bala-11786", "password");
            PreparedStatement stmt = con.prepareStatement("delete from menu where item_id=?");
            stmt.setInt(1, item_id);
            stmt.executeUpdate();
            con.close();
            initMenu();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void initMenu(){
        try{
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "bala-11786", "password");
            PreparedStatement stmt = con.prepareStatement("select item_id, item_name, item_cost from menu;");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                User.items.put(rs.getInt(1), new Item(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}