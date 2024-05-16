
// Modals & Notifications
const swalWithBootstrapButtons = Swal.mixin({
    customClass: {
        confirmButton: 'btn btn-primary',
        cancelButton: 'btn btn-gray'
    },
    buttonsStyling: false
});
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
function handleInfoModal(message) {
    const notyf = new Notyf({
        position: {
            x: 'center',
            y: 'top',
        },
        types: [
            {
                type: 'info',
                background: '#003044',
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
        message: message
    });
};

// Success modal
function handleSuccessModal(messageData) {
    swalWithBootstrapButtons.fire({
        icon: 'success',
        title: messageData.title,
        text: messageData.text,
        showConfirmButton: true,
        timer: 1500
    });
};

// Const
const publicationId = document.getElementById('publicationId');
const noReviewsBtn = document.getElementById('noReviewsYet');
const upVoteBtn = document.getElementById('upVoteBtn');
const downVoteBtn = document.getElementById('downVoteBtn');
const REFRESH_DELAY = 1500;

// Handling no reviews clicked
if (noReviewsBtn != null) {
    noReviewsBtn.addEventListener('click', function () {

        console.log('btn clicked')
        handleInfoModal("Be the first to interact with this publication!");
    });
};

// Handle upVote & removeUpVote
if (upVoteBtn != null) {
    upVoteBtn.addEventListener('click', function () {
        changeUpVoteStatus();
    });
};

function changeUpVoteStatus() {

    console.log(`PUBLICATION ID : ${publicationId.innerText}`)
    let url = "/publications/view/upvote?publicationId="+publicationId.innerText;

    $.ajax({
        type: "POST",
        url: url,
        success: function(response) {

            if (upVoteBtn.id === "removeUpVoteBtn") {
                upVoteBtn.className = "btn btn-outline-indigo d-inline-flex align-items-center w-7";
                upVoteBtn.id = "upVoteBtn";
                upVoteBtn.innerHTML =
                    '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" style="fill: rgba(255, 255, 255, 1);transform: ;msFilter:;"><path d="M20 8h-5.612l1.123-3.367c.202-.608.1-1.282-.275-1.802S14.253 2 13.612 2H12c-.297 0-.578.132-.769.36L6.531 8H4c-1.103 0-2 .897-2 2v9c0 1.103.897 2 2 2h13.307a2.01 2.01 0 0 0 1.873-1.298l2.757-7.351A1 1 0 0 0 22 12v-2c0-1.103-.897-2-2-2zM4 10h2v9H4v-9zm16 1.819L17.307 19H8V9.362L12.468 4h1.146l-1.562 4.683A.998.998 0 0 0 13 10h7v1.819z"></path></svg> '
                handleInfoModal("U have unliked the post");

                return;
            }

            if (downVoteBtn.classList.contains("btn-danger")) {
                // Updating downvote btn style
                downVoteBtn.className = "btn btn-outline-danger d-inline-flex align-items-center w-7";
                downVoteBtn.id = "upVoteBtn";
                downVoteBtn.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" style="fill: rgba(255, 255, 255, 1);transform: ;msFilter:;"><path d="M20 3H6.693A2.01 2.01 0 0 0 4.82 4.298l-2.757 7.351A1 1 0 0 0 2 12v2c0 1.103.897 2 2 2h5.612L8.49 19.367a2.004 2.004 0 0 0 .274 1.802c.376.52.982.831 1.624.831H12c.297 0 .578-.132.769-.36l4.7-5.64H20c1.103 0 2-.897 2-2V5c0-1.103-.897-2-2-2zm-8.469 17h-1.145l1.562-4.684A1 1 0 0 0 11 14H4v-1.819L6.693 5H16v9.638L11.531 20zM18 14V5h2l.001 9H18z"></path></svg>'
            }

            upVoteBtn.className = "btn btn-indigo d-inline-flex align-items-center w-6";
            upVoteBtn.id = "removeUpVoteBtn";
            upVoteBtn.innerHTML = '<i>' + response + '</i>'+
                '<svg class="ml-2" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" style="fill: rgba(255, 255, 255, 1);transform: ;msFilter:;"><path d="M4 21h1V8H4a2 2 0 0 0-2 2v9a2 2 0 0 0 2 2zM20 8h-7l1.122-3.368A2 2 0 0 0 12.225 2H12L7 7.438V21h11l3.912-8.596L22 12v-2a2 2 0 0 0-2-2z"></path></svg>'
            handleInfoModal("U have liked the post");

            return;
        },
        error: function(reponse) {
            // Handle the error response
            const errorData = JSON.parse(reponse.responseText);
            handleError(errorData);
            return;
        },
        dataType: "json",
        contentType: "application/json"
    });
};

// Handle change downVote status

if (downVoteBtn != null) {
    downVoteBtn.addEventListener('click', function () {
        changeDownVoteStatus();
    });
};

function changeDownVoteStatus() {

    let url = "/publications/view/downvote?publicationId="+publicationId.innerText;

    $.ajax({
        type: "POST",
        url: url,
        success: function(response) {

            if (downVoteBtn.id === "removeDownVoteBtn") {
                downVoteBtn.className = "btn btn-outline-danger d-inline-flex align-items-center w-7";
                downVoteBtn.id = "upVoteBtn";
                downVoteBtn.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" style="fill: rgba(255, 255, 255, 1);transform: ;msFilter:;"><path d="M20 3H6.693A2.01 2.01 0 0 0 4.82 4.298l-2.757 7.351A1 1 0 0 0 2 12v2c0 1.103.897 2 2 2h5.612L8.49 19.367a2.004 2.004 0 0 0 .274 1.802c.376.52.982.831 1.624.831H12c.297 0 .578-.132.769-.36l4.7-5.64H20c1.103 0 2-.897 2-2V5c0-1.103-.897-2-2-2zm-8.469 17h-1.145l1.562-4.684A1 1 0 0 0 11 14H4v-1.819L6.693 5H16v9.638L11.531 20zM18 14V5h2l.001 9H18z"></path></svg>'
                handleInfoModal("Publication down-vote removed!");

                return;
            }

            if (upVoteBtn.classList.contains("btn-indigo")) {
                // Updating upvote btn style
                upVoteBtn.className = "btn btn-outline-indigo d-inline-flex align-items-center w-7";
                upVoteBtn.id = "upVoteBtn";
                upVoteBtn.innerHTML =
                    '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" style="fill: rgba(255, 255, 255, 1);transform: ;msFilter:;"><path d="M20 8h-5.612l1.123-3.367c.202-.608.1-1.282-.275-1.802S14.253 2 13.612 2H12c-.297 0-.578.132-.769.36L6.531 8H4c-1.103 0-2 .897-2 2v9c0 1.103.897 2 2 2h13.307a2.01 2.01 0 0 0 1.873-1.298l2.757-7.351A1 1 0 0 0 22 12v-2c0-1.103-.897-2-2-2zM4 10h2v9H4v-9zm16 1.819L17.307 19H8V9.362L12.468 4h1.146l-1.562 4.683A.998.998 0 0 0 13 10h7v1.819z"></path></svg> '
            }

            downVoteBtn.className = "btn btn-danger d-inline-flex align-items-center w-7";
            downVoteBtn.id = "removeDownVoteBtn";
            downVoteBtn.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" style="fill: rgba(255, 255, 255, 1);transform: ;msFilter:;"><path d="M20 3h-1v13h1a2 2 0 0 0 2-2V5a2 2 0 0 0-2-2zM4 16h7l-1.122 3.368A2 2 0 0 0 11.775 22H12l5-5.438V3H6l-3.937 8.649-.063.293V14a2 2 0 0 0 2 2z"></path></svg>'
            handleInfoModal("Publication down-voted!");

            return;
        },
        error: function(reponse) {
            // Handle the error response
            const errorData = JSON.parse(reponse.responseText);
            handleError(errorData);
            return;
        },
        dataType: "json",
        contentType: "application/json"
    });
};

// Handle deleting publication
function submitPublicationDeleteRequest() {
    let url = "/publications/delete?publicationId="+publicationId.innerText;

    $.ajax({
        type: "POST",
        url: url,
        success: function(response) {
            deletePublicationBtn.disabled = true;

            // Throwing the success modal
            handleSuccessModal({
                title: "Success!",
                text: "Publication has been successfully deleted."
            });

            // Refreshing 'After a 1.5s delay'.
            setTimeout(
                function () {
                    window.location.replace("/publications/feed");
                }, REFRESH_DELAY
            );

            return;
        },
        error: function(reponse) {
            // Handle the error response
            const errorData = JSON.parse(reponse.responseText);
            handleError(errorData);
            return;
        },
        dataType: "json",
        contentType: "application/json"
    });
};

function handlePublicationDeleteModal() {
    let footer = '<p>Make sure to comply with '+'<a href="/privacy" class="text-primary fw-black">Alfheim\'s policies</a>'+'</p>'

    swalWithBootstrapButtons.fire({
        icon: 'error',
        title: 'Un-Revertible Action!',
        text: 'Are you sure you want to delete the publication?',
        showCancelButton: true,
        confirmButtonClass: 'btn btn-danger',
        confirmButtonText: 'Delete Publication',
        cancelButtonClass: 'bt-outline-grey-400 fw-black',
        cancelButtonText: 'Cancel',
        footer: footer
    }).then((result) => {
        if (result.isConfirmed) {
            // Confirmed, submitting delete req
            submitPublicationDeleteRequest();

        } else if (result.dismiss === Swal.DismissReason.cancel) {
            // Cancelled
        }
    });
};

const deletePublicationBtn = document.getElementById('deletePublicationBtn');

if (deletePublicationBtn != null) {
    deletePublicationBtn.addEventListener('click', function () {
        console.log('clicked')
        handlePublicationDeleteModal();
    });
};