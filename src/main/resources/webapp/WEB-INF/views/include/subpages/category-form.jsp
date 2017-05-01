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
			<!-- Title -->
			<div class='key-value glow-shadow even'>
				<div class='key'>
					<label for="title"><s:message code="title" /></label>
				</div>
				<div class='value'>
					<form:input path="title" />
				</div>
			</div>
			<!-- Description -->
			<div class='key-value glow-shadow even'>
				<div class='key'>
					<label for="description"><s:message code="desc" /></label>
				</div>
				<div class='value'>
					<form:input path="description" />
				</div>
			</div>
			<!-- notes -->
			<div class='key-value glow-shadow even notes'>
				<div class='key'>
					<label for="notes"><s:message code="notes" /></label>
				</div>
				<div class='value'>
					<form:textarea path="notes" cssClass="notes" />
				</div>
			</div>
		</fieldset>
	</form:form>
</div>