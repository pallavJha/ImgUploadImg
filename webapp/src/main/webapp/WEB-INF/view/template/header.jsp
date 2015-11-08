<nav class="navbar navbar-default navbar-fixed-top">
	<div class="navbar-header">
		<button type="button" class="navbar-toggle collapsed"
			data-toggle="collapse" data-target="#navbar" aria-expanded="false"
			aria-controls="navbar">
			<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span>
			<span class="icon-bar"></span> <span class="icon-bar"></span>
		</button>
		<a class="navbar-brand" href="#"><strong><span
				class="glyphicon glyphicon-piggy-bank"></span></strong></a>
	</div>
	<div id="navbar" class="collapse navbar-collapse">
		<ul class="nav navbar-nav">
			<li><a href="${pageContext.request.contextPath}/app/base/"><strong><span
						class="glyphicon glyphicon-home"></span>Home</strong></a></li>
			<li><a
				href="${pageContext.request.contextPath}/app/base/new-student"><strong><span
						class="glyphicon glyphicon-plus"></span>Add</strong></a></li>
			<li><a
				href="${pageContext.request.contextPath}/app/base/search-page"><strong><span
						class="glyphicon glyphicon-search"></span>Search</strong></a></li>

		</ul>
		<ul class="nav navbar-nav pull-right">
			<li class="dropdown"><a href="#" class="dropdown-toggle"
				data-toggle="dropdown" role="button" aria-haspopup="true"
				aria-expanded="false"><strong><span
						class="glyphicon glyphicon-flag"></span>Settings</strong><span
					class="caret"></span></a>
				<ul class="dropdown-menu">
					<li><a href="#" id="userModalLink" data-toggle="modal"
						data-target="#userModal">Users</a></li>
					<li><a href="${pageContext.request.contextPath}/app/admin">Admin</a></li>

					<li role="separator" class="divider"></li>
					<li class="dropdown-header">Settings</li>
				</ul></li>

			<li class="dropdown"><a href="#" class="dropdown-toggle"
				data-toggle="dropdown" role="button" aria-haspopup="true"
				aria-expanded="true"><strong><span
						class="glyphicon glyphicon-user"></span><span id="username">Username</span></strong><span
					class="caret"></span></a>
				<ul class="dropdown-menu menu-left">
					<li><a href="${pageContext.request.contextPath}/app/logout">Log
							Out</a></li>
					<li role="separator" class="divider"></li>
					<li class="dropdown-header">Profile Settings</li>
				</ul></li>
		</ul>
	</div>
	<!--/.nav-collapse -->
</nav>
<!-- Trigger the modal with a button -->

<!-- Modal -->
<div id="userModal" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<input type="hidden" name="${_csrf.parameterName}"
			value="${_csrf.token}" id="_csrf" />
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">User View</h4>
			</div>
			<div class="modal-body">
				<strong>User Creation Form</strong>
				<hr />
				<div class="row">
					<div class="row">
						<div class="col-sm-10">
							<form class="form-inline" role="form">
								<div class="form-group left-buffer">
									<input type="text" class="form-control" id="username"
										placeholder="Username">
								</div>
								<div class="form-group">
									<input type="password" class="form-control" id="password"
										placeholder="Password">
								</div>
								<div class="form-group">
									<input type="checkbox" id="adminCheck">Is Admin?
								</div>
							</form>
						</div>

						<div class="col-sm-2 left-pad-0">
							<button class="btn btn-default" id="saveuser">Create</button>
						</div>
					</div>

				</div>
				<hr />
				<div class="row">
					<div class="row">
						<div class=" col-sm-offset-4 col-sm-1">
							<button id="showUsers" class="btn btn-default">Show
								Users</button>
						</div>
					</div>
					<div class="row">
						<div class="allusers col-sm-offset-3 col-sm-4 top-buffer-sm"></div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			</div>
		</div>

	</div>
</div>
<script
	src="${pageContext.request.contextPath}/static-resources/js/user.js"></script>
