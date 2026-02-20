const contextPath = 'http://localhost:8090/HW2.1';
const users = document.querySelector('.user-list');
document.addEventListener('DOMContentLoaded', getAllUsers());
users.addEventListener('keydown', e => changeUsername(e));
users.addEventListener('click', e => deleteUser(e));

async function getAllUsers() {
    const response = await fetch(`${contextPath}/api/v1/users`);
    const userList = await response.json();
    userList.forEach(u => addUserToList(u));
}

function addUserToList(user) {
    const li = document.createElement('li');
    li.id = user.id;
    li.innerHTML = `
        <div class="user" style="display: flex; align-items: center; gap: 20px">
          <h3>${user.id}: ${user.name}</h3>
          <input class="rename-input" type="text" placeholder="Change name">
          <button class="delete-button" style="background: red">x</button>
        </div>
    `;
    users.appendChild(li);
}

document.querySelector('.user-form').addEventListener('submit', async function (e) {
    e.preventDefault();
    const formData = new FormData(this);
    const data = {
        name: formData.get('user-name')
    };

    const response = await fetch(`${contextPath}/api/v1/users`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
    const newUser = await response.json();
    addUserToList(newUser);
    this.reset();
});


async function changeUsername(event) {
    const eventTarget = event.target;
    if (event.key === 'Enter' && eventTarget.classList.contains('rename-input')) {
        const li = eventTarget.closest('li');
        const userId = li.id;
        const userToUpdate = {
            id: li.id,
            name: eventTarget.value
        };

        const response = await fetch(`${contextPath}/api/v1/users/${userId}`, {
           method: 'PUT',
           headers: {
               'Content-Type': 'application/json'
           },
            body: JSON.stringify(userToUpdate)
        });
        const updatedUser = await response.json();

        const h3 = li.querySelector('h3');
        h3.textContent = `${updatedUser.id}: ${updatedUser.name}`;
        eventTarget.value = '';
    }
}

async function deleteUser(event) {
    const  eventTarget = event.target;
    if (eventTarget.classList.contains('delete-button')) {
        const li = eventTarget.closest('li');
        await fetch(`${contextPath}/api/v1/users/${li.id}`, {
            method: 'DELETE'
        });
        li.remove();
    }
}