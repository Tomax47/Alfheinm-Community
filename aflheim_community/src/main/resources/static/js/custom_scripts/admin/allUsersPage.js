console.log("All users page script is hooked.")

const userTable = document.querySelector('#datatable');

// Refetch
let Refetch = () => {
    console.log("RE-FETCH HOOKED.")
    $.ajax({
        type: "GET",
        url: "/admin/users",
        success: function (response) {
            RenderServicesTable(response, userTable)
        },
        dataType: "json",
        contentType: "application/json"
    })
};

function RenderServicesTable(responseUserDto, usersTable) {
    console.log("RENDERING THE USERS TABLE");

    let tableContent = '<thead>\n<tr>\n' +
        '                          <th>Username</th>' +
        '                          <th>Email</th>' +
        '                          <th>Role</th>' +
        '                          <th>Account State</th>' +
        '                          <th>Lead</th>' +
        '                      </tr>\n</thead>\n<tbody>';

    for (let i = 0; i < responseUserDto.length; i++) {
        tableContent += '<tr>';
        tableContent += '    <td>'+ responseUserDto[i]['username'] +'</td>';
        tableContent += '    <td>'+ responseUserDto[i]['email'] +'</td>';
        tableContent += '    <td>'+ responseUserDto[i]['role'] +'</td>';
        tableContent += '    <td>'+ responseUserDto[i]['accountState'] +'</td>';
        tableContent += '    <td>'+ '<a' + 'th:href="@{/admin/users/{username}(username=${' + responseUserDto[i]['username'] + '})}"' + '>' + 'View Profile' +'</a>' +'</td>';
        tableContent += '</tr>';
    };

    tableContent += '</tbody>';

    console.log("Setting final table up!");
    usersTable.html(tableContent);

};

// if (userTable.children.length === 0) {
//     console.log("FIRST PAGE IS LOADING!")
//     Refetch;
// } else {
//     console.log('The tbody has child elements.');
// }