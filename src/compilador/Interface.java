package compilador;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

public class Interface extends JFrame {

    private JTextArea areaCodigo;          // mostra o código do arquivo aberto
    private JTextArea areaListaTokens;     // mostra os tokens reconhecidos
    private JTextPane painelMensagens;     // mostra aceito/rejeitado e erros com cores diferentes
    private JLabel contadorLinhas;         // mostra "Linhas: X"
    private JTree arvoresintatica;         // exibe a árvore sintática
    private DefaultTreeModel modeloArvore; // controla os dados da árvore
    private File arquivoAberto;            // guarda o arquivo que o usuário escolheu

    public Interface() {
        setTitle("ExMachina - Compilador");
        setSize(1200, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel do topo com botões e contador de linhas
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton botaoAbrirArquivo = new JButton("Abrir Arquivo");
        JButton botaoCompilar     = new JButton("Compilar");
        contadorLinhas = new JLabel("Linhas: 0");
        painelTopo.add(botaoAbrirArquivo);
        painelTopo.add(botaoCompilar);
        painelTopo.add(contadorLinhas);
        add(painelTopo, BorderLayout.NORTH);

        // Painel que exibe o código do arquivo
        areaCodigo = new JTextArea();
        areaCodigo.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaCodigo.setEditable(false);
        JScrollPane scrollCodigo = new JScrollPane(areaCodigo);
        scrollCodigo.setBorder(BorderFactory.createTitledBorder("Código"));

        // Painel que lista os tokens reconhecidos
        areaListaTokens = new JTextArea();
        areaListaTokens.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaListaTokens.setEditable(false);
        JScrollPane scrollTokens = new JScrollPane(areaListaTokens);
        scrollTokens.setBorder(BorderFactory.createTitledBorder("Tokens Reconhecidos"));

        // Painel que exibe a árvore sintática
        modeloArvore = new DefaultTreeModel(new DefaultMutableTreeNode("(vazio)"));
        arvoresintatica = new JTree(modeloArvore);
        JScrollPane scrollArvore = new JScrollPane(arvoresintatica);
        scrollArvore.setBorder(BorderFactory.createTitledBorder("Árvore Sintática"));

        // Divide a tela: código | tokens
        JSplitPane divisorCodigoTokens = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollCodigo, scrollTokens);
        divisorCodigoTokens.setDividerLocation(400);

        // Divide a tela: (código | tokens) | árvore
        JSplitPane divisorPrincipal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, divisorCodigoTokens, scrollArvore);
        divisorPrincipal.setDividerLocation(750);
        add(divisorPrincipal, BorderLayout.CENTER);

        // Painel do rodapé com mensagens — usa JTextPane para suportar múltiplas cores
        painelMensagens = new JTextPane();
        painelMensagens.setFont(new Font("Monospaced", Font.PLAIN, 12));
        painelMensagens.setEditable(false);
        JScrollPane scrollMensagens = new JScrollPane(painelMensagens);
        scrollMensagens.setBorder(BorderFactory.createTitledBorder("Mensagens"));
        scrollMensagens.setPreferredSize(new Dimension(0, 120));
        add(scrollMensagens, BorderLayout.SOUTH);

