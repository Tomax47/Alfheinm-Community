
// ################ HANDLE ACTIONS BUTTONS DROPDOWN ################
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

