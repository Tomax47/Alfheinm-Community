
// ################ HANDLE THE NAVBAR TOGGLER FUNCTIONALITY ################
const togglerButton = document.querySelector('.navbar-toggler');
const navItemsContainer = document.querySelector('#navbar-default-primary');

// Add a click event listener to the toggler button
togglerButton.addEventListener('click', () => {
    // Toggle the 'show' class on the nav-items container
    navItemsContainer.classList.toggle('show');
});

// NEWSLETTER PART
const phoneNumberInput = document.getElementById('newsletterPhoneNumber');
const submitBtn = document.getElementById('subscribeBtn');

var numbersRegex = /^[0-9]+$/;

function CheckNumberFormat(number) {

    console.log(`PHONE : ${phoneNumberInput.value.startsWith("7")}`)
    if (numbersRegex.test(number.value) && number.value.length === 11 && phoneNumberInput.value.startsWith("7")) {
        return true;
    }

    return false;
};

phoneNumberInput.addEventListener('input', function () {

    if (CheckNumberFormat(phoneNumberInput)) {
        if (phoneNumberInput.classList.contains('is-invalid')) {
            phoneNumberInput.classList.remove('is-invalid');
        }

        phoneNumberInput.classList.add('is-valid');
        submitBtn.disabled = false;
        return;
    }

    phoneNumberInput.classList.add('is-invalid');
    submitBtn.disabled = true;
});

submitBtn.addEventListener('click', function () {

    if (CheckNumberFormat(phoneNumberInput)) {
        console.log("SUCCESS");

        let url = "/newsletter/subscribe?phoneNumber=" + phoneNumberInput.value;

        $.ajax({
            type: "POST",
            url: url,
            success: function(response) {
                console.log(`Response : ${response}`);

                // Throwing the success modal
                handleSuccessModal({
                    title: "Success!",
                    text: "You have successfully subscribed to the newsletter."
                });

                phoneNumberInput.value = '';
                submitBtn.disabled = true;
                submitBtn.value = "Subscribed";
                return;
            },
            error: function(response) {

                console.log(`ERROR MSG : ${response.responseJSON.message}`)
                handleError(response.responseJSON.message);
            },
            dataType: "json",
            contentType: "application/json"
        });
        return;
    }

    console.log("FAIL");
});
