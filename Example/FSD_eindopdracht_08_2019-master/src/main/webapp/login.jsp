<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template pageTitle="Log in Page">
	<jsp:attribute name="navigation">
		<t:nav_login pageType="login">
		</t:nav_login>
	</jsp:attribute>
	<jsp:body>
	<div class="container py-5">
		<div class="row">
			<div class="offset-lg-3 offset-md-2 col-lg-6 col-md-8 offset-lg-3 offset-md-2">
				<div class="card bgHexagonLogo text-white bg-secondary">
					<div class="card-header bg-dark text-white">
						<h4 class="card-title text-uppercase mt-1 mb-1"><i class="fas fa-user-lock"></i> Log in</h4>
					</div>
					<div class="card-body">
						<form id="login_form" class="needs-validation" action="LoginServlet" method="post" novalidate>
							<div class="row login_row">
								<div class="col-lg-8 col-md-10 col-sm-8 col-12">
									<div class="form-label-group">
										<!-- todo: update pattern -->
										<input type="text" id="username" name="user" pattern="(?!(?:[^0-9]*[0-9]){4}|(?:[^\.]*[\.]){2})^[A-Za-z]{1}[A-Za-z0-9\.]{4,38}?[^\.]{1}$" maxlength="40" placeholder="Username" class="form-control" title="Fill in your username" required />
										<label for="username">Username</label>
										<div class="invalid-tooltip">
											Please enter a valid username.
										</div>
									</div>
								</div>
							</div>
							<div class="row login_row">
								<div class="col-lg-8 col-md-10 col-sm-8 col-12">
									<div class="form-label-group">
										<!-- todo: update pattern pattern="[A-Za-z0-9]{4,}"-->
										<input type="password" id="password" name="pwd"  placeholder="Password" class="form-control" title="Fill in your password" required />
										<label for="password">Password</label>
										<div class="invalid-tooltip">
											Please enter a valid password.
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-8 col-sm-8 col-12">
									<div class="float-left col-12">
										<a class="text-warning" href="password.jsp">Forgot password?</a>
									</div>
									<div class="float-left col-12">
										<a class="text-warning" href="register.jsp">Sign up</a>
									</div>
								</div>
								<div class="col-md-4 col-sm-4 col-12">
									<div class="float-sm-right">
										<i id="button_icon" class="fas fa-lock"></i>
										<button id="login_button" class="btn btn-dark rounded-1" type="submit" disabled>Log in</button>
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