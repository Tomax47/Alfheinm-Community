console.log("CUSTOM ALERT ADMIN PROFILE PAGE SCRIPT HOOKED.")
const swalWithBootstrapButtons = Swal.mixin({
    customClass: {
        confirmButton: 'btn btn-primary',
        cancelButton: 'btn btn-gray'
    },
    buttonsStyling: false
});

let username = document.getElementById('username').textContent.trim().slice(1);

console.log(`USERNAME : ${username}`)

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
const checkbox = document.getElementById('user-confirm-toggle-checked');
checkbox.addEventListener('change', (event) => {
    if (!event.target.checked) {
        event.preventDefault();
        checkbox.checked = true;

        handleSuccessModal({
            title: 'Confirmed!',
            text: 'This user is already confirmed.'
        });
    }
});

// Un-banable admin
const banCheckbox = document.getElementById('admin-ban-toggle');
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

// Un-suspendable admin
const suspendCheckbox = document.getElementById('admin-suspend-toggle');
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
