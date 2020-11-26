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

@WebServlet( "/BookSelectOne.do")
public class BookSelectOne extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("json/application;charset=utf-8");
        response.getWriter().print(json(request.getParameter("id")));
    }
    static String json(String bookid){
        String id1= bookid;
        String data="{\"data\":[";
        BookDAO d=new BookDAO();
        Book book=d.selectById(Integer.parseInt(id1));
        data+="{\"id\":\""+book.getId()+"\",\"name\":\""+book.getName()+"\",\"author\":\""+book.getAuthor()+"\",\"price\":\""+book.getPrice()+"\"}";
        data+="]}";
        return data;
    }
}
