$(document).ready(function() {
	(function () {
		'use strict';
		var form = document.getElementById('changepassword_form');
		if (form != null) {
			form.addEventListener('keyup', function (event) {
				changePasswordFormFunc(form, event);
			}, false);
			form.addEventListener('change', function (event) {
				changePasswordFormFunc(form, event);
			}, false);
			form.addEventListener('input', function (event) {
				changePasswordFormFunc(form, event);
			}, false);
		}
	})();
	
	(function () {
		'use strict';
		var form = document.getElementById('login_form');
		if (form != null) {
			form.addEventListener('keyup', function (event) {
				loginFormFunc(form, event);
			}, false);
			form.addEventListener('change', function (event) {
				loginFormFunc(form, event);
			}, false);
			form.addEventListener('input', function (event) {
				loginFormFunc(form, event);
			}, false);
		}
	})();

	$("#invalidCheck").click( function () {
		if ($( '#invalidCheck' ).prop( 'checked')) {
			$( '#register_btn' ).prop( 'disabled', false );
		} else {
			$( '#register_btn' ).prop( 'disabled', true );
		}
	});

	$('#msgCharsLeft').text('32 characters left');
	$("#address").keyup( function () {
		const maxChars = 32;
		var msgLength = $(this).val().length;

		if ( msgLength >= maxChars ) {
			$('#msgCharsLeft').text('You have reached the limit of ' + maxChars + ' characters.');
			// change the color of the message (to red)
			$('#msgCharsLeft').addClass('msgCharLimitColor'); // this class is style.css
		} else {
			var left = maxChars - msgLength;
			$('#msgCharsLeft').text( left + ' character' + (left>1?'s':'') + ' left' );
			$('#msgCharsLeft').removeClass('msgCharLimitColor');
		}
	});

	$('input[type="file"]').change(function(e) {
		'use strict';
		var form = document.getElementById('demo_form');
		demoFormFunc(form, e);
	});
});

function demoFormFunc(form, event) {
	var fileName = event.target.files[0].name;
	var fileSize = event.target.files[0].size;
	var fileType = event.target.files[0].type;
	if ((checkFileSize(fileSize)) && (checkFileType(fileType))) {
		$('.custom-file-label').text(fileName);
		document.getElementById('file_button').removeAttribute('disabled');
		document.getElementById('button_icon').className = "fas fa-lock-open";
	} else {
		event.preventDefault();
		event.stopPropagation();
		document.getElementById('file_button').setAttribute('disabled', 'disabled');
		document.getElementById('button_icon').className = "fas fa-lock";
	}
	form.classList.add('was-validated');
}
	
function changePasswordFormFunc(form, event) {
	newpassword2.setCustomValidity(newpassword2.value != newpassword.value ? "Passwords do not match." : "");
	if (form.checkValidity() === false) {
		event.preventDefault();
		event.stopPropagation();
		document.getElementById('login_button').setAttribute('disabled', 'disabled');
		document.getElementById('button_icon').className = "fas fa-lock";
	} else {
		document.getElementById('login_button').removeAttribute('disabled');
		document.getElementById('button_icon').className = "fas fa-lock-open";
	}
	form.classList.add('was-validated');
}
	
function loginFormFunc(form, event) {
	if (form.checkValidity() === false) {
		event.preventDefault();
		event.stopPropagation();
		document.getElementById('login_button').setAttribute('disabled', 'disabled');
		document.getElementById('button_icon').className = "fas fa-lock";
	} else {
		document.getElementById('login_button').removeAttribute('disabled');
		document.getElementById('button_icon').className = "fas fa-lock-open";
	}
	form.classList.add('was-validated');
}

function checkFileSize(fileSize) {
	if (fileSize > (1024 * 1024 * 10)) {
		return false;
	}
	return true;
}

function checkFileType(fileType) {
	if ((fileType == "video/ogg") || (fileType == "audio/mpeg") || (fileType == "audio/wav")) {
		return true;
	}
	return false;
}

$(function () {
  'use strict'

  $('[data-toggle="offcanvas"]').on('click', function () {
    $('.offcanvas-collapse').toggleClass('open')
  })
})