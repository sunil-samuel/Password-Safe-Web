<%@include file="/WEB-INF/views/include/directive/page.jsp"%>
<div class="center-all">
	<form class="login">
		<div class="form-pair">
			<div class="key">
				<s:message code="userId" />
			</div>
			<div class="value">
				<input type="text" name="userid">
			</div>
		</div>
		<div class="form-pair">
			<div class="key">
				<s:message code="pwd" />
			</div>
			<div class="value">
				<input type="password" name="pwd">
			</div>
		</div>
		<div class="button">
			<button class="submit-red">
				<s:message code="login" />
			</button>
		</div>
	</form>
</div>

<script type="text/javascript">
	$(function() {
		home.loginForm();
	});
</script>