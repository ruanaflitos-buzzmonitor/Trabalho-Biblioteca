package observador;

/**
 * PADRAO OBSERVER (comportamental) - papel de "Observador".
 *
 * Quem implementa esta interface pode ser AVISADO quando acontece algo
 * com o objeto que ele esta observando.
 *
 * No sistema: um Usuario observa um Livro que esta emprestado (entra na
 * lista de espera). Quando o livro e devolvido, o Livro chama o metodo
 * notificar() de cada usuario da lista.
 *
 * Por ser uma interface, o Livro nao precisa conhecer a classe Usuario:
 * ele so sabe que tem uma lista de "Observadores" (baixo acoplamento).
 */
public interface Observador {

    void notificar(String mensagem);
}
