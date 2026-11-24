package modelo;

import java.util.ArrayList;
import java.util.List;

import observador.Observador;

/**
 * Representa uma pessoa cadastrada na biblioteca.
 *
 * CLASSE ABSTRATA: cada tipo de usuario (Aluno, Professor) define o seu
 * limite de livros e o seu prazo de devolucao (POLIMORFISMO).
 *
 * Implementa Observador (PADRAO OBSERVER): o usuario pode entrar na lista
 * de espera de um livro e recebe um aviso quando ele for devolvido.
 */
public abstract class Usuario implements Observador {

    private String nome;
    private String matricula;
    private List<Emprestimo> emprestimos; // livros que estao com o usuario agora
    private List<String> avisos;          // avisos recebidos pelo Observer

    // protected: so as subclasses (Aluno e Professor) usam este construtor
    protected Usuario(String nome, String matricula) {
        this.nome = nome;
        this.matricula = matricula;
        this.emprestimos = new ArrayList<>();
        this.avisos = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public String getMatricula() {
        return matricula;
    }

    // Devolve uma COPIA da lista: quem esta de fora pode ler, mas nao
    // consegue adicionar ou remover emprestimos por conta propria (encapsulamento)
    public List<Emprestimo> getEmprestimos() {
        return new ArrayList<>(emprestimos);
    }

    public List<String> getAvisos() {
        return new ArrayList<>(avisos);
    }

    // ---------- Regras de emprestimo ----------
    // Quem responde e o proprio usuario, porque e ele que conhece os seus
    // emprestimos e o seu limite (GRASP: Especialista na Informacao).

    public boolean podePegarEmprestado() {
        return emprestimos.size() < getLimiteDeLivros();
    }

    public boolean estaComLivro(Livro livro) {
        return buscarEmprestimo(livro) != null;
    }

    // GRASP Criador: o usuario guarda os seus emprestimos e conhece o seu
    // prazo, entao e ele quem cria o objeto Emprestimo
    public Emprestimo registrarEmprestimo(Livro livro) {
        Emprestimo emprestimo = new Emprestimo(livro, getPrazoEmDias());
        emprestimos.add(emprestimo);
        return emprestimo;
    }

    public void registrarDevolucao(Livro livro) {
        Emprestimo emprestimo = buscarEmprestimo(livro);
        if (emprestimo != null) {
            emprestimos.remove(emprestimo);
        }
    }

    private Emprestimo buscarEmprestimo(Livro livro) {
        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo.getLivro().getId() == livro.getId()) {
                return emprestimo;
            }
        }
        return null;
    }

    // ---------- Observer: o que acontece quando o usuario e avisado ----------

    @Override
    public void notificar(String mensagem) {
        avisos.add(mensagem);
    }

    // ---------- Metodos abstratos: cada tipo de usuario responde diferente ----------

    public abstract String getTipo();

    public abstract int getLimiteDeLivros();

    public abstract int getPrazoEmDias();

    @Override
    public String toString() {
        return getTipo() + ": " + nome + " | matricula " + matricula
                + " | livros: " + emprestimos.size() + " de " + getLimiteDeLivros()
                + " | avisos: " + avisos.size();
    }
}
