document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('contact-form');
    const nameInput = document.getElementById('name');
    const emailInput = document.getElementById('email');
    const phoneInput = document.getElementById('phone');
    const contactIdInput = document.getElementById('contact-id');
    const contactsList = document.getElementById('contacts-list');

    function getContacts() {
        return JSON.parse(localStorage.getItem('contacts') || '[]');
    }

    function saveContacts(contacts) {
        localStorage.setItem('contacts', JSON.stringify(contacts));
    }

    function renderContacts() {
        const contacts = getContacts();
        contactsList.innerHTML = '';
        contacts.forEach((contact, index) => {
            const div = document.createElement('div');
            div.className = 'contact';
            div.innerHTML = `
                <strong>${contact.name}</strong><br>
                <small>${contact.email}</small><br>
                <small>${contact.phone}</small>
                <div class="contact-buttons">
                    <button onclick="editContact(${index})">Editar</button>
                    <button onclick="deleteContact(${index})">Remover</button>
                </div>
            `;
            contactsList.appendChild(div);
        });
    }

    window.editContact = function(index) {
        const contact = getContacts()[index];
        nameInput.value = contact.name;
        emailInput.value = contact.email;
        phoneInput.value = contact.phone;
        contactIdInput.value = index;
    };

    window.deleteContact = function(index) {
        const contacts = getContacts();
        contacts.splice(index, 1);
        saveContacts(contacts);
        renderContacts();
    };

    form.addEventListener('submit', (e) => {
        e.preventDefault();
        const name = nameInput.value.trim();
        const email = emailInput.value.trim();
        const phone = phoneInput.value.trim();
        if (!name || !email || !phone) {
            alert('Preencha todos os campos!');
            return;
        }
        const contacts = getContacts();
        const id = contactIdInput.value;
        if (id) {
            contacts[id] = { name, email, phone };
        } else {
            contacts.push({ name, email, phone });
        }
        saveContacts(contacts);
        renderContacts();
        form.reset();
    });

    renderContacts();
});
