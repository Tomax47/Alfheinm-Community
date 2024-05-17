
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
const cardNumberInput = document.getElementById('cardNumberLabel');
const cardExpDateInput = document.getElementById('cardExpiryLabel');
const cardCVCInput = document.getElementById('cardCVCLabel');
const cardNameInput = document.getElementById('cardNameLabel');
const submitPaymentBtn = document.getElementById('submitPaymentBtn');
const amountInput = document.getElementById('amountInputLabel');

if (amountInput != null) {
    amountInput.addEventListener('input', function() {
        // Remove any non-numeric characters and leading zeros
        this.value = this.value.replace(/[^0-9.]/g, '').replace(/^0+/, '');

        // Convert the value to a double
        let amount = parseFloat(this.value);

        // Check if the value is a valid number and above 0
        if (isNaN(amount) || amount <= 0) {
            this.value = '';
        }
    });
}
function SubmitPayment() {

    let url;

    if (document.getElementById('memberTier') != null) {
        url = "/stripe/checkout/memberTier/charge";
    } else if (document.getElementById('supportTier') != null) {

        parseFloat(amountInput.value);
        url = "/stripe/checkout/support/charge?amount="+ amountInput.value;
    }

    console.log("Submitting payment to " + url);

    let formData = {
        "cardNumber": cardNumberInput.value,
        "expMonth": cardExpDateInput.value.slice(0, 2),
        "expYear": cardExpDateInput.value.slice(3),
        "cvc": cardCVCInput.value,
        "name": cardNameInput.value
    };

    console.log(formData);

    $.ajax({
        type: "POST",
        url: url,
        data: JSON.stringify(formData),

        success: function(response) {

            window.location.replace("/payment/success?chargeId="+response.chargeId);

        },
        error: function(response) {

            const errorData = response.responseJSON.message;
            handleError(errorData);
        },
        dataType: "json",
        contentType: "application/json"
    });
}

submitPaymentBtn.addEventListener('click', function () {

    console.log("CLICKED");
    SubmitPayment();
});

cardExpDateInput.addEventListener('input', function() {
    let inputValue = this.value.replace(/[^0-9\/]/g, ''); // Remove non-digit characters

    if (inputValue.length === 2) {
        if (inputValue > 12) {
            inputValue = '';
        }
    }

    if (inputValue.length > 5) {
        // Limiting input to 5 characters
        inputValue = inputValue.slice(0, 5);
    }

    if (inputValue.length === 4 && inputValue.indexOf("/") === -1) {
        inputValue = `${inputValue.slice(0, 2)}/${inputValue.slice(2)}`;
    }

    this.value = inputValue;

    // Separate month and year
    const month = inputValue.slice(0, 2);
    const year = inputValue.slice(3);

    console.log(`Month: ${month}, Year: ${year}`);
});