package com.example.pokemon_api.pokemon;

import com.example.pokemon_api.pokemon.model.PokemonView;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pokemon")
@CrossOrigin(origins = "http://localhost:3000") // Next.js
public class PokemonController {

    private final PokemonService pokemonService;
    private final PokemonNameCache nameCache;

    public PokemonController(PokemonService pokemonService, PokemonNameCache nameCache) {
        this.pokemonService = pokemonService;
        this.nameCache = nameCache;
    }

    @GetMapping
    public List<PokemonView> list(
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset
    ) {
        return pokemonService.getPokemonList(limit, offset);
    }

    @GetMapping("/search")
    public List<PokemonView> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "20") int limit
    ) {
        return pokemonService.searchPokemon(q, limit);
    }

    @GetMapping("/cache-status")
    public Map<String, Object> getCacheStatus() {
        return Map.of(
                "ready", nameCache.isInitialized(),
                "count", nameCache.size()
        );
    }
}
