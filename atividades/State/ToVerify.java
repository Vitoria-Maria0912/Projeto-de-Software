public class ToVerify implements State {

    UserStories userStories;
    
    @Override
    public void moverUS(Cliente cliente) {
        if(isScrumMaster(cliente)) {
            if(isAprovada()) {
                this.userStories.setState(new Done());
            } else {
                this.userStories.setState(new ToDo());
            }
        }
    }

    private boolean isScrumMaster(Cliente cliente) {
        return cliente.equals(Cliente.SCRUM_MASTER);
    }

    private boolean isAprovada() {
        return this.userStories.getStatusVerificacao().equals(StatusVerificacao.APROVADA);
    }
}
