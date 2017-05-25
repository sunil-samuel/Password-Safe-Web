<%@include file="/WEB-INF/views/include/directive/page.jsp"%>
<div class="menu-widget">
	<div class='menu'>
		<ul id="main-menu" class="sm sm-simple nowrap">
			<li><a class='ignore-url' href=""><s:message code="file" /></a>
				<ul>
					<c:if
						test="${sessionScope.loggedinUserInfo.role=='Admin' || sessionScope.loggedinUserInfo.role=='ReadWrite'}">
						<li><a href="/invalid" class="import-link"><span
								class="ui-icon ui-icon-arrowreturn-1-e"></span> <s:message
									code="import" /> &hellip;</a></li>
					</c:if>
					<li><a href="/export" class="export-link"><span
							class="ui-icon ui-icon-arrowreturn-1-w"></span> <s:message
								code="export" /> &hellip;</a></li>
					<li><a href="/invalid" class="exit-link"><span
							class="ui-icon ui-icon-circlesmall-close"></span> <s:message
								code="exit" /></a></li>
				</ul></li>

			<li><a class='ignore-url' href=""><s:message code="category" /></a>
				<ul>
					<c:if
						test="${sessionScope.loggedinUserInfo.role=='Admin' || sessionScope.loggedinUserInfo.role=='ReadWrite'}">
						<li><a href="/invalid" class="new-category-link"><span
								class="ui-icon ui-icon-document"></span> <s:message
									code="newCat" /></a></li>
						<li><a href="/invalid" class="edit-category-link"><span
								class="ui-icon ui-icon-pencil"></span> <s:message code="editCat" /></a></li>
						<li><a href="/invalid" class="delete-category-link"><span
								class="ui-icon ui-icon-scissors"></span> <s:message
									code="delCat" /></a></li>
					</c:if>
					<li><a href="/invalid" class="view-category-link"><span
							class="ui-icon ui-icon-info"></span> <s:message code="viewCat" /></a></li>
				</ul></li>
			<li><a class='ignore-url' href=""><s:message code="entry" /></a>
				<ul>
					<c:if
						test="${sessionScope.loggedinUserInfo.role=='Admin' || sessionScope.loggedinUserInfo.role=='ReadWrite'}">
						<li><a href="/invalid" class="new-entry-link"><span
								class="ui-icon ui-icon-document"></span> <s:message
									code="newEntry" /></a></li>
						<li><a href="/invalid" class="edit-entry-link"><span
								class="ui-icon ui-icon-pencil"></span> <s:message
									code="editEntry" /></a></li>
						<li><a href="/invalid" class="delete-entry-link"><span
								class="ui-icon ui-icon-scissors"></span> <s:message
									code="delEntry" /></a></li>
					</c:if>
					<li><a href="/invalid" class="view-entry-link"><span
							class="ui-icon ui-icon-info"></span> <s:message code="viewEntry" /></a></li>
				</ul></li>
			<li><a class='ignore-url' href=""><s:message code="admin" /></a>
				<ul>
					<li><a href="/invalid" class="my-account-link"><s:message
								code="myAccount" /></a></li>
					<c:if test="${sessionScope.loggedinUserInfo.role=='Admin'}">
						<li><a href="/invalid" class="new-user-link"><s:message
									code="newUser" /></a></li>
						<li><a href="/invalid" class="list-user-link"><s:message
									code="listUser" /></a></li>
					</c:if>
				</ul></li>
		</ul>
		<div class="nowrap search-widget">
			<input name="search" class="search-input"><span
				class="ui-icon ui-icon-search search-submit"></span>
		</div>
	</div>
</div>
<div class="content-widget">
	<div class="left-panel">
		<div class='title'>
			<s:message code="categories" />
		</div>
		<div class='category-widget'></div>
	</div>
	<div class="right-panel">
		<div class='title'>
			<s:message code="entries" />
		</div>
		<div class='entry-widget'></div>
	</div>
</div>
<script type="text/javascript">
	$(function() {
		home.list();
	});
</script>