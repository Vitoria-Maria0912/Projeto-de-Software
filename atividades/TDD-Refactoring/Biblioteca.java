import java.util.Collection;
import java.util.HashSet;

public class Biblioteca {

    private Collection<Livro> livros;
    private Collection<Usuario> usuarios;
    private Collection<Reserva> reservas;

    public Biblioteca (){
        this.livros = new HashSet<>();
        this.usuarios = new HashSet<>();
        this.reservas = new HashSet<>();
    }

    public void cadastrarLivro(Livro livro){
        this.livros.add(livro);
    }

    // US01
    public String pesquisarLivro (String titulo){
        String livroDesejado = null;
        for (Livro livro: this.livros) {
            if(livro.getTitulo().equals(titulo)){
                livroDesejado = livro.toString();
            }
        }
        if (livroDesejado == null){
            livroDesejado = "O livro nao existe";
        }
        return livroDesejado;
    }

    // US02
    public Collection<Livro> getLivros() {
        return this.livros;
    }

    public String livrosDisponiveis() {
        String livrosDisponiveis = "";
        for (Livro livro: this.livros) {
            if(livro.getDisponibilidade() == Disponibilidade.DISPONIVEL){
                livrosDisponiveis += livro + "\n";
            }
        }
        return livrosDisponiveis;
    }

    public void reservarLivro(int idLivro, int idUsuario) throws RuntimeException{
        String mensagemDeErro = "";
        for (Livro livro: this.livros) {
            if(livro.getIdLivro() == idLivro &&
                livro.getDisponibilidade() == Disponibilidade.DISPONIVEL){
                livro.alugarLivro();
                this.reservas.add(new Reserva(idUsuario, idLivro));
            } else if (livro.getDisponibilidade() == Disponibilidade.ALUGADO) {
                mensagemDeErro = "O livro ja foi reservado";
            }
        }
        if(!mensagemDeErro.isEmpty()){
            throw new RuntimeException(mensagemDeErro);
        }
    }
}
