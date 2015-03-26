var doesDateExist = function(day, month, year) {
	if (month < 1 || month > 12) {
		return false;
	}
	if (day >= 1 && day <= 28) {
		return true;
	}
	if (month != 2) {
		return (day == 29) || (day == 30) || (((day == 31) && !(month == 4 || month == 6 || month == 9 || month == 11)));
	} else {
		if (day == 29) {
			return ((year % 4 == 0) && !(year % 100 == 0)) || ((year % 400) == 0);
		}
	}
	return false;
}

var checkDate = function(date) {

	//date not mandatory
	if(date.length == 0) {
		return true;
	}

	pattern = /^\d{1,4}\-\d{1,2}\-\d{1,2}$/;
	if(!pattern.test(date)) {
		return false;
	}
	subPattern = /\d+/g;
	matches = date.match(subPattern);
	day = matches[2];
	month = matches[1];
	year = matches[0];
	return doesDateExist(day, month, year);
}

var checkName = function(name) {
	if(!name) {
		return false;
	}
	name = name.trim();

	if(name.length == 0)
		return false;
	//return true;
	return !(/[!@#$%^*()_+\-=\[\]{};':"\\|<>?]+$/.test(name));
}
var error = false;
var focusedField = null;


var checkNameField = function(name) {
	if (checkName($("#computerName").val())) {
		$("#computerName").removeClass("error");
		$("#computerNameError").addClass("collapse");
	} else {
		$("#computerName").addClass("error");
		$("#computerNameError").removeClass("collapse");
		$("#computerNameError").addClass("error");

		if(!error) {
			error = true;
			focusedField = $("#computerName");
		}

	}
}

var checkIntroDateField = function(date) {
	if (checkDate($("#introduced").val())) {
		$("#introduced").removeClass("error");
		$("#introducedError").addClass("collapse");
	} else {
		$("#introduced").addClass("error");
		$("#introducedError").addClass("error");
		$("#introducedError").removeClass("collapse");
	}
	if(!error) {
		error = true;
		focusedField = $("#introduced");
	}
}

var checkDiscontDateField = function() {
	if (checkDate($("#discontinued").val())) {
		$("#discontinued").removeClass("error");
		$("#discontinuedError").addClass("collapse");
	} else {
		$("#discontinued").addClass("error");
		$("#discontinuedError").addClass("error");
		$("#discontinuedError").removeClass("collapse");
	}
	if(!error) {
		error = true;
		focusedField = $("#discontinued");
	}
}


$("#computerName").keyup(function() {checkNameField()});

$("#introduced").keyup(function() {checkIntroDateField()});

$("#discontinued").keyup(function() {checkDiscontDateField()});

var validate = function() {
	checkNameField();
	checkIntroDateField();
	checkDiscontDateField();

	if(error) {
		focusedField.focus();
	}
	return (!error);
}
