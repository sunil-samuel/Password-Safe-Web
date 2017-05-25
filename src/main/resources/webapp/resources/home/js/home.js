var home = {
	resource : null,
	categoryForm : null,
	newCategoryDialog : null,
	timerTimeout : null,

	index : function(resource) {

		home.resource = resource;
		$.ajaxSetup({
			cache : false
		});

		home.registerThemeDropDown();
		home.registerLanguageDropDown();
		home.createClock();
	},

	registerThemeDropDown : function() {
		$('.theme-dropdown').off('change').on('change', function() {
			/**
			 * First remove the existing stylesheet.
			 */
			$("link#dynamic-sheet").remove();
			$("<link/>", {
				rel : "stylesheet",
				type : "text/css",
				id : "dynamic-sheet",
				href : "/resources/home/css/" + this.value + ".css"
			}).appendTo("head");
		});
	},

	registerLanguageDropDown : function() {
		$('.language-dropdown').off('change').on('change', function() {
			var jqxhr = $.get("/languageRoller?locale=" + this.value);
			jqxhr.done(function(data, textStatus, jqXhr) {
				$('body').append(data);
			});
		});
	},

	upload : function() {
		$('#upload').fileupload({
			/**
			 * Called when a new file is added
			 */
			add : function(e, data) {
				$('.fileList').html(data.files[0].name);
				$('.import-button').click(function() {
					data.submit();
					return false;
				});
				return false;
			},
			done : function(e, data) {
				$('.fileList').html("Upload Complete");
				$('#import-dialog').dialog("destroy");
				home.createDialog(data.result, data.textStatus);
				return false;
			},

			fail : function(e, data) {
				home.createDialog(data.jqXHR.responseText, data.errorThrown);
				return false;
			},
			progress : function(e, data) {
				var progress = parseInt(data.loaded / data.total * 100, 10);
				$('.progress').html(progress);
			}
		});
	},

	loginForm : function() {
		$(".submit-red").on('click', function() {
			var user = $("input[name=userid]").val();
			var pwd = $("input[name=pwd]").val();
			var msg = "";

			if (home.isEmpty(user)) {
				msg += "<div class='error'>" + jsvar.missingUserId + "</div>";
			}
			if (home.isEmpty(pwd)) {
				msg += "<div id='error'>" + jsvar.missingPwd + "</div>";
			}

			if (!home.isEmpty(msg)) {
				home.createDialog(msg, jsvar.headerInput);
				return false;
			}

			var jqxhr = $.post(home.resource + "login", {
				userid : user,
				pwd : pwd
			});
			jqxhr.done(function(data, textStatus, jqXHR) {
				$(".main-widget").html(data);
			});
			jqxhr.fail(function(jqXHR, textStatus, errorThrown) {
				home.createDialog(jqXHR.responseText, jqXHR.status);
			});
			return false;
		});
	},

	allEntry : function() {
		$(".entry-element").tooltip();
		home.clipboardSetup(".copyable");
		$('.entry-element').off('click').on('click', function() {
			var cls = "entry-select";
			/*
			 * If this was already selected then just unselect it.
			 */
			var hasClass = $(this).parent().closest('div').hasClass(cls);
			$('.entry-element').parent().closest('div').removeClass(cls);
			if (!hasClass) {
				$(this).parent().closest('div').addClass(cls);
			}
		});
		home.registerActions([ '.edit-entry', '.edit-entry-link' ], [
				'.delete-entry', '.delete-entry-link' ], [ '.view-entry',
				'.view-entry-link' ]);
	},

	editUsers : function() {
		$(".entry-element").tooltip();
		$('.entry-element').off('click').on('click', function() {
			var cls = "entry-select";
			/*
			 * If this was already selected then just unselect it.
			 */
			var hasClass = $(this).parent().closest('div').hasClass(cls);
			$('.entry-element').parent().closest('div').removeClass(cls);
			if (!hasClass) {
				$(this).parent().closest('div').addClass(cls);
			}
			return false;
		});
		$('.edit-user').off('click').on('click', function() {
			home.processEditUserOnClick(this);
			return false;
		});

		$('.delete-user').off('click').on('click', function() {
			home.processDeleteUserOnClick(this);
			return false;
		});
	},

	registerActions : function(editElement, deleteElement, viewElement) {
		$(editElement[0]).off('click').on('click', function() {
			var thisClass = $(this).closest('div.entry-start');
			$('.entry-start').removeClass('entry-select');
			thisClass.addClass('entry-select');
			$(editElement[1]).click();
			return false;
		});
		$(deleteElement[0]).off('click').on('click', function() {
			var thisClass = $(this).closest('div.entry-start');
			$('.entry-start').removeClass('entry-select');
			thisClass.addClass('entry-select');
			$(deleteElement[1]).click();
			return false;
		});
		if (viewElement) {
			$(viewElement[0]).off('click').on('click', function() {
				var thisClass = $(this).closest('div.entry-start');
				$('.entry-start').removeClass('entry-select');
				thisClass.addClass('entry-select');
				$(viewElement[1]).click();
				return false;
			});
		}
	},

	entryView : function() {
		home.clipboardSetup(".copyable");
	},

	clipboardSetup : function(element) {
		$(element).off('dblclick').on('dblclick', function() {
			var value = $.trim($(this).data("value"));
			$('<textarea>').attr({
				id : 'to-copy-text',
				value : value,
				text : value
			}).appendTo('body');
			$('#to-copy-text').html(value);
			$('#to-copy-text').select();
			var copied = document.execCommand("copy");
			$('#to-copy-text').remove();
			home.clipboardAlert(copied);
		});
	},

	clipboardAlert : function(copied) {
		var msg = jsvar.successClipboard;
		if (!copied) {
			msg = jsvar.failedClipboard;
		}
		jQuery('<div/>', {
			id : 'clipboard-alert',
			title : msg,
			'class' : 'clipboard-alert',
			text : msg
		}).appendTo('body');
		$('.clipboard-alert').fadeIn().delay(2000).fadeOut(function() {
			$('.clipboard-alert').remove();
		});
	},

	list : function() {
		home.setupMenu();
		// home.createPasswordEye();
		// home.createCategorySetup();
		home.getAllLevelCategories(null, home.gatherAllTopLevelCategories);

		$('.left-panel').resizable({});
		$('.right-panel').resizable({});

		home.exitOnClick();
		home.importOnClick();
		home.exportOnClick();

		home.newEntryOnClick();
		home.editEntryOnClick();
		home.deleteEntryOnClick();
		home.viewEntryOnClick();

		home.newCategoryOnClick();
		home.deleteCategoryOnClick();
		home.editCategoryOnClick();
		home.viewCategoryOnClick();

		home.myAccountOnClick();
		home.newUserOnClick();
		home.listUserOnClick();

		home.searchOnClick();

		// $('.ignore-url').off('click').on('click', function(e) {
		// return false;
		// });
	},

	searchOnClick : function() {
		$(".search-submit").off('click').on('click', function(e) {
			home.processSearchOnClick();
			return false;
		});
	},
	myAccountOnClick : function() {
		$(".my-account-link").off('click').on('click', function(e) {
			home.processMyAccountOnClick();
			return false;
		});
	},

	newUserOnClick : function() {
		$('.new-user-link').off('click').on('click', function(e) {
			home.processNewUserOnClick();
			return false;
		});
	},

	listUserOnClick : function() {
		$('.list-user-link').off('click').on('click', function(e) {
			home.processListUserOnClick();
			return false;
		});
	},

	importOnClick : function() {
		$('.import-link').off('click').on('click', function() {
			home.processImportOnClick();
			return false;
		});
	},

	exportOnClick : function() {
		$('.export-link').off('click').on('click', function() {
			home.processExportOnClick();
			return false;
		});
	},

	exitOnClick : function() {
		$('.exit-link').off('click').on('click', function() {
			home.createDialogYesNo(jsvar.confirmExit, home.processExit);
			return false;
		});
	},

	newEntryOnClick : function() {
		$(".new-entry-link").off('click').on('click', function() {
			var selected = home.getSelectedCategoryInfo();
			if (!selected || !selected[0]) {
				home.createDialog(jsvar.missingEntry, jsvar.headerCatCreate);
				return false;
			}
			home.processNewEntry(selected[0]);
			return false;
		});
	},

	editEntryOnClick : function() {
		$(".edit-entry-link").off('click').on('click', function() {
			var selected = home.getSelectedEntryInfo();
			if (!selected || !selected[0]) {
				home.createDialog(jsvar.missingEntry, jsvar.headerEntryEdit);
				return false;
			}
			home.processEditEntry(selected);
			return false;
		});
	},

	deleteEntryOnClick : function() {
		$(".delete-entry-link").off('click').on('click', function() {
			var selected = home.getSelectedEntryInfo();
			if (!selected || !selected[0]) {
				home.createDialog(jsvar.missingEntry, "Delete Entry");
				return false;
			}
			home.createDialogYesNo(jsvar.delEntry, home.processDeleteEntry);
			return false;
		});
	},

	viewEntryOnClick : function() {
		$('.view-entry-link').off('click').on('click', function() {
			var selected = home.getSelectedEntryInfo();
			if (!selected || !selected[0]) {
				home.createDialog(jsvar.missingEntry, jsvar.headerEntryView);
				return false;
			}
			home.processViewEntry(selected[0]);
			return false;
		});
	},

	newCategoryOnClick : function() {
		$(".new-category-link").off('click').on('click', function() {
			/**
			 * If the user selected a category on the left panel, then let them
			 * know that this category is being created inside the selected
			 * category.
			 */
			var selected = home.getSelectedCategoryInfo();
			home.processNewCategory(selected);
			return false;
		});
	},

	editCategoryOnClick : function() {
		$(".edit-category-link").off('click').on('click', function() {
			var selected = home.getSelectedCategoryInfo();
			if (!selected || !selected[0]) {
				home.createDialog(jsvar.missingCat, jsvar.headerCatCreate);
				return false;
			}
			home.processUpdateCategory(selected[0]);
			return false;
		});
	},

	deleteCategoryOnClick : function() {
		$(".delete-category-link")
				.off('click')
				.on(
						'click',
						function() {
							var selected = home.getSelectedCategoryInfo();
							if (!selected || !selected[0]) {
								home.createDialog(jsvar.missingCat,
										jsvar.headerCatDel);
								return false;
							}
							var confirm = $("<div class='dialog confirm-dialog'>"
									+ home.template(jsvar.delCat, {
										'category' : selected[1]
									}) + "</div>");
							confirm.data("catId", selected[0]);
							confirm.data("element", this);
							var buttons = {
								Cancel : function() {
									$(this).dialog("destroy");
								}
							};
							buttons[jsvar.headerCatDel] = home.processDeleteCategory;

							confirm.dialog({
								autoOpen : true,
								modal : true,
								title : jsvar.delCat,
								closeOnEscape : true,
								close : function() {
									$(this).dialog("destroy");
								},
								buttons : buttons
							// }
							});
							return false;
						});
	},

	viewCategoryOnClick : function() {
		$('.view-category-link').off('click').on('click', function() {
			var selected = home.getSelectedCategoryInfo();
			if (!selected || !selected[0]) {
				home.createDialog(jsvar.missingCat, jsvar.headerCatView);
				return false;
			}
			home.processViewCategory(selected[0]);
			return false;
		});
	},

	/**
	 * This method is called from the success of a request to the backend.
	 */
	gatherAllTopLevelCategories : function(data) {
		$(".category-widget").html(data);
		$(".category-element:even").addClass('even');
		$(".category-element:odd").addClass('odd');
		home.setOnclickForCategory();
	},

	setOnclickForCategory : function() {
		var cls = "category-select";
		/**
		 * When a category is clicked, get all items for it
		 */
		$(".category-element").off('click').on('click', function() {
			var hasClass = $(this).hasClass(cls);
			/**
			 * Remove this class from all elements. We just need to remove from
			 * this one, but we are removing from all in case. If it is true,
			 * then remove any of the sub-elements and remove the 'opened'
			 * class.
			 */
			$('.category-element').removeClass(cls);
			/**
			 * Check to see if this is already selected. True - already
			 * selected.
			 */
			if (hasClass) {
				$(this).removeClass("opened");
				$(this).parent().find('.sub-folders').html("");
				$(this).find('.ui-icon-folder-collapsed').removeClass('hide');
				$(this).find('.ui-icon-folder-open').addClass('hide');
			} else {
				$(this).addClass(cls);
				home.getEntriesAndFoldersForCategory(this);
				$(this).addClass("opened");
				$(this).find('.ui-icon-folder-collapsed').addClass('hide');
				$(this).find('.ui-icon-folder-open').removeClass('hide');
			}
		});
	},

	getEntriesAndFoldersForCategory : function(element) {
		var id = $(element).data("id");
		home.getAllLevelCategories(id, home.processFoldersForCategory);
		home.getAllLevelEntries(id);
	},

	processFoldersForCategory : function(data, id) {
		$('.' + id + '-sub-folders').html(data);
		home.setOnclickForCategory();

	},

	getAllLevelCategories : function(id, func, use) {
		var resource = home.resource + "secure/category/get-all-level";
		var next = "?";
		if (id && id != "") {
			resource = resource + next + "id=" + id;
			next = "&";
		}
		if (use && use != "") {
			resource = resource + next + use;
		}
		var jqxhr = $.get(resource);
		jqxhr.done(function(data, textStatus, jqXhr) {
			func(data, id, textStatus, jqXhr);
		}).fail(function(jqXhr, textStatus, errorThrown) {
			home.createDialog(jqXhr.responseText, jqXhr.status);
		});
	},

	getAllLevelEntries : function(id) {
		var url = home.resource + "secure/entry/get-all-entries?id=" + id;

		var jqxhr = $.get(url);
		jqxhr.done(function(data, textStatus, jqXhr) {
			$('.entry-widget').html(data);
			$('.entry-start:even').addClass('even');
			$('.entry-start:odd').addClass('odd');
		}).fail(function(jqXhr, textStatus, errorThrown) {
			home.createDialog(jqXhr.responseText, jqXhr.status);
		});
	},

	/*
	 * Submit a new category
	 */
	categoryForm : function() {

	},

	createPasswordEye : function() {
		/**
		 * The way we show the password is to have a hidden input [type=text]
		 * that also has the same data as the actual password input does. To
		 * keep tract of the changes, we look for a 'input propertychange' to
		 * trigger writing the data to the 'text' input also.
		 */
		$(".password-eye").unbind('input propertychange').bind(
				'input propertychange', function() {
					$(".password-eye-text").val($(this).val());
				});
		forceEyeOpen = false;
		$(".eye-icon").on('mouseover', function() {
			if (!forceEyeOpen) {
				$('.password-eye').hide();
				$('.password-eye-text').show();
			}
		});
		$(".eye-icon").on('mouseout', function() {
			if (!forceEyeOpen) {
				$('.password-eye').show();
				$('.password-eye-text').hide();
			}
		});
		$(".eye-icon").on('click', function() {
			if (forceEyeOpen) {
				$('.password-eye').show();
				$('.password-eye-text').hide();
				forceEyeOpen = false;
			} else {
				$('.password-eye').hide();
				$('.password-eye-text').show();
				forceEyeOpen = true;
			}
		});
	},

	setupMenu : function() {
		$('#main-menu').smartmenus({
			keepInViewport : true,
			subIndicators : true,
			subIndicatorsPos : 'append',
			subIndicatorsText : "&#10148;"
		});
	},

	processExit : function() {
		var jqxhr = $.get(home.resource + "secure/logout");
		jqxhr.done(function(data, textStatus, jqXhr) {
			$('.main-widget').html(data);
			return false;
		}).fail(function(jqXhr, textStatus, errorThrown) {
			return false;
		});
	},

	processSearchOnClick : function() {
		var searchTerms = $(".search-input").val();
		var jqxhr = $.get(home.resource + "secure/entry/search-entries?search="
				+ searchTerms);
		jqxhr.done(function(data, textStatus, jqXhr) {
			$('.entry-widget').html(data);
			$('.entry-start:even').addClass('even');
			$('.entry-start:odd').addClass('odd');
		}).fail(function(jqXhr, textStatus, errorThrown) {
			home.createDialog(jqXhr.responseText, jqXhr.status);
		});
	},
	processMyAccountOnClick : function() {
		var jqxDialog = new JQXhrDialog(home.resource
				+ "secure/admin/my-account").addButton("Update",
				home.processMyAccount).addButton("Close", "close").setTitle(
				"My Account").setDisableCancel(true).process();
	},

	processNewUserOnClick : function() {
		var jqxDialog = new JQXhrDialog(home.resource
				+ "secure/admin/priv/new-user").addButton("Create",
				home.processNewUser).addButton("Close", "close").setTitle(
				"New Account").setDisableCancel(true).process();
	},

	processListUserOnClick : function() {
		var jqxhr = $.get(home.resource + "secure/admin/priv/users-list");
		jqxhr.done(function(data, textStatus, jqXhr) {
			$('.entry-widget').html(data);
			$('.entry-start:even').addClass('even');
			$('.entry-start:odd').addClass('odd');
			return false;
		}).fail(function(jqXhr, textStatus, errorThrown) {
			home.createDialog(jqXhr.responseText, errorThrown);
			return false;
		});
	},

	processEditUserOnClick : function(element) {
		var id = $(element).closest('.entry-start').data('id');
		var email = $(element).closest('.entry-start').data('email');

		var jqDialog = new JQXhrDialog(home.resource
				+ "secure/admin/priv/edit-user?id=" + id + "&email=" + email)
				.setTitle('Edit User ' + email).addButton("Update User",
						home.processEditUser).process();
	},

	processDeleteUserOnClick : function(element) {
		var id = $(element).closest('.entry-start').data('id');
		var email = $(element).closest('.entry-start').data('email');
		var myFunc = function() {
			home.processDeleteUser({
				id : id,
				email : email
			});
		}
		home.createDialogYesNo("Are you sure you want to delete user [" + email
				+ "]", myFunc);
	},

	processEditEntry : function(selected) {
		var id = selected[0];
		var search = selected[2];
		var funcToCall = function() {
			home.createEntry(search, this);
		}
		var jqDialog = new JQXhrDialog(home.resource
				+ "secure/entry/get-entry-for-edit?id=" + id).addButton(
				jsvar.headerEntryUpdate, funcToCall).setFailedMessage(
				jsvar.failedEntryEdit, jsvar.headerEntryEdit).process();
	},

	processExportOnClick : function() {
		var jqxDialog = new JQXhrDialog(home.resource
				+ "secure/export/exportData").addButton(jsvar.exportConfirm,
				home.processExport).process();
	},

	processImportOnClick : function() {
		var jqDialog = new JQXhrDialog(home.resource + "secure/import/upload")
				.setClasses("import-dialog").setId("import-dialog").process();
	},

	processNewEntry : function(catId) {
		var jqDialog = new JQXhrDialog(home.resource
				+ "secure/entry/new-entry?catId=" + catId).addButton(
				jsvar.headerEntryCreate, home.createEntry).process();
	},

	processDeleteEntry : function() {
		var selected = home.getSelectedEntryInfo();
		var jqxhr = $.post(home.resource + "secure/entry/remove-entry?id="
				+ selected[0]);
		jqxhr.done(function(data, textStatus, jqXhr) {
			home.createDialog(data, textStatus);
			$('.category-' + selected[0]).remove();
			return false;
		}).fail(function(jqXhr, textStatus, errorThrown) {
			home.createDialog(jqXhr.responseText, errorThrown);
			return false;
		});
	},

	processViewEntry : function(id) {
		var jqxhr = $.get(home.resource + "secure/entry/get-entry-for-view?id="
				+ id);
		jqxhr.done(function(data, textStatus, jqXhr) {
			home.createDialog(data, jsvar.headerEntryView, false);
			return false;
		}).fail(function(jqXhr, textStatus, errorThrown) {
			home.createDialog(jqXhr.responseText, errorThrown);
			return false;
		});
	},

	processEditUser : function() {
		var data = home.accountDate();
		$(this).dialog("close");
		home.processEditUserPost(data);
	},

	processDeleteUser : function(vars) {
		var jqxhr = $.post(home.resource + "secure/admin/priv/delete-user",
				vars);
		jqxhr.done(function(data, textStatus, jqXhr) {
			// home.createDialog(data, textStatus);
			$('.list-user-link').click();
			return false;
		}).fail(function(jqXhr, textStatus, errorThrown) {
			home.createDialog(jqXhr.responseText, errorThrown);
			return false;
		});

	},

	processExport : function() {
		var format = $("input[name='file-format']:checked").val();
		if (home.isEmpty(format)) {
			home.createDialog("Please select a file format");
			return false;
		}
		var password = $("input[name='password']").val();
		var repassword = $("input[name='repassword']").val();
		if (home.isEmpty(password)) {
			home.createDialog("Please enter a password");
			return false;
		}
		if (password != repassword) {
			home.createDialog("Please ensure that the two passwords match");
			return false;
		}
		$(this).dialog("close");
		home.processExportRequest(format, password);
	},

	processExportRequest : function(format, password) {
		document.location = home.resource
				+ "secure/export/exportDataToFile?format=" + format
				+ "&password=" + password;
	},

	processNewCategory : function(selected) {
		var resource = home.resource + "secure/category/new-category";
		if (selected && selected[0]) {
			resource = resource + "?parentId=" + selected[0];
		}
		var jqDialog = new JQXhrDialog(resource).addButton(
				jsvar.headerCatCreate, home.createCategory).process();
	},

	processNewUser : function() {
		var data = home.accountDate();
		if (data == false) {
			return false;
		}
		if (home.isEmpty(data["password"])) {
			home
					.createDialog("Please enter a valid Password",
							"Empty Password");
			return false;
		}
		home.processNewUserPost(data, this);
	},

	processMyAccount : function() {
		var data = home.accountDate();
		$(this).dialog("close");
		home.processMyAccountPost(data);
	},

	accountDate : function() {
		var element = $(".my-account-form");

		var id = $("input[name='id']", element).val();
		var firstName = $("input[name='firstName']", element).val();
		var lastName = $("input[name='lastName']", element).val();
		var email = $("input[name='email']", element).val();
		var phoneNumber = $("input[name='phoneNumber']", element).val();
		var pwd = $("input[name='password']", element).val();
		var pwdVerify = $("input[name='passwordVerify']", element).val();
		var role = $("input[name='role']:checked").val();
		/**
		 * If pwdVerify is not empty, then validate it with pwd
		 */
		if (!home.isEmpty(pwd)) {
			if (pwd != pwdVerify) {
				home.createDialog("Passwords do not match", "invalid.password");
				return false;
			}
		}
		if (home.isEmpty(email)) {
			home.createDialog("EMail cannot be empty", "empty.email");
			return false;
		}
		if (!home.isEmailFormat(email)) {
			home.createDialog("E-Mail is not in correct format");
			return false;
		}
		if (home.isEmpty(role)) {
			home.createDialog("Please select a valid role");
			return false;
		}
		return {
			id : id,
			firstName : firstName,
			lastName : lastName,
			email : email,
			phoneNumber : phoneNumber,
			password : pwd,
			role : role
		};
		// home.createDialog("first:[" + firstName + "] last[" + lastName
		// + "]email [" + email + "]phone [" + phone + "] role[" + role
		// + "]");
	},

	processDeleteCategory : function() {
		var id = $('.confirm-dialog').data("catId");
		var element = $('.confirm-dialog').data("element");
		$(this).dialog("destroy");
		var jqxhr = $.get(home.resource + "secure/category/child-exists?id="
				+ id);
		jqxhr.done(function(data, textStatus, jqXhr) {
			home.createDialog(data, textStatus);
			$('.category-select').closest(".category-start").remove();
			return false;
		}).fail(function(jqXhr, textStatus, errorThrown) {
			home.createDialog(jqXhr.responseText, errorThrown);
			return false;
		});
	},

	processUpdateCategory : function(id) {
		var jqDialog = new JQXhrDialog(home.resource
				+ "secure/category/get-by-id?id=" + id).addButton(
				jsvar.headerCatUpdate, home.createCategory).process();
	},

	processViewCategory : function(id) {
		var jqxhr = $.get(home.resource + "secure/category/get-by-id?id=" + id
				+ "&type=view");
		jqxhr.done(function(data, textStatus, jqXhr) {
			home.createDialog(data, jsvar.headerCatView, false);
			return false;
		}).fail(function(jqXhr, textStatus, errorThrown) {
			home.createDialog(jqXhr.responseText, errorThrown);
			return false;
		});
	},

	getSelectedCategoryInfo : function() {
		return home.getSelectedInfo(".category-select");
	},

	getSelectedEntryInfo : function() {
		return home.getSelectedInfo(".entry-select");
	},

	getSelectedInfo : function(element) {
		var selectedElement = $(element);
		var parentId, parentTitle;
		if (selectedElement && selectedElement.length) {
			return [ selectedElement.data("id"), selectedElement.data("title"),
					selectedElement.data("search") ];
		}
	},

	createEntry : function(search, dialogElement) {
		var url = home.resource + "secure/entry/new-entry";
		var element = $(".create-entry");

		var title = $("input[name='title']", element).val();
		var description = $("input[name='description']", element).val();
		var username = $("input[name='username']", element).val();
		var password = $("input[name='password']", element).val();
		var urlp = $("input[name='url']", element).val();
		var notes = $("textarea[name='notes']", element).val();
		var id = $("input[name='id']", element).val();
		var parentCatId = $("input[name='parentCategoryId']", element).val();

		if (home.isEmpty(title)) {
			home.createDialog(jsvar.missingTitle, jsvar.headerEntryCreate);
			return false;
		}
		if (home.isEmpty(parentCatId)) {
			home.createDialog(jsvar.missingEntry, jsvar.headerEntryEdit);
			return false;
		}
		if (!home.isEmpty(urlp) && !home.isUrlFormat(urlp)) {
			home.createDialog(jsvar.urlInvalidFormat, jsvar.headerEntryCreate);
			return false;
		}
		var vars = {
			title : title,
			description : description,
			username : username,
			password : password,
			url : urlp,
			notes : notes
		};
		if (home.isEmpty(id)) {
			var selected = home.getSelectedCategoryInfo();
			vars["parentCategoryId"] = selected[0];
		} else {
			vars["parentCategoryId"] = parentCatId;
			vars["id"] = id;
		}
		if (home.isEmpty(dialogElement)) {
			$(this).dialog("close");
		} else {
			$(dialogElement).dialog("close");
		}
		var jqxhr = $.post(url, vars);
		jqxhr
				.done(
						function(data, textStatus, jqXHR) {
							home.createDialog(data, textStatus);
							/*
							 * Now update the list of entries for category
							 */
							// TODO - We need to check if this is a search. If
							// it is, then
							// re-run the search instead of the following.
							if (home.isEmpty(search)) {
								home
										.getEntriesAndFoldersForCategory($(".category-select.opened"));
							} else {
								/**
								 * make sure that the search box has the search
								 * that create this hitlist and then make it
								 * search.
								 */
								$('.search-input').val(search);
								$('.search-submit').click();
							}
						}).fail(function(jqXHR, textStatus, errorThrown) {
					home.createDialog(jsvar.failedEntryCreate);
					return false;
				});
		return false;
	},
	processNewUserPost : function(vars, element) {
		var jqxhr = $.post(home.resource + "secure/admin/priv/new-user", vars);
		jqxhr.done(function(data, textStatus, jqXhr) {
			// home.createDialog(data, textStatus);
			$(element).dialog("close");
			$('.list-user-link').click();
			return false;
		}).fail(function(jqXhr, textStatus, errorThrown) {
			home.createDialog(jqXhr.responseText, errorThrown);
			return false;
		});
	},

	processEditUserPost : function(vars) {
		if (home.isEmpty(vars["password"])) {
			delete vars["password"];
		}
		var jqxhr = $.post(home.resource + "secure/admin/priv/edit-user", vars);
		jqxhr.done(function(data, textStatus, jqXhr) {
			home.createDialog(data, textStatus);
			$('.list-user-link').click();
			return false;
		}).fail(function(jqXhr, textStatus, errorThrown) {
			home.createDialog(jqXhr.responseText, errorThrown);
			return false;
		});

	},

	processMyAccountPost : function(vars) {
		/**
		 * For an existing user, if the password is empty then don't use it.
		 * This just means that the user did not want to change the password.
		 */
		if (home.isEmpty(vars["password"])) {
			delete vars["password"];
		}
		var jqxhr = $.post(home.resource + "secure/admin/my-account", vars);
		jqxhr.done(function(data, textStatus, jqXhr) {
			home.createDialog(data, textStatus);
			return false;
		}).fail(function(jqXhr, textStatus, errorThrown) {
			home.createDialog(jqXhr.responseText, errorThrown);
			return false;
		});
	},

	/**
	 * Submit the create new category form from the button on the dialog.
	 */
	createCategory : function() {

		var element = $(".edit-category");
		var editing = true;
		var url = home.resource + "secure/category/update-category";
		if (!element || element.length <= 0) {
			element = $('.create-category');
			editing = false;
			url = home.resource + "secure/category/new-category";
		}
		var title = $("input[name='title']", element).val();
		var description = $("input[name='description']", element).val();
		var notes = $("textarea[name='notes']", element).val();
		var parentId = $("input[name='parentId']", element).val();
		var id = $("input[name='id']", element).val();
		if (home.isEmpty(title)) {
			home.createDialog(jsvar.missingTitle, jsvar.headerCatCreate);
			return false;
		}
		$(this).dialog("close");
		var jqxhr = $.post(url, {
			title : title,
			description : description,
			notes : notes,
			parentId : parentId,
			id : id
		});
		jqxhr.done(function(data, textStatus, jqXHR) {
			home.createDialog(data, textStatus);
			var jsonData = $(data).closest('.json-data').attr('data-json');
			try {
				var catInfo = JSON.parse(jsonData);
			} catch (e) {
				home.createDialog("Could not parse [" + jsonData + "]." + e);
			}
			if (catInfo.editing) {
				/**
				 * This runs when we are editing an existing category.
				 */
				var element = $("." + catInfo.id + "-category-element");
				$(".category-name", element).html(catInfo.title);
				$(element).data("title", catInfo.title);
			} else {
				/**
				 * This runs when we are creating a new category.
				 */
				home.getAllLevelCategories(null,
						home.gatherAllTopLevelCategories);
				// home.getAllLevelCategories(catInfo.parentId,
				// home.processNewCategory, "use=edit");
			}
		});
		jqxhr.fail(function(jqXHR, textStatus, errorThrown) {
			home.createDialog(jqXHR.responseText, jqXHR.status);
		});

		return false;
	},

	createDialog : function(text, title, modal) {
		var dialog = $("<div class='dialog'>" + text + "</div>");

		var buttons = {};
		buttons[jsvar.ok] = function() {
			$(this).dialog("destroy");
		};
		dialog.dialog({
			autoOpen : false,
			// modal : true,
			title : title,
			closeOnEscape : true,
			close : function() {
				$(this).dialog("destroy");
			},
			buttons : buttons
		});
		if (home.isEmpty(modal) || modal == true) {
			dialog.dialog({
				modal : true
			});
		}
		dialog.dialog("open");
	},

	createDialogYesNo : function(text, yesFunction, noFunction) {
		var dialog = $("<div class='dialog'>" + text + "</div>");
		/**
		 * We want to make sure that we close the dialog when the yes function
		 * is clicked. The only way to do that is to create another function
		 * that calls the noFunction after the dialog is closed.
		 */
		var yesFunc = function() {
			yesFunction();
			$(this).dialog("destroy");
			$(".dialog", this).remove();
		}
		var noFunc;
		if (home.isEmpty(noFunction)) {
			noFunc = function() {
				$(this).dialog("destroy");
				$(".dialog", this).remove();
			}
		} else {
			noFunc = function() {
				noFunction();
				$(this).dialog("destory");
				$(".dialog", this).remove();
			}
		}
		var buttons = {};
		buttons[jsvar.yes] = yesFunc;
		buttons[jsvar.no] = noFunc;

		dialog.dialog({
			autoOpen : false,
			modal : true,
			title : "yes/no",
			closeOnEscape : true,
			close : function() {
				$(this).dialog("destroy");
				$(".dialog", this).remove();
			},
			buttons : buttons
		});
		dialog.dialog("open");
	},

	createClock : function() {
		var clockWidget = $('<div/>', {
			id : 'clock-widget',
			title : 'Clock',
			'class' : 'clock-widget'
		}).appendTo('body');
		var colon = $('<span/>', {
			id : 'time-colon',
			title : 'colon',
			'class' : 'time-colon',
			text : ":"
		});
		$('<span/>', {
			id : 'clock-hour',
			title : 'hour',
			'class' : 'clock-hour'
		}).appendTo('.clock-widget');
		colon.clone().appendTo(clockWidget);
		$('<span/>', {
			id : 'clock-minute',
			title : 'minute',
			'class' : 'clock-minute'
		}).appendTo('.clock-widget');
		colon.clone().appendTo(clockWidget);
		$('<span/>', {
			id : 'clock-second',
			title : 'second',
			'class' : 'clock-second'
		}).appendTo('.clock-widget');
		$('<span/>', {
			id : 'clock-ampm',
			title : 'ampm',
			'class' : 'clock-ampm'
		}).appendTo('.clock-widget');
		home.clockUpdate();
	},

	clockUpdate : function() {
		var timeNow = new Date();
		var hours = timeNow.getHours();
		var minutes = timeNow.getMinutes();
		var seconds = timeNow.getSeconds();
		var pHours = ((hours > 12) ? hours - 12 : hours);
		var pMinutes = (minutes < 10 ? "0" + minutes : minutes);
		var pSeconds = (seconds < 10 ? "0" + seconds : seconds);
		var ampm = (hours > 12 ? "pm" : "am");
		$('.clock-widget .clock-hour').text(pHours);
		$('.clock-widget .clock-minute').text(pMinutes);
		$('.clock-widget .clock-second').text(pSeconds);
		$('.clock-widget .clock-ampm').text(ampm);
		home.timerTimeout = setTimeout(home.clockUpdate, 1000);
	},

	isEmpty : function(str) {
		return (typeof str == 'undefined' || str == null || 0 === str.length);
	},

	isUrlFormat : function(str) {
		return ((str.startsWith("http://") || str.startsWith("https://")) && str
				.indexOf(".") > 0);
	},

	isEmailFormat : function(email) {
		var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
		return re.test(email);
	},

	template : function(str, data) {
		var newStr = str.replace(/%(\w*)%/g, function(m, key) {
			return data.hasOwnProperty(key) ? data[key] : "";
		});
		return newStr;
	}
}
