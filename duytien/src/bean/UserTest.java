<%@page import="vn.com.thanhsang.model.bean.Employee"%>
<%@page
	import="org.apache.taglibs.standard.tag.common.core.ForEachSupport"%>
<%@page import="vn.com.thanhsang.model.bean.User"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="Tran Thanh Sang">
<link rel="icon" href="icon/logo.ico">

<title>Index</title>

<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/bootstrap-theme.min.css" rel="stylesheet">
<script src="js/ie10-viewport-bug-workaround.js"></script>
<script src="js/ie-emulation-modes-warning.js"></script>
<script src="js/jquery-3.1.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/bootstrap-filestyle.min.js"></script>
<style type="text/css">
body {
	padding-top: 40px;
	padding-bottom: 40px;
	background-color: #eee;
}

.wrap {
	min-width: 600px;
}

.hearder {
	background-color: #c5d9f1;
	text-align: center;
	border-color: #1f497d;
	border-style: solid;
	border-width: 1px;
	margin-bottom: 20px;
}

.form {
	border-color: #1f497d;
	border-style: solid;
	border-width: 1px;
	margin-top: 20px;
	padding: 20px 20px 20px 20px;
}

.item-group {
	background-color: #daeef3;
	padding: 20px 20px 20px 20px;
	overflow-y: scroll;
	max-height: 200px;
	height: 200px;
}
</style>

<script>
	function confirmDelete(userId) {
		alert("Bạn vừa xóa thành công " + userId
				+ ". Nhấn OK để load lại trang!");
	};
	var list = Array();
	$(function(){
	    $('a.checkEdit').click(function(e){
	    	e.preventDefault();
    		$(this).closest('tr').css({"color": "#54ae54"});
    		$(this).closest('tr').find('td:nth-child(3)').prop("contenteditable",true);	
    		$(this).closest('tr').find('td:nth-child(4)').prop("contenteditable",true);	
    		$(this).closest('tr').find('td:nth-child(5)').prop("contenteditable",true);	
    		$(this).closest('tr').find('td:nth-child(6)').prop("contenteditable",true);	
    		$(this).closest('tr').find('td:nth-child(7)').prop("contenteditable",true);	
    		
	    });
	    
	    $('a.checkDelete').click(function(e){
	    	e.preventDefault();
    		$(this).closest('tr').css({"color": "rgb(255,0,0)"});
    		$(this).closest('tr').find('td:nth-child(3)').prop("contenteditable",false);	
    		$(this).closest('tr').find('td:nth-child(4)').prop("contenteditable",false);	
    		$(this).closest('tr').find('td:nth-child(5)').prop("contenteditable",false);	
    		$(this).closest('tr').find('td:nth-child(6)').prop("contenteditable",false);	
    		$(this).closest('tr').find('td:nth-child(7)').prop("contenteditable",false);	
	    });
	    
	    $('#btnCommit').click(function(){
			$('body > div.container > div.wrap > div.form > div.form > div.table-responsive > div.table > #fbody > tr').each(function(){
				if($(this).css("color")=="rgb(255,0,0)"){
					alert("NI NE");
					var singleObject = {};
					singleObject['emp_id'] = $(this).closest('tr').find('td:nth-child(2)').text();
					singleObject['emp_firstname'] = $(this).closest('tr').find('td:nth-child(3)').text();
					singleObject['emp_lastname'] = $(this).closest('tr').find('td:nth-child(4)').text();
					singleObject['email'] = $(this).closest('tr').find('td:nth-child(5)').text();
					singleObject['phone'] = $(this).closest('tr').find('td:nth-child(6)').text();
					singleObject['dob'] = $(this).closest('tr').find('td:nth-child(7)').text();
					list.push(singleObject);
				}
			});
	    });
	});
</script>

</head>

