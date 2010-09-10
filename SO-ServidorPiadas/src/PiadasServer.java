import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Servidor de piadas
 * @author Rafael de Castro Dantas Sales - rafaelcds@gmail.com
 */
public class PiadasServer {
	
	public static void main(String[] args) {
		try {
			List<String> piadas = obterPiadas();
			Random random = new Random();
			
			ServerSocket serverSocket = new ServerSocket(6013);
			
			while (true) {
				Socket client = serverSocket.accept();
				PrintWriter printOut = new PrintWriter(client.getOutputStream(), true);
				//Envia uma piada aleatória:
				printOut.println(piadas.get(random.nextInt(piadas.size())));

				client.close();
			}
		} catch (Exception ioe) {
			System.err.println(ioe);
		}
	}
	
	public static List<String> obterPiadas() throws Exception {
		List<String> piadas = new ArrayList<String>();
		InputStreamReader fileISReader = new InputStreamReader(new FileInputStream("piadas.txt"), "ISO-8859-1");
		BufferedReader readerPiadas = new BufferedReader(fileISReader);
		String linha;
		StringBuilder piadaAtual = new StringBuilder();
		while ((linha = readerPiadas.readLine()) != null) {
			if (linha.trim().isEmpty()) {
				continue;
			} else if (linha.trim().equals("!FIM_DA_PIADA!")) {
				piadas.add(piadaAtual.toString());
				piadaAtual = new StringBuilder();
			} else {
				if (piadaAtual.length() > 0) {
					piadaAtual.append("\n");
				}
				piadaAtual.append(linha);
			}
		}
		
		readerPiadas.close();
		return piadas;
	}
}
