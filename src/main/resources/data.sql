-- Insertar datos iniciales para la aplicación Dog Management System
-- Estos datos se cargan automáticamente al iniciar la aplicación con H2

-- Insertar propietarios
INSERT INTO owners (first_name, last_name, email, phone, address, city, postal_code, age, observations, is_active, created_at, updated_at) VALUES
('Juan', 'Pérez', 'juan.perez@email.com', '3001234567', 'Calle 123 #45-67', 'Bogotá', '11111', 32, 'Propietario responsable, primera vez con mascota', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('María', 'González', 'maria.gonzalez@email.com', '3012345678', 'Carrera 15 #23-45', 'Medellín', '22222', 28, 'Tiene experiencia con perros grandes', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Carlos', 'Rodríguez', 'carlos.rodriguez@email.com', '3123456789', 'Avenida 80 #12-34', 'Cali', '33333', 35, 'Vive en casa con jardín amplio', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Ana', 'Martínez', 'ana.martinez@email.com', '3134567890', 'Calle 50 #78-90', 'Barranquilla', '44444', 26, 'Busca perro para compañía', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Luis', 'Hernández', 'luis.hernandez@email.com', '3145678901', 'Carrera 7 #56-78', 'Cartagena', '55555', 45, 'Familia con niños pequeños', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insertar perros
INSERT INTO dogs (name, breed, age, color, weight, birth_date, description, is_vaccinated, size, is_available, owner_id, created_at, updated_at) VALUES
('Max', 'Golden Retriever', 3, 'Dorado', 28.5, '2021-03-15', 'Perro muy amigable y juguetón, ideal para familias con niños. Le encanta jugar en el agua.', true, 'MEDIANO', false, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Bella', 'Labrador', 2, 'Negro', 24.0, '2022-06-20', 'Perra cariñosa y obediente, perfecta para personas activas. Muy inteligente.', true, 'MEDIANO', false, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Rocky', 'Pastor Alemán', 4, 'Café y Negro', 35.0, '2020-01-10', 'Perro guardián muy leal, necesita ejercicio diario. Excelente con niños.', true, 'GRANDE', false, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Luna', 'Husky Siberiano', 1, 'Gris y Blanco', 22.0, '2023-04-05', 'Cachorra muy energética, necesita mucho ejercicio. Muy sociable con otros perros.', true, 'MEDIANO', false, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Toby', 'Bulldog Francés', 3, 'Crema', 12.0, '2021-08-12', 'Perro tranquilo y cariñoso, ideal para apartamentos. Muy bueno con personas mayores.', true, 'PEQUEÑO', false, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Perros disponibles para adopción
('Buddy', 'Beagle', 2, 'Tricolor', 15.0, '2022-09-18', 'Perro muy alegre y amigable, le encanta jugar. Perfecto para familias activas.', true, 'PEQUEÑO', true, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Lola', 'Cocker Spaniel', 1, 'Marrón', 13.5, '2023-02-14', 'Cachorra muy dulce y cariñosa, fácil de entrenar. Ideal para principiantes.', true, 'PEQUEÑO', true, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Zeus', 'Rottweiler', 3, 'Negro', 45.0, '2021-11-30', 'Perro fuerte y protector, necesita dueño con experiencia. Muy leal una vez que confía.', true, 'GRANDE', true, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Mía', 'Border Collie', 2, 'Blanco y Negro', 18.0, '2022-05-25', 'Perra muy inteligente y activa, necesita estimulación mental. Excelente para deportes caninos.', true, 'MEDIANO', true, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Simba', 'Shih Tzu', 4, 'Dorado y Blanco', 6.5, '2020-12-08', 'Perro pequeño y elegante, muy cariñoso. Perfecto para personas que buscan un compañero tranquilo.', true, 'PEQUEÑO', true, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- Cachorros disponibles
('Chocolate', 'Labrador', 0, 'Chocolate', 8.0, '2024-01-15', 'Cachorro muy tierno y juguetón, apenas está aprendiendo comandos básicos.', false, 'PEQUEÑO', true, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Nieve', 'Samoyedo', 0, 'Blanco', 7.5, '2024-02-20', 'Cachorra muy peluda y adorable, necesita cuidados especiales para su pelaje.', false, 'PEQUEÑO', true, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Coco', 'Yorkshire Terrier', 0, 'Café y Negro', 2.5, '2024-03-10', 'Cachorro muy pequeño pero valiente, ideal para personas que viven en apartamentos.', false, 'PEQUEÑO', true, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insertar propietarios adicionales sin perros
INSERT INTO owners (first_name, last_name, email, phone, address, city, postal_code, age, observations, is_active, created_at, updated_at) VALUES
('Pedro', 'Jiménez', 'pedro.jimenez@email.com', '3156789012', 'Calle 85 #34-56', 'Bucaramanga', '66666', 38, 'Interesado en adoptar un perro de raza grande', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Laura', 'Sánchez', 'laura.sanchez@email.com', '3167890123', 'Carrera 45 #67-89', 'Pereira', '77777', 31, 'Busca un perro tranquilo para acompañar en casa', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Diego', 'Ramírez', 'diego.ramirez@email.com', '3178901234', 'Avenida 30 #78-90', 'Manizales', '88888', 29, 'Familia joven buscando su primera mascota', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Sofía', 'Torres', 'sofia.torres@email.com', '3189012345', 'Calle 60 #12-34', 'Ibagué', '99999', 42, 'Propietaria con mucha experiencia en perros', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Andrés', 'Vargas', 'andres.vargas@email.com', '3190123456', 'Carrera 20 #45-67', 'Pasto', '10101', 33, 'Busca un perro guardián para su finca', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP); 