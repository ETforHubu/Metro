package web;

import dao.BookDAO;
import entity.Book;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet( "/BookSelect.do")
public class BookSelect extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("json/application;charset=utf-8");
        response.getWriter().print(json());
    }
    static String json(){
        String data="{\"data\":[";
        String fh="";
        BookDAO d=new BookDAO();
        List<Book> list=d.selectAll();
        for(Book book:list){
            data+=fh+"{\"id\":\""+book.getId()+"\",\"name\":\""+book.getName()+"\",\"author\":\""+book.getAuthor()+"\",\"price\":\""+book.getPrice()+"\"}";
            fh=",";
        }
        data+="]}";
        return data;
    }
}
