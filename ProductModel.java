package mvc;

import java.io.Serializable;

public class ProductModel implements Serializable {
	private int p_id;
	private String p_name;
	private double p_price;
	private int p_hsn;
	private String p_image;
	private int p_cate_id;
	private int p_quantity;
	private int p_stock;
	private int p_discount;

	// Constructor and other methods omitted for brevity

	// public ProductModel(int p_id, String p_name, double p_price, String p_hsn, String p_image, String p_category) {
	// this.p_id = p_id;
	// this.p_name = p_name;
	// this.p_price = p_price;
	// this.p_hsn = p_hsn;
	// this.p_image = p_image;
	// this.p_category = p_category;
	// }

	public int getP_discount() {
		return p_discount;
	}

	public void setP_discount(int p_discount) {
		this.p_discount = p_discount;
	}

	public int getP_stock() {
		return p_stock;
	}

	public void setP_stock(int p_stock) {
		this.p_stock = p_stock;
	}

	public int getP_quantity() {
		return p_quantity;
	}

	public void setP_quantity(int p_quantity) {
		this.p_quantity = 0;
	}

	public void setProd_ID(int p_id) {
		this.p_id = p_id;
	}

	public void setProd_name(String p_name) {
		this.p_name = p_name;
	}

	public void setProd_price(double p_price) {
		this.p_price = p_price;
	}

	public void setProd_hsn(int p_hsn) {
		this.p_hsn = p_hsn;
	}

	public void setProd_img(String p_image) {
		this.p_image = p_image;
	}

	public void setProd_cate_ID(int p_cate_id) {
		this.p_cate_id = p_cate_id;
	}

	public int getProd_ID() {
		return p_id;
	}

	public String getProd_name() {
		return p_name;
	}

	public double getProd_price() {
		return p_price;
	}

	public int getProd_hsn() {
		return p_hsn;
	}

	public String getProd_img() {
		return p_image;
	}

	public int getProd_cate_ID() {
		return p_cate_id;
	}
}