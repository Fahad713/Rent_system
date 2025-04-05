<%@ page import="java.util.List, java.util.Map, java.util.Set" %>
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
            <a href="Requirements">My Requirements</a>
        </div>

        <!-- Content Section -->
        <div class="container">
            <h2>Available Listings</h2>

            <%
                List<Map<String, String>> propertyList = (List<Map<String, String>>) request.getAttribute("propertyList");
                if (propertyList != null && !propertyList.isEmpty()) {
                    // Get the keys from the first property map to create dynamic headers
                    Set<String> columnNames = propertyList.get(0).keySet();
            %>

            <!-- Dynamic Table -->
            <table>
                <tr>
                    <% for (String columnName : columnNames) { %>
                        <th><%= columnName.replace("_", " ") %></th>
                    <% } %>
                </tr>

                <% for (Map<String, String> property : propertyList) { %>
                <tr>
                    <% for (String columnName : columnNames) { %>
                        <td><%= property.get(columnName) %></td>
                    <% } %>
                </tr>
                <% } %>
            </table>

            <%
                } else {
            %>
            <p style="text-align:center;">No properties available</p>
            <%
                }
            %>

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
