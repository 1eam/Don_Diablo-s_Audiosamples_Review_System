<%@ tag description="Login Navigation Template" pageEncoding="UTF-8" %>
<%@ attribute name="pageType" required="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
	<c:when test="${pageType == 'demo'}">
		<li class="nav-item active">
			<a class="nav-link" href="demo.jsp"><i class="fas fa-upload"></i> Demo drop</a>
		</li>
	</c:when>
	<c:otherwise>
		<li class="nav-item">
			<a class="nav-link" href="demo.jsp"><i class="fas fa-upload"></i> Demo drop</a>
		</li>
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${pageType == 'comment'}">
		<li class="nav-item active">
			<a class="nav-link" href="#"><i class="fas fa-comments"></i> Comment</a>
		</li>
	</c:when>
	<c:otherwise>
		<li class="nav-item">
			<a class="nav-link" href="#"><i class="fas fa-comments"></i> Comment</a>
		</li>
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${pageType == 'user'}">
		<li class="nav-item active">
			<a class="nav-link" href="UserPage.jsp"><i class="fas fa-user"></i> User page</a>
		</li>
	</c:when>
	<c:otherwise>
		<li class="nav-item">
			<a class="nav-link" href="UserPage.jsp"><i class="fas fa-user"></i> User page</a>
		</li>
	</c:otherwise>
</c:choose>
<li class="nav-item">
	<a class="nav-link" href="LogoutServlet"><i class="fas fa-user-slash"></i> Log out</a>
</li>