<%@ page import="mvc.ProductModel" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
<style>
.nav-item1 {
  padding-left: 300px;
}
.nav-item2 {
  padding-left: 40px;
}
.nav-item3 {
  padding-left: 100px;
}
.navbar{
    background-image: linear-gradient(to right, #ff8a00, #e52e71);

}
.styling {
    
background-image: linear-gradient(to right, #ffbb33, #ff6f91);
}


.product-card {
  display: flex;
  flex-direction: column;
  align-items: center; /* Center horizontally */
  justify-content: center; /* Center vertically */
  border: 1px solid #ccc;
  border-radius: 5px;
  padding: 20px;
  margin: 10px;
  width: 200px; /* Adjust the width of the card as needed */
  height: 350px;
}

.product-card img {
 max-width: 100px; /* Adjust this value as needed */
  max-height: 200px;
  
}

.product-card-body {
  text-align: center;
}

</style>
<title>Home page</title>
</head>
<body>
<nav class="navbar navbar-expand-lg bg-body-tertiary">
  <div class="container-fluid">
    <a class="navbar-brand" href="#">SHOPPING SITE</a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNavDropdown">
    
      <ul class="navbar-nav">
        <form method="GET" id="categoryForm">
    <select class="btn btn-outline-dark" style="margin-right:40px;" name="category" id="categorySelect">
        <option value="">Select Category</option>
        <option value="1001">Electronics</option>
        <option value="1002">Clothing</option>
        <option value="1003">Footwear</option>
        <option value="1004">Furniture</option>
       	<option value="1005">Toys</option>
    </select>
</form>
<form method="GET" id="sortForm">
    <select class="btn btn-outline-dark" style="margin-right:40px;" name="sort" id="sortSelect">
        <option value="">sort by price</option>
        <option value="asc">Ascending</option>
        <option value="desc">Descending</option>
    </select>
</form>
        <li>
         <button type="button" class="btn btn-outline-dark" style="margin-right:40px;" onclick="viewCart()">View Cart</button>
        </li>
        
        <li >
          <a href="login.html" style="margin-right:40px;" class="btn btn-outline-dark" onClick="login.html">Login</a>
        </li>
        <li >
         <button type="button" style="margin-right:40px;" class="btn btn-outline-dark" onclick="window.location.href='register.jsp'">signup/Register</button>
         
        </li>
      </ul>
      
    </div>
    
  </div>
</nav>
<div class="styling">

<div class="container" id="productDisplayArea">
  <div class="row" >
    <% 
    List<mvc.ProductModel> productsList = (List<mvc.ProductModel>) request.getAttribute("products");
    if (productsList != null) {
      for (ProductModel product : productsList) {
    %>
    <div class="col-md-4">
      <div class="card product-card">
        <img src="<%= product.getProd_img() %>" class="card-img-top" alt="<%= product.getProd_name() %>">
        <div class="card-body product-card-body">
          <h5 class="card-title"><%= product.getProd_name() %></h5>
          <p class="card-text">Price: <%= product.getProd_price() %></p>
            
          <button onclick="addItemToCart('<%= product.getProd_ID() %>','<%= product.getProd_name() %>', '<%= product.getProd_price() %>','<%= product.getP_stock() %>', '<%= product.getP_quantity() %>','<%= product.getProd_cate_ID()%>','<%= product.getProd_img() %>','<%=product.getProd_hsn() %>')" class="btn btn-primary">Add to Cart</button>
        </div>
      </div>
    </div>
    <% 
      }
    }
    %>
  </div>
</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

<script>
$(document).ready(function() {
    $('#categorySelect').change(function() {
        var categoryId = $(this).val(); // Get the selected category ID
        var sort_by = $('#sortSelect').val(); // Get the selected sorting option

        // Make an AJAX request to fetch products based on category and sorting options
        $.ajax({
            type: "GET",
            url: "ProductController",
            data: { category: categoryId, sort: sort_by }, // Pass category and sort options as data
            success: function(response) {
                // Update the products section with the received HTML content
                $('#productDisplayArea .row').html($(response).find('.row').html());
            },
            error: function(xhr, status, error) {
                console.error(xhr.responseText);
                alert("Error fetching products");
            }
        });
    });

    $('#sortSelect').change(function() {
        var categoryId = $('#categorySelect').val(); // Get the selected category ID
        var sort_by = $(this).val(); // Get the selected sorting option

        // AJAX request to fetch products based on category and sorting options
        $.ajax({
            type: "GET",
            url: "ProductController",
            data: { category: categoryId, sort: sort_by }, // Pass category and sort options as data
            success: function(response) {
                // Update the products section with the received HTML content
                $('#productDisplayArea .row').html($(response).find('.row').html());
            },
            error: function(xhr, status, error) {
                console.error(xhr.responseText);
                alert("Error fetching products");
            }
        });
    });
});
</script>
<script>
function addItemToCart(id, name, price,stock,quantity,categoryID,prod_img,hsn) {
    // Prompt the user to enter a pin code
    var pinCode = prompt("Please enter your pin code:");
    
    quantity = quantity +1;
    if (pinCode === null || pinCode === "") {
        // If the user cancels or leaves the pin code empty, do nothing
        return;
    }
    alert(hsn);

    // Prepare the data to be sent in the AJAX request
    var data = {
        productId: id,
        productName: name,
        productPrice: price,
        productStock : stock,
        productQuantity:quantity,
        productCatID: categoryID,
        productImage: prod_img,
  		hsnc:hsn,
        pincode: pinCode  // Add pin code to the data
    };
    

    // AJAX request to verify the pin code
    $.ajax({
        type: "POST",
        url: "CartController",
        data: data,
        success: function(response) {
                // If pin code verification is successful, alert and add the item to cart
            	 if (response === "Service unavailable for the provided pin code.") {
        			alert(response);
    				} else {
        				alert("Item added to cart");
    				}
        },
        error: function(xhr, status, error) {
            console.error(xhr.responseText);
            alert("Error adding item to cart");
        }
    });
}


</script>
<script>
function viewCart() {
    window.location.href = "CartController";
}
</script>
</div>
</body>
</html>