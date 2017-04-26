/**
 * A reusable object that is used to process
 * 
 * @returns
 */

function JQXhrDialog() {
	this.classes = null;
	this.id = null;
	this.url = arguments[0] || null;
	this.buttons = arguments[1] || null;
	this.jqxhr = null;
	this.disableCancel = false;
	this.message = null;
	this.failedMessage = null;
	this.failedHeader = null;
	this.title = null;
	this.cancelFunction = function() {
		$(this).dialog("destroy");
	};

	return this;
}

JQXhrDialog.prototype.setButtons = function(buttons) {
	this.buttons = buttons;
	return this;
}

JQXhrDialog.prototype.setTitle = function(title) {
	this.title = title;
	return this;
}

JQXhrDialog.prototype.setId = function(id) {
	this.id = id;
	return this;
}

JQXhrDialog.prototype.setClasses = function(classes) {
	this.classes = classes;
	return this;
}

JQXhrDialog.prototype.setFailedMessage = function(message, header) {
	this.failedMessage = message;
	this.failedHeader = header;
	return this;
}

JQXhrDialog.prototype.setDisableCancel = function(cancel) {
	this.disableCancel = cancel;
	return this;
}

JQXhrDialog.prototype.addButton = function(key, value) {
	if (this.buttons == null) {
		this.buttons = {};
	}
	if (typeof value == 'string' && value == 'close') {
		value = this.cancelFunction;
	}
	this.buttons[key] = value;
	return this;
}

JQXhrDialog.prototype.getButtons = function(url) {
	if (!this.disableCancel) {
		if (this.buttons == null) {
			this.buttons = {};
		}
		this.buttons["Cancel"] = this.cancelFunction;
	}
	return this.buttons;
}

JQXhrDialog.prototype.setUrl = function(url) {
	this.url = url;
	return this;
}

JQXhrDialog.prototype.setMessage = function(message) {
	this.message = message;
	return this;
}

JQXhrDialog.prototype.process = function() {
	this.jqxhr = $.get(this.url);
	var buttons = this.getButtons();

	var failMessage = this.failedMessage;
	var failHeader = this.failedHeader;
	var classes = this.classes;
	var id = this.id;
	var title = this.title;

	this.jqxhr.done(function(data, textStatus, jqXhr) {
		// var dataDialog = $("<div class='dialog'>" + data + "</div>");
		var dataDialog = $('<div/>', {
			html : data,
			'class' : classes,
			id : id
		});

		dataDialog.dialog({
			autoOpen : false,
			width : "auto",
			minHeight : "auto",
			autoResize : true,
			modal : true,
			closeOnEscape : true,
			title : title,
			position : {
				my : "center",
				at : "center",
				of : window
			},
			close : function() {
				$(this).dialog("destroy");
			},
			buttons : buttons
		});
		dataDialog.dialog("open");
		return false;
	}).fail(
			function(jqXhr, textStatus, errorThrown) {
				home.createDialog(failMessage ? failMessage
						: jqXhr.responseText, failHeader ? failHeader
						: errorThrown);
				return false;
			});
	return this;
}
