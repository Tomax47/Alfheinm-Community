
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

const submitBtn = document.getElementById('resendSubmitBtn');
const REFRESH_DELAY = 1500;
const email = document.getElementById('email').placeholder.trim();

console.log(`Email ${email}`)

function submitResendConfirmationEmail() {

    let url = "/confirm/process/resend?email="+email;

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
                    title: "Resnet!",
                    text: "Another confirmation email has been sent to your email"
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
};

submitBtn.addEventListener('click', function () {

    submitBtn.classList.add('disabled');
    submitResendConfirmationEmail();
});