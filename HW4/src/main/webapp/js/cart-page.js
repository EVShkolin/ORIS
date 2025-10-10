document.addEventListener("DOMContentLoaded", loadCartItems);

async function loadCartItems() {
    const response = await fetch('http://localhost:8090/HW4/api/cart');
    const cartItems = await response.json();
    displayCartItems(cartItems);
}

function displayCartItems(cartItems) {
    const container = document.getElementById('cart-items');

    if (cartItems.length === 0) {
        container.innerHTML = '<div>Товары не найдены</div>';
        return;
    }

    container.innerHTML = cartItems.map(item => `
        <div class="product-card" id="${item.id}">
            <h3>${item.name} x ${item.quantity}</h3>
            <img class="product-image" src="${item.imageUrl}" alt="none"/>
            <h3>${item.price}</h3>
        </div>
    `).join('');
}

document.addEventListener("click", function (e) {
    const response = fetch("http://localhost:8090/HW4/api/order")
});