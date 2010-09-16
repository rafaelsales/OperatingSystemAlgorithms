import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ECOServer {

	public static void main(String[] args) {
		System.out.println("Servidor ECO");
		ExecutorService executorService = chooseExecutor();

		try {
			ServerSocket serverSocket = new ServerSocket(6014);

			while (true) {
				System.out.println("Aguardando uma nova conexão...");
				Socket clientSocket = serverSocket.accept();

				// Handles the client request in a thread of the thread pool:
				executorService.execute(new ClientRequestHandler(clientSocket));
			}
		} catch (Exception ioe) {
			System.err.println(ioe);
		} finally {
			executorService.shutdown();
		}
	}

	private static ExecutorService chooseExecutor() {
		Scanner scannerInt = new Scanner(System.in);
		System.out.println("Defina o tipo de Thread Pool a ser utilizado");
		System.out.println("1 - Single Thread Executor");
		System.out.println("2 - Fixed Thread Executor");
		System.out.println("3 - Cached Thread Pool");

		int opcao;
		do {
			System.out.print("Opção: ");
			opcao = scannerInt.nextInt();
		} while (opcao < 1 || opcao > 3);

		switch (opcao) {
		case 1:
			return Executors.newSingleThreadExecutor();
		case 2:
			int numberThreads;
			do {
				System.out.print("Digite a quantidade de threads: ");
				numberThreads = scannerInt.nextInt();
			} while (numberThreads < 1);
			return Executors.newFixedThreadPool(numberThreads);
		case 3:
			return Executors.newCachedThreadPool();
		}
		return null;
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

					// Recebe e ecoa os dados do cliente:
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
					// Envia o que havia escrito no buffer quando parou de receber dados:
					if (i > 0) {
						outputStream.write(buffer);
					}
					outputStream.write(-1);
				}

				clientSocket.close();
				System.out.println("Cliente " + clientSocket.getInetAddress().getHostAddress() + " desconectado.");
			} catch (Exception e) {
				System.out.println("Ocorreu um erro inesperado durante a conexão com o cliente "
						+ clientSocket.getInetAddress().getHostAddress());
			}
		}

	}
}
