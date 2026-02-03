package com.example.pokemon_api.pokemon;

import com.example.pokemon_api.pokemon.model.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URI;

@Component
public class PokeApiClient {

    private final RestClient restClient;

    public PokeApiClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://pokeapi.co/api/v2")
                .build();
    }

    /** 一覧 */
    public PokemonListResponse fetchPokemonList(int limit, int offset) {
        return restClient.get()
                .uri(uri -> uri
                        .path("/pokemon")
                        .queryParam("limit", limit)
                        .queryParam("offset", offset)
                        .build())
                .retrieve()
                .body(PokemonListResponse.class);
    }

    /** 詳細（URL指定） */
    public Pokemon fetchPokemon(String url) {
        return restClient.get()
                .uri(url)
                .retrieve()
                .body(Pokemon.class);
    }

    /** 詳細（ID指定） */
    public Pokemon fetchPokemonById(int id) {
        return restClient.get()
                .uri("/pokemon/{id}", id)
                .retrieve()
                .body(Pokemon.class);
    }
    //エンドポイント
    public PokemonSpeciesApiResponse fetchPokemonSpecies(int id) {
        return restClient.get()
                .uri("/pokemon-species/{id}", id)
                .retrieve()
                .body(PokemonSpeciesApiResponse.class);
    }

    @Cacheable(cacheNames = "abilities")
    public AbilityApiResponse fetchAbility(String url) {
        return restClient.get()
                .uri(URI.create(url)) // ⭐ ここがポイント
                .retrieve()
                .body(AbilityApiResponse.class);
    }

    @Cacheable(cacheNames = "types")
    public TypeApiResponse fetchType(String url) {
        return restClient.get()
                .uri(URI.create(url))
                .retrieve()
                .body(TypeApiResponse.class);
    }
}