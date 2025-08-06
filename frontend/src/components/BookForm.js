import React, { useState } from 'react';
import axios from 'axios';

function BookForm() {
  const [formData, setFormData] = useState({
    title: '',
    author: '',
    publishedDate: '',
    genre: ''
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    await axios.post("http://localhost:8080/api/books", formData);
    setFormData({ title: '', author: '', publishedDate: '', genre: '' });
    window.location.reload(); // to refresh book list
  };

  return (
    <form onSubmit={handleSubmit}>
      <input name="title" value={formData.title} onChange={handleChange} placeholder="Title" required />
      <input name="author" value={formData.author} onChange={handleChange} placeholder="Author" required />
      <input name="publishedDate" type="date" value={formData.publishedDate} onChange={handleChange} required />
      <input name="genre" value={formData.genre} onChange={handleChange} placeholder="Genre" required />
      <button type="submit">Add Book</button>
    </form>
  );
}

export default BookForm;
