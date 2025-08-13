import React from 'react';
import BookItem from './BookItem';

const BookList = ({ books, onEdit, onDelete }) => {
  if (!books || books.length === 0) {
    return (
      <div className="book-list-empty">
        <p>No books available. Add your first book!</p>
      </div>
    );
  }

  return (
    <div className="book-list">
      <h2>Your Book Collection ({books.length} books)</h2>

      <div className="book-table">
        <table>
          <thead>
            <tr>
              <th>Title</th>
              <th>Author</th>
              <th>Published Date</th>
              <th>Genre</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {books.map(book => (
              <BookItem
                key={book.id}
                book={book}
                onEdit={onEdit}
                onDelete={onDelete}
              />
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default BookList;