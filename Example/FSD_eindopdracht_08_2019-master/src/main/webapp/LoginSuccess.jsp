<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%@ page import="com.diabolo.security.SessionValidator" %>

<%
//allow access only if session exists
String firstName = null;
String lastName = null;
String email = null;
String groupName = null;
int userID = 0;
int groupID = 0;
HttpSession session = null;
Cookie[] cookies = null;
String userName = null;
String sessionID = null;

SessionValidator validator = new SessionValidator(request, response);
if (validator.isValidSession()) {
    if (validator.isValidCookies()) {
        session = validator.getSession();
        cookies = validator.getCookies();
        firstName = (String) session.getAttribute("firstname");
        lastName = (String) session.getAttribute("lastname");
        email = (String) session.getAttribute("email");
        groupName = (String) session.getAttribute("groupname");
        userID = (int) session.getAttribute("userid");
        groupID = (int) session.getAttribute("groupid");
        sessionID = session.getId();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("user")) {
                userName = cookie.getValue();
            }
        }
    }
}
%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Log in Success Page | Don Diablo</title>
    <link rel="icon" type="image/png" sizes="192x192" href="images/android-icon-192x192.png">
    <link rel="icon" type="image/png" sizes="32x32" href="images/favicon-32x32.png">
    <link rel="icon" type="image/png" sizes="96x96" href="images/favicon-96x96.png">
    <link rel="icon" type="image/png" sizes="16x16" href="images/favicon-16x16.png">
    <link rel="stylesheet" href="resources/bootstrap.min.css">
	<link rel="stylesheet" href="resources/style.css">
	<link rel="stylesheet" href="resources/floating-labels.css">
	<link rel="stylesheet" href="resources/offcanvas.css">
	<link rel="stylesheet" href="resources/fontawesome/css/all.min.css">
</head>

<body class="bgMainLogo">
	<nav class="navbar navbar-dark navbar-expand-md bg-dark sticky-top">
		<a class="navbar-brand d-inline-block d-md-none" href="#">
			<img class="dondiablo_png" src="images/dondiablo.png">
		</a>
		<button class="navbar-toggler" type="button" data-toggle="offcanvas" data-target="#mainNavbar">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="navbar-collapse offcanvas-collapse" id="mainNavbar">
			<ul class="container navbar-nav justify-content-between">
				<li class="nav-item d-md-inline-block d-none">
					<a class="navbar-brand" href="/DonDiablo/">
						<img class="dondiablo_png" src="images/dondiablo.png">
					</a>					
				</li>
				<li class="nav-item">
					<a class="nav-link" href="#"><i class="fas fa-upload"></i> Demo drop</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="#"><i class="fas fa-comments"></i> Comment</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="UserPage.jsp"><i class="fas fa-user"></i> User page</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="LogoutServlet"><i class="fas fa-user-slash"></i> Log out</a>
				</li>
			</ul>
		</div>
	</nav>
	
	<div class="container py-5">
		<div class="row">
			<div class="offset-md-2 col-md-8 offset-md-2">
				<div class="card text-white bg-secondary">
					<div class="card-header bg-dark text-white">
						<h4 class="card-title text-uppercase mt-1 mb-1"><i class="fas fa-cat"></i> Welcome <%=firstName%> <%=lastName%></h4>
					</div>
					<div class="card-body">
						<div class="row">
							<div class="col-md-4 col-sm-4 col-xs-12">
								<div class="float-left">
									Log in successful.
								</div>
							</div>
							<div class="col-md-8 col-sm-8 col-xs-12">
								<div class="float-right">
									Session ID: <%=sessionID%>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6 col-sm-6 col-xs-12">
								<div class="float-left">
									Username: <%=userName%>
								</div>
							</div>
							<div class="col-md-6 col-sm-6 col-xs-12">
								<div class="float-right">
									Email: <%=email%>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-4 col-sm-4 col-xs-12">
								<div class="float-left">
									User ID: <%=userID%>
								</div>
							</div>
							<div class="col-md-4 col-sm-4 col-xs-12">
								<div class="float-center">
									Group ID: <%=groupID%>
								</div>
							</div>
							<div class="col-md-4 col-sm-4 col-xs-12">
								<div class="float-right">
									Group name: <%=groupName%>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script src="resources/jquery.slim.min.js"></script>
	<script src="resources/popper.min.js"></script>
	<script src="resources/bootstrap.min.js"></script>
	<script src="resources/javascript.js"></script>

</body>
</html>