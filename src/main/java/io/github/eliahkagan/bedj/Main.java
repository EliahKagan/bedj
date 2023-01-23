// Copyright (c) 2023 Eliah Kagan
//
// Permission to use, copy, modify, and/or distribute this software for any
// purpose with or without fee is hereby granted.
//
// THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH
// REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY
// AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
// INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM
// LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR
// OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
// PERFORMANCE OF THIS SOFTWARE.

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

        // Show the table of similarities.
        for (var row : embeddings) {
            for (var col : embeddings) {
                System.out.format(" %12.8f", dot(row, col));
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
