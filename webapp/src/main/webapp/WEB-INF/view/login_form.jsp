<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style>
body {
	background-image:
		url("/event-webapp/static-resources/images/login-background.jpg");
}
</style>
<div class="container">
	<div id="loginbox" style="margin-top: 50px;"
		class="mainbox col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
		<div class="panel panel-info">
			<div class="panel-heading">
				<div class="panel-title">
					<span><a href="#" id="signin-button" class="btn btn-default">Sign
							In!</a></span> <span class="pull-right"><a href="#"
						id="signup-button" class="btn btn-info">Sign Up!</a></span>
				</div>
			</div>

			<div style="padding-top: 30px" class="panel-body" id="login-form-div">

				<div style="display: none" id="login-alert"
					class="alert alert-danger col-sm-12"></div>

				<form class="form-horizontal" role="form"
					action='<c:url value='/login' />' method='POST'>
					<input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" />


					<div style="margin-bottom: 25px" class="input-group">
						<span class="input-group-addon"><i
							class="glyphicon glyphicon-user"></i></span> <input id="login-username"
							type="text" class="form-control" name="username" value=""
							placeholder="username or email">
					</div>

					<div style="margin-bottom: 25px" class="input-group">
						<span class="input-group-addon"><i
							class="glyphicon glyphicon-lock"></i></span> <input id="login-password"
							type="password" class="form-control" name="password"
							placeholder="password">
					</div>

					<!-- <div class="input-group">
						<div class="checkbox">
							<label> <input id="login-remember" type="checkbox"
								name="remember-me" value="1"> Remember me
							</label>
						</div>
					</div> -->
					<div style="margin-top: 10px" class="form-group pull-right">
						<!-- Button -->
						<div class="col-sm-12 controls">
							<button type="submit" class="btn btn-success btn-sm">Sign
								In</button>
						</div>
					</div>
				</form>
			</div>

			<div style="padding-top: 30px" class="panel-body"
				id="signup-form-div">

				<form class="form-horizontal" role="form" id="signup-form">
					<input type="hidden" id ="csrf_val" name="${_csrf.parameterName}"
						value="${_csrf.token}" />


					<div style="margin-bottom: 25px" class="input-group">
						<span class="input-group-addon"><i
							class="glyphicon glyphicon-user"></i></span> <input id="signup-username"
							type="text" class="form-control" name="username" value=""
							placeholder="username or email">
					</div>

					<div style="margin-bottom: 25px" class="input-group">
						<span class="input-group-addon"><i
							class="glyphicon glyphicon-lock"></i></span> <input
							id="signup-password-1" type="password" class="form-control"
							name="password-1" placeholder="password">
					</div>

					<div style="margin-bottom: 25px" class="input-group">
						<span class="input-group-addon"><i
							class="glyphicon glyphicon-lock"></i></span> <input
							id="signup-password-2" type="password" class="form-control"
							name="" placeholder="confirm password">
					</div>

					<div style="margin-bottom: 25px" class="input-group">
						<span class="input-group-addon"><i
							class="glyphicon glyphicon-lock"></i></span> <input id="signup-age"
							type="text" class="form-control" name="age" placeholder="age">
					</div>

					<div class="input-group">
						<div class="checkbox">
							<label> <input id="admin-check" type="checkbox"
								name="is-admin" value="1"> Is Admin?
							</label>
						</div>
					</div>
				</form>
				<div style="margin-top: 10px" class="form-group pull-right">
					<!-- Button -->
					<div class="col-sm-12 controls">
						<button id="signup-button-success" class="btn btn-success btn-sm">Sign
							Up!</button>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>

<script>
	var contextPath = "${pageContext.request.contextPath}";
	var error = false;
	<c:if test="${error == true}">
		error = true;
	</c:if>
</script>
<script
	src="${pageContext.request.contextPath}/static-resources/js/login.js"></script>
<script>