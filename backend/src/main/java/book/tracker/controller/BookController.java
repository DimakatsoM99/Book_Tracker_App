package book.tracker.controller;
import book.tracker.entity.Book;
import book.tracker.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://localhost:3000")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService aBookService) {
        this.bookService = aBookService;
    }

    /**
     * GET /api/books - Get all books
     * Optional query parameters:
     * - search: Search in title or author
     * - genre: Filter by genre
     * - author: Filter by author
     */
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String author) {

        try {
            List<Book> books;

            // Apply filters based on query parameters
            if (search != null && !search.trim().isEmpty()) {
                books = bookService.searchBooks(search);
            } else if (genre != null && !genre.trim().isEmpty()) {
                books = bookService.getBooksByGenre(genre);
            } else if (author != null && !author.trim().isEmpty()) {
                books = bookService.searchBooksByAuthor(author);
            } else {
                books = bookService.getAllBooks();
            }

            return ResponseEntity.ok(books);

        } catch (Exception e) {
            // Log error and return server error
            System.err.println("Error getting books: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/books/{id} - Get a book by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        try {
            Optional<Book> book = bookService.getBookById(id);

            if (book.isPresent()) {
                return ResponseEntity.ok(book.get());
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            System.err.println("Error getting book by ID: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * POST /api/books - Create a new book
     */
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        try {
            Book createdBook = bookService.createBook(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);

        } catch (IllegalArgumentException e) {
            // Business logic error (validation, duplicates, etc.)
            System.err.println("Validation error creating book: " + e.getMessage());
            return ResponseEntity.badRequest().build();

        } catch (Exception e) {
            // Unexpected error
            System.err.println("Error creating book: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * PUT /api/books/{id} - Update an existing book
     */
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book book) {
        try {
            Book updatedBook = bookService.updateBook(id, book);
            return ResponseEntity.ok(updatedBook);

        } catch (IllegalArgumentException e) {
            // Business logic error (not found, validation, duplicates, etc.)
            System.err.println("Validation error updating book: " + e.getMessage());

            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.badRequest().build();
            }

        } catch (Exception e) {
            // Unexpected error
            System.err.println("Error updating book: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * DELETE /api/books/{id} - Delete a book
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.noContent().build();

        } catch (IllegalArgumentException e) {
            // Book not found
            System.err.println("Error deleting book: " + e.getMessage());
            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            // Unexpected error
            System.err.println("Error deleting book: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    }

