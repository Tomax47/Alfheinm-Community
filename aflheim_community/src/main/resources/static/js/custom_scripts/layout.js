
// ################ HANDLE THE NAVBAR TOGGLER FUNCTIONALITY ################
const togglerButton = document.querySelector('.navbar-toggler');
const navItemsContainer = document.querySelector('#navbar-default-primary');

// Add a click event listener to the toggler button
togglerButton.addEventListener('click', () => {
    // Toggle the 'show' class on the nav-items container
    navItemsContainer.classList.toggle('show');
});