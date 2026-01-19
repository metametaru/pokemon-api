package com.example.pokemon_api.pokemon.model;

import java.util.List;

public record PokemonApiResponse(
        int id,
        String name,
        int height,
        int weight,
        NamedApiResource species,
        List<TypeSlot> types,
        List<AbilitySlot> abilities
) {}
