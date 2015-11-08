<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/static-resources/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/static-resources/bootstrap/3.3.5/css/bootstrap-theme.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/static-resources/animate/animate.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/static-resources/css/basic.css">
<script
	src="${pageContext.request.contextPath}/static-resources/jquery/jquery-2.1.4.js"></script>
<script
	src="${pageContext.request.contextPath}/static-resources/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script
	src="${pageContext.request.contextPath}/static-resources/js/main.js"></script>
<script
	src="${pageContext.request.contextPath}/static-resources/notify/notify.min.js"></script>
<script>
function getContextPath(){
	return "${pageContext.request.contextPath}";
}
</script>
<style>
</style>
</head>
<body>
	<tiles:insertAttribute name="header" />
	<tiles:insertAttribute name="menu" />
	<div class="container top-buffer-header">
		<tiles:insertAttribute name="body" />
	</div>
	<tiles:insertAttribute name="footer" />
</body>
</html>
