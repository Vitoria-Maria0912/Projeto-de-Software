public class GoBack implements Command{
    
    private Radio radio;

    public GoBack(Radio radio){
        this.radio = radio;
    }

    @Override
    public void execute() {
        radio.goBack();
    }
}
