
public class Gerente implements Cargo {

    private String nome;

    @Override
    public String getNome(){
        return this.nome;
    }

    @Override
    public void setNome(String novoNome){
        this.nome = novoNome;
    }

}
