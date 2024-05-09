console.log("CUSTOM ALERT ADMIN PROFILE PAGE SCRIPT HOOKED.")
const swalWithBootstrapButtons = Swal.mixin({
    customClass: {
        confirmButton: 'btn btn-primary',
        cancelButton: 'btn btn-gray'
    },
    buttonsStyling: false
});

let username = document.getElementById('username').textContent.trim().slice(1);
let userRole = document.getElementById('userRole').textContent.trim();
let REFRESH_DELAY = 1500;

console.log(`USERNAME : ${username}\nROLE : ${userRole}`)

// ## MODALS ##

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
        message: errorData.errorMessage
    });
};

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

// Warning modal
function handleWarningModal(messageData) {
    swalWithBootstrapButtons.fire(
        messageData.title,
        messageData.text,
        'warning'
    );
};

// Send Delete request
function makeDeleteUserRequest(url) {
    console.log("SENDING A DELETE REQUEST...");

    $.ajax({
        type: "POST",
        url: url,
        success: function(xhr) {
            console.log(`Response : ${xhr}`);
            window.location.replace("/admin/users");
        },
        error: function(xhr) {
            // Handle the error response
            if (xhr.responseText === "OK") {
                // TODO: FIX IT SO THE SUCCESS GETS HOOKED UPON A SUCCESSFUL REQUEST.
                window.location.replace("/admin/users");
            }
            const errorData = JSON.parse(xhr.responseText);
            handleError(errorData);
        },
        dataType: "json",
        contentType: "application/json"
    });
};

document.getElementById('deleteUserBtn').addEventListener('click', function () {
    console.log('Button clicked!')
    swalWithBootstrapButtons.fire({
        icon: 'error',
        title: 'Non-Revertible Action!',
        text: 'Are you sure you want to delete this account?',
        showCancelButton: true,
        confirmButtonClass: 'btn btn-danger',
        confirmButtonText: 'Delete Account',
        cancelButtonClass: 'fw-black',
        cancelButtonText: 'Cancel',
        footer: '<a href="/policy">Make sure your action respects Alfheim\'s policies</a>'
    }).then((result) => {
        if (result.isConfirmed) {
            // Admin confirmed, perform delete action. Preparing the url
            console.log('ADMIN confirmed deletion');
            let url = "/admin/users/delete?username=" + username;

            console.log(`LINK : ${url}`);

            // Making the request
            makeDeleteUserRequest(url);

        } else if (result.dismiss === Swal.DismissReason.cancel) {
            // Admin cancelled
            console.log('ADMIN cancelled deletion');
        }
    });
});


// USER BOTTOM ACTIONS

// User Confirmed toggle keep default
const ConfirmCheckbox = document.getElementById('user-confirm-toggle-checked');
const userConfirmCheckbox = document.getElementById('user-confirm-toggle');

if (ConfirmCheckbox !== null) {
    ConfirmCheckbox.addEventListener('change', (event) => {
        console.log("CONFIRM TOGGLE CLICKED")
        if (!event.target.checked) {
            event.preventDefault();
            ConfirmCheckbox.checked = true;

            handleSuccessModal({
                title: 'Confirmed!',
                text: 'This user is already confirmed.'
            });
        }
    });
}

// Confirm user btn handle
if (userConfirmCheckbox !== null) {
    userConfirmCheckbox.addEventListener('change', (event) => {

        let url = "/admin/users/account/confirm?username=" + username;

        $.ajax({
            type: "POST",
            url: url,
            success: function(xhr) {
                console.log(`Response : ${xhr}`);
                window.location.replace("/admin/users/"+username);
                // handleSuccessModal();
            },
            error: function(xhr) {
                // Handle the error response
                if (xhr.responseText === "OK") {
                    // TODO: FIX IT SO THE SUCCESS GETS HOOKED UPON A SUCCESSFUL REQUEST.
                    userBanCheckbox.disabled = true;

                    // Throwing the success modal
                    handleSuccessModal({
                        title: "Success!",
                        text: "User account has been Confirmed successfully."
                    });

                    // Refreshing 'After a 1.5s delay'.
                    setTimeout(
                      function () {
                          window.location.replace("/admin/users/"+username);
                      }, REFRESH_DELAY
                    );

                }
                const errorData = JSON.parse(xhr.responseText);
                handleError(errorData);

                // Refreshing
                window.location.replace("/admin/users/"+username);
            },
            dataType: "json",
            contentType: "application/json"
        });
    });
};

