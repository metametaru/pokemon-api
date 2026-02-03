package com.example.pokemon_api.favorite;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    // お気に入り一覧を取得
    @GetMapping
    public ResponseEntity<List<Integer>> getFavorites(@AuthenticationPrincipal String userId) {
        List<Integer> favorites = favoriteService.getFavorites(userId);
        return ResponseEntity.ok(favorites);
    }

    // 特定のポケモンがお気に入りかどうか確認
    @GetMapping("/{pokemonId}")
    public ResponseEntity<Map<String, Boolean>> isFavorite(
            @AuthenticationPrincipal String userId,
            @PathVariable Integer pokemonId) {
        boolean isFavorite = favoriteService.isFavorite(userId, pokemonId);
        return ResponseEntity.ok(Map.of("isFavorite", isFavorite));
    }

    // お気に入りに追加
    @PostMapping("/{pokemonId}")
    public ResponseEntity<Map<String, String>> addFavorite(
            @AuthenticationPrincipal String userId,
            @PathVariable Integer pokemonId) {
        favoriteService.addFavorite(userId, pokemonId);
        return ResponseEntity.ok(Map.of("message", "お気に入りに追加しました"));
    }

    // お気に入りから削除
    @DeleteMapping("/{pokemonId}")
    public ResponseEntity<Map<String, String>> removeFavorite(
            @AuthenticationPrincipal String userId,
            @PathVariable Integer pokemonId) {
        favoriteService.removeFavorite(userId, pokemonId);
        return ResponseEntity.ok(Map.of("message", "お気に入りから削除しました"));
    }
}
