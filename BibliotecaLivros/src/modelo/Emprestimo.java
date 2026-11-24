package modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Registra um emprestimo: qual livro foi emprestado, em que dia
 * e ate quando ele deve ser devolvido.
 */
public class Emprestimo {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Livro livro;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucaoPrevista;

    public Emprestimo(Livro livro, int prazoEmDias) {
        this.livro = livro;
        this.dataEmprestimo = LocalDate.now();
        this.dataDevolucaoPrevista = dataEmprestimo.plusDays(prazoEmDias);
    }

    public Livro getLivro() {
        return livro;
    }

    public String getDataDevolucaoFormatada() {
        return dataDevolucaoPrevista.format(FORMATO_DATA);
    }

    @Override
    public String toString() {
        return "[" + livro.getId() + "] " + livro.getTitulo()
                + " | emprestado em " + dataEmprestimo.format(FORMATO_DATA)
                + " | devolver ate " + getDataDevolucaoFormatada();
    }
}
