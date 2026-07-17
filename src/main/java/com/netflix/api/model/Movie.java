package com.netflix.api.model;

import com.netflix.api.model.enums.EGenre;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String title;
  private String description;

  @Column(name = "release_date")
  private LocalDate releaseDate;

  @Enumerated(EnumType.STRING)
  private EGenre genre;

  @Column(name = "poster_path")
  private String posterPath;

  @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Review> reviews = new ArrayList<>();

  public Movie(
      String title, String description, LocalDate releaseDate, EGenre genre, String posterPath) {
    this.title = title;
    this.description = description;
    this.releaseDate = releaseDate;
    this.genre = genre;
    this.posterPath = posterPath;
  }
}
