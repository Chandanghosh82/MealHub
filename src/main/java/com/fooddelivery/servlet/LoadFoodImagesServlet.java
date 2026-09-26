
package com.fooddelivery.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.fooddelivery.util.DBConnection;
import com.fooddelivery.util.PexelsConfig;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/loadFoodImages")
public class LoadFoodImagesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        try {

            Connection con = DBConnection.getConnection();

            String selectSql =
                    "SELECT food_id, food_name "
                    + "FROM food_items "
                    + "WHERE image_url IS NULL "
                    + "OR image_url = ''";

            PreparedStatement selectPs =
                    con.prepareStatement(selectSql);

            ResultSet rs =
                    selectPs.executeQuery();

            int count = 0;

            while (rs.next()) {

                int foodId =
                        rs.getInt("food_id");

                String foodName =
                        rs.getString("food_name");

                String imageUrl =
                        getPexelsImage(foodName);

                if (imageUrl != null) {

                    String updateSql =
                            "UPDATE food_items "
                            + "SET image_url = ? "
                            + "WHERE food_id = ?";

                    PreparedStatement updatePs =
                            con.prepareStatement(updateSql);

                    updatePs.setString(
                            1,
                            imageUrl);

                    updatePs.setInt(
                            2,
                            foodId);

                    updatePs.executeUpdate();

                    updatePs.close();

                    count++;

                    out.println(
                            "<p>Loaded: "
                            + foodName
                            + "</p>");
                }
                else {

                    out.println(
                            "<p>Not found: "
                            + foodName
                            + "</p>");
                }
            }

            rs.close();
            selectPs.close();
            con.close();

            out.println("<h2>Finished</h2>");

            out.println(
                    "<p>Images loaded: "
                    + count
                    + "</p>");

        }
        catch (Exception e) {

            e.printStackTrace();

            out.println(
                    "<h3>Error loading images</h3>");

            out.println(
                    "<p>"
                    + e.getMessage()
                    + "</p>");
        }
    }


    private String getPexelsImage(String foodName) {

        String searchQuery =
                foodName;


        if (foodName.equalsIgnoreCase(
                "Chicken Kebab")) {

            searchQuery =
                    "chicken kebab Indian food";
        }


        else if (foodName.equalsIgnoreCase(
                "Paneer Chilli")) {

            searchQuery =
                    "chilli paneer Indian food";
        }


        else if (foodName.equalsIgnoreCase(
                "Gajar Halwa")) {

            searchQuery =
                    "gajar ka halwa Indian dessert";
        }


        else if (foodName.equalsIgnoreCase(
                "Mango Lassi")) {

            searchQuery =
                    "mango lassi Indian drink";
        }


        try {

            String query =
                    URLEncoder.encode(
                            searchQuery,
                            "UTF-8");


            URL url =
                    new URL(
                            "https://api.pexels.com/v1/search"
                            + "?query="
                            + query
                            + "&per_page=5");


            HttpURLConnection connection =
                    (HttpURLConnection)
                    url.openConnection();


            connection.setRequestMethod(
                    "GET");


            connection.setRequestProperty(
                    "Authorization",
                    PexelsConfig.API_KEY);


            connection.setConnectTimeout(
                    5000);


            connection.setReadTimeout(
                    5000);


            int responseCode =
                    connection.getResponseCode();


            if (responseCode == 200) {

                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        connection
                                        .getInputStream()));


                StringBuilder json =
                        new StringBuilder();


                String line;


                while ((line =
                        reader.readLine()) != null) {

                    json.append(line);
                }


                reader.close();


                String data =
                        json.toString();


                int start =
                        data.indexOf(
                                "\"large\":\"");


                if (start != -1) {

                    start += 9;


                    int end =
                            data.indexOf(
                                    "\"",
                                    start);


                    if (end != -1) {

                        String imageUrl =
                                data.substring(
                                        start,
                                        end);


                        if (!imageUrl.isEmpty()) {

                            connection.disconnect();

                            return imageUrl;
                        }
                    }
                }
            }


            connection.disconnect();

        }
        catch (Exception e) {

            System.out.println(
                    "Failed: "
                    + foodName);
        }


        return null;
    }
}
