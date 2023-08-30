package org.example.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.example.yurim.Statics.*;

public class BookDAO {
    private Connection conn;
    private Statement st;
    private PreparedStatement ps;
    private ResultSet rs;

    /**DB Connection 함수**/
    public void getConnection(){
        try {
            Class.forName(CLASSNAME);
            conn = DriverManager.getConnection(CONNECTION_URL,USERNAME,PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException 발생");
        } catch (SQLException e){
            System.out.println("Connection 단계에서 SQLException 발생");
        }
    }

    /**Book Info INSERT 함수**/
    public void bookRegister(List<Book> bookList){
        String InsertSQL = "insert into booktable(title, price, publisher, authors, sale_price, isbn ) values(?,?,?,?,?,?)";
        getConnection();
        try{
            ps = conn.prepareStatement(InsertSQL);
            for(Book book : bookList){
                ps.setString(1, book.getTitle());
                ps.setInt(2, book.getPrice());
                ps.setString(3, book.getPublisher());
                ps.setString(4, book.getAuthors());
                ps.setInt(5, book.getSale_price());
                ps.setString(6, book.getIsbn());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("INSERT 단계에서 SQLException 발생");
        } finally {
            dbClose();
        }
    }

    /**Book Info SELECT 함수**/
    public List<Book> bookSelect(){
        String SelectSQL = "select * from booktable order by title";
        getConnection();
        List<Book> bookListDB = new ArrayList<>();
        try {
            ps = conn.prepareStatement(SelectSQL);
            rs = ps.executeQuery();
            while(rs.next()){
                String title = rs.getString("title");
                int price = rs.getInt("price");
                String publisher = rs.getString("publisher");
                String authors = rs.getString("authors");
                int sale_price = rs.getInt("sale_price");
                String isbn = rs.getString("isbn");
                Book book = new Book(title,price,publisher,authors,sale_price,isbn);
                bookListDB.add(book);
            }
        } catch (SQLException e) {
            System.out.println("SELECT 단계에서 SQLException 발생");
        } finally {
            dbClose();
        }
        return bookListDB;
    }


    /**DB Connection CLOSE 함수**/
    public void dbClose(){
        try {
            if(rs != null){
                rs.close();
            }
            if(ps != null){
                ps.close();
            }
            if(conn != null){
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("DB closing 단계에서 SQLException 발생");
        }
    }

}
