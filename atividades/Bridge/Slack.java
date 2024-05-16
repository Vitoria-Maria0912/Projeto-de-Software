public class Slack extends MnSG {

    public Slack(Mensagem mensagem) {
        super(mensagem);
    }

    @Override
    public void enviarMensagemAoAplicativo() {
        System.out.println("Enviando mensagem via Slack...");
        this.mensagem.enviarMensagem();
    }
}
