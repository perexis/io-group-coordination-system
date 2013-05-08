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
	$("form").submit(function(event) {
		event.preventDefault();
		$("#result").html('');

		form = $(this);
		formData = form.serialize();
		formAction = form.attr('action');
		
		$.ajax({
			type : "POST",
			url : formAction,
			data : formData,
			success : function(data) {
				form.get(0).reset();
				$('div#result').show();
				$("#result").html(data);
				$("html, body").animate({ scrollTop: 0 }, "fast");
			},
			error : function(errMsg) {
				$('div#result').hide();
				alert("Error: " + errMsg.returned_val);
			}
		});
	});
});
</script>
<title>Admin console</title>
</head>

<body>

<div id="result"></div>
<h3>Admin console</h3>

<hr/>

<a href="/">Back to main page</a>

<hr/>
<table><tr align="center"><td>User</td><td>UserItem</td><td>Layer</td><td>Utils</td></tr>
<tr>
<td>
<form:form action="/admin/addUser" modelAttribute="formUser" method="post">
<fieldset> 
	<div class="field">
	<label>ID</label><form:input path="id"/> <br/>
	<label>Password</label><form:input path="password"/> <br/>
	<label>Avatar (URL)</label><form:input path="avatar"/> <br/>
	<label>Name</label><form:input path="name"/> <br/>
	<label>Surname</label><form:input path="surname"/> <br/>
	<label>Phone</label><form:input path="phone"/> <br/>
	<label>Email</label><form:input path="email"/>
	</div>
	<div class="submit"><input type="submit" value="Add" ></div>
</fieldset>
</form:form>
</td>
<td>
<form:form action="/admin/addUserItem" modelAttribute="formUserItem" method="post">
<fieldset> 
	<div class="field">
	<label>ID</label><form:input path="id"/> <br/>
	<label>Description</label><form:input path="description"/> <br/>
	<label>Image (URL)</label><form:input path="image"/>
	</div>
	<div class="submit"><input type="submit" value="Add" ></div>
</fieldset>
</form:form>
</td>
<td>
<form:form action="/admin/addLayer" modelAttribute="formString" method="post">
<fieldset> 
	<div class="field">
	<label>ID</label><form:input path="value"/> <br/>
	</div>
	<div class="submit"><input type="submit" value="Add"></div>
</fieldset>
</form:form>
</td>
<td>
<form:form action="/admin/listSessions" modelAttribute="formString" method="post">
<fieldset> 
	<div class="submit"><input type="submit" value="List sessions"></div>
</fieldset>
</form:form>
</td>
</tr>
<tr>
<td>
<form:form action="/admin/deleteUser" modelAttribute="formUser" method="post">
<fieldset> 
	<div class="field">
	<label>ID</label><form:input path="id"/> <br/>
	</div>
	<div class="submit"><input type="submit" value="Delete"></div>
</fieldset>
</form:form>
</td>
<td>
<form:form action="/admin/deleteUserItem" modelAttribute="formUserItem" method="post">
<fieldset> 
	<div class="field">
	<label>ID</label><form:input path="id"/>
	</div>
	<div class="submit"><input type="submit" value="Delete" ></div>
</fieldset>
</form:form>
</td>
<td>
<form:form action="/admin/deleteLayer" modelAttribute="formString" method="post">
<fieldset> 
	<div class="field">
	<label>ID</label><form:input path="value"/> <br/>
	</div>
	<div class="submit"><input type="submit" value="Delete"></div>
</fieldset>
</form:form>
</td>
</tr>
<tr>
<td>
<form:form action="/admin/updateUser" modelAttribute="formUser" method="post">
<fieldset> 
	<div class="field">
	<label>ID</label><form:input path="id"/> <br/>
	<label>Password</label><form:input path="password"/> <br/>
	<label>Avatar (URL)</label><form:input path="avatar"/> <br/>
	<label>Name</label><form:input path="name"/> <br/>
	<label>Surname</label><form:input path="surname"/> <br/>
	<label>Phone</label><form:input path="phone"/> <br/>
	<label>Email</label><form:input path="email"/>
	</div>
	<div class="submit"><input type="submit" value="Update" ></div>
</fieldset>
</form:form>
</td>
<td>
<form:form action="/admin/updateUserItem" modelAttribute="formUserItem" method="post">
<fieldset> 
	<div class="field">
	<label>ID</label><form:input path="id"/> <br/>
	<label>Description</label><form:input path="description"/> <br/>
	<label>Image (URL)</label><form:input path="image"/>
	</div>
	<div class="submit"><input type="submit" value="Update" ></div>
</fieldset>
</form:form>
</td>
<td>

</td>
</tr>
<tr>
<td>
<form:form action="/admin/findUser" modelAttribute="formUser" method="post">
<fieldset> 
	<div class="field">
	<label>ID</label><form:input path="id"/> <br/>
	</div>
	<div class="submit"><input type="submit" value="Find"></div>
</fieldset>
</form:form>
</td>
<td>
<form:form action="/admin/findUserItem" modelAttribute="formUserItem" method="post">
<fieldset> 
	<div class="field">
	<label>ID</label><form:input path="id"/>
	</div>
	<div class="submit"><input type="submit" value="Find" ></div>
</fieldset>
</form:form>
</td>
<td>
<form:form action="/admin/findLayer" modelAttribute="formString" method="post">
<fieldset> 
	<div class="field">
	<label>ID</label><form:input path="value"/> <br/>
	</div>
	<div class="submit"><input type="submit" value="Find"></div>
</fieldset>
</form:form>
</td>
</tr>
<tr>
<td>
<form:form action="/admin/listAllUsers" method="post">
<fieldset> 
	<div class="submit"><input type="submit" value="List all"></div>
</fieldset>
</form:form>
</td>
<td>
<form:form action="/admin/listAllUserItems" method="post">
<fieldset> 
	<div class="submit"><input type="submit" value="List all"></div>
</fieldset>
</form:form>
</td>
<td>
<form:form action="/admin/listAllLayers" modelAttribute="formString" method="post">
<fieldset> 
	<div class="submit"><input type="submit" value="List all"></div>
</fieldset>
</form:form>
</td>
</tr>
<tr>
<td>
<form:form action="/admin/deleteAllUsers" method="post">
<fieldset> 
	<div class="submit"><input type="submit" value="Delete all"></div>
</fieldset>
</form:form>
</td>
<td>
<form:form action="/admin/deleteAllUserItems" method="post">
<fieldset> 
	<div class="submit"><input type="submit" value="Delete all"></div>
</fieldset>
</form:form>
</td>
<td>
<form:form action="/admin/deleteAllLayers" modelAttribute="formString" method="post">
<fieldset> 
	<div class="submit"><input type="submit" value="Delete all"></div>
</fieldset>
</form:form>
</td>
</tr>
</table>













</body>

</html>