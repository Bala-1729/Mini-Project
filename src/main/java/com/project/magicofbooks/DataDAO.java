package com.project.magicofbooks;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DataDAO {
    public static int register(String user_name, String password) {
        int status=-1;
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/magicofbooks", "bala-11786", "password");
            PreparedStatement stmt = con.prepareStatement("insert into user (user_name, password) values (?,?)");
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
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/magicofbooks", "bala-11786", "password");
            PreparedStatement stmt = con.prepareStatement("select password,user_id from user where user_name=?");
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
    public static void displayBooks() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/magicofbooks", "bala-11786", "password");
            PreparedStatement stmt = con.prepareStatement("select * from books");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                System.out.println("Book Id: "+rs.getInt(1)+" || Book name: "+rs.getString(2));
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void getBookDetails(int book_id) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/magicofbooks", "bala-11786", "password");
            PreparedStatement stmt = con.prepareStatement("select * from books where book_id=?");
            stmt.setInt(1,book_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                System.out.println(new Book(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(6), rs.getInt(7)));
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void bookOperations(int user_id, int book_id, int option, String type){
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/magicofbooks", "bala-11786", "password");
            if(option==1){
                PreparedStatement stmt_1 = con.prepareStatement("select book_name from books where book_id=?");
                stmt_1.setInt(1, book_id);
                ResultSet rs = stmt_1.executeQuery();
                rs.next();
                String book_name = rs.getString(1);

                PreparedStatement stmt_2 = con.prepareStatement("insert into selectedBooks(user_id, book_id, book_name, type) values(?,?,?,?)");
                stmt_2.setInt(1, user_id);
                stmt_2.setInt(2, book_id);
                stmt_2.setString(3, book_name);
                stmt_2.setString(4, type);

                stmt_2.executeUpdate();
                updateCopiesSold(book_id);
            }
            else if(option==2){
                PreparedStatement stmt = con.prepareStatement("delete from selectedBooks where user_id=? and book_id=? and type=?");
                stmt.setInt(1, user_id);
                stmt.setInt(2, book_id);
                stmt.setString(3, type);
                stmt.executeUpdate();
            }
            else if(option==3){
                PreparedStatement stmt = con.prepareStatement("select book_id, book_name from selectedBooks where type=?");
                stmt.setString(1, type);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()){
                    System.out.println("Book ID: "+rs.getInt(1)+" || Book name: "+rs.getString(2));
                }
            }
            con.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
    }

    public static void addUpdateBook(int book_id, String book_name, String author_name, String desc, String genre, int price) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/magicofbooks", "bala-11786", "password");
            PreparedStatement stmt = null;
            if(book_id==-1){
                 stmt = con.prepareStatement("insert into books (book_name, author_name, description, genre, price) values(?,?,?,?,?)");
            }
            else{
                stmt = con.prepareStatement("update books set book_name=?, author_name=?, description=?, genre=?, price=? where book_id=?");
                stmt.setInt(6, book_id);
            }
            stmt.setString(1, book_name);
            stmt.setString(2, author_name);
            stmt.setString(3, desc);
            stmt.setString(4, genre);
            stmt.setInt(5, price);
            stmt.executeUpdate();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void deleteBook(int book_id) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/magicofbooks", "bala-11786", "password");
            PreparedStatement stmt = con.prepareStatement("delete from books where book_id=?");
            stmt.setInt(1, book_id);
            stmt.executeUpdate();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void displayGenreBooks() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/magicofbooks", "bala-11786", "password");
            PreparedStatement stmt = con.prepareStatement("select * from books where genre='auto-bio'");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                System.out.println("Book Id: "+rs.getInt(1)+" || Book name: "+rs.getString(2));
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void displayBooksOrder(int option) {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/magicofbooks", "bala-11786", "password");
            PreparedStatement stmt = null;
            if(option==2){
                stmt = con.prepareStatement("select * from books order by price desc");
            }
            else if(option==1){
                stmt = con.prepareStatement("select * from books order by number_of_copies_sold asc");
            }
            else {
                stmt = con.prepareStatement("select * from books order by price asc");
            }
            System.out.println(stmt.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                System.out.println("Book Id: "+rs.getInt(1)+" || Book name: "+rs.getString(2));
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void updateCopiesSold(int book_id){
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/magicofbooks", "bala-11786", "password");
            PreparedStatement stmt_1 = con.prepareStatement("select number_of_copies_sold from books where book_id=?");
            stmt_1.setInt(1,book_id);
            ResultSet rs = stmt_1.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            PreparedStatement stmt_2 = con.prepareStatement("update books set number_of_copies_sold=? where book_id=?");
            stmt_2.setInt(1,count+1);
            stmt_2.setInt(2, book_id);
            stmt_2.executeUpdate();
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
