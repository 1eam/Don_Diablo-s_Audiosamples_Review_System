/**
* Password Validator Popover for Bootstrap 4
* Based on the version made by Brian Nippert https://www.jqueryscript.net/form/Password-Strenth-Checker-Validator-Bootstrap.html
* Changes made: - This version is working with a strict CSP header (original only allowed 'unsafe inline').
*				- Disabled the progressbar (it failed to properly load)
*				- Added color responses and a whitespace check
*				- Updated and modified regular expressions
* TODO: Clean up code, add descriptions for all methods.
* Author: Bart Loeffen
* Date: 03/August/2019
* Version: 1.1
*/


/**
* This section should be in a seperate JavaScript file, or inline on the page.
*/
$(document).ready(function () {
	// Configure MinSize -- default is 5
	PasswordValidator.minSize = 10;
	// Configure MaxSize -- default is 15
	PasswordValidator.maxSize = 32;
	
	// whether you want to validate on prohibited characters 
	//PasswordValidator.prohibitedConfigured = false;
	
	PasswordValidator.setup('newpassword','newpassword2');
});


//Count variable ued for progress bar
var count = 0;

var PasswordValidator = new function () {
	this.minSize = 5;
	this.maxSize = 15;
    this.lengthConfigured = true;
	this.lowercaseConfigured = true;
    this.uppercaseConfigured = true;
    this.digitConfigured = true;
    this.specialConfigured = true;
    this.prohibitedConfigured = true;
	this.whitespaceConfigured = true;

    //this.specialCharacters = ['_', '#', '%', '*', '@'];
    this.specialCharacters = ['!','@','#','$','%','&','_','*','-','?'];
    this.prohibitedCharacters = ['$', '&', '=', '!'];
	
	this.lengthCheckPassed = false;
	this.lowercaseCheckPassed = false;
	this.uppercaseCheckPassed = false;
	this.digitCheckPassed = false;
	this.specialCheckPassed = false;
	this.whitespaceCheckPassed = false;

    this.elementnumber = 0;
    this.setup = function (passwordField, verifyField) {
        //console.log(passwordField);
        this.elementnumber++;
        var passwordFieldEle= $('#'+passwordField);
        this.addPasswordField(passwordFieldEle,);
        if(verifyField !== undefined) {
            var verifyFieldEle= $('#'+verifyField);
            this.addVerifyField(verifyFieldEle,$(passwordFieldEle).attr('id'));
        }
    }

    this.addPasswordField = function (passwordElement) {
        num = this.elementnumber;
		passwordElementID = $(passwordElement).attr('id');
        //Set Popover Attributes
        $(passwordElement).attr("data-placement", "right");
        $(passwordElement).attr("data-toggle", "popover");
        $(passwordElement).attr("data-trigger", "focus");
        $(passwordElement).attr("data-html", "true");
        $(passwordElement).attr("title", "Password Requirements");
        //$(passwordElement).attr("onfocus", "PasswordValidator.onFocus(this," + num + ")");
        //console.log(passwordElementID);
		document.getElementById(passwordElementID).addEventListener('focus', function (event) {
			PasswordValidator.onFocus(event.target, num);
		}, false);
        //$(passwordElement).attr("onblur", "PasswordValidator.onBlur(this," + num + ")");
		document.getElementById(passwordElementID).addEventListener('blur', function (event) {
			PasswordValidator.onBlur(event.target, num);
		}, false);
        //$(passwordElement).attr("onkeyup", "PasswordValidator.checkPassword(this," + num + ")");
		document.getElementById(passwordElementID).addEventListener('keyup', function (event) {
			PasswordValidator.checkPassword(event.target, num);
		}, false);

        //Create progress bar container
        /*var progressBardiv = document.createElement("div");

        progressBardiv.id = "progress" + num;
        $(progressBardiv).addClass("progress");

        //Progress bar element
        var progressBar = document.createElement("div");
        $(progressBar).addClass("progress-bar");
        $(progressBar).addClass("bg-info");
        progressBar.id = "progressBar" + num;
        $(progressBar).attr("role", "progressbar");
        $(progressBar).attr("aria-valuenow", "100");
        $(progressBar).attr("aria-valuemin", "0");
        $(progressBar).attr("aria-valuemax", "100");
        $(progressBar).css("width", "0%");
        $(progressBardiv).append(progressBar);

        //Add popover data including the progress bar
        $(passwordElement).attr("data-content", '&bull; Between 10-12 Characters <br/>&bull; An upper Case Letter<br/>&bull; A Number<br/>&bull; At Least 1 of the Following (_,-,#,%,*,+)<br/>&bull; None of the Following ($,&,=,!,@)<br/>' + progressBardiv.outerHTML);
		*/
    }

    //TODO: Add validation to check the repeat password field
    this.addVerifyField = function (verifyElement, passwordElementID) {
		verifyElementID = $(verifyElement).attr('id');
        $(verifyElement).attr("data-placement", "right");
        $(verifyElement).attr("data-toggle", "popover");
        $(verifyElement).attr("data-trigger", "focus");
        $(verifyElement).attr("data-content", "Passwords do not match!");
        $(verifyElement).attr("data-html", "true");
        //$(verifyElement).attr("onfocus", "PasswordValidator.checkVerify(this,'" + passwordElementID + "')");
		
		document.getElementById(verifyElementID).addEventListener('focus', function (event) {
			PasswordValidator.checkVerify(event.target, passwordElementID);
		}, false);
        //$(verifyElement).attr("onkeyup", "PasswordValidator.checkVerify(this,'" + passwordElementID + "')");
		document.getElementById(verifyElementID).addEventListener('keyup', function (event) {
			PasswordValidator.checkVerify(event.target, passwordElementID);
		}, false);
		
    }

    //TODO: Check to see if the 2 passwords are the same
    this.checkVerify = function (verifyElement, passwordElementID) {

        if (verifyElement.value == $('#' + passwordElementID).val()) {
            $(verifyElement).popover('hide');
            $(verifyElement).addClass("has-success");
        } else {
            $(verifyElement).popover('show');
            $(verifyElement).removeClass("has-success");
            //var popover = $(verifyElement).attr("data-content", 'Passwords do not match!');
            //popover.setContent();
        }

    }

    this.checkPassword = function (e, num) {
        // var id = e.id;
        // var num = id.match(/\d/g);
        // num = num.join("");
        count = 0;
        var password = e.value;

        var length = this.lengthConfigured ? this.checkLength(password) : '';
		var lower = this.lowercaseConfigured ? this.checkLowerCase(password) : '';
        var upper = this.uppercaseConfigured ? this.checkUpperCase(password) : '';
        //var digit = this.digitConfigured ? this.checkDigit(password) : '';
        var digit = this.digitConfigured ? this.checkCountDigit(password) : '';
        var special = this.specialConfigured ? this.checkCountSpecialCharacter(password) : '';
        //var special = this.specialConfigured ? this.checkSpecialCharacters(password) : '';
        //var prohibited = this.prohibitedConfigured ? this.checkProhibitedCharacter(password) : '';
		var whitespace = this.whitespaceConfigured ? this.checkWhitespace(password) : '';
        //if (length.length + lower.length + upper.length + digit.length + special.length == 0) {
		if (this.lengthCheckPassed && this.lowercaseCheckPassed && this.uppercaseCheckPassed
			&& this.digitCheckPassed && this.specialCheckPassed && this.whitespaceCheckPassed) {
            $(e).popover('hide');
            //$(e).addClass("is-invalid");
            return true;
        } else {
            $(e).popover('show');
            //$(e).removeClass("is-valid");
            //setProgressBar(count, e, num);
            //var popover = $(e).attr("data-content", length + lower + upper + digit + special + prohibited + '<br/>' + document.getElementById("progress" + num).outerHTML).data('bs.popover');
			var popover = $(e).attr("data-content", length + lower + upper + digit + special + whitespace).data('bs.popover');
            popover.setContent();
            return false;
        }

    }

    /**
     * Checks to see if the password contains 1 till 2 special character
     * @param string password to test
     * @returns {string} string to add to the popover
     */
    this.checkCountSpecialCharacter = function (string) {
		var specialCount = string.replace(/[^!@#$%&_*\-?]/g, "").length;
        if ((specialCount >= 1) && (specialCount <= 2)) {
			this.specialCheckPassed = true;
            return addPopoutLine("<span class=\"text-success\">1 or 2 of the following " + this.specialCharacters.join(' ') + " <i class=\"fas fa-check\"></i></span>");
        } else if (specialCount > 2) {
			this.specialCheckPassed = false;
            return addPopoutLine("<span class=\"text-danger\">Less then 3 of the following " + this.specialCharacters.join(' ') + " <i class=\"fas fa-times\"></i></span>");
        } else {
			this.specialCheckPassed = false;
            return addPopoutLine("1 or 2 of the following " + this.specialCharacters.join(' '));
        }
    }

    /**
     * Checks to see if the password contains an approved special character
     * @param string password to test
     * @returns {string} string to add to the popover
     */
    this.checkSpecialCharacters = function (string) {
        // var specialChar = new RegExp("[_\\-#%*\\+]");
        var specialChar = new RegExp("[" + this.specialCharacters.join('') + "]");
      
        if (specialChar.test(string) == false) {
            return addPopoutLine("At Least 1 of the Following (" + this.specialCharacters.join(',') + ")");
        } else {
            count++;
            return "";
        }
    }

    /**
     * Checks to see if any prohibited special characters are present in the password.
     * @param string passwor dot test
     * @returns {string} string to add to the popover
     */
    this.checkProhibitedCharacter = function (string) {
        // var specialChar = new RegExp("[$&=!@]");//= /[$&=!@]/;
        var specialChar = new RegExp("[" + this.prohibitedCharacters.join('') + "]");

        if (specialChar.test(string) == true) {
            return addPopoutLine("None of the Following (" + this.prohibitedCharacters.join(',') + ")");
        } else {
            count++;
            return "";
        }
    }

    /**
     * Checks to see if there is at least 1 digit in the password
     * @param string password to test
     * @returns {string} string to add to the popover
     */
    this.checkDigit = function checkDigit(string) {
        var hasNumber = /\d/;
        if (hasNumber.test(string) == false) {
            return addPopoutLine("A Number");
        } else {
            count++;
            return "";
        }
    }

    /**
     * Checks to see if there is 1 till 3 digit in the password
     * @param string password to test
     * @returns {string} string to add to the popover
     */
    this.checkCountDigit = function checkCountDigit(string) {
		var digitCount = string.replace(/[^0-9]/g, "").length;
        if ((digitCount >= 1) && (digitCount <= 3)) {
			this.digitCheckPassed = true;
            return addPopoutLine("<span class=\"text-success\">Between 1-3 numbers <i class=\"fas fa-check\"></i></span>");
        } else if (digitCount > 3) {
			this.digitCheckPassed = false;
            return addPopoutLine("<span class=\"text-danger\">Less then 4 numbers <i class=\"fas fa-times\"></i></span>");
        } else {
			this.digitCheckPassed = false;
            return addPopoutLine("Between 1-3 numbers");
        }
    }

    /**
     * Checks to see if there is no whitespace character in the password
     * @param string password to test
     * @returns {string} string to add to the popover
     */
    this.checkWhitespace = function checkWhitespace(string) {
        var hasWhitespace = /\s/;
        if (hasWhitespace.test(string) == true) {
			this.whitespaceCheckPassed = false;
            return addPopoutLine("<span class=\"text-danger\">No whitespace allowed! <i class=\"fas fa-times\"></i></span>");
        } else {
			this.whitespaceCheckPassed = true;
            return "";
        }
    }

    /**
     * Checks to ensure at least 1 character is lower case
     * @param string password to test
     * @returns {string} string to add to the popover
     */
    this.checkLowerCase = function (string) {
        if (string.replace(/[^a-z]/g, "").length == 0) {
			this.lowercaseCheckPassed = false;
            return addPopoutLine("An lower case letter");
        } else {
            count++;
			this.lowercaseCheckPassed = true;
            return addPopoutLine("<span class=\"text-success\">An lower case letter <i class=\"fas fa-check\"></i></span>");
			//return "";
        }
    }

    /**
     * Checks to ensure at least 1 character is upper case
     * @param string password to test
     * @returns {string} string to add to the popover
     */
    this.checkUpperCase = function (string) {
        if (string.replace(/[^A-Z]/g, "").length == 0) {
			this.uppercaseCheckPassed = false;
            return addPopoutLine("An upper case letter");
        } else {
            count++;
			this.uppercaseCheckPassed = true;
            return addPopoutLine("<span class=\"text-success\">An upper case letter <i class=\"fas fa-check\"></i></span>");
			//return "";
        }
    }

    /**
     * Checks the length of the password
     * @param string password to test
     * @returns {string} string to add to the popover
     */
    this.checkLength = function (string) {
        if (string.length > this.maxSize || string.length < this.minSize) {
			this.lengthCheckPassed = false;
            return addPopoutLine("Between " + this.minSize + "-" + this.maxSize + " characters");
        } else {
            count++;
			this.lengthCheckPassed = true;
            return addPopoutLine("<span class=\"text-success\">Between " + this.minSize + "-" + this.maxSize + " characters <i class=\"fas fa-check\"></i></span>");
            //return "";
        }

    }

    /**
     * sets the progress bar (e) to the percent
     * @param percent percent to set progress bar to
     * @param e  password field element
     */
    function setProgressBar(percent, e, num) {
        percentNum = (percent / 5) * 100;
        percent = percentNum.toString() + "%";
        $("#progressBar" + num).css("width", percent);
    }

    /**
     * returns string that is formatted with a bullet point and <br> at the end for the popover.
     * @param string popover text
     * @returns {string} formatted popover string
     */
    function addPopoutLine(string) {
        return "&bull; " + string + "<br/>";
    }

    /**
     * On focus event that checks the password when the focus is gained.
     * @param e password element
     */
    this.onFocus = function (e, num) {
		//console.log("bla");
		//console.log(event);
        //this.checkPassword(event.target, num);

        this.checkPassword(e, num);
    }

    this.onBlur = function (e, num) {
        //if (this.checkPassword(e, num) == false) {

        //	}
    }
}