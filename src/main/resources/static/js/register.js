document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("registerForm");

    form.addEventListener("submit", async function (e) {
        e.preventDefault();

        const first_name = document.getElementById("first_name").value.trim();
        const last_name = document.getElementById("last_name").value.trim();
        const email = document.getElementById("email").value.trim();
        const password = document.getElementById("password").value;
        const phoneNumber = document.getElementById("phoneNumber").value.trim();

        const requestBody = {
            first_name,
            last_name,
            email,
            password,
            phoneNumber: phoneNumber !== "" ? phoneNumber : null
        };

        try {
            const response = await fetch("https://event-booking-service.onrender.com/api/v1/auth/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(requestBody),
            });

            if (!response.ok) {
                const errorData = await response.json();
                alert("Registration failed: " + (errorData.message || "Unknown error"));
                return;
            }

            alert("Registration successful! You can now log in.");
            window.location.href = "../Home_Page.html";

        } catch (error) {
            console.error("Registration error:", error);
            alert("An error occurred during registration.");
        }
    });
});
