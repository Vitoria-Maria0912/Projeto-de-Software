public class Play implements Command {

    private Radio radio;

    public Play(Radio radio){
        this.radio = radio;
    }

    @Override
    public void execute() {
        radio.play();
    }
}