<body>
	<%
		//String userName = (String)session.getAttribute("userName");
		String role = (String) session.getAttribute("role");
		ArrayList<Employee> listEmployee = (ArrayList<Employee>) session.getAttribute("listEmployee");
	%>
	<div class="container">
		<div class="wrap">

			<%
				if ("Admin".equals(role)) {
			%>
			<div class="hearder">
				<h2>EMPLOYEE MANAGEMENT</h2>
			</div>
			<div class="row">

				<div class="col-md-1 col-md-offset-0" style="float: right">
					<a href="logout" style="text-decoration: underline">Logout</a>
				</div>

				<div class="col-md-2 col-md-offset-9" style="float: right">
					User: <strong>${userName}</strong>
				</div>
			</div>
			<div class="form">



				<div class="form">

					<%
						Employee employee = (Employee) session.getAttribute("employeeUpdate");
							if (employee == null) {
								employee = new Employee("", "", "", "", "", "");
							}
					%>
					<form:form class="form-signin" role="form" action="addOrUpdate"
						method="post" modelAttribute="updateForm">
						<div class="row">
							<div class="col-md-1 col-md-offset-0">
								<label>Id</label>
							</div>
							<div class="col-md-4 col-md-offset-0">
								<input class="form-control" placeholder="Id" required="required"
									autofocus="autofocus" type="text" id="idUser" name="employeeId"
									maxlength="50" value="<%=employee.getEmployeeId()%>">
							</div>
							<div class="col-md-1 col-md-offset-1">
								<label>First_Name</label>
							</div>
							<div class="col-md-4 col-md-offset-0">
								<input class="form-control" placeholder="First_Name"
									required="required" autofocus="autofocus" type="text"
									name="firstName" maxlength="50"
									value="<%=employee.getFirstName()%>">
							</div>
						</div>
						<br>
						<div class="row">
							<div class="col-md-1 col-md-offset-0">
								<label>Last_Name</label>
							</div>
							<div class="col-md-4 col-md-offset-0">
								<input class="form-control" placeholder="Last_Name"
									required="required" autofocus="autofocus" type="text"
									name="lastName" maxlength="50"
									value="<%=employee.getLastName()%>">
							</div>
							<div class="col-md-1 col-md-offset-1">
								<label>Email</label>
							</div>
							<div class="col-md-4 col-md-offset-0">
								<input class="form-control" placeholder="Email"
									required="required" autofocus="autofocus" type="text"
									name="email" maxlength="50" value="<%=employee.getEmail()%>">
							</div>
						</div>
						<br>
						<div class="row">
							<div class="col-md-1 col-md-offset-0">
								<label>Phone</label>
							</div>
							<div class="col-md-4 col-md-offset-0">
								<input class="form-control" placeholder="Phone"
									required="required" autofocus="autofocus" type="text"
									name="phone" maxlength="50" value="<%=employee.getPhone()%>">
							</div>
							<div class="col-md-1 col-md-offset-1">
								<label>DOB</label>
							</div>
							<div class="col-md-4 col-md-offset-0">
								<input class="form-control" placeholder="DOB"
									required="required" autofocus="autofocus" type="text"
									name="dob" maxlength="50" value="<%=employee.getDob()%>">
							</div>
						</div>
						<br>
						<div style="text-align: center;">
							<button class="btn btn-md btn-primary " type="submit"
								style="text-align: center; margin-top: 10px">Commit
								(Add or Update)</button>
						</div>

					</form:form>

				</div>
				<%
					String keyWord = (String) session.getAttribute("KeyWord");
						String category = (String) session.getAttribute("Category");
				%>
				<form:form class="form-signin" role="form" action="index"
					method="get" modelAttribute="searchForm">

					<div class="form">
						<p>
							<strong>Tìm kiếm </strong>
						</p>
						<div class="row">
							<div class="col-md-4">
								<div class="input-group">
									<span class="input-group-btn">
										<button class=" btn btn-success btn-block" type="submit">
											<span class="glyphicon glyphicon-search"></span>
										</button>
									</span> <input name="keyword" type="text" class="form-control"
										placeholder="Search" value="<%=keyWord%>">
								</div>
								<!-- /input-group -->
							</div>
							<!-- /.col-lg-6 -->
							<div class="col-md-2 col-md-offset-0">
								<label>Tìm kiếm theo</label>
							</div>
							<div class="col-md-2 col-md-offset-0">
								<select class="form-control btn btn-default" name="category">
									<option <%if ("Default".equals(category)) {%>
										selected="selected" <%}%> value="Default">Default</option>
									<option <%if ("Id".equals(category)) {%> selected="selected"
										<%}%> value="Id">Id</option>
									<option <%if ("First_Name".equals(category)) {%>
										selected="selected" <%}%> value="First_Name">First_Name</option>
									<option <%if ("Last_Name".equals(category)) {%>
										selected="selected" <%}%> value="Last_Name">Last_Name</option>
									<option <%if ("Email".equals(category)) {%> selected="selected"
										<%}%> value="Email">Email</option>
									<option <%if ("Phone".equals(category)) {%> selected="selected"
										<%}%> value="Phone">Phone</option>
									<option <%if ("DOB".equals(category)) {%> selected="selected"
										<%}%> value="DOB">DOB</option>
								</select>
							</div>
						</div>
					</div>
				</form:form>



				<div class="form">
					<p>
						<strong>List of employee:</strong>
					</p>
					<div class="table-responsive table-bordered"
						style="overflow-y: scroll; max-height: 250px">
						<table class="table">
							<!-- On cells (`td` or `th`) -->
							<thead>
							<tr>
								<th class="info">No</th>
								<th class="info">Id <select
									class="form-control btn btn-default" name="category"
									style="width: 20px">
										<option <%if ("Default".equals(category)) {%>
											selected="selected" <%}%> value="Default">Default</option>
										<option <%if ("Id".equals(category)) {%> selected="selected"
											<%}%> value="Id">Id</option>
										<option <%if ("First_Name".equals(category)) {%>
											selected="selected" <%}%> value="First_Name">First_Name</option>
										<option <%if ("Last_Name".equals(category)) {%>
											selected="selected" <%}%> value="Last_Name">Last_Name</option>
										<option <%if ("Email".equals(category)) {%>
											selected="selected" <%}%> value="Email">Email</option>
										<option <%if ("Phone".equals(category)) {%>
											selected="selected" <%}%> value="Phone">Phone</option>
										<option <%if ("DOB".equals(category)) {%> selected="selected"
											<%}%> value="DOB">DOB</option>
								</select>
								</th>
								<th class="info">First_Name <select
									class="form-control btn btn-default" name="category"
									style="width: 20px">
										<option <%if ("Default".equals(category)) {%>
											selected="selected" <%}%> value="Default">Default</option>
										<option <%if ("Id".equals(category)) {%> selected="selected"
											<%}%> value="Id">Id</option>
										<option <%if ("First_Name".equals(category)) {%>
											selected="selected" <%}%> value="First_Name">First_Name</option>
										<option <%if ("Last_Name".equals(category)) {%>
											selected="selected" <%}%> value="Last_Name">Last_Name</option>
										<option <%if ("Email".equals(category)) {%>
											selected="selected" <%}%> value="Email">Email</option>
										<option <%if ("Phone".equals(category)) {%>
											selected="selected" <%}%> value="Phone">Phone</option>
										<option <%if ("DOB".equals(category)) {%> selected="selected"
											<%}%> value="DOB">DOB</option>
								</select>
								</th>
								<th class="info">Last_Name <select
									class="form-control btn btn-default" name="category"
									style="width: 20px">
										<option <%if ("Default".equals(category)) {%>
											selected="selected" <%}%> value="Default">Default</option>
										<option <%if ("Id".equals(category)) {%> selected="selected"
											<%}%> value="Id">Id</option>
										<option <%if ("First_Name".equals(category)) {%>
											selected="selected" <%}%> value="First_Name">First_Name</option>
										<option <%if ("Last_Name".equals(category)) {%>
											selected="selected" <%}%> value="Last_Name">Last_Name</option>
										<option <%if ("Email".equals(category)) {%>
											selected="selected" <%}%> value="Email">Email</option>
										<option <%if ("Phone".equals(category)) {%>
											selected="selected" <%}%> value="Phone">Phone</option>
										<option <%if ("DOB".equals(category)) {%> selected="selected"
											<%}%> value="DOB">DOB</option>
								</select>
								</th>
								<th class="info">Email <select
									class="form-control btn btn-default" name="category"
									style="width: 20px">
										<option <%if ("Default".equals(category)) {%>
											selected="selected" <%}%> value="Default">Default</option>
										<option <%if ("Id".equals(category)) {%> selected="selected"
											<%}%> value="Id">Id</option>
										<option <%if ("First_Name".equals(category)) {%>
											selected="selected" <%}%> value="First_Name">First_Name</option>
										<option <%if ("Last_Name".equals(category)) {%>
											selected="selected" <%}%> value="Last_Name">Last_Name</option>
										<option <%if ("Email".equals(category)) {%>
											selected="selected" <%}%> value="Email">Email</option>
										<option <%if ("Phone".equals(category)) {%>
											selected="selected" <%}%> value="Phone">Phone</option>
										<option <%if ("DOB".equals(category)) {%> selected="selected"
											<%}%> value="DOB">DOB</option>
								</select>
								</th>
								<th class="info">Phone <select
									class="form-control btn btn-default" name="category"
									style="width: 20px">
										<option <%if ("Default".equals(category)) {%>
											selected="selected" <%}%> value="Default">Default</option>
										<option <%if ("Id".equals(category)) {%> selected="selected"
											<%}%> value="Id">Id</option>
										<option <%if ("First_Name".equals(category)) {%>
											selected="selected" <%}%> value="First_Name">First_Name</option>
										<option <%if ("Last_Name".equals(category)) {%>
											selected="selected" <%}%> value="Last_Name">Last_Name</option>
										<option <%if ("Email".equals(category)) {%>
											selected="selected" <%}%> value="Email">Email</option>
										<option <%if ("Phone".equals(category)) {%>
											selected="selected" <%}%> value="Phone">Phone</option>
										<option <%if ("DOB".equals(category)) {%> selected="selected"
											<%}%> value="DOB">DOB</option>
								</select>
								</th>
								<th class="info">DOB <select
									class="form-control btn btn-default" name="category"
									style="width: 20px">
										<option <%if ("Default".equals(category)) {%>
											selected="selected" <%}%> value="Default">Default</option>
										<option <%if ("Id".equals(category)) {%> selected="selected"
											<%}%> value="Id">Id</option>
										<option <%if ("First_Name".equals(category)) {%>
											selected="selected" <%}%> value="First_Name">First_Name</option>
										<option <%if ("Last_Name".equals(category)) {%>
											selected="selected" <%}%> value="Last_Name">Last_Name</option>
										<option <%if ("Email".equals(category)) {%>
											selected="selected" <%}%> value="Email">Email</option>
										<option <%if ("Phone".equals(category)) {%>
											selected="selected" <%}%> value="Phone">Phone</option>
										<option <%if ("DOB".equals(category)) {%> selected="selected"
											<%}%> value="DOB">DOB</option>
								</select>
								</th>
								<th class="info">Thao tác</th>
								<th class="info">For All</th>
							</tr>
							</thead>
							<%
								int numPage = (int) request.getAttribute("numPage");
									int pageNumber = (int) request.getAttribute("pageNumber");
									int start = (int) request.getAttribute("start");
									for (int i = 0; i < listEmployee.size(); i++) {
							%>
							<tbody id="fbody">
							<tr>
								<td class="active"><%=start++%></td>
								<td class="active"><%=listEmployee.get(i).getEmployeeId()%></td>
								<td class="active"><%=listEmployee.get(i).getFirstName()%></td>
								<td class="active"><%=listEmployee.get(i).getLastName()%></td>
								<td class="active"><%=listEmployee.get(i).getEmail()%></td>
								<td class="active"><%=listEmployee.get(i).getPhone()%></td>
								<td class="active"><%=listEmployee.get(i).getDob()%></td>
								<td class="active"><a
									href="update?id=<%=listEmployee.get(i).getEmployeeId()%>">Edit</a>&nbsp;&nbsp;
									<a
									onclick="confirmDelete('<%=listEmployee.get(i).getEmployeeId()%>')"
									href="delete?id=<%=listEmployee.get(i).getEmployeeId()%>&page=<%=pageNumber%>">Delete</a></td>
								<td class="active"><a class="checkEdit" href="#"> <span
										class="glyphicon glyphicon-ok"></span>
								</a>&nbsp;&nbsp; <a class="checkDelete" href="#"> <span
										class="glyphicon glyphicon-remove"></span>
								</a></td>
							</tr>
							</tbody>
							<%
								}
							%>
						</table>
					</div>
					<div style="text-align: center;">
						<button id="btnCommit" class="btn btn-md btn-primary "
							type="button" style="text-align: center; margin-top: 10px">Commit
							(Add or Update)</button>
						<button class="btn btn-md btn-primary " type="submit"
							style="text-align: center; margin-top: 10px">Filter
							(Refresh)</button>
					</div>
					<nav aria-label="Page navigation">
						<ul class="pagination">
							<li><a <%if (pageNumber != 1 && numPage > 0) {%>
								href="index?page=1&category=<%=category%>&keyword=<%=keyWord%>"
								<%} else {%> style="display: none;" <%}%> aria-label="First">
									<span aria-hidden="true"> &lt&lt; </span>
							</a></li>
							<li><a <%if (pageNumber != 1 && numPage > 0) {%>
								href="index?page=<%=pageNumber - 1%>&category=<%=category%>&keyword=<%=keyWord%>"
								<%} else {%> style="display: none;" <%}%> aria-label="Previous">
									<span aria-hidden="true">&lt;</span>
							</a></li>
							<%
								for (int pageNum = 1; pageNum <= numPage; pageNum++) {
							%>
							<li><a
								href="index?page=<%=pageNum%>&category=<%=category%>&keyword=<%=keyWord%>"><%=pageNum%></a></li>
							<%
								}
							%>
							<li><a <%if (pageNumber != numPage && numPage > 0) {%>
								href="index?page=<%=pageNumber + 1%>&category=<%=category%>&keyword=<%=keyWord%>"
								<%} else {%> style="display: none;" <%}%> aria-label="Next">
									<span aria-hidden="true">&gt;</span>
							</a></li>
							<li><a <%if (pageNumber != numPage && numPage > 0) {%>
								href="index?page=<%=numPage%>&category=<%=category%>&keyword=<%=keyWord%>"
								<%} else {%> style="display: none;" <%}%> aria-label="Last">
									<span aria-hidden="true">&gt&gt;</span>
							</a></li>
						</ul>
					</nav>
				</div>

			</div>
			<%
				} else {
			%>
			<div class="hearder">
				<h2>ACCESS DENIED</h2>
			</div>
			<div class="row">

				<div class="col-md-1 col-md-offset-0" style="float: right">
					<a href="logout" style="text-decoration: underline">Logout</a>
				</div>

				<div class="col-md-2 col-md-offset-9" style="float: right">
					User: <strong>${userName}</strong>
				</div>
			</div>
			<%
				}
			%>
		</div>
	</div>
</body>
</html>
