package servico;

import java.util.ArrayList;
import java.util.List;

import busca.BuscaStrategy;
import fabrica.LivroFactory;
import integracao.FonteDeLivros;
import modelo.Aluno;
import modelo.Emprestimo;
import modelo.Livro;
import modelo.Professor;
import modelo.Usuario;

/**
 * PADRAO FACADE (estrutural)
 *
 * Problema: cada operacao da biblioteca envolve varias classes. Emprestar
 * um livro, por exemplo, exige achar o usuario e o livro na Biblioteca,
 * conferir as regras, marcar o livro como emprestado e registrar o
 * emprestimo no usuario. Se o menu fizesse tudo isso, ele dependeria de
 * todas as classes do sistema.
 *
 * Solucao: esta classe oferece uma "fachada" com metodos simples
 * (emprestarLivro, devolverLivro, buscarLivros...) e esconde os detalhes.
 * O menu conversa so com ela.
 *
 * GRASP: tambem e o CONTROLADOR (Controller) do sistema - recebe os pedidos
 * que vem da interface (menu) e repassa o trabalho para as classes certas.
 */
public class BibliotecaFacade {

    private Biblioteca biblioteca;
    private LivroFactory fabrica;

    public BibliotecaFacade() {
        this.biblioteca = Biblioteca.getInstancia(); // Singleton
        this.fabrica = new LivroFactory();
    }

    // ==================== LIVROS ====================

    // Recebe a interface FonteDeLivros, entao funciona com qualquer fonte
    // (no nosso caso, o adaptador do catalogo antigo)
    public int importarLivros(FonteDeLivros fonte) {
        List<Livro> livrosImportados = fonte.carregarLivros();
        biblioteca.adicionarLivro(livrosImportados);
        return livrosImportados.size();
    }

    public String cadastrarLivro(String tipo, String titulo, String autor, int ano, int informacaoExtra) {
        if (titulo.isEmpty() || autor.isEmpty()) {
            return "Erro: titulo e autor sao obrigatorios.";
        }
        Livro livro = fabrica.criarLivro(tipo, titulo, autor, ano, informacaoExtra); // Factory Method
        if (livro == null) {
            return "Erro: tipo de livro desconhecido: " + tipo + ".";
        }
        biblioteca.adicionarLivro(livro);
        return "Livro cadastrado: " + livro;
    }

    public List<Livro> listarLivros() {
        return biblioteca.getLivros();
    }

    // Strategy: o jeito de percorrer a lista e sempre o mesmo,
    // so muda a estrategia que decide se o livro "corresponde"
    public List<Livro> buscarLivros(String termo, BuscaStrategy estrategia) {
        List<Livro> encontrados = new ArrayList<>();
        for (Livro livro : biblioteca.getLivros()) {
            if (estrategia.corresponde(livro, termo)) {
                encontrados.add(livro);
            }
        }
        return encontrados;
    }

    // ==================== USUARIOS ====================

    public String cadastrarAluno(String nome, String matricula) {
        return cadastrarUsuario(new Aluno(nome, matricula));
    }

    public String cadastrarProfessor(String nome, String matricula) {
        return cadastrarUsuario(new Professor(nome, matricula));
    }

    // Recebe um Usuario qualquer (Aluno ou Professor): polimorfismo
    private String cadastrarUsuario(Usuario usuario) {
        if (usuario.getNome().isEmpty() || usuario.getMatricula().isEmpty()) {
            return "Erro: nome e matricula sao obrigatorios.";
        }
        if (biblioteca.buscarUsuarioPorMatricula(usuario.getMatricula()) != null) {
            return "Erro: ja existe um usuario com a matricula " + usuario.getMatricula() + ".";
        }
        biblioteca.adicionarUsuario(usuario);
        return usuario.getTipo() + " cadastrado: " + usuario.getNome()
                + " (matricula " + usuario.getMatricula() + ").";
    }

    public List<Usuario> listarUsuarios() {
        return biblioteca.getUsuarios();
    }

