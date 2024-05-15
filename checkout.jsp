<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="mvc.OrderModel" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Checkout</title>
    <style>
        /* Styles for the table */
        table {
            border-collapse: collapse;
            width: 100%;
        }

        th, td {
            border: 1px solid #dddddd;
            text-align: left;
            padding: 8px;
        }

        th {
            background-color: #f2f2f2;
        }

        /* Other styles */
        body {
            background-image: linear-gradient(to right, #ffbb33, #ff6f91);
        }

        .back-to-cart {
            display: inline-block;
            padding: 10px 20px;
            margin-top: 20px;
            background-color: #4CAF50;
            color: white;
            text-decoration: none;
            border-radius: 8px;
            transition: background-color 0.3s ease;
        }

        .back-to-cart:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
    <h2>Order Summary</h2>
    <table>
        <tr>
            <th>Product Name</th>
            <th>Product ID</th>
            <th>Price</th>
            <th>Order Total</th>
            <th>Shipping Charges</th>
            <th>Shipping Charges (GST)</th>
            <th>GST After Discount</th>
            <th>Cash Back</th>
        </tr>
        <% 
            List<OrderModel> orderDetails = (List<OrderModel>) request.getAttribute("orderDetails");
            double grandTotal = 0.0;
            if (orderDetails != null) {
                for (OrderModel orderModel : orderDetails) {
                    grandTotal = orderModel.getOrderTotal();
        %>
        <tr>
            <td><%= orderModel.getPm().getProd_name() %></td>
            <td><%= orderModel.getPm().getProd_ID() %></td>
            <td><%= orderModel.getPm().getProd_price() %></td>
            <td><%= orderModel.getOrderTotal() %></td>
            <td><%= orderModel.getShippingCharges() %></td>
            <td><%= orderModel.getGSTOnShippingCharges() %></td>
            <td><%= orderModel.getAfter_dis_gst() %></td>
            <td><%= orderModel.getCash_back() %></td>
        </tr>
        <% 
                }
            }
        %>
    </table>
    <form id="couponForm">
        
       
        <select name="code" id="couponCode">
<option value="CODE1">CODE1</option>
<option value="CODE2">CODE2</option>
<option value="CODE3">CODE3</option>
<option value="CODE4">CODE4</option>
<option value="CODE5">CODE5</option>
</select>
 <button type="button" id="applyCouponBtn">Apply</button>
    </form>

    <p>Total Grand Total: <%= request.getAttribute("grandTotal") %></p> <!-- Display Total Grand Total -->

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script>
 
        $('#applyCouponBtn').click(function () {
            var code = $('#couponCode').val();
            $.ajax({
                type: 'POST',
                url: 'CouponServlet',
                data: { code: code },
                success: function (response) {
                    // Handle the response here
                    console.log("Response value: " + response);

                    // Get the current grand total
                    var grandTotal = parseFloat('<%= request.getAttribute("grandTotal") %>');
                    var cash_back = parseInt('<%= request.getAttribute("cash_back") %>');
                    // Subtract the response value from the grand total
                    var newGrandTotal = grandTotal - parseFloat(response)- cash_back;
					$('#cpn').val(response);
                    // Update the grand total on the page
                    $('#grandTotal').text(newGrandTotal.toFixed(2));
                    
                    
                },
                error: function (xhr, status, error) {
                    // Handle errors here
                    console.error(xhr.responseText);
                }
            });
        });
   
</script>


    <!-- Display Total Grand Total -->
    
<p>Total Grand Total after applying coupon: <span id="grandTotal"><%= request.getAttribute("grandTotal") %></span></p>
<form>
discount:<input type="text" id="cpn">
</form>
    <a href="cart.jsp" class="back-to-cart">Back to Cart</a>
    <a href="payment.jsp" class="back-to-cart">Proceed To Payment</a>

    
</body>
</html>
