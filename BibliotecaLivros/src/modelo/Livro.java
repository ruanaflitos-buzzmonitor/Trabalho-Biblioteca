package modelo;

import java.util.ArrayList;
import java.util.List;

import observador.Observador;

/**
 * Representa um livro do acervo.
 *
 * CLASSE ABSTRATA: nao existe um "livro generico". Todo livro e um
 * LivroFisico ou um Ebook, e cada um completa os metodos abstratos
 * getTipo() e getDetalhes() do seu jeito.
 *
 * PADRAO OBSERVER (comportamental) - papel de "Sujeito" (o observado):
 * quando o livro esta emprestado, usuarios podem entrar na lista de espera
 * dele. Quando o livro e devolvido, ele mesmo avisa todos da lista.
 */
public abstract class Livro {

    private static int proximoId = 1; // contador compartilhado para gerar os ids

    private int id;
    private String titulo;
    private String autor;
    private int ano;
    private boolean disponivel;
    private List<Observador> listaDeEspera; // os observadores deste livro

    // protected: so as subclasses (LivroFisico e Ebook) usam este construtor
    protected Livro(String titulo, String autor, int ano) {
        this.id = proximoId;
        proximoId++;
        this.titulo = titulo;
        this.autor = autor;
        this.ano = ano;
        this.disponivel = true;
        this.listaDeEspera = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getAno() {
        return ano;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void emprestar() {
        disponivel = false;
    }

    public void devolver() {
        disponivel = true;
        notificarObservadores(); // Observer: avisa quem estava esperando
    }

    // ---------- Metodos do padrao Observer ----------

    public boolean adicionarObservador(Observador observador) {
        if (listaDeEspera.contains(observador)) {
            return false; // essa pessoa ja estava na lista
        }
        listaDeEspera.add(observador);
        return true;
    }

    public int getTamanhoListaDeEspera() {
        return listaDeEspera.size();
    }

    private void notificarObservadores() {
        for (Observador observador : listaDeEspera) {
            observador.notificar("O livro \"" + titulo + "\" (id " + id + ") foi devolvido e esta disponivel!");
        }
        listaDeEspera.clear(); // todos foram avisados, entao a lista comeca de novo
    }

    // ---------- Metodos abstratos: cada tipo de livro responde do seu jeito ----------

    public abstract String getTipo();

    public abstract String getDetalhes();

    @Override
    public String toString() {
        String situacao;
        if (disponivel) {
            situacao = "Disponivel";
        } else if (listaDeEspera.isEmpty()) {
            situacao = "Emprestado";
        } else {
            situacao = "Emprestado (" + listaDeEspera.size() + " na lista de espera)";
        }
        return "[" + id + "] " + titulo + " - " + autor + " (" + ano + ") | "
                + getTipo() + ", " + getDetalhes() + " | " + situacao;
    }
}
