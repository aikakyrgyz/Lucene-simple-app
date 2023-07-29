-- Create the "Authors" table

CREATE TABLE authors (
    author_id SERIAL PRIMARY KEY,
    author_name VARCHAR(100) NOT NULL,
    author_birthdate DATE,
    author_country VARCHAR(50)
);

-- Create the "Books" table
CREATE TABLE books (
    book_id SERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    publication_year INTEGER,
    isbn VARCHAR(20),
    author_id INTEGER REFERENCES authors(author_id)
);

-- Create the "Genres" table
CREATE TABLE genres (
    genre_id SERIAL PRIMARY KEY,
    genre_name VARCHAR(50) NOT NULL
);

-- Create the "Book_Genres" junction table
CREATE TABLE book_genres (
    book_id INTEGER REFERENCES books(book_id),
    genre_id INTEGER REFERENCES genres(genre_id),
    PRIMARY KEY (book_id, genre_id)
);

-- Create the "Users" table
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    user_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    registration_date DATE
);

-- Create the "Borrowings" table
CREATE TABLE borrowings (
    borrowing_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id),
    book_id INTEGER REFERENCES books(book_id),
    borrowing_date DATE,
    return_date DATE
);
-- Sample data for "Authors" table
INSERT INTO authors (author_name, author_birthdate, author_country)
VALUES 
    ('J.K. Rowling', '1965-07-31', 'United Kingdom'),
    ('Stephen King', '1947-09-21', 'United States'),
    ('Haruki Murakami', '1949-01-12', 'Japan'),
    ('Agatha Christie', '1890-09-15', 'United Kingdom'),
    ('Ernest Hemingway', '1899-07-21', 'United States'),
    ('Jane Austen', '1775-12-16', 'United Kingdom');

-- Sample data for "Books" table
INSERT INTO books (title, publication_year, isbn, author_id)
VALUES 
    ('Harry Potter and the Sorcerer''s Stone', 1997, '978-0590353403', 1),
    ('The Shining', 1977, '978-0385121675', 2),
    ('Norwegian Wood', 1987, '978-0375704024', 3),
    ('Murder on the Orient Express', 1934, '978-0062693662', 4),
    ('The Old Man and the Sea', 1952, '978-0684801223', 5),
    ('Pride and Prejudice', 1813, '978-0141439518', 6);
    ('You are my pride', 1944, '978-0141439519', 1),
    ('Pride and sin', 2017, '978-0141434419', 1);


-- Sample data for "Genres" table
INSERT INTO genres (genre_name)
VALUES 
    ('Fantasy'),
    ('Horror'),
    ('Contemporary Fiction'),
    ('Mystery'),
    ('Classics'),
    ('Romance');

-- Sample data for "Book_Genres" table
INSERT INTO book_genres (book_id, genre_id)
VALUES
    (1, 1),
    (1, 6),
    (2, 2),
    (3, 3),
    (4, 4),
    (5, 5),
    (6, 6);

-- Sample data for "Users" table
INSERT INTO users (user_name, email, registration_date)
VALUES 
    ('Emily Johnson', 'emily@example.com', '2022-02-01'),
    ('Daniel Smith', 'daniel@example.com', '2022-03-15'),
    ('Sophia Lee', 'sophia@example.com', '2022-04-10'),
    ('Michael Brown', 'michael@example.com', '2022-05-05'),
    ('Olivia Wilson', 'olivia@example.com', '2022-06-20'),
    ('William Taylor', 'william@example.com', '2022-07-12');

-- Sample data for "Borrowings" table
INSERT INTO borrowings (user_id, book_id, borrowing_date, return_date)
VALUES
    (1, 1, '2022-05-01', '2022-06-01'),
    (2, 2, '2022-05-15', '2022-06-15'),
    (3, 3, '2022-06-01', '2022-07-01'),
    (4, 4, '2022-06-20', '2022-07-20'),
    (5, 5, '2022-07-01', '2022-08-01'),
    (6, 6, '2022-07-15', '2022-08-15');
