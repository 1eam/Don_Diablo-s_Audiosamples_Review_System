$(document).ready(function() {	
	(function () {
		'use strict';
		var nextbutton = document.getElementById('next_button');
		if (nextbutton != null) {
			nextbutton.addEventListener('click', function (event) {
				//loadPostMethod("Test.exe", nextButtonClick, "email="+document.getElementById('email').value);
				validateInput(document.getElementById('email').value, "Email", nextButtonClick);
			}, false);
		}
		var previousbutton = document.getElementById('previous_button');
		if (previousbutton != null) {
			previousbutton.addEventListener('click', function (event) {
				previousButtonClick();
			}, false);
		}
		/*
		var submitbutton = document.getElementById('submit_button');
		if (submitbutton != null) {
			submitbutton.addEventListener('click', function (event) {
				loadPostMethod("Test.exe", myFunction3, "email="+document.getElementById('email').value);
			}, false);
		}
		*/
		var userinputfield = document.getElementById('username');
		if (userinputfield != null) {
			userinputfield.addEventListener('blur', function (event) {
				validateInput(document.getElementById('username').value, "Username", userInputCheck);
			}, false);
		}
	})();
	
	/*(function () {
		'use strict';
		var buttons = document.getElementsByTagName('button');
		for (var i = 1; i < buttons.length; i++) {
			var buttonId = buttons[i].id;
			//console.log(buttonId);
			var button = document.getElementById(buttonId);
			if (button != null) {
				button.addEventListener('click', function (event) {
					loadPostMethod("Test.exe", buttonId, "email="+document.getElementById('email').value);
				}, false);
			}
		}
	})();*/
	
	(function () {
		'use strict';
		var form = document.getElementById('signup_form');
		if (form != null) {
			form.addEventListener('keyup', function (event) {
				signupFormFunc(form, event);
			}, false);
			form.addEventListener('change', function (event) {
				signupFormFunc(form, event);
			}, false);
			form.addEventListener('input', function (event) {
				signupFormFunc(form, event);
			}, false);
		}
	})();
	
	function signupFormFunc(form, event) {
		newpassword2.setCustomValidity(newpassword2.value != newpassword.value ? "Passwords do not match." : "");
		if (form.checkValidity() === false) {
			event.preventDefault();
			event.stopPropagation();
			document.getElementById('submit_button').setAttribute('disabled', 'disabled');
			document.getElementById('button_icon').className = "fas fa-lock";
		} else {
			document.getElementById('submit_button').removeAttribute('disabled');
			document.getElementById('button_icon').className = "fas fa-lock-open";
		}				
		form.classList.add('was-validated');
	}
});

var usernameInput = "";
var usernameTaken = false;

function validateUsername(string) {
	var pattern = /(?!(?:[^0-9]*[0-9]){4}|(?:[^\.]*[\.]){2})^[A-Za-z]{1}[A-Za-z0-9\.]{4,38}?[^\.]{1}$/g;
	var result = pattern.test(string);
	return result;
}

