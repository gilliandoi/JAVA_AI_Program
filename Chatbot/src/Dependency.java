import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class Dependency {

	static String cabochaCmd = "C:/Program Files (x86)/CaboCha/bin/cabocha";
	static String encoding = "SHIFT-JIS";

	// String型で文字列を受け取り、それを係り受け解析する関数
	public static void analyzeDependency(String text) {
		try {
			String result = "";

			/** Caution!!! Need a Full path to CaboCha */
			Process prcs = Runtime.getRuntime().exec("C:/Program Files (x86)/CaboCha/bin/cabocha -f4");
			OutputStreamWriter writer = new OutputStreamWriter(prcs.getOutputStream(), "SHIFT-JIS");
			PrintWriter printWriter = new PrintWriter(writer);

			InputStreamReader reader = new InputStreamReader(prcs.getInputStream(), "SHIFT-JIS");
			BufferedReader buffreader = new BufferedReader(reader);

			printWriter.println(text); // MeCabに文字列を送る
			printWriter.flush();

			while ((result = buffreader.readLine()) != null) {
				System.out.println(result);
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
