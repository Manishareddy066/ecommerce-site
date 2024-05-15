package mvc;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {
	ShoppingDAO sd;

	public void init() {
		sd = new ServiceDAL();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<ProductModel> cartItems = (List<ProductModel>) request.getSession().getAttribute("cartItems");
		double orderTotal = 0;
		if (cartItems != null) {
			// Calculate order total for all items
			for (ProductModel item : cartItems) {
				orderTotal += item.getProd_price();

			}
		}
		// Create an OrderModel object to hold order details
		OrderModel orderModel = new OrderModel();
		sd.insertOrderAndItems(cartItems, orderTotal, orderModel);
		// Optionally, you can set orderModel as a request attribute and forward it to a confirmation page
		request.setAttribute("orderModel", orderModel);
		request.getRequestDispatcher("cart.jsp").forward(request, response);
	}
}
