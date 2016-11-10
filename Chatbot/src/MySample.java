import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MySample {
	static private HashMap<String, Integer> cnt = new HashMap<String, Integer>();

	public static void main(String[] args) {
		// MeCabを起動
		Morpheme.startMeCab();

		// ここに追加
		// 文字列を自由化。入力を受け取る
		Scanner stdIn = new Scanner(System.in);
		System.out.print("Ask： ");
		String strinput = stdIn.next();

		// 形態素解析
		ArrayList<Morpheme> morphs = new ArrayList<Morpheme>();
//		System.out.println("\n形態素解析：");
		morphs = morpheme(strinput);

		// 係り受け解析
//		System.out.println("\n係り受け解析：");
//		Dependency.analyzeDependency(strinput);

		// 応答文生成
		System.out.println("\nQuest：");
		String response = Chatbot.analyzeChat(morphs);
		System.out.println(response);
	}

	/**
	 * 形態素解析
	 *
	 * @param str
	 */
	static ArrayList<Morpheme> morpheme(String str) {
		ArrayList<Morpheme> morphs = Morpheme.analyzeMorpheme(str);

		for (int i = 0; i < morphs.size(); i++) {
			// 形態素を1つずつmorphに格納するループ
			Morpheme morph = morphs.get(i);
//			System.out.println(morph);
		}

		return morphs;
	}

	static void counter(String str) {
		if (!cnt.containsKey(str)) {
			cnt.put(str, 1);
		} else {
			cnt.put(str, cnt.get(str) + 1);
		}
	}
}
