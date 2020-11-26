package dao;

import db.DBConnection;
import entity.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BookDAO implements BookInterface{
     static List<Book> list=new ArrayList<>();
        static {
             list.add(new Book(1,"中国","李晓鸥",251));
            list.add(new Book(2,"美国","张晓东",441));
            list.add(new Book(3,"德国","发卡死",151));
            list.add(new Book(4,"日本","fsa gads",351));
            list.add(new Book(5,"中国","李晓鸥",451));

        }

    @Override
    public Book add(Book b) {
        try {
            int i = 0;
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps=null;
            String sql = "insert into book values(?,?,?,?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,b.getId());
            ps.setString(2,b.getName());
            ps.setString(3,b.getAuthor());
            ps.setFloat(4,b.getPrice());
            i = ps.executeUpdate();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    @Override
    public boolean delete(int b) {
        try {
            int i = 0;
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps=null;
            String sql = "delete from book where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,b);
            i = ps.executeUpdate();
            ps.close();
            conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean update(Book b) {
            for(Book book:list){
                if(book.getId()==b.getId()){
                    book.setAuthor(b.getAuthor());
                    book.setName(b.getName());
                    book.setPrice(b.getPrice());
                    return true;
                }
            }
        return false;
    }

    @Override
    public Book selectById(int b) {
        Book book1 = new Book();
        try {
            int i = 0;
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps=null;
            ResultSet rs = null;
            String sql = "select id,name,author, price from book where id=?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1,b);
            rs = ps.executeQuery();
            if(rs.next()){
                int id= rs.getInt(1);
                String name=rs.getString(2);
                String author = rs.getString(3);
                float price = rs.getFloat(4);
                book1.setId(id);
                book1.setName(name);
                book1.setAuthor(author);
                book1.setPrice(price);
            }
            ps.close();
            conn.close();
            return book1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Book selectByName(Book b) {
        for (Book book:list){
            if(book.getName().toUpperCase()==b.getName().toUpperCase()){
                return book;
            }
        }
        return null;
    }

    @Override
    public List<Book> selectAll() {
        List<Book> listBook=new ArrayList<>();
        try {
            int i = 0;
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps=null;
            ResultSet rs = null;
            String sql = "select id,name,author, price from book";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                int id= rs.getInt(1);
                String name=rs.getString(2);
                String author = rs.getString(3);
                float price = rs.getFloat(4);
                Book book1 = new Book();
                book1.setId(id);
                book1.setName(name);
                book1.setAuthor(author);
                book1.setPrice(price);
                listBook.add(book1);
            }
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listBook;
    }
}
