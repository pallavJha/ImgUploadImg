<div class="row">
	<div class="col-sm-offset-3 col-sm-6">
		<input id="search-input" type="text" class="form-control" name="name" placeholder="Search Here">
	</div>
</div>
<script>
$("#search-input").change(function(){
	$.ajax({
		url : "${pageContext.request.contextPath}/app/base/search-main",
		data : "toSearch="+$("#search-input").val(),
		type : "GET",
		async : false,
		success : function(result) {
			console.log(result);
		}
	});
});
</script>