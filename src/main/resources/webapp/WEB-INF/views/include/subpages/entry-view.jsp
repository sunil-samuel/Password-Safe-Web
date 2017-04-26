
<%
	pageContext.setAttribute("nlChar", "\n");
%>
<%@include file="/WEB-INF/views/include/directive/page.jsp"%>
<div class='entry-view glow-shadow'>
	<!-- id -->
	<div class='key-value'>
		<div class='value copyable' data-value="${entry.id}">${entry.id}</div>
		<div class='key'>
			<s:message code="id" />
		</div>
	</div>
	<!-- title -->
	<div class='key-value'>
		<div class='value copyable' data-value="${entry.title}">${entry.title}</div>
		<div class='key'>
			<s:message code="title" />
		</div>
	</div>
	<!-- description -->
	<div class='key-value'>
		<div class='value copyable' data-value="${entry.description}">${fn:replace(entry.description, nlChar, "<br>")}</div>
		<div class='key'>
			<s:message code="desc" />
		</div>
	</div>
	<!-- username -->
	<div class='key-value'>
		<div class='value copyable' data-value="${entry.username}">${entry.username}</div>
		<div class='key'>
			<s:message code="username" />
		</div>
	</div>
	<!-- password -->
	<div class='key-value'>
		<div class='value copyable' data-value="${entry.password}">${entry.password}</div>
		<div class='key'>
			<s:message code="pwd" />
		</div>
	</div>
	<!-- url -->
	<div class='key-value'>
		<div class='value copyable' data-value="${entry.url}">
			<a target="_blank" href='${entry.url}'>${entry.url}</a>
		</div>
		<div class='key'>
			<s:message code="url" />
		</div>
	</div>
	<!-- notes -->
	<div class='key-value'>
		<div class='value copyable' data-value="${entry.notes}">${fn:replace(entry.notes, nlChar, "<br>")}</div>
		<div class='key'>
			<s:message code="notes" />
		</div>
	</div>
	<!-- created -->
	<div class='key-value'>
		<div class='value copyable' data-value="${entry.created}">${entry.created}</div>
		<div class='key'>
			<s:message code="created" />
		</div>
	</div>
	<!-- updated -->
	<div class='key-value'>
		<div class='value copyable' data-value="${entry.updated}">${entry.updated}</div>
		<div class='key'>
			<s:message code="updated" />
		</div>
	</div>
</div>
<div class='message'>
	<s:message code="clipboard" />
</div>
<script type="text/javascript">
	$(function() {
		home.entryView();
	});
</script>