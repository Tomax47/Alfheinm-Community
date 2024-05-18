
const swalWithBootstrapButtons = Swal.mixin({
    customClass: {
        confirmButton: 'btn btn-primary',
        cancelButton: 'btn btn-gray'
    },
    buttonsStyling: false
});

// Handle success modal
function handleSuccessModal(messageData) {
    swalWithBootstrapButtons.fire({
        icon: 'success',
        title: messageData.title,
        text: messageData.text,
        showConfirmButton: true,
        timer: 1500
    });
};
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
const passwordInput = document.getElementById('password');
const passwordInvalidField = document.getElementById('passwordInvalidField');
const passwordConfirmInput = document.getElementById('confirmPassword');
const passwordConfirmInvalidField = document.getElementById('confirmPasswordInvalidField');
const formSubmitBtn = document.getElementById('passwordResetBtn');
const form = document.querySelector('form');

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

formSubmitBtn.addEventListener('click', function (){

    this.disabled = true;

    if (ValidatePassword(passwordInput) && IsCorrectConfirmPassword(passwordConfirmInput)) {
        // Valid password. Sending request
        // Submitting form
        let formData = new FormData(form);

        fetch('/profile/password/reset', {
            method: 'POST',
            body: formData
        })
            .then(response => {
                if (response.ok) {

                    let resp = response.body;
                    // Success
                    handleSuccessModal({
                        title: "Success!",
                        text: "Your password has been changed successfully"
                    })

                    setTimeout(
                        function () {
                            window.location.replace("/profile");
                        }, 1500
                    );

                } else if (response.status === 404) {
                    throw new Error("User ain't found");
                } else if (response.status === 500) {
                    throw new Error("Something went wrong");
                } else {
                    throw Error("Unknown Error")
                }
            })
            .catch(error => {
                handleError(error);
            });
        return;
    }

    handleError("Please provide a valid password!")
});