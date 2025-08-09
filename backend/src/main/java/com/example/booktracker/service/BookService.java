package com.example.booktracker.service;

import com.example.booktracker.model.Book;
import com.example.booktracker.repository.BookRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository repo;

    public BookService(BookRepository repo) {
        this.repo = repo;
    }

    public List<Book> getAllBooks() { return repo.findAll(); }
    public Optional<Book> getById(Long id) { return repo.findById(id); }
    public Book create(Book b) { return repo.save(b); }
    public Optional<Book> update(Long id, Book b) {
        return repo.findById(id).map(existing -> {
            existing.setTitle(b.getTitle());
            existing.setAuthor(b.getAuthor());
            existing.setPublishedDate(b.getPublishedDate());
            existing.setGenre(b.getGenre());
            return repo.save(existing);
        });
    }
    public void delete(Long id) { repo.deleteById(id); }
}
