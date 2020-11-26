package web;

import dao.BookDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/BookDelete")
public class BookDelete extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        BookDAO bookDAO=new BookDAO();
        //int id= Integer.parseInt(request.getParameter("id"));

        response.getWriter().print(delBook(request.getParameter("id")));
    }

    static String delBook(String bookid){
        BookDAO d=new BookDAO();
        if(d.delete(Integer.parseInt(bookid)))
            return "success";
        else
            return null;
    }
}
