<%@ page import="java.util.List, java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Rent Property</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/Views/CSS/Dashboard.css">
    </head>
    <body>

        <!-- Header -->
        <div class="header">
            <h1>RENT PROPERTY</h1>
            <button class="logout-btn" onclick="logout()">Logout</button>
        </div>

        <!-- Navigation Bar -->
        <div class="nav-bar">
            <a href="Properties">Properties</a>
            <a href="RequirementsServlet">My Requirements</a>
        </div>

        <!-- Content Section -->
        <div class="container">
            <h2>Available Listings</h2>

            <!-- Dynamic Table -->
            <table>
                <tr>
                    <th>Property Name</th>
                    <th>Location</th>
                    <th>Price</th>
                    <th>Type</th>
                </tr>
                <%
                    List<Map<String, String>> propertyList = (List<Map<String, String>>) request.getAttribute("propertyList");
                    if (propertyList != null) {
                        for (Map<String, String> property : propertyList) {
                %>
                <tr>
                    <td><%= property.get("name")%></td>
                    <td><%= property.get("location")%></td>
                    <td><%= property.get("price")%></td>
                    <td><%= property.get("type")%></td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="4" style="text-align:center;">No properties available</td>
                </tr>
                <% }%>
            </table>

            <!-- Buttons -->
            <div class="buttons">
                <button class="add-property" onclick="location.href = 'addProperty.jsp'">Add Property</button>
                <button class="add-requirement" onclick="location.href = 'addRequirement.jsp'">Add Requirements</button>
            </div>
        </div>

        <!-- JavaScript for Logout -->
        <script>
            function logout() {
                window.location.href = 'logout.jsp';
            }
        </script>

    </body>
</html>
