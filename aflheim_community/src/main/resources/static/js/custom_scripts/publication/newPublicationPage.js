
// ClassicEditor
BalloonEditor
    .create( document.querySelector( '#editor' ), {

        placeholder: "Write then select the text you want to edit",
        toolbar: {
            items: [
            'undo', 'redo', 'heading', '|',
                'bold', 'italic', 'blockQuote', '|',
                'link', 'mediaEmbed', '|',
                'bulletedList', 'numberedList', '|',
                'insertTable'
                ],
            shouldNotGroupWhenFull: true
            },
        ui: {
            viewportOffset: {
                top: window.innerHeight * 0.05
            }
        },
        mediaEmbed: {
            previewsInData: true
        },
        link: {
            decorators: {
                openInNewTab: {
                    mode: 'manual',
                    label: 'Open in a new tab',
                    attributes: {
                        target: '_blank',
                        rel: 'noopener noreferrer'
                    }
                },
                bold: {
                    mode: 'manual',
                    label: 'Bold link',
                    attributes: {
                        style: 'font-weight: bold; color: #004154;'
                    }
                },
                highlight: {
                    mode: 'manual',
                    label: 'BG Highlighted link',
                    attributes: {
                        style: 'background-color: #f2c689;'
                    }
                }
            }
        },
        heading: {
            options: [
                { model: 'paragraph', title: 'Paragraph', class: 'ck-heading_paragraph' },
                { model: 'heading1', view: 'h1', title: 'Heading 1', class: 'ck-heading_heading1' },
                { model: 'heading2', view: 'h2', title: 'Heading 2', class: 'ck-heading_heading2' },
                { model: 'heading3', view: 'h3', title: 'Heading 3', class: 'ck-heading_heading3' },
                { model: 'heading4', view: 'h4', title: 'Heading 4', class: 'ck-heading_heading4' },
                { model: 'heading5', view: 'h5', title: 'Heading 5', class: 'ck-heading_heading5' },
                { model: 'heading6', view: 'h6', title: 'Heading 6', class: 'ck-heading_heading6' }
            ],
            decorators: {
                bold: {
                    mode: 'automatic',
                    label: 'Bold text',
                    attributes: {
                        style: 'font-weight: 800;'
                    }
                },
                highlight: {
                    mode: 'manual',
                    label: 'BG Highlighted',
                    attributes: {
                        style: 'background-color: #f2c689;'
                    }
                }
            }
        },
        paragraph: {
            decorators: {
                bold: {
                    mode: 'manual',
                    label: 'Bold text',
                    attributes: {
                        style: 'font-weight: bold;'
                    }
                },
                highlight: {
                    mode: 'manual',
                    label: 'BG Highlighted',
                    attributes: {
                        style: 'background-color: #f2c689;'
                    }
                }
            }
        }
    } )
    .then( editor => {
        console.log( 'Editor was initialized', editor );
    } )
    .catch( error => {
        console.error( error );
    } );


// Notification
const swalWithBootstrapButtons = Swal.mixin({
    customClass: {
        confirmButton: 'btn btn-primary',
        cancelButton: 'btn btn-gray'
    },
    buttonsStyling: false
});

// Modal
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

const username = document.getElementById('username').innerText.trim().slice(1);
console.log(`USERNAME ${username}`)

const REFRESH_DELAY = 1500;

// Inputs
const editorArea = document.getElementById('editor');
const submitBtn = document.getElementById('submitPublicationBtn');
const categorySelectionInput = document.getElementById('categories');
const publicationImageInput = document.getElementById('publicationImage');
const publicationDescriptionInput = document.getElementById('publicationDescription');
const publicationTitleInput = document.getElementById('publicationTitle');
const titleInvalidBox = document.getElementById('titleInvalidBox');
const descriptionInvalidBox = document.getElementById('descriptionInvalidBox');
const editorAreaInvalidBox = document.getElementById('editorAreaInvalidBox');

