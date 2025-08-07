-- users Table


CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    avatar TEXT,
    role ENUM('user', 'admin') DEFAULT 'user',
    reset_password_token VARCHAR(255),
    reset_password_token_expire DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);


-- orders Table


CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    items_price DECIMAL(10,2) NOT NULL DEFAULT 0.0,
    tax_price DECIMAL(10,2) NOT NULL DEFAULT 0.0,
    shipping_price DECIMAL(10,2) NOT NULL DEFAULT 0.0,
    total_price DECIMAL(10,2) NOT NULL DEFAULT 0.0,
    payment_id VARCHAR(255) NOT NULL,
    payment_status VARCHAR(255) NOT NULL,
    paid_at DATETIME,
    delivered_at DATETIME,
    order_status VARCHAR(255) NOT NULL DEFAULT 'Processing',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);


-- shipping_info Table

CREATE TABLE shipping_info (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    address VARCHAR(255) NOT NULL,
    country VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    phone_no VARCHAR(20) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

-- order_items Table

CREATE TABLE order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    image TEXT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    product_id INT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);



-- products Table

CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10,2) NOT NULL DEFAULT 0.0,
    description TEXT NOT NULL,
    ratings DECIMAL(3,2) DEFAULT 0,
    category ENUM(
        'Electronics',
        'Mobile Phones',
        'Laptops',
        'Accessories',
        'Headphones',
        'Food',
        'Books',
        'Clothes/Shoes',
        'Beauty/Health',
        'Sports',
        'Outdoor',
        'Home'
    ) NOT NULL,
    seller VARCHAR(255) NOT NULL,
    stock INT NOT NULL CHECK (stock <= 99),
    num_of_reviews INT DEFAULT 0,
    user_id INT, 
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE SET NULL
);


-- product_images

CREATE TABLE product_images (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    image TEXT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);


-- product_reviews Table 

CREATE TABLE product_reviews (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    user_id INT NOT NULL,
    rating DECIMAL(3,2) NOT NULL,
    comment TEXT NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

