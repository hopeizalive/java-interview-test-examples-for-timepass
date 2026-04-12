package com.example.jpa.interview.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "books")
@NamedQuery(
        name = Book.NAMED_FIND_BY_AUTHOR_NAME,
        query = "select b from Book b join b.author a where a.name = :name order by b.title"
)
@NamedEntityGraph(
        name = Book.GRAPH_WITH_DETAILS,
        attributeNodes = {
                @NamedAttributeNode("author"),
                @NamedAttributeNode("categories")
        }
)
public class Book {

    public static final String NAMED_FIND_BY_AUTHOR_NAME = "Book.findByAuthorName";
    public static final String GRAPH_WITH_DETAILS = "Book.withDetails";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToMany
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    protected Book() {
    }

    public Book(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    void setAuthor(Author author) {
        this.author = author;
    }

    public Set<Category> getCategories() {
        return categories;
    }
}
