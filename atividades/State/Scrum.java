import java.util.List;

public abstract class Scrum {
    
    Cliente cliente;
    
    List<UserStories> userStories;

    public void addUserStories(UserStories userStory){
        this.userStories.add(userStory);
    }

    public void moverUS(UserStories userStory){
        userStory.moverUS(this.cliente);
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setStatusVerificacao(UserStories userStory, StatusVerificacao statusVerificacao) {
        userStory.setStatusVerificacao(this.cliente, statusVerificacao);
    }
}