// Un-banable admin
const banCheckbox = document.getElementById('admin-ban-toggle');
const userBanCheckbox = document.getElementById('user-ban-toggle');
if (banCheckbox !== null) {
    banCheckbox.addEventListener('change', (event) => {
        if (event.target.checked) {
            event.preventDefault();
            banCheckbox.checked = false;

            handleWarningModal({
                title: 'Prohibited Action!',
                text: 'You cannot ban an admin.'
            });
        }
    });
}

// Handle user ban toggle
if (userBanCheckbox !== null) {
    userBanCheckbox.addEventListener('change', (event) => {

        let url = "/admin/users/account/banState?username=" + username;

        $.ajax({
            type: "POST",
            url: url,
            success: function(xhr) {
                console.log(`Response : ${xhr}`);
                window.location.replace("/admin/users/"+username);
                // handleSuccessModal();
            },
            error: function(xhr) {
                // Handle the error response
                if (xhr.responseText === "OK") {
                    // TODO: FIX IT SO THE SUCCESS GETS HOOKED UPON A SUCCESSFUL REQUEST.
                    userBanCheckbox.disabled = true;

                    // Throwing the success modal
                    handleSuccessModal({
                        title: "Success!",
                        text: "Account Ban state has been changed successfully."
                    });

                    // Refreshing 'After a 1.5s delay'.
                    setTimeout(
                        function () {
                            window.location.replace("/admin/users/"+username);
                        }, REFRESH_DELAY
                    );

                }
                const errorData = JSON.parse(xhr.responseText);
                handleError(errorData);

                // Refreshing
                window.location.replace("/admin/users/"+username);
            },
            dataType: "json",
            contentType: "application/json"
        });

    });
}

// Un-suspendable admin
const suspendCheckbox = document.getElementById('admin-suspend-toggle');
const userSuspendCheckbox = document.getElementById('user-suspend-toggle');
if (suspendCheckbox !== null) {
    suspendCheckbox.addEventListener('change', (event) => {
        if (event.target.checked) {
            event.preventDefault();
            suspendCheckbox.checked = false;

            handleWarningModal({
                title: 'Prohibited Action!',
                text: 'You cannot suspend admins\' accounts.'
            });
        }
    });
}

if (userSuspendCheckbox !== null) {
    userSuspendCheckbox.addEventListener('change', (event) => {

        let url = "/admin/users/account/suspensionState?username=" + username;

        $.ajax({
            type: "POST",
            url: url,
            success: function(xhr) {
                console.log(`Response : ${xhr}`);
                window.location.replace("/admin/users/"+username);
                // handleSuccessModal();
            },
            error: function(xhr) {
                // Handle the error response
                if (xhr.responseText === "OK") {
                    // TODO: FIX IT SO THE SUCCESS GETS HOOKED UPON A SUCCESSFUL REQUEST.
                    userSuspendCheckbox.disabled = true;

                    // Throwing the success modal
                    handleSuccessModal({
                        title: "Success!",
                        text: "Account Suspension state has been changed successfully."
                    });

                    // Refreshing 'After a 1.5s delay'.
                    setTimeout(
                        function () {
                            window.location.replace("/admin/users/"+username);
                        }, REFRESH_DELAY
                    );

                }
                const errorData = JSON.parse(xhr.responseText);
                handleError(errorData);

                // Refreshing
                window.location.replace("/admin/users/"+username);
            },
            dataType: "json",
            contentType: "application/json"
        });

    });
}

// ### Password reset part ###

