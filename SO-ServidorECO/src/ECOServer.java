import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Servidor ECO
 * @author Rafael de Castro Dantas Sales - rafaelcds@gmail.com
 */
public class ECOServer {
	
	public static void main(String[] args) {
		try {			
			int BUFFER_SIZE = 1024;
			ServerSocket serverSocket = new ServerSocket(6014);
			System.out.println("Servidor ECO");
			
			while (true) {
				Socket client = serverSocket.accept();
				System.out.println("Cliente " + client.getInetAddress().getHostAddress() + " conectado");
				InputStream inputStream = client.getInputStream();
				OutputStream outputStream = client.getOutputStream();
				
				while (client.isConnected()) {
					
					//Recebe e ecoa os dados do cliente:
					byte readByte;
					byte[] buffer = new byte[BUFFER_SIZE];
					int i = 0;
					while ((readByte = (byte) inputStream.read()) != -1) {
						buffer[i++] = readByte;
						if (i == BUFFER_SIZE) {
							outputStream.write(buffer);
							buffer = new byte[BUFFER_SIZE];
						}
					}
					//Envia o que havia escrito no buffer quando parou de receber dados:
					if (i > 0) {
						outputStream.write(buffer);
					}
					outputStream.write(-1);
				}
				
				client.close();
			}
		} catch (Exception ioe) {
			System.err.println(ioe);
		}
	}
}
