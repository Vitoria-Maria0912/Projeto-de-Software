public class UserStories {
    
    State state;

    StatusVerificacao statusVerificacao = StatusVerificacao.NEGADA;

    public void moverUS(Cliente cliente){
        this.state.moverUS(cliente);
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setStatusVerificacao(Cliente cliente, StatusVerificacao statusVerificacao) {
        if(cliente.equals(Cliente.SCRUM_MASTER)){
            this.statusVerificacao = statusVerificacao;
        }
    }

    public StatusVerificacao getStatusVerificacao(){
        return this.statusVerificacao;
    }

}
