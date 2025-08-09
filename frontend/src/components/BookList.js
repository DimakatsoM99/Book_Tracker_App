import React, { useEffect, useState } from 'react';
import axios from 'axios';

export default function BookList() {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchBooks = async () => {
    try {
      const res = await axios.get('http://localhost:8080/api/books');
      setBooks(res.data);
    } catch (err) {
      console.error(err);
      alert('Failed to load books');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchBooks(); }, []);

  const remove = async (id) => {
    if (!window.confirm('Delete this book?')) return;
    try {
      await axios.delete(`http://localhost:8080/api/books/${id}`);
      setBooks(books.filter(b => b.id !== id));
    } catch (err) {
      console.error(err);
      alert('Delete failed');
    }
  };

  if (loading) return <p>Loading books…</p>;
  if (!books.length) return <p>No books yet — add one above.</p>;

  return (
    <div>
      <h2>All Books</h2>
      <table style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr style={{ textAlign: 'left', borderBottom: '1px solid #ddd' }}>
            <th>ID</th><th>Title</th><th>Author</th><th>Date</th><th>Genre</th><th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {books.map(b => (
            <tr key={b.id} style={{ borderBottom: '1px solid #f0f0f0' }}>
              <td>{b.id}</td>
              <td>{b.title}</td>
              <td>{b.author}</td>
              <td>{b.publishedDate}</td>
              <td>{b.genre}</td>
              <td>
                <button onClick={() => remove(b.id)}>Delete</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
