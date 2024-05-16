public class ToDo implements State {

    UserStories userStories;
    
    @Override
    public void moverUS(Cliente cliente) {
        this.userStories.setState(new InProgress());
    }
}