<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
<title>View Doctor Details</title>
</head>
<body>
<div class="container mt-4">
<form action="getDetails" method="post">
  <div class="mb-3">
    <label for="DocID">Doctor ID</label>
     <input type="number" class="form-control" name ="DocID" id="DocID">
  </div>
  <button type="submit" class="btn btn-primary">Submit To view</button>
</form>
</div>
</body>
</html>