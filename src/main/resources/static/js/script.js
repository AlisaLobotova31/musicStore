let allProducts = [];

async function fetchProducts() {
    try {
        const response = await fetch('http://localhost:8080/api/public/products');
        allProducts = await response.json();
        displayProducts(allProducts);
    } catch (error) {
        console.error('Error fetching products:', error);
    }
}

async function fetchCategories() {
    try {
        const response = await fetch('http://localhost:8080/api/public/products/categories');
        const categories = await response.json();
        const categoryFilter = document.getElementById('category-filter');
        if (categoryFilter) {
            categories.forEach(category => {
                const option = document.createElement('option');
                option.value = category;
                option.textContent = category;
                categoryFilter.appendChild(option);
            });
        }
    } catch (error) {
        console.error('Error fetching categories:', error);
    }
}

async function fetchBrands() {
    try {
        const response = await fetch('http://localhost:8080/api/public/products/brands');
        const brands = await response.json();
        const brandFilter = document.getElementById('brand-filter');
        if (brandFilter) {
            brands.forEach(brand => {
                const option = document.createElement('option');
                option.value = brand;
                option.textContent = brand;
                brandFilter.appendChild(option);
            });
        }
    } catch (error) {
        console.error('Error fetching brands:', error);
    }
}

function displayProducts(products) {
    const productGrid = document.getElementById('product-grid');
    if (!productGrid) {
        console.log('Product grid not found');
        return;
    }
    productGrid.innerHTML = '';

    products.forEach(product => {
        const productDiv = document.createElement('div');
        productDiv.classList.add('product');
        productDiv.innerHTML = `
            <div class="product-image">
                <img src="${product.image || 'images/placeholder.jpg'}" alt="${product.name}">
            </div>
            <div class="product-info">
                <h3>${product.name}</h3>
                <p>${product.price} руб.</p>
                <button onclick="addToCart(${product.id})">Добавить в корзину</button>
            </div>
        `;
        productGrid.appendChild(productDiv);
    });
}

function filterAndSortProducts() {
    const searchInput = document.getElementById('search-input').value.toLowerCase();
    const categoryFilter = document.getElementById('category-filter').value;
    const brandFilter = document.getElementById('brand-filter').value;
    const sortFilter = document.getElementById('sort-filter').value;

    let filteredProducts = [...allProducts];

    if (searchInput) {
        filteredProducts = filteredProducts.filter(product =>
            product.name.toLowerCase().includes(searchInput)
        );
    }

    if (categoryFilter) {
        filteredProducts = filteredProducts.filter(product =>
            product.category === categoryFilter
        );
    }

    if (brandFilter) {
        filteredProducts = filteredProducts.filter(product =>
            product.brand === brandFilter
        );
    }

    switch (sortFilter) {
        case 'price-asc':
            filteredProducts.sort((a, b) => a.price - b.price);
            break;
        case 'price-desc':
            filteredProducts.sort((a, b) => b.price - a.price);
            break;
        case 'name-asc':
            filteredProducts.sort((a, b) => a.name.localeCompare(b.name));
            break;
        case 'name-desc':
            filteredProducts.sort((a, b) => b.name.localeCompare(a.name));
            break;
        default:
            break;
    }

    displayProducts(filteredProducts);
}

async function addToCart(productId) {
    try {
        console.log('Sending request to add product to cart, productId:', productId);
        const response = await fetch(`http://localhost:8080/api/cart/add/${productId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'include',
        });
        console.log('Response status:', response.status);
        console.log('Response ok:', response.ok);

        if (response.ok) {
            console.log('Product added to cart, updating UI...');
            await updateCartCount();
            if (typeof loadCart === 'function') {
                await loadCart();
            }
            alert('Товар добавлен в корзину!');
        } else if (response.status === 401) {
            const errorData = await response.json();
            const errorMessage = errorData.message || 'Пожалуйста, авторизуйтесь';
            console.log('Unauthorized, message:', errorMessage);
            alert(errorMessage);
            window.location.href = '/login.html';
        } else {
            const errorText = await response.text();
            console.log('Failed to add product to cart, status:', response.status, 'message:', errorText);
            alert('Ошибка при добавлении товара в корзину: ' + errorText);
        }
    } catch (error) {
        console.error('Error adding to cart:', error);
        alert('Произошла ошибка. Пожалуйста, попробуйте снова.');
    }
}

async function fetchCart() {
    try {
        const response = await fetch(`http://localhost:8080/api/cart`, {
            credentials: 'include',
            cache: 'no-store',
        });
        if (response.ok) {
            const cart = await response.json();
            console.log('Fetched cart:', cart);
            return cart.items || [];
        } else if (response.status === 401) {
            const currentPath = window.location.pathname;
            console.log('User not authenticated, current path:', currentPath);
            // Перенаправляем только если пользователь на странице корзины
            if (currentPath.includes('cart.html')) {
                alert('Пожалуйста, авторизуйтесь, чтобы открыть корзину.');
                window.location.href = '/login.html';
            }
            return [];
        } else {
            console.error('Failed to fetch cart, status:', response.status);
            console.error('Response text:', await response.text());
            return [];
        }
    } catch (error) {
        console.error('Error fetching cart:', error);
        return [];
    }
}

