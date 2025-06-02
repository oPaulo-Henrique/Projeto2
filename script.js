document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('task-form');
    const titleInput = document.getElementById('title');
    const descriptionInput = document.getElementById('description');
    const taskIdInput = document.getElementById('task-id');
    const tasksList = document.getElementById('tasks-list');

    function getTasks() {
        return JSON.parse(localStorage.getItem('tasks') || '[]');
    }

    function saveTasks(tasks) {
        localStorage.setItem('tasks', JSON.stringify(tasks));
    }

    function renderTasks() {
        const tasks = getTasks();
        tasksList.innerHTML = '';
        tasks.forEach((task, index) => {
            const div = document.createElement('div');
            div.className = 'task';
            div.innerHTML = `
                <strong>${task.title}</strong>
                <p>${task.description}</p>
                <div class="task-buttons">
                    <button onclick="editTask(${index})">Editar</button>
                    <button onclick="deleteTask(${index})">Remover</button>
                </div>
            `;
            tasksList.appendChild(div);
        });
    }

    window.editTask = function(index) {
        const task = getTasks()[index];
        titleInput.value = task.title;
        descriptionInput.value = task.description;
        taskIdInput.value = index;
    };

    window.deleteTask = function(index) {
        const tasks = getTasks();
        tasks.splice(index, 1);
        saveTasks(tasks);
        renderTasks();
    };

    form.addEventListener('submit', (e) => {
        e.preventDefault();
        const title = titleInput.value.trim();
        const description = descriptionInput.value.trim();
        if (!title || !description) {
            alert('Preencha todos os campos!');
            return;
        }
        const tasks = getTasks();
        const id = taskIdInput.value;
        if (id) {
            tasks[id] = { title, description };
        } else {
            tasks.push({ title, description });
        }
        saveTasks(tasks);
        renderTasks();
        form.reset();
    });

    renderTasks();
});
