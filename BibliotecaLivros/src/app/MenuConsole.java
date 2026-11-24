package app;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import busca.BuscaPorAutor;
import busca.BuscaPorTitulo;
import busca.BuscaStrategy;
import fabrica.LivroFactory;
import integracao.CatalogoAntigo;
import integracao.CatalogoAntigoAdapter;
import modelo.Emprestimo;
import modelo.Livro;
import modelo.Usuario;
import servico.BibliotecaFacade;

/**
 * Menu de texto (console) do sistema.
 *
 * Responsabilidade desta classe: LER o que a pessoa digita e MOSTRAR as
 * respostas na tela. Ela nao tem regra de negocio: todas as operacoes
 * passam pela BibliotecaFacade.
 */
public class MenuConsole {

    private Scanner teclado;
    private BibliotecaFacade facade;

    public MenuConsole() {
        this.teclado = new Scanner(System.in);
        this.facade = new BibliotecaFacade();
    }

    public void iniciar() {
        carregarDadosIniciais();

        int opcao;
        do {
            mostrarMenu();
            opcao = lerInteiro("Escolha uma opcao: ", 0, 9);
            System.out.println();

            switch (opcao) {
                case 1:
                    listarLivros();
                    break;
                case 2:
                    buscarLivros();
                    break;
                case 3:
                    cadastrarLivro();
                    break;
                case 4:
                    listarUsuarios();
                    break;
                case 5:
                    cadastrarUsuario();
                    break;
                case 6:
                    emprestarLivro();
                    break;
                case 7:
                    devolverLivro();
                    break;
                case 8:
                    entrarNaListaDeEspera();
                    break;
                case 9:
                    verAvisos();
                    break;
                case 0:
                    System.out.println("Sistema encerrado. Ate logo!");
                    break;
            }
        } while (opcao != 0);

        teclado.close();
    }

    // Prepara o sistema com alguns dados, para facilitar os testes
    private void carregarDadosIniciais() {
        // PADRAO ADAPTER: os livros do sistema antigo entram no sistema novo
        int quantidade = facade.importarLivros(new CatalogoAntigoAdapter(new CatalogoAntigo()));

        facade.cadastrarAluno("Ana Souza", "A01");
        facade.cadastrarAluno("Bruno Lima", "A02");
        facade.cadastrarProfessor("Carla Mendes", "P01");

        System.out.println("==================================================");
        System.out.println("     SISTEMA DE BIBLIOTECA - CONTROLE DE LIVROS");
        System.out.println("==================================================");
        System.out.println(quantidade + " livros foram importados do catalogo antigo.");
        System.out.println("Usuarios ja cadastrados para teste:");
        for (Usuario usuario : facade.listarUsuarios()) {
            System.out.println("  " + usuario.getMatricula() + " - " + usuario.getNome()
                    + " (" + usuario.getTipo() + ")");
        }
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("------------------- MENU -------------------");
        System.out.println(" 1 - Listar livros");
        System.out.println(" 2 - Buscar livros");
        System.out.println(" 3 - Cadastrar livro");
        System.out.println(" 4 - Listar usuarios");
        System.out.println(" 5 - Cadastrar usuario");
        System.out.println(" 6 - Emprestar livro");
        System.out.println(" 7 - Devolver livro");
        System.out.println(" 8 - Entrar na lista de espera de um livro");
        System.out.println(" 9 - Ver avisos de um usuario");
        System.out.println(" 0 - Sair");
        System.out.println("--------------------------------------------");
    }

    // ==================== OPCOES DO MENU ====================

    private void listarLivros() {
        System.out.println("--- LIVROS DO ACERVO ---");
        mostrarLivros(facade.listarLivros());
    }

    private void buscarLivros() {
        System.out.println("--- BUSCAR LIVROS ---");
        System.out.println("1 - Por titulo");
        System.out.println("2 - Por autor");
        int tipoBusca = lerInteiro("Buscar por: ", 1, 2);
        String termo = lerTexto("Digite o texto da busca: ");

        // PADRAO STRATEGY: o menu escolhe a estrategia e a Facade busca com ela
        BuscaStrategy estrategia;
        if (tipoBusca == 1) {
            estrategia = new BuscaPorTitulo();
        } else {
            estrategia = new BuscaPorAutor();
        }

        System.out.println();
        System.out.println("Resultado da busca:");
        mostrarLivros(facade.buscarLivros(termo, estrategia));
    }

