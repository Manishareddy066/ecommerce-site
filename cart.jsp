<%@ page import="mvc.ProductModel" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="mvc.ServiceDAL" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart</title>
    <style>
        body {
            background-image: linear-gradient(to right, #ffbb33, #ff6f91);
        }

        .container {
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
        }

        .cart-item {
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 10px;
            margin-bottom: 20px;
            display: flex;
            align-items: center;
        }

        .cart-item img {
            width: 100px;
            height: auto;
            margin-right: 20px;
        }

        .cart-item-details {
            flex: 1;
        }

        .cart-item-details h3 {
            margin: 0;
        }

        .cart-item-details p {
            margin: 5px 0;
        }

        .remove-button {
            background-color: #ff6347;
            border: none;
            color: white;
            padding: 8px 16px;
            text-align: center;
            text-decoration: none;
            font-size: 14px;
            cursor: pointer;
            border-radius: 4px;
            margin-left: auto; /* Move the button to the right */
        }

        .remove-button:hover {
            background-color: #ff4332;
        }

        .checkout-button {
            background-color: #4CAF50;
            border: none;
            color: white;
            padding: 8px 16px;
            text-align: center;
            text-decoration: none;
            font-size: 14px;
            cursor: pointer;
            border-radius: 4px;
        }

        .checkout-button:hover {
            background-color: #45a049;
        }
        
    </style>
</head>
<body>
<div class="container">
    <h2>Shopping Cart</h2>
    <%
        ServiceDAL serviceDAL = new ServiceDAL();
        Map<Integer, Integer> quantityMap = new HashMap<>();
        List<ProductModel> cartItems = (List<ProductModel>) request.getSession().getAttribute("cartItems");
        if (cartItems != null) {
            for (ProductModel item : cartItems) {
                int productId = item.getProd_ID();
                quantityMap.put(productId, quantityMap.getOrDefault(productId, 0) + 1);
            }
            for (ProductModel item : cartItems) {
                int productId = item.getProd_ID();
                if (quantityMap.containsKey(productId)) {
                    int quantity = quantityMap.get(productId);
    %>
    <div class="cart-item">
        <img src="<%= item.getProd_img() %>" alt="<%= item.getProd_name() %>">
        <div class="cart-item-details">
            <h3><%= item.getProd_name() %></h3>
            <p>Price: $<%= item.getProd_price() %></p>
            <p>Quantity: <%= quantity %></p>
            <p>Available Stock: <%= item.getP_stock() - quantity %></p>
            <button class="remove-button" onclick="removeItem('<%= item.getProd_ID() %>')">Remove</button>
            <button class="checkout-button" onClick="placeOrder()">Place Order</button>
        </div>
    </div>
    <%
                    quantityMap.remove(productId);
                }
            }
        }
    %>
    <a href="ProductController" class="checkout-button">Continue Shopping</a>
    <a href="#" class="checkout-button" onclick="checkout()">Checkout</a>
</div>

<script>
    function checkout() {
        window.location.href = "CheckoutServlet";
    }
    function placeOrder() {
        fetch('OrderServlet', {
            method: 'POST'
        })
            .then(response => {
                if (response.ok) {
                    window.location.reload();
                } else {
                    console.error('Failed to place order');
                }
            })
            .catch(error => {
                console.error('Error placing order:', error);
            });
    }

    function removeItem(productId) {
        fetch('CartController?productId=' + productId, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    window.location.reload();
                } else {
                    console.error('Failed to remove item from cart');
                }
            })
            .catch(error => {
                console.error('Error removing item from cart:', error);
            });
    }
</script>

</body>
</html>