async function updateCartCount() {
    const cartItems = await fetchCart();
    const cartCount = document.getElementById('cart-count');
    if (!cartCount) {
        console.log('Cart count element not found, skipping update.');
        return;
    }

    console.log('Cart items for count:', cartItems);
    const totalItems = cartItems.reduce((sum, item) => {
        const qty = Number(item.quantity) || 0;
        return sum + qty;
    }, 0);
    cartCount.textContent = totalItems;
    console.log('Updated cart count:', totalItems);
}

async function checkout() {
    try {
        const response = await fetch('http://localhost:8080/api/cart/checkout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'include',
        });
        if (response.ok) {
            console.log('Checkout successful, updating UI...');
            alert('Заказ оформлен!');
            await updateCartCount();
            if (typeof loadCart === 'function') {
                await loadCart();
            }
        } else if (response.status === 401) {
            alert('Пожалуйста, авторизуйтесь, чтобы оформить заказ.');
            window.location.href = '/login.html';
        } else {
            console.error('Checkout failed, status:', response.status);
            console.error('Response text:', await response.text());
            alert('Ошибка при оформлении заказа. Пожалуйста, проверьте корзину.');
        }
    } catch (error) {
        console.error('Error during checkout:', error);
        alert('Произошла ошибка. Пожалуйста, попробуйте снова.');
    }
}

async function checkAuthStatus() {
    const userStatus = document.getElementById('user-status');
    const loginLink = document.getElementById('login-link');
    const logoutLink = document.getElementById('logout-link');
    const profileLink = document.getElementById('profile-link');
    const adminButton = document.getElementById('admin-button');

    if (!userStatus || !loginLink || !logoutLink || !profileLink || !adminButton) {
        console.log('Some navigation elements not found:', {
            userStatus: !!userStatus,
            loginLink: !!loginLink,
            logoutLink: !!logoutLink,
            profileLink: !!profileLink,
            adminButton: !!adminButton,
        });
    }

    try {
        console.log('Checking auth status...');
        const response = await fetch('http://localhost:8080/api/users/me', {
            credentials: 'include',
        });
        console.log('Auth status response:', response.status);

        if (response.ok) {
            console.log('User is authenticated');
            const user = await response.json();
            console.log('User data:', user);
            if (userStatus) userStatus.textContent = 'Вы вошли';
            if (loginLink) loginLink.style.display = 'none';
            if (logoutLink) logoutLink.style.display = 'inline';
            if (profileLink) profileLink.style.display = 'inline';
            if (adminButton && user.role === 'ADMIN') {
                adminButton.style.display = 'inline';
            } else if (adminButton) {
                adminButton.style.display = 'none';
            }
        } else {
            console.log('User is not authenticated, status:', response.status);
            console.log('Response text:', await response.text());
            if (userStatus) userStatus.textContent = 'Гость';
            if (loginLink) loginLink.style.display = 'inline';
            if (logoutLink) logoutLink.style.display = 'none';
            if (profileLink) profileLink.style.display = 'none';
            if (adminButton) adminButton.style.display = 'none';

            const currentPath = window.location.pathname;
            if (currentPath.includes('profile.html') || currentPath.includes('author.html') || currentPath.includes('cart.html')) {
                alert('Пожалуйста, авторизуйтесь, чтобы открыть эту страницу.');
                window.location.href = '/login.html';
            }
        }
    } catch (error) {
        console.error('Error checking auth status:', error);
        if (userStatus) userStatus.textContent = 'Гость';
        if (loginLink) loginLink.style.display = 'inline';
        if (logoutLink) logoutLink.style.display = 'none';
        if (profileLink) profileLink.style.display = 'none';
        if (adminButton) adminButton.style.display = 'none';

        const currentPath = window.location.pathname;
        if (currentPath.includes('profile.html') || currentPath.includes('author.html') || currentPath.includes('cart.html')) {
            alert('Пожалуйста, авторизуйтесь, чтобы открыть эту страницу.');
            window.location.href = '/login.html';
        }
    }
}

document.addEventListener('DOMContentLoaded', async () => {
    await checkAuthStatus();
    if (document.getElementById('product-grid')) {
        await fetchProducts();
        await fetchCategories();
        await fetchBrands();
    }
    // Вызываем updateCartCount только на страницах, где это необходимо
    const currentPath = window.location.pathname;
    if (!currentPath.includes('cart.html')) {
        await updateCartCount();
    }

    const searchInput = document.getElementById('search-input');
    const categoryFilter = document.getElementById('category-filter');
    const brandFilter = document.getElementById('brand-filter');
    const sortFilter = document.getElementById('sort-filter');

    if (searchInput) {
        searchInput.addEventListener('input', filterAndSortProducts);
    }
    if (categoryFilter) {
        categoryFilter.addEventListener('change', filterAndSortProducts);
    }
    if (brandFilter) {
        brandFilter.addEventListener('change', filterAndSortProducts);
    }
    if (sortFilter) {
        sortFilter.addEventListener('change', filterAndSortProducts);
    }
});