import java.util.ArrayList;
import java.util.Iterator;

public class Chatbot {
	public static String analyzeChat(ArrayList<Morpheme> morphs) {
		String verbBase = null;
		String nounBase = null;
		for (Iterator<Morpheme> i = morphs.iterator(); i.hasNext();) {
			Morpheme morph = i.next();
			// 動詞なら、原型を取り出し
			if (morph.isVerb()) {
				verbBase = morph.getBaseform();
			} else if (nounBase == null && morph.isNoun()) {
				nounBase = morph.getSurface();
			}
		}

		String response = "";
		if (verbBase != null) {
			response = "え、" + verbBase + "んですか？";
		}

		if (nounBase != null) {
			response += nounBase + "ですか？";
		}

		if (response != "") {
			return response;
		}
		return "すみません、意味が分かりません。";
	}
}
