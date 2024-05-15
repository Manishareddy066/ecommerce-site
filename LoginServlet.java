
package mvc;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	ShoppingDAO sd;

	public void init() {
		sd = new ServiceDAL();
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String username = req.getParameter("user");
		String password = req.getParameter("pwd");
		String isValid = sd.validateUser(username, password);
		res.setContentType("text/plain");
		PrintWriter pw = res.getWriter();
		pw.print(isValid);
	}
}