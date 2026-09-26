
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

@WebServlet("/myOrders")
public class MyOrdersServlet extends HttpServlet {

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

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT order_id, order_date, total_amount, status "
                    + "FROM orders "
                    + "WHERE user_id = ? "
                    + "ORDER BY order_id DESC";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            out.println("<html>");

            out.println("<head>");

            out.println("<title>My Orders</title>");

            out.println("<style>");

            out.println("body {");
            out.println("font-family: Arial, sans-serif;");
            out.println("background: #f4f6f8;");
            out.println("margin: 0;");
            out.println("padding: 40px;");
            out.println("}");

            out.println(".container {");
            out.println("max-width: 1150px;");
            out.println("margin: auto;");
            out.println("}");

            out.println("h1 {");
            out.println("text-align: center;");
            out.println("color: #222;");
            out.println("margin-bottom: 30px;");
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

            out.println(".status {");
            out.println("font-weight: bold;");
            out.println("color: #007bff;");
            out.println("}");

            out.println(".details {");
            out.println("display: inline-block;");
            out.println("background: #007bff;");
            out.println("color: white;");
            out.println("padding: 9px 18px;");
            out.println("text-decoration: none;");
            out.println("border-radius: 6px;");
            out.println("font-size: 14px;");
            out.println("font-weight: 600;");
            out.println("transition: 0.3s;");
            out.println("margin-right: 6px;");
            out.println("}");

            out.println(".details:hover {");
            out.println("background: #0056b3;");
            out.println("transform: translateY(-1px);");
            out.println("}");

            out.println(".cancel {");
            out.println("display: inline-block;");
            out.println("background: #dc3545;");
            out.println("color: white;");
            out.println("padding: 9px 18px;");
            out.println("text-decoration: none;");
            out.println("border-radius: 6px;");
            out.println("font-size: 14px;");
            out.println("font-weight: 600;");
            out.println("transition: 0.3s;");
            out.println("}");

            out.println(".cancel:hover {");
            out.println("background: #b02a37;");
            out.println("transform: translateY(-1px);");
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
            out.println("}");

            out.println(".back:hover {");
            out.println("background: #111;");
            out.println("}");

            out.println("</style>");

            out.println("</head>");

            out.println("<body>");

            out.println("<div class='container'>");

            out.println("<h1>My Orders</h1>");

            out.println("<table>");

            out.println("<tr>");

            out.println("<th>Order ID</th>");
            out.println("<th>Order Date</th>");
            out.println("<th>Total Amount</th>");
            out.println("<th>Status</th>");
            out.println("<th>Details</th>");
            out.println("<th>Action</th>");

            out.println("</tr>");

            boolean found = false;

            while (rs.next()) {

                found = true;

                int orderId = rs.getInt("order_id");

                String orderDate = rs.getString("order_date");

                String totalAmount = rs.getString("total_amount");

                String status = rs.getString("status");

                out.println("<tr>");

                out.println("<td>" + orderId + "</td>");

                out.println("<td>" + orderDate + "</td>");

                out.println("<td>₹" + totalAmount + "</td>");

                out.println("<td class='status'>" + status + "</td>");

                out.println("<td>");

                out.println("<a href='orderDetails?orderId="
                        + orderId
                        + "' class='details'>");

                out.println("View Details");

                out.println("</a>");

                out.println("</td>");

                out.println("<td>");

                if ("PLACED".equals(status) || "CONFIRMED".equals(status)) {

                    out.println("<a href='cancelOrder?orderId="
                            + orderId
                            + "' class='cancel' "
                            + "onclick=\"return confirm('Are you sure you want to cancel this order?');\">");

                    out.println("Cancel Order");

                    out.println("</a>");

                } else {

                    out.println("—");
                }

                out.println("</td>");

                out.println("</tr>");
            }

            if (!found) {

                out.println("<tr>");

                out.println("<td colspan='6'>");

                out.println("You have not placed any orders yet.");

                out.println("</td>");

                out.println("</tr>");
            }

            out.println("</table>");

            out.println("<a href='index.html' class='back'>← Back to Home</a>");

            out.println("</div>");

            out.println("</body>");

            out.println("</html>");

            rs.close();

            ps.close();

            con.close();

        } catch (Exception e) {

            out.println("<h2>Error while loading orders</h2>");

            out.println("<p>" + e.getMessage() + "</p>");

            out.println("<a href='index.html'>Back to Home</a>");

            e.printStackTrace();
        }
    }
}


