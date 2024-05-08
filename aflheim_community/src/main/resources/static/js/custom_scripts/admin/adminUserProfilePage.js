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

