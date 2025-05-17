window.onload = () => {
    const userToken = localStorage.getItem('token');
    const signInBtn = document.getElementById('btn-signin');
    const logoutBtn = document.getElementById('btn-logout');

    if (userToken) {
        signInBtn.style.display = 'none';
        logoutBtn.style.display = 'inline-block';
    } else {
        signInBtn.style.display = 'inline-block';
        logoutBtn.style.display = 'none';
    }

    logoutBtn.addEventListener('click', () => {
        if (userToken) {
            fetch('https://event-booking-service.onrender.com/api/v1/auth/logout', {
                method: 'POST',
                headers: {
                    'Authorization': 'Bearer ' + userToken,
                    'Content-Type': 'application/json'
                }
            })
                .then(response => {
                    if (response.ok) {
                        localStorage.removeItem('token');
                        alert('Logged out successfully.');
                        window.location.reload();
                    } else {
                        alert('Failed to logout from server.');
                    }
                })
                .catch(error => {
                    console.error('Logout error:', error);
                    alert('Error occurred during logout.');
                });
        } else {
            localStorage.removeItem('token');
            window.location.reload();
        }
    });
};
