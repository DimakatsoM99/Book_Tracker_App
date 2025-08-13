package book.tracker.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "books")

public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 255, message = "Author must not exceed 255 characters")
    @Column(nullable = false)
    private String author;

    @Column(name = "published_date")
    private LocalDate publishedDate;

    @Size(max = 100, message = "Genre must not exceed 100 characters")
    private String genre;

    public Book() {}
    public Book(String aTitle, String aAuthor, LocalDate aPublishedDate, String aGenre) {
        this.title = aTitle;
        this.author = aAuthor;
        this.publishedDate = aPublishedDate;
        this.genre = aGenre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long aId) {
        this.id = aId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String aTitle) {
        this.title = aTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String aAuthor) {
        this.author = aAuthor;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate aPublishedDate) {
        this.publishedDate = aPublishedDate;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String aGenre) {
        this.genre = aGenre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString() for debugging
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publishedDate=" + publishedDate +
                ", genre='" + genre + '\'' +
                '}';
    }
}
