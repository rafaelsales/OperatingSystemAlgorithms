import java.net.*;
import java.io.*;

/**
 * Cliente do servidor de piadas
 * @author Rafael de Castro Dantas Sales - rafaelcds@gmail.com
 */
public class PiadasClient {
	public static void main(String[] args) {
		System.out.println("Cliente do servidor de piadas");
		try {
			System.out.println("Conectando-se ao servidor de piadas");
			//Cria uma conexão via socket com o servidor de piadas:
			Socket socket = new Socket("127.0.0.1", 6013);
			
			System.out.println("Recebendo piada...");
			System.out.println();
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String linha;
			//Recebe e imprime as linhas recebidas:
			while ((linha = reader.readLine()) != null) {
				System.out.println(linha);
			}
			
			System.out.println();
			
			System.out.println("Piada recebida com sucesso!");
			socket.close();
		} catch (IOException ioe) {
			System.err.println(ioe);
		}
	}
}
