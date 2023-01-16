package io.github.eliahkagan.bedj;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.embedding.Embedding;
import com.theokanning.openai.embedding.EmbeddingRequest;

public class Main {
    public static void main(String[] args) throws IOException {
        var service = new OpenAiService(getApiKey());

        var texts = List.of(
            "French kissing experts",
            "kissing French experts",
            "The canine shall never perish from the earth.",
            "Somewhere in the world, there will always be dogs.");

        var embeddings = getEmbeddings(texts);
    }

    private static List<List<Double>>
    getEmbeddings(List<String> texts) throws IOException {
        var service = new OpenAiService(getApiKey());

        var request = EmbeddingRequest.builder()
            .model("text-embedding-ada-002")
            .input(texts)
            .build();

        var embeddings = service.createEmbeddings(request)
            .getData()
            .stream()
            .sorted(Comparator.comparing(Embedding::getIndex))
            .map(Embedding::getEmbedding)
            .toList();

        System.out.println(embeddings);
    }

    private static String getApiKey() throws IOException {
        var key = System.getenv("OPENAI_API_KEY");

        if (key == null || key.isBlank()) {
            var path = Path.of(".api_key");
            key = Files.readString(path, StandardCharsets.UTF_8);
        }

        return key.strip();
    }

    private static double dot(List<Double> vector1, List<Double> vector2) {
        if (vector1.size() != vector2.size()) {
            var message = String.format(
                "can't dot vectors of different dimension (%d and %d)",
                vector1.size(),
                vector2.size());

            throw new IllegalArgumentException(message);
        }

        return IntStream.range(0, vector1.size())
            .mapToDouble(index -> vector1.get(index) * vector2.get(index))
            .sum();
    }
}
