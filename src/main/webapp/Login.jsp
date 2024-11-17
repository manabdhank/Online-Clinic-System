<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Login Page-Online Clinic System</title>
<link rel="stylesheet" href="style.css">
<script type="text/javascript" src="script.js"></script>
</head>
<body>
<h1>Online Clinic Management System</h1>
	<div class="login-box">
		<h2>Login</h2>
		<h5>${message}</h5>
		<form action="login" method="post">
			<div class="user-box">
				<input type="text" name="usrid" required=""> <label>UserID</label>
			</div>
			<div class="user-box">
				<input type="password" name="passwd" required=""> <label>Password</label>
			</div>
			<a href="SignUp.jsp">New Patient?</a><br>
			<input type="submit" name="Submit" value="Submit" />
		</form>
	</div>
</body>
</html>