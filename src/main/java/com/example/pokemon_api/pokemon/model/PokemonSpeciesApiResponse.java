package com.example.pokemon_api.pokemon.model;

import java.util.List;

public record PokemonSpeciesApiResponse(
        List<NameEntry> names
) {
    public record NameEntry(
            NamedApiResource language,
            String name
    ) {}
}
