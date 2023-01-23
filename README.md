# Text embeddings via OpenAI - a few Java examples

These are some examples of embeddings using OpenAI models.

Examples are here because I find each to be of some interest, but this is not
intended as a tutorial for how to use embeddings.

For instructive examples, see the official OpenAI repository
[**openai-cookbook**](https://github.com/openai/openai-cookbook).

This repository, *bedj*, is like [*bed*](https://github.com/EliahKagan/bed),
but this is in Java and less extensive.

## License

[0BSD](https://spdx.org/licenses/0BSD.html). See [**`LICENSE`**](LICENSE).

## Notes

The code is in
[`Main.java`](src/main/java/io/github/eliahkagan/bedj/Main.java).

It is written to assume your API key is in a file called `.api_key`. Do not
commit it to Git! The `.gitignore` file excludes it, to help avoid that.
