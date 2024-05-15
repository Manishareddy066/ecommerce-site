<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registration Form</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-image: linear-gradient(to right, #ffbb33, #ff6f91);
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .form-container {
            background-color: #ffffff;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            padding: 20px;
            max-width: 400px;
            width: 100%;
            box-sizing: border-box;
        }

        .form-container input[type="text"],
        .form-container input[type="password"],
        .form-container button[type="submit"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 5px;
            box-sizing: border-box;
        }

        .form-container button[type="submit"] {
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
        }

        .form-container button[type="submit"]:hover {
            background-color: #45a049;
        }
    </style>
</head>
<body>
    <%@ page import="java.sql.Connection, java.sql.DriverManager, java.sql.PreparedStatement, java.sql.SQLException" %>
    <%
        // Database connection parameters
        final String DB_URL = "jdbc:postgresql://192.168.110.48:5432/plf_training_admin";
        final String DB_USER = "plf_training";
        final String DB_PASSWORD = "pff123";
        
        try {
            // Load the PostgreSQL JDBC driver
            Class.forName("org.postgresql.Driver");
            
            // Establish database connection
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            // Handling form submission
            if (request.getMethod().equalsIgnoreCase("post")) {
                String name = request.getParameter("name");
                String mobile = request.getParameter("mobile");
                String location = request.getParameter("location");
                String password = request.getParameter("password");
                
                // Inserting data into database
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO cust_det_05 (name, mobile, location, password) VALUES (?, ?, ?, ?)");
                stmt.setString(1, name);
                stmt.setString(2, mobile);
                stmt.setString(3, location);
                stmt.setString(4, password);
                int rowsAffected = stmt.executeUpdate();
                
                if (rowsAffected > 0) {
                    response.sendRedirect("index.jsp");
                } else {
                    out.println("<p>Error occurred while registering!</p>");
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            out.println("<p>Error occurred while connecting to database!</p>");
        }
    %>

    <div class="form-container">
        <form id="regd-form" action="" method="post">
            Name:<input type="text" id="name" name="name" placeholder="Name" required><br>
            Mobile:<input type="text" id="mobile" name="mobile" placeholder="Mobile" required><br>
            Location:<input type="text" id="location" name="location" placeholder="Location" required><br>
            Password:<input type="password" id="password" name="password" placeholder="Password" required><br>
            Confirm Password:<input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm Password" required><br>
            <button type="submit">Register</button>
        </form>
    </div>
</body>
</html>