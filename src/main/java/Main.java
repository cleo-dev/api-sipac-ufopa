import controller.ControladorUsuario;
import exception.*;
import model.*;
import util.Login;
import util.ServicoHttp;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        Credenciais cred = new Credenciais(args[0], args[1]);

        try {
            new Login().fazerLogin(cred);

            Usuario u = new ControladorUsuario().obterUsuario();

            /* ---------- impressão ---------- */
            System.out.println("\nDados do Usuário:");
            System.out.println("Nome: " + u.getPerfil().getNomeCompleto());
            System.out.println("Matrícula: " + u.getPerfil().getMatricula());
            System.out.println("Vínculo: " + u.getPerfil().getTipoDeVinculo());
            System.out.println("Situação: " + u.getPerfil().getSituacaoDoVinculo());
            System.out.println("Foto: " + u.getPerfil().getURLFoto());

            System.out.println("\nDados da Carteira:");
            System.out.println("Código: " + u.getCarteira().getCodigo());
            System.out.println("Saldo: " + u.getCarteira().getSaldo() + " refeições");
            System.out.println("QR Code: " + u.getCarteira().getStrQRCode());

            System.out.println("\nHistórico de Transações:");
            imprimirTransacoes(u.getHistoricoTransacoes().getTransacoes());

        } catch (ExcecaoErroDeConectividade |
                 ExcecaoUsuarioSenhaInvalido |
                 ExcecaoUsuarioSemCadastroRU e) {

            System.err.println("Erro: " + e.getMessage());
            System.exit(1);

        } finally {
            ServicoHttp.shutdown();   // fecha OkHttp
            System.exit(0);           // garante que nenhuma thread restará
        }
    }

    private static void imprimirTransacoes(List<Transacao> ts) {
        String cab = "| %-12s | %-8s | %-30s | %-8s | %-8s | %-10s | %-10s | %-10s | %-10s |%n";
        String lin = "| %-12s | %-8s | %-30s | %8s | %8s | %10s | %10s | %10s | %10s |%n";

        System.out.println("==============================================================================================================================");
        System.out.printf(cab,
                "Data", "Hora", "Operação", "Gerados", "Receber",
                "Adiant.", "Compens.", "Saldo Ant.", "Saldo Atual");
        System.out.println("|-------------|----------|--------------------------------|----------|----------|------------|------------|------------|------------|");

        for (Transacao t : ts) {
            System.out.printf(lin,
                    t.getData(), t.getHora(), t.getNomeOperacao(),
                    t.getCreditosGerados(), t.getCreditoReceber(),
                    t.getCreditoAdiantado(), t.getCreditoCompensado(),
                    t.getSaldoAnterior(), t.getSaldoAtual());
        }
        System.out.println("==============================================================================================================================\n");
    }
}
