package com.example.pokemon_api.favorite;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public List<Integer> getFavorites(String userId) {
        return favoriteRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(Favorite::getPokemonId)
                .toList();
    }

    public boolean isFavorite(String userId, Integer pokemonId) {
        return favoriteRepository.existsByUserIdAndPokemonId(userId, pokemonId);
    }

    @Transactional
    public void addFavorite(String userId, Integer pokemonId) {
        if (!favoriteRepository.existsByUserIdAndPokemonId(userId, pokemonId)) {
            favoriteRepository.save(new Favorite(userId, pokemonId));
        }
    }

    @Transactional
    public void removeFavorite(String userId, Integer pokemonId) {
        favoriteRepository.deleteByUserIdAndPokemonId(userId, pokemonId);
    }
}
