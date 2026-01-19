package com.example.pokemon_api.pokemon.mapper;

import com.example.pokemon_api.pokemon.PokeApiClient;
import com.example.pokemon_api.pokemon.model.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PokemonMapper {

//    public PokemonView toView(Pokemon pokemon) {
//        return new PokemonView(
//                pokemon.id(),
//                translatePokemonName(pokemon.name()),
//                pokemon.types().stream()
//                        .map(t -> translateType(t.type().name()))
//                        .toList(),
//                pokemon.abilities().stream()
//                        .map(a -> translateAbility(a.ability().name()))
//                        .toList()
//        );
//    }
private final PokeApiClient client;

    public PokemonMapper(PokeApiClient client) {
    this.client = client;
}

    public PokemonView toView(Pokemon pokemon, String japaneseName) {

        var types = pokemon.types().stream()
                .map(slot -> {
                    var typeDetail = client.fetchType(slot.type().url());
                    return extractJapaneseName(typeDetail.names());
                })
                .toList();

        var abilities = pokemon.abilities().stream()
                .map(slot -> {
                    var abilityDetail = client.fetchAbility(slot.ability().url());
                    return extractJapaneseName(abilityDetail.names());
                })
                .toList();

        return new PokemonView(
                pokemon.id(),
                japaneseName,
                types,
                abilities
        );
    }

    private String translatePokemonName(String name) {
        return name; // 仮（あとで日本語化）
    }

    private String translateType(String type) {
        return type;
    }

    private String translateAbility(String ability) {
        return ability;
    }

    private String extractJapaneseName(List<Name> names) {
        return names.stream()
                .filter(n -> "ja-Hrkt".equals(n.language().name()))
                .map(Name::name)
                .findFirst()
                .orElseThrow();
    }
}