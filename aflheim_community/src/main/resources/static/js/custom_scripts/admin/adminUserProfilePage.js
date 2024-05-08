console.log("CUSTOM ALERT ADMIN PROFILE PAGE SCRIPT HOOKED.")
const swalWithBootstrapButtons = Swal.mixin({
    customClass: {
        confirmButton: 'btn btn-primary',
        cancelButton: 'btn btn-gray'
    },
    buttonsStyling: false
});

let username = document.getElementById('username').textContent;

console.log(`USERNAME : ${username}`)

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
            // Admin confirmed, perform delete action
            console.log('ADMIN confirmed deletion');
            let url = "/admin/users/"+username+"/delete";

            $.ajax({

            });

        } else if (result.dismiss === Swal.DismissReason.cancel) {
            // Admin cancelled
            console.log('ADMIN cancelled deletion');
        }
    });
});
