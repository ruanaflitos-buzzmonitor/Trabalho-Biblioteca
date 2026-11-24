package busca;

import modelo.Livro;

/**
 * Estrategia concreta: procura o texto digitado no nome do AUTOR do livro
 * (sem diferenciar letras maiusculas de minusculas).
 */
public class BuscaPorAutor implements BuscaStrategy {

    @Override
    public boolean corresponde(Livro livro, String termo) {
        return livro.getAutor().toLowerCase().contains(termo.toLowerCase());
    }
}
