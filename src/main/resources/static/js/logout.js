document.addEventListener("DOMContentLoaded", function () {
    const logoutBtn = document.getElementById("logoutBtn");

    if (logoutBtn) {
        logoutBtn.addEventListener("click", async function () {
            const token = localStorage.getItem("token");

            if (!token) {
                alert("You are not logged in.");
                redirectToLogin();
                return;
            }

            try {
                const response = await fetch("http://localhost:8080/api/v1/auth/logout", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: "Bearer " + token,
                    },
                });

                if (!response.ok) {
                    const errorData = await response.json();
                    alert("Logout failed: " + (errorData.message || "Unknown error"));
                    return;
                }

                alert("Logged out successfully!");

                localStorage.removeItem("token");
                localStorage.removeItem("tokenExpiry");

                redirectToLogin();

            } catch (error) {
                console.error("Logout error:", error);
                alert("An error occurred while logging out.");
            }
        });
    }
});

function redirectToLogin() {
    window.location.href = "../Sign_In.html";
}
