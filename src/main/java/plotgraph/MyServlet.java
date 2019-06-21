package plotgraph;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
        if (request.getParameter("updateButton") != null) {
        	UpdateData ud = new UpdateData(request.getParameter("N"));
        	System.out.println("開始更新"+request.getParameter("N"));
            ud.update();
//            response.setContentType("text/html");
//            java.io.PrintWriter out = response.getWriter( );
//            out.println("<h1>save</h1>");
        } 
        else if (request.getParameter("allUpdateButton") != null) {
        	System.out.println("開始更新全部資料");
        	UpdateAllData updateAllData = new UpdateAllData();
        	updateAllData.updateAll();
        	updateAllData.parseAndSavaAll();
        	
        } 

        request.getRequestDispatcher("Graph3.jsp").forward(request, response);
    }

}
