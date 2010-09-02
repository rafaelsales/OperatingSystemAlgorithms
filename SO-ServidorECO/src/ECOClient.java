import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Cliente do servidor ECO
 * @author Rafael de Castro Dantas Sales - rafaelcds@gmail.com
 */
public class ECOClient {
	public static void main(String[] args) {
		try {
			//Cria uma conexão via socket com o servidor de piadas:
			Socket socket = new Socket("127.0.0.1", 6014);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();

			System.out.print("Digite algo para enviar ao servidor ECO: ");
			Scanner scanner = new Scanner(System.in);
			byte[] dados = scanner.nextLine().getBytes();
			
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
			
			socket.close();
		} catch (IOException ioe) {
			System.err.println(ioe);
		}
	}
}
