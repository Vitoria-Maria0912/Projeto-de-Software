public class Sms extends MnSG {

    public Sms(Mensagem mensagem) {
        super(mensagem);
    }

    @Override
    public void enviarMensagemAoAplicativo() {
        System.out.println("Enviando mensagem via Sms...");
        mensagem.enviarMensagem();
    }
}
