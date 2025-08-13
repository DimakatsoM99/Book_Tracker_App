import React from 'react';

const BookItem = ({ book, onEdit, onDelete }) => {

  /**
   * Format date for display
   */
  const formatDate = (dateString) => {
    if (!dateString) return 'Not specified';

    try {
      const date = new Date(dateString);
      return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
      });
    } catch (error) {
      return dateString;
    }
  };

  /**
   * Handle edit button click
   */
  const handleEdit = () => {
    onEdit(book);
  };

  /**
   * Handle delete button click
   */
  const handleDelete = () => {
    onDelete(book.id);
  };

  return (
    <tr className="book-item">
      <td className="book-title">
        <strong>{book.title}</strong>
      </td>

      <td className="book-author">
        {book.author}
      </td>

      <td className="book-date">
        {formatDate(book.publishedDate)}
      </td>

      <td className="book-genre">
        <span className="genre-tag">
          {book.genre || 'Unspecified'}
        </span>
      </td>

      <td className="book-actions">
        <button
          className="btn btn-edit"
          onClick={handleEdit}
          title="Edit book"
        >
          ✏️ Edit
        </button>

        <button
          className="btn btn-delete"
          onClick={handleDelete}
          title="Delete book"
        >
          🗑️ Delete
        </button>
      </td>
    </tr>
  );
};

export default BookItem;