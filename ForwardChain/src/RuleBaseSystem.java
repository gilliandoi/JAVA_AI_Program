import java.io.FileReader;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * RuleBaseSystem
 *
 */
public class RuleBaseSystem {
	static RuleBase rb;

	public static void main(String args[]) {
		rb = new RuleBase();
		rb.forwardChain();
	}
}

/**
 * ワーキングメモリを表すクラス．
 *
 *
 */
class WorkingMemory {
	ArrayList<String> assertions;

	WorkingMemory() {
		assertions = new ArrayList<String>();
	}

	/**
	 * マッチするアサーションに対するバインディング情報を返す （再帰的）
	 *
	 * @param 前件を示す
	 *            ArrayList
	 * @return バインディング情報が入っている ArrayList
	 */
	public Vector matchingAssertions(Vector theAntecedents) {
		// 複数の変数情報を保持するリスト
		Vector bindings = new Vector();
		return matchable(theAntecedents, 0, bindings);
	}

	private Vector matchable(Vector theAntecedents, int n,
			Vector bindings) {
		if (n == theAntecedents.size()) {
			return bindings;
		} else if (n == 0) {
			//可能な変数候補を生成
			boolean success = false;
			for (int i = 0; i < assertions.size(); i++) {
				HashMap<String, String> binding = new HashMap<String, String>();
				if ((new Matcher()).matching((String) theAntecedents.get(n),
						(String) assertions.get(i), binding)) {
					bindings.add(binding);
					success = true;
				}
			}
			if (success) {
				return matchable(theAntecedents, n + 1, bindings);
			} else {
				return null;
			}
		} else {
			boolean success = false;
			Vector newBindings = new Vector();
			for (int i = 0; i < bindings.size(); i++) {
				for (int j = 0; j < assertions.size(); j++) {
					if ((new Matcher()).matching(
							(String) theAntecedents.get(n),
							(String) assertions.get(j),
							(HashMap) bindings.get(i))) {
						newBindings.add(bindings.get(i));
						success = true;
					}
				}
			}
			if (success) {
				return matchable(theAntecedents, n + 1, newBindings);
			} else {
				return null;
			}
		}
	}

	/**
	 * アサーションをワーキングメモリに加える．
	 *
	 * @param アサーションを表す
	 *            String
	 */
	public void addAssertion(String theAssertion) {
		System.out.println("ADD:" + theAssertion);
		assertions.add(theAssertion);
	}

	/**
	 * 指定されたアサーションがすでに含まれているかどうかを調べる．
	 *
	 * @param アサーションを表す
	 *            String
	 * @return 含まれていれば true，含まれていなければ false
	 */
	public boolean contains(String theAssertion) {
		return assertions.contains(theAssertion);
	}

	/**
	 * ワーキングメモリの情報をストリングとして返す．
	 *
	 * @return ワーキングメモリの情報を表す String
	 */
	public String toString() {
		return assertions.toString();
	}

}

/**
 * ルールベースを表すクラス．
 * ルールを管理する
 *
 */
class RuleBase {
	String fileName;
	FileReader f;
	StreamTokenizer st;
	WorkingMemory wm;
	ArrayList<Rule> rules;

	/**
	 * ワーキングメモリ初期化（ルールベース取得）
	 */
	RuleBase() {
		fileName = "src/AnimalMaster.data";
		wm = new WorkingMemory();

		// my-pet
		wm.addAssertion("my-pet like to eat fish");
		wm.addAssertion("my-pet can highly jump");
		// Joe's-pet
		wm.addAssertion("Joe's-pet like to play with people");
		wm.addAssertion("Joe's-pet can help people to do some jobs");
		// Tramp's-pet
		wm.addAssertion("Tramp's-pet can fly");
		wm.addAssertion("Tramp's-pet has wings");
		wm.addAssertion("Tramp's-pet is black");
		wm.addAssertion("Tramp's-pet lives at Road4");
		rules = new ArrayList<Rule>();
		loadRules(fileName);
	}

