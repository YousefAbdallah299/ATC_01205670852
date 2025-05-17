function checkTokenValidity() {
    const token = localStorage.getItem("token");
    const expiry = localStorage.getItem("tokenExpiry");

    // No token or expiry stored
    if (!token || !expiry) {
        redirectToLogin();
        return;
    }

    // Check if token expired
    if (Date.now() > Number(expiry)) {
        alert("Session expired. Please log in again.");
        localStorage.removeItem("token");
        localStorage.removeItem("tokenExpiry");
        redirectToLogin();
    }
}

// Redirect to login page
function redirectToLogin() {
    window.location.href = "../Sign_In.html";
}

