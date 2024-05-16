public class Stop implements Command{

    private Radio radio;

    public Stop(Radio radio){
        this.radio = radio;
    }

    @Override
    public void execute() {
        radio.stop();
    }
}
