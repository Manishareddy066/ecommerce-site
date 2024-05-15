package mvc;

import java.sql.SQLException;
import java.util.List;

public interface ShoppingDAO {
	List<ProductModel> getAllProducts() throws SQLException;

	List<ProductModel> getProductsByCategoryId(int categoryId) throws SQLException;

	boolean checkServiceAvailability(int pinCode, int productCategory);

	void updateProductStock(int productId, int newStock);

	double calGrandTotal(double ot, double sc);

	double calShipping(double prod_price, double orderTotal);
	// double calculateGST(double amount);
	//
	// double calculateShippingCharges(double orderTotal);

	double calGstShipping(double shippingCharges, int hsnid);

	String validateUser(String username, String password);

	void insertOrderAndItems(List<ProductModel> cartItems, double orderTotal, OrderModel orderModel);

	int applyCode(String code);

	double calDiscount(double prod_price, int prod_hsn);

	int calcashBack(double grand_total);
}