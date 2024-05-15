package mvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {
	ShoppingDAO sd;
	ShoppingBLL sb;

	public void init() {
		sd = new ServiceDAL();
		sb = new ShoppingBLL();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<ProductModel> cartItems = (List<ProductModel>) request.getSession().getAttribute("cartItems");
		List<OrderModel> orderDetails = new ArrayList<>();
		double orderTotal = 0.0;

		if (cartItems != null) {
			// Calculate order total for all items
			for (ProductModel item : cartItems) {
				orderTotal += item.getProd_price();

			}

			for (ProductModel item : cartItems) {
				// Create a new OrderModel for each item
				OrderModel orderModel = new OrderModel();
				orderModel.setPm(item);

				// Calculate shipping charges for each item
				double shippingCharges = sd.calShipping(item.getProd_price(), orderTotal);
				orderModel.setShippingCharges(shippingCharges);
				// Calculate GST on shipping charges for each item
				double gstShippingCharges = sd.calGstShipping(shippingCharges, item.getProd_hsn());
				System.out.println(item.getProd_hsn());
				orderModel.setGSTOnShippingCharges(gstShippingCharges);
				// Set the same order total for all items
				orderModel.setOrderTotal(orderTotal);
				double grand_total = sd.calGrandTotal(orderTotal, gstShippingCharges);
				int cash_back = sd.calcashBack(grand_total);
				orderModel.setCash_back(cash_back);
				request.setAttribute("cash_back", cash_back);
				orderModel.setGrandTotal(grand_total);
				request.setAttribute("grandTotal", grand_total);
				double after_dis_gst = sd.calDiscount(item.getProd_price(), item.getProd_hsn());
				System.out.println("discount :" + after_dis_gst);
				orderModel.setAfter_dis_gst(after_dis_gst);
				request.setAttribute("after_dis_gst", after_dis_gst);
				// Add the orderModel to the list
				orderDetails.add(orderModel);
			}
		}

		// Set the list of order details in request attribute
		request.setAttribute("orderDetails", orderDetails);

		// Forward the request to checkout.jsp
		request.getRequestDispatcher("/checkout.jsp").forward(request, response);
	}
}