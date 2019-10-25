$(document).ready(function() {
	var obj = $("#dragandrophandler");
	obj.on('dragenter', function (e) {
		e.stopPropagation();
		e.preventDefault();
		$(this).css('border', '2px solid #0B85A1');
	});
	obj.on('dragover', function (e) {
		e.stopPropagation();
		e.preventDefault();
	});
	obj.on('drop', function (e) {
		
		$(this).css('border', '2px dotted #0B85A1');
		e.preventDefault();
		var files = e.originalEvent.dataTransfer.files;
		
		//We need to send dropped files to Server
		handleFileUpload(files,obj);
	});
	$(document).on('dragenter', function (e) {
		e.stopPropagation();
		e.preventDefault();
	});
	$(document).on('dragover', function (e) {
		e.stopPropagation();
		e.preventDefault();
		obj.css('border', '2px dotted #0B85A1');
	});
	$(document).on('drop', function (e) {
		e.stopPropagation();
		e.preventDefault();
	}); 
});

function handleFileUpload(files,obj) {
	var fileSize = files[0].size;
	var fileName = files[0].name;
	var fileType = files[0].type;
	if ((checkFileSize(fileSize)) && (checkFileType(fileType))) {
		$('.custom-file-label').text(fileName);
		file_selector.files = files;
		document.getElementById('file_button').removeAttribute('disabled');
		document.getElementById('button_icon').className = "fas fa-lock-open";
	} else {
		event.preventDefault();
		event.stopPropagation();
		document.getElementById('file_button').setAttribute('disabled', 'disabled');
		document.getElementById('button_icon').className = "fas fa-lock";
	}
	document.getElementById('demo_form').classList.add('was-validated');
}

var loadingTimer;
var loadingTextArray = [
	"Still busy processing your demo...",
	"If you look left, you don't see anything on your right.",
	"\"Love is a serious mental disease\" -Plato",
	"\"If you want to be happy, be\" -Leo Tolstoy",
	"\"Life is trying things to see if they work\" -Ray Bradbury",
	"\"Nothing is impossible, the word itself says 'I'm possible'\" -Audrey Hepburn"];

function hideLoadingScreen() {
	clearInterval(loadingTimer);
	document.body.removeChild(document.getElementById('vinyl_div'));
	showUploadSucces();
}

function showLoadingScreen() {
	loadingTimer = setInterval(showLoadingText, 2000);
	var outerDiv = document.createElement('div');
	outerDiv.setAttribute('id', 'vinyl_div');
	outerDiv.classList.add('dimScreen');
	var innerDiv = document.createElement('div');
	innerDiv.classList.add('container', 'h-100', 'd-flex', 'flex-column', 'align-items-center', 'justify-content-center');
	var svgBody = '<g><g><circle  cx="27.667" cy="27.667" r="3.618"/><path d="M27.667,0C12.387,0,0,12.387,0,27.667s12.387,27.667,27.667,27.667s27.667-12.387,27.667-27.667 S42.947,0,27.667,0z M17.118,6.881c3.167-1.61,6.752-2.518,10.549-2.518c0.223,0,0.444,0.003,0.665,0.009 c0.367,0.01,0.619,0.922,0.564,2.025l-0.282,5.677c-0.055,1.103-0.289,1.986-0.523,1.979c-0.141-0.004-0.282-0.006-0.424-0.006 c-1.997,0-3.894,0.43-5.603,1.202c-1.007,0.455-2.212,0.184-2.774-0.767l-2.896-4.897C15.832,8.634,16.133,7.382,17.118,6.881z M15.986,17.295l-4.278-3.742c-0.832-0.727-0.918-1.994-0.119-2.756c0.019-0.018,0.037-0.035,0.057-0.053 c0.802-0.76,2.059-0.605,2.737,0.266l3.494,4.484c0.679,0.871,0.837,1.889,0.391,2.314C17.821,18.235,16.818,18.022,15.986,17.295 z M17.877,27.667c0-5.407,4.383-9.79,9.79-9.79s9.79,4.383,9.79,9.79s-4.383,9.79-9.79,9.79S17.877,33.074,17.877,27.667z M38.17,48.476c-3.156,1.596-6.725,2.495-10.503,2.495c-0.248,0-0.495-0.004-0.741-0.011c-0.409-0.013-0.692-0.929-0.632-2.032 l0.31-5.676c0.061-1.103,0.322-1.981,0.586-1.972c0.158,0.005,0.317,0.008,0.477,0.008c1.834,0,3.582-0.362,5.179-1.018 c1.022-0.42,2.275-0.144,2.877,0.782l3.101,4.77C39.426,46.747,39.156,47.977,38.17,48.476z M43.619,44.656 c-0.766,0.72-2.005,0.551-2.703-0.305l-3.59-4.407c-0.698-0.856-0.876-1.848-0.435-2.255c0.442-0.407,1.443-0.179,2.274,0.549 l4.28,3.744C44.277,42.709,44.386,43.936,43.619,44.656z"/></g></g>';
	var svg = '<svg class="vinyl-logo" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 55.334 55.334" >' + svgBody + '</svg>';
	innerDiv.innerHTML = '' + svg;
	var textSpan = document.createElement('span');
	textSpan.setAttribute('id', 'text_span_id');
	textSpan.innerHTML = 'Processing audio...';
	innerDiv.appendChild(textSpan);
	outerDiv.appendChild(innerDiv);
	document.body.insertBefore(outerDiv, document.body.firstChild);
	document.body.appendChild(outerDiv);
	
	//console.log(getComputedStyle(document.getElementById('navbarid')).zIndex);
}

function showUploadSucces() {
	var successDiv = document.createElement('div');
	successDiv.classList.add('row', 'text-center');
	var successColDiv = document.createElement('div');
	successColDiv.classList.add('col-lg-12', 'col-md-12', 'col-sm-12', 'col-12');
	successColDiv.innerHTML = "<img src=\"images/donparty.gif\">";
	successColDiv.innerHTML += "<h3 class=\"text-light bg-secondary point_nine_opacity\">Thank you for dropping a demo!</h3>";
	successDiv.appendChild(successColDiv);
	document.getElementById('form_card_body').innerHTML = successDiv.outerHTML;
}

function showLoadingText() {
	document.getElementById('text_span_id').innerHTML = loadingTextArray[Math.floor(Math.random() * loadingTextArray.length)];
}

function sendFileToServer(formData) {	
	var xhttp = new XMLHttpRequest(),
		last_response_len = false;
	xhttp.open("POST", "DemoUpload" , true);
	xhttp.onreadystatechange = function() {
		if (xhttp.readyState == 4 && xhttp.status == 200) {
			console.log("Upload success");
			hideLoadingScreen();
			//cFunction(xhttp);
		}
	};
	xhttp.onprogress = function (e) {
		var this_response, response = e.currentTarget.response;
		if (last_response_len === false) {
			this_response = response;
			last_response_len = response.length;
		} else {
			this_response = response.substring(last_response_len);
			last_response_len = response.length;
		}
		this_response = this_response.replace(/\r?\n|\r/g, "");
		if (this_response == "something something something darkside") {
			console.log("Success!");
		} else {
			console.log("Still processing...|" + this_response + "|");
		}
	};
	xhttp.send(formData);
}

$('#file_button').click(function () {
	console.log(file_selector.files[0].name + " " + file_selector.files[0].size);
	var fd = new FormData();
	fd.append('file', file_selector.files[0]);
	fd.append('title', title.value);
	sendFileToServer(fd);
	showLoadingScreen();
});