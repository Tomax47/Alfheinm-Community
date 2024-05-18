
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
const form = document.querySelector('form');
const passwordInput = document.getElementById('password');
const passwordInvalidField = document.getElementById('passwordInvalidField');
const emailInput = document.getElementById('email');
const emailInvalidField = document.getElementById('emailInvalidField');
const signInBtn = document.getElementById('signInBtn');

// Regex form check
var emailRegex = /^[^\s'""` ]+@[^\s'""` ]+\.[^\s'""` ]+$/

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
    return emailRegex.test(email.value);
}

// Password validator
passwordInput.addEventListener("input", function () {

    if (!ValidatePassword(this)) {

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

    this.classList.remove('is-valid');
    this.classList.remove('is-invalid');
})

// Email validator
email.addEventListener("input", function () {

    if (!ValidateEmail(this.value)) {

        // Invalid email input
        if (passwordInput.classList.contains('is-valid')) {
            passwordInput.classList.remove('is-valid');
        }

        passwordInput.classList.add('is-invalid');
        emailInvalidField.innerText = "Invalid email";

        return;
    }

    // Valid email
    if (passwordInput.classList.contains('is-invalid')) {
        passwordInput.classList.remove('is-invalid');
        passwordInput.classList.add('is-valid');
    }

});

// Form submit
signInBtn.addEventListener('click', function (event) {

    event.preventDefault();

    if (ValidateEmail(emailInput) && ValidatePassword(passwordInput)) {
        // Submitting form
        form.submit();

        return;
    }

    handleError("Form contains invalid inputs")
});
