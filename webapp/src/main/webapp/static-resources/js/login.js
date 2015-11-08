/**
 * Script for Login functions
 */

$(document).ready(function() {
	if (error == true) {
		$.notify("SignIn Error!", "error");
	}
	$("#signup-form-div").hide();
});
$("#signin-button").click(function(){
	$("#signup-form-div").hide("fast");
	$("#login-form-div").show("fast");
});

$("#signup-button").click(function(){
	$("#login-form-div").hide("fast");
	$("#signup-form-div").show("fast");
});

$("#signup-button-success").click(function(){
	var password1 = $("#signup-password-1").val();
	var password2 = $("#signup-password-2").val();
	if(password1 != password2)
	{
		$.notify("Passwords should match.", "error");
		return;
	}
	var adminCheck = $("#admin-check").is(':checked'); 
	console.log("adminCheck : ",adminCheck);
	$.ajax({
			url : contextPath+"/app/signup/create",
			data : {
				username   : $("#signup-username").val(),
				password   : $("#signup-password-1").val(),
				adminCheck : adminCheck,
				age        : $("#signup-age").val(),
				_csrf      : $("#csrf_val").val()
			},
			type : "POST",
			async : false,
			success : function(result) {
				$.notify("Student Added!", "success");
				var returnObj = $.parseJSON(result);
				if(returnObj["access-to-index"] == "true" && returnObj["u-d"] == "true")
				{
					var hostname = "base/";
					window.location.replace(hostname);
				}
			},
			error:function(){
				$.notify("Error!", "error");
			}
	});
});