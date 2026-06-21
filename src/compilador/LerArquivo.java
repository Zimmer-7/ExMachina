package compilador;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class LerArquivo {
	public static Reader abrir(String caminho) throws IOException{
	    return new FileReader(caminho);
	} 
}
