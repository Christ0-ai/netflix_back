package com.netflix.api.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private int rating;
  private String comment;
  private LocalDate creationDate;

  @ManyToOne(fetch = FetchType.LAZY)
  private Movie movie;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;
}
