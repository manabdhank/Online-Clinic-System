<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" href="style.css">
<style>
#doc {
	margin: auto;
	margin-top: 19%;
	font-family: Arial, Helvetica, sans-serif;
	border-collapse: collapse;
	width: 70%;
}

#doc td, #doc th {
	border: 1px solid #ddd;
	padding: 8px;
}

#doc th {
	padding-top: 9px;
	padding-bottom: 9px;
	text-align: left;
	background-color: grey;
	color: white;
}
</style>
<title>Doctor details</title>
<link rel="stylesheet" href="style.css">
</head>
<body>

<center><h1>Details for the given DoctorID</h1></center>
<form action="leave">
	<table id="doc">
		<tr>
			<th>Id</th>
			<th>Name</th>
			<th>DOB</th>
			<th>Joining</th>
			<th>Gender</th>
			<th>Qualification</th>
			<th>Specialization</th>
			<th>Experience</th>
			<th>Address</th>
			<th>Contact_At</th>
			<th>Booking</th>
		</tr>
		
		<c:forEach items = "${allDoc}" var="doctor">
		<input type="text" value="${doctor.docID}" name="id" id="id" style="visibility:hidden;height:0px;">
		<tr>
			<td>${doctor.docID}</td>
			<td>${doctor.docName}</td>
			<td>${doctor.docDOB}</td>
			<td>${doctor.docDOJ}</td>
			<td>${doctor.docGender}</td>
			<td>${doctor.docQualification}</td>
			<td>${doctor.docSpecialization}</td>
			<td>${doctor.docYearsofExperience}</td>
			<td>${doctor.docAddress}</td>
			<td>${doctor.docContactNumber}</td>
			<td><a href="bookApp"><input type="button" value="Book Appointement"></a></td>
		</tr>
		</c:forEach>
		
	</table>
</form>
</body>
</html>