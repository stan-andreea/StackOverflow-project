package com.utcn.demo.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "content")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Content {

    public Content(Long id){
        this.id=id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Content question;

    @Column(name="title")
    private String title;

    @Column(nullable = false, name = "date_created")
    private Date dateCreated;

    @Column(nullable = false, name = "time_minutes")
    private int timeMinutes;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false, name = "text")
    private String text;


    @ElementCollection
    private List<Long> tag_ids;
}

