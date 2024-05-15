package mvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/CartController")
public class CartController extends HttpServlet {
	ShoppingDAO sd;

	public void init() {
		sd = new ServiceDAL();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Extract product information from the request
		String productId = request.getParameter("productId");
		String productName = request.getParameter("productName");
		String productPrice = request.getParameter("productPrice");
		String productstock = request.getParameter("productStock");
		String productquantity = request.getParameter("productQuantity");
		int productCategory = Integer.parseInt(request.getParameter("productCatID"));
		String image = request.getParameter("productImage");
		String hsn_id = request.getParameter("hsnc");
		int pinCode = Integer.parseInt(request.getParameter("pincode"));
		System.out.println("Received pin code: " + pinCode);

		// Check if the pin code is serviceable
		boolean isServiceable = sd.checkServiceAvailability(pinCode, productCategory);
		// Create a ProductModel object
		if (isServiceable) {
			// Create a ProductModel object
			ProductModel product = new ProductModel();
			product.setProd_ID(Integer.parseInt(productId));
			product.setProd_name(productName);
			product.setProd_price(Double.parseDouble(productPrice));
			product.setP_stock(Integer.parseInt(productstock));
			product.setP_quantity(Integer.parseInt(productquantity));
			product.setProd_cate_ID(productCategory);
			product.setProd_img(image);
			product.setProd_hsn(Integer.parseInt(hsn_id));
			// Update product stock in the database

			int newStock = Integer.parseInt(productstock) - Integer.parseInt(productquantity);
			sd.updateProductStock(Integer.parseInt(productId), newStock);
			// Retrieve the current cart items from session or create a new list
			List<ProductModel> cartItems = getCartItemsFromSession(request);
			// Add the new product to the cart
			cartItems.add(product);
			// Update the cart in session
			updateCartInSession(request, cartItems);

			// Redirect to index.jsp after adding to cart
			response.sendRedirect("index.jsp");
		} else {
			// Return a response indicating service unavailable
			response.setContentType("text/plain");
			response.getWriter().write("Service unavailable for the provided pin code.");
		}

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Display the cart contents
		List<ProductModel> cartItems = getCartItemsFromSession(request);
		request.getSession().setAttribute("cartItems", cartItems);
		// Forward the request to the cart.jsp page
		request.getRequestDispatcher("/cart.jsp").forward(request, response);
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Extract product ID to be removed from the request
		String productIdToRemove = request.getParameter("productId");
		if (productIdToRemove != null) {
			int productId = Integer.parseInt(productIdToRemove);
			// Retrieve the current cart items from session
			List<ProductModel> cartItems = getCartItemsFromSession(request);
			// Remove the item with the specified ID from the cart
			cartItems.removeIf(item -> item.getProd_ID() == productId);
			// Update the cart in session
			updateCartInSession(request, cartItems);
			// Respond with success status
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			// If product ID is not provided in the request, respond with bad request status
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	private List<ProductModel> getCartItemsFromSession(HttpServletRequest request) {
		// Retrieve cart items from session or create a new list if not present
		List<ProductModel> cartItems = (List<ProductModel>) request.getSession().getAttribute("cart");
		if (cartItems == null) {
			cartItems = new ArrayList<>();
			request.getSession().setAttribute("cart", cartItems);
		}
		return cartItems;
	}

	private void updateCartInSession(HttpServletRequest request, List<ProductModel> cartItems) {
		// Update cart items in session
		request.getSession().setAttribute("cart", cartItems);
	}
}