import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ProcessBuilder.Redirect;


public class SubProcessCommunicationTest {

	private static final int ITERATION = 1 * 100 * 1000;
	private static final String PAYLOAD = "fgsfgjdskjgsdjghsdjfghafjegfdgjhskghsghoihiothoirbrnnbotrnositngrtubrbntrihotsijhrtibionsbirtnbirsntbn";

	public static void main(String[] args) throws Exception {
		String javaHome = System.getProperty("java.home");
        String javaBin = javaHome +
                File.separator + "bin" +
                File.separator + "java";
        String classpath = System.getProperty("java.class.path");
        String className = OtherProcess.class.getCanonicalName();

        ProcessBuilder builder = new ProcessBuilder(
                javaBin, "-cp", classpath, className);
        builder.redirectError(Redirect.INHERIT);
        final Process process = builder.start();
        
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				process.destroy();
			}
        }));
        
        BufferedReader listener = new BufferedReader(new InputStreamReader(process.getInputStream()));
		BufferedWriter sender = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
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

		process.destroy();
	}
}
