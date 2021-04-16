<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="user.UserDAO"%>
<%@ page import="java.io.PrintWriter"%>


<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/heml; charset="UTF-8">

<title>JSP 게시판 웹 사이트</title>
</head>
<body>
	<%
		session.invalidate(); //현재 이 페이지의 접속한 회원의 세션을 빼앗는다.
	%>
	<script>
		location.href = 'main.jsp';
	</script>
</body>
</html>