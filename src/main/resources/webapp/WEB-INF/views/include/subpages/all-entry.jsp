<%@include file="/WEB-INF/views/include/directive/page.jsp"%>
<c:choose>
	<c:when test="${fn:length(entries) gt 0}">
		<div class='entries'>
			<div class=''>
				<div class='nowrap entry-element entry-header action-header'>Action</div>
				<div class='nowrap entry-element entry-header'>
					<s:message code="title" />
				</div>
				<div class='nowrap entry-element entry-header'>
					<s:message code="desc" />
				</div>
				<div class='nowrap entry-element entry-header username-header'>
					<s:message code="username" />
				</div>
				<div class='nowrap entry-element entry-header password-header'>
					<s:message code="pwd" />
				</div>
				<div class='nowrap entry-element entry-header url-data'>
					<s:message code="url" />
				</div>
			</div>
			<c:forEach items="${entries}" var="entry">
				<div class='entry-start category-${entry.id}' data-id="${entry.id}"
					data-title="${entry.title}">
					<div class='nowrap action-element entry-element center'>
						<a href='/invalid' class='view-entry' data-id="${entry.id}"
							title='View ${entry.title}'><span
							class="ui-icon ui-icon-info"></span></a>
						<c:if
							test="${sessionScope.loggedinUserInfo.role=='Admin' || sessionScope.loggedinUserInfo.role=='ReadWrite'}">
							<a href='/invalid' class='edit-entry' data-id="${entry.id}"
								title='Edit ${entry.title}'><span
								class="ui-icon ui-icon-pencil"></span></a>
							<a href='/invalid' data-id="${entry.id}"
								title='Delete  ${entry.title}' class='delete-entry'><span
								class="ui-icon ui-icon-trash"></span></a>
						</c:if>
					</div>
					<div title="${entry.title}" data-value="${entry.title}"
						class='nowrap title-element entry-element copyable'>
						<c:out escapeXml="false"
							value="${empty entry.title ? '&nbsp;' : entry.title}" />
					</div>
					<div title="${entry.description}" data-value="${entry.description}"
						class='nowrap description-element entry-element copyable'>
						<c:out escapeXml="false"
							value="${empty entry.description ? '&nbsp;' : entry.description}" />
					</div>
					<div title="${entry.username}" data-value="${entry.username}"
						class='nowrap username-element entry-element copyable'>
						<c:out escapeXml="false"
							value="${empty entry.username ? '&nbsp;' : entry.username}" />
					</div>
					<div data-value="${entry.password}"
						title="Double Click to Save Password to Clipboard"
						class='nowrap password-element entry-element copyable'>
						<c:out escapeXml="false"
							value="${empty entry.password ? '&nbsp;' : '*****'}" />
					</div>
					<div title="${entry.url}" data-value="${entry.url}"
						class='nowrap url-element entry-element copyable url-data last-element'>
						<c:choose>
							<c:when test="${empty entry.url}">
								<c:set var="url" value="&nbsp;" />
							</c:when>
							<c:otherwise>
								<c:set var="url">
									<a target="_blank" href='${entry.url}'>${entry.url}</a>
								</c:set>
							</c:otherwise>
						</c:choose>
						${url}
					</div>
				</div>
			</c:forEach>
		</div>
		<div class='message'>
			<s:message code="clipboard" />
		</div>
	</c:when>
	<c:otherwise>
		<div class='message'>
			<s:message code="noEntry" />
		</div>
	</c:otherwise>
</c:choose>
<script type="text/javascript">
	$(function() {
		home.allEntry();
	});
</script>