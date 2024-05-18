
// Modals & Notifications
const swalWithBootstrapButtons = Swal.mixin({
    customClass: {
        confirmButton: 'btn btn-primary',
        cancelButton: 'btn btn-gray'
    },
    buttonsStyling: false

});

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
        message: errorData
    });
};

// Handle information notification modal
function handleInfoNotificationModal(infoMessage) {
    const notyf = new Notyf({
        position: {
            x: 'center',
            y: 'top',
        },
        types: [
            {
                type: 'info',
                background: '#0494d2',
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
        message: infoMessage
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

// Handle info modal
function handleInfoModal(messageData) {
    swalWithBootstrapButtons.fire(
        messageData.title,
        messageData.text,
        'info'
    )
};

// Const
let username = document.getElementById('username').textContent.trim().slice(1);

// Update form
let nameInput = document.getElementById('first_name');
let surnameInput = document.getElementById('last_name');
let birthdayInput = document.getElementById('birthday');
let genderInput = document.getElementById('gender');
let phoneInput = document.getElementById('phone');
let addressInput = document.getElementById('address');
let countryInput = document.getElementById('country');
let cityInput = document.getElementById('city');
let regionInput = document.getElementById('region');
let zipInput = document.getElementById('zip');
let fileInput = document.getElementById('profilePicture');
let updateFormSubmitBtn = document.getElementById('updateFormSubmitBtn');
const form = document.querySelector('form');

// Regex
var lettersRegex = /^[A-Za-z ]+$/;
var numbersRegex = /^[0-9]+$/;
var addressRegex = /^[a-zA-Z0-9., ]+$/;
var dateRegex = /^\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$/;

function checkInput(inputElement) {
    if (!inputElement.value.match(lettersRegex)) {
        return false;
    } else {
        return true;
    }
};

// Event listeners to check inputs on keyup
nameInput.addEventListener('keyup', function() {
    if (!checkInput(nameInput)) {
        this.classList.remove('is-valid');
        this.classList.add('is-invalid');
    } else {
        this.classList.add('is-valid');
        this.classList.remove('is-invalid');
    }
});

nameInput.addEventListener('blur', function() {
    if (this.value.trim() === '') {
        this.classList.remove('is-valid');
        this.classList.remove('is-invalid');
    }
});

// Surname
surnameInput.addEventListener('keyup', function() {
    if (!checkInput(surnameInput)) {
        this.classList.remove('is-valid');
        this.classList.add('is-invalid');
    } else {
        this.classList.add('is-valid');
        this.classList.remove('is-invalid');
    }
});

surnameInput.addEventListener('blur', function() {
    if (this.value.trim() === '') {
        this.classList.remove('is-valid');
        this.classList.remove('is-invalid');
    }
});

// City
cityInput.addEventListener('keyup', function() {
    if (!checkInput(cityInput)) {
        this.classList.remove('is-valid');
        this.classList.add('is-invalid');
    } else {
        this.classList.add('is-valid');
        this.classList.remove('is-invalid');
    }
});

cityInput.addEventListener('blur', function() {
    if (this.value.trim() === '') {
        this.classList.remove('is-valid');
        this.classList.remove('is-invalid');
    }
});

// Region
regionInput.addEventListener('keyup', function() {
    if (!checkInput(regionInput)) {
        this.classList.remove('is-valid');
        this.classList.add('is-invalid');
    } else {
        this.classList.add('is-valid');
        this.classList.remove('is-invalid');
    }
});

regionInput.addEventListener('blur', function() {
    if (this.value.trim() === '') {
        this.classList.remove('is-valid');
        this.classList.remove('is-invalid');
    }
});

// Number and ZIP input check
function checkNumericalInput(inputElement) {
    if (!inputElement.value.match(numbersRegex)) {
        return false;
    } else {
        return true;
    }
};
phoneInput.addEventListener('keyup', function() {
    if (!checkNumericalInput(phoneInput) || this.value.length !== 10) {
        this.classList.remove('is-valid');
        this.classList.add('is-invalid');
    } else {
        this.classList.add('is-valid');
        this.classList.remove('is-invalid');
    }
});

phoneInput.addEventListener('blur', function() {
    if (this.value.trim() === '') {
        this.classList.remove('is-valid');
        this.classList.remove('is-invalid');
    }
});

zipInput.addEventListener('keyup', function() {
    if (!checkNumericalInput(zipInput) || this.value.length > 10 || this.value.length < 5) {
        this.classList.remove('is-valid');
        this.classList.add('is-invalid');
    } else {
        this.classList.add('is-valid');
        this.classList.remove('is-invalid');
    }
});

zipInput.addEventListener('blur', function() {
    if (this.value.trim() === '') {
        this.classList.remove('is-valid');
        this.classList.remove('is-invalid');
    }
});

// Address
function checkAddressInput(inputElement) {
    if (!inputElement.value.match(addressRegex)) {
        return false;
    } else {
        return true;
    }
};
addressInput.addEventListener('keyup', function() {
    if (!checkAddressInput(addressInput)) {
        this.classList.remove('is-valid');
        this.classList.add('is-invalid');
    } else {
        this.classList.add('is-valid');
        this.classList.remove('is-invalid');
    }
});

addressInput.addEventListener('blur', function() {
    if (this.value.trim() === '') {
        this.classList.remove('is-valid');
        this.classList.remove('is-invalid');
    }
});

// Gender input check
genderInput.addEventListener('change', function() {

    // Based on the value
    let selectedGender = genderInput.value;

    // Check if the selected gender is in the list of countries
    console.log(`SELECTED GENDER : ${selectedGender}`)
    if (selectedGender === "Male" || selectedGender === "Female" || selectedGender === "None") {
        if (this.classList.contains('is-invalid')) {
            this.classList.remove('is-invalid');
        }
    } else {
        this.classList.add('is-invalid');
    }
});

// Check file validation
function checkProfileImageFile(file) {
    if (file) {
        var allowedTypes = ['image/png', 'image/jpeg', 'image/webp'];
        var maxSize =  1024 * 1024; // 1MB

        if (allowedTypes.includes(file.type) && file.size <= maxSize) {
            console.log('File is valid');
            if (fileInput.classList.contains('is-invalid')) {
                fileInput.classList.remove('is-invalid');
            };

            return true;

        } else {
            // File is invalid
            fileInput.classList.add('is-invalid');
            fileInput.value = '';

            return false;
        }
    }
};

fileInput.addEventListener('change', function () {
    var fileInput = this;
    var file = fileInput.files[0];

    checkProfileImageFile(file);
});

// Country selection and input check
const countries = [
    { name: 'Afghanistan', code: 'AF' },
    { name: 'Åland Islands', code: 'AX' },
    { name: 'Albania', code: 'AL' },
    { name: 'Algeria', code: 'DZ' },
    { name: 'American Samoa', code: 'AS' },
    { name: 'Andorra', code: 'AD' },
    { name: 'Angola', code: 'AO' },
    { name: 'Anguilla', code: 'AI' },
    { name: 'Antarctica', code: 'AQ' },
    { name: 'Antigua and Barbuda', code: 'AG' },
    { name: 'Argentina', code: 'AR' },
    { name: 'Armenia', code: 'AM' },
    { name: 'Aruba', code: 'AW' },
    { name: 'Australia', code: 'AU' },
    { name: 'Austria', code: 'AT' },
    { name: 'Azerbaijan', code: 'AZ' },
    { name: 'Bahamas', code: 'BS' },
    { name: 'Bahrain', code: 'BH' },
    { name: 'Bangladesh', code: 'BD' },
    { name: 'Barbados', code: 'BB' },
    { name: 'Belarus', code: 'BY' },
    { name: 'Belgium', code: 'BE' },
    { name: 'Belize', code: 'BZ' },
    { name: 'Benin', code: 'BJ' },
    { name: 'Bermuda', code: 'BM' },
    { name: 'Bhutan', code: 'BT' },
    { name: 'Bolivia', code: 'BO' },
    { name: 'Bosnia and Herzegovina', code: 'BA' },
    { name: 'Botswana', code: 'BW' },
    { name: 'Bouvet Island', code: 'BV' },
    { name: 'Brazil', code: 'BR' },
    { name: 'British Indian Ocean Territory', code: 'IO' },
    { name: 'Brunei Darussalam', code: 'BN' },
    { name: 'Bulgaria', code: 'BG' },
    { name: 'Burkina Faso', code: 'BF' },
    { name: 'Burundi', code: 'BI' },
    { name: 'Cambodia', code: 'KH' },
    { name: 'Cameroon', code: 'CM' },
    { name: 'Canada', code: 'CA' },
    { name: 'Cape Verde', code: 'CV' },
    { name: 'Cayman Islands', code: 'KY' },
    { name: 'Central African Republic', code: 'CF' },
    { name: 'Chad', code: 'TD' },
    { name: 'Chile', code: 'CL' },
    { name: 'China', code: 'CN' },
    { name: 'Christmas Island', code: 'CX' },
    { name: 'Cocos (Keeling) Islands', code: 'CC' },
    { name: 'Colombia', code: 'CO' },
    { name: 'Comoros', code: 'KM' },
    { name: 'Congo', code: 'CG' },
    { name: 'Congo, The Democratic Republic of the', code: 'CD' },
    { name: 'Cook Islands', code: 'CK' },
    { name: 'Costa Rica', code: 'CR' },
    { name: "Cote D'Ivoire", code: 'CI' },
    { name: 'Croatia', code: 'HR' },
    { name: 'Cuba', code: 'CU' },
    { name: 'Cyprus', code: 'CY' },
    { name: 'Czech Republic', code: 'CZ' },
    { name: 'Denmark', code: 'DK' },
    { name: 'Djibouti', code: 'DJ' },
    { name: 'Dominica', code: 'DM' },
    { name: 'Dominican Republic', code: 'DO' },
    { name: 'Ecuador', code: 'EC' },
    { name: 'Egypt', code: 'EG' },
    { name: 'El Salvador', code: 'SV' },
    { name: 'Equatorial Guinea', code: 'GQ' },
    { name: 'Eritrea', code: 'ER' },
    { name: 'Estonia', code: 'EE' },
    { name: 'Ethiopia', code: 'ET' },
    { name: 'Falkland Islands (Malvinas)', code: 'FK' },
    { name: 'Faroe Islands', code: 'FO' },
    { name: 'Fiji', code: 'FJ' },
    { name: 'Finland', code: 'FI' },
    { name: 'France', code: 'FR' },
    { name: 'French Guiana', code: 'GF' },
    { name: 'French Polynesia', code: 'PF' },
    { name: 'French Southern Territories', code: 'TF' },
    { name: 'Gabon', code: 'GA' },
    { name: 'Gambia', code: 'GM' },
    { name: 'Georgia', code: 'GE' },
    { name: 'Germany', code: 'DE' },
    { name: 'Ghana', code: 'GH' },
    { name: 'Gibraltar', code: 'GI' },
    { name: 'Greece', code: 'GR' },
    { name: 'Greenland', code: 'GL' },
    { name: 'Grenada', code: 'GD' },
    { name: 'Guadeloupe', code: 'GP' },
    { name: 'Guam', code: 'GU' },
    { name: 'Guatemala', code: 'GT' },
    { name: 'Guernsey', code: 'GG' },
    { name: 'Guinea', code: 'GN' },
    { name: 'Guinea-Bissau', code: 'GW' },
    { name: 'Guyana', code: 'GY' },
    { name: 'Haiti', code: 'HT' },
    { name: 'Heard Island and McDonald Islands', code: 'HM' },
    { name: 'Holy See (Vatican City State)', code: 'VA' },
    { name: 'Honduras', code: 'HN' },
    { name: 'Hong Kong', code: 'HK' },
    { name: 'Hungary', code: 'HU' },
    { name: 'Iceland', code: 'IS' },
    { name: 'India', code: 'IN' },
    { name: 'Indonesia', code: 'ID' },
    { name: 'Iran, Islamic Republic of', code: 'IR' },
    { name: 'Iraq', code: 'IQ' },
    { name: 'Ireland', code: 'IE' },
    { name: 'Isle of Man', code: 'IM' },
    { name: 'Israel', code: 'IL' },
    { name: 'Italy', code: 'IT' },
    { name: 'Jamaica', code: 'JM' },
    { name: 'Japan', code: 'JP' },
    { name: 'Jersey', code: 'JE' },
    { name: 'Jordan', code: 'JO' },
    { name: 'Kazakhstan', code: 'KZ' },
    { name: 'Kenya', code: 'KE' },
    { name: 'Kiribati', code: 'KI' },
    { name: "Korea, Democratic People's Republic of", code: 'KP' },
    { name: 'Korea, Republic of', code: 'KR' },
    { name: 'Kuwait', code: 'KW' },
    { name: 'Kyrgyzstan', code: 'KG' },
    { name: "Lao People's Democratic Republic", code: 'LA' },
    { name: 'Latvia', code: 'LV' },
    { name: 'Lebanon', code: 'LB' },
    { name: 'Lesotho', code: 'LS' },
    { name: 'Liberia', code: 'LR' },
    { name: 'Libyan Arab Jamahiriya', code: 'LY' },
    { name: 'Liechtenstein', code: 'LI' },
    { name: 'Lithuania', code: 'LT' },
    { name: 'Luxembourg', code: 'LU' },
    { name: 'Macao', code: 'MO' },
    { name: 'Macedonia, The Former Yugoslav Republic of', code: 'MK' },
    { name: 'Madagascar', code: 'MG' },
    { name: 'Malawi', code: 'MW' },
    { name: 'Malaysia', code: 'MY' },
    { name: 'Maldives', code: 'MV' },
    { name: 'Mali', code: 'ML' },
    { name: 'Malta', code: 'MT' },
    { name: 'Marshall Islands', code: 'MH' },
    { name: 'Martinique', code: 'MQ' },
    { name: 'Mauritania', code: 'MR' },
    { name: 'Mauritius', code: 'MU' },
    { name: 'Mayotte', code: 'YT' },
    { name: 'Mexico', code: 'MX' },
    { name: 'Micronesia, Federated States of', code: 'FM' },
    { name: 'Moldova, Republic of', code: 'MD' },
    { name: 'Monaco', code: 'MC' },
    { name: 'Mongolia', code: 'MN' },
    { name: 'Montenegro', code: 'ME' },
    { name: 'Montserrat', code: 'MS' },
    { name: 'Morocco', code: 'MA' },
    { name: 'Mozambique', code: 'MZ' },
    { name: 'Myanmar', code: 'MM' },
    { name: 'Namibia', code: 'NA' },
    { name: 'Nauru', code: 'NR' },
    { name: 'Nepal', code: 'NP' },
    { name: 'Netherlands', code: 'NL' },
    { name: 'Netherlands Antilles', code: 'AN' },
    { name: 'New Caledonia', code: 'NC' },
    { name: 'New Zealand', code: 'NZ' },
    { name: 'Nicaragua', code: 'NI' },
    { name: 'Niger', code: 'NE' },
    { name: 'Nigeria', code: 'NG' },
    { name: 'Niue', code: 'NU' },
    { name: 'Norfolk Island', code: 'NF' },
    { name: 'Northern Mariana Islands', code: 'MP' },
    { name: 'Norway', code: 'NO' },
    { name: 'Oman', code: 'OM' },
    { name: 'Pakistan', code: 'PK' },
    { name: 'Palau', code: 'PW' },
    { name: 'Palestinian Territory, Occupied', code: 'PS' },
    { name: 'Panama', code: 'PA' },
    { name: 'Papua New Guinea', code: 'PG' },
    { name: 'Paraguay', code: 'PY' },
    { name: 'Peru', code: 'PE' },
    { name: 'Philippines', code: 'PH' },
    { name: 'Pitcairn', code: 'PN' },
    { name: 'Poland', code: 'PL' },
    { name: 'Portugal', code: 'PT' },
    { name: 'Puerto Rico', code: 'PR' },
    { name: 'Qatar', code: 'QA' },
    { name: 'Reunion', code: 'RE' },
    { name: 'Romania', code: 'RO' },
    { name: 'Russian Federation', code: 'RU' },
    { name: 'RWANDA', code: 'RW' },
    { name: 'Saint Helena', code: 'SH' },
    { name: 'Saint Kitts and Nevis', code: 'KN' },
    { name: 'Saint Lucia', code: 'LC' },
    { name: 'Saint Pierre and Miquelon', code: 'PM' },
    { name: 'Saint Vincent and the Grenadines', code: 'VC' },
    { name: 'Samoa', code: 'WS' },
    { name: 'San Marino', code: 'SM' },
    { name: 'Sao Tome and Principe', code: 'ST' },
    { name: 'Saudi Arabia', code: 'SA' },
    { name: 'Senegal', code: 'SN' },
    { name: 'Serbia', code: 'RS' },
    { name: 'Seychelles', code: 'SC' },
    { name: 'Sierra Leone', code: 'SL' },
    { name: 'Singapore', code: 'SG' },
    { name: 'Slovakia', code: 'SK' },
    { name: 'Slovenia', code: 'SI' },
    { name: 'Solomon Islands', code: 'SB' },
    { name: 'Somalia', code: 'SO' },
    { name: 'South Africa', code: 'ZA' },
    { name: 'South Georgia and the South Sandwich Islands', code: 'GS' },
    { name: 'Spain', code: 'ES' },
    { name: 'Sri Lanka', code: 'LK' },
    { name: 'Sudan', code: 'SD' },
    { name: 'Suriname', code: 'SR' },
    { name: 'Svalbard and Jan Mayen', code: 'SJ' },
    { name: 'Swaziland', code: 'SZ' },
    { name: 'Sweden', code: 'SE' },
    { name: 'Switzerland', code: 'CH' },
    { name: 'Syrian Arab Republic', code: 'SY' },
    { name: 'Taiwan, Province of China', code: 'TW' },
    { name: 'Tajikistan', code: 'TJ' },
    { name: 'Tanzania, United Republic of', code: 'TZ' },
    { name: 'Thailand', code: 'TH' },
    { name: 'Timor-Leste', code: 'TL' },
    { name: 'Togo', code: 'TG' },
    { name: 'Tokelau', code: 'TK' },
    { name: 'Tonga', code: 'TO' },
    { name: 'Trinidad and Tobago', code: 'TT' },
    { name: 'Tunisia', code: 'TN' },
    { name: 'Turkey', code: 'TR' },
    { name: 'Turkmenistan', code: 'TM' },
    { name: 'Turks and Caicos Islands', code: 'TC' },
    { name: 'Tuvalu', code: 'TV' },
    { name: 'Uganda', code: 'UG' },
    { name: 'Ukraine', code: 'UA' },
    { name: 'United Arab Emirates', code: 'AE' },
    { name: 'United Kingdom', code: 'GB' },
    { name: 'United States', code: 'US' },
    { name: 'United States Minor Outlying Islands', code: 'UM' },
    { name: 'Uruguay', code: 'UY' },
    { name: 'Uzbekistan', code: 'UZ' },
    { name: 'Vanuatu', code: 'VU' },
    { name: 'Venezuela', code: 'VE' },
    { name: 'Viet Nam', code: 'VN' },
    { name: 'Virgin Islands, British', code: 'VG' },
    { name: 'Virgin Islands, U.S.', code: 'VI' },
    { name: 'Wallis and Futuna', code: 'WF' },
    { name: 'Western Sahara', code: 'EH' },
    { name: 'Yemen', code: 'YE' },
    { name: 'Zambia', code: 'ZM' },
    { name: 'Zimbabwe', code: 'ZW' }
];
countryInput.addEventListener('click', () => {
    if (countryInput.options.length === 1) {
        const fragment = document.createDocumentFragment();
        countries.forEach(country => {
            const option = document.createElement('option');
            option.value = country.code;
            option.text = country.name;
            fragment.appendChild(option);
        });
        countryInput.appendChild(fragment);
    }
});

countryInput.addEventListener('change', function() {

    // Check if the selected country is in the list of countries
    if (!checkCountrySelectionInput()) {
        this.classList.add('is-invalid');
    } else {
        if (this.classList.contains('is-invalid')) {
            this.classList.remove('is-invalid');
        }
    }
});

function checkCountrySelectionInput() {
    // Based on the value "country code" & the text "country name"
    let selectedCountry = countryInput.options[countryInput.selectedIndex];

    let isCountryIncluded = countries.some(country => {
        return country.name === selectedCountry.text.trim() && country.code === selectedCountry.value.trim();
    });

    return isCountryIncluded;
};

function checkInputReadiness() {

    console.log('check data readiness.')

    if (nameInput.value !== '') {
        if (!checkInput(nameInput)) {
            console.log('1')
            return false;
        }
    }

    if (surnameInput.value !== '') {
        if (!checkInput(surnameInput)) {
            console.log('1')
            return false;
        }
    }

    if (regionInput.value !== '') {
        if (!checkInput(regionInput)) {
            console.log('1')
            return false;
        }
    }

    if (cityInput.value !== '') {
        if (!checkInput(cityInput)) {
            console.log('1')
            return false;
        }
    }

    if (phoneInput.value !== '') {
        if (!checkNumericalInput(phoneInput) || phoneInput.value.length !== 10) {
            console.log('2')
            return false;
        }
    }

    if (zipInput.value !== '') {
        if (!checkNumericalInput(zipInput) || zipInput.value.length < 5 || zipInput.value.length > 10) {
            console.log('2')
            return false;
        }
    }

    if (addressInput.value !== '') {
        if (!checkAddressInput(addressInput)) {
            console.log('3')
            return false;
        }
    }

    if (genderInput.value !== "Male" && genderInput.value !== "Female" && genderInput.value !== "None") {
        console.log('4')
        return false;
    }

    if (countryInput.options[countryInput.selectedIndex].value !== '') {
        if (!checkCountrySelectionInput()) {
            console.log('5')
            return false;
        }
    }

    if (birthdayInput.value !== '') {
        if (!dateRegex.test(birthdayInput.value)) {
            console.log('6')
            return false;
        }
    }

    console.log(`FILE : ${fileInput.files[0]}, IS UNDEFINED? ${fileInput.files[0] === undefined}`)
    if (fileInput.files[0] !== undefined) {
        console.log(`7777777777 ${fileInput.files[0].size} | ${fileInput.files[0].name} | ${fileInput.files[0].type}`);
        if (!checkProfileImageFile(fileInput.files[0])) {
            console.log('8');
            return false;
        }
    }

    // All passed
    console.log('check data readiness done.')
    return true;
};

function anyNewData() {

    let hasOneValue = false;

    if (nameInput.value !== '') {
        hasOneValue = true;
    }
    if (surnameInput.value !== '') {
        hasOneValue = true;
    }
    if (addressInput.value !== '') {
        hasOneValue = true;
    }
    if (cityInput.value !== '') {
        hasOneValue = true;
    }
    if (regionInput.value !== '') {
        hasOneValue = true;
    }
    if (zipInput.value !== '') {
        hasOneValue = true;
    }
    if (birthdayInput.value !== '') {
        hasOneValue = true;
    }
    if (phoneInput.value !== '') {
        hasOneValue = true;
    }
    if (countryInput.options[countryInput.selectedIndex].value !== '') {
        hasOneValue = true;
    }
    if (genderInput.value !== "None") {
        hasOneValue = true;
    }

    let hasProfilePicture = fileInput.files[0] !== undefined;

    console.log(`has at least one new value? : ${hasOneValue}. has new profile file? ${hasProfilePicture}`);
    return hasOneValue || hasProfilePicture;
};


// Submit update form
updateFormSubmitBtn.addEventListener('click', function (event) {
    event.preventDefault();

   if (anyNewData()) {
       if (checkInputReadiness()) {

           form.submit();
           return;
       }

       handleError("Form contains invalid inputs");
       return
   }

   handleInfoNotificationModal("Nothing new tp update!");
   return;

});
$(document).ready(function() {
    $('#dropdownMenuOffset').on('click', function(e) {
        console.log("FUNCTION CALLED")
        e.preventDefault();
        $(this).next('.dropdown-menu').toggleClass('show');
    });

    $(document).on('click', function(e) {
        if (!$(e.target).closest('.dropdown').length) {
            $('.dropdown-menu').removeClass('show');
        }
    });
});

