<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Guess book</title>
</head>
<body>
<form method="post"
	action="<%=request.getContextPath()%>/viewGuestBook">
<div><label for="username">Username: </label><input type="text"
	name="username" /></div>
<div><label for="message">Message</label></div>
<div><textarea name="message"></textarea></div>
<div><input type="submit" /> <input type="reset" /></div>
</form>
<div>
<h3>Entries</h3>
<c:set var="msg" value="Wow"/>
<c:choose>
	<c:when test="${entries!=null}">
	<table>
	<tr><th>#</th><th>From</th><th>Msg</th><th>Posted At</th></tr>
		<c:forEach var="entry" items="${entries}" varStatus="counter">
			 <tr>
				<td>${counter.count}</td>
				<td>${entry.author}</td>
				<td>${entry.content}</td>
				<td>${entry.postedDate}</td>	 
			 </tr>
		</c:forEach>
		</table>
	</c:when>
	<c:otherwise>
		<div>(No entries)</div>
	</c:otherwise>
</c:choose></div>
</body>
</html>