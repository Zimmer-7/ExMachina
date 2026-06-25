package compilador;

public class MensagensErro {

    public static String deSintaxe(ParseException e) {
        Token erro = (e.currentToken != null) ? e.currentToken.next : null;
        StringBuilder sb = new StringBuilder("Erro de sintaxe");

        if (erro != null) {
            sb.append(" na linha ").append(erro.beginLine)
              .append(", coluna ").append(erro.beginColumn)
              .append(": token inesperado \"").append(erro.image).append("\"");
        }

        if (e.expectedTokenSequences != null && e.expectedTokenSequences.length > 0) {
            sb.append(".\n");
            sb.append(e.expectedTokenSequences.length == 1 ? "Esperado: " : "Esperado um de: ");
            for (int i = 0; i < e.expectedTokenSequences.length; i++) {
                if (i > 0) sb.append(", ");
                for (int j = 0; j < e.expectedTokenSequences[i].length; j++) {
                    if (j > 0) sb.append(" ");
                    sb.append(e.tokenImage[e.expectedTokenSequences[i][j]]);
                }
            }
        }
        sb.append(".");
        return sb.toString();
    }

    public static String deLexico(TokenMgrError e) {
        String msg = e.getMessage();
        if (msg == null) {
            return "Erro léxico.";
        }
        return "Erro léxico" + msg
            .replaceFirst("^Lexical error", "")
            .replace(" at line ", " na linha ")
            .replace(", column ", ", coluna ")
            .replace(".  Encountered: ", ". Encontrado: ")
            .replace("after prefix ", "após o prefixo ")
            .replace("(in lexical state ", "(no estado léxico ")
            .replace("<EOF>", "<FIM-DE-ARQUIVO>");
    }
}
