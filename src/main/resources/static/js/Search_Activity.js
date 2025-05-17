const createTableRow = (event) => {
    const start = new Date(event.startDatetime).toLocaleString();
    const end = new Date(event.endDatetime).toLocaleString();

    const cover = event.coverImageUrl
        ? `<img src="${event.coverImageUrl}" alt="Cover Image" style="max-width: 100px; max-height: 60px;">`
        : 'No Image';

    const tags = event.tags && event.tags.length > 0 ? event.tags.join(', ') : '-';

    return `
        <tr>
            <td>${event.title}</td>
            <td>${event.description}</td>
            <td>${start}</td>
            <td>${end}</td>
            <td>$${event.ticketPrice.toFixed(2)}</td>
            <td>${event.status}</td>
            <td>${event.category}</td>
            <td>${cover}</td>
            <td>${tags}</td>
            <td><button onclick="bookTicket('${event.eventId}')">Book Ticket</button></td>
        </tr>
    `;
};

async function search() {
    const title = document.getElementById('title').value.trim();
    if (!title) {
        alert('Please enter a title to search.');
        return;
    }

    document.getElementById('results').innerHTML = '';

    try {
        const response = await fetch(`http://localhost:8080/api/v1/event/name/${encodeURIComponent(title)}`);

        if (!response.ok) {
            if (response.status === 404) {
                alert('No event found with this title.');
                return;
            } else {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
        }

        const event = await response.json();

        const html = createTableRow(event);
        document.getElementById('results').innerHTML = html;

    } catch (error) {
        alert('Error fetching event: ' + error.message);
        console.error('Fetch error:', error);
    }
}

async function bookTicket(eventId) {
    const token = localStorage.getItem('token'); // adjust based on your token storage

    if (!token) {
        alert('You must be logged in to book tickets.');
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/api/v1/booking/book/${eventId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify({ eventId })
        });

        if (!response.ok) {
            throw new Error(`Booking failed with status: ${response.status}`);
        }

        alert('Ticket booked successfully!');
    } catch (error) {
        alert('Please Sign In to book tickets.');
        console.error('Booking error:', error);
    }
}