	/**
	 * 前向き推論を行うためのメソッド
	 *
	 */
	public void forwardChain() {
		boolean newAssertionCreated;
		// 新しいアサーションが生成されなくなるまで続ける．
		do {
			newAssertionCreated = false;
			for (int i = 0; i < rules.size(); i++) {
				Rule aRule = (Rule) rules.get(i);
				System.out.println("apply rule:" + aRule.getName());
				//前件
				Vector antecedents = aRule.getAntecedents();
				//後件
				String consequent = aRule.getConsequent();
				Vector bindings = wm.matchingAssertions(antecedents);
				if (bindings != null) {
					for (int j = 0; j < bindings.size(); j++) {
						// 後件をインスタンシエーション（具体化）
						String newAssertion = instantiate((String) consequent,
								(HashMap) bindings.get(j));
						// ワーキングメモリーになければ成功
						if (!wm.contains(newAssertion)) {
							System.out.println("Success: " + newAssertion);
							wm.addAssertion(newAssertion);
							newAssertionCreated = true;
						}
					}
				}
			}
			System.out.println("Working Memory" + wm);
		} while (newAssertionCreated);
		System.out.println("No rule produces a new assertion");
	}

	private String instantiate(String thePattern, HashMap theBindings) {
		String result = new String();
		StringTokenizer st = new StringTokenizer(thePattern);
		for (int i = 0; i < st.countTokens();) {
			String tmp = st.nextToken();
			// 変数かどうかの判断
			if (var(tmp)) {
				result = result + " " + (String) theBindings.get(tmp);
			} else {
				result = result + " " + tmp;
			}
		}
		return result.trim();
	}

	private boolean var(String str1) {
		// 先頭が ? なら変数
		return str1.startsWith("?");
	}

	/**
	 * ルールファイルを読込む
	 * @param theFileName
	 */
	private void loadRules(String theFileName) {
		String line;
		try {
			int token;
			f = new FileReader(theFileName);
			st = new StreamTokenizer(f);
			while ((token = st.nextToken()) != StreamTokenizer.TT_EOF) {
				switch (token) {
				case StreamTokenizer.TT_WORD:
					String name = null;
					Vector antecedents = null;
					String consequent = null;
					if ("rule".equals(st.sval)) {
						st.nextToken();
						// if(st.nextToken() == '"'){
						name = st.sval;
						st.nextToken();
						if ("if".equals(st.sval)) {
							antecedents = new Vector();
							st.nextToken();
							while (!"then".equals(st.sval)) {
								antecedents.add(st.sval);
								st.nextToken();
							}
							if ("then".equals(st.sval)) {
								st.nextToken();
								consequent = st.sval;
							}
						}
						// }
					}
					// ルールの生成
					rules.add(new Rule(name, antecedents, consequent));
					break;
				default:
					System.out.println(token);
					break;
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		for (int i = 0; i < rules.size(); i++) {
			System.out.println(((Rule) rules.get(i)).toString());
		}
	}
}

/**
 * ルールを表すクラス．
 *
 *
 */
class Rule {
	String name;
	Vector antecedents;
	String consequent;

	Rule(String theName, Vector theAntecedents, String theConsequent) {
		this.name = theName;
		this.antecedents = theAntecedents;
		this.consequent = theConsequent;
	}

	/**
	 * ルールの名前を返す．
	 *
	 * @return 名前を表す String
	 */
	public String getName() {
		return name;
	}

	/**
	 * ルールをString形式で返す
	 *
	 * @return ルールを整形したString
	 */
	public String toString() {
		return name + " " + antecedents.toString() + "->" + consequent;
	}

	/**
	 * ルールの前件を返す．
	 *
	 * @return 前件を表す ArrayList
	 */
	public Vector getAntecedents() {
		return antecedents;
	}

	/**
	 * ルールの後件を返す．
	 *
	 * @return 後件を表す String
	 */
	public String getConsequent() {
		return consequent;
	}

}