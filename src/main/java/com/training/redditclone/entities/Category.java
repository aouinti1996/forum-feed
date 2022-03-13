package com.training.redditclone.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.time.Instant;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "The Category Name is required")
    private String name;
    @NotBlank(message = "The Category's description is required")
    private String description;
    @OneToMany(fetch = LAZY)
    private List<Article> articles;
    private Instant createdDate;
    @ManyToOne(fetch = LAZY)
    private User user;
}
