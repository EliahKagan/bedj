package io.github.eliahkagan.bedj;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import com.codepoetics.protonpack.StreamUtils;
import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.embedding.Embedding;
import com.theokanning.openai.embedding.EmbeddingRequest;

public class Main {
    public static void main(String[] args) throws IOException {
        var embeddings = getEmbeddings(List.of(
            "French kissing experts",
            "kissing French experts",
            "experts in French kissing",
            "kissing French people who are experts",
            "The canine shall never perish from the earth.",
            "Somewhere in the world, there will always be dogs.",
            "Quelque part au monde, il y aura toujours des chiens.",
            "The tabular data have been sequestered from the public."
        ));

        for (var i = 0; i < embeddings.size(); ++i) {
            for (var j = 0; j < embeddings.size(); ++j) {
                var similarity = dot(embeddings.get(i), embeddings.get(j));
                System.out.format(" %10f", similarity);
            }
            System.out.println();
        }
    }

    private static List<List<Double>>
    getEmbeddings(List<String> texts) throws IOException {
        var service = new OpenAiService(getApiKey());

        var request = EmbeddingRequest.builder()
            .model("text-embedding-ada-002")
            .input(texts)
            .build();

        return service.createEmbeddings(request)
            .getData()
            .stream()
            .sorted(Comparator.comparing(Embedding::getIndex))
            .map(Embedding::getEmbedding)
            .toList();
    }

    private static String getApiKey() throws IOException {
        var key = System.getenv("OPENAI_API_KEY");

        if (key == null || key.isBlank()) {
            var path = Path.of(".api_key");
            key = Files.readString(path, StandardCharsets.UTF_8);
        }

        return key.strip();
    }

    private static double dot(List<Double> xs, List<Double> ys) {
        return StreamUtils.zip(xs.stream(), ys.stream(), (x, y) -> x * y)
            .reduce(0.0, Double::sum);
    }
}
