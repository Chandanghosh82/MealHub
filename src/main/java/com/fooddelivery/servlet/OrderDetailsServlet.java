
package com.fooddelivery.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.fooddelivery.util.DBConnection;

@WebServlet("/orderDetails")
public class OrderDetailsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {

            response.sendRedirect("login.html");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        String orderIdText = request.getParameter("orderId");

        if (orderIdText == null) {

            out.println("<h2>Order ID is required</h2>");
            out.println("<a href='myOrders'>Back to My Orders</a>");
            return;
        }

        try {

            int orderId = Integer.parseInt(orderIdText);

            Connection con = DBConnection.getConnection();

            String orderSql = "SELECT o.order_id, o.order_date, "
                    + "o.total_amount, o.status, u.name "
                    + "FROM orders o "
                    + "LEFT JOIN users u ON o.user_id = u.user_id "
                    + "WHERE o.order_id = ? AND o.user_id = ?";

            PreparedStatement orderPs = con.prepareStatement(orderSql);

            orderPs.setInt(1, orderId);
            orderPs.setInt(2, userId);

            ResultSet orderRs = orderPs.executeQuery();

            if (!orderRs.next()) {

                out.println("<h2>Order not found</h2>");
                out.println("<a href='myOrders'>Back to My Orders</a>");

                orderRs.close();
                orderPs.close();
                con.close();

                return;
            }

            String customerName = orderRs.getString("name");

            String orderDate = orderRs.getString("order_date");

            String totalAmount = orderRs.getString("total_amount");

            String status = orderRs.getString("status");

            String itemSql = "SELECT f.food_name, oi.quantity, "
                    + "oi.price, (oi.quantity * oi.price) AS subtotal "
                    + "FROM order_items oi "
                    + "JOIN food_items f ON oi.food_id = f.food_id "
                    + "WHERE oi.order_id = ?";

            PreparedStatement itemPs = con.prepareStatement(itemSql);

            itemPs.setInt(1, orderId);

            ResultSet itemRs = itemPs.executeQuery();

            out.println("<html>");

            out.println("<head>");

            out.println("<title>Order Details</title>");

            out.println("<style>");

            out.println("body {");
            out.println("font-family: Arial, sans-serif;");
            out.println("background: #f4f6f8;");
            out.println("margin: 0;");
            out.println("padding: 40px;");
            out.println("}");

            out.println(".container {");
            out.println("max-width: 950px;");
            out.println("margin: auto;");
            out.println("}");

            out.println("h1 {");
            out.println("text-align: center;");
            out.println("color: #222;");
            out.println("margin-bottom: 30px;");
            out.println("}");

            out.println(".info {");
            out.println("background: white;");
            out.println("padding: 25px;");
            out.println("margin-bottom: 25px;");
            out.println("border-radius: 10px;");
            out.println("box-shadow: 0 4px 15px rgba(0,0,0,0.08);");
            out.println("}");

            out.println(".info p {");
            out.println("font-size: 16px;");
            out.println("margin: 12px 0;");
            out.println("color: #444;");
            out.println("}");

            out.println(".status {");
            out.println("font-weight: bold;");
            out.println("color: #007bff;");
            out.println("}");

            out.println("table {");
            out.println("width: 100%;");
            out.println("border-collapse: collapse;");
            out.println("background: white;");
            out.println("border-radius: 10px;");
            out.println("overflow: hidden;");
            out.println("box-shadow: 0 4px 15px rgba(0,0,0,0.08);");
            out.println("}");

            out.println("th, td {");
            out.println("padding: 15px;");
            out.println("border-bottom: 1px solid #eee;");
            out.println("text-align: center;");
            out.println("}");

            out.println("th {");
            out.println("background: #222;");
            out.println("color: white;");
            out.println("font-size: 15px;");
            out.println("}");

            out.println("td {");
            out.println("color: #444;");
            out.println("}");

            out.println(".total {");
            out.println("background: white;");
            out.println("padding: 20px;");
            out.println("margin-top: 20px;");
            out.println("border-radius: 10px;");
            out.println("text-align: right;");
            out.println("font-size: 20px;");
            out.println("font-weight: bold;");
            out.println("box-shadow: 0 4px 15px rgba(0,0,0,0.08);");
            out.println("}");

            out.println(".back {");
            out.println("display: inline-block;");
            out.println("margin-top: 25px;");
            out.println("padding: 10px 18px;");
            out.println("background: #333;");
            out.println("color: white;");
            out.println("text-decoration: none;");
            out.println("border-radius: 6px;");
            out.println("font-weight: 600;");
            out.println("transition: 0.3s;");
            out.println("}");

            out.println(".back:hover {");
            out.println("background: #111;");
            out.println("transform: translateY(-1px);");
            out.println("}");

            out.println("</style>");

            out.println("</head>");

            out.println("<body>");

            out.println("<div class='container'>");

            out.println("<h1>Order Details</h1>");

            out.println("<div class='info'>");

            out.println("<p><strong>Customer Name:</strong> "
                    + customerName + "</p>");

            out.println("<p><strong>Order ID:</strong> "
                    + orderId + "</p>");

            out.println("<p><strong>Order Date:</strong> "
                    + orderDate + "</p>");

            out.println("<p><strong>Status:</strong> "
                    + "<span class='status'>" + status + "</span></p>");

            out.println("</div>");

            out.println("<table>");

            out.println("<tr>");

            out.println("<th>Food Item</th>");
            out.println("<th>Quantity</th>");
            out.println("<th>Price</th>");
            out.println("<th>Subtotal</th>");

            out.println("</tr>");

            boolean foundItems = false;

            while (itemRs.next()) {

                foundItems = true;

                String foodName = itemRs.getString("food_name");

                int quantity = itemRs.getInt("quantity");

                String price = itemRs.getString("price");

                String subtotal = itemRs.getString("subtotal");

                out.println("<tr>");

                out.println("<td>" + foodName + "</td>");

                out.println("<td>" + quantity + "</td>");

                out.println("<td>₹" + price + "</td>");

                out.println("<td>₹" + subtotal + "</td>");

                out.println("</tr>");
            }

            if (!foundItems) {

                out.println("<tr>");

                out.println("<td colspan='4'>No items found</td>");

                out.println("</tr>");
            }

            out.println("</table>");

            out.println("<div class='total'>");

            out.println("Total Amount: ₹" + totalAmount);

            out.println("</div>");

            out.println("<a href='myOrders' class='back'>← Back to My Orders</a>");

            out.println("</div>");

            out.println("</body>");

            out.println("</html>");

            itemRs.close();

            itemPs.close();

            orderRs.close();

            orderPs.close();

            con.close();

        } catch (NumberFormatException e) {

            out.println("<h2>Invalid Order ID</h2>");

            out.println("<a href='myOrders'>Back to My Orders</a>");

        } catch (Exception e) {

            out.println("<h2>Error while loading order details</h2>");

            out.println("<p>" + e.getMessage() + "</p>");

            out.println("<a href='myOrders'>Back to My Orders</a>");

            e.printStackTrace();
        }
    }
}
