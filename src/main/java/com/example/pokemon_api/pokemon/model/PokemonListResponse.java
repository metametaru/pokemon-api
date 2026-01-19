package com.example.pokemon_api.pokemon.model;

import java.util.List;

public record PokemonListResponse(
        List<NamedApiResource> results
) {}
