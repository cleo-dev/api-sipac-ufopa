package util;

public class ColecaoRegex {
    // NOME_COMPLETO: captura "Nome:" (com ou sem acento)
    public static final String NOME_COMPLETO =
        "(?i)<th[^>]*>\\s*Nome:?\\s*</th>\\s*<td[^>]*>\\s*([^<]+?)\\s*</td>";

    // MATRICULA: captura "Matrícula" (com "í" ou entidade &#237;)
    public static final String MATRICULA =
        "(?i)<td[^>]*>\\s*Matr(?:í|&#237;)cula:\\s*</td>\\s*<td[^>]*>\\s*(\\d+)\\s*</td>";

    // CODIGO: captura "Código" (com "ó" ou entidade &#243;)
    public static final String CODIGO =
        "(?i)<th[^>]*>C(?:ó|&#243;)digo:?\\s*</th>\\s*<td[^>]*>\\s*(\\d+)\\s*</td>";

    // SITUACAO: captura "Situação" (com "ç"/"ã" ou entidades &#231;/&#227;)
    public static final String SITUACAO =
        "(?i)<th[^>]*>Situa(?:ç|&#231;)(?:ã|&#227;)o:?\\s*</th>\\s*<td[^>]*>\\s*([^<]+?)\\s*</td>";

    // TIPO_DE_VINCULO: captura "Vínculo" (com "í" ou entidade &#237;)
    public static final String TIPO_DE_VINCULO =
        "(?i)<th[^>]*>Tipo de V(?:í|&#237;)nculo:?\\s*</th>\\s*<td[^>]*>\\s*([^<]+?)\\s*</td>";

    // SALDO: captura "Total de Refeições" (com "ç" ou entidade &#231;)
    public static final String SALDO =
    "(?i)<th[^>]*>Total de Refei(?:ç|&#231;)(?:õ|&#245;)es:?\\s*</th>\\s*<td[^>]*>\\s*<span[^>]*>(\\d+)</span>\\s*</td>";
    // STRING_QRCODE: captura o código do QRCode
    public static final String STRING_QRCODE =
        "/sipac/QRCode\\?codigo=(.+?)&tamanho=";

    // FOTO_DE_PERFIL: URL da foto (imagens comuns)
    public static final String FOTO_DE_PERFIL =
    "<img\\s+[^>]*src\\s*=\\s*[\"'](https?://[^\"']+\\.(?:jpeg|jpg|gif|png|bmp))[\"'][^>]*>";

    // USUARIO_SENHA_INVALIDO: mensagem de erro de login
    public static final String USUARIO_SENHA_INVALIDO =
        "<b>\\s+(Usuário e/ou senha inválidos)\\s+</b>";

    // USUARIO_SEM_CADASTRO_RU: mensagem de usuário sem cadastro
    public static final String USUARIO_SEM_CADASTRO_RU =
        "<em>(Voc(?:ê|&#234;) não possui um cartão do restaurante)\\.</em>";

    // HISTORICO_TRANSACOES: tabela de transações
    public static final String HISTORICO_TRANSACOES =
    // Início da linha (classe linhaPar ou linhaImpar)
    "(?is)<tr\\s+class=\"(?:linhaPar|linhaImpar)\".*?>\\s*"
  + "<td>.*?</td>\\s*"                                                      // célula vazia
  // Data & hora
  + "<td[^>]*>\\s*(\\d{2}/\\d{2}/\\d{4})&nbsp;(\\d{2}:\\d{2}).*?</td>\\s*"
  // Operação – aceita “(Almoço)”, “(Jantar)”, etc.
  + "<td[^>]*nowrap[^>]*>\\s*([^<]+?)\\s*(?:\\([^)]*\\))?\\s*</td>\\s*"
  // Créditos gerados
  + "<td[^>]*>\\s*(?:<!--.*?-->\\s*)?(\\d+).*?</td>\\s*"
  // À receber
  + "<td[^>]*>\\s*(?:<!--.*?-->\\s*)?(\\d+).*?</td>\\s*"
  // Adiantados
  + "<td[^>]*>\\s*(?:<!--.*?-->\\s*)?(\\d+).*?</td>\\s*"
  // Compensados
  + "<td[^>]*>\\s*(?:<!--.*?-->\\s*)?(\\d+).*?</td>\\s*"
  // Saldo anterior
  + "<td[^>]*>\\s*(?:<!--.*?-->\\s*)?(\\d+).*?</td>\\s*"
  // Saldo atual
  + "<td[^>]*>\\s*(?:<!--.*?-->\\s*)?(\\d+).*?</td>";
}