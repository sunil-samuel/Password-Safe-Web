<%@include file="/WEB-INF/views/include/directive/page.jsp"%>
<form id="upload" method="post"
	action='<c:url value="/secure/import/upload"/>'
	enctype="multipart/form-data">
	<p>
		Please enter the password for the file: <input type='password'
			name='password'>
	</p>
	<p>
		<input type="file" name="safeFile">
	</p>
	<div class='fileList'></div>
	<div class='progress'></div>
	<p>
		<button class='import-button'>
			<s:message code="import.confirm" />
		</button>
	</p>
</form>
<script type="text/javascript">
	$(function() {
		home.upload();
	});
</script>