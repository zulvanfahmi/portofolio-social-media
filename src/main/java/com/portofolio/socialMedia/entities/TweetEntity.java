package com.portofolio.socialMedia.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tweet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TweetEntity extends BaseProperties{
    
    @Column(nullable = false, updatable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_tweet;

    @Column(nullable = false, length = 280)
    private String content;
    
    @Column(columnDefinition = "Text")
    private String imageUrls;

    // RELASI

    // Relasi ke User
    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private UserEntity user;

    // Relasi ke Tweet lain (self-join untuk reply)
    @ManyToOne
    @JoinColumn(name = "id_parentTweet")
    private TweetEntity parentTweet;

    @OneToMany(mappedBy = "parentTweet")
    private List<TweetEntity> replies = new ArrayList<>();

}
