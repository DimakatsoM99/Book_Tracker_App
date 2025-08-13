import React, { useState, useEffect } from 'react';

const BookForm = ({ book, onSubmit, onCancel }) => {
  // Form state
  const [formData, setFormData] = useState({
    title: '',
    author: '',
    publishedDate: '',
    genre: ''
  });

  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);

  // Populate form if editing existing book
  useEffect(() => {
    if (book) {
      setFormData({
        title: book.title || '',
        author: book.author || '',
        publishedDate: book.publishedDate || '',
        genre: book.genre || ''
      });
    }
  }, [book]);

  /**
   * Handle input changes
   */
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));

    // Clear error for this field when user starts typing
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  /**
   * Validate form data
   */
  const validateForm = () => {
    const newErrors = {};

    if (!formData.title.trim()) {
      newErrors.title = 'Title is required';
    }

    if (!formData.author.trim()) {
      newErrors.author = 'Author is required';
    }

    if (formData.publishedDate) {
      const date = new Date(formData.publishedDate);
      const today = new Date();

      if (date > today) {
        newErrors.publishedDate = 'Publication date cannot be in the future';
      }
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  /**
   * Handle form submission
   */
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    setIsSubmitting(true);

    try {
      // Prepare data for submission
      const submitData = {
        title: formData.title.trim(),
        author: formData.author.trim(),
        publishedDate: formData.publishedDate || null,
        genre: formData.genre.trim() || null
      };
        console.log('📝 Submitting data:', submitData);
      await onSubmit(submitData);

      // Reset form if this was a create operation
      if (!book) {
        setFormData({
          title: '',
          author: '',
          publishedDate: '',
          genre: ''
        });
      }

    } catch (error) {
      console.error('Form submission error:', error);
    } finally {
      setIsSubmitting(false);
    }
  };

  /**
   * Handle cancel
   */
  const handleCancel = () => {
    setFormData({
      title: '',
      author: '',
      publishedDate: '',
      genre: ''
    });
    setErrors({});
    onCancel();
  };

  return (
    <div className="book-form">
      <h2>{book ? 'Edit Book' : 'Add New Book'}</h2>

      <form onSubmit={handleSubmit}>
        {/* Title Field */}
        <div className="form-group">
          <label htmlFor="title">Title *</label>
          <input
            type="text"
            id="title"
            name="title"
            value={formData.title}
            onChange={handleChange}
            placeholder="Enter book title"
            className={errors.title ? 'error' : ''}
          />
          {errors.title && <span className="error-text">{errors.title}</span>}
        </div>

        {/* Author Field */}
        <div className="form-group">
          <label htmlFor="author">Author *</label>
          <input
            type="text"
            id="author"
            name="author"
            value={formData.author}
            onChange={handleChange}
            placeholder="Enter author name"
            className={errors.author ? 'error' : ''}
          />
          {errors.author && <span className="error-text">{errors.author}</span>}
        </div>

        {/* Published Date Field */}
        <div className="form-group">
          <label htmlFor="publishedDate">Published Date</label>
          <input
            type="date"
            id="publishedDate"
            name="publishedDate"
            value={formData.publishedDate}
            onChange={handleChange}
            className={errors.publishedDate ? 'error' : ''}
          />
          {errors.publishedDate && <span className="error-text">{errors.publishedDate}</span>}
        </div>

        {/* Genre Field */}
        <div className="form-group">
          <label htmlFor="genre">Genre</label>
          <input
            type="text"
            id="genre"
            name="genre"
            value={formData.genre}
            onChange={handleChange}
            placeholder="Enter genre (optional)"
          />
        </div>

        {/* Form Actions */}
        <div className="form-actions">
          <button
            type="submit"
            className="btn btn-primary"
            disabled={isSubmitting}
          >
            {isSubmitting ? 'Saving...' : (book ? 'Update Book' : 'Add Book')}
          </button>

          <button
            type="button"
            className="btn btn-secondary"
            onClick={handleCancel}
            disabled={isSubmitting}
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
};

export default BookForm;