CREATE TABLE paypal_transactions (
    transaction_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    description TEXT,
    payment_id VARCHAR(255) NOT NULL, -- Adicionado o campo payment_id conforme a classe PaypalModel

    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(user_id) -- Assumindo que a tabela de usuários existe e tem um user_id
);
