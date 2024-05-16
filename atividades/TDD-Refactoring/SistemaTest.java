import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SistemaTest {

    Biblioteca biblioteca;
    Livro livro;

    Usuario usuario;

    @BeforeEach
    void setUp() {
        this.biblioteca = new Biblioteca();
        this.livro = new Livro(1, "Psoft");
        this.biblioteca.cadastrarLivro(livro);
        this.usuario = new Usuario(12);
    }

    @Test
    void livrosDoSistemaTest(){
        assertEquals(this.livro.toString() + "\n", biblioteca.livrosDisponiveis());
    }

    @Test
    void livrosDisponiveisTest(){
        this.biblioteca.reservarLivro(this.livro.getIdLivro(), this.usuario.getIdUsuario());
        assertEquals("", biblioteca.livrosDisponiveis());
    }

    @Test
    void pesquisarLivroInexistenteTest() throws RuntimeException {
        try {
            biblioteca.pesquisarLivro("Teoria da Computacao");
        } catch (RuntimeException e) {
            throw new RuntimeException("O livro nao existe");
        }
    }

    @Test
    void pesquisarLivroExistenteTest() throws RuntimeException {
        assertEquals(this.livro.toString(),biblioteca.pesquisarLivro("Psoft"));
    }

    @Test
    void reservarLivroInexistenteTest() throws RuntimeException {
        try {
            biblioteca.reservarLivro(2, 12);
        } catch (RuntimeException e) {
            assertEquals("O livro nao existe", e.getMessage());
        }
    }

    @Test
    void reservarLivroIndisponivelTest() throws RuntimeException {
        try {
            biblioteca.reservarLivro(1, 12);
            biblioteca.reservarLivro(1, 12);
        } catch (RuntimeException e) {
            assertEquals("O livro ja foi reservado", e.getMessage());
        }
    }
}