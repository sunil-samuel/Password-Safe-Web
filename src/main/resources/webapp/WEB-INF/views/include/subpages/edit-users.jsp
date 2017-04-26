<%@include file="/WEB-INF/views/include/directive/page.jsp"%>
<div class='edit-members'>
	<div class=''>
		<div class='nowrap entry-element entry-header action-header'>Action</div>
		<div class='nowrap entry-element entry-header name-header'>Name</div>
		<div class='nowrap entry-element entry-header email-header'>E-Mail</div>
		<div class='nowrap entry-element entry-header phone-header'>Phone
			Number</div>
		<div class='nowrap entry-element entry-header role-header'>Role</div>
	</div>
	<c:forEach var="member" items="${members}">
		<div class='entry-start member-${member.id}' data-id="${member.id}"
			data-email="${member.email}">
			<div class='nowrap action-element entry-element center'>
				<a href='/invalid' class='ignore-url edit-user'
					title='Edit ${member.lastName}, ${member.firstName}'><span
					class="ui-icon ui-icon-pencil"></span></a> <a href='/invalid'
					title='Delete  ${member.lastName}, ${member.firstName}'
					class='ignore-url delete-user'><span
					class="ui-icon ui-icon-trash"></span></a>
			</div>
			<div class='nowrap name-element entry-element'
				title="${member.lastName}, ${member.firstName}">${member.lastName},
				${member.firstName}</div>
			<div class='nowrap email-element entry-element'
				title="${member.email}">${member.email}</div>
			<div class='nowrap phone-element entry-element'
				title="${member.phoneNumber}">${member.phoneNumber}</div>
			<div class='nowrap role-element entry-element' title="${member.role}">${member.role}</div>
		</div>
	</c:forEach>
</div>
<script type="text/javascript">
	$(function() {
		home.editUsers();
	});
</script>