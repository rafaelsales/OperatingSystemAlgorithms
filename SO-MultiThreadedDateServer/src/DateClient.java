import java.net.*;
import java.io.*;

public class DateClient {
	public static void main(String[] args) {
		try {
			Socket clientSocket = new Socket("127.0.0.1", 6013);
			InputStream inputStream = clientSocket.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

			String line;
			while ((line = bufferedReader.readLine()) != null) {
				System.out.println(line);
			}
			clientSocket.close();
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}
