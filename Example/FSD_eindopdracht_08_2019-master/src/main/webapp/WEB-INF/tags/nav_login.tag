<%@ tag description="Login Navigation Template" pageEncoding="UTF-8" %>
<%@ attribute name="pageType" required="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<li class="nav-item">
	<a class="nav-link disabled" href="#"><i class="fas fa-upload"></i> Demo drop</a>
</li>
<li class="nav-item">
	<a class="nav-link disabled" href="#"><i class="fas fa-comments"></i> Comment</a>
</li>
<c:choose>
	<c:when test="${pageType == 'login'}">
		<li class="nav-item active">
			<a class="nav-link" href="login.jsp"><i class="fas fa-user-lock"></i> Log in</a>
		</li>
	</c:when>
	<c:otherwise>
		<li class="nav-item">
			<a class="nav-link" href="login.jsp"><i class="fas fa-user-lock"></i> Log in</a>
		</li>
	</c:otherwise>
</c:choose>
<c:choose>
	<c:when test="${pageType == 'register'}">
		<li class="nav-item active">
			<a class="nav-link" href="register.jsp"><i class="fas fa-user-plus"></i> Sign up</a>
		</li>
	</c:when>
	<c:otherwise>
		<li class="nav-item">
			<a class="nav-link" href="register.jsp"><i class="fas fa-user-plus"></i> Sign up</a>
		</li>
	</c:otherwise>
</c:choose>