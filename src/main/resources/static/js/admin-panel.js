const API_BASE_URL = "http://localhost:8080/api/v1/event";

function getToken() {
    return localStorage.getItem("token") || "";
}

function parseDateTimeLocal(dateTimeLocal) {
    return dateTimeLocal ? new Date(dateTimeLocal).toISOString().slice(0, 19) : null;
}

async function loadEvents() {
    try {
        const res = await fetch(API_BASE_URL + '/all?pageNo=0&pageSize=100&sortBy=id');
        if (!res.ok) throw new Error('Failed to fetch events');
        const data = await res.json();
        const events = data.events || [];
        const container = document.getElementById('events-list');
        container.innerHTML = '';
        if (events.length === 0) {
            container.innerHTML = '<p>No events found.</p>';
            return;
        }
        for (const event of events) {
            const card = document.createElement('div');
            card.className = 'event-card';

            // Image or placeholder
            const img = document.createElement('img');
            img.className = 'event-image';
            img.src = event.coverImageUrl || 'https://via.placeholder.com/120x90?text=No+Image';
            img.alt = event.title;
            card.appendChild(img);

            // Info section
            const info = document.createElement('div');
            info.className = 'event-info';

            const title = document.createElement('h3');
            title.textContent = event.title;
            info.appendChild(title);

            const desc = document.createElement('p');
            desc.textContent = event.description || 'No description';
            info.appendChild(desc);

            const category = document.createElement('p');
            category.textContent = `Category: ${event.category || 'N/A'}`;
            info.appendChild(category);

            const dates = document.createElement('p');
            dates.textContent = `From: ${new Date(event.startDatetime).toLocaleString()} To: ${new Date(event.endDatetime).toLocaleString()}`;
            info.appendChild(dates);

            const venue = document.createElement('p');
            venue.textContent = `Venue: ${event.venue || 'N/A'}`;
            info.appendChild(venue);

            card.appendChild(info);

            // Delete button
            const delBtn = document.createElement('button');
            delBtn.className = 'book-btn book-now';
            delBtn.textContent = 'Delete';
            delBtn.onclick = () => {
                if (confirm(`Are you sure you want to delete event "${event.title}" (ID: ${event.eventId})?`)) {
                    deleteEvent(event.eventId);
                }
            };
            card.appendChild(delBtn);

            // Cancel button
            const cancelBtn = document.createElement('button');
            cancelBtn.className = 'book-btn book-now';
            cancelBtn.textContent = 'Cancel';
            cancelBtn.style.marginLeft = '8px';
            cancelBtn.onclick = () => {
                if (confirm(`Are you sure you want to cancel event "${event.title}" (ID: ${event.eventId})?`)) {
                    cancelEvent(event.eventId);
                }
            };
            card.appendChild(cancelBtn);

            container.appendChild(card);
        }
    } catch (err) {
        alert('Failed to load events: ' + err.message);
    }
}

document.getElementById('create-event-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const form = e.target;
    const token = getToken();
    if (!token) {
        alert("You must be logged in.");
        return;
    }
    const tags = form.tags.value.split(',').map(t => t.trim()).filter(t => t !== "");
    const body = {
        title: form.title.value.trim(),
        description: form.description.value.trim(),
        startDate: parseDateTimeLocal(form.startDate.value),
        endDate: parseDateTimeLocal(form.endDate.value),
        categoryName: form.categoryName.value.trim(),
        totalTickets: parseInt(form.totalTickets.value),
        ticketPrice: parseFloat(form.ticketPrice.value),
        tags: tags.length > 0 ? [...new Set(tags)] : [],
        imageUrl: form.imageUrl.value.trim() || null,
        venueName: form.venueName.value.trim(),
    };
    try {
        const res = await fetch(API_BASE_URL + "/add", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token,
            },
            body: JSON.stringify(body),
        });
        if (!res.ok) {
            const errData = await res.json();
            throw new Error(JSON.stringify(errData));
        }
        alert("Event created successfully");
        form.reset();
        loadEvents();
    } catch (err) {
        alert("Failed to create event: " + err.message);
    }
});

document.getElementById('update-event-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const form = e.target;
    const token = getToken();
    if (!token) {
        alert("You must be logged in.");
        return;
    }
    const eventId = parseInt(form.eventId.value);
    if (isNaN(eventId)) {
        alert("Event ID must be a number.");
        return;
    }
    // Only include fields that have non-empty values
    const body = {};
    if (form.title.value.trim() !== "") body.title = form.title.value.trim();
    if (form.description.value.trim() !== "") body.description = form.description.value.trim();
    if (form.startDate.value !== "") body.startDate = parseDateTimeLocal(form.startDate.value);
    if (form.endDate.value !== "") body.endDate = parseDateTimeLocal(form.endDate.value);
    if (form.categoryName.value.trim() !== "") body.categoryName = form.categoryName.value.trim();
    if (form.totalTickets.value !== "") body.totalTickets = parseInt(form.totalTickets.value);
    if (form.ticketPrice.value !== "") body.ticketPrice = parseFloat(form.ticketPrice.value);
    if (form.tags.value.trim() !== "") {
        const tags = form.tags.value.split(',').map(t => t.trim()).filter(t => t !== "");
        if (tags.length > 0) body.tags = [...new Set(tags)];
    }
    if (form.imageUrl.value.trim() !== "") body.imageUrl = form.imageUrl.value.trim();
    if (form.venueName.value.trim() !== "") body.venueName = form.venueName.value.trim();

    try {
        const res = await fetch(`${API_BASE_URL}/update/${eventId}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token,
            },
            body: JSON.stringify(body),
        });
        if (!res.ok) {
            const errData = await res.json();
            throw new Error(JSON.stringify(errData));
        }
        alert("Event updated successfully");
        form.reset();
        loadEvents();
    } catch (err) {
        alert("Failed to update event: " + err.message);
    }
});

async function deleteEvent(eventId) {
    const token = getToken();
    if (!token) {
        alert("You must be logged in.");
        return;
    }
    try {
        const res = await fetch(`${API_BASE_URL}/delete/${eventId}`, {
            method: "DELETE",
            headers: {
                "Authorization": "Bearer " + token,
            },
        });
        if (!res.ok) {
            const errData = await res.json();
            throw new Error(JSON.stringify(errData));
        }
        alert("Event deleted successfully");
        loadEvents();
    } catch (err) {
        alert("Delete event failed: " + err.message);
    }
}

async function cancelEvent(eventId) {
    const token = getToken();
    if (!token) {
        alert("You must be logged in.");
        return;
    }
    try {
        const res = await fetch(`${API_BASE_URL}/cancel/${eventId}`, {
            method: "PUT",
            headers: {
                "Authorization": "Bearer " + token,
            },
        });
        if (!res.ok) {
            const errData = await res.json();
            throw new Error(JSON.stringify(errData));
        }
        alert("Event canceled successfully");
        loadEvents();
    } catch (err) {
        alert("Cancel event failed: " + err.message);
    }
}

window.addEventListener('load', loadEvents);
