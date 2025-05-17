document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById("loginForm");

    loginForm.addEventListener("submit", async function (event) {
        event.preventDefault();

        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        const requestBody = { email, password };

        try {
            const response = await fetch("http://localhost:8080/api/v1/auth/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(requestBody),
            });

            if (!response.ok) {
                const errorData = await response.json();
                alert("Login failed: " + (errorData.message || "Unknown error"));
                return;
            }

            const data = await response.json();

            // Save token and expiry time (current time + 30 minutes)
            const expiryTime = Date.now() + 30 * 60 * 1000; // 30 mins in ms
            localStorage.setItem("token", data.token);
            localStorage.setItem("tokenExpiry", expiryTime);

            alert("Login successful!");

            if (data.role === "ADMIN") {
                window.location.href = "Admin_Panel.html";
            } else {
                window.location.href = "../Home_Page.html";
            }

        } catch (error) {
            console.error("Login error:", error);
            alert("An error occurred while logging in.");
        }
    });
});
