package com.example.pokemon_api.pokemon;

import com.example.pokemon_api.pokemon.mapper.PokemonMapper;
import com.example.pokemon_api.pokemon.model.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PokemonService {

    private final PokeApiClient pokeApiClient;
    private final PokemonMapper pokemonMapper;

    public PokemonService(
            PokeApiClient pokeApiClient,
            PokemonMapper pokemonMapper
    ) {
        this.pokeApiClient = pokeApiClient;
        this.pokemonMapper = pokemonMapper;
    }

    /**
     * ポケモン一覧取得（ページング対応）
     */
    public List<PokemonView> getPokemonList(int limit, int offset) {

        PokemonListResponse list =
                pokeApiClient.fetchPokemonList(limit, offset);

        return list.results().stream()
                .map(r -> {
                    Pokemon pokemon = pokeApiClient.fetchPokemon(r.url());

                    String jpName = translatePokemonName(
                            pokemon.id(),
                            pokemon.name()
                    );

                    return pokemonMapper.toView(pokemon, jpName);
                })
                .toList();
    }

    /**
     * 単体取得（将来用）
     */
//    public PokemonView getPokemonById(int id) {
//
//        Pokemon pokemon =
//                pokeApiClient.fetchPokemonById(id);
//
//        return pokemonMapper.toView(pokemon);
//    }

    // ★ 追加
    public String translatePokemonName(int pokemonId, String fallbackName) {

        PokemonSpeciesApiResponse species =
                pokeApiClient.fetchPokemonSpecies(pokemonId);

        return species.names().stream()
                .filter(n -> "ja-Hrkt".equals(n.language().name()))
                .map(PokemonSpeciesApiResponse.NameEntry::name)
                .findFirst()
                .orElse(fallbackName);
    }
}