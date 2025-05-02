package util;
import org.apache.commons.text.StringEscapeUtils;
import controller.ControladorUsuario;
import exception.ExcecaoErroDeConectividade;
import exception.ExcecaoUsuarioSemCadastroRU;
import exception.ExcecaoUsuarioSenhaInvalido;
import helper.AnalisadorRegex;
import model.Credenciais;
import model.HistoricoTransacoes;
import okhttp3.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Fluxo de autenticação no SIPAC + scraping dos dados do RU.
 * Agora com validação null-safe para cada campo capturado, evitando
 * NullPointerException quando o HTML muda.
 */
public class Login {

    /**
     * Executa todo o fluxo de login, scraping e criação de usuário.
     */
    public void fazerLogin(Credenciais credenciais)
            throws ExcecaoUsuarioSenhaInvalido,
                   ExcecaoErroDeConectividade,
                   ExcecaoUsuarioSemCadastroRU {

        // 1) Obter cookie de sessão
        String cookie = obterCookie();

        // 2) Autenticar usuário no SIPAC
        autenticarUsuario(credenciais, cookie);

        // 3) Carregar páginas HTML de saldo e perfil
        String paginaSaldo  = obterPaginaSaldoCartaoRU(cookie);
        String paginaPerfil = obterPaginaPerfilUsuario(cookie);

        // 4) Gravar HTML em disco para depuração
        try {
            Files.writeString(Path.of("page_saldo.html"),  paginaSaldo);
            Files.writeString(Path.of("page_perfil.html"), paginaPerfil);
            System.out.println("⚠  HTML salvo em page_saldo.html e page_perfil.html");
        } catch (IOException e) {
            // usa só o construtor padrão, porque o outro não existe
            throw new ExcecaoErroDeConectividade();
        }

        // 5) Extrair campos com validação
        String nomeCompleto = campoObrigatorio(
                ColecaoRegex.NOME_COMPLETO, paginaSaldo,  "NOME COMPLETO");

        String matricula = campoObrigatorio(
                ColecaoRegex.MATRICULA, paginaPerfil, "MATRÍCULA")
            .replace(" ", "");

        String codigo = campoObrigatorio(
                ColecaoRegex.CODIGO, paginaSaldo, "CÓDIGO DA CARTEIRA");

        String situacao = campoObrigatorio(
                ColecaoRegex.SITUACAO, paginaSaldo, "SITUAÇÃO DO VÍNCULO");

        String tipoVinculo = campoObrigatorio(
                ColecaoRegex.TIPO_DE_VINCULO, paginaSaldo, "TIPO DE VÍNCULO");

        String saldoStr = campoObrigatorio(
                ColecaoRegex.SALDO, paginaSaldo, "SALDO");
        int saldo = Integer.parseInt(saldoStr);

        String strQRCode = campoObrigatorio(
                ColecaoRegex.STRING_QRCODE, paginaSaldo, "QRCODE");

        String urlFoto = campoObrigatorio(
                ColecaoRegex.FOTO_DE_PERFIL, paginaPerfil, "URL DA FOTO");

        HistoricoTransacoes historico =
                AnalisadorRegex.localizarTransacoes(paginaSaldo);

        // 6) Criar usuário no sistema local
        new ControladorUsuario().criarNovoUsuario(
                nomeCompleto,
                matricula,
                tipoVinculo,
                situacao,
                urlFoto,
                codigo,
                saldo,
                strQRCode,
                credenciais.getUsuario(),
                credenciais.getSenha(),
                historico
        );
    }

    // =============== Métodos auxiliares =====================

    /**
     * Garante que um campo regex exista no HTML; caso contrário lança
     * IllegalStateException com mensagem clara.
     */

     private String campoObrigatorio(String regex, String html, String nomeCampo) {
        String valor = AnalisadorRegex.localizarOcorrencia(regex, html);
        if (valor == null) {
            throw new IllegalStateException("Campo " + nomeCampo + " não encontrado na página!");
        }
        return StringEscapeUtils.unescapeHtml4(valor.trim()); // Decodifica entidades HTML
    }

    /**
     * Faz GET na página inicial do SIPAC para capturar cookie de sessão.
     */
    private String obterCookie() throws ExcecaoErroDeConectividade {
        try {
            ServicoHttp http = new ServicoHttp();
            try (Response resp = http.fazerRequisicaoHttpGET(Endpoints.PAGINA_INICIAL_SIPAC)) {
                String setCookie = Objects.requireNonNull(
                        resp.headers().get("Set-Cookie"),
                        "Header Set-Cookie ausente");
                return setCookie.substring(0, setCookie.indexOf(";"));
            }
        } catch (IOException e) {
            throw new ExcecaoErroDeConectividade();
        }
    }

    /**
     * Envia usuário e senha para o SIPAC; detecta falha de login via regex.
     */
    private void autenticarUsuario(Credenciais credenciais, String cookie)
            throws ExcecaoErroDeConectividade, ExcecaoUsuarioSenhaInvalido {
        try {
            ServicoHttp http = new ServicoHttp();
            String body = http.fazerRequisicaoHttpPOST(
                    Endpoints.LOGON_SIPAC + cookie.toLowerCase(),
                    "width=1920&height=1080&login=" + credenciais.getUsuario() +
                    "&senha=" + credenciais.getSenha(),
                    cookie);
            if (AnalisadorRegex.localizarOcorrencia(
                    ColecaoRegex.USUARIO_SENHA_INVALIDO, body) != null) {
                throw new ExcecaoUsuarioSenhaInvalido();
            }
        } catch (IOException e) {
            throw new ExcecaoErroDeConectividade();
        }
    }

    /**
     * Carrega a página de saldo do RU; detecta usuário sem cadastro RU via regex.
     */
    private String obterPaginaSaldoCartaoRU(String cookie)
            throws ExcecaoErroDeConectividade, ExcecaoUsuarioSemCadastroRU {
        try {
            ServicoHttp http = new ServicoHttp();
            String body = http.fazerRequisicaoHttpPOST(
                    Endpoints.SALDO_RU_SIPAC,
                    "formmenuadm=formmenuadm&jscook_action=" +
                    "formmenuadm_menuaaluno_menu%3AA%5D%23%7BsaldoCartao.iniciar%7D" +
                    "&javax.faces.ViewState=j_id1",
                    cookie);
            if (AnalisadorRegex.localizarOcorrencia(
                    ColecaoRegex.USUARIO_SEM_CADASTRO_RU, body) != null) {
                throw new ExcecaoUsuarioSemCadastroRU();
            }
            return body;
        } catch (IOException e) {
            throw new ExcecaoErroDeConectividade();
        }
    }

    /**
     * Carrega a página de perfil do usuário no portal SIPAC.
     */
    private String obterPaginaPerfilUsuario(String cookie)
            throws ExcecaoErroDeConectividade {
        try {
            ServicoHttp http = new ServicoHttp();
            return http.fazerRequisicaoHttpGET(
                    Endpoints.PORTAL_DO_ALUNO_SIPAC, cookie);
        } catch (IOException e) {
            throw new ExcecaoErroDeConectividade();
        }
    }
}
