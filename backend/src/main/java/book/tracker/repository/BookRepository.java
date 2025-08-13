package book.tracker.repository;
import book.tracker.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
public interface BookRepository extends JpaRepository<Book, Long>{

    // Basic CRUD operations are inherited from JpaRepository:
    // - save(Book book)                    → INSERT/UPDATE
    // - findById(Long id)                  → SELECT by ID
    // - findAll()                          → SELECT all books
    // - deleteById(Long id)                → DELETE by ID
    // - delete(Book book)                  → DELETE entity
    // - count()                            → COUNT all books
    // - existsById(Long id)                → CHECK if exists

    // Custom query methods for additional functionality

    /**
     * Find books by title (case-insensitive partial match)
     * Example: findByTitleContainingIgnoreCase("gatsby")
     *          → finds "The Great Gatsby"
     */
    List<Book> findByTitleContainingIgnoreCase(String title);

    /**
     * Find books by author (case-insensitive partial match)
     * Example: findByAuthorContainingIgnoreCase("orwell")
     *          → finds books by "George Orwell"
     */
    List<Book> findByAuthorContainingIgnoreCase(String author);

    /**
     * Find books by exact genre (case-insensitive)
     * Example: findByGenreIgnoreCase("fiction")
     *          → finds all Fiction books
     */
    List<Book> findByGenreIgnoreCase(String genre);

    /**
     * Find books published after a specific date
     * Example: findByPublishedDateAfter(LocalDate.of(2000, 1, 1))
     *          → finds books published after year 2000
     */
    List<Book> findByPublishedDateAfter(LocalDate date);

    /**
     * Find books published before a specific date
     * Example: findByPublishedDateBefore(LocalDate.of(1950, 1, 1))
     *          → finds books published before 1950
     */
    List<Book> findByPublishedDateBefore(LocalDate date);

    /**
     * Find books published between two dates (inclusive)
     * Example: findByPublishedDateBetween(start, end)
     *          → finds books in date range
     */
    List<Book> findByPublishedDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Find books by both author and genre
     * Example: findByAuthorContainingIgnoreCaseAndGenreIgnoreCase("tolkien", "fantasy")
     *          → finds Fantasy books by Tolkien
     */
    List<Book> findByAuthorContainingIgnoreCaseAndGenreIgnoreCase(String author, String genre);

    /**
     * Check if a book exists with the same title and author
     * Useful for preventing duplicate books
     * Example: existsByTitleAndAuthor("1984", "George Orwell")
     *          → returns true if book already exists
     */
    boolean existsByTitleAndAuthor(String title, String author);

    /**
     * Custom JPQL query to search books by title OR author
     * Example: findByTitleOrAuthorContaining("orwell")
     *          → finds books with "orwell" in title OR author
     */
    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(b.author) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Book> findByTitleOrAuthorContaining(@Param("searchTerm") String searchTerm);

    /**
     * Get all unique genres in the database (for dropdowns/filters)
     * Example: ["Fiction", "Non-Fiction", "Biography", "Science Fiction"]
     */
    @Query("SELECT DISTINCT b.genre FROM Book b WHERE b.genre IS NOT NULL ORDER BY b.genre")
    List<String> findDistinctGenres();

    /**
     * Count books by genre (for analytics/statistics)
     * Example: countByGenre("Fiction") → returns number of Fiction books
     */
    @Query("SELECT COUNT(b) FROM Book b WHERE LOWER(b.genre) = LOWER(:genre)")
    Long countByGenre(@Param("genre") String genre);

    /**
     * Find the most recently published books
     * Example: findTop5ByOrderByPublishedDateDesc() → 5 newest books
     */
    List<Book> findTop5ByOrderByPublishedDateDesc();

    /**
     * Find books with null publication date (data cleanup)
     */
    List<Book> findByPublishedDateIsNull();
}
