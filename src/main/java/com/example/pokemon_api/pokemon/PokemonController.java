package com.example.pokemon_api.pokemon;

import com.example.pokemon_api.pokemon.mapper.PokemonMapper;
import com.example.pokemon_api.pokemon.model.PokemonView;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pokemon")
@CrossOrigin(origins = "http://localhost:3000") // Next.js
public class PokemonController {

//    private final PokemonService pokemonService;
//
//    public PokemonController(PokemonService pokemonService) {
//        this.pokemonService = pokemonService;
//    }
//
//    @GetMapping
//    public String getPokemonList(
//            @RequestParam(defaultValue = "20") int limit,
//            @RequestParam(defaultValue = "0") int offset
//    ) {
//        return pokemonService.getPokemonList(limit, offset);
//    }

    private final PokemonService pokemonService;

    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping
    public List<PokemonView> list(
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset
    ) {
        return pokemonService.getPokemonList(limit, offset);
    }
}
