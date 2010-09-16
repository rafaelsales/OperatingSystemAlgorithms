import java.net.*;
import java.io.*;

public class DateServer {
	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(6013);

			// now listen for connections
			while (true) {
				System.out.println("Aguardando uma nova conexão...");
				Socket clientSocket = serverSocket.accept();
				// we have a connection

				// Handles the client request in a new thread:
				Thread threadClientHandler = new Thread(new ClientRequestHandler(clientSocket));
				threadClientHandler.start();
			}
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public static class ClientRequestHandler implements Runnable {
		private final Socket clientSocket;

		public ClientRequestHandler(Socket clientSocket) {
			this.clientSocket = clientSocket;
		}

		@Override
		public void run() {
			try {
				System.out.println("Cliente " + clientSocket.getInetAddress().getHostAddress() + " conectado.");
				PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
				// write the Date to the socket
				printWriter.println(new java.util.Date().toString());

				// close the socket and resume listening for more connections
				clientSocket.close();
				System.out.println("Cliente " + clientSocket.getInetAddress().getHostAddress() + " desconectado.");
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}
}