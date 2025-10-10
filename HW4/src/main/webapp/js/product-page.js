document.addEventListener("DOMContentLoaded", loadProducts);

async function loadProducts() {
    const response = await fetch('http://localhost:8090/HW4/api/products');
    const products = await response.json();
    displayProducts(products);
}

function displayProducts(products) {
    const container = document.getElementById('products');

    if (products.length === 0) {
        container.innerHTML = '<div>Товары не найдены</div>';
        return;
    }

    container.innerHTML = products.map(product => `
        <div class="product-card" id="${product.id}">
            <h3>${product.name}</h3>
            <img class="product-image" src="${product.imageUrl}" alt="none"/>
            <h3>${product.price}</h3>
            <button class="buy-button" id="${product.id}-btn">В корзину</button>
        </div>
    `).join('');
}

document.addEventListener("click", function (e) {
    const containerId = e.target.id.replace('-btn', '')
    const productCard = document.getElementById(containerId);
    const productData = {
        id: productCard.id,
        name: productCard.querySelectorAll('h3')[0].textContent,
        imageUrl: productCard.querySelector('.product-image').src,
        price: productCard.querySelectorAll('h3')[1].textContent
    };
    console.log(productData)
    fetch('http://localhost:8090/HW4/api/cart', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(productData),
        credentials: 'include'
    });
});