let passwordInput = document.getElementById("password");
const passwordResetBtn = document.getElementById("passwordResetBtn");

// Checking the password
passwordInput.addEventListener('input', function() {
    // Get the entered password
    const password = this.value;

    // Password check
    const isValidLength = password.length >= 8;
    const containMaliciousChars = /['`"]/.test(password);

    // Test the password against the pattern
    if (isValidLength && !containMaliciousChars) {
        // Valid
        this.classList.add('is-valid');
        this.classList.remove('is-invalid');
    } else if (isValidLength && containMaliciousChars) {
        // Contains invalid chars
        this.classList.remove('is-valid');
        this.classList.add('is-invalid');
    } else if (!isValidLength && !containMaliciousChars) {
        // Too short
        this.classList.remove('is-valid');
        this.classList.add('is-invalid');
    }
});

// Handle user clicking out of the input box
passwordInput.addEventListener('blur', function() {
    // Removing the validation note for empty input
    if (this.value.trim() === '') {
        this.classList.remove('is-invalid');
        this.classList.remove('is-valid');
    }
});

// handle view/hide password
const passwordVisibilityToggle = document.getElementById('hideShowPasswordToggle');

passwordVisibilityToggle.addEventListener('click', function() {
    // Toggle the password input type between 'password' and 'text'
    if (passwordInput.type === 'password') {
        passwordInput.type = 'text';
        this.innerHTML = `
      <svg class="icon icon-xs text-gray-600" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
        <path d="M10 12a2 2 0 100-4 2 2 0 000 4z" />
        <path fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd" />
      </svg>
    `;
    } else {
        passwordInput.type = 'password';
        this.innerHTML = `
      <svg class="icon icon-xs text-gray-600" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
        <path fill-rule="evenodd" d="M5 9V7a5 5 0 0110 0v2a2 2 0 012 2v5a2 2 0 01-2 2H5a2 2 0 01-2-2v-5a2 2 0 012-2zm8-2v2H7V7a3 3 0 016 0z" clip-rule="evenodd" />
      </svg>
    `;
    }
});

// Submitting password reset request
passwordResetBtn.addEventListener('click', function(event) {
    if (passwordInput.classList.contains('is-valid')) {
        console.log('Password is valid. Submitting request...');
        if (userRole === "ADMIN") {
            // Refusing request. Can't edit admins' details
            handleError({
                errorMessage: "Cannot change admins password"
            });

            // Clearing input value
            passwordInput.value = '';
            passwordInput.classList.remove('is-valid');

        } else {

            // Submitting request.
            let newPassword = passwordInput.value;
            let url = "/admin/user/password/reset?username=" + username + "&newPassword=" + newPassword;

            $.ajax({
                type: "POST",
                url: url,
                success: function(xhr) {
                    console.log(`Response : ${xhr}`);
                    // handleSuccessModal();
                },
                error: function(xhr) {
                    // Handle the error response
                    if (xhr.responseText === "OK") {
                        // TODO: FIX IT SO THE SUCCESS GETS HOOKED UPON A SUCCESSFUL REQUEST.
                        // Clearing input value
                        passwordInput.value = '';
                        passwordInput.classList.remove('is-valid');

                        // Throwing the success modal
                        handleSuccessModal({
                            title: "Success!",
                            text: "Password has been changed successfully"
                        });
                    }
                    const errorData = JSON.parse(xhr.responseText);
                    handleError(errorData);

                    // Clearing input value
                    passwordInput.value = '';
                    passwordInput.classList.remove('is-valid');
                },
                dataType: "json",
                contentType: "application/json"
            });
        }

    } else {
        console.log('Password is invalid. Request not submitted.');
        event.preventDefault();
        handleError({ errorMessage: "Submission refused. Invalid password!" });
    }
});

// ### Handle blacklisting user ###

// URL -> /admin/user/blacklist/add with a request body data = {json here}

const otherReasonInput = document.getElementById('otherReason');
const reasonSelect = document.getElementById('reportReason');
const reputationPointsInput = document.getElementById('reputationPtsInput');
const blacklistSubmitButton = document.getElementById('blacklistReportSubmitBtn');

// Submition handling
blacklistSubmitButton.addEventListener('click', function() {
    // Get the selected reason value
    let selectedReason = reasonSelect.options[reasonSelect.selectedIndex].value;
    const reputationStrikeValue = parseInt(reputationPointsInput.value);

    if (userRole !== "ADMIN") {
        if (isCorrectReason(selectedReason)) {
            if (isInRangeStrike(reputationStrikeValue)){

                // Setting up the reason
                if (selectedReason === '6') {
                    console.log("Setting up selected reason as other");
                    selectedReason = otherReasonInput.value;
                } else {
                    selectedReason = reasonSelect.options[reasonSelect.selectedIndex].innerHTML;
                }

                console.log(`\nFINAL SELECTED REASON : ${selectedReason}`);

                submitUserBlacklistReport(username, selectedReason, reputationStrikeValue);
                console.log("FORM SUBMITTED")
            } else {

                // Displaying error
                reputationPointsInput.classList.add('is-invalid');
            }
        }
    } else {
        handleError({ errorMessage: "You can't blacklist an admin!" });
    }

});

// Check correct reason specification "If other, then specify"
function isCorrectReason(selectedReason) {
    // Check if the selected reason is "Other"
    if (selectedReason === '6') {
        // Check if the otherReason input is empty
        if (otherReasonInput.value.trim() === '') {

            // Displaying the error
            otherReasonInput.classList.remove('is-valid');
            otherReasonInput.classList.add('is-invalid');

            // Returning false, so the form ain't get submitted
            return;
        }
        return true;
    }
    return true;
};

// Check reputation strike pts
function isInRangeStrike(strikeValue) {
    console.log(`Is in range ? ${strikeValue > 1 && strikeValue < 200}`)
    if (strikeValue > 1 && strikeValue < 200) {
        return true;
    } else {
        return false;
    }
}

// Handle event to restrict input to > 1 & < 200
reputationPointsInput.addEventListener('input', function() {
    let reputationPoints = parseInt(reputationPointsInput.value);

    // Reputation Strike Value : 1-200 pts
    if (isNaN(reputationPoints) || reputationPoints < 1 ) {
        reputationPointsInput.value = 1;

        // Displaying error
        reputationPointsInput.classList.add('is-invalid');
    } else if (reputationPoints > 200) {
        reputationPointsInput.value = 200;

        // Displaying error
        reputationPointsInput.classList.add('is-invalid');
    } else {
        reputationPointsInput.classList.remove('is-invalid');
    }
});

// Submit user blacklist request function
function submitUserBlacklistReport(username, reason, reputationPtsDeducted) {

    let data = {
        "username": username,
        "reason": reason,
        "reputationPtsDeducted": reputationPtsDeducted
    };
    
    $.ajax({
        type: "POST", // метод запроса
        url: "/admin/user/blacklist/add",
        data: JSON.stringify(data),
        success: function(xhr) {
            console.log(`Response : ${xhr}`);
            // handleSuccessModal();
        },
        error: function(xhr) {
            // Handle the error response
            if (xhr.responseText === "OK") {
                // TODO: FIX IT SO THE SUCCESS GETS HOOKED UPON A SUCCESSFUL REQUEST.
                // Clearing inputs
                otherReasonInput.value = ''
                otherReasonInput.classList.remove('is-invalid')

                reputationPointsInput.value = ''
                reputationPointsInput.classList.remove('is-invalid')


                // Throwing the success modal
                handleSuccessModal({
                    title: "Success!",
                    text: "Report has been made and user blacklisted"
                });
            }
            const errorData = JSON.parse(xhr.responseText);
            handleError(errorData);

            // Clearing input value
            passwordInput.value = '';
            passwordInput.classList.remove('is-valid');
        },
        dataType: "json",
        contentType: "application/json"
    });
}