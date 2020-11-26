package web;

import dao.BookDAO;
import entity.Book;

import javax.servlet.annotation.WebServlet;
import java.io.IOException;
        @WebServlet("/BookAdd.do")
        public class BookAdd extends javax.servlet.http.HttpServlet {
         protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        int id= Integer.parseInt(request.getParameter("id"));
        String name=request.getParameter("name");
        float price=Float.parseFloat(request.getParameter("price"));
        String author=request.getParameter("author");
        System.out.println(id);
        System.out.println(name);
        System.out.println(price);
        System.out.println(author);
        BookDAO bookDAO=new BookDAO();
        if(bookDAO.add(new Book(id,name,author,price))!=null){
            response.getWriter().print("添加成功");
        }else{
            response.getWriter().print("添加失败");
        }

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }
}
