<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
	function change() {
		// Lấy đối tượng
		var object = document.getElementById("object");

		// lấy thuộc tính type
		var type = object.type;

		// kiểm tra thuộc tính type và thay đổi
		if (type == "password") {

			object.type = 'text';
		} else {
			object.type = "password";
		}

	}
</script>
</head>
<body>
	<input type="password" value="Nguyenduytien" id="object" />
	<input type="checkbox" onclick="change()">
</body>
</html>