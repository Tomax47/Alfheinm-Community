
// Modals
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
const emailInput = document.getElementById('email');
const passwordInput = document.getElementById('password');
const passwordConfirmInput = document.getElementById('confirmPassword');
const formSubmitBtn = document.getElementById('passwordResetBtn')
const resetToken = document.getElementById('resetToken');
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

// Email validator
emailInput.addEventListener("input", function () {

    this.value = '';
});

formSubmitBtn.addEventListener('click', function (event) {
   event.preventDefault();

   formSubmitBtn.disabled = true;

   if (resetToken === null) {
       // Invalid token
       handleError("Invalid token. Make another request");

       setTimeout(
           function () {
               window.location.replace("/login/password/recover");
           }, 1500
       );
   }

   if (ValidatePassword(passwordInput) &&
       IsCorrectConfirmPassword(passwordConfirmInput)) {

       // Submitting form
       let formData = new FormData(form);

       fetch('/password/reset?reset-verification-code=' + resetToken.innerHTML, {
           method: 'POST',
           body: formData
       })
           .then(response => {
               if (response.ok) {
                   //Success
                   handleSuccessModal({
                      title: "Success!",
                      text: "Your password has been successfully changed"
                   });

                   setTimeout(
                       function () {
                           window.location.replace("/login");
                       }, 1500
                   );

               } else if (response.status === 410) {
                   // Record expired
                   handleError("Password reset request expired. Do a new one...")

                   setTimeout(
                       function () {
                           window.location.replace("/login/password/recover");
                       }, 1500
                   );

               } else if (response.status === 404) {
                   // Record not found
                   handleError("No valid request has been found. Submit a request...")

                   setTimeout(
                       function () {
                           window.location.replace("/login/password/recover");
                       }, 1500
                   );

               } else {
                   throw new Error("Something went wrong");
               }
           })
           .catch(error => {
               handleError(error);
           });
       return;
   }

   // Invalid data
    handleError("Form contains invalid data");
});

