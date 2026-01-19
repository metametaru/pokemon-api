package com.example.pokemon_api.pokemon.model;

import java.util.List;

public record PokemonView(
        int id,
        String nameJa,
        //int height,
       // int weight,
        List<String> types,
        List<String> abilities
) {
}
