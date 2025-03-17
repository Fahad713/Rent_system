<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign Up</title>
    <link rel="stylesheet" href="../index.css">
</head>
<body>
    <header class="header">
        <h1>PROPERTY RENT PLATFORM</h1>
        <nav class="nav">
            <ul>
                <li><a href="../index.html">HOME</a></li>
                <li><a href="SignIN.jsp">SIGN IN</a></li>
            </ul>
        </nav>
    </header>
    
    <main class="main">
        <section>
            <h2>Sign Up</h2>
            <form action="${pageContext.request.contextPath}/SignUP" method="post" class="signup-form">
                <label for="name">Full Name:</label>
                <input type="text" id="name" name="name" required>
                
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required>
                
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required>
                
                <label for="phone">Phone Number:</label>
                <input type="tel" id="phone" name="phone" required>
                
                <button type="submit">Sign Up</button>
            </form>
        </section>
    </main>
</body>
</html>
