/**
 * Script for User functions
 */
$(document).ready(function(){
	getUserName();
});
function getUserName(){
		$.ajax({
			url     : getContextPath()+"/app/User/getUserName",
			async   : false,
			type    : "GET",
			success : function(result) {
				$("#username").html(result);
			},
			error   : function(){
				$("#username").html(result);
				$.notify("Error in getting username!", "error");
			}
		});
}
$("#saveuser").click(function(){
	var adminCheck = $("#adminCheck").is(':checked'); 
	$.ajax({
		url : getContextPath()+"/app/User/save-user",
		async : false,
		type : "POST",
		data: {
				"username"  : $("#username").val(),
				"password"  : $("#password").val(),
				"adminCheck": adminCheck,
				"_csrf"     : $("#_csrf").val()
			  },
		success : function(result) {
			$.notify("User created!", "success");
			$("#username").val("");
			$("#password").val("");
		},
		error : function(){
			$.notify("Unable to create new user!", "error");
		}
			 
	});
});

$("#showUsers").click(function(){
			$.ajax({
					url : getContextPath()+"/app/User/getAllUsers",
					async : false,
					type : "POST",
					data : {
							"_csrf"    : $("#_csrf").val()
							},
					success : function(result) {
							$(".allusers").html("");
							var data = "<ul class='list-group'>";
							var obj = jQuery.parseJSON(result);
							for (i = 0; i < obj.length; i++) {
								var temp = obj[i];
								console.log(temp);
								var row = "<li class='list-group-item'>"
										+ temp['username']
										+ "</li>";
								data = data + row;
							}
							data = data + "</ul>"
							$(".allusers").append(data);
							$.notify("Username gathered!", "info");
						},
					error:function(){
							$.notify("Error while fetching username!", "error");
						}
					});
		});