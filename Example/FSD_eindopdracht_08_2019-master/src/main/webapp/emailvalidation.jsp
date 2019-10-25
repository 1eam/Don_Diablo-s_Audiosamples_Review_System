<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template pageTitle="Validate Email">
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
						<h4 class="card-title text-uppercase mt-1 mb-1"><i class="fas fa-envelope"></i> Validate email</h4>
					</div>
					<div class="card-body">
						<form id="login_form" class="needs-validation" action="ValidateEmail" method="post" novalidate>
							<div class="row login_row">
								<div class="col-lg-8 col-md-10 col-sm-8 col-12">
									<div class="form-label-group">
										<!-- todo: update pattern -->
										<input type="text" id="validation" name="code" pattern="^[A-Za-z0-9]{100}$" maxlength="100" placeholder="Validation code" class="form-control" title="Fill in the validation code" required />
										<label for="validation">Validation code</label>
										<div class="invalid-tooltip">
											Please enter a valid code.
										</div>
									</div>
								</div>
							</div>
							<div class="row">
								<div class="offset-md-7 offset-sm-8 col-md-5 col-sm-4 col-12">
									<div class="float-sm-right">
										<i id="button_icon" class="fas fa-lock"></i>
										<button id="login_button" class="btn btn-dark rounded-1" type="submit" disabled>Validate</button>
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