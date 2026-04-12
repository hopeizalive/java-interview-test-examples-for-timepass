package com.example.springdata.interview.sddata.l14;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sd14_blog_post")
@NamedEntityGraph(
        name = Sd14BlogPost.WITH_COMMENTS,
        attributeNodes = @NamedAttributeNode("comments"))
public class Sd14BlogPost {

    public static final String WITH_COMMENTS = "Sd14BlogPost.withComments";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Sd14Comment> comments = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Sd14Comment> getComments() {
        return comments;
    }

    public void addComment(Sd14Comment c) {
        comments.add(c);
        c.setPost(this);
    }
}
