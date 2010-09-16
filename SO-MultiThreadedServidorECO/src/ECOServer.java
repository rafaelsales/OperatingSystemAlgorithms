import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ECOServer {
	
	public static void main(String[] args) {
		try {			
			ServerSocket serverSocket = new ServerSocket(6014);
			System.out.println("Servidor ECO");
			
			while (true) {
				System.out.println("Aguardando uma nova conexão...");
				Socket clientSocket = serverSocket.accept();
				
				// Handles the client request in a new thread:
				Thread threadClientHandler = new Thread(new ClientRequestHandler(clientSocket));
				threadClientHandler.start();
			}
		} catch (Exception ioe) {
			System.err.println(ioe);
		}
	}
	
	public static class ClientRequestHandler implements Runnable {
		
		private static final int BUFFER_SIZE = 1024;
		private final Socket clientSocket;

		public ClientRequestHandler(Socket clientSocket) {
			this.clientSocket = clientSocket;
		}

		@Override
		public void run() {
			try {
				System.out.println("Cliente " + clientSocket.getInetAddress().getHostAddress() + " conectado.");
				InputStream inputStream = clientSocket.getInputStream();
				OutputStream outputStream = clientSocket.getOutputStream();
				
				while (clientSocket.isConnected()) {
					
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
				
				clientSocket.close();
				System.out.println("Cliente " + clientSocket.getInetAddress().getHostAddress() + " desconectado.");
			} catch (Exception e) {
				System.out.println("Ocorreu um erro inesperado durante a conexão com o cliente " + clientSocket.getInetAddress().getHostAddress());
			}
		}
		
	}
}
