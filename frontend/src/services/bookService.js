import axios from 'axios';

// Base URL for your backend API
const API_BASE_URL = '/api/books';

// Create axios instance with default config
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Book Service - handles all API calls to backend
const bookService = {

  /**
   * Get all books
   * @returns {Promise} Array of books
   */
  getAllBooks: async () => {
  console.log('getAllBooks called - hitting:', '/api/books/');
    try {
      const response = await api.get('');
      console.log('getAllBooks response:', response.data);
      return response.data;
    } catch (error) {
      console.error('Error fetching books:', error);
      throw error;
    }
  },

  /**
   * Get a book by ID
   * @param {number} id - Book ID
   * @returns {Promise} Book object
   */
  getBookById: async (id) => {
    try {
      const response = await api.get(`/${id}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching book with ID ${id}:`, error);
      throw error;
    }
  },

  /**
   * Create a new book
   * @param {Object} bookData - Book object {title, author, publishedDate, genre}
   * @returns {Promise} Created book object
   */
  createBook: async (bookData) => {
    try {
      const response = await api.post('', bookData);
      return response.data;
    } catch (error) {
      console.error('Error creating book:', error);
      throw error;
    }
  },

  /**
   * Update an existing book
   * @param {number} id - Book ID
   * @param {Object} bookData - Updated book data
   * @returns {Promise} Updated book object
   */
  updateBook: async (id, bookData) => {
    try {
      const response = await api.put(`/${id}`, bookData);
      return response.data;
    } catch (error) {
      console.error(`Error updating book with ID ${id}:`, error);
      throw error;
    }
  },

  /**
   * Delete a book
   * @param {number} id - Book ID to delete
   * @returns {Promise} Deletion confirmation
   */
  deleteBook: async (id) => {
    try {
      await api.delete(`/${id}`);
      return { success: true, message: 'Book deleted successfully' };
    } catch (error) {
      console.error(`Error deleting book with ID ${id}:`, error);
      throw error;
    }
  },
};

export default bookService;
