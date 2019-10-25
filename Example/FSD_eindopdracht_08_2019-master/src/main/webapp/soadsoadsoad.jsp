<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" session="false" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:template pageTitle="S.O.A.D. x 3">
	<jsp:attribute name="navigation">
		<t:nav_login>
		</t:nav_login>
	</jsp:attribute>
	<jsp:body>
	<div class="container py-5">
		<div class="row">
			<div class="offset-md-2 col-md-8 offset-md-2">
				<div class="card bgHexagonLogo text-white bg-secondary">
					<div class="card-header bg-dark text-danger">
						<h4 class="card-title text-uppercase mt-1 mb-1">System of a Down <i class="fas fa-times"></i> 3</h4>
					</div>
					<div class="card-body">
						<div class="row text-center bg-secondary point_nine_opacity">
							<div class="col-lg-12 col-md-12 col-sm-12 col-12">
								<h3 class="text-warning">You really had to do it, didn't you...</h3>
								<h4 class="text-warning">Here it is then:</h4>
							</div>
						</div>
						<div class="row text-center">
							<div class="col-lg-12 col-md-12 col-sm-12 col-12">
								<audio controls loop preload="none">
									<source src="media/soad.mp3" type="audio/mpeg">
									Your browser (ergo you) sucks, get Firefox!
								</audio>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	</jsp:body>
</t:template>