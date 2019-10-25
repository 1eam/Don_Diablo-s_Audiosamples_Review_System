<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template pageTitle="Two Factor Authentication(2FA) Page">
	<jsp:attribute name="navigation">
		<t:nav_2falogin>
		</t:nav_2falogin>
	</jsp:attribute>
	<jsp:body>
	<div class="container py-5">
		<div class="row">
			<div class="offset-md-3 col-md-6 offset-md-3">
				<div class="card bgHexagonLogo text-white bg-secondary">
					<div class="card-header bg-dark text-white">
						<h4 class="card-title text-uppercase mt-1 mb-1"><i class="fas fa-mobile-alt"></i> Two Factor Authentication</h4>
					</div>
					<div class="card-body">
						<form id="login_form" class="needs-validation" action="TwoFactorServlet" method="post" novalidate>
							<div class="row">
								<div class="col-lg-8 col-md-10 col-sm-8 col-12">
									<div class="form-label-group">
										<input type="text" class="form-control" id="username" name="code" pattern="^\d{6}$" maxlength="6" placeholder="123456" title="Fill in your 6-digit 2FA code" required />
										<label for="username">Code (6-digit number)</label>
										<div class="invalid-tooltip">
											Please enter a 6-digit number code.
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="col-md-12 col-sm-12 col-12">
									<div class="float-right bg-secondary">
										<i id="button_icon" class="fas fa-lock"></i>
										<button id="login_button" class="btn btn-dark rounded-1" type="submit" disabled>Authenticate</button>
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