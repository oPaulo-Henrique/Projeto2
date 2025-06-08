document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('contact-form');
    const nameInput = document.getElementById('name');
    const emailInput = document.getElementById('email');
    const phoneInput = document.getElementById('phone');
    const contactIdInput = document.getElementById('contact-id');
    const contactsList = document.getElementById('contacts-list');
    const successMessage = document.getElementById('success-message');

    function getContacts() {
        return JSON.parse(localStorage.getItem('contacts') || '[]');
    }

    function saveContacts(contacts) {
        localStorage.setItem('contacts', JSON.stringify(contacts));
    }

    function renderContacts() {
        if (!contactsList) return;
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
        const contacts = getContacts();
        const contact = contacts[index];

        localStorage.setItem('editIndex', index);
        window.location.href = 'index.html';
    };

    window.deleteContact = function(index) {
        const contacts = getContacts();
        contacts.splice(index, 1);
        saveContacts(contacts);
        alert('Contato removido com sucesso!');
        location.reload();
    };

    // 🟨 Ao carregar a página de cadastro, verificar se está em modo de edição
    const editIndex = localStorage.getItem('editIndex');
    if (editIndex !== null) {
        const contacts = getContacts();
        const contact = contacts[editIndex];

        nameInput.value = contact.name;
        emailInput.value = contact.email;
        phoneInput.value = contact.phone;
        contactIdInput.value = editIndex;

        localStorage.removeItem('editIndex'); // limpar para evitar edições indesejadas
    }

    if (form) {
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
            form.reset();
            if (successMessage) {
                successMessage.textContent = 'Contato salvo com sucesso!';
                setTimeout(() => successMessage.textContent = '', 3000);
            }
        });
    }

    renderContacts();
});
