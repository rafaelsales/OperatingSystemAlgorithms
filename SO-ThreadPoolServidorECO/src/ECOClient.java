import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ECOClient {
	public static void main(String[] args) {
		try {
			System.out.println("Cliente do servidor ECO.");
			//Cria uma conexão via socket com o servidor de piadas:
			Scanner scanner = new Scanner(System.in);
			System.out.println("Conectando ao servidor...");
			Socket socket = new Socket("127.0.0.1", 6014);
			System.out.println("Conectado ao servidor!");
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			System.out.println("Conectando ao servidor...");
			System.out.println("Digite um texto e pressione ENTER para enviar e receber o ECO.");
			System.out.println("Para encerrar pressione ENTER com a linha vazia.");
			System.out.println("---");

			while (true) {
				System.out.print("Digite um texto (vazio para encerrar): ");
				String line = scanner.nextLine();
				if (line.isEmpty()) {
					break;
				}
				byte[] dados = line.getBytes();
				
				for (byte b : dados) {
					outputStream.write(b);
				}
				outputStream.write(-1);
				
				byte readByte;
				ByteArrayOutputStream readBytes = new ByteArrayOutputStream();
				while ((readByte = (byte) inputStream.read()) != -1) {
					readBytes.write(readByte);
				}
				System.out.println("ECO: " + new String(readBytes.toByteArray()));
			}
			socket.close();
		} catch (IOException ioe) {
			System.err.println(ioe);
		}
	}
}
