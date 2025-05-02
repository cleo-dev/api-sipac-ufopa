package model;

import java.util.ArrayList;
import java.util.List;

public class HistoricoTransacoes {
    private List<Transacao> transacoes;

    public HistoricoTransacoes() {
        transacoes = new ArrayList<>();
    }

    public void adicionarTransacao(Transacao transacao) {
        transacoes.add(transacao);
    }

    public List<Transacao> getTransacoes() {
        return transacoes;
    }
}