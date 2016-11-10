import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class Unifier {
	StringTokenizer st1;
	StringTokenizer st2;
	Hashtable vars;
	String buffer1[] = new String[0];
	String buffer2[] = new String[0];

	Unifier() {
		vars = new Hashtable();
	}

	public boolean unify(String string1, String string2) {
		if (string1.equals(string2)) {
			return true;
		}

		st1 = new StringTokenizer(string1);
		st2 = new StringTokenizer(string2);

		// 数が一致しない場合、falseを返す
		if (st1.countTokens() != st2.countTokens()) {
			return false;
		}

		// 定数同士
		int length = st1.countTokens();
		buffer1 = new String[length];
		buffer2 = new String[length];

		// アサーションが分割して、配列にセットする
		for (int i = 0; i < length; i++) {
			buffer1[i] = st1.nextToken();
			buffer2[i] = st2.nextToken();
		}

		for (int i = 0; i < length; i++) {
			if (!tokenMatching(buffer1[i], buffer2[i])) {
				// トークンが一つでもUnifyに失敗したら失敗
				return false;
			}
		}

		System.out.println(vars.toString());
		return true;
	}

	boolean tokenMatching(String token1, String token2) {

		// 文字列が同じの場合、trueをリターンする
		if (token1.equals(token2)) {
			return true;
		}

		// 一つが変数、一つが変数ではない場合
		if (var(token1) && !var(token2)) {
			return varMatching(token1, token2);
		}

		// 同上
		if (!var(token1) && var(token2)) {
			return varMatching(token2, token1);
		}

		// 両方が変数の場合
		if (var(token1) && var(token2)) {
			return varMatching(token2, token1);
		}

		return false;
	}

	boolean varMatching(String vartoken, String token) {
		// 変数が束縛されるか判断
		if (vars.containsKey(vartoken)) {
			// 定数が束縛した変数の値と同じの場合、trueをリターンする
			if (token.equals(vars.get(vartoken))) {
				return true;
			} else {
				return false;
			}
		} else {
			// 置き換え処理
			replaceBuffer(vartoken, token);
			if (vars.contains(vartoken)) {
				// hashmapに格納されている変数が出た場合
				replaceBindings(vartoken, token);
			}

			// 変数を束縛する
			vars.put(vartoken, token);
		}
		return true;
	}

	boolean var(String str1) {
		// 先頭が？なら変数
		return str1.startsWith("?");
	}

	void replaceBuffer(String preString, String postString) {
		for (int i = 0; i < buffer1.length; i++) {
			if (preString.equals(buffer1[i])) {
				buffer1[i] = postString;
			}
			if (preString.equals(buffer2[i])) {
				buffer2[i] = postString;
			}
		}
	}

	void replaceBindings(String preString, String postString) {
		Enumeration keys;
		for (keys = vars.keys(); keys.hasMoreElements();) {
			String key = (String) keys.nextElement();
			if (preString.equals(vars.get(key))) {
				vars.put(key, postString);
			}
		}
	}
}
