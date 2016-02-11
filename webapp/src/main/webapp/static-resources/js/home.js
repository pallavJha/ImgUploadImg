/**
 * JS File for home.jsp
 */
function sendLinkToServer() {
	$("#send_link").attr("disabled", "disabled");
	$.ajax({
		url : getContextPath() + "/app/url",
		async : true,
		type : "GET",
		data : {
			"url" : $("#urlHolder").val()
		},
		success : function(result) {
			debugger;
			console.log(result.length);
			var parsed = JSON.parse(result);
			var b = "";
			for(var x = 0; x<parsed.length ; x++){
				b = b + "<div class='row'>";
				b = b + "<div class='col-sm-offset-1 col-sm-4'>";
				b = b + parsed[x];
				b = b + "</div>";
				b = b + "<div class='col-sm-offset-2 col-sm-4'>";
				b = b + parsed[++x];
				b = b + "</div>";
				b = b + "<div class='col-sm-offset-1'>";
				b = b + "</div>";
				b = b + "</div>";
			}
			$("#imageHolder").append(b);
			$("#modalButton").click();
			$("#send_link").removeAttr("disabled");
			$.notify("Images!", { position:"botton left" }, "success");
		},
		error : function(jQXHR) {
			console.log(jQXHR);
			$.notify(jQXHR.responseText, "error");
			$("#send_link").removeAttr("disabled");
		}
	});
}

$(document).ready(function() {
	$("#modalButton").hide();
	$('#loading').hide();
	$("#send_link").click(function() {
		sendLinkToServer();
	});
	$("#urlHolder").keypress(function(event) {
		var keycode = (event.keyCode ? event.keyCode : event.which);
		if (keycode == '13') {
			$("#send_link").trigger("click");
		}
	});
	
	$(document).ajaxStart(function() {
	    $('#loading').show();
	});
	$(document).ajaxStop(function() {
	    $('#loading').hide();
	});
}());