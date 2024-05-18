
// Modals
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

// Vars
let currentPageBtn = document.getElementById('currentPage');
let nextPageBtn = document.getElementById('nextPage');
let previousPageBtn = document.getElementById('previousPage');
const publicationsBodyFeed = document.getElementById('publicationsBody');
const prevBtnPageItem = document.getElementById('previousBtnPageItem');
const nextBtnPageItem = document.getElementById('nextBtnPageItem');
const searchQueryInput = document.getElementById('searchQueryInput');

let currentPageNumber = parseInt(currentPageBtn.innerText);

// Formatting the date
function formatDate(timestamp) {
    const date = new Date(timestamp);
    const month = date.getMonth() + 1;
    const day = date.getDate();
    const year = date.getFullYear().toString().slice(-2);
    let hours = date.getHours();
    const minutes = date.getMinutes();
    const ampm = hours >= 12 ? 'PM' : 'AM';

    hours = hours % 12;
    hours = hours ? hours : 12; // Handle midnight (0 hours)

    return `${month}/${day}/${year}, ${hours}:${minutes < 10 ? '0' : ''}${minutes} ${ampm}`;
}

// Add publications
function addPublications(publicationDtos) {
    // Clear the old data
    publicationsBodyFeed.innerHTML = '';

    let cardsDiv = document.createElement('div');
    cardsDiv.setAttribute("class", "row");

    publicationDtos.forEach((publication) => {

        // Setting the icon up
        let icon = '';
        if ((publication.upVotesCount + publication.downVotesCount > 4) && (publication.upVotesCount * 100) / (publication.downVotesCount + publication.upVotesCount) > 80.0) {
          icon = '<svg class="text-danger card-body-svg" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" d="M12.395 2.553a1 1 0 00-1.45-.385c-.345.23-.614.558-.822.88-.214.33-.403.713-.57 1.116-.334.804-.614 1.768-.84 2.734a31.365 31.365 0 00-.613 3.58 2.64 2.64 0 01-.945-1.067c-.328-.68-.398-1.534-.398-2.654A1 1 0 005.05 6.05 6.981 6.981 0 003 11a7 7 0 1011.95-4.95c-.592-.591-.98-.985-1.348-1.467-.363-.476-.724-1.063-1.207-2.03zM12.12 15.12A3 3 0 017 13s.879.5 2.5.5c0-1 .5-4 1.25-4.5.5 1 .786 1.293 1.371 1.879A2.99 2.99 0 0113 13a2.99 2.99 0 01-.879 2.121z" clip-rule="evenodd"></path></svg>'
        };

        let publicationBody = '<div class="col-lg-6">' +
            '           <div class="card mb-4">' +
            '              <a href="#"><img class="card-img-top" src="' + publication.coverImageUrl + '" alt="..." /></a>' +
            '              <div class="card-body">' +
            '                <a class="d-flex align-items-center">' +
            '                  <img class="avatar rounded-circle me-1" src="' + publication.authorProfilePictureUrl + '" alt="Avatar">' +
            '                  <div class="d-block">' +
            '                    <span class="fw-black">@' + publication.authorUsername + '</span>' +
            '                  </div>' +
            '                </a>' +
            '                <div class=" d-flex ml-2 small text-muted mb-3">Posted on ' + formatDate(publication.createdAt) + '</div>' +
            '                <h2 class="card-title h4 fw-black">' + publication.title +
                                icon +
            '                </h2>' +
            '                <p class="card-text">' + publication.description + '</p>' +
            '                <a class="btn btn-outline-gray-500" href="/publications/view?publicationId=' + publication.id + '">Read more</a>' +
            '              </div>' +
            '            </div>' +
            '           </div>';

        // Append the new data
        cardsDiv.innerHTML += publicationBody;
    });

    publicationsBodyFeed.appendChild(cardsDiv);
};

// Handle fetching publications
function PagSearchPublications(page, query) {
    let url = `/publications/feed/pag?page=${page}&query=${query}`;

    // Sending the requests.
    $.ajax({
        type: "GET",
        url: url,
        success: function(response) {
            addPublications(response);
        },
        error: function (response) {
            const errorData = JSON.parse(response.responseText);

            if (currentPageNumber > 0) {
                currentPageNumber -= 1;
            }

            currentPageBtn.innerText = currentPageNumber;
            currentPageBtn.setAttribute("value", `${currentPageNumber}`)

            nextBtnPageItem.classList.add('disabled');
            handleError(errorData);
        },
        dataType: "json",
        contentType: "application/json"
    });
};

