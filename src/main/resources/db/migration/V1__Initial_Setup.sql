-- Booking Table
CREATE TABLE booking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    show_id BIGINT NOT NULL,
    booking_time TIMESTAMP NOT NULL,
    subtotal DECIMAL(10, 2),
    discount DECIMAL(10, 2)
);

ALTER TABLE booking
ADD COLUMN movie_name VARCHAR(255) NOT NULL,
ADD COLUMN theatre_name VARCHAR(255) NOT NULL,
ADD COLUMN grand_total DECIMAL(10, 2) NOT NULL;

CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20)
);

CREATE TABLE ticket (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT NOT NULL,
    seat_number VARCHAR(10) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,  -- The price at the time of booking
    FOREIGN KEY (booking_id) REFERENCES booking(id)
);