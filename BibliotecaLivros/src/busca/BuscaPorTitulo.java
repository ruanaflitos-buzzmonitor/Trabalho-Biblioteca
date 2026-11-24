package busca;

import modelo.Livro;

/**
 * Estrategia concreta: procura o texto digitado no TITULO do livro
 * (sem diferenciar letras maiusculas de minusculas).
 */
public class BuscaPorTitulo implements BuscaStrategy {

    @Override
    public boolean corresponde(Livro livro, String termo) {
        return livro.getTitulo().toLowerCase().contains(termo.toLowerCase());
    }
}
