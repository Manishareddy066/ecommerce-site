package mvc;

import java.util.List;

public class ShoppingBLL {
	public double calculateGST(double pr, double gst, int dis) {
		// Implement GST calculation logic
		double actual_price = (pr) / (1 + (gst / 100));
		System.out.println("ap " + actual_price);
		double prod_gst = pr - actual_price;
		System.out.println("pg " + prod_gst);
		double prod_dis = actual_price * (dis / 100);
		double ap = actual_price - prod_dis;
		double after_dis_gst = (prod_gst * ap) / actual_price;
		return after_dis_gst;
	}

	public double calculateGrandTotal(List<Double> prod_ship, double ot) {
		// TODO Auto-generated method stub
		double total = ot;
		for (int i = 0; i < prod_ship.size(); i++) {
			System.out.println("to " + total);
			total = total + prod_ship.get(i);
		}
		return total;
	}

	public double calculateShipping(double prod_price, double orderTotal, int shippingAmt) {
		double shipping = (prod_price / orderTotal) * shippingAmt;
		return shipping;
	}

	public double calculateGSTShipping(double shippingCharges, int gst) {
		// TODO Auto-generated method stub
		double gst_shipping = (gst / 100) * shippingCharges;
		return gst_shipping;
	}
}