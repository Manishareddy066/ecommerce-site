package mvc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ServiceDAL implements ShoppingDAO {
	private final Properties properties = new Properties();
	ShoppingBLL sb;

	public ServiceDAL() {
		sb = new ShoppingBLL();
		try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
			if (input == null) {
				System.err.println("Unable to find db.properties file");
				return;
			}
			properties.load(input);
			System.out.println("Loaded properties: " + properties); // Print loaded properties
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String validateUser(String username, String password) {
		String isValid = null;
		try {
			Class.forName("org.postgresql.Driver");
			try (Connection conn = DriverManager.getConnection(properties.getProperty("db.url"),
					properties.getProperty("db.user"), properties.getProperty("db.password"))) {
				String query = "SELECT password FROM cust_det_05 WHERE name=?";
				try (PreparedStatement st = conn.prepareStatement(query)) {
					st.setString(1, username);
					try (ResultSet rs = st.executeQuery()) {
						if (rs.next()) {
							String storedPassword = rs.getString("password");
							if (password.equals(storedPassword)) {
								isValid = "success";
							}
						}
					}
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return isValid;
	}

	public List<ProductModel> getAllProducts() throws SQLException {
		List<ProductModel> products = new ArrayList<>();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Connection conn = null;
		String query = "SELECT p.prod_id, p.prod_title, s.prod_price, p.prod_hsnc_id, p.prod_image, p.prod_prct_id, s.prod_batchno, s.prod_stock, s.prod_mrp FROM i205_products p JOIN i205_product_stock s ON p.prod_id = s.prod_id";
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {

			conn = DriverManager.getConnection(properties.getProperty("db.url"), properties.getProperty("db.user"),
					properties.getProperty("db.password"));
			stmt = conn.prepareStatement(query);
			rs = stmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("prod_id");
				String name = rs.getString("prod_title");
				double price = rs.getDouble("prod_price");
				int HSNCode = rs.getInt("prod_hsnc_id");
				String img = rs.getString("prod_image");
				int category = rs.getInt("prod_prct_id");
				int stock = rs.getInt("prod_stock");
				ProductModel product = new ProductModel();
				product.setProd_ID(id);
				product.setProd_name(name);
				product.setProd_price(price);
				product.setProd_hsn(HSNCode);
				product.setProd_img(img);
				product.setProd_cate_ID(category);
				product.setP_stock(stock);
				products.add(product);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return products;
	}

	public List<ProductModel> getProductsByCategoryId(int categoryId) throws SQLException {
		List<ProductModel> products = new ArrayList<>();
		ResultSet rs = null;
		PreparedStatement stmt = null;
		Connection conn = null;
		String query = "SELECT p.prod_id, p.prod_title, s.prod_price, p.prod_hsnc_id, p.prod_image, p.prod_prct_id, "
				+ "s.prod_batchno, s.prod_stock, s.prod_mrp FROM i205_products p JOIN i205_product_stock s ON p.prod_id = s.prod_id AND p.prod_prct_id=?";
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(properties.getProperty("db.url"), properties.getProperty("db.user"),
					properties.getProperty("db.password"));
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, categoryId);
			rs = stmt.executeQuery();

			while (rs.next()) {
				// Create ProductModel objects and add them to the list
				ProductModel product = new ProductModel();
				int id = rs.getInt("prod_id");
				String name = rs.getString("prod_title");
				double price = rs.getDouble("prod_price");
				int HSNCode = rs.getInt("prod_hsnc_id");
				String img = rs.getString("prod_image");
				int category = rs.getInt("prod_prct_id");
				int stock = rs.getInt("prod_stock");
				product.setProd_ID(id);
				product.setProd_name(name);
				product.setProd_price(price);
				product.setProd_hsn(HSNCode);
				product.setProd_img(img);
				product.setProd_cate_ID(category);
				product.setP_stock(stock);
				// product.setP_quantity(0);
				products.add(product);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			// Handle exceptions appropriately
		} finally {
			// Close ResultSet, PreparedStatement, and Connection
			// This is usually done in a finally block to ensure resources are released
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return products;
	}

	public boolean checkServiceAvailability(int pinCode, int productCategory) {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try (

				// Establish a connection to the database
				Connection conn = DriverManager.getConnection(properties.getProperty("db.url"),
						properties.getProperty("db.user"), properties.getProperty("db.password"));
				// Create a prepared statement to execute the query
				PreparedStatement statement = conn
						.prepareStatement("SELECT COUNT(*) " + "FROM i205_serviceable_regions sr "
								+ "JOIN i205_product_category_wise_serviceable_regions csr ON sr.srrg_id = csr.srrg_id "
								+ "JOIN i205_unserviceable_locations usl ON csr.prct_id = usl.prct_id "
								+ "WHERE sr.srrg_pinfrom <= ? AND sr.srrg_pinto >= ? "
								+ "AND csr.prct_id = ? AND usl.loc_id != ?");) {
			// Set the pin code parameters
			statement.setInt(1, pinCode);
			statement.setInt(2, pinCode);
			statement.setInt(3, productCategory);
			statement.setInt(4, pinCode);
			// Execute the query and get the result set
			ResultSet resultSet = statement.executeQuery();

			// If the result set has at least one row, the pin code is serviceable
			if (resultSet.next() && resultSet.getInt(1) > 0) {

				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// Handle any SQL exceptions
		}

		// If no serviceable regions match the pin code, return false
		return false;
	}

	public void updateProductStock(int productId, int newStock) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			// Establish database connection
			conn = DriverManager.getConnection(properties.getProperty("db.url"), properties.getProperty("db.user"),
					properties.getProperty("db.password"));
			System.out.println(productId);
			System.out.println(newStock);
			conn.setAutoCommit(false);
			// Prepare SQL statement
			String sql = "UPDATE i205_product_stock SET prod_stock = ? WHERE prod_id = ?";
			pstmt = conn.prepareStatement(sql);

			// Set parameters
			pstmt.setInt(1, newStock);
			pstmt.setInt(2, productId);

			// Execute update
			int rowsAffected = pstmt.executeUpdate();
			conn.commit();

			if (rowsAffected > 0) {
				System.out.println("Product stock updated successfully for product ID: " + productId);
			} else {
				System.out.println("Failed to update product stock for product ID: " + productId);
			}
		} catch (SQLException e) {
			System.out.println("SQL Error while updating product stock:");
			e.printStackTrace();
		} finally {
			// Close PreparedStatement and Connection
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println("Error while closing PreparedStatement or Connection:");
				e.printStackTrace();
			}
		}
	}

	public void insertOrderAndItems(List<ProductModel> cartItems, double orderTotal, OrderModel orderModel) {

		String orderId = null;
		try {
			orderId = generateOrderId();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String orderdate = null;
		try {
			orderdate = generateOrderDate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OrderModel om = new OrderModel();
		try (Connection conn = DriverManager.getConnection(properties.getProperty("db.url"),
				properties.getProperty("db.user"), properties.getProperty("db.password"))) {
			conn.setAutoCommit(false);

			// Insert into orders_tab_05 table
			try (PreparedStatement orderStmt = conn
					.prepareStatement("INSERT INTO i205_orders (order_id,order_date,order_total) VALUES (?, ?,?)")) {
				orderStmt.setString(1, orderId);
				om.setOrderId(orderId);
				orderStmt.setString(2, orderdate);
				om.setOrderDate(orderdate);
				orderStmt.setDouble(3, orderTotal);
				om.setOrderTotal(orderTotal);
				orderStmt.executeUpdate();
			}

			// Insert into order_products_tab_05 table

			try (PreparedStatement itemStmt = conn
					.prepareStatement("INSERT INTO i205_order_products (order_id,prod_id,price) VALUES (?, ?,?)")) {
				for (ProductModel item : cartItems) {
					itemStmt.setString(1, orderId);
					itemStmt.setInt(2, item.getProd_ID());

					itemStmt.setDouble(3, item.getProd_price());
					itemStmt.executeUpdate();
				}
			}
			// Populate the orderModel object with the order details
			orderModel.setOrderId(orderId);
			orderModel.setOrderDate(orderdate);
			orderModel.setOrderTotal(orderTotal);

			conn.commit();
		} catch (SQLException e) {
			// Handle exception
			e.printStackTrace();
		}
	}

	private String generateOrderId() throws SQLException {
		String orderId = null;
		try (Connection conn = DriverManager.getConnection(properties.getProperty("db.url"),
				properties.getProperty("db.user"), properties.getProperty("db.password"))) {
			try (PreparedStatement stmt = conn.prepareStatement("SELECT generate_order_id()")) {
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					orderId = rs.getString(1);
				}
			}
		}
		return orderId;
	}

	private String generateOrderDate() throws SQLException {
		String orderdate = null;
		try (Connection conn = DriverManager.getConnection(properties.getProperty("db.url"),
				properties.getProperty("db.user"), properties.getProperty("db.password"))) {
			try (PreparedStatement stmt = conn.prepareStatement("SELECT get_current_date()")) {
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					orderdate = rs.getString(1);
				}
			}
		}
		return orderdate;
	}

	public double calShipping(double prod_price, double ot) {
		int shippingAmt = 0;
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		double shipping = 0;
		try (Connection conn = DriverManager.getConnection(properties.getProperty("db.url"),
				properties.getProperty("db.user"), properties.getProperty("db.password"))) {
			String shippingQuery = "SELECT orvl_shippingamount FROM i205_OrderValueWiseShippingCharges WHERE orvl_from <= ? AND orvl_to >= ?";
			try (PreparedStatement stmt = conn.prepareStatement(shippingQuery)) {
				stmt.setDouble(1, ot);
				stmt.setDouble(2, ot);
				ResultSet rs1 = stmt.executeQuery();
				if (rs1.next()) {
					shippingAmt = rs1.getInt(1);
					shipping = sb.calculateShipping(prod_price, ot, shippingAmt);
				}
			}
		} catch (Exception e) {

		}
		return shipping;
	}

	public double calGrandTotal(double ot, double sc) {
		double grandTotal = 0.0;
		List<Double> prodShipChargesGst = new ArrayList<>();
		int shippingAmt = 0;
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Establish database connection
		try (Connection conn = DriverManager.getConnection(properties.getProperty("db.url"),
				properties.getProperty("db.user"), properties.getProperty("db.password"))) {
			// Retrieve shipping amount based on order value
			String shippingQuery = "SELECT orvl_shippingamount FROM i205_OrderValueWiseShippingCharges WHERE orvl_from <= ? AND orvl_to >= ?";
			try (PreparedStatement stmt = conn.prepareStatement(shippingQuery)) {
				stmt.setDouble(1, ot);
				stmt.setDouble(2, ot);
				ResultSet rs1 = stmt.executeQuery();
				if (rs1.next()) {
					shippingAmt = rs1.getInt(1);
				}
			} catch (Exception e) {
			}
			grandTotal = ot + sc + shippingAmt;
			System.out.println(grandTotal);

		} catch (Exception e) {
		}
		return grandTotal;
	}

	public double calGstShipping(double shippingCharges, int hsncId) {
		double gstShippingCharges = 0;
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(hsncId);
		try (Connection conn = DriverManager.getConnection(properties.getProperty("db.url"),
				properties.getProperty("db.user"), properties.getProperty("db.password"))) {
			String productQuery = "SELECT h.hsnc_gstc_percentage FROM i205_hsn_codes h WHERE h.hsnc_id = ?";
			try (PreparedStatement stmt = conn.prepareStatement(productQuery)) {
				stmt.setInt(1, hsncId);
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) {
					int gstPercentage = rs.getInt("hsnc_gstc_percentage");
					System.out.println(gstPercentage);
					System.out.println(shippingCharges);
					gstShippingCharges = (gstPercentage / 100.0) * shippingCharges;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gstShippingCharges;
	}

	public int applyCode(String code) {
		int flag = 0;
		int tval = 0;

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try (Connection conn = DriverManager.getConnection(properties.getProperty("db.url"),
				properties.getProperty("db.user"), properties.getProperty("db.password"))) {
			String productQuery = "SELECT dscpn_dis_val FROM i205_coupon WHERE dscpn_code = ?";
			try (PreparedStatement stmt = conn.prepareStatement(productQuery)) {
				stmt.setString(1, code);
				ResultSet rs = stmt.executeQuery();
				if (rs.next()) { // Move the cursor to the first row
					tval = rs.getInt(1); // Retrieve data from the ResultSet
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return tval;
	}

	@Override
	public double calDiscount(double prod_price, int prod_hsn) {
		// TODO Auto-generated method stub
		double gst = 0;
		int dis = 0;
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try (Connection conn = DriverManager.getConnection(properties.getProperty("db.url"),
				properties.getProperty("db.user"), properties.getProperty("db.password"))) {
			String pq = "SELECT p.prod_discount,h.hsnc_gstc_percentage FROM i205_hsn_codes h,i205_products p WHERE h.hsnc_id =p.prod_hsnc_id and h.hsnc_id= ?";

			try (PreparedStatement stmt = conn.prepareStatement(pq)) {
				stmt.setInt(1, prod_hsn);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					dis = rs.getInt(1);
					gst = rs.getDouble(2);
					System.out.println("dis" + dis);
					System.out.println("gst" + gst);

				}
				if (dis > 0)
					return (sb.calculateGST(prod_price, gst, dis));
				else {
					System.out.println("dis none");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int calcashBack(double grand_total) {
		// TODO Auto-generated method stub
		int cb = 0;
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try (Connection conn = DriverManager.getConnection(properties.getProperty("db.url"),
				properties.getProperty("db.user"), properties.getProperty("db.password"))) {
			String pq = "SELECT cash_back FROM i205_OrderValueWiseShippingCharges WHERE orvl_from <= ? AND orvl_to >= ?";

			try (PreparedStatement stmt = conn.prepareStatement(pq)) {
				stmt.setDouble(1, grand_total);
				stmt.setDouble(2, grand_total);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					cb = rs.getInt(1);

					System.out.println("cb" + cb);

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cb;
	}

}