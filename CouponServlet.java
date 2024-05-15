package mvc;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CouponServlet
 */
@WebServlet("/CouponServlet")
public class CouponServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ShoppingDAO sd;

	public void init() {
		sd = new ServiceDAL();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/plain");
		String code = request.getParameter("code");
		int val = sd.applyCode(code);
		PrintWriter out = response.getWriter();
		out.print(val);
		System.out.print("value is" + val); // Send coupon value as response

	}
}
