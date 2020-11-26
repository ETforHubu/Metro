package web;

import dao.BookDAO;
import entity.Book;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/BookUpdate")
public class BookUpdate extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        int id= Integer.parseInt(request.getParameter("id"));
        String name=request.getParameter("name");
        float price=Float.parseFloat(request.getParameter("price"));
        String author=request.getParameter("author");
        System.out.println(id);
        BookDAO bookDAO=new BookDAO();
        if(bookDAO.update(new Book(id,name,author,price))){
            response.getWriter().print("修改成功");
        }else{
            response.getWriter().print("修改失败");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id= Integer.parseInt(request.getParameter("id"));
        BookDAO dao=new BookDAO();
        request.getSession().setAttribute("book",dao.selectById(id));
        String sele=request.getParameter("sele");
        if("sele".equals(sele)){
            request.getRequestDispatcher("select.jsp").forward(request,response);
        }
        else
            request.getRequestDispatcher("update.jsp").forward(request,response);
    }
}
