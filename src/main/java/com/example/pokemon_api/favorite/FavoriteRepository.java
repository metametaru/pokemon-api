package com.example.pokemon_api.favorite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findByUserIdOrderByCreatedAtDesc(String userId);

    Optional<Favorite> findByUserIdAndPokemonId(String userId, Integer pokemonId);

    boolean existsByUserIdAndPokemonId(String userId, Integer pokemonId);

    void deleteByUserIdAndPokemonId(String userId, Integer pokemonId);
}
