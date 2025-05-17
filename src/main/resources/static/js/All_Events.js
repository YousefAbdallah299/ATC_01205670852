let currentPage = 0;
const pageSize = 5;
const sortBy = "id";

const userToken = localStorage.getItem("token");

let bookedEventIds = new Set();

async function fetchBookedEvents() {
    if (!userToken) {
        bookedEventIds = new Set();
        return;
    }
    try {
        const response = await fetch('http://localhost:8080/api/v1/booking/all?pageNo=0&pageSize=100', {
            headers: {
                'Authorization': `Bearer ${userToken}`,
                'Content-Type': 'application/json'
            }
        });
        if (!response.ok) throw new Error('Failed to fetch bookings');
        const data = await response.json();
        bookedEventIds = new Set(data.bookings.map(b => b.event.id));
    } catch (error) {
        console.error('Error fetching booked events:', error);
        bookedEventIds = new Set();
    }
}

async function bookEvent(eventId, button) {
    console.log("Token:", userToken);

    if (!userToken) {
        alert("Please sign in to book events.");
        return;
    }
    button.disabled = true;
    button.textContent = "Booking...";
    try {
        const response = await fetch(`http://localhost:8080/api/v1/booking/book/${eventId}`, {
            method: "POST",
            headers: {
                'Authorization': `Bearer ${userToken}`,
                'Content-Type': 'application/json'
            }
        });
        if (!response.ok) {
            const errData = await response.json();
            alert(errData.message || "Failed to book event.");
            button.disabled = false;
            button.textContent = "Book Now";
            return;
        }

        window.location.href = "../html/congrats.html";

    } catch (error) {
        console.error("Error booking event:", error);
        alert("Please sign in to book events.");
        button.disabled = false;
        button.textContent = "Book Now";
    }
}

async function fetchAndDisplayEvents(pageNo = 0) {
    currentPage = pageNo;

    await fetchBookedEvents();

    const params = new URLSearchParams({
        pageNo: currentPage,
        pageSize: pageSize+1,
        sortBy: sortBy
    });

    fetch(`http://localhost:8080/api/v1/event/all?${params.toString()}`)
        .then(response => {
            if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);
            return response.json();
        })
        .then(data => {
            const events = data.events;
            if (!events || events.length === 0) {
                document.getElementById('activetable').innerHTML = "<p>No events found.</p>";
                document.getElementById('prevPage').disabled = true;
                document.getElementById('nextPage').disabled = true;
                return;
            }

            const html = events.map(event => {
                return `
                    <div class="event-card">
                        <img class="event-image" src="${event.coverImageUrl ? event.coverImageUrl : 'https://via.placeholder.com/120x90?text=No+Image'}" alt="Event Cover" />
                        <div class="event-info">
                            <h3>${event.title}</h3>
                            <p>${event.description}</p>
                            <p><strong>Start:</strong> ${formatDateTime(event.startDatetime)}</p>
                            <p><strong>End:</strong> ${formatDateTime(event.endDatetime)}</p>
                            <p><strong>Venue:</strong> ${event.venue}</p>
                            <p><strong>Price:</strong> ${event.ticketPrice.toFixed(2)} EGP</p>
                            <p><strong>Status:</strong> ${event.status}</p>
                            <p><strong>Category:</strong> ${event.category}</p>
                            <p><strong>Tags:</strong> ${event.tags.join(", ")}</p>
                        </div>
                        <button class="book-btn book-now" data-event-id="${event.eventId}">
                            Book Now
                        </button>
                    </div>
                `;
            }).join("");

            document.getElementById('activetable').innerHTML = html;

            document.getElementById('prevPage').disabled = currentPage === 0;
            document.getElementById('nextPage').disabled = events.length < pageSize;

            // Add event listeners to buttons
            document.querySelectorAll(".book-btn.book-now").forEach(button => {
                button.addEventListener("click", () => {
                    const eventId = +button.getAttribute("data-event-id");
                    bookEvent(eventId, button);
                });
            });
        })
        .catch(err => {
            console.error("Error fetching events:", err);
            document.getElementById('activetable').innerHTML = "<p>Error loading events.</p>";
            document.getElementById('prevPage').disabled = true;
            document.getElementById('nextPage').disabled = true;
        });
}

function formatDateTime(dateTimeString) {
    if (!dateTimeString) return "";
    const date = new Date(dateTimeString);
    return date.toLocaleString('en-GB');
}

// Initial load
window.onload = () => fetchAndDisplayEvents(currentPage);

// Pagination controls
document.getElementById('nextPage').addEventListener('click', () => {
    currentPage++;
    fetchAndDisplayEvents(currentPage);
});

document.getElementById('prevPage').addEventListener('click', () => {
    if (currentPage > 0) {
        currentPage--;
        fetchAndDisplayEvents(currentPage);
    }
});
