import java.net.*;
import java.io.*;

/**
 * Cliente do servidor ECO
 * @author Rafael de Castro Dantas Sales - rafaelcds@gmail.com
 */
public class PiadasClient {
	public static void main(String[] args) {
		try {
			//Cria uma conexão via socket com o servidor de piadas:
			Socket socket = new Socket("127.0.0.1", 6014);
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String linha;
			//Recebe e imprime as linhas recebidas:
			while ((linha = reader.readLine()) != null) {
				System.out.println(linha);
			}
			socket.close();
		} catch (IOException ioe) {
			System.err.println(ioe);
		}
	}
}
