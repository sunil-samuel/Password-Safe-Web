<%@include file="/WEB-INF/views/include/directive/page.jsp"%>
<div class='category-view glow-shadow'>
	<!-- title -->
	<div class='key-value'>
		<div class='key'>
			<s:message code="title" />
		</div>
		<div class='value' data-value="${category.title}">${category.title}</div>
	</div>
	<!-- description -->
	<div class='key-value'>
		<div class='key'>
			<s:message code="desc" />
		</div>
		<div class='value' data-value="${category.description}">${category.description}</div>
	</div>
	<!-- notes -->
	<div class='key-value'>
		<div class='key'>
			<s:message code="notes" />
		</div>
		<div class='value' data-value="${category.notes}">${category.notes}</div>
	</div>
	<!-- created -->
	<div class='key-value'>
		<div class='key'>
			<s:message code="created" />
		</div>
		<div class='value' data-value="${category.created}">${category.created}</div>
	</div>
	<!-- updated -->
	<div class='key-value'>
		<div class='key'>
			<s:message code="updated" />
		</div>
		<div class='value' data-value="${category.updated}">${category.updated}</div>
	</div>
</div>