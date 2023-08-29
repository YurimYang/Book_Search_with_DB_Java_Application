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

    public void getConnection(){
        try {
            Class.forName(CLASSNAME);
            conn = DriverManager.getConnection(CONNECTION_URL,USERNAME,PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**Book Info INSERT 기능**/
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbClose();
        }
    }

    

    /**Book Info SELECT 기능**/
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbClose();
        }
        return bookListDB;
    }


    /**close기능**/
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