        // Define a ação de cada botão
        botaoAbrirArquivo.addActionListener(e -> abrirArquivo());
        botaoCompilar.addActionListener(e -> compilar());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Escreve texto colorido no painel de mensagens
    private void escreverMensagem(String texto, Color cor) {
        StyledDocument doc = painelMensagens.getStyledDocument();
        Style estilo = painelMensagens.addStyle("estilo", null);
        StyleConstants.setForeground(estilo, cor);
        StyleConstants.setFontFamily(estilo, "Monospaced");
        StyleConstants.setFontSize(estilo, 12);
        try {
            doc.insertString(doc.getLength(), texto, estilo);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void limparMensagens() {
        painelMensagens.setText("");
    }

    // Realça as linhas com erro em vermelho claro no painel de código
    private void realcarLinhasComErro(List<Integer> linhas) {
        Highlighter highlighter = areaCodigo.getHighlighter();
        highlighter.removeAllHighlights();
        Highlighter.HighlightPainter corErro =
            new DefaultHighlighter.DefaultHighlightPainter(new Color(255, 180, 180));
        for (int linha : linhas) {
            try {
                int indice = linha - 1; // JTextArea usa índice 0-based
                if (indice < 0 || indice >= areaCodigo.getLineCount()) continue;
                int inicio = areaCodigo.getLineStartOffset(indice);
                int fim    = areaCodigo.getLineEndOffset(indice);
                highlighter.addHighlight(inicio, fim, corErro);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Extrai números de linha das strings de erro no formato "Linha X: ..."
    private List<Integer> extrairLinhasDeErro(List<String> erros) {
        List<Integer> linhas = new ArrayList<>();
        Pattern padrao = Pattern.compile("linha (\\d+)", Pattern.CASE_INSENSITIVE);
        for (String erro : erros) {
            Matcher m = padrao.matcher(erro);
            if (m.find()) linhas.add(Integer.parseInt(m.group(1)));
        }
        return linhas;
    }

    private void abrirArquivo() {
        JFileChooser explorador = new JFileChooser();
        if (explorador.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            arquivoAberto = explorador.getSelectedFile();
            try {
                String conteudoDoArquivo = Files.readString(arquivoAberto.toPath());
                areaCodigo.setText(conteudoDoArquivo);
                contadorLinhas.setText("Linhas: " + conteudoDoArquivo.lines().count());
                limparMensagens();
                areaListaTokens.setText("");
                modeloArvore.setRoot(new DefaultMutableTreeNode("(vazio)"));
            } catch (IOException erro) {
                limparMensagens();
                escreverMensagem("Erro ao abrir arquivo: " + erro.getMessage(), Color.RED);
            }
        }
    }

    private void compilar() {
        if (arquivoAberto == null) {
            limparMensagens();
            escreverMensagem("Abra um arquivo antes de compilar.", Color.RED);
            return;
        }

        limparMensagens();
        areaListaTokens.setText("");
        modeloArvore.setRoot(new DefaultMutableTreeNode("(vazio)"));
        areaCodigo.getHighlighter().removeAllHighlights(); // limpa realces anteriores

        ResultadoCompilacao resultado = new ResultadoCompilacao();

        try (Reader leitorArquivo = LerArquivo.abrir(arquivoAberto.getPath())) {
            // Passa o resultado para o parser preencher a árvore e coletar erros
            ExMachina analisador = new ExMachina(leitorArquivo, resultado);
            analisador.inicio();

            // Sempre mostra "Linguagem Aceita ExMachina." em verde
            escreverMensagem("Linguagem Aceita ExMachina.", new Color(0, 128, 0));

            // Se houver erros recuperados, mostra em vermelho e realça as linhas
            if (!resultado.erros.isEmpty()) {
                escreverMensagem("\n\nERROS RECUPERADOS:\n", Color.RED);
                for (String erro : resultado.erros) {
                    escreverMensagem(erro + "\n", Color.RED);
                }
                realcarLinhasComErro(extrairLinhasDeErro(resultado.erros));
            }

        } catch (ParseException erroSintatico) {
            String msgSintatico = MensagensErro.deSintaxe(erroSintatico);
            escreverMensagem("REJEITADO\n", Color.RED);
            escreverMensagem(msgSintatico, Color.RED);
            // Realça a linha do erro fatal usando o token onde falhou
            List<Integer> linhaFatal = new ArrayList<>();
            linhaFatal.add(erroSintatico.currentToken.next.beginLine);
            realcarLinhasComErro(linhaFatal);
        } catch (TokenMgrError erroLexico) {
            escreverMensagem("REJEITADO\n", Color.RED);
            escreverMensagem(MensagensErro.deLexico(erroLexico), Color.RED);
            // Extrai a linha da mensagem do TokenMgrError (formato: "line X, column Y")
            List<Integer> linhaLexico = extrairLinhasDeErro(List.of(erroLexico.getMessage()));
            realcarLinhasComErro(linhaLexico);
        } catch (IOException e) {
            escreverMensagem("Erro ao ler arquivo: " + e.getMessage(), Color.RED);
        }

        // Exibe tokens reconhecidos
        StringBuilder listaTokens = new StringBuilder(
            String.format("%-25s %s%n", "LEXEMA", "TIPO"));
        listaTokens.append("-".repeat(40)).append("\n");
        for (String[] tok : resultado.tokens)
            listaTokens.append(String.format("%-25s %s%n", tok[0], tok[1]));
        areaListaTokens.setText(listaTokens.toString());

        // Exibe a árvore sintática construída pelo parser
        if (resultado.raizArvore != null) {
            modeloArvore.setRoot(resultado.raizArvore);
            for (int i = 0; i < arvoresintatica.getRowCount(); i++)
                arvoresintatica.expandRow(i);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Interface::new);
    }
}
