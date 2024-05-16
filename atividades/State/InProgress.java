public class InProgress implements State {
    
    UserStories userStories;

    @Override
    public void moverUS(Cliente cliente) {
        if(isDesenvolvedor(cliente)){
            this.userStories.setState(new ToVerify());
        }
    }

    private boolean isDesenvolvedor(Cliente cliente) {
        return cliente.equals(Cliente.DESENVOLVEDOR);
    }
}
