import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;


public class OtherSocketProcess {
	
	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("localhost", 6635);

		BufferedReader listener = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		BufferedWriter sender = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

		while(true) {
			String data;
			while((data = listener.readLine()) != null) {
				sender.write(data);
				sender.newLine();
				sender.flush();
			}
		}
	}
}
