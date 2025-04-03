-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS planning_db;

-- Seleccionar la base de datos a usarevents
USE planning_db;

-- Crear la tabla 'events'
CREATE TABLE IF NOT EXISTS events (
    id SERIAL PRIMARY KEY,                    -- Identificador único autoincremental
    user_id BIGINT NOT NULL,   
    amount BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,               -- Título del evento
    date DATE NOT NULL,                    -- Fecha de fin
    type ENUM('income', 'savings', 'expense') NOT NULL -- Tipo de evento (ingreso, ahorro, egreso)
);


-- Crear la tabla 'Goals' para gestionar las metas
CREATE TABLE IF NOT EXISTS Goals (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,      -- Identificador único autoincremental (tipo BIGINT)
    user_id BIGINT NOT NULL,   
    title VARCHAR(255) NOT NULL,               -- Título de la meta
    target_amount DECIMAL NOT NULL,      -- Monto objetivo
    type ENUM('incremental', 'reductive') NOT NULL  -- Tipo de meta (incremental o reductivo)
);
