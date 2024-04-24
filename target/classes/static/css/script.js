let y = document.getElementById("btn");
let role = document.getElementById("role").value;
let textBoxDiv = document.getElementById("textBoxDiv");

function register() {
	y.style.left = "50px";
}

function validatePassword() {
	let password = document.getElementById("password").value;
	let confirmPassword = document.getElementById("confirmPassword").value;

	if (password != confirmPassword) {
		document.getElementById("passwordmessage").innerHTML = "Passwords do not match!";
		return false;
	} else {
		document.getElementById("passwordmessage").innerHTML = "";
		return true;
	}
}

window.onload = function() {
	let confirmPasswordInput = document.getElementById("confirmPassword");
	confirmPasswordInput.addEventListener("blur", function() {
		validatePassword();
	});
};

document.addEventListener('DOMContentLoaded', function() {
	function addToCart() {
		let customAlert = document.getElementById('customAlert');
		customAlert.style.display = 'block';
		customAlert.innerHTML = 'Added to Cart';
		setTimeout(() => {
			customAlert.style.display = 'none';
		}, 4000);
	}
	addToCart();
});

const urlParams = new URLSearchParams(window.location.search);
const message = urlParams.get('message');  
if (message) {                     
	alert(message);
}

