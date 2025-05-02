package exception;

public class ExcecaoUsuarioSenhaInvalido extends Exception{
    public ExcecaoUsuarioSenhaInvalido() {
        super("Usuario e/ou senha invalidos");
    }
}
