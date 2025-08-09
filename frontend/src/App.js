import React from 'react';
import BookForm from './components/BookForm';
import BookList from './components/BookList';

function App() {
  return (
    <div style={{ maxWidth: 900, margin: '24px auto', fontFamily: 'Arial, sans-serif' }}>
      <h1>📚 Book Tracker</h1>
      <BookForm />
      <hr />
      <BookList />
    </div>
  );
}

export default App;
