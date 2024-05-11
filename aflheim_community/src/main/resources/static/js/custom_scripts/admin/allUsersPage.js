console.log("All users page script is hooked.")

const usersDataTable = document.querySelector('#usersDataTable');
let currentPageBtn = document.getElementById('currentPage');
let nextPageBtn = document.getElementById('nextPage');
let previousPageBtn = document.getElementById('previousPage');

let currentPageNumber = parseInt(currentPageBtn.innerText);

const searchQueryInput = document.getElementById('searchQueryInput');
const sizeSelect = document.getElementById('sizeSelect');
const sizeStatement = document.getElementById('statementSize');
currentPageBtn.addEventListener('click', () => {
    let query;
    let size = sizeSelect.options[sizeSelect.selectedIndex].value;

    if (searchQueryInput.value === '') {
        query = null;
    } else {
        query = searchQueryInput.value;
    }

    if (size !== '4' && size !== '8' && size !== '10' && size !== '12') {
        size = null;
    }

    PagSearchUsers(currentPageNumber, size, query);
});
nextPageBtn.addEventListener('click', () => {

    currentPageNumber += 1;
    let query;
    let size = sizeSelect.options[sizeSelect.selectedIndex].value;

    if (searchQueryInput.value === '') {
        query = null;
    } else {
        query = searchQueryInput.value;
    }

    if (size !== '4' && size !== '8' && size !== '10' && size !== '12') {
        size = null;
    }

    console.log(`Size : ${size}. Query : ${query}`);

    // Fetching next page's data
    PagSearchUsers(currentPageNumber, size, query);

    // Updating the currentPageNumber value
    currentPageBtn.innerText = currentPageNumber;
    currentPageBtn.setAttribute("value", `${currentPageNumber}`)
});
previousPageBtn.addEventListener('click', () => {
    if (currentPageNumber != "0") {

        let query;
        let size = sizeSelect.options[sizeSelect.selectedIndex].value;

        if (searchQueryInput.value === '') {
            query = null;
        } else {
            query = searchQueryInput.value;
        }

        if (size !== '4' && size !== '8' && size !== '10' && size !== '12') {
            size = null;
        }

        currentPageNumber -= 1;
        // Fetching previous page's data
        PagSearchUsers(currentPageNumber, size, query);

        // Updating the currentPageNumber value
        currentPageBtn.innerText = currentPageNumber;
        currentPageBtn.setAttribute("value", `${currentPageNumber}`)
    } else {
        onsole.log("Can't fetch pages below 0!")
    }
});

// Size selection change
sizeSelect.addEventListener('change', function () {
    let query;
    let size = sizeSelect.options[sizeSelect.selectedIndex].value;

    if (searchQueryInput.value === '') {
        query = null;
    } else {
        query = searchQueryInput.value;
    }

    if (size !== '4' && size !== '8' && size !== '10' && size !== '12') {
        size = null;
    }

    // Fetching previous page's data
    PagSearchUsers(currentPageNumber, size, query);

    if (size !== '4' && size !== '8' && size !== '10' && size !== '12') {
        sizeStatement.innerText = '4';
    } else {
        sizeStatement.innerText = size;
    }
});

// Query input change
searchQueryInput.addEventListener('input', function () {
    let query;
    let size = sizeSelect.options[sizeSelect.selectedIndex].value;

    if (searchQueryInput.value === '') {
        query = null;
    } else {
        query = searchQueryInput.value;
    }

    if (size !== '4' && size !== '8' && size !== '10' && size !== '12') {
        size = null;
    }

    // Fetching previous page's data
    PagSearchUsers(currentPageNumber, size, query);
});

// Refetch
function PagSearchUsers(page, size, query) {

    console.log(`RE-FETCH HOOKED. Page -> ${page}`)

    let url = `/admin/users/paginate?page=${page}&size=${size}&query=${query}&sort=null&direction=null`;

    console.log(`DATA URL -> ${url.toString()}`)

    // Sending the requests.
    $.ajax({
        type: "GET",
        url: url,
        success: function(data) {
            RenderServicesTable(data);
        },
        error: "redirect:/admin/users?error",
        dataType: "json",
        contentType: "application/json"
    });
};

function RenderServicesTable(responseUserDto) {
    console.log("RENDERING THE USERS TABLE");

    // Clear any existing tbody
    const existingTbody = usersDataTable.querySelector('tbody');
    if (existingTbody) {
        existingTbody.remove();
    }

    // Create a new tbody element
    const newTbody = document.createElement('tbody');

    // Setting the content up
    responseUserDto.forEach( (user) => {
        console.log('USER LOG : ' + user.toString());
        console.log("Setting final table up! Appending user...");

        //Preparing the userRole and userAccountState
        let userRoleElement;
        if (user.role === 'ADMIN') {
            userRoleElement = '<span class="badge bg-success">Admin</span>';
        } else if (user.role === 'MEMBER'){
            userRoleElement = `<span class="badge bg-secondary">Member</span>`;
        } else {
            userRoleElement = `<span class="badge bg-primary">Visitor</span>`;
        }

        let userAccountStateElement;
        if (user.accountState === 'CONFIRMED') {
            userAccountStateElement = `<span class="fw-normal text-success">Confirmed</span>`;
        } else if (user.accountState === 'NOT_CONFIRMED') {
            userAccountStateElement = `<span class="fw-normal text-secondary">Not Confirmed</span>`;
        } else {
            userAccountStateElement = `<span class="fw-normal text-danger">` + user.accountState + `</span>`;
        }

        let usersListItem = '<tr>' +
            '                            <td>' +
            '                                <div class="form-check dashboard-check">' +
            '                                    <input class="form-check-input" type="checkbox" value="" id="userCheck1">' +
            '                                    <label class="form-check-label" for="userCheck1"></label>' +
            '                                </div>' +
            '                            </td>' +
            '                            <td>' +
            '                                <a href="/admin/users/' + user.username + '" class="d-flex align-items-center">' +
            '                                    <img src="' + user.profilePicture + '" class="avatar rounded-circle me-3" alt="Avatar">' +
            '                                    <div class="d-block">' +
            '                                        <span class="fw-black">@' + user.username + '</span>' +
            '                                        <div class="small text-gray">' + user.email + '</div>' +
            '                                    </div>' +
            '                                </a>' +
            '                            </td>' +
            '                            <td>' +
            '                                <span class="fw-normal">' + user.birthdate + '</span>' +
            '                            </td>' +
            '                            <td>' +
                                            userRoleElement +
            '                            </td>' +
            '                            <td>' +
                                            userAccountStateElement +
            '                            </td>' +
            '                            <td>' +
            '                                <span class="fw-normal text-black">' + user.reputation + '</span>' +
            '                            </td>' +
            '                       </tr>';

        const tr = document.createElement('tr');
        tr.innerHTML = usersListItem;
        newTbody.appendChild(tr);
    });

    usersDataTable.appendChild(newTbody);
}