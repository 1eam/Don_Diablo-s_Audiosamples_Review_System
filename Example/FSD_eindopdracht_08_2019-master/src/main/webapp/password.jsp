<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template pageTitle="Password Reset">
	<jsp:attribute name="navigation">
		<t:nav_login>
		</t:nav_login>
	</jsp:attribute>
	<jsp:body>
	<div class="container py-5">
		<div class="row">
			<div class="offset-lg-3 offset-md-2 col-lg-6 col-md-8 offset-lg-3 offset-md-2">
				<div class="card bgHexagonLogo text-white bg-secondary">
					<div class="card-header bg-dark text-white">
						<h4 class="card-title text-uppercase mt-1 mb-1"><i class="fas fa-user-injured"></i> Forgot password</h4>
					</div>
					<div class="card-body">
						<form id="login_form" class="needs-validation" action="PasswordResetServlet" method="post" novalidate>
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
										<!-- todo: update pattern -->
										<input type="email" id="email" name="email" pattern="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?" placeholder="Email" class="form-control" title="Fill in your email address" required />
										<label for="email">Email</label>
										<div class="invalid-tooltip">
											Please enter a valid email address.
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="offset-md-7 offset-sm-8 col-md-5 col-sm-4 col-12">
									<div class="float-sm-right">
										<i id="button_icon" class="fas fa-lock"></i>
										<button id="login_button" class="btn btn-dark rounded-1" type="submit" disabled>Reset password</button>
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