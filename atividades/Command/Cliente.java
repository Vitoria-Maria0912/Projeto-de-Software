public class Cliente {

    public static void main(String[] args) {
        
        RadioStation station1 = new RadioStation();
        Command commandPlay = new Play(new Radio());
        station1.setComando(commandPlay);
        station1.executeCommand();

        Command commandStop = new Stop(new Radio());
        station1.setComando(commandStop);
        station1.executeCommand();

        Command commandGoBack = new GoBack(new Radio());
        station1.setComando(commandGoBack);
        station1.executeCommand();

        Command commandGoFoward = new GoFoward(new Radio());
        station1.setComando(commandGoFoward);
        station1.executeCommand();
    }
}
