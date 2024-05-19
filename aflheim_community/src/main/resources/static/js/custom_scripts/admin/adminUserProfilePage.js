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
        message: errorData
    });
};

// Handle information notification modal
function handleInfoNotificationModal(infoMessage) {
    const notyf = new Notyf({
        position: {
            x: 'center',
            y: 'top',
        },
        types: [
            {
                type: 'info',
                background: '#0494d2',
                icon: {
                    className: 'fas fa-info-circle',
                    tagName: 'span',
                    color: '#fff'
                },
                dismissible: false
            }
        ]
    });
    notyf.open({
        type: 'info',
        message: infoMessage
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

// Handle info modal
function handleInfoModal(messageData) {
    swalWithBootstrapButtons.fire(
        messageData.title,
        messageData.text,
        'info'
    )
};

// handle blacklisted info modal
function handleBlacklistedInfoModal(blacklistReport) {

    let footer = '<div class="form-check">' +
        '                   <input class="form-check-input" type="checkbox" value="" id="errorReportCheckbox"> ' +
        '                   <label class="form-check-label" for="errorReportCheckbox">Error Report</label>' +
        '               </div>'

    swalWithBootstrapButtons.fire({
        icon: 'info',
        title: 'Blacklist Report',
        html: '<p class="mt-3"><span class="fw-black">Created on : </span>' + blacklistReport.createdAt + '</p>' +
            '  <p><span class="fw-black">Reason : </span>' + blacklistReport.reason + '</p>' +
            '  <p><span class="fw-black">Reputation Strike :</span> -' + blacklistReport.reputationPtsDeducted + '</p>' +
            '  <p><span class="fw-black">State : </span>' + blacklistReport.state + '</p>',
        showCancelButton: true,
        confirmButtonClass: 'text-white btn btn-outline-info',
        confirmButtonText: 'Remove From Blacklist',
        cancelButtonClass: 'bt-outline-info fw-black',
        cancelButtonText: 'Cancel',
        footer: footer
    }).then((result) => {
        if (result.isConfirmed) {
            // Admin confirmed, perform delete action. Preparing the url
            console.log('ADMIN confirmed blacklist removal');

            let isErrorReport = document.getElementById('errorReportCheckbox').checked;
            // Making the request
            makeBlacklistUserRemovalRequest(isErrorReport);

        } else if (result.dismiss === Swal.DismissReason.cancel) {
            // Admin cancelled
            console.log('ADMIN CANCELLED REMOVING FROM BLACKLIST ACTION');
        }
    });
};

// Send Delete request
function makeDeleteUserRequest(url) {
    console.log("SENDING A DELETE REQUEST...");

    $.ajax({
        type: "POST",
        url: url,
        success: function(response) {
            console.log(`Response : ${response}`);

            // Throwing the success modal
            handleSuccessModal({
                title: "Success!",
                text: "User account has been deleted successfully."
            });

            // Refreshing 'After a 1.5s delay'.
            setTimeout(
                function () {
                    window.location.replace("/admin/users");
                }, REFRESH_DELAY
            );
        },
        error: function(response) {
            // Handle the error response
            if (response.status === 200) {
                // TODO: FIX IT SO THE SUCCESS GETS HOOKED UPON A SUCCESSFUL REQUEST.
                window.location.replace("/admin/users");
            }
            const errorData = response.responseJSON.message;
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

            if (userRole === "ADMIN") {
                handleError("Can't delete an admin!");
                return;
            }

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
            success: function(response) {
                console.log(`Response : ${response}`);
                window.location.replace("/admin/users/"+username);
                // handleSuccessModal();
            },
            error: function(response) {
                // Handle the error response
                if (response.status === 200) {
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
                const errorData = response.responseJSON.message;
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
            error: function(response) {
                // Handle the error response
                if (response.status === 200) {
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
                const errorData = response.responseJSON.message;
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
            error: function(response) {
                // Handle the error response
                if (response.status === 200) {
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
            handleError("Cannot change admins password");

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
                success: function(response) {
                    console.log(`Response : ${response}`);
                    // handleSuccessModal();
                },
                error: function(response) {
                    // Handle the error response
                    if (response.status === 200) {
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
                    const errorData = response.responseJSON.message;
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
        handleError("Submission refused. Invalid password!");
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
        handleError("You can't blacklist an admin!");
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

    if (strikeValue >= 1 && strikeValue <= 200) {
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

// ## USER BLACKLIST PART ##
const openReportModalBtn = document.getElementById('openReportBtn');

if (openReportModalBtn !== null) {
    openReportModalBtn.addEventListener('click', function () {
        console.log('button clicked. displaying modal report')
        displayReportDetailsModal();
    });
}

// Submit user blacklist request function
function submitUserBlacklistReport(username, reason, reputationPtsDeducted) {
    let isBlacklistedDiv = document.getElementById('blacklistedDiv');

    console.log(`IS BLACK LISTED GIV NULL? ${isBlacklistedDiv === null}`);
    if (isBlacklistedDiv === null) {
        let data = {
            "username": username,
            "reason": reason,
            "reputationPtsDeducted": reputationPtsDeducted
        };

        $.ajax({
            type: "POST",
            url: "/admin/user/blacklist/add",
            data: JSON.stringify(data),
            success: function(xhr) {
                console.log(`Response : ${xhr}`);
                // handleSuccessModal();
            },
            error: function(response) {
                // Handle the error response
                if (response.responseText === "OK") {
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

                    // Refreshing 'After a 1.5s delay'.
                    setTimeout(
                        function () {
                            window.location.replace("/admin/users/"+username);
                        }, REFRESH_DELAY
                    );
                }
                console.log(`Error Message : ${response.responseJSON.message}`)
                const errorData = response.responseJSON.message;
                handleError(errorData);

                // Clearing input value
                passwordInput.value = '';
                passwordInput.classList.remove('is-valid');
            },
            dataType: "json",
            contentType: "application/json"
        });
    } else if (userRole === "ADMIN") {
        handleError("Can't blacklist an admin!");
    } else {
        handleError("User is already blacklisted!");
    }

};

// Handle showing report's details
function displayReportDetailsModal() {

    let url = "/admin/user/blacklist/report?username=" + username;

    $.ajax({
        type: "GET",
        url: url,
        success: function(data) {
            // Throwing the blacklist info modal
            handleBlacklistedInfoModal(data);
        },
        error: function(data) {
            // Handle the error response
            const errorData = JSON.parse(data.responseText);
            handleError(errorData);
        },
        dataType: "json",
        contentType: "application/json"
    });
};

// Handle remove user from blacklist
function makeBlacklistUserRemovalRequest(isErrorReport) {
    console.log(`\n\nREMOVING USER FROM BLACKLIST. ERROR REPORT? ${isErrorReport}`)

    let url = "/admin/user/blacklist/remove?username=" + username +"&isErrorReport=" + isErrorReport;

    $.ajax({
        type: "POST",
        url: url,
        success: function(data) {
            // Throwing the blacklist info modal
            console.log('Success hooken')
            handleBlacklistedInfoModal(data);
        },
        error: function(data) {
            console.log(`Message : ${data.status}`)
            if (data.status === 200) {
                handleSuccessModal({
                    title: "Success!",
                    text: "User has been successfully removed from the blacklist."
                });

                // Refreshing 'After a 1.5s delay'.
                setTimeout(
                    function () {
                        window.location.replace("/admin/users/"+username);
                    }, REFRESH_DELAY
                );
                return;
            }

            // Handle the error response
            const errorData = JSON.parse(data.responseJSON.message);
            handleError(errorData);
        },
        dataType: "json",
        contentType: "application/json"
    });

};

// ## USER PROFILE UPDATE FORM ##

let nameInput = document.getElementById('first_name');
let surnameInput = document.getElementById('last_name');
let birthdayInput = document.getElementById('birthday');
let genderInput = document.getElementById('gender');
let phoneInput = document.getElementById('phone');
let addressInput = document.getElementById('address');
let countryInput = document.getElementById('country');
let cityInput = document.getElementById('city');
let regionInput = document.getElementById('region');
let zipInput = document.getElementById('zip');
let fileInput = document.getElementById('profilePicture');
let updateFormSubmitBtn = document.getElementById('updateFormSubmitBtn');

// Only alphabetic input check
var lettersRegex = /^[A-Za-z ]+$/;
var numbersRegex = /^[0-9]+$/;
var addressRegex = /^[a-zA-Z0-9., ]+$/;
var dateRegex = /^\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$/;
function checkInput(inputElement) {
    if (!inputElement.value.match(lettersRegex)) {
        return false;
    } else {
        return true;
    }
};

// Event listeners to check inputs on keyup
nameInput.addEventListener('keyup', function() {
    if (!checkInput(nameInput)) {
        this.classList.remove('is-valid');
        this.classList.add('is-invalid');
    } else {
        this.classList.add('is-valid');
        this.classList.remove('is-invalid');
    }
});

nameInput.addEventListener('blur', function() {
    if (this.value.trim() === '') {
        this.classList.remove('is-valid');
        this.classList.remove('is-invalid');
    }
});

// Surname
surnameInput.addEventListener('keyup', function() {
    if (!checkInput(surnameInput)) {
        this.classList.remove('is-valid');
        this.classList.add('is-invalid');
    } else {
        this.classList.add('is-valid');
        this.classList.remove('is-invalid');
    }
});

surnameInput.addEventListener('blur', function() {
    if (this.value.trim() === '') {
        this.classList.remove('is-valid');
        this.classList.remove('is-invalid');
    }
});

// City
cityInput.addEventListener('keyup', function() {
    if (!checkInput(cityInput)) {
        this.classList.remove('is-valid');
        this.classList.add('is-invalid');
    } else {
        this.classList.add('is-valid');
        this.classList.remove('is-invalid');
    }
});

cityInput.addEventListener('blur', function() {
    if (this.value.trim() === '') {
        this.classList.remove('is-valid');
        this.classList.remove('is-invalid');
    }
});

// Region
regionInput.addEventListener('keyup', function() {
    if (!checkInput(regionInput)) {
        this.classList.remove('is-valid');
        this.classList.add('is-invalid');
    } else {
        this.classList.add('is-valid');
        this.classList.remove('is-invalid');
    }
});

regionInput.addEventListener('blur', function() {
    if (this.value.trim() === '') {
        this.classList.remove('is-valid');
        this.classList.remove('is-invalid');
    }
});

// Number and ZIP input check
function checkNumericalInput(inputElement) {
    if (!inputElement.value.match(numbersRegex)) {
        return false;
    } else {
        return true;
    }
};
phoneInput.addEventListener('keyup', function() {
    if (!checkNumericalInput(phoneInput) || this.value.length !== 10) {
        this.classList.remove('is-valid');
        this.classList.add('is-invalid');
    } else {
        this.classList.add('is-valid');
        this.classList.remove('is-invalid');
    }
});

phoneInput.addEventListener('blur', function() {
    if (this.value.trim() === '') {
        this.classList.remove('is-valid');
        this.classList.remove('is-invalid');
    }
});

zipInput.addEventListener('keyup', function() {
    if (!checkNumericalInput(zipInput) || this.value.length > 10 || this.value.length < 5) {
        this.classList.remove('is-valid');
        this.classList.add('is-invalid');
    } else {
        this.classList.add('is-valid');
        this.classList.remove('is-invalid');
    }
});

zipInput.addEventListener('blur', function() {
    if (this.value.trim() === '') {
        this.classList.remove('is-valid');
        this.classList.remove('is-invalid');
    }
});

// Address
function checkAddressInput(inputElement) {
    if (!inputElement.value.match(addressRegex)) {
        return false;
    } else {
        return true;
    }
};
addressInput.addEventListener('keyup', function() {
    if (!checkAddressInput(addressInput)) {
        this.classList.remove('is-valid');
        this.classList.add('is-invalid');
    } else {
        this.classList.add('is-valid');
        this.classList.remove('is-invalid');
    }
});

addressInput.addEventListener('blur', function() {
    if (this.value.trim() === '') {
        this.classList.remove('is-valid');
        this.classList.remove('is-invalid');
    }
});

// Gender input check
genderInput.addEventListener('change', function() {

    // Based on the value
    let selectedGender = genderInput.value;

    // Check if the selected gender is in the list of countries
    console.log(`SELECTED GENDER : ${selectedGender}`)
    if (selectedGender === "Male" || selectedGender === "Female" || selectedGender === "None") {
        if (this.classList.contains('is-invalid')) {
            this.classList.remove('is-invalid');
        }
    } else {
        this.classList.add('is-invalid');
    }
});

// Check file validation
function checkProfileImageFile(file) {
    if (file) {
        var allowedTypes = ['image/png', 'image/jpeg', 'image/webp'];
        var maxSize =  1024 * 1024; // 1MB

        if (allowedTypes.includes(file.type) && file.size <= maxSize) {
            console.log('File is valid');
            if (fileInput.classList.contains('is-invalid')) {
                fileInput.classList.remove('is-invalid');
            };

            return true;

        } else {
            // File is invalid
            fileInput.classList.add('is-invalid');
            fileInput.value = '';

            return false;
        }
    }
};

fileInput.addEventListener('change', function () {
    var fileInput = this;
    var file = fileInput.files[0];

    checkProfileImageFile(file);
});

// Country selection and input check
const countries = [
    { name: 'Afghanistan', code: 'AF' },
    { name: 'Åland Islands', code: 'AX' },
    { name: 'Albania', code: 'AL' },
    { name: 'Algeria', code: 'DZ' },
    { name: 'American Samoa', code: 'AS' },
    { name: 'Andorra', code: 'AD' },
    { name: 'Angola', code: 'AO' },
    { name: 'Anguilla', code: 'AI' },
    { name: 'Antarctica', code: 'AQ' },
    { name: 'Antigua and Barbuda', code: 'AG' },
    { name: 'Argentina', code: 'AR' },
    { name: 'Armenia', code: 'AM' },
    { name: 'Aruba', code: 'AW' },
    { name: 'Australia', code: 'AU' },
    { name: 'Austria', code: 'AT' },
    { name: 'Azerbaijan', code: 'AZ' },
    { name: 'Bahamas', code: 'BS' },
    { name: 'Bahrain', code: 'BH' },
    { name: 'Bangladesh', code: 'BD' },
    { name: 'Barbados', code: 'BB' },
    { name: 'Belarus', code: 'BY' },
    { name: 'Belgium', code: 'BE' },
    { name: 'Belize', code: 'BZ' },
    { name: 'Benin', code: 'BJ' },
    { name: 'Bermuda', code: 'BM' },
    { name: 'Bhutan', code: 'BT' },
    { name: 'Bolivia', code: 'BO' },
    { name: 'Bosnia and Herzegovina', code: 'BA' },
    { name: 'Botswana', code: 'BW' },
    { name: 'Bouvet Island', code: 'BV' },
    { name: 'Brazil', code: 'BR' },
    { name: 'British Indian Ocean Territory', code: 'IO' },
    { name: 'Brunei Darussalam', code: 'BN' },
    { name: 'Bulgaria', code: 'BG' },
    { name: 'Burkina Faso', code: 'BF' },
    { name: 'Burundi', code: 'BI' },
    { name: 'Cambodia', code: 'KH' },
    { name: 'Cameroon', code: 'CM' },
    { name: 'Canada', code: 'CA' },
    { name: 'Cape Verde', code: 'CV' },
    { name: 'Cayman Islands', code: 'KY' },
    { name: 'Central African Republic', code: 'CF' },
    { name: 'Chad', code: 'TD' },
    { name: 'Chile', code: 'CL' },
    { name: 'China', code: 'CN' },
    { name: 'Christmas Island', code: 'CX' },
    { name: 'Cocos (Keeling) Islands', code: 'CC' },
    { name: 'Colombia', code: 'CO' },
    { name: 'Comoros', code: 'KM' },
    { name: 'Congo', code: 'CG' },
    { name: 'Congo, The Democratic Republic of the', code: 'CD' },
    { name: 'Cook Islands', code: 'CK' },
    { name: 'Costa Rica', code: 'CR' },
    { name: "Cote D'Ivoire", code: 'CI' },
    { name: 'Croatia', code: 'HR' },
    { name: 'Cuba', code: 'CU' },
    { name: 'Cyprus', code: 'CY' },
    { name: 'Czech Republic', code: 'CZ' },
    { name: 'Denmark', code: 'DK' },
    { name: 'Djibouti', code: 'DJ' },
    { name: 'Dominica', code: 'DM' },
    { name: 'Dominican Republic', code: 'DO' },
    { name: 'Ecuador', code: 'EC' },
    { name: 'Egypt', code: 'EG' },
    { name: 'El Salvador', code: 'SV' },
    { name: 'Equatorial Guinea', code: 'GQ' },
    { name: 'Eritrea', code: 'ER' },
    { name: 'Estonia', code: 'EE' },
    { name: 'Ethiopia', code: 'ET' },
    { name: 'Falkland Islands (Malvinas)', code: 'FK' },
    { name: 'Faroe Islands', code: 'FO' },
    { name: 'Fiji', code: 'FJ' },
    { name: 'Finland', code: 'FI' },
    { name: 'France', code: 'FR' },
    { name: 'French Guiana', code: 'GF' },
    { name: 'French Polynesia', code: 'PF' },
    { name: 'French Southern Territories', code: 'TF' },
    { name: 'Gabon', code: 'GA' },
    { name: 'Gambia', code: 'GM' },
    { name: 'Georgia', code: 'GE' },
    { name: 'Germany', code: 'DE' },
    { name: 'Ghana', code: 'GH' },
    { name: 'Gibraltar', code: 'GI' },
    { name: 'Greece', code: 'GR' },
    { name: 'Greenland', code: 'GL' },
    { name: 'Grenada', code: 'GD' },
    { name: 'Guadeloupe', code: 'GP' },
    { name: 'Guam', code: 'GU' },
    { name: 'Guatemala', code: 'GT' },
    { name: 'Guernsey', code: 'GG' },
    { name: 'Guinea', code: 'GN' },
    { name: 'Guinea-Bissau', code: 'GW' },
    { name: 'Guyana', code: 'GY' },
    { name: 'Haiti', code: 'HT' },
    { name: 'Heard Island and McDonald Islands', code: 'HM' },
    { name: 'Holy See (Vatican City State)', code: 'VA' },
    { name: 'Honduras', code: 'HN' },
    { name: 'Hong Kong', code: 'HK' },
    { name: 'Hungary', code: 'HU' },
    { name: 'Iceland', code: 'IS' },
    { name: 'India', code: 'IN' },
    { name: 'Indonesia', code: 'ID' },
    { name: 'Iran, Islamic Republic of', code: 'IR' },
    { name: 'Iraq', code: 'IQ' },
    { name: 'Ireland', code: 'IE' },
    { name: 'Isle of Man', code: 'IM' },
    { name: 'Israel', code: 'IL' },
    { name: 'Italy', code: 'IT' },
    { name: 'Jamaica', code: 'JM' },
    { name: 'Japan', code: 'JP' },
    { name: 'Jersey', code: 'JE' },
    { name: 'Jordan', code: 'JO' },
    { name: 'Kazakhstan', code: 'KZ' },
    { name: 'Kenya', code: 'KE' },
    { name: 'Kiribati', code: 'KI' },
    { name: "Korea, Democratic People's Republic of", code: 'KP' },
    { name: 'Korea, Republic of', code: 'KR' },
    { name: 'Kuwait', code: 'KW' },
    { name: 'Kyrgyzstan', code: 'KG' },
    { name: "Lao People's Democratic Republic", code: 'LA' },
    { name: 'Latvia', code: 'LV' },
    { name: 'Lebanon', code: 'LB' },
    { name: 'Lesotho', code: 'LS' },
    { name: 'Liberia', code: 'LR' },
    { name: 'Libyan Arab Jamahiriya', code: 'LY' },
    { name: 'Liechtenstein', code: 'LI' },
    { name: 'Lithuania', code: 'LT' },
    { name: 'Luxembourg', code: 'LU' },
    { name: 'Macao', code: 'MO' },
    { name: 'Macedonia, The Former Yugoslav Republic of', code: 'MK' },
    { name: 'Madagascar', code: 'MG' },
    { name: 'Malawi', code: 'MW' },
    { name: 'Malaysia', code: 'MY' },
    { name: 'Maldives', code: 'MV' },
    { name: 'Mali', code: 'ML' },
    { name: 'Malta', code: 'MT' },
    { name: 'Marshall Islands', code: 'MH' },
    { name: 'Martinique', code: 'MQ' },
    { name: 'Mauritania', code: 'MR' },
    { name: 'Mauritius', code: 'MU' },
    { name: 'Mayotte', code: 'YT' },
    { name: 'Mexico', code: 'MX' },
    { name: 'Micronesia, Federated States of', code: 'FM' },
    { name: 'Moldova, Republic of', code: 'MD' },
    { name: 'Monaco', code: 'MC' },
    { name: 'Mongolia', code: 'MN' },
    { name: 'Montenegro', code: 'ME' },
    { name: 'Montserrat', code: 'MS' },
    { name: 'Morocco', code: 'MA' },
    { name: 'Mozambique', code: 'MZ' },
    { name: 'Myanmar', code: 'MM' },
    { name: 'Namibia', code: 'NA' },
    { name: 'Nauru', code: 'NR' },
    { name: 'Nepal', code: 'NP' },
    { name: 'Netherlands', code: 'NL' },
    { name: 'Netherlands Antilles', code: 'AN' },
    { name: 'New Caledonia', code: 'NC' },
    { name: 'New Zealand', code: 'NZ' },
    { name: 'Nicaragua', code: 'NI' },
    { name: 'Niger', code: 'NE' },
    { name: 'Nigeria', code: 'NG' },
    { name: 'Niue', code: 'NU' },
    { name: 'Norfolk Island', code: 'NF' },
    { name: 'Northern Mariana Islands', code: 'MP' },
    { name: 'Norway', code: 'NO' },
    { name: 'Oman', code: 'OM' },
    { name: 'Pakistan', code: 'PK' },
    { name: 'Palau', code: 'PW' },
    { name: 'Palestinian Territory, Occupied', code: 'PS' },
    { name: 'Panama', code: 'PA' },
    { name: 'Papua New Guinea', code: 'PG' },
    { name: 'Paraguay', code: 'PY' },
    { name: 'Peru', code: 'PE' },
    { name: 'Philippines', code: 'PH' },
    { name: 'Pitcairn', code: 'PN' },
    { name: 'Poland', code: 'PL' },
    { name: 'Portugal', code: 'PT' },
    { name: 'Puerto Rico', code: 'PR' },
    { name: 'Qatar', code: 'QA' },
    { name: 'Reunion', code: 'RE' },
    { name: 'Romania', code: 'RO' },
    { name: 'Russian Federation', code: 'RU' },
    { name: 'RWANDA', code: 'RW' },
    { name: 'Saint Helena', code: 'SH' },
    { name: 'Saint Kitts and Nevis', code: 'KN' },
    { name: 'Saint Lucia', code: 'LC' },
    { name: 'Saint Pierre and Miquelon', code: 'PM' },
    { name: 'Saint Vincent and the Grenadines', code: 'VC' },
    { name: 'Samoa', code: 'WS' },
    { name: 'San Marino', code: 'SM' },
    { name: 'Sao Tome and Principe', code: 'ST' },
    { name: 'Saudi Arabia', code: 'SA' },
    { name: 'Senegal', code: 'SN' },
    { name: 'Serbia', code: 'RS' },
    { name: 'Seychelles', code: 'SC' },
    { name: 'Sierra Leone', code: 'SL' },
    { name: 'Singapore', code: 'SG' },
    { name: 'Slovakia', code: 'SK' },
    { name: 'Slovenia', code: 'SI' },
    { name: 'Solomon Islands', code: 'SB' },
    { name: 'Somalia', code: 'SO' },
    { name: 'South Africa', code: 'ZA' },
    { name: 'South Georgia and the South Sandwich Islands', code: 'GS' },
    { name: 'Spain', code: 'ES' },
    { name: 'Sri Lanka', code: 'LK' },
    { name: 'Sudan', code: 'SD' },
    { name: 'Suriname', code: 'SR' },
    { name: 'Svalbard and Jan Mayen', code: 'SJ' },
    { name: 'Swaziland', code: 'SZ' },
    { name: 'Sweden', code: 'SE' },
    { name: 'Switzerland', code: 'CH' },
    { name: 'Syrian Arab Republic', code: 'SY' },
    { name: 'Taiwan, Province of China', code: 'TW' },
    { name: 'Tajikistan', code: 'TJ' },
    { name: 'Tanzania, United Republic of', code: 'TZ' },
    { name: 'Thailand', code: 'TH' },
    { name: 'Timor-Leste', code: 'TL' },
    { name: 'Togo', code: 'TG' },
    { name: 'Tokelau', code: 'TK' },
    { name: 'Tonga', code: 'TO' },
    { name: 'Trinidad and Tobago', code: 'TT' },
    { name: 'Tunisia', code: 'TN' },
    { name: 'Turkey', code: 'TR' },
    { name: 'Turkmenistan', code: 'TM' },
    { name: 'Turks and Caicos Islands', code: 'TC' },
    { name: 'Tuvalu', code: 'TV' },
    { name: 'Uganda', code: 'UG' },
    { name: 'Ukraine', code: 'UA' },
    { name: 'United Arab Emirates', code: 'AE' },
    { name: 'United Kingdom', code: 'GB' },
    { name: 'United States', code: 'US' },
    { name: 'United States Minor Outlying Islands', code: 'UM' },
    { name: 'Uruguay', code: 'UY' },
    { name: 'Uzbekistan', code: 'UZ' },
    { name: 'Vanuatu', code: 'VU' },
    { name: 'Venezuela', code: 'VE' },
    { name: 'Viet Nam', code: 'VN' },
    { name: 'Virgin Islands, British', code: 'VG' },
    { name: 'Virgin Islands, U.S.', code: 'VI' },
    { name: 'Wallis and Futuna', code: 'WF' },
    { name: 'Western Sahara', code: 'EH' },
    { name: 'Yemen', code: 'YE' },
    { name: 'Zambia', code: 'ZM' },
    { name: 'Zimbabwe', code: 'ZW' }
];
countryInput.addEventListener('click', () => {
    if (countryInput.options.length === 1) {
        const fragment = document.createDocumentFragment();
        countries.forEach(country => {
            const option = document.createElement('option');
            option.value = country.code;
            option.text = country.name;
            fragment.appendChild(option);
        });
        countryInput.appendChild(fragment);
    }
});

countryInput.addEventListener('change', function() {

    // Check if the selected country is in the list of countries
    if (!checkCountrySelectionInput()) {
        this.classList.add('is-invalid');
    } else {
        if (this.classList.contains('is-invalid')) {
            this.classList.remove('is-invalid');
        }
    }
});

function checkCountrySelectionInput() {
    // Based on the value "country code" & the text "country name"
    let selectedCountry = countryInput.options[countryInput.selectedIndex];

    let isCountryIncluded = countries.some(country => {
        return country.name === selectedCountry.text.trim() && country.code === selectedCountry.value.trim();
    });

    return isCountryIncluded;
};

function checkInputReadiness() {

    console.log('check data readiness.')

    if (nameInput.value !== '') {
        if (!checkInput(nameInput)) {
            console.log('1')
            return false;
        }
    }

    if (surnameInput.value !== '') {
        if (!checkInput(surnameInput)) {
            console.log('1')
            return false;
        }
    }

    if (regionInput.value !== '') {
        if (!checkInput(regionInput)) {
            console.log('1')
            return false;
        }
    }

    if (cityInput.value !== '') {
        if (!checkInput(cityInput)) {
            console.log('1')
            return false;
        }
    }

    if (phoneInput.value !== '') {
        if (!checkNumericalInput(phoneInput) || phoneInput.value.length !== 10) {
            console.log('2')
            return false;
        }
    }

    if (zipInput.value !== '') {
        if (!checkNumericalInput(zipInput) || zipInput.value.length < 5 || zipInput.value.length > 10) {
            console.log('2')
            return false;
        }
    }

    if (addressInput.value !== '') {
        if (!checkAddressInput(addressInput)) {
            console.log('3')
            return false;
        }
    }

    if (genderInput.value !== "Male" && genderInput.value !== "Female" && genderInput.value !== "None") {
        console.log('4')
        return false;
    }

    if (countryInput.options[countryInput.selectedIndex].value !== '') {
        if (!checkCountrySelectionInput()) {
            console.log('5')
            return false;
        }
    }

    if (birthdayInput.value !== '') {
        if (!dateRegex.test(birthdayInput.value)) {
            console.log('6')
            return false;
        }
    }

    console.log(`FILE : ${fileInput.files[0]}, IS UNDEFINED? ${fileInput.files[0] === undefined}`)
    if (fileInput.files[0] !== undefined) {
        console.log(`7777777777 ${fileInput.files[0].size} | ${fileInput.files[0].name} | ${fileInput.files[0].type}`);
        if (!checkProfileImageFile(fileInput.files[0])) {
            console.log('8');
            return false;
        }
    }

    // All passed
    console.log('check data readiness done.')
    return true;
};

// Checking if there is any new data in the form to update
function anyNewData(data, rawJsonData) {

    let hasOneValue = false;

    // TODO CHECK THE JSON OBJECT
    if (rawJsonData.name !== '') {
        hasOneValue = true;
    }
    if (rawJsonData.surname !== '') {
        hasOneValue = true;
    }
    if (rawJsonData.address !== '') {
        hasOneValue = true;
    }
    if (rawJsonData.city !== '') {
        hasOneValue = true;
    }
    if (rawJsonData.region !== '') {
        hasOneValue = true;
    }
    if (rawJsonData.zip !== '') {
        hasOneValue = true;
    }
    if (rawJsonData.birthdate !== '') {
        hasOneValue = true;
    }
    if (rawJsonData.number !== '') {
        hasOneValue = true;
    }
    if (rawJsonData.country !== 'None') {
        hasOneValue = true;
    }
    if (rawJsonData.gender !== 'None') {
        hasOneValue = true;
    }


    console.log(data.get('profilePictureFile'));
    let hasProfilePicture = data.get('profilePictureFile') !== 'undefined';

    console.log(`Empty : ${hasOneValue}. Profile file : ${hasProfilePicture}`);
    return hasOneValue || hasProfilePicture;
};

// Getting the input
function getInputsValues() {

    console.log('Get input values.')
    // Preparing image file
    let formData = new FormData();
    formData.append('profilePictureFile', fileInput.files[0]);

    // Preparing country part
    let countrySelected;
    if (countryInput.options[countryInput.selectedIndex].value === null) {
        countrySelected = 'None';
    } else {
        countrySelected = countryInput.options[countryInput.selectedIndex].text.trim();
    }

    let data = {
        name: nameInput.value,
        surname: surnameInput.value,
        address: addressInput.value,
        city: cityInput.value,
        region: regionInput.value,
        zip: zipInput.value,
        country: countrySelected,
        gender: genderInput.value,
        birthdate: birthdayInput.value,
        number: phoneInput.value,
    };

    // Checking for new data
    console.log('INPUTS SURNAAAAAAAAAAAAAAAAAAAAAAAAAAAAAME : ' + (data.surname === ''))
    if (anyNewData(formData, data)) {

        formData.append("data", JSON.stringify(data));

        console.log(`Get input values done. FILE -> ${formData.get('profilePictureFile')}, DATA -> ${formData.get('data')}`);
        return formData;
    } else {
        // No new data
        console.log('ERRRRRRRRRRRRRRRRRROOOOOOOOOOOOOOORRRRRRRRRRR')
        return false;
    }
};

// Clearing inputs
function clearInputs() {
    nameInput.value = '';
    nameInput.classList.remove('is-valid');
    nameInput.classList.remove('is-invalid');
    surnameInput.value = '';
    surnameInput.classList.remove('is-valid');
    surnameInput.classList.remove('is-invalid');
    cityInput.value = '';
    cityInput.classList.remove('is-valid');
    cityInput.classList.remove('is-invalid');
    zipInput.value = '';
    zipInput.classList.remove('is-valid');
    zipInput.classList.remove('is-invalid');
    countryInput.value = '';
    countryInput.classList.remove('is-valid');
    countryInput.classList.remove('is-invalid');
    regionInput.value = '';
    regionInput.classList.remove('is-valid');
    regionInput.classList.remove('is-invalid');
    addressInput.value = '';
    addressInput.classList.remove('is-valid');
    addressInput.classList.remove('is-invalid');
    genderInput.value = 'None';
    genderInput.classList.remove('is-valid');
    genderInput.classList.remove('is-invalid');
    fileInput.value = '';
    fileInput.classList.remove('is-valid');
    fileInput.classList.remove('is-invalid');
    phoneInput.value = '';
    phoneInput.classList.remove('is-valid');
    phoneInput.classList.remove('is-invalid');
    birthdayInput.value = '';
    birthdayInput.classList.remove('is-valid');
    birthdayInput.classList.remove('is-invalid');
};
function updateUsersData(response) {

    nameInput.placeholder = response.name;
    surnameInput.placeholder = response.surname;
    cityInput.placeholder = response.city;
    zipInput.placeholder = response.zip;
    regionInput.placeholder = response.region;
    addressInput.placeholder = response.address;
    fileInput.placeholder = '';
    countryInput.options[0].innerText = response.country;
    genderInput.options[0].innerText = 'Current : ' + response.gender;
    phoneInput.placeholder = response.number;
    birthdayInput.placeholder = response.birthdate;
}
function submitUserDataUpdateRequest() {

    if (checkInputReadiness()) {

        let data = getInputsValues();

        // Sending ajax request
        console.log(`Data is not false? ${(data !== false)}`);
       if (data !== false) {
           $.ajax({
               type: "POST",
               enctype: 'multipart/form-data',
               url: "/admin/user/profile/update?username=" + username,
               data: data,
               contentType: false,
               processData: false,
               cache: false,
               success: function(response) {

                   handleSuccessModal({
                       title: "Success!",
                       text: "User's data has been successfully updated"
                   });

                   // Updating users info on the front
                   updateUsersData(response);

                   // Clearing inputs
                   clearInputs();
               },
               error: function(response) {
                   // Handle the error response
                   const errorData = JSON.parse(response.responseText);
                   handleError(errorData);

                   // Clearing inputs
                   clearInputs();
               }
           });
       } else {
           handleInfoNotificationModal("Nothing New To Update!")
       }
    } else {
        // Error. Invalid input value/s
        handleError("The form has invalid data");
    }
};

if (updateFormSubmitBtn !== null) {
    updateFormSubmitBtn.addEventListener('click', function () {
        console.log('Btn submit form clicked')

        console.log(`Submitting user request. IS ADMIN? ${userRole === "ADMIN"}. USER ROLE : ${userRole}`)
        if (userRole !== "ADMIN") {

            submitUserDataUpdateRequest();
        } else {
            // Refusing request. Can't edit admins' details
            handleError("Cannot change admins details");
        }
    });
};

let suggestUpdateBtn = document.getElementById('SuggestImprovement');
if (suggestUpdateBtn !== null) {
    suggestUpdateBtn.addEventListener('click', function () {

        handleInfoNotificationModal("This is a pending feature. We are working on adding it ASAP!")
    });
};