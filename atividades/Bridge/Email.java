public class Email extends MnSG {

    public Email(Mensagem mensagem) {
        super(mensagem);
    }

    @Override
    public void enviarMensagemAoAplicativo() {
        System.out.println("Enviando mensagem via Email...");
        mensagem.enviarMensagem();
    }
}