function validateEmail(string) {
	var pattern = /[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?/g;
	var result = pattern.test(string);
	return result;
}

function validateInput(inputValue, inputType, cFunction) {
	switch (inputType) {
		case "Username":
			if (validateUsername(inputValue)) {
				if (usernameInput == "") {
					usernameInput = inputValue;
					loadGetMethod("api/soad/"+inputType+"/"+inputValue, cFunction);
					break;
				}
				if (usernameInput != inputValue) {
					usernameInput = inputValue;
					loadGetMethod("api/soad/"+inputType+"/"+inputValue, cFunction);
					break;
				}
				break;
			}
			break;
		case "Email":
			if (validateEmail(inputValue)) {
				loadGetMethod("api/soad/"+inputType+"/"+inputValue, cFunction);
			}
			break;
	}
}

function loadGetMethod(url, cFunction) {
	var xhttp = new XMLHttpRequest();
	xhttp.open("GET", url, true);
	xhttp.onreadystatechange = function() {
		if (xhttp.readyState == 4 && xhttp.status == 200) {
			cFunction(xhttp);
		}
	};
	xhttp.send(null);
}

function loadPostMethod(url, cFunction, params) {
	var xhttp = new XMLHttpRequest();
	xhttp.open("POST", url, true);
	xhttp.onreadystatechange = function() {
		if (xhttp.readyState == 4 && xhttp.status == 200) {
			cFunction(xhttp);
		}
	};
	xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	xhttp.send(params);
}

function previousButtonClick() {
	$('#form_section2').addClass('d-none');
	$('#next_button_div').removeClass('d-none');
	$('#prev_submit_btn_row').addClass('d-none');
	document.getElementById('email').removeAttribute('readonly');
	document.getElementById('signup_form').classList.remove('was-validated');
}

function nextButtonClick(xhttp) {
	console.log(xhttp.responseText);
	var testBool = xhttp.responseText.toString().toLowerCase() == "false";
	if (testBool) {
		//console.log("Yes");
		$('#form_section2').removeClass('d-none');
		$('#next_button_div').addClass('d-none');
		$('#prev_submit_btn_row').removeClass('d-none');
		document.getElementById('email').setAttribute('readonly', true);
		$('#email').popover('dispose');
		document.getElementById('signup_form').classList.remove('was-validated');
		getCountryJSON();
	} else {
		//console.log("No...");
		$('#form_section2').addClass('d-none');
		$('#next_button_div').removeClass('d-none');
		$('#prev_submit_btn_row').addClass('d-none');
		showEmailFoundPopover();
	}
}

function showEmailFoundPopover() {
	var emailId = '#email';
	$(emailId).attr("data-placement", "right");
	$(emailId).attr("data-toggle", "popover");
	$(emailId).attr("data-trigger", "focus");
	$(emailId).attr("data-content", "<span class=\"text-danger\">Email address already in database!</span>");
	$(emailId).attr("data-html", "true");
	$(emailId).popover('show');
}

function getCountryJSON() {
	if (!document.getElementById('country').classList.contains('custom-select')) {
		var xhttp = new XMLHttpRequest();
		xhttp.open("GET", "resources/country.json", true);
		xhttp.onreadystatechange = function() {
			if (xhttp.readyState == 4 && xhttp.status == 200) {
				createCountryDropdown(JSON.parse(xhttp.responseText));
			}
		};
		xhttp.send(null);
	}
}

function createCountryDropdown(jsonData) {
	var container = document.getElementById('country');	
	container.classList.add('custom-select');
	var sortable = [];
	for (var key in jsonData) {
		sortable.push([jsonData[key], key]);
	}
	sortable.sort();
	//console.log(sortable);
	for (var i = 0; i < sortable.length; i++) {
		var option = document.createElement('option');
		option.value = sortable[i][1];
		option.innerHTML = sortable[i][0];
		if (sortable[i][1] == "NL") {
			option.selected = 'selected';
		}
		container.appendChild(option);
	}		
}

function myFunction2(xhttp) {
	//action goes here
}

function userInputCheck(xhttp) {
	var testBool = xhttp.responseText.toString().toLowerCase() == "false";
	if (testBool) {
		//console.log("Yes");
		//$('#form_section2').removeClass('d-none');
		//$('#next_button_div').addClass('d-none');
		//$('#prev_submit_btn_row').removeClass('d-none');
		//document.getElementById('email').setAttribute('disabled', 'disabled');
		$('#username').popover('dispose');
		document.getElementById('username').classList.remove('is-invalid');
		usernameTaken = false;
	} else {
		//console.log("No...");
		//$('#form_section2').addClass('d-none');
		//$('#next_button_div').removeClass('d-none');
		//$('#prev_submit_btn_row').addClass('d-none');
		document.getElementById('username').classList.add('is-invalid');
		usernameTaken = true;
		showUserFoundPopover();
	}
	username.setCustomValidity(usernameTaken ? "Username is already taken." : "");
}

function showUserFoundPopover() {
	var userId = '#username';
	$(userId).attr("data-placement", "right");
	$(userId).attr("data-toggle", "popover");
	$(userId).attr("data-trigger", "focus");
	$(userId).attr("data-content", "<span class=\"text-danger\">Username is already taken!</span>");
	$(userId).attr("data-html", "true");
	$(userId).popover('show');
}