const lettersRegex = /^[A-Za-z0-9 ]+$/;
const prohibitedChars = /['`"]/;
const contentProhibitedChars = /['`]/;

let cardTitle = document.getElementById('cardTitle');
let cardDescription = document.getElementById('cardDescription');

// Input letters check
function checkLettersInput(inputElement) {
    if (!inputElement.value.match(lettersRegex) && prohibitedChars.test(inputElement.value)) {
        return false;
    } else {
        return true;
    }
};

// Input length check
function checkContentLength(type, inputElement) {
  if (type === "title") {
      if (inputElement.value.length > 50) {
          return false;
      }
      return true;

  } else if (type === "description") {
      if (inputElement.value.length > 150) {
          return false;
      }
      return true;

  } else if (type === "content") {
      if (inputElement.innerHTML.length > 4000) {
          return false;
      }
      return true;

  } else {
      return false;
  }
};

function checkContentAreaInput(contentInput) {
    if (!contentInput.innerHTML.match(lettersRegex) && contentProhibitedChars.test(contentInput.innerHTML)) {
        return false;
    } else {
        return true;
    }
}

// List of categories
const categories = [
    {tag: "History"},
    {tag: "Methodology"},
    {tag: "Religion"},
    {tag: "Runes"},
    {tag: "Letters"},
    {tag: "Old Norse"},
    {tag: "Language"},
    {tag: "Sagas"},
    {tag: "Traditions"},
    {tag: "Food"},
    {tag: "Habits"}
]

// Adding the categories when clicked on the dropdown
categorySelectionInput.addEventListener('click', () => {
    if (categorySelectionInput.options.length === 1) {
        const fragment = document.createDocumentFragment();
        categories.forEach(category => {
            const option = document.createElement('option');
            option.value = category.tag;
            option.text = category.tag;
            fragment.appendChild(option);
        });
        categorySelectionInput.appendChild(fragment);
    }
});

// Checking the category's tag validity
function checkCategorySelectionInput() {
    // Based on the value "country code" & the text "country name"
    let selectedCategory = categorySelectionInput.options[categorySelectionInput.selectedIndex];

    let isTagIncluded = categories.some(category => {
        return category.tag === selectedCategory.value.trim() && category.tag === selectedCategory.value.trim();
    });

    return isTagIncluded;
};

const svgIconTitle = '<svg class="text-danger card-body-svg" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M12.395 2.553a1 1 0 00-1.45-.385c-.345.23-.614.558-.822.88-.214.33-.403.713-.57 1.116-.334.804-.614 1.768-.84 2.734a31.365 31.365 0 00-.613 3.58 2.64 2.64 0 01-.945-1.067c-.328-.68-.398-1.534-.398-2.654A1 1 0 005.05 6.05 6.981 6.981 0 003 11a7 7 0 1011.95-4.95c-.592-.591-.98-.985-1.348-1.467-.363-.476-.724-1.063-1.207-2.03zM12.12 15.12A3 3 0 017 13s.879.5 2.5.5c0-1 .5-4 1.25-4.5.5 1 .786 1.293 1.371 1.879A2.99 2.99 0 0113 13a2.99 2.99 0 01-.879 2.121z" clip-rule="evenodd"></path></svg>';

// Title input check
publicationTitleInput.addEventListener('keyup', function () {

    // Setting the default title
    if (this.value === '') {
        cardTitle.innerHTML = "History of Vikings longships" + svgIconTitle;
        return;
    }

    // Applying changes
    cardTitle.innerHTML = this.value + svgIconTitle;

    // Checking input
    if (!checkLettersInput(this)) {
        titleInvalidBox.innerText = "Contains Prohibited Characters"
        this.classList.add('is-invalid');
        return;
    }

    if (!checkContentLength("title", this)) {
        titleInvalidBox.innerText = "Too long title"
        this.classList.add('is-invalid');
        return;
    }

    // else
    if (this.classList.contains('is-invalid')) {
        this.classList.remove('is-invalid');
    }

    this.classList.add('is-valid');
});

publicationTitleInput.addEventListener('blur', function () {
    if (this.value.trim() === '') {
        this.classList.remove('is-valid');
        this.classList.remove('is-invalid');
    }

});

// Description input check
publicationDescriptionInput.addEventListener('keyup', function () {

    // Setting the default title
    if (this.value === '') {
        cardDescription.innerHTML = "Briefing on the history of longships making during the vikings erra!";
        return;
    }

    // Applying changes
    cardDescription.innerText = this.value;

    // Checking the input
    if (!checkLettersInput(this)) {
        descriptionInvalidBox.innerText = "Contains Prohibited Characters"
        this.classList.add('is-invalid');
        return;
    }

    if (!checkContentLength("description", this)) {
        descriptionInvalidBox.innerText = "Too long description"
        this.classList.add('is-invalid');
        return;
    }

    // else
    if (this.classList.contains('is-invalid')) {
        this.classList.remove('is-invalid');
    }

    this.classList.add('is-valid');
});

publicationDescriptionInput.addEventListener('blur', function () {
    if (this.value.trim() === '') {
        this.classList.remove('is-valid');
        this.classList.remove('is-invalid');
    }

});

// Checking content input
editorArea.addEventListener('keyup', function () {

    // Checking the input
    if (!checkContentAreaInput(this)) {
        descriptionInvalidBox.innerText = "Contains Prohibited Characters"
        this.classList.add('is-invalid');
        return;
    }

    if (!checkContentLength("content", this)) {
        descriptionInvalidBox.innerText = "Too long content"
        this.classList.add('is-invalid');
        return;
    }

    // else
    if (this.classList.contains('is-invalid')) {
        this.classList.remove('is-invalid');
    }

    this.classList.add('is-valid');
});

// Check file
function checkPublicationImageFile(file) {
    if (file) {
        var allowedTypes = ['image/png', 'image/jpeg', 'image/webp'];
        var maxSize =  1024 * 1024; // 1MB

        if (allowedTypes.includes(file.type) && file.size <= maxSize) {
            console.log('File is valid');
            if (publicationImageInput.classList.contains('is-invalid')) {
                publicationImageInput.classList.remove('is-invalid');
            };

            return true;

        } else {
            // File is invalid
            publicationImageInput.classList.add('is-invalid');
            publicationImageInput.value = '';

            return false;
        }
    }
};

publicationImageInput.addEventListener('change', function () {
    var fileInput = this;
    var file = fileInput.files[0];

    checkPublicationImageFile(file);
});

// Check inputs readiness
function checkInputReadiness() {

    console.log('check data readiness.')

    if (publicationTitleInput.value !== '') {
        if (!checkLettersInput(publicationTitleInput)) {
            publicationTitleInput.classList.add('is-invalid');
            titleInvalidBox.innerText = "Prohibited characters"
            return false;
        } else if (!checkContentLength("title", publicationTitleInput)) {
            publicationTitleInput.classList.add('is-invalid');
            titleInvalidBox.innerText = "Too long title"
            return false;
        } else if ( publicationTitleInput.value.length < 15) {
            publicationTitleInput.classList.add('is-invalid');
            titleInvalidBox.innerText = "Too short title"
            return false;
        }
    } else {
        publicationTitleInput.classList.add('is-invalid');
        titleInvalidBox.innerText = "Title can't be empty";
        return false;
    }

    if (publicationDescriptionInput.value !== '') {
        if (!checkLettersInput(publicationDescriptionInput)) {
            publicationDescriptionInput.classList.add('is-invalid');
            descriptionInvalidBox.innerText = "Prohibited characters";
            return false;
        } else if (!checkContentLength("description", publicationDescriptionInput)) {
            publicationDescriptionInput.classList.add('is-invalid');
            descriptionInvalidBox.innerText = "Too long description";
            return false;
        } else if (publicationDescriptionInput.value.length < 40) {
            publicationDescriptionInput.classList.add('is-invalid');
            descriptionInvalidBox.innerText = "Too short description";
            return false;
        }
    } else {
        publicationDescriptionInput.classList.add('is-invalid');
        descriptionInvalidBox.innerText = "Description can't be empty";
        return false;
    }

    if (editorArea.innerHTML !== '') {
        if (!checkContentAreaInput(editorArea)) {
            editorArea.classList.add('is-invalid');
            editorAreaInvalidBox.innerText = "Prohibited characters";
            return false;
        } else if (!checkContentLength("content", editorArea)) {
            editorArea.classList.add('is-invalid');
            editorAreaInvalidBox.innerText = "Too long content";
            return false;
        } else if (editorArea.innerHTML.length < 200) {
            editorArea.classList.add('is-invalid');
            editorAreaInvalidBox.innerText = "Too short content";
            return false;
        }
    } else {
        editorArea.classList.add('is-invalid');
        editorAreaInvalidBox.innerText = "Much void! Add some content";
        return false;
    }

    if (categorySelectionInput.options[categorySelectionInput.selectedIndex].value !== '') {
        if (!checkCategorySelectionInput()) {
            categorySelectionInput.classList.add('is-invalid');
            document.getElementById('categoriesInvalidBox').innerText = "Invalid category";
            return false;
        }
    }

    console.log(`FILE : ${publicationImageInput.files[0]}, IS UNDEFINED? ${publicationImageInput.files[0] === undefined}`)
    if (publicationImageInput.files[0] !== undefined) {
        console.log(`7777777777 ${publicationImageInput.files[0].size} | ${publicationImageInput.files[0].name} | ${publicationImageInput.files[0].type}`);
        if (!checkPublicationImageFile(publicationImageInput.files[0])) {
            publicationImageInput.classList.add('is-invalid');
            document.getElementById('imageInvalidBox').innerText = "Invalid file";
            return false;
        }
    } else {
        publicationImageInput.classList.add('is-invalid');
        document.getElementById('imageInvalidBox').innerText = "Each post deserves a proper image";
        return false;
    }

    // All passed
    console.log('check data readiness done.')
    return true;
};

// Clear Inputs
function clearInputs() {
    publicationTitleInput.value = '';
    publicationTitleInput.classList.remove('is-valid');
    publicationTitleInput.classList.remove('is-invalid');

    publicationDescriptionInput.value = '';
    publicationDescriptionInput.classList.remove('is-valid');
    publicationDescriptionInput.classList.remove('is-invalid');

    categorySelectionInput.value = 'None';
    categorySelectionInput.classList.remove('is-valid');
    categorySelectionInput.classList.remove('is-invalid');

    editorArea.innerHTML = '';
    editorArea.classList.remove('is-valid');
    editorArea.classList.remove('is-invalid');

    publicationImageInput.value = '';
    publicationImageInput.classList.remove('is-valid');
    publicationImageInput.classList.remove('is-invalid');

};

// Get data
function getInputsValues() {

    console.log('Get input values.')
    // Preparing image file
    let formData = new FormData();
    formData.append('publicationImage', publicationImageInput.files[0]);

    // Preparing country part
    let finalSelectedCategory;
    if (categorySelectionInput.options[categorySelectionInput.selectedIndex].value === null || categorySelectionInput.options[categorySelectionInput.selectedIndex].value === 'None') {
        finalSelectedCategory = 'None';
    } else {
        finalSelectedCategory = categorySelectionInput.options[categorySelectionInput.selectedIndex].text.trim();
    }

    let content = editorArea.innerHTML;

    let data = {
        title: publicationTitleInput.value,
        description: publicationDescriptionInput.value,
        category: finalSelectedCategory,
        content: content
    };

    formData.append("data", JSON.stringify(data));

    return formData;
};

// AJAX Request Function
function submitPublishRequest() {

    if (checkInputReadiness()) {
        // Valid inputs
        let data = getInputsValues();
        $.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: "/profile/publication/add?username=" + username,
            data: data,
            contentType: false,
            processData: false,
            cache: false,
            success: function(response) {

                // Success response
                handleSuccessModal({
                    title: "It's Online!",
                    text: "The post has been made successfully"
                });

                // Clearing inputs
                clearInputs();

                // Redirecting to the post
                setTimeout(
                    function () {
                        window.location.replace("/publications/view?publicationId="+response);
                    }, REFRESH_DELAY
                );

            },
            error: function(response) {

                // Handle the error response
                const errorData = JSON.parse(response.responseText);
                handleError(errorData);

            }
        });

    } else {
        // Invalid inputs caught
        handleError({ errorMessage: "Form contains invalid data" })
    }
};

// Submit form
submitBtn.addEventListener('click', function () {
   console.log(editorArea.innerHTML);

   submitPublishRequest();
});

