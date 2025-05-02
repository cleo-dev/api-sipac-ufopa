package controller;

import model.*;

public class ControladorUsuario {
    public void criarNovoUsuario(String nomeCompleto, String matricula, String tipoDeVinculo, String situacaoDoVinculo, String URLFoto,
                                 String codigoCarteira, int saldoCarteira, String strQRCodeCarteira,
                                 String nomeUsuario, String senhaUsuario, HistoricoTransacoes historicoTransacoes) {
        Perfil perfil = new Perfil(nomeCompleto, matricula, tipoDeVinculo, situacaoDoVinculo, URLFoto);
        Carteira carteira = new Carteira(codigoCarteira, saldoCarteira, strQRCodeCarteira);
        Credenciais credenciais = new Credenciais(nomeUsuario, senhaUsuario);
        Usuario.getInstance(perfil, carteira, credenciais, historicoTransacoes);
    }

    public Usuario obterUsuario() {
        // Obtém a instância existente (parâmetros são ignorados após a primeira criação)
        Usuario usuario = Usuario.getInstance(null, null, null, null);
        if (usuario.getPerfil() == null || usuario.getCarteira() == null) {
            throw new IllegalStateException("Usuário não foi inicializado. Crie um usuário primeiro.");
        }
        return usuario;
    }

    public String consultarNomeUsuario(Usuario usuario) {
        return usuario.getPerfil().getNomeCompleto();
    }

    public String consultarTipoDeVinculo(Usuario usuario) {
        return usuario.getPerfil().getTipoDeVinculo();
    }

    public String consultarSituacaoDoVinculo(Usuario usuario) {
        return usuario.getPerfil().getSituacaoDoVinculo();
    }

    public String consultarURLFoto(Usuario usuario) {
        return usuario.getPerfil().getURLFoto();
    }

    public String consultarCodigoCarteira(Usuario usuario) {
        return usuario.getCarteira().getCodigo();
    }

    public double consultarSaldoCarteira(Usuario usuario) {
        return usuario.getCarteira().getSaldo();
    }

    public String consultarStrQRCodeCarteira(Usuario usuario) {
        return usuario.getCarteira().getStrQRCode();
    }

    public void atualizarSaldoCarteira(Usuario usuario, int novoSaldo) {
        usuario.getCarteira().setSaldo(novoSaldo);
    }
}