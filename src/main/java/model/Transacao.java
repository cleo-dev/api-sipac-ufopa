package model;

/** POJO que representa uma linha do histórico de transações do RU. */
public class Transacao {
    private String data;
    private String hora;
    private String nomeOperacao;
    private String creditosGerados;
    private String creditoReceber;
    private String creditoAdiantado;
    private String creditoCompensado;
    private String saldoAnterior;
    private String saldoAtual;

    public Transacao(String data,
                     String hora,
                     String nomeOperacao,
                     String creditosGerados,
                     String creditoReceber,
                     String creditoAdiantado,
                     String creditoCompensado,
                     String saldoAnterior,
                     String saldoAtual) {

        this.data              = data;
        this.hora              = hora;
        this.nomeOperacao      = nomeOperacao;
        this.creditosGerados   = creditosGerados;
        this.creditoReceber    = creditoReceber;
        this.creditoAdiantado  = creditoAdiantado;
        this.creditoCompensado = creditoCompensado;
        this.saldoAnterior     = saldoAnterior;
        this.saldoAtual        = saldoAtual;
    }

    /* ---------- getters & setters ---------- */
    public String getData()               { return data; }
    public void   setData(String data)    { this.data = data; }

    public String getHora()               { return hora; }
    public void   setHora(String hora)    { this.hora = hora; }

    public String getNomeOperacao()       { return nomeOperacao; }
    public void   setNomeOperacao(String n){ this.nomeOperacao = n; }

    public String getCreditosGerados()    { return creditosGerados; }
    public void   setCreditosGerados(String v){ this.creditosGerados = v; }

    public String getCreditoReceber()     { return creditoReceber; }
    public void   setCreditoReceber(String v){ this.creditoReceber = v; }

    public String getCreditoAdiantado()   { return creditoAdiantado; }
    public void   setCreditoAdiantado(String v){ this.creditoAdiantado = v; }

    public String getCreditoCompensado()  { return creditoCompensado; }
    public void   setCreditoCompensado(String v){ this.creditoCompensado = v; }

    public String getSaldoAnterior()      { return saldoAnterior; }
    public void   setSaldoAnterior(String v){ this.saldoAnterior = v; }

    public String getSaldoAtual()         { return saldoAtual; }
    public void   setSaldoAtual(String v) { this.saldoAtual = v; }
}
