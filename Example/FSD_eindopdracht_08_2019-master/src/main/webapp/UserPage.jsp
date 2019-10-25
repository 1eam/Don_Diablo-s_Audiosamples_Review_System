<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%@ page import="com.diabolo.security.SessionValidator" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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

<c:set var="firstName"><%=firstName%></c:set>
<c:set var="lastName"><%=lastName%></c:set>
<c:set var="sessionID"><%=sessionID%></c:set>
<c:set var="userName"><%=userName%></c:set>
<c:set var="email"><%=email%></c:set>
<c:set var="userID"><%=userID%></c:set>
<c:set var="groupID"><%=groupID%></c:set>
<c:set var="groupName"><%=groupName%></c:set>

<t:template pageTitle="User Page">
	<jsp:attribute name="navigation">
		<t:nav_user pageType="user">
		</t:nav_user>
	</jsp:attribute>
	<jsp:body>
	<div class="container py-5">
		<div class="row">
			<div class="offset-md-2 col-md-8 offset-md-2">
				<div class="card text-white bg-secondary">
					<div class="card-header bg-dark text-white">
						<h4 class="card-title text-uppercase mt-1 mb-1"><i class="fas fa-cat"></i> Welcome ${firstName} ${lastName}</h4>
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
									Session ID: ${sessionID}
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-6 col-sm-6 col-xs-12">
								<div class="float-left">
									Username: ${userName}
								</div>
							</div>
							<div class="col-md-6 col-sm-6 col-xs-12">
								<div class="float-right">
									Email: ${email}
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-4 col-sm-4 col-xs-12">
								<div class="float-left">
									User ID: ${userID}
								</div>
							</div>
							<div class="col-md-4 col-sm-4 col-xs-12">
								<div class="float-center">
									Group ID: ${groupID}
								</div>
							</div>
							<div class="col-md-4 col-sm-4 col-xs-12">
								<div class="float-right">
									Group name: ${groupName}
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	</jsp:body>
</t:template>