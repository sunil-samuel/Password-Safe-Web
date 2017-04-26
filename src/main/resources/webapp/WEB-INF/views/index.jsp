<%@include file="/WEB-INF/views/include/directive/page.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title><s:message code="appName" /></title>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/jquery-ui/jquery-ui.min.css"/>" />

<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/smartmenus/css/sm-core-css.css"/>" />

<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/smartmenus/css/sm-simple/sm-simple.css"/>" />

<link rel="stylesheet" type="text/css"
	href="<c:url value="/resources/home/css/home.css"/>" />
<link rel="stylesheet" type="text/css" id="dynamic-sheet"
	href="<c:url value="/resources/home/css/light-gray.css"/>" />

<!-- Javascript -->
<script type="text/javascript"
	src="<c:url value="/resources/jquery/js/jquery-3.1.1.min.js"/>"></script>

<script src="/resources/jquery-ui/js/jquery-ui.min.js"></script>

<script type="text/javascript"
	src="<c:url value="/resources/smartmenus/jquery.smartmenus.min.js"/>"></script>

<script type="text/javascript"
	src="<c:url value="/resources/upload/js/jquery.ui.widget.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/resources/upload/js/jquery.iframe-transport.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/resources/upload/js/jquery.fileupload.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/resources/upload/js/jquery.fileupload-jquery-ui.js"/>"></script>

<script type="text/javascript" src="<c:url value="/js"/>"></script>

<script type="text/javascript"
	src="<c:url value="resources/home/js/jquery-ui.dialog.js"/>"></script>

<script type="text/javascript"
	src="<c:url value="resources/home/js/home.js"/>"></script>
<script type="text/javascript">
	$(function() {
		home.index("<c:url value="/"/>");
	});
</script>
</head>
<body>
	<div class="title">
		<s:message code="appName" />
	</div>
	<div class='config-area'>
		<div class='language-roller config-roller'>
			Languages: <select class='language-dropdown'>
				<option value="en"
					<c:out value="${language == 'en' ? 'selected' : ''}" />>English</option>
				<option value="de"
					<c:out value="${language == 'de' ? 'selected' : ''}" />>Deutsche</option>
				<option value="ml"
					<c:out value="${language == 'ml' ? 'selected' : ''}" />>മലയാളം</option>
			</select>
		</div>
		<div class='vertical-bar config-roller'>&#124;</div>
		<div class='theme-roller config-roller'>
			Themes: <select class='theme-dropdown'>
				<option value="light-gray">light-gray</option>
				<option value="light-red">light-red</option>
				<option value="blue-circle">blue-circle</option>
			</select>
		</div>
	</div>
	<div class='main-widget'>
		<c:import url="${url}" />
	</div>
	<div class='footer-widget'>
		Build #:
		<s:message code="build.timestamp" />
	</div>
</body>
</html>