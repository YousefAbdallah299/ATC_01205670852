let currentPage = 0;
const pageSize = 5;
const sortBy = "bookingId";

const userToken = localStorage.getItem("token");

async function fetchBookings(pageNo = 0) {
    if (!userToken) {
        document.getElementById('activetable').innerHTML = '<p>Please sign in to see your bookings.</p>';
        document.getElementById('prevPage').disabled = true;
        document.getElementById('nextPage').disabled = true;
        return;
    }

    const params = new URLSearchParams({
        pageNo: pageNo,
        pageSize: pageSize,
        sortBy: sortBy
    });

    try {
        const response = await fetch(`http://localhost:8080/api/v1/booking/all?${params.toString()}`, {
            headers: {
                'Authorization': `Bearer ${userToken}`,
                'Content-Type': 'application/json'
            }
        });

        if (response.status === 401) {
            alert('Session expired or unauthorized. Please sign in again.');
            localStorage.removeItem('token');
            window.location.href = 'Sign_In.html';
            return;
        }

        if (!response.ok) throw new Error('Failed to fetch bookings');

        const data = await response.json();

        const bookings = data.bookings;
        if (!bookings || bookings.length === 0) {
            document.getElementById('activetable').innerHTML = '<p>No bookings found.</p>';
            document.getElementById('prevPage').disabled = true;
            document.getElementById('nextPage').disabled = true;
            return;
        }

        const html = bookings.map(booking => {
            return `
                <div class="booking-card">
                    <h3>${booking.eventName}</h3>
                    <p><strong>Venue:</strong> ${booking.venueName}</p>
                    <p><strong>Price:</strong> ${booking.price.toFixed(2)} EGP</p>
                    <p><strong>Booked On:</strong> ${formatDateTime(booking.bookingDatetime)}</p>
                    <p><strong>Tickets:</strong> ${booking.numOfTickets}</p>
                    <button class="cancel-btn" data-booking-id="${booking.bookingId}">
                        Cancel Ticket
                    </button>
                </div>
            `;
        }).join('');

        document.getElementById('activetable').innerHTML = html;

        document.getElementById('prevPage').disabled = pageNo === 0;
        document.getElementById('nextPage').disabled = bookings.length < pageSize;

        document.querySelectorAll('.cancel-btn').forEach(button => {
            button.addEventListener('click', () => {
                const bookingId = +button.getAttribute('data-booking-id');
                cancelTicket(bookingId, button);
            });
        });

        currentPage = pageNo;

    } catch (error) {
        console.error('Error fetching bookings:', error);
        document.getElementById('activetable').innerHTML = '<p>Error loading bookings.</p>';
        document.getElementById('prevPage').disabled = true;
        document.getElementById('nextPage').disabled = true;
    }
}

async function cancelTicket(bookingId, button) {
    if (!userToken) {
        alert('Please sign in to cancel bookings.');
        window.location.href = 'Sign_In.html';
        return;
    }

    button.disabled = true;
    button.textContent = 'Cancelling...';

    try {
        const response = await fetch(`http://localhost:8080/api/v1/booking/cancel/${bookingId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${userToken}`,
                'Content-Type': 'application/json'
            }
        });

        if (response.status === 401) {
            alert('Session expired or unauthorized. Please sign in again.');
            localStorage.removeItem('token');
            window.location.href = 'Sign_In.html';
            return;
        }

        if (!response.ok) {
            const errData = await response.json();
            alert(errData.message || 'Failed to cancel booking.');
            button.disabled = false;
            button.textContent = 'Cancel Ticket';
            return;
        }

        alert('Booking cancelled successfully.');
        fetchBookings(currentPage);

    } catch (error) {
        console.error('Error cancelling booking:', error);
        alert('Failed to cancel booking.');
        button.disabled = false;
        button.textContent = 'Cancel Ticket';
    }
}

function formatDateTime(dateTimeString) {
    if (!dateTimeString) return '';
    const date = new Date(dateTimeString);
    return date.toLocaleString('en-GB');
}

window.onload = () => fetchBookings(currentPage);

document.getElementById('nextPage').addEventListener('click', () => {
    currentPage++;
    fetchBookings(currentPage);
});

document.getElementById('prevPage').addEventListener('click', () => {
    if (currentPage > 0) {
        currentPage--;
        fetchBookings(currentPage);
    }
});
