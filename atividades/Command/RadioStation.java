// Invoker
public class RadioStation {

    Command command;
    
    public RadioStation() {}

    public void setComando(Command command) {
        this.command = command;
    }

    public void executeCommand(){
        this.command.execute();
    }
}
