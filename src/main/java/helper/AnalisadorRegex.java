package helper;

import model.HistoricoTransacoes;
import model.Transacao;
import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnalisadorRegex {

    /* ---------- campo simples ---------- */
    public static String localizarOcorrencia(String regex, String html) {
        Pattern p = Pattern.compile(regex,
                Pattern.MULTILINE | Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher m = p.matcher(html);
        return m.find() ? m.group(1) : null;
    }

    /* ---------- histórico ---------- */
    public static HistoricoTransacoes localizarTransacoes(String html) {

        HistoricoTransacoes historico = new HistoricoTransacoes();

        /* 1) captura cada <tr> da tabela */
        Pattern pLinha = Pattern.compile(
                "(?is)<tr\\s+class=\"(?:linhaPar|linhaImpar)\".*?>(.*?)</tr>");
        Matcher mLinha = pLinha.matcher(html);

        /* regex genérica para cada célula */
        Pattern pTd = Pattern.compile("(?is)<td[^>]*>(.*?)</td>");

        while (mLinha.find()) {
            String trechoTr = mLinha.group(1);

            /* 2) extrai todas as <td> */
            Matcher mTd = pTd.matcher(trechoTr);
            List<String> cols = new ArrayList<>();
            while (mTd.find()) {
                String raw = mTd.group(1)
                        .replaceAll("(?is)<!--.*?-->", "")  // remove comentários
                        .replaceAll("(?is)<[^>]+>", "")     // remove tags internas
                        .trim();
                cols.add(StringEscapeUtils.unescapeHtml4(raw));
            }

            if (cols.size() < 9) continue; // linha quebrada
            cols.remove(0);                // descarta a primeira célula vazia

            /* 3) Data & Hora (NBSP -> espaço) */
            String dh = cols.get(0).replace('\u00A0', ' ');
            String[] dataHora = dh.split("\\s+");
            if (dataHora.length != 2) continue;

            /* 4) cria Transacao – colunas na ordem exata */
            Transacao t = new Transacao(
                    dataHora[0],       // Data
                    dataHora[1],       // Hora
                    cols.get(1),       // Operação (com Jantar/Almoço)
                    cols.get(2),       // Créditos Gerados
                    cols.get(3),       // À Receber
                    cols.get(4),       // Adiantados
                    cols.get(5),       // Compensados
                    cols.get(6),       // Saldo Anterior
                    cols.get(7)        // Saldo Atual
            );
            historico.adicionarTransacao(t);
        }
        return historico;
    }
}
