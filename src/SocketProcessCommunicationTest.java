import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class SocketProcessCommunicationTest {

	private static final int ITERATION = 1 * 100 * 1000;
	private static final String PAYLOAD = "fgsfgjdskjgsdjghsdjfghafjegfdgjhskghsghoihiothoirbrnnbotrnositngrtubrbntrihotsijhrtibionsbirtnbirsntbn";
	
	public static void main(String[] args) throws Exception {
		String javaHome = System.getProperty("java.home");
        String javaBin = javaHome +
                File.separator + "bin" +
                File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String className = OtherSocketProcess.class.getCanonicalName();

        ProcessBuilder builder = new ProcessBuilder(
                javaBin, "-cp", classpath, className);
        builder.inheritIO();

        final ServerSocket server = new ServerSocket(6635);
        Future<Socket> socketFuture = Executors.newSingleThreadExecutor().submit(new Callable<Socket>(){

			@Override
			public Socket call() throws Exception {
				return server.accept();
			}});
        final Process process = builder.start();
        
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				process.destroy();
			}
        }));
                
        Socket socket = socketFuture.get();
        BufferedReader listener = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		BufferedWriter sender = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		String HUGE_PAYLOAD = PAYLOAD;
		for(int i = 0; i < 70; i++) {
			HUGE_PAYLOAD += PAYLOAD;
		}
		long start = System.nanoTime();
		for(int i = 1; i <= ITERATION; i++) {
			sender.write(HUGE_PAYLOAD + ":" + Integer.toString(i));
			sender.newLine();
			sender.flush();
			String data;
			while((data = listener.readLine()) != null) {
				if(data.endsWith(":" + i)) {
					break;
				}
			}
		}
		long end = System.nanoTime();
		
		System.out.println("Took total " + (end - start) + " ns for iterations " + ITERATION);
		System.out.println("Average round trip " + (end - start) / ITERATION + " ns");

		socket.close();
		server.close();
		process.destroy();
	}
}
