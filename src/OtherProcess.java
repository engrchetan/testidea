import java.io.BufferedReader;
import java.io.InputStreamReader;


public class OtherProcess {
	
	public static void main(String[] args) throws Exception {
		BufferedReader listener = new BufferedReader(new InputStreamReader(System.in));
		while(true) {
			String data;
			while((data = listener.readLine()) != null) {
				System.out.println(data);
			}
		}
	}
}
