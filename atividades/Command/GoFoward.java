public class GoFoward implements Command{
    
    private Radio radio;

    public GoFoward(Radio radio){
        this.radio = radio;
    }

    @Override
    public void execute() {
        radio.goFoward();
    }
}
