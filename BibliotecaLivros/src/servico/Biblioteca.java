package servico;

import java.util.ArrayList;
import java.util.List;

import modelo.Livro;
import modelo.Usuario;

/**
 * PADRAO SINGLETON (criacional)
 *
 * Problema: o acervo de livros e a lista de usuarios precisam existir em
 * UM lugar so. Se fosse possivel criar duas Bibliotecas, um livro poderia
 * aparecer emprestado em uma e disponivel na outra (dados inconsistentes).
 *
 * Solucao: o construtor e PRIVADO (ninguem consegue fazer "new Biblioteca()")
 * e a unica instancia e acessada pelo metodo estatico getInstancia().
 */
public class Biblioteca {

    private static Biblioteca instancia; // guarda a unica instancia

    private List<Livro> livros;
    private List<Usuario> usuarios;

    private Biblioteca() {
        livros = new ArrayList<>();
        usuarios = new ArrayList<>();
    }

    public static Biblioteca getInstancia() {
        if (instancia == null) {          // so cria na primeira vez que alguem pede
            instancia = new Biblioteca();
        }
        return instancia;                 // nas proximas vezes, devolve a mesma
    }

    // SOBRECARGA: dois metodos com o mesmo nome, mas parametros diferentes

    public void adicionarLivro(Livro livro) {
        livros.add(livro);
    }

    public void adicionarLivro(List<Livro> novosLivros) {
        for (Livro livro : novosLivros) {
            adicionarLivro(livro);
        }
    }

    public void adicionarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public Livro buscarLivroPorId(int id) {
        for (Livro livro : livros) {
            if (livro.getId() == id) {
                return livro;
            }
        }
        return null; // nao encontrou
    }

    public Usuario buscarUsuarioPorMatricula(String matricula) {
        for (Usuario usuario : usuarios) {
            if (usuario.getMatricula().equalsIgnoreCase(matricula)) {
                return usuario;
            }
        }
        return null; // nao encontrou
    }

    // Devolvem COPIAS das listas: quem esta de fora nao consegue adicionar
    // ou remover itens sem passar pela Biblioteca (encapsulamento)

    public List<Livro> getLivros() {
        return new ArrayList<>(livros);
    }

    public List<Usuario> getUsuarios() {
        return new ArrayList<>(usuarios);
    }
}
