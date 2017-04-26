<%@include file="/WEB-INF/views/include/directive/page.jsp"%>
<c:set var="task" value="${update ? 'Edit' : 'Create'} Category" />
<div class='category-create-form' title='${task}'>
	<form:form method="POST" modelAttribute="category" action="/invalid"
		cssClass="${update?'edit':'create'}-category">
		<fieldset>
			<legend>${task}</legend>
			<form:hidden path="parentId" />
			<form:hidden path="id" />
			<c:if test="${not empty parent}">
				<div class='parent-title'>Parent: ${parent.title}</div>
			</c:if>
			<div class='form-element-pair'>
				<label for="title"><s:message code="title" /></label>
				<form:input path="title" />
			</div>
			<div class='form-element-pair'>
				<label for="description"><s:message code="desc" /></label>
				<form:input path="description" />
			</div>
			<div class='form-element-pair'>
				<label for="notes"><s:message code="notes" /></label>
				<form:textarea path="notes" cssClass="notes" />
			</div>
		</fieldset>
	</form:form>
</div>