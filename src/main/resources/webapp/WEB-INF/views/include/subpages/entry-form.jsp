<%@include file="/WEB-INF/views/include/directive/page.jsp"%>
<div class="entry-create-form entry-view glow-shadow"
	title="Create Entry">
	<form:form method="POST" modelAttribute="entry" cssClass="create-entry">
		<fieldset>
			<legend>${legend}</legend>
			<form:hidden path="id" />
			<form:hidden path="parentCategoryId" />
			<div class='parent'>
				<s:message code="category" />
				: <span class='parent-category'>${category.title}</span>
			</div>
			<!-- Title -->
			<div class='key-value glow-shadow even'>
				<div class='value'>
					<form:input path="title" />
				</div>
				<div class='key'>
					<label for="title"><s:message code="title" /></label>
				</div>
			</div>
			<!-- Description -->
			<div class='key-value glow-shadow even'>
				<div class='value'>
					<form:input path="description" />
				</div>
				<div class='key'>
					<label for="description"><s:message code="desc" /></label>
				</div>
			</div>
			<!-- Username -->
			<div class='key-value glow-shadow even'>
				<div class='value'>
					<form:input path="username" />
				</div>
				<div class='key'>
					<label for="username"><s:message code="username" /></label>
				</div>
			</div>
			<!-- Password -->
			<div class='key-value glow-shadow even'>
				<div class='value'>
					<form:password path="password" cssClass="password-eye"
						showPassword="true" />

					<input type="text" name="password-text" value="${entry.password}"
						class="hide password-eye-text"> <span class="eye-icon"></span>
				</div>
				<div class='key'>
					<label for="password"><s:message code="pwd" /></label>
				</div>
			</div>
			<!-- url -->
			<div class='key-value glow-shadow even'>
				<div class='value'>
					<form:input path="url" />
				</div>
				<div class='key'>
					<label for="url"><s:message code="url" /></label>
				</div>
			</div>
			<!-- notes -->
			<div class='key-value glow-shadow even notes'>
				<div class='value'>
					<form:textarea path="notes" cssClass="notes" />
				</div>
				<div class='key'>
					<label for="notes"><s:message code="notes" /></label>
				</div>
			</div>
			<!-- Allow form submission with keyboard without duplicating the dialog button -->
			<input type="submit" tabindex="-1"
				style="position: absolute; top: -1000px">
		</fieldset>
	</form:form>
</div>
<script type="text/javascript">
	$(function() {
		home.createPasswordEye();
	});
</script>
