package model;

import java.util.Objects;

/**
 * Singleton thread‑safe usando o padrão “double‑checked locking”.
 * – A instância só é criada uma vez, de forma preguiçosa.
 * – O campo é {@code volatile} para garantir visibilidade entre threads.
 */
public class Usuario {

    private static volatile Usuario instance;   // <‑‑ garantia de visibilidade

    private Perfil               perfil;
    private Carteira             carteira;
    private Credenciais          credenciais;
    private HistoricoTransacoes  historicoTransacoes;

    private Usuario(Perfil perfil,
                    Carteira carteira,
                    Credenciais credenciais,
                    HistoricoTransacoes historicoTransacoes) {

        this.perfil              = Objects.requireNonNull(perfil);
        this.carteira            = Objects.requireNonNull(carteira);
        this.credenciais         = Objects.requireNonNull(credenciais);
        this.historicoTransacoes = Objects.requireNonNull(historicoTransacoes);
    }

    /** Inicializa (se necessário) e devolve a instância única. */
    public static Usuario getInstance(Perfil perfil,
                                      Carteira carteira,
                                      Credenciais credenciais,
                                      HistoricoTransacoes historicoTransacoes) {

        if (instance == null) {                       // 1ª verificação (rápida)
            synchronized (Usuario.class) {
                if (instance == null) {               // 2ª verificação (segura)
                    instance = new Usuario(perfil, carteira, credenciais,
                                            historicoTransacoes);
                }
            }
        }
        return instance;
    }

    /* ---------- getters & setters ---------- */
    public Perfil getPerfil() { return perfil; }
    public void   setPerfil(Perfil p) { this.perfil = p; }

    public Carteira getCarteira() { return carteira; }
    public void     setCarteira(Carteira c) { this.carteira = c; }

    public Credenciais getCredenciais() { return credenciais; }
    public void        setCredenciais(Credenciais c) { this.credenciais = c; }

    public HistoricoTransacoes getHistoricoTransacoes() { return historicoTransacoes; }
    public void                setHistoricoTransacoes(HistoricoTransacoes h) { this.historicoTransacoes = h; }
}
