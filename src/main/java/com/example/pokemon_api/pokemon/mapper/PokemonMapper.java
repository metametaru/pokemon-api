package com.example.pokemon_api.pokemon.mapper;

import com.example.pokemon_api.pokemon.PokeApiClient;
import com.example.pokemon_api.pokemon.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PokemonMapper {

    private static final Logger log = LoggerFactory.getLogger(PokemonMapper.class);

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

        var imageUrl = pokemon.sprites() != null ? pokemon.sprites().frontDefault() : null;

        return new PokemonView(
                pokemon.id(),
                japaneseName,
                pokemon.height(),
                pokemon.weight(),
                types,
                abilities,
                imageUrl
        );
    }

    private String extractJapaneseName(List<Name> names) {
        // デバッグ: 利用可能な言語を出力
        var availableLanguages = names.stream()
                .map(n -> n.language() != null ? n.language().name() : "null")
                .toList();
        log.debug("利用可能な言語: {}", availableLanguages);

        var japaneseName = names.stream()
                .filter(n -> n.language() != null && "ja-hrkt".equalsIgnoreCase(n.language().name()))
                .map(Name::name)
                .findFirst();

        if (japaneseName.isPresent()) {
            return japaneseName.get();
        }

        var englishName = names.stream()
                .filter(n -> n.language() != null && "en".equals(n.language().name()))
                .map(Name::name)
                .findFirst()
                .orElse("不明");

        log.warn("日本語名が見つかりません。英語名を使用: {} (利用可能: {})", englishName, availableLanguages);
        return englishName;
    }
}