
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/Login")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String username = "root", password = "123456";

    public Login() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(response.SC_MOVED_TEMPORARILY);
        if (username.equals(request.getParameter("username")) && password .equals(request.getParameter("password")) ) {
            response.setContentType("text/html;charset=UTF-8");
            response.setStatus(response.SC_MOVED_TEMPORARILY);
            response.setHeader("Location", "http://172.18.57.145/Unicom/html/index.html");
        } else {
            response.setContentType("text/html;charset=UTF-8");
            response.setStatus(response.SC_MOVED_TEMPORARILY);
            response.setHeader("Location", "http://172.18.57.145/Unicom/html/login.html");
        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
