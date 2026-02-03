package com.example.pokemon_api.pokemon;

import com.example.pokemon_api.pokemon.model.PokemonListResponse;
import com.example.pokemon_api.pokemon.model.PokemonSpeciesApiResponse;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PokemonNameCache {

    private static final Logger log = LoggerFactory.getLogger(PokemonNameCache.class);

    private final PokeApiClient pokeApiClient;

    // ID → 日本語名のキャッシュ
    private final Map<Integer, String> nameCache = new ConcurrentHashMap<>();

    // ID → 英語名のキャッシュ（部分一致検索用）
    private final Map<Integer, String> englishNameCache = new ConcurrentHashMap<>();

    private volatile boolean initialized = false;

    public PokemonNameCache(PokeApiClient pokeApiClient) {
        this.pokeApiClient = pokeApiClient;
    }

    @PostConstruct
    public void init() {
        // バックグラウンドでキャッシュを構築
        new Thread(this::buildCache).start();
    }

    private void buildCache() {
        log.info("ポケモン名キャッシュの構築を開始...");

        try {
            // 全ポケモンリストを取得
            PokemonListResponse list = pokeApiClient.fetchPokemonList(1010, 0);

            for (var result : list.results()) {
                try {
                    // URLからIDを抽出
                    String[] parts = result.url().split("/");
                    int id = Integer.parseInt(parts[parts.length - 1]);

                    // 英語名を保存
                    englishNameCache.put(id, result.name());

                    // 日本語名を取得
                    PokemonSpeciesApiResponse species = pokeApiClient.fetchPokemonSpecies(id);
                    String jpName = species.names().stream()
                            .filter(n -> "ja-Hrkt".equalsIgnoreCase(n.language().name())
                                    || "ja-hrkt".equalsIgnoreCase(n.language().name()))
                            .map(PokemonSpeciesApiResponse.NameEntry::name)
                            .findFirst()
                            .orElse(result.name());

                    nameCache.put(id, jpName);

                    if (id % 100 == 0) {
                        log.info("キャッシュ構築中... {}/1010", id);
                    }
                } catch (Exception e) {
                    log.warn("ID {} のキャッシュ構築に失敗: {}", result.name(), e.getMessage());
                }
            }

            initialized = true;
            log.info("ポケモン名キャッシュの構築完了: {}件", nameCache.size());

        } catch (Exception e) {
            log.error("キャッシュ構築エラー", e);
        }
    }

    /**
     * キャッシュが構築済みかどうか
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * 部分一致で検索し、マッチしたポケモンIDのリストを返す
     */
    public List<Integer> search(String query, int limit) {
        String lowerQuery = query.toLowerCase();

        return nameCache.entrySet().stream()
                .filter(entry -> {
                    String jpName = entry.getValue().toLowerCase();
                    String enName = englishNameCache.getOrDefault(entry.getKey(), "").toLowerCase();
                    return jpName.contains(lowerQuery) || enName.contains(lowerQuery);
                })
                .map(Map.Entry::getKey)
                .limit(limit)
                .toList();
    }

    /**
     * IDから日本語名を取得
     */
    public String getJapaneseName(int id) {
        return nameCache.getOrDefault(id, "???");
    }

    /**
     * キャッシュされているポケモン数
     */
    public int size() {
        return nameCache.size();
    }
}
