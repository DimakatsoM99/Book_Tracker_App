import React, { useState } from 'react';
import axios from 'axios';

const initial = { title: '', author: '', publishedDate: '', genre: '' };

export default function BookForm() {
  const [form, setForm] = useState(initial);
  const [loading, setLoading] = useState(false);

  const change = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const submit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await axios.post('http://localhost:8080/api/books', form);
      setForm(initial);
      // notify other components - simplest: reload window
      window.location.reload();
    } catch (err) {
      console.error(err);
      alert('Failed to add book');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={submit} style={{ display: 'grid', gap: 8 }}>
      <input name="title" value={form.title} onChange={change} placeholder="Title" required />
      <input name="author" value={form.author} onChange={change} placeholder="Author" required />
      <input name="publishedDate" type="date" value={form.publishedDate} onChange={change} required />
      <input name="genre" value={form.genre} onChange={change} placeholder="Genre" required />
      <button disabled={loading} type="submit">{loading ? 'Adding...' : 'Add Book'}</button>
    </form>
  );
}
