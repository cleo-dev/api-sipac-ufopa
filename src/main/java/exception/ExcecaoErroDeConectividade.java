package exception;

import java.io.IOException;

public class ExcecaoErroDeConectividade extends IOException {
    public ExcecaoErroDeConectividade(){
        super("Verifique a conexão com a internet!");
    }
}