    private void cadastrarLivro() {
        System.out.println("--- CADASTRAR LIVRO ---");
        System.out.println("1 - Livro fisico");
        System.out.println("2 - E-book");
        int opcaoTipo = lerInteiro("Tipo do livro: ", 1, 2);
        String titulo = lerTexto("Titulo: ");
        String autor = lerTexto("Autor: ");
        int ano = lerInteiro("Ano de publicacao: ", 1, LocalDate.now().getYear());

        String tipo;
        int informacaoExtra;
        if (opcaoTipo == 1) {
            tipo = LivroFactory.FISICO;
            informacaoExtra = lerInteiro("Numero de paginas: ", 1, 10000);
        } else {
            tipo = LivroFactory.EBOOK;
            informacaoExtra = lerInteiro("Tamanho do arquivo (MB): ", 1, 2000);
        }

        System.out.println(facade.cadastrarLivro(tipo, titulo, autor, ano, informacaoExtra));
    }

    private void listarUsuarios() {
        System.out.println("--- USUARIOS ---");
        for (Usuario usuario : facade.listarUsuarios()) {
            System.out.println(usuario);
            for (Emprestimo emprestimo : usuario.getEmprestimos()) {
                System.out.println("     -> " + emprestimo);
            }
        }
    }

    private void cadastrarUsuario() {
        System.out.println("--- CADASTRAR USUARIO ---");
        System.out.println("1 - Aluno");
        System.out.println("2 - Professor");
        int opcaoTipo = lerInteiro("Tipo de usuario: ", 1, 2);
        String nome = lerTexto("Nome: ");
        String matricula = lerTexto("Matricula: ");

        if (opcaoTipo == 1) {
            System.out.println(facade.cadastrarAluno(nome, matricula));
        } else {
            System.out.println(facade.cadastrarProfessor(nome, matricula));
        }
    }

    private void emprestarLivro() {
        System.out.println("--- EMPRESTAR LIVRO ---");
        String matricula = lerTexto("Matricula do usuario: ");
        int idLivro = lerInteiro("Id do livro: ");
        System.out.println(facade.emprestarLivro(matricula, idLivro));
    }

    private void devolverLivro() {
        System.out.println("--- DEVOLVER LIVRO ---");
        String matricula = lerTexto("Matricula do usuario: ");
        int idLivro = lerInteiro("Id do livro: ");
        System.out.println(facade.devolverLivro(matricula, idLivro));
    }

    private void entrarNaListaDeEspera() {
        System.out.println("--- LISTA DE ESPERA ---");
        String matricula = lerTexto("Matricula do usuario: ");
        int idLivro = lerInteiro("Id do livro: ");
        System.out.println(facade.entrarNaListaDeEspera(matricula, idLivro));
    }

    private void verAvisos() {
        System.out.println("--- AVISOS ---");
        String matricula = lerTexto("Matricula do usuario: ");
        System.out.println(facade.consultarAvisos(matricula));
    }

    // ==================== METODOS AUXILIARES ====================

    private void mostrarLivros(List<Livro> livros) {
        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado.");
        }
        for (Livro livro : livros) {
            System.out.println(livro);
        }
    }

    private String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return teclado.nextLine().trim();
    }

    // SOBRECARGA 1: le qualquer numero inteiro (repete ate a pessoa digitar um numero)
    private int lerInteiro(String mensagem) {
        while (true) {
            String digitado = lerTexto(mensagem);
            try {
                return Integer.parseInt(digitado);
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido! Digite um numero inteiro.");
            }
        }
    }

    // SOBRECARGA 2: le um numero inteiro que precisa estar entre minimo e maximo
    private int lerInteiro(String mensagem, int minimo, int maximo) {
        int numero = lerInteiro(mensagem);
        while (numero < minimo || numero > maximo) {
            System.out.println("Digite um numero entre " + minimo + " e " + maximo + ".");
            numero = lerInteiro(mensagem);
        }
        return numero;
    }
}