currentPageBtn.addEventListener('click', () => {
    let query;

    if (currentPageNumber < 0) {
        currentPageNumber = 0;

        // Disabling prev
        if (!prevBtnPageItem.classList.contains('disabled')) {
            prevBtnPageItem.classList.add('disabled');
        }

        // Enabling next
        if (nextBtnPageItem.classList.contains('disabled')) {
            nextBtnPageItem.classList.remove('disabled');
        }
    }

    if (currentPageNumber === 0 && nextBtnPageItem.classList.contains('disabled')) {
        nextBtnPageItem.classList.remove('disabled');
    }


    if (searchQueryInput.value === '') {
        query = null;
    } else {
        query = searchQueryInput.value;
    }

    PagSearchPublications(currentPageNumber, query);
});
nextPageBtn.addEventListener('click', () => {

    currentPageNumber += 1;

    if (prevBtnPageItem.classList.contains('disabled')) {
        prevBtnPageItem.classList.remove('disabled');
    }

    let query;

    if (currentPageNumber < 0) {
        currentPageNumber = 0;
    }

    if (searchQueryInput.value === '') {
        query = null;
    } else {
        query = searchQueryInput.value;
    }

    console.log(`Query : ${query}`);

    // Fetching next page's data
    PagSearchPublications(currentPageNumber, query);

    // Updating the currentPageNumber value
    currentPageBtn.innerText = currentPageNumber;
    currentPageBtn.setAttribute("value", `${currentPageNumber}`)
});
previousPageBtn.addEventListener('click', () => {
    if (currentPageNumber != 0) {

        if (nextBtnPageItem.classList.contains('disabled')) {
            nextBtnPageItem.classList.remove('disabled');
        }

        let query;

        if (currentPageNumber < 0) {
            currentPageNumber = 0;
        }

        if (searchQueryInput.value === '') {
            query = null;
        } else {
            query = searchQueryInput.value;
        }

        currentPageNumber -= 1;
        // Fetching previous page's data
        PagSearchPublications(currentPageNumber, query);

        // Updating the currentPageNumber value
        currentPageBtn.innerText = currentPageNumber;
        currentPageBtn.setAttribute("value", `${currentPageNumber}`)
    } else {
        prevBtnPageItem.classList.add('disabled');
        console.log("Can't fetch pages below 0!")
    }
});

searchQueryInput.addEventListener('keyup', function () {

    if (searchQueryInput.value === '') {
        if (nextBtnPageItem.classList.contains('disabled')) {
            nextBtnPageItem.classList.remove('disabled');
        }

        if (currentPageNumber > 0 && prevBtnPageItem.classList.contains('disabled')) {
            prevBtnPageItem.classList.remove('disabled')
        }
    }
    PagSearchPublications(currentPageNumber, searchQueryInput.value);
});

// Categories search
function CategorySearch(page, category) {
    let url = `/publications/categories?page=${page}&category=${category}`;

    // Sending the requests.
    $.ajax({
        type: "GET",
        url: url,
        success: function(response) {
            addPublications(response);
        },
        error: function (response) {
            const errorData = response.responseJSON.message;

            if (currentPageNumber > 0) {
                currentPageNumber -= 1;
            }

            currentPageBtn.innerText = currentPageNumber;
            currentPageBtn.setAttribute("value", `${currentPageNumber}`)

            nextBtnPageItem.classList.add('disabled');
            handleError(errorData);
        },
        dataType: "json",
        contentType: "application/json"
    });
};

const categories = document.querySelectorAll('.badge');

// List of categories
const categoriesList = [
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

// Check category inclusion
function checkCategorySelectionInput(selectedCategory) {
    // Based on the value "country code" & the text "country name"

    let isTagIncluded = categoriesList.some(category => {
        return category.tag === selectedCategory;
    });

    return isTagIncluded;
};
categories.forEach(category => {
    category.addEventListener('click', function() {

        const categoryName = this.innerText.trim();

        // Perform an action based on the clicked category
        if (checkCategorySelectionInput(categoryName)) {
            CategorySearch(currentPageNumber, categoryName);
        } else {
            handleError({ errorMessage: "No such category!" });
        }

    });
});