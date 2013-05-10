<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<html>

<head>
<link type="text/css" rel="stylesheet" href="<c:url value="/static/style.css" />" />
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js">
</script>
<script>
$(document).ready(function() {
	$('div#result:empty').hide();
	$(".testForm").submit(function(event) {
		event.preventDefault();
		$("#result").html('');
		
		form = $(this);
		json = form.find('input[name="value"]').val();
		formAction = form.attr('action');
		
		$.ajax({
			type : "POST",
			url : formAction,
			data : json,
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(data) {
				form.get(0).reset();
				$('div#result').show();
				$("#result").html(JSON.stringify(data));
				$("html, body").animate({ scrollTop: 0 }, "fast");
			},
			error : function(xhr, textStatus, errorThrown) {
				$('div#result').show();
				$("#result").html(xhr.responseText);
				$("html, body").animate({ scrollTop: 0 }, "fast");
			}
		});
	});
});
</script>
<title>JSON testing page</title>
</head>

<body>

<div id="result">${message}</div>
<h3>JSON testing page</h3>

<hr/>

Log in as admin (login: admin, pass: admin (or just leave blank)):
<form:form action="/admin/" modelAttribute="formSimpleUser" method="post">
<fieldset> 
	<div class="field">
	<label>Login</label><form:input path="login"/> <br/>
	<label>Password</label><form:input path="password"/>
	</div>
	<div class="submit"><input type="submit" value="Log in!"></div>
</fieldset>
</form:form>

<hr/>

<form:form class="testForm" action="/login" modelAttribute="formString" method="post">
	<input type="submit" value="/login">
	<form:input path="value" size="100"/>
</form:form>

<form:form class="testForm" action="/logout" modelAttribute="formString" method="post">
	<input type="submit" value="/logout">
	<form:input path="value" size="100"/>
</form:form>

<form:form class="testForm" action="/getUsers" modelAttribute="formString" method="post">
	<input type="submit" value="/getUsers">
	<form:input path="value" size="100"/>
</form:form>

<hr/>

<form:form class="testForm" action="/getMapItems" modelAttribute="formString" method="post">
	<input type="submit" value="/getMapItems">
	<form:input path="value" size="100"/>
</form:form>

<form:form class="testForm" action="/getLayers" modelAttribute="formString" method="post">
	<input type="submit" value="/getLayers">
	<form:input path="value" size="100"/>
</form:form>

<form:form class="testForm" action="/addItemToLayer" modelAttribute="formString" method="post">
	<input type="submit" value="/addItemToLayer">
	<form:input path="value" size="100"/>
</form:form>

<form:form class="testForm" action="/removeMapItem" modelAttribute="formString" method="post">
	<input type="submit" value="/removeMapItem">
	<form:input path="value" size="100"/>
</form:form>

</body>

</html>