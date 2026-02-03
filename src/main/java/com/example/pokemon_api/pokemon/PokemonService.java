package com.example.pokemon_api.pokemon;

import com.example.pokemon_api.pokemon.mapper.PokemonMapper;
import com.example.pokemon_api.pokemon.model.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PokemonService {

    private final PokeApiClient pokeApiClient;
    private final PokemonMapper pokemonMapper;
    private final PokemonNameCache nameCache;

    public PokemonService(
            PokeApiClient pokeApiClient,
            PokemonMapper pokemonMapper,
            PokemonNameCache nameCache
    ) {
        this.pokeApiClient = pokeApiClient;
        this.pokemonMapper = pokemonMapper;
        this.nameCache = nameCache;
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

    public String translatePokemonName(int pokemonId, String fallbackName) {

        PokemonSpeciesApiResponse species =
                pokeApiClient.fetchPokemonSpecies(pokemonId);

        return species.names().stream()
                .filter(n -> "ja-hrkt".equalsIgnoreCase(n.language().name()))
                .map(PokemonSpeciesApiResponse.NameEntry::name)
                .findFirst()
                .orElse(fallbackName);
    }

    /**
     * ポケモン名で部分一致検索（キャッシュ使用）
     */
    public List<PokemonView> searchPokemon(String query, int limit) {
        // キャッシュが未構築の場合はエラー
        if (!nameCache.isInitialized()) {
            throw new IllegalStateException("検索の準備中です。しばらくお待ちください。");
        }

        // キャッシュから部分一致検索でIDリストを取得
        List<Integer> matchedIds = nameCache.search(query, limit);

        // マッチしたIDのポケモン詳細だけ取得
        return matchedIds.stream()
                .map(id -> {
                    Pokemon pokemon = pokeApiClient.fetchPokemonById(id);
                    String jpName = nameCache.getJapaneseName(id);
                    return pokemonMapper.toView(pokemon, jpName);
                })
                .toList();
    }
}