<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template pageTitle="Sign up for Don Diablo">
	<jsp:attribute name="navigation">
		<t:nav_login pageType="register">
		</t:nav_login>
	</jsp:attribute>
	<jsp:attribute name="extraJS">
		<script src="resources/password.js"></script>
		<script src="resources/ajax.js"></script>
	</jsp:attribute>
	<jsp:body>
	<div class="container py-5">
		<div class="row">
			<div class="offset-lg-3 offset-md-2 col-lg-6 col-md-8 offset-lg-3 offset-md-2">
				<div class="card bgHexagonLogo text-white bg-secondary">
					<div class="card-header bg-dark text-white">
						<h4 class="card-title text-uppercase mt-1 mb-1"><i class="fas fa-user-plus"></i> Create your account</h4>
					</div>
					<div class="card-body">
						<form id="signup_form" class="needs-validation" action="CreateNewUser" method="post" novalidate>
							<div class="row login_row">
								<div class="col-lg-8 col-md-10 col-sm-8 col-12">
									<div class="form-label-group">
										<!-- todo: update pattern -->
										<input type="email" id="email" name="email" pattern="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?" maxlength="100" placeholder="Email" class="form-control" title="Fill in your email address" required />
										<label for="email">Email</label>
										<div class="invalid-tooltip">
											Please enter a valid email address.
										</div>
									</div>
								</div>
							</div>
							<div id="form_section2" class="d-none">
								<div class="row login_row">
									<div class="col-lg-8 col-md-10 col-sm-8 col-12">
										<div class="form-label-group">
											<!-- todo: update pattern -->
											<input type="text" id="username" name="user" pattern="(?!(?:[^0-9]*[0-9]){4}|(?:[^\.]*[\.]){2})^[A-Za-z]{1}[A-Za-z0-9\.]{4,38}?[^\.]{1}$" maxlength="40" placeholder="Username" class="form-control" title="Fill in your username" required />
											<label for="username">Username</label>
											<div class="invalid-tooltip">
												6-40 characters, maximum 3 numbers.
											</div>
										</div>
									</div>
								</div>
								<div class="row login_row">
									<div class="col-lg-8 col-md-10 col-sm-8 col-12">
										<div class="form-label-group">
											<!-- todo: update pattern -->
											<input type="password" id="newpassword" name="newpassword" pattern="^(?!(?:[^0-9]*[0-9]){4}|(?:[^!@#$%&_*\-?]*[!@#$%&_*\-?]){3})(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%&_*\-?])(?:[\da-zA-Z!@#$%&_*\-?]){10,32}$" maxlength="32" placeholder="Password" class="form-control" title="Fill in your password" required />
											<label for="newpassword">Password</label>
											<div class="invalid-tooltip">
												Please enter a valid password.
											</div>
										</div>
									</div>
								</div>
								<div class="row login_row">
									<div class="col-lg-8 col-md-10 col-sm-8 col-12">
										<div class="form-label-group">
											<!-- todo: update pattern -->
											<input type="password" id="newpassword2" name="newpassword2" pattern="^(?!(?:[^0-9]*[0-9]){4}|(?:[^!@#$%&_*\-?]*[!@#$%&_*\-?]){3})(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%&_*\-?])(?:[\da-zA-Z!@#$%&_*\-?]){10,32}$" maxlength="32" placeholder="Confirm password" class="form-control" title="Confirm your password" />
											<label for="newpassword2">Confirm password</label>
											<div class="invalid-tooltip">
												Passwords do not match.
											</div>
										</div>
									</div>
								</div>
								<div class="row login_row">
									<div class="col-lg-8 col-md-10 col-sm-8 col-12">
										<div class="form-label-group">
											<!-- todo: update pattern -->
											<input type="text" id="firstname" name="firstname" pattern="^[\w'][^0-9\s\x22\-,._!¡?÷?¿/\\+=@#$%ˆ&*(){}|~<>;:\[\]]{1,39}$" maxlength="50" placeholder="First name" class="form-control" title="Enter your first name" required />
											<label for="firstname">First name</label>
											<div class="invalid-tooltip">
												Please enter your first name.
											</div>
										</div>
									</div>
								</div>
								<div class="row login_row">
									<div class="col-lg-8 col-md-10 col-sm-8 col-12">
										<div class="form-label-group">
											<!-- todo: update pattern -->
											<input type="text" id="lastname" name="lastname" pattern="^(?!.*\s{2})([\w'])[^0-9\t\n\r\f\v\x22\-,._!¡?÷?¿/\\+=@#$%ˆ&*(){}|~<>;:\[\]]{1,38}?[^\s]{1}$" maxlength="50" placeholder="Last name" class="form-control" title="Enter your last name" required />
											<label for="lastname">Last name</label>
											<div class="invalid-tooltip">
												Please enter your last name.
											</div>
										</div>
									</div>
								</div>
								<div class="row login_row">
									<div class="col-lg-8 col-md-10 col-sm-8 col-12">
										<div class="form-label-group">
											<select id="country" name="country" required>
											</select>
											<div class="invalid-tooltip">
												Please select your country.
											</div>
										</div>
									</div>
								</div>
							</div>
							<div id="next_button_div" class="row">
								<div class="col-md-10 col-sm-10 col-12 bg-secondary point_nine_opacity">
									<small class="float-left not_active">
										By clicking next, you agree to our <a class="text-warning" href="#" target="_blank">Terms</a> and that you have read our <a class="text-warning" href="#" target="_blank">Privacy Policy</a> and <a class="text-warning" href="#" target="_blank">Content Policy</a>.
									</small>
								</div>
								<div class="col-md-2 col-sm-2 col-12">
									<div class="float-sm-right">
										<button id="next_button" class="btn btn-dark rounded-1" type="button">Next</button>
									</div>
								</div>
							</div>
							<div id="prev_submit_btn_row" class="row d-none">
								<div class="col-md-6 col-sm-6 col-12">
									<div class="float-left">
										<button id="previous_button" class="btn btn-dark rounded-1" type="button">Previous</button>
									</div>
								</div>
								<div class="col-md-6 col-sm-6 col-12">
									<div class="float-sm-right">
										<i id="button_icon" class="fas fa-lock"></i>
										<button id="submit_button" class="btn btn-dark rounded-1" type="submit">Submit</button>
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