<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%@ page import="com.diabolo.security.SessionValidator" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
//allow access only if session exists
HttpSession session = null;
Cookie[] cookies = null;

SessionValidator validator = new SessionValidator(request, response);
if (validator.isValidSession()) {
    if (validator.isValidCookies()) {
        session = validator.getSession();
        cookies = validator.getCookies();
    }
}
%>

<t:template pageTitle="Demo drop">
	<jsp:attribute name="navigation">
		<t:nav_user pageType="demo">
		</t:nav_user>
	</jsp:attribute>
	<jsp:attribute name="extraJS">	
		<script src="resources/dragndrop.js"></script>
	</jsp:attribute>
	<jsp:body>
	<div class="container py-5">
		<div class="row">
			<div class="offset-lg-3 offset-md-2 col-lg-6 col-md-8 offset-lg-3 offset-md-2">
				<div class="card bgHexagonLogo text-white bg-secondary">
					<div class="card-header bg-dark text-white">
						<h4 class="card-title text-uppercase mt-1 mb-1"><i class="fas fa-upload"></i> Demo drop</h4>
					</div>
					<div id="form_card_body" class="card-body">
						<form id="demo_form" class="needs-validation" action="" method="post" novalidate>
							<div class="row login_row">
								<div class="col-lg-8 col-md-10 col-sm-8 col-12">
									<div class="form-label-group">
										<input type="text" id="title" name="title" pattern="" maxlength="50" placeholder="Title" class="form-control" title="Title of the demo" required />
										<label for="title">Title</label>
										<div class="invalid-tooltip">
											Give your demo a title.
										</div>
									</div>
								</div>
							</div>
							<div class="row login_row">
								<div class="col-lg-8 col-md-10 col-sm-8 col-12">
									<div class="custom-file">
										<input type="file" id="file_selector" name="file" class="custom-file-input" accept=".mp3,.ogg,.wav" required />
										<label class="custom-file-label" for="file_selector" data-browse="Select demo">Select your demo...</label>
										<div class="invalid-tooltip">
											Please select your demo file to upload.
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-4">
									<div id="dragandrophandler" class="card mb-4 bg-secondary text-white point_nine_opacity">									
										<svg class="bd-placeholder-img card-img-top" width="100%" height="225" xmlns="http://www.w3.org/2000/svg" preserveAspectRatio="xMidYMid slice" focusable="false" role="img" aria-label="Placeholder: Drag & Drop Demo Here">
											<title>Placeholder</title>
											<rect width="100%" height="100%" fill="#55595c"/>
											<text x="50%" y="50%" fill="#eceeef" dy=".3em">Drag & Drop Demo Here</text>
										</svg>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="offset-md-7 offset-sm-8 col-md-5 col-sm-4 col-12">
									<div class="float-sm-right">
										<i id="button_icon" class="fas fa-lock"></i>
										<button id="file_button" class="btn btn-dark rounded-1" type="button" disabled>Drop demo</button>
									</div>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	</jsp:body>
</t:template>