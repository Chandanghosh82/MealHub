
package com.fooddelivery.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.fooddelivery.util.DBConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();

        Object userIdObject = session.getAttribute("userId");

        if (userIdObject == null) {

            response.sendRedirect("login.html");

            return;
        }

        int userId = (Integer) userIdObject;

        String foodIdParameter = request.getParameter("foodId");

        try {

            Connection con = DBConnection.getConnection();

            /*
             * ADD FOOD TO CART
             */

            if (foodIdParameter != null &&
                !foodIdParameter.trim().isEmpty()) {

                int foodId = Integer.parseInt(foodIdParameter);

                String checkSql =
                        "SELECT quantity FROM cart " +
                        "WHERE user_id = ? AND food_id = ?";

                PreparedStatement checkPs =
                        con.prepareStatement(checkSql);

                checkPs.setInt(1, userId);
                checkPs.setInt(2, foodId);

                ResultSet checkRs =
                        checkPs.executeQuery();

                if (checkRs.next()) {

                    int quantity =
                            checkRs.getInt("quantity");

                    String updateSql =
                            "UPDATE cart SET quantity = ? " +
                            "WHERE user_id = ? AND food_id = ?";

                    PreparedStatement updatePs =
                            con.prepareStatement(updateSql);

                    updatePs.setInt(1, quantity + 1);
                    updatePs.setInt(2, userId);
                    updatePs.setInt(3, foodId);

                    updatePs.executeUpdate();

                    updatePs.close();

                } else {

                    String insertSql =
                            "INSERT INTO cart " +
                            "(user_id, food_id, quantity) " +
                            "VALUES (?, ?, ?)";

                    PreparedStatement insertPs =
                            con.prepareStatement(insertSql);

                    insertPs.setInt(1, userId);
                    insertPs.setInt(2, foodId);
                    insertPs.setInt(3, 1);

                    insertPs.executeUpdate();

                    insertPs.close();
                }

                checkRs.close();
                checkPs.close();
            }

            /*
             * GET APPLIED COUPON
             */

            String appliedCoupon =
                    (String) session.getAttribute("appliedCoupon");

            /*
             * HTML
             */

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");

            out.println("<meta charset='UTF-8'>");

            out.println("<meta name='viewport' " +
                    "content='width=device-width, initial-scale=1.0'>");

            out.println("<title>MealHub - My Cart</title>");

            /*
             * CSS
             */

            out.println("<style>");

            out.println("*{" +
                    "margin:0;" +
                    "padding:0;" +
                    "box-sizing:border-box;" +
                    "}");

            out.println("body{" +
                    "font-family:Arial,sans-serif;" +
                    "background:#f7f7f7;" +
                    "color:#222;" +
                    "}");

            /* NAVBAR */

            out.println(".navbar{" +
                    "background:white;" +
                    "padding:18px 7%;" +
                    "display:flex;" +
                    "align-items:center;" +
                    "justify-content:space-between;" +
                    "box-shadow:0 2px 10px rgba(0,0,0,0.08);" +
                    "}");

            out.println(".left-nav{" +
                    "display:flex;" +
                    "align-items:center;" +
                    "gap:15px;" +
                    "}");

            out.println(".home-circle{" +
                    "width:42px;" +
                    "height:42px;" +
                    "border-radius:50%;" +
                    "background:#ff5200;" +
                    "color:white;" +
                    "display:flex;" +
                    "align-items:center;" +
                    "justify-content:center;" +
                    "text-decoration:none;" +
                    "font-size:20px;" +
                    "}");

            out.println(".home-circle:hover{" +
                    "background:#e64900;" +
                    "}");

            out.println(".logo{" +
                    "font-size:30px;" +
                    "font-weight:800;" +
                    "color:#ff5200;" +
                    "text-decoration:none;" +
                    "}");

            out.println(".logo span{" +
                    "color:#222;" +
                    "}");

            out.println(".nav-links{" +
                    "display:flex;" +
                    "gap:25px;" +
                    "align-items:center;" +
                    "}");

            out.println(".nav-links a{" +
                    "text-decoration:none;" +
                    "color:#333;" +
                    "font-weight:600;" +
                    "}");

            out.println(".nav-links a:hover{" +
                    "color:#ff5200;" +
                    "}");

            /* CONTAINER */

            out.println(".container{" +
                    "width:90%;" +
                    "max-width:1050px;" +
                    "margin:45px auto;" +
                    "}");

            out.println(".heading{" +
                    "text-align:center;" +
                    "margin-bottom:30px;" +
                    "}");

            out.println(".heading h1{" +
                    "font-size:34px;" +
                    "margin-bottom:8px;" +
                    "}");

            out.println(".heading p{" +
                    "color:#777;" +
                    "}");

            /* CART */

            out.println(".cart-box{" +
                    "background:white;" +
                    "border-radius:16px;" +
                    "padding:25px;" +
                    "box-shadow:0 5px 20px rgba(0,0,0,0.08);" +
                    "}");

            out.println("table{" +
                    "width:100%;" +
                    "border-collapse:collapse;" +
                    "}");

            out.println("th{" +
                    "background:#ff5200;" +
                    "color:white;" +
                    "padding:14px;" +
                    "text-align:center;" +
                    "}");

            out.println("td{" +
                    "padding:15px;" +
                    "text-align:center;" +
                    "border-bottom:1px solid #eee;" +
                    "}");

            out.println("tr:hover{" +
                    "background:#fff8f4;" +
                    "}");

            out.println(".food-name{" +
                    "font-weight:600;" +
                    "}");

            /* QUANTITY */

            out.println(".quantity-box{" +
                    "display:flex;" +
                    "align-items:center;" +
                    "justify-content:center;" +
                    "gap:8px;" +
                    "}");

            out.println(".quantity-btn{" +
                    "width:34px;" +
                    "height:34px;" +
                    "border:none;" +
                    "border-radius:7px;" +
                    "font-size:20px;" +
                    "font-weight:bold;" +
                    "cursor:pointer;" +
                    "display:flex;" +
                    "align-items:center;" +
                    "justify-content:center;" +
                    "text-decoration:none;" +
                    "}");

            out.println(".minus-btn{" +
                    "background:#dff5e3;" +
                    "color:#198754;" +
                    "}");

            out.println(".minus-btn:hover{" +
                    "background:#198754;" +
                    "color:white;" +
                    "}");

            out.println(".plus-btn{" +
                    "background:#198754;" +
                    "color:white;" +
                    "}");

            out.println(".plus-btn:hover{" +
                    "background:#157347;" +
                    "}");

            out.println(".quantity-number{" +
                    "min-width:35px;" +
                    "font-size:17px;" +
                    "font-weight:700;" +
                    "text-align:center;" +
                    "}");

            /* SUBTOTAL */

            out.println(".subtotal{" +
                    "font-weight:700;" +
                    "}");

            /* REMOVE */

            out.println(".remove-btn{" +
                    "color:#e53935;" +
                    "text-decoration:none;" +
                    "font-weight:600;" +
                    "}");

            out.println(".remove-btn:hover{" +
                    "text-decoration:underline;" +
                    "}");

            /* COUPON */

            out.println(".coupon-box{" +
                    "margin-top:25px;" +
                    "padding:20px;" +
                    "background:#fff8f2;" +
                    "border:1px solid #ffe0cc;" +
                    "border-radius:12px;" +
                    "}");

            out.println(".coupon-title{" +
                    "font-size:18px;" +
                    "font-weight:700;" +
                    "margin-bottom:12px;" +
                    "}");

            out.println(".coupon-form{" +
                    "display:flex;" +
                    "gap:10px;" +
                    "}");

            out.println(".coupon-input{" +
                    "flex:1;" +
                    "padding:12px;" +
                    "border:1px solid #ddd;" +
                    "border-radius:8px;" +
                    "font-size:14px;" +
                    "outline:none;" +
                    "}");

            out.println(".coupon-input:focus{" +
                    "border-color:#ff5200;" +
                    "}");

            out.println(".coupon-btn{" +
                    "background:#ff5200;" +
                    "color:white;" +
                    "border:none;" +
                    "padding:12px 20px;" +
                    "border-radius:8px;" +
                    "font-weight:700;" +
                    "cursor:pointer;" +
                    "}");

            out.println(".coupon-btn:hover{" +
                    "background:#e64900;" +
                    "}");

            out.println(".coupon-success{" +
                    "margin-top:10px;" +
                    "color:#198754;" +
                    "font-weight:600;" +
                    "font-size:14px;" +
                    "}");

            out.println(".coupon-error{" +
                    "margin-top:10px;" +
                    "color:#e53935;" +
                    "font-weight:600;" +
                    "font-size:14px;" +
                    "}");

            /* SUMMARY */

            out.println(".summary-box{" +
                    "margin-top:25px;" +
                    "padding:20px;" +
                    "background:#f8f9fa;" +
                    "border-radius:12px;" +
                    "}");

            out.println(".summary-row{" +
                    "display:flex;" +
                    "justify-content:space-between;" +
                    "padding:8px 0;" +
                    "font-size:15px;" +
                    "}");

            out.println(".discount{" +
                    "color:#198754;" +
                    "font-weight:700;" +
                    "}");

            out.println(".final-total{" +
                    "display:flex;" +
                    "justify-content:space-between;" +
                    "border-top:1px solid #ddd;" +
                    "margin-top:8px;" +
                    "padding-top:15px;" +
                    "font-size:23px;" +
                    "font-weight:800;" +
                    "color:#198754;" +
                    "}");

            /* TOTAL */

            out.println(".total-box{" +
                    "display:flex;" +
                    "justify-content:flex-end;" +
                    "align-items:center;" +
                    "gap:25px;" +
                    "margin-top:25px;" +
                    "}");

            out.println(".total{" +
                    "font-size:22px;" +
                    "font-weight:800;" +
                    "}");

            /* ORDER BUTTON */

            out.println(".proceed-btn{" +
                    "display:inline-block;" +
                    "background:#198754;" +
                    "color:white;" +
                    "padding:13px 24px;" +
                    "border-radius:9px;" +
                    "text-decoration:none;" +
                    "font-weight:700;" +
                    "font-size:16px;" +
                    "}");

            out.println(".proceed-btn:hover{" +
                    "background:#157347;" +
                    "color:white;" +
                    "}");

            /* CONTINUE */

            out.println(".continue-btn{" +
                    "display:inline-block;" +
                    "margin-top:20px;" +
                    "color:#ff5200;" +
                    "text-decoration:none;" +
                    "font-weight:600;" +
                    "}");

            /* EMPTY */

            out.println(".empty{" +
                    "text-align:center;" +
                    "padding:50px;" +
                    "}");

            out.println(".empty-icon{" +
                    "font-size:70px;" +
                    "margin-bottom:15px;" +
                    "}");

            out.println(".empty h2{" +
                    "margin-bottom:10px;" +
                    "}");

            out.println(".empty p{" +
                    "color:#777;" +
                    "margin-bottom:20px;" +
                    "}");

            out.println(".shop-btn{" +
                    "display:inline-block;" +
                    "background:#ff5200;" +
                    "color:white;" +
                    "padding:12px 22px;" +
                    "border-radius:8px;" +
                    "text-decoration:none;" +
                    "font-weight:600;" +
                    "}");

            /* MOBILE */

            out.println("@media(max-width:600px){");

            out.println(".navbar{" +
                    "padding:15px 5%;" +
                    "}");

            out.println(".logo{" +
                    "font-size:23px;" +
                    "}");

            out.println(".nav-links{" +
                    "display:none;" +
                    "}");

            out.println(".container{" +
                    "width:95%;" +
                    "}");

            out.println("table{" +
                    "font-size:12px;" +
                    "}");

            out.println("th,td{" +
                    "padding:8px;" +
                    "}");

            out.println(".quantity-btn{" +
                    "width:30px;" +
                    "height:30px;" +
                    "}");

            out.println(".total-box{" +
                    "flex-direction:column;" +
                    "align-items:stretch;" +
                    "}");

            out.println(".proceed-btn{" +
                    "text-align:center;" +
                    "}");

            out.println(".coupon-form{" +
                    "flex-direction:column;" +
                    "}");

            out.println(".coupon-btn{" +
                    "width:100%;" +
                    "}");

            out.println("}");

            out.println("</style>");

            out.println("</head>");

            out.println("<body>");

            /*
             * NAVBAR
             */

            out.println("<nav class='navbar'>");

            out.println("<div class='left-nav'>");

            out.println("<a href='index.html' " +
                    "class='home-circle'>🏠</a>");

            out.println("<a href='index.html' " +
                    "class='logo'>Meal<span>Hub</span></a>");

            out.println("</div>");

            out.println("<div class='nav-links'>");

            out.println("<a href='foods'>Food</a>");

            out.println("<a href='myOrders'>My Orders</a>");

            out.println("<a href='index.html'>Home</a>");

            out.println("</div>");

            out.println("</nav>");

            /*
             * HEADING
             */

            out.println("<div class='container'>");

            out.println("<div class='heading'>");

            out.println("<h1>🛒 My Cart</h1>");

            out.println("<p>Review your items before placing your order.</p>");

            out.println("</div>");

            /*
             * CART BOX
             */

            out.println("<div class='cart-box'>");

            String sql =
                    "SELECT c.food_id, " +
                    "c.quantity, " +
                    "f.food_name, " +
                    "f.price " +
                    "FROM cart c " +
                    "JOIN food_items f " +
                    "ON c.food_id = f.food_id " +
                    "WHERE c.user_id = ? " +
                    "ORDER BY c.cart_id";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setInt(1, userId);

            ResultSet rs =
                    ps.executeQuery();

            boolean found = false;

            double total = 0;

            boolean hasBurger = false;

            boolean hasBiryani = false;

            /*
             * TABLE
             */

            out.println("<table>");

            out.println("<tr>");

            out.println("<th>Food</th>");

            out.println("<th>Price</th>");

            out.println("<th>Quantity</th>");

            out.println("<th>Subtotal</th>");

            out.println("<th>Action</th>");

            out.println("</tr>");

            while (rs.next()) {

                found = true;

                int foodId =
                        rs.getInt("food_id");

                String foodName =
                        rs.getString("food_name");

                double price =
                        rs.getDouble("price");

                int quantity =
                        rs.getInt("quantity");

                double subtotal =
                        price * quantity;

                total = total + subtotal;

                String lowerFoodName =
                        foodName.toLowerCase();

                if (lowerFoodName.contains("burger")) {
                    hasBurger = true;
                }

                if (lowerFoodName.contains("biryani")) {
                    hasBiryani = true;
                }

                out.println("<tr>");

                out.println("<td class='food-name'>"
                        + foodName
                        + "</td>");

                out.println("<td>₹"
                        + String.format("%.2f", price)
                        + "</td>");

                out.println("<td>");

                out.println("<div class='quantity-box'>");

                out.println("<a href='updateCart?foodId="
                        + foodId
                        + "&action=decrease' "
                        + "class='quantity-btn minus-btn'>"
                        + "−"
                        + "</a>");

                out.println("<span class='quantity-number'>"
                        + quantity
                        + "</span>");

                out.println("<a href='updateCart?foodId="
                        + foodId
                        + "&action=increase' "
                        + "class='quantity-btn plus-btn'>"
                        + "+"
                        + "</a>");

                out.println("</div>");

                out.println("</td>");

                out.println("<td class='subtotal'>₹"
                        + String.format("%.2f", subtotal)
                        + "</td>");

                out.println("<td>");

                out.println("<a href='removeCart?foodId="
                        + foodId
                        + "' "
                        + "class='remove-btn'>"
                        + "Remove"
                        + "</a>");

                out.println("</td>");

                out.println("</tr>");
            }

            out.println("</table>");

            /*
             * EMPTY CART
             */

            if (!found) {

                session.removeAttribute("appliedCoupon");

                out.println("<div class='empty'>");

                out.println("<div class='empty-icon'>🛒</div>");

                out.println("<h2>Your cart is empty</h2>");

                out.println("<p>"
                        + "Add some delicious food to your cart."
                        + "</p>");

                out.println("<a href='foods' "
                        + "class='shop-btn'>"
                        + "Explore Food"
                        + "</a>");

                out.println("</div>");

            } else {

                /*
                 * COUPON BOX
                 */

                out.println("<div class='coupon-box'>");

                out.println("<div class='coupon-title'>🎁 Apply Offer / Coupon</div>");

                out.println("<form class='coupon-form' method='post' action='cart'>");

                out.println("<input class='coupon-input' "
                        + "type='text' "
                        + "name='coupon' "
                        + "placeholder='Enter coupon code' "
                        + "value='" +
                        (appliedCoupon == null ? "" : appliedCoupon) +
                        "'>");

                out.println("<button class='coupon-btn' type='submit'>"
                        + "Apply Coupon"
                        + "</button>");

                out.println("</form>");

                if (appliedCoupon != null) {

                    out.println("<div class='coupon-success'>"
                            + "✓ Coupon " + appliedCoupon + " applied."
                            + "</div>");

                } else {

                    out.println("<div style='margin-top:10px;color:#777;font-size:13px;'>"
                            + "Try FIRST50, SAVE100, BURGER30, BIRYANI20 or NEW50"
                            + "</div>");
                }

                out.println("</div>");

                /*
                 * CALCULATE DISPLAY DISCOUNT
                 */

                double discount = 0;

                String couponMessage = "";

                if (appliedCoupon != null) {

                    if ("FIRST50".equals(appliedCoupon)) {

                        String orderCheckSql =
                                "SELECT COUNT(*) FROM orders WHERE user_id = ?";

                        PreparedStatement orderCheckPs =
                                con.prepareStatement(orderCheckSql);

                        orderCheckPs.setInt(1, userId);

                        ResultSet orderCheckRs =
                                orderCheckPs.executeQuery();

                        int orderCount = 0;

                        if (orderCheckRs.next()) {
                            orderCount =
                                    orderCheckRs.getInt(1);
                        }

                        orderCheckRs.close();
                        orderCheckPs.close();

                        if (orderCount == 0) {

                            discount = total * 0.50;

                        } else {

                            session.removeAttribute("appliedCoupon");

                            appliedCoupon = null;

                            couponMessage =
                                    "FIRST50 is only valid for your first order.";
                        }

                    } else if ("SAVE100".equals(appliedCoupon)) {

                        if (total >= 499) {

                            discount = 100;

                        } else {

                            session.removeAttribute("appliedCoupon");

                            appliedCoupon = null;

                            couponMessage =
                                    "SAVE100 requires a minimum cart value of ₹499.";
                        }

                    } else if ("BURGER30".equals(appliedCoupon)) {

                        if (hasBurger) {

                            double burgerTotal = 0;

                            String burgerSql =
                                    "SELECT c.quantity, f.price, f.food_name " +
                                    "FROM cart c " +
                                    "JOIN food_items f ON c.food_id = f.food_id " +
                                    "WHERE c.user_id = ?";

                            PreparedStatement burgerPs =
                                    con.prepareStatement(burgerSql);

                            burgerPs.setInt(1, userId);

                            ResultSet burgerRs =
                                    burgerPs.executeQuery();

                            while (burgerRs.next()) {

                                String name =
                                        burgerRs.getString("food_name");

                                if (name.toLowerCase().contains("burger")) {

                                    burgerTotal +=
                                            burgerRs.getDouble("price") *
                                            burgerRs.getInt("quantity");
                                }
                            }

                            burgerRs.close();
                            burgerPs.close();

                            discount =
                                    burgerTotal * 0.30;

                        } else {

                            session.removeAttribute("appliedCoupon");

                            appliedCoupon = null;

                            couponMessage =
                                    "BURGER30 is valid only when a burger is in your cart.";
                        }

                    } else if ("BIRYANI20".equals(appliedCoupon)) {

                        if (hasBiryani) {

                            double biryaniTotal = 0;

                            String biryaniSql =
                                    "SELECT c.quantity, f.price, f.food_name " +
                                    "FROM cart c " +
                                    "JOIN food_items f ON c.food_id = f.food_id " +
                                    "WHERE c.user_id = ?";

                            PreparedStatement biryaniPs =
                                    con.prepareStatement(biryaniSql);

                            biryaniPs.setInt(1, userId);

                            ResultSet biryaniRs =
                                    biryaniPs.executeQuery();

                            while (biryaniRs.next()) {

                                String name =
                                        biryaniRs.getString("food_name");

                                if (name.toLowerCase().contains("biryani")) {

                                    biryaniTotal +=
                                            biryaniRs.getDouble("price") *
                                            biryaniRs.getInt("quantity");
                                }
                            }

                            biryaniRs.close();
                            biryaniPs.close();

                            discount =
                                    biryaniTotal * 0.20;

                        } else {

                            session.removeAttribute("appliedCoupon");

                            appliedCoupon = null;

                            couponMessage =
                                    "BIRYANI20 is valid only when biryani is in your cart.";
                        }

                    } else if ("NEW50".equals(appliedCoupon)) {

                        if (total >= 199) {

                            discount = 50;

                        } else {

                            session.removeAttribute("appliedCoupon");

                            appliedCoupon = null;

                            couponMessage =
                                    "NEW50 requires a minimum cart value of ₹199.";
                        }

                    } else if ("LIMITED".equals(appliedCoupon)) {

                        if (total >= 299) {

                            discount = 0;

                            couponMessage =
                                    "✓ Free delivery applied. Delivery is already FREE.";

                        } else {

                            session.removeAttribute("appliedCoupon");

                            appliedCoupon = null;

                            couponMessage =
                                    "LIMITED requires a minimum cart value of ₹299.";
                        }

                    } else {

                        session.removeAttribute("appliedCoupon");

                        appliedCoupon = null;

                        couponMessage =
                                "Invalid coupon code.";
                    }
                }

                if (discount > total) {
                    discount = total;
                }

                double finalTotal =
                        total - discount;

                /*
                 * SUMMARY
                 */

                out.println("<div class='summary-box'>");

                out.println("<div class='summary-row'>");

                out.println("<span>Cart Total</span>");

                out.println("<span>₹"
                        + String.format("%.2f", total)
                        + "</span>");

                out.println("</div>");

                out.println("<div class='summary-row'>");

                out.println("<span>Discount</span>");

                out.println("<span class='discount'>- ₹"
                        + String.format("%.2f", discount)
                        + "</span>");

                out.println("</div>");

                if (!couponMessage.isEmpty()) {

                    String messageClass =
                            couponMessage.startsWith("✓")
                            ? "coupon-success"
                            : "coupon-error";

                    out.println("<div class='"
                            + messageClass
                            + "'>"
                            + couponMessage
                            + "</div>");
                }

                out.println("<div class='final-total'>");

                out.println("<span>Final Total</span>");

                out.println("<span>₹"
                        + String.format("%.2f", finalTotal)
                        + "</span>");

                out.println("</div>");

                out.println("</div>");

                /*
                 * TOTAL / PAYMENT
                 */

                out.println("<div class='total-box'>");

                out.println("<div class='total'>");

                out.println("Pay: ₹"
                        + String.format("%.2f", finalTotal));

                out.println("</div>");

                out.println("<a href='placeOrder' "
                        + "class='proceed-btn'>"
                        + "Proceed to Pay"
                        + "</a>");

                out.println("</div>");
            }

            /*
             * CONTINUE SHOPPING
             */

            out.println("<a href='foods' "
                    + "class='continue-btn'>"
                    + "← Continue Shopping"
                    + "</a>");

            out.println("</div>");

            out.println("</div>");

            out.println("</body>");

            out.println("</html>");

            rs.close();

            ps.close();

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

            out.println("<h3>Unable to load cart</h3>");

            out.println("<p>Error: "
                    + e.getMessage()
                    + "</p>");
        }
    }

    /*
     * APPLY COUPON
     */

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null ||
            session.getAttribute("userId") == null) {

            response.sendRedirect("login.html");

            return;
        }

        String coupon =
                request.getParameter("coupon");

        if (coupon != null) {

            coupon =
                    coupon.trim().toUpperCase();

            if (coupon.isEmpty()) {

                session.removeAttribute("appliedCoupon");

            } else {

                session.setAttribute(
                        "appliedCoupon",
                        coupon
                );
            }
        }

        response.sendRedirect("cart");
    }
}
