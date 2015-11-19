/**
 * JS File for home.jsp
 */
function sendLinkToServer() {
	$.ajax({
		url : getContextPath() + "/app/url",
		async : false,
		type : "GET",
		data : {
			"url" : $("#urlHolder").val()
		},
		success : function(result) {
			console.log(result);
		},
		error : function(jQXHR) {
			$.notify("Error in getting username!", "error");
		}
	});
}

$(document).ready(function() {
	$("#send_link").click(function() {
		sendLinkToServer();
	});
	$("#urlHolder").keypress(function(event) {
		var keycode = (event.keyCode ? event.keyCode : event.which);
		if (keycode == '13') {
			$("#send_link").trigger("click");
		}
	});
}());