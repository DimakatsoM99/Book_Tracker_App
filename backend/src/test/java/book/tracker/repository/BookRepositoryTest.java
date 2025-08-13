package book.tracker.repository;

import book.tracker.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@ActiveProfiles("test")
public class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    private Book testBook1;
    private Book testBook2;

    @BeforeEach
    void setUp() {
        // Create test data
        testBook1 = new Book(
                "The Great Gatsby",
                "F. Scott Fitzgerald",
                LocalDate.of(1925, 4, 10),
                "Classic Literature"
        );

        testBook2 = new Book(
                "1984",
                "George Orwell",
                LocalDate.of(1949, 6, 8),
                "Dystopian Fiction"
        );
    }

    @Test
    void testSaveAndFindById() {
        // Save book
        Book savedBook = bookRepository.save(testBook1);

        // Verify book was saved with ID
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("The Great Gatsby");

        // Find by ID
        Optional<Book> foundBook = bookRepository.findById(savedBook.getId());
        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo("The Great Gatsby");
    }

    @Test
    void testFindAll() {
        // Save multiple books
        bookRepository.save(testBook1);
        bookRepository.save(testBook2);

        // Find all books
        List<Book> books = bookRepository.findAll();

        assertThat(books).hasSize(2);
        assertThat(books).extracting(Book::getTitle)
                .containsExactlyInAnyOrder("The Great Gatsby", "1984");
    }

    @Test
    void testDeleteById() {
        // Save book
        Book savedBook = bookRepository.save(testBook1);
        Long bookId = savedBook.getId();

        // Verify book exists
        assertThat(bookRepository.findById(bookId)).isPresent();

        // Delete book
        bookRepository.deleteById(bookId);

        // Verify book is deleted
        assertThat(bookRepository.findById(bookId)).isEmpty();
    }

    @Test
    void testFindByTitleContainingIgnoreCase() {
        // Save books
        bookRepository.save(testBook1);
        bookRepository.save(testBook2);

        // Search by partial title (case insensitive)
        List<Book> books = bookRepository.findByTitleContainingIgnoreCase("gatsby");

        assertThat(books).hasSize(1);
        assertThat(books.get(0).getTitle()).isEqualTo("The Great Gatsby");
    }

    @Test
    void testFindByAuthorContainingIgnoreCase() {
        // Save books
        bookRepository.save(testBook1);
        bookRepository.save(testBook2);

        // Search by partial author name (case insensitive)
        List<Book> books = bookRepository.findByAuthorContainingIgnoreCase("orwell");

        assertThat(books).hasSize(1);
        assertThat(books.get(0).getAuthor()).isEqualTo("George Orwell");
    }

    @Test
    void testExistsByTitleAndAuthor() {
        // Save book
        bookRepository.save(testBook1);

        // Check if book exists
        boolean exists = bookRepository.existsByTitleAndAuthor(
                "The Great Gatsby",
                "F. Scott Fitzgerald"
        );
        assertThat(exists).isTrue();

        // Check if non-existent book exists
        boolean notExists = bookRepository.existsByTitleAndAuthor(
                "Non-existent Book",
                "Unknown Author"
        );
        assertThat(notExists).isFalse();
    }

    @Test
    void testCount() {
        // Initially no books
        assertThat(bookRepository.count()).isEqualTo(0);

        // Save books
        bookRepository.save(testBook1);
        bookRepository.save(testBook2);

        // Verify count
        assertThat(bookRepository.count()).isEqualTo(2);
    }

    @Test
    void testFindByGenreIgnoreCase() {
        // Save books
        bookRepository.save(testBook1);
        bookRepository.save(testBook2);

        // Find by genre (case insensitive)
        List<Book> classicBooks = bookRepository.findByGenreIgnoreCase("classic literature");

        assertThat(classicBooks).hasSize(1);
        assertThat(classicBooks.get(0).getGenre()).isEqualTo("Classic Literature");
    }
}
