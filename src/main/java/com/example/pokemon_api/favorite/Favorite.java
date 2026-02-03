package com.example.pokemon_api.favorite;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "favorites", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "pokemon_id"})
})
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "pokemon_id", nullable = false)
    private Integer pokemonId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public Favorite() {
    }

    public Favorite(String userId, Integer pokemonId) {
        this.userId = userId;
        this.pokemonId = pokemonId;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public Integer getPokemonId() {
        return pokemonId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
