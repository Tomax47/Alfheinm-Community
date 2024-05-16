
// Modals & Notifications
const swalWithBootstrapButtons = Swal.mixin({
    customClass: {
        confirmButton: 'btn btn-primary',
        cancelButton: 'btn btn-gray'
    },
    buttonsStyling: false
});

function handleSuccessModal(messageData) {
    swalWithBootstrapButtons.fire({
        icon: 'success',
        title: messageData.title,
        text: messageData.text,
        showConfirmButton: true,
        timer: 1500
    });
};

// Error handling
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
const deleteProfileBtn = document.getElementById('deleteProfileBtn');
const username = document.getElementById('username').innerHTML;
const REFRESH_DELAY = 1500;

deleteProfileBtn.addEventListener('click', function () {

    let url = "/profile/delete?username=" + username;

    // Sending the requests.
    $.ajax({
        type: "POST",
        url: url,
        success: function(response) {
            console.log(`${response}`);
        },
        error: function (response) {

            if (response.status === 200) {

                handleSuccessModal({
                    title: "Deleted!",
                    text: "Your account has been successfully deleted"
                });

                setTimeout(
                    function () {
                        window.location.replace("/login");
                    }, REFRESH_DELAY
                );

            }

            const errorData = response.responseJSON.message;

            if (response.status === 404) {

                handleError(errorData);

                setTimeout(
                    function () {
                        window.location.replace("/register");
                    }, REFRESH_DELAY
                );

            } else if (response.status === 409) {

                handleError(errorData);

                setTimeout(
                    function () {
                        window.location.replace("/login");
                    }, REFRESH_DELAY
                );
            }

        },
        dataType: "json",
        contentType: "application/json"
    });
});