<%@include file="/WEB-INF/views/include/directive/page.jsp"%>
<!-- ${fn:length(categories)} -->
<c:forEach items="${categories}" var="category">
	<div class='category-start category-${level}'>
		<div data-id='${category.id}' data-title='${category.title}'
			data-parentid='${category.parentId}'
			class='noselect category-element ${category.id}-category-element'>
			<span class="ui-icon  ui-icon-folder-collapsed"></span> <span
				class="hide ui-icon ui-icon-folder-open"></span><span
				class='category-name'>${category.title}</span>
		</div>
		<div class='sub-folders ${category.id}-sub-folders'></div>
	</div>
</c:forEach>