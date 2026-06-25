package compilador;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;

public class ResultadoCompilacao {
	public List<String[]> tokens = new ArrayList<>();
    public List<String> erros    = new ArrayList<>();
    public DefaultMutableTreeNode raizArvore = new DefaultMutableTreeNode("programa");

    public void addToken(String lexema, String tipo) {
        tokens.add(new String[]{lexema, tipo});
    }

    public void addErro(int linha, String msg) {
        erros.add("Linha " + linha + ": " + msg);
    }
}
