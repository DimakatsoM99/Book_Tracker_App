package book.tracker.service;
import book.tracker.entity.Book;
import book.tracker.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // ===============================================
    // BASIC CRUD OPERATIONS
    // ===============================================

    /**
     * Get all books
     * @return List of all books in the database
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Get a book by ID
     * @param id Book ID
     * @return Optional containing the book if found
     */
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    /**
     * Create a new book
     * @param book Book to create
     * @return Created book with generated ID
     * @throws IllegalArgumentException if book already exists or data is invalid
     */
    public Book createBook(Book book) {
        // Business rule: Validate required fields
        validateBookData(book);

        // Business rule: Prevent duplicate books
        if (bookRepository.existsByTitleAndAuthor(book.getTitle(), book.getAuthor())) {
            throw new IllegalArgumentException(
                    String.format("Book with title '%s' by author '%s' already exists",
                            book.getTitle(), book.getAuthor())
            );
        }

        // Business rule: Set reasonable defaults
        if (book.getGenre() == null || book.getGenre().trim().isEmpty()) {
            book.setGenre("Unspecified");
        }

        return bookRepository.save(book);
    }

    /**
     * Update an existing book
     * @param id Book ID to update
     * @param updatedBook Updated book data
     * @return Updated book
     * @throws IllegalArgumentException if book not found or data invalid
     */
    public Book updateBook(Long id, Book updatedBook) {
        Optional<Book> existingBookOpt = bookRepository.findById(id);

        if (existingBookOpt.isEmpty()) {
            throw new IllegalArgumentException("Book with ID " + id + " not found");
        }

        Book existingBook = existingBookOpt.get();

        // Business rule: Validate updated data
        validateBookData(updatedBook);

        // Business rule: Check for duplicate when updating title/author
        if (!existingBook.getTitle().equals(updatedBook.getTitle()) ||
                !existingBook.getAuthor().equals(updatedBook.getAuthor())) {

            if (bookRepository.existsByTitleAndAuthor(updatedBook.getTitle(), updatedBook.getAuthor())) {
                throw new IllegalArgumentException(
                        String.format("Another book with title '%s' by author '%s' already exists",
                                updatedBook.getTitle(), updatedBook.getAuthor())
                );
            }
        }

        // Update fields
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setPublishedDate(updatedBook.getPublishedDate());
        existingBook.setGenre(updatedBook.getGenre());

        return bookRepository.save(existingBook);
    }

    /**
     * Delete a book by ID
     * @param id Book ID to delete
     * @throws IllegalArgumentException if book not found
     */
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new IllegalArgumentException("Book with ID " + id + " not found");
        }

        bookRepository.deleteById(id);
    }

    // ===============================================
    // SEARCH AND FILTER OPERATIONS
    // ===============================================

    /**
     * Search books by title (partial match, case-insensitive)
     */
    public List<Book> searchBooksByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return getAllBooks();
        }
        return bookRepository.findByTitleContainingIgnoreCase(title.trim());
    }

    /**
     * Search books by author (partial match, case-insensitive)
     */
    public List<Book> searchBooksByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            return getAllBooks();
        }
        return bookRepository.findByAuthorContainingIgnoreCase(author.trim());
    }

    /**
     * Search books by title OR author
     */
    public List<Book> searchBooks(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllBooks();
        }
        return bookRepository.findByTitleOrAuthorContaining(searchTerm.trim());
    }

    /**
     * Get books by genre
     */
    public List<Book> getBooksByGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            return getAllBooks();
        }
        return bookRepository.findByGenreIgnoreCase(genre.trim());
    }

    /**
     * Get books published after a specific year
     */
    public List<Book> getBooksPublishedAfter(int year) {
        LocalDate date = LocalDate.of(year, 1, 1);
        return bookRepository.findByPublishedDateAfter(date);
    }

    /**
     * Get recently published books (top 5)
     */
    public List<Book> getRecentBooks() {
        return bookRepository.findTop5ByOrderByPublishedDateDesc();
    }

    // ===============================================
    // ANALYTICS AND STATISTICS
    // ===============================================

    /**
     * Get total number of books
     */
    public long getTotalBookCount() {
        return bookRepository.count();
    }

    /**
     * Get all unique genres
     */
    public List<String> getAllGenres() {
        return bookRepository.findDistinctGenres();
    }

    /**
     * Count books by genre
     */
    public long countBooksByGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            return 0;
        }
        return bookRepository.countByGenre(genre.trim());
    }

    // ===============================================
    // PRIVATE HELPER METHODS
    // ===============================================

    /**
     * Validate book data according to business rules
     */
    private void validateBookData(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }

        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Book title is required");
        }

        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Book author is required");
        }

        // Business rule: Title and author length limits
        if (book.getTitle().length() > 255) {
            throw new IllegalArgumentException("Book title cannot exceed 255 characters");
        }

        if (book.getAuthor().length() > 255) {
            throw new IllegalArgumentException("Book author cannot exceed 255 characters");
        }

        // Business rule: Genre length limit
        if (book.getGenre() != null && book.getGenre().length() > 100) {
            throw new IllegalArgumentException("Book genre cannot exceed 100 characters");
        }

        // Business rule: Publication date cannot be in the future
        if (book.getPublishedDate() != null && book.getPublishedDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Publication date cannot be in the future");
        }
    }
}