    public String consultarAvisos(String matricula) {
        Usuario usuario = biblioteca.buscarUsuarioPorMatricula(matricula);
        if (usuario == null) {
            return "Erro: usuario nao encontrado.";
        }
        List<String> avisos = usuario.getAvisos();
        if (avisos.isEmpty()) {
            return usuario.getNome() + " nao tem avisos.";
        }
        String texto = "Avisos de " + usuario.getNome() + ":";
        for (String aviso : avisos) {
            texto = texto + "\n  - " + aviso;
        }
        return texto;
    }

    // ==================== EMPRESTIMOS ====================

    public String emprestarLivro(String matricula, int idLivro) {
        Usuario usuario = biblioteca.buscarUsuarioPorMatricula(matricula);
        Livro livro = biblioteca.buscarLivroPorId(idLivro);

        String erro = verificarUsuarioELivro(usuario, livro);
        if (erro != null) {
            return erro;
        }
        if (!livro.isDisponivel()) {
            return "O livro \"" + livro.getTitulo() + "\" ja esta emprestado. "
                    + "Entre na lista de espera (opcao 8) para ser avisado quando ele voltar.";
        }
        if (!usuario.podePegarEmprestado()) {
            return "Erro: " + usuario.getNome() + " ja atingiu o limite de "
                    + usuario.getLimiteDeLivros() + " livros emprestados.";
        }

        livro.emprestar();
        Emprestimo emprestimo = usuario.registrarEmprestimo(livro);
        return "Emprestimo realizado: \"" + livro.getTitulo() + "\" para " + usuario.getNome()
                + ". Devolver ate " + emprestimo.getDataDevolucaoFormatada() + ".";
    }

    public String devolverLivro(String matricula, int idLivro) {
        Usuario usuario = biblioteca.buscarUsuarioPorMatricula(matricula);
        Livro livro = biblioteca.buscarLivroPorId(idLivro);

        String erro = verificarUsuarioELivro(usuario, livro);
        if (erro != null) {
            return erro;
        }
        if (!usuario.estaComLivro(livro)) {
            return "Erro: " + usuario.getNome() + " nao esta com o livro \"" + livro.getTitulo() + "\".";
        }

        int pessoasEsperando = livro.getTamanhoListaDeEspera();
        usuario.registrarDevolucao(livro);
        livro.devolver(); // Observer: aqui o livro avisa a lista de espera

        String mensagem = "Devolucao registrada: \"" + livro.getTitulo() + "\".";
        if (pessoasEsperando > 0) {
            mensagem = mensagem + " Aviso enviado para " + pessoasEsperando + " usuario(s) da lista de espera.";
        }
        return mensagem;
    }

    public String entrarNaListaDeEspera(String matricula, int idLivro) {
        Usuario usuario = biblioteca.buscarUsuarioPorMatricula(matricula);
        Livro livro = biblioteca.buscarLivroPorId(idLivro);

        String erro = verificarUsuarioELivro(usuario, livro);
        if (erro != null) {
            return erro;
        }
        if (livro.isDisponivel()) {
            return "O livro \"" + livro.getTitulo() + "\" esta disponivel. Pode pegar emprestado agora!";
        }
        if (usuario.estaComLivro(livro)) {
            return "Erro: " + usuario.getNome() + " ja esta com esse livro.";
        }
        if (!livro.adicionarObservador(usuario)) { // Observer: usuario passa a observar o livro
            return usuario.getNome() + " ja esta na lista de espera desse livro.";
        }
        return usuario.getNome() + " entrou na lista de espera de \"" + livro.getTitulo()
                + "\" e sera avisado(a) quando ele for devolvido.";
    }

    // Confere se o usuario e o livro existem.
    // Devolve a mensagem de erro, ou null se esta tudo certo.
    private String verificarUsuarioELivro(Usuario usuario, Livro livro) {
        if (usuario == null) {
            return "Erro: usuario nao encontrado.";
        }
        if (livro == null) {
            return "Erro: livro nao encontrado.";
        }
        return null;
    }
}
