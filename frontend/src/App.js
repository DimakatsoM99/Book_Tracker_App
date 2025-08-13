import React, { useState, useEffect } from 'react';
import BookList from './components/BookList';
import BookForm from './components/BookForm';
import bookService from './services/bookService';
import './App.css';

function App() {
  // State management
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [editingBook, setEditingBook] = useState(null);

  // Load books when component mounts
  useEffect(() => {
    loadBooks();
  }, []);

  /**
   * Load all books from the backend
   */
  const loadBooks = async () => {
    try {
      setLoading(true);
      setError(null);
      console.log('Loading books...');
      const booksData = await bookService.getAllBooks();
      console.log('Books loaded:', booksData);
      setBooks(booksData);
    } catch (err) {
      setError('Failed to load books. Make sure the backend is running.');
      console.error('Error loading books:', err);
    } finally {
      setLoading(false);
    }
  };

  /**
   * Handle creating a new book
   */
  const handleCreateBook = async (bookData) => {

  console.log('Creating book with data:', bookData);
    try {
      const newBook = await bookService.createBook(bookData);
          console.log('Book created successfully:', newBook);

      setBooks([...books, newBook]);
      setShowForm(false);
      setError(null);
    } catch (err) {
        console.error(' Full error details:', {
          message: err.message,
          response: err.response?.data,
          status: err.response?.status,
          error: err
        });
      setError('Failed to create book. Please check your input.');
      console.error('Error creating book:', err);
    }
  };

  /**
   * Handle updating an existing book
   */
  const handleUpdateBook = async (id, bookData) => {
    try {
      const updatedBook = await bookService.updateBook(id, bookData);
      setBooks(books.map(book => book.id === id ? updatedBook : book));
      setEditingBook(null);
      setShowForm(false);
      setError(null);
    } catch (err) {
      setError('Failed to update book. Please check your input.');
      console.error('Error updating book:', err);
    }
  };

  /**
   * Handle deleting a book
   */
  const handleDeleteBook = async (id) => {
    if (window.confirm('Are you sure you want to delete this book?')) {
      try {
        await bookService.deleteBook(id);
        setBooks(books.filter(book => book.id !== id));
        setError(null);
      } catch (err) {
        setError('Failed to delete book.');
        console.error('Error deleting book:', err);
      }
    }
  };

  /**
   * Handle editing a book
   */
  const handleEditBook = (book) => {
    setEditingBook(book);
    setShowForm(true);
  };

  /**
   * Handle canceling form
   */
  const handleCancelForm = () => {
    setShowForm(false);
    setEditingBook(null);
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>📚 Book Tracker</h1>
        <p>Manage your personal book collection</p>
      </header>

      <main className="App-main">
        {/* Error message */}
        {error && (
          <div className="error-message">
            {error}
          </div>
        )}

        {/* Add Book button */}
        {!showForm && (
          <div className="actions">
            <button
              className="btn btn-primary"
              onClick={() => setShowForm(true)}
            >
              + Add New Book
            </button>
          </div>
        )}

        {/* Book Form (Add/Edit) */}
        {showForm && (
          <BookForm
            book={editingBook}
            onSubmit={editingBook ?
              (bookData) => handleUpdateBook(editingBook.id, bookData) :
              handleCreateBook
            }
            onCancel={handleCancelForm}
          />
        )}

        {/* Loading state */}
        {loading && (
          <div className="loading">
            <p>Loading books...</p>
          </div>
        )}

        {/* Book List */}
        {!loading && (
          <BookList
            books={books}
            onEdit={handleEditBook}
            onDelete={handleDeleteBook}
          />
        )}

        {/* Empty state */}
        {!loading && books.length === 0 && !error && (
          <div className="empty-state">
            <p>No books found. Add your first book to get started!</p>
          </div>
        )}
      </main>
    </div>
  );
}

export default App;