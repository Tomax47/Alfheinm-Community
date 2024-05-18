
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
const recoverPasswordBtn = document.getElementById('recoverPasswordBtn');
const form = document.querySelector('form');

var emailRegex =  /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|.(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
function ValidateEmail(email) {

    if (email.value.toLowerCase().match(emailRegex) && !(/['"` ]/.test(email.value))) {
        return true;
    }

    return false;
}

emailInput.addEventListener("input", function () {

    if (ValidateEmail(emailInput)) {
        // Valid email
        if (emailInput.classList.contains('is-invalid')) {
            emailInput.classList.remove('is-invalid');
        }
        emailInput.classList.add('is-valid');
        emailInvalidField.innerText = "Valid email";
        return true;
    } else {
        // Invalid email
        if (emailInput.classList.contains('is-valid')) {
            emailInput.classList.remove('is-valid');
        }
        emailInput.classList.add('is-invalid');
        emailInvalidField.innerText = "Invalid email format";
        return false;
    }

});

recoverPasswordBtn.addEventListener('click', function (event) {

    event.preventDefault();
    recoverPasswordBtn.disabled = true;

    if (ValidateEmail(emailInput)) {
        // Submitting req

        fetch('/login/password/recover?email='+emailInput.value, {
            method: 'POST',
        })
            .then(response => {
                if (response.ok) {

                    let resp = response.body;
                    // Success
                    handleSuccessModal({
                        title: "Success!",
                        text: "The password reset Email has been sent to your Email address"
                    })

                    setTimeout(
                        function () {
                            window.location.replace("/login");
                        }, 1500
                    );

                } else if (response.status === 404) {
                    // Redirect /register
                    handleError("User does not exist");

                    setTimeout(
                        function () {
                            window.location.replace("/register");
                        }, 1500
                    );

                } else if (response.status === 409) {
                    // Redirect /login
                    handleError("You already have an active record. Try after 5 minutes");

                    setTimeout(
                        function () {
                            window.location.replace("/login");
                        }, 1500
                    );

                } else {
                    throw Error("Something went wrong")
                }
            })
            .catch(error => {
                handleError(error);
            });
        return;
    }

    handleError("Invalid Email Format");
});