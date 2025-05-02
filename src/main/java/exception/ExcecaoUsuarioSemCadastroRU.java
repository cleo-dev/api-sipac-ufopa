package exception;

public class ExcecaoUsuarioSemCadastroRU extends Exception{
    public ExcecaoUsuarioSemCadastroRU() {
        super("Usuario sem cadastro no RU");
    }
}

