
// Error modal
function handleError(errorData) {
    const notyf = new Notyf({
        position: {
            x: 'center',
            y: 'top',
        },
        types: [
            {
                type: 'error',
                background: '#FA5252',
                icon: {
                    className: 'fas fa-times',
                    tagName: 'span',
                    color: '#fff'
                },
                dismissible: false
            }
        ]
    });
    notyf.open({
        type: 'error',
        message: errorData
    });
};

// Const
const usernameInput = document.getElementById('username');
const usernameInvalidField = document.getElementById('usernameInvalidField');
const passwordInput = document.getElementById('password');
const passwordInvalidField = document.getElementById('passwordInvalidField');
const passwordConfirmInput = document.getElementById('confirmPassword');
const passwordConfirmInvalidField = document.getElementById('confirmPasswordInvalidField');
const emailInput = document.getElementById('email');
const emailInvalidField = document.getElementById('emailInvalidField');
const formSubmitBtn = document.getElementById('signUpBtn');
const form = document.querySelector('form');
const termsCheckbox = document.getElementById('termsBox');

// Regex form check
var emailRegex =  /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|.(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
var usernameRegex = /^[a-zA-Z0-9]{1,15}$/;

// Username validation
function ValidateUsername(username) {
    if (usernameRegex.test(username.value) && username.value.length >= 4) {
        return true;
    }

    return false;
}

function CheckUsernameUniqueness() {

    // Sending the requests.
    $.ajax({
        type: "GET",
        url: "/register/usernameCheck?username=" + usernameInput.value,
        success: function(response) {
            console.log(`result ${response}`);

            if (response === true) {
                if (usernameInput.classList.contains('is-invalid')) {
                    usernameInput.classList.remove('is-invalid');
                }

                usernameInput.classList.add('is-valid');
                usernameInvalidField.innerText = "Looks goods";
                return;
            }

            // Taken username
            usernameInput.classList.add('is-invalid');
            usernameInvalidField.innerText = "Already taken username";
            return;
        },
        error: function (response) {
            handleError(response.responseJSON.message);
        },
        dataType: "json",
        contentType: "application/json"
    });
}
usernameInput.addEventListener('input', function () {

    if (ValidateUsername(this)) {
        // Valid username
        CheckUsernameUniqueness();
    }

    // Invalid username
    if (usernameInput.classList.contains('is-valid')) {
        usernameInput.classList.remove('is-valid');
    }

    usernameInput.classList.add('is-invalid');
    usernameInvalidField.innerText = "Invalid or too short username";

});

// Password validation
function ValidatePassword(password) {

    // Password check
    const isValidLength = password.value.length >= 8;
    const containMaliciousChars = /['`" ]/.test(password.value);

    // Test the password against the pattern
    if (isValidLength && !containMaliciousChars) {
        // Valid
        return true;
    } else if (isValidLength && containMaliciousChars) {
        // Contains invalid chars
        return false;
    } else if (!isValidLength && !containMaliciousChars) {
        // Too short
        return false;
    }
}

function ValidateEmail(email) {

    if (email.value.toLowerCase().match(emailRegex) && !(/['"` ]/.test(email.value))) {
        return true;
    }

    return false;
}

function IsCorrectConfirmPassword(confirmPassword) {

    if (confirmPassword.value === passwordInput.value) {
        return true;
    }

    return false;
}

// Password validator
passwordInput.addEventListener("input", function () {

    if (!ValidatePassword(this) && this.value.length > 2) {

        // Invalid password input
        passwordInput.classList.add('is-invalid');
        passwordInvalidField.innerText = "Invalid Password";

        return;
    }

    // Valid
    if (passwordInput.classList.contains('is-invalid')) {
        passwordInput.classList.remove('is-invalid');
    }

});

passwordInput.addEventListener('blur', function () {

    if (this.value.length === 0) {
        this.classList.remove('is-valid');
        this.classList.remove('is-invalid');
    }

});

// Confirm password validator
passwordConfirmInput.addEventListener("input", function () {

    if (IsCorrectConfirmPassword(this)) {
        // Match
        if (passwordConfirmInput.classList.contains('is-invalid')) {
            passwordConfirmInput.classList.remove('is-invalid')
        }

        passwordConfirmInput.classList.add('is-valid');
        passwordInvalidField.innerText = "Looks good";
        return;
    }

    // Mismatch
    if (passwordConfirmInput.classList.contains('is-valid')) {
        passwordConfirmInput.classList.remove('is-valid');
    }

    passwordConfirmInput.classList.add('is-invalid');
    passwordConfirmInvalidField.innerText = "Passwords ain't match";

});

// Email validator
emailInput.addEventListener("input", function () {

    if (ValidateEmail(emailInput)) {
        // Valid email
        console.log("0000000000000000000")
        if (emailInput.classList.contains('is-invalid')) {
            emailInput.classList.remove('is-invalid');
        }
        emailInput.classList.add('is-valid');
        emailInvalidField.innerText = "Valid email";
        return true;
    } else {
        // Invalid email
        console.log("1111111111111111111")
        if (emailInput.classList.contains('is-valid')) {
            emailInput.classList.remove('is-valid');
        }
        emailInput.classList.add('is-invalid');
        emailInvalidField.innerText = "Invalid email format";
        return false;
    }

});

// Form submit
formSubmitBtn.addEventListener('click', function (event) {

    event.preventDefault();

    if (ValidateEmail(emailInput) &&
        ValidatePassword(passwordInput) &&
        IsCorrectConfirmPassword(passwordConfirmInput) &&
        ValidateUsername(usernameInput)) {

        if (termsCheckbox.checked) {

            formSubmitBtn.disabled = true;
            
            // Submitting form
            let formData = new FormData(form);

            fetch('/register', {
                method: 'POST',
                body: formData
            })
                .then(response => {
                    if (response.ok) {
                        //Success
                        setTimeout(
                            function () {
                                window.location.replace("/confirm/process/"+formData.get("email"));
                            }, 1500
                        );

                    } else if (response.status === 409) {
                        throw new Error("Username or Email already exist");
                    } else if (response.status === 500) {
                        throw new Error("Something went wrong");
                    } else if (response.status === 400) {
                        throw new Error("You have made a bad request");
                    }
                })
                .catch(error => {
                    handleError(error);
                });
            return;
        }

        handleError("Please check the 'Terms & Conditions' box");
        return;
    }

    handleError("Form contains invalid inputs")
});

