<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
  <script src="scripts/jquery-3.1.0.min.js"></script>
  <script type="text/javascript" src="scripts/jquery.validate.js"></script>
  <script src="bootstrap/js/bootstrap.min.js"></script>
</head>
<body>
     <div id="loading">Loading</div>
     <div id="result"></div>
     <script type="text/javascript">
      $(document).ready(function(){
    	  $('#loading').click(function(){
    		  $.getJSON( "https://raw.githubusercontent.com/duytien1993ht/vidu/master/duytien/WebContent/data.json", function( data ) {
    			  var items = [];
    			  $.each( data, function( key, val ) {
    			    items.push( "<li id='" + key + "'>" + val + "</li>" );
    			  });
    			  
    			});
    	  });
      });
    </script>
</body>
</html>