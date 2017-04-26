<%@include file="/WEB-INF/views/include/directive/page.jsp"%>
<form:form method="POST" action="/update" modelAttribute="member"
	class='my-account-form'>
	<form:hidden path="id" />
	<div class='my-account entry-view glow-shadow'>
		<!-- First Name -->
		<div class='key-value glow-shadow even'>
			<div class='key'>First Name</div>
			<div class='value'>
				<form:input path="firstName" />
			</div>
		</div>
		<!-- Last Name -->
		<div class='key-value glow-shadow odd'>
			<div class='key'>Last Name</div>
			<div class='value'>
				<form:input path="lastName" />
			</div>
		</div>
		<!-- E-Mail -->
		<div class='key-value glow-shadow even'>
			<div class='key'>E-Mail</div>
			<div class='value'>
				<form:input path="email" />
			</div>
		</div>
		<!-- Phone Number -->
		<div class='key-value glow-shadow odd'>
			<div class='key'>Phone Number</div>
			<div class='value'>
				<form:input path="phoneNumber" />
			</div>
		</div>
		<!-- Password -->
		<div class='key-value glow-shadow even'>
			<div class='key'>Password</div>
			<div class='value'>
				<form:password path="password" />
			</div>
		</div>
		<!-- Re-enter Password -->
		<div class='key-value glow-shadow odd'>
			<div class='key'>Re-enter Password</div>
			<div class='value'>
				<form:password path="passwordVerify" />
			</div>
		</div>
		<!-- Role -->
		<div class='key-value glow-shadow even'>
			<div class='key'>Role</div>
			<div class='value'>
				<c:forEach var="role" items="${roles}">
					<c:set var="checked" value="" />
					<c:if test="${member.role == role}">
						<c:set var="checked" value="checked" />
					</c:if>
					${role}:<input type="radio" name="role" value="${role}" ${checked}>
					<br>
				</c:forEach>
			</div>
		</div>
	</div>
</form:form>