<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class="form_div top-buffer row" align="center">
	<div class="col-md-12">
		<form class="form-horizontal" role="form" id="student-form">
			<input type="hidden" name="${_csrf.parameterName}"
				value="${_csrf.token}" />
			<div class="form-group row">
				<div class=' col-sm-offset-3 col-sm-1 '>
					<label class="control-label " for="email">Name:</label>
				</div>
				<div class="col-sm-5">
					<input type="text" class="form-control" name="name">
				</div>
			</div>
			<div class="form-group row">
				<div class="col-sm-1 col-sm-offset-3">
					<label class="control-label " for="pwd">Color:</label>
				</div>
				<div class="col-sm-5">
					<input type="text" class="form-control" name="schoolBag.color">
				</div>
			</div>
			<div class=" row form-group initail bottom-buffer">

				<div class="col-sm-offset-4 col-sm-5">
					<input type="text" class="form-control"
						name="subjects[0].subjectName" placeholder="Subject Name">
				</div>
				<div class=" col-sm-1">
					<button type="button" class="btn btn-default" id="addSubject"
						aria-label="Left Align">
						<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
					</button>
				</div>

			</div>
			<div class="form-group row top-buffer">
				<div class="col-sm-1 col-sm-offset-8">
					<input type="button" id="submit-button" value="Submit" />
				</div>
				<div class="col-sm-1">
					<input type="button" value="View Records" id="view-records" />
				</div>
			</div>
		</form>
	</div>
</div>

<div class="row top-buffer table-div">
	<table class="table table-striped">
		<thead>
			<th>Id</th>
			<th>Name</th>
			<th>Subjects</th>
			<th>Bag-Color</th>
		</thead>
		<tbody id="table-body">
		</tbody>
	</table>
</div>


<script type="text/javascript">
	var i = 0;
	$(document).ready(function() {
		$(".table-div").hide();
	});
	$("#addSubject")
			.click(
					function() {
						i = i + 1;
						var nextElem = "<div class='row top-buffer do-remove'><div class='col-sm-offset-4 col-sm-5'>"
								+ "<input type='text' class='form-control' name='subjects["+i+"].subjectName' placeholder='Subject Name'>"
								+ "</div>" + "</div>";
						$(".initail").after(nextElem);
					});

	$("#submit-button").click(function() {
		$.ajax({
			url : "${pageContext.request.contextPath}/app/base/save-student",
			data : $("#student-form").serialize(),
			type : "POST",
			async : false,
			success : function(result) {
				console.log(result);
				$(".form_div input").each(function() {
					$(this).val("");
					$("#submit-button").val("Submit");
					$("#view-records").val("View Records");
				});
				$(".do-remove").each(function() {
					$(this).remove();
				});
				$.notify("Student Added!", "success");
				setTimeout(function(){
					location.reload();	
				}, 1500);
				
			},
			error:function(){
				$.notify("Error!", "error");
			}
		});
	});

	$("#view-records")
			.click(
					function() {
						$
								.ajax({
									url : "${pageContext.request.contextPath}/app/base/view-all-students",
									async : false,
									success : function(result) {
										console.log(result);
										$("#table-body").html("");
										var obj = jQuery.parseJSON(result);
										for (i = 0; i < obj.length; i++) {
											var temp = obj[i];
											var row = "<tr><td>" + temp['id']
													+ "</td><td>"
													+ temp['name']
													+ "</td><td>"
													+ temp['subjects-length']
													+ "</td><td>"
													+ temp['bagColor']
													+ "</td></tr>";
											$("#table-body").append(row);
										}
										$(".table-div").show();
										$.notify("Table created!", "info");
									}
								});
					});
</script>