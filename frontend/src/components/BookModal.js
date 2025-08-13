// frontend/src/components/BookModal.js

import React, { useState, useEffect } from 'react';
import bookService from '../services/bookService';

const BookModal = ({ bookId, isOpen, onClose, onEdit, onDelete }) => {
  const [book, setBook] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Load book details when modal opens
  useEffect(() => {
    if (isOpen && bookId) {
      loadBookDetails();
    }
  }, [isOpen, bookId]);

  const loadBookDetails = async () => {
    try {
      setLoading(true);
      setError(null);
      const bookData = await bookService.getBookById(bookId);
      setBook(bookData);
    } catch (err) {
      setError('Failed to load book details');
      console.error('Error loading book details:', err);
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'Not specified';

    try {
      const date = new Date(dateString);
      return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
      });
    } catch (error) {
      return dateString;
    }
  };

  const handleEdit = () => {
    onEdit(book);
    onClose();
  };

  const handleDelete = () => {
    onDelete(book.id);
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div className="modal-overlay" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        {/* Modal Header */}
        <div className="modal-header">
          <h2>Book Details</h2>
          <button className="modal-close" onClick={onClose}>
            ✕
          </button>
        </div>

        {/* Modal Body */}
        <div className="modal-body">
          {loading && (
            <div className="modal-loading">
              <p>Loading book details...</p>
            </div>
          )}

          {error && (
            <div className="modal-error">
              <p>{error}</p>
            </div>
          )}

          {book && (
            <div className="book-details">
              <div className="detail-row">
                <span className="detail-label">ID:</span>
                <span className="detail-value">#{book.id}</span>
              </div>

              <div className="detail-row">
                <span className="detail-label">Title:</span>
                <span className="detail-value title-highlight">{book.title}</span>
              </div>

              <div className="detail-row">
                <span className="detail-label">Author:</span>
                <span className="detail-value">{book.author}</span>
              </div>

              <div className="detail-row">
                <span className="detail-label">Published Date:</span>
                <span className="detail-value">{formatDate(book.publishedDate)}</span>
              </div>

              <div className="detail-row">
                <span className="detail-label">Genre:</span>
                <span className="detail-value">
                  <span className="genre-tag">{book.genre || 'Unspecified'}</span>
                </span>
              </div>

              <div className="detail-row">
                <span className="detail-label">Added:</span>
                <span className="detail-value text-muted">
                  ID #{book.id} in your collection
                </span>
              </div>
            </div>
          )}
        </div>

        {/* Modal Footer */}
        {book && (
          <div className="modal-footer">
            <button
              className="btn btn-edit"
              onClick={handleEdit}
            >
              ✏️ Edit Book
            </button>

            <button
              className="btn btn-delete"
              onClick={handleDelete}
            >
              🗑️ Delete Book
            </button>

            <button
              className="btn btn-secondary"
              onClick={onClose}
            >
              Close
            </button>
          </div>
        )}
      </div>
    </div>
  );
};

export default BookModal;