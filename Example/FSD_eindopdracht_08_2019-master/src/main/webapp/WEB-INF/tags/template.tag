<%@ tag description="Overall Page Template" pageEncoding="UTF-8" %>
<%@ attribute name="pageTitle" required="true" %>
<%@ attribute name="navigation" fragment="true" %>
<%@ attribute name="extraJS" fragment="true" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>${pageTitle} | Don Diablo</title>
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
	<nav id="navbarid" class="navbar navbar-dark navbar-expand-md bg-dark sticky-top">
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
				<jsp:invoke fragment="navigation"/>
			</ul>
		</div>
	</nav>

	<jsp:doBody/>
	
	<script src="resources/jquery.slim.min.js"></script>
	<script src="resources/popper.min.js"></script>
	<script src="resources/bootstrap.min.js"></script>
	<script src="resources/javascript.js"></script>
	<jsp:invoke fragment="extraJS"/>

</body>
</html>