import java.io.FileReader;
import java.io.Serializable;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

public class RuleBaseSystem {
	static RuleBase rb;
	static FileManager fm;

	public static void main(String args[]) {
		if (args.length != 1) {
			System.out.println("Usage: %java RuleBaseSystem [query strings]");
			System.out.println("Example:");
			System.out.println(" \"?x is b\" and \"?x is c\" are queries");
			System.out.println("  %java RuleBaseSystem \"?x is b,?x is c\"");
		} else {
			fm = new FileManager();
			Vector rules = fm.loadRules("src/AnimalMaster.data");
			Vector wm = fm.loadWm("src/AnimalMasterWm.data");
			rb = new RuleBase(rules, wm);
			StringTokenizer st = new StringTokenizer(args[0], ",");
			Vector queries = new Vector();
			for (int i = 0; i < st.countTokens();) {
				queries.add(st.nextToken());
			}
			rb.backwardChain(queries);
		}
	}
}

class RuleBase implements Serializable {
	String fileName;
	Vector wm;
	Vector rules;

	RuleBase(Vector theRules, Vector theWm) {
		wm = theWm;
		rules = theRules;
	}

	public void setWm(Vector theWm) {
		wm = theWm;
	}

	public void setRules(Vector theRules) {
		rules = theRules;
	}

	/**
	 * 後ろ向き推論
	 * @param hypothesis 仮説
	 */
	public void backwardChain(Vector hypothesis) {
		System.out.println("Hypothesis:" + hypothesis);
		Vector orgQueries = (Vector) hypothesis.clone();
		Hashtable binding = new Hashtable();
		if (matchingPatterns(hypothesis, binding)) {
			System.out.println("Yes");
			System.out.println(binding);
			// 最終的な結果を基のクェリーに代入して表示する
			for (int i = 0; i < orgQueries.size(); i++) {
				String aQuery = (String) orgQueries.get(i);
				System.out.println("binding: " + binding);
				String anAnswer = instantiate(aQuery, binding);
				System.out.println("Query: " + aQuery);
				System.out.println("Answer:" + anAnswer);
			}
		} else {
			System.out.println("No");
		}
	}

	/**
	 * マッチするワーキングメモリのアサーションとルールの後件 に対するバインディング情報を返す
	 */
	private boolean matchingPatterns(Vector thePatterns,
			Hashtable theBinding) {
		String firstPattern;
		if (thePatterns.size() == 1) {
			firstPattern = (String) thePatterns.get(0);
			if (matchingPatternOne(firstPattern, theBinding, 0) != -1) {
				return true;
			} else {
				return false;
			}
		} else {
			firstPattern = (String) thePatterns.get(0);
			thePatterns.remove(0);

			int cPoint = 0;
			while (cPoint < wm.size() + rules.size()) {
				// 元のバインディングを取っておく
				HashMap<String, String> orgBinding = new HashMap<String, String>();
				for (Iterator<String> i = theBinding.keySet().iterator(); i
						.hasNext();) {
					String key = i.next();
					String value = (String) theBinding.get(key);
					orgBinding.put(key, value);
				}
				int tmpPoint = matchingPatternOne(firstPattern, theBinding,
						cPoint);
				System.out.println("tmpPoint: " + tmpPoint);
				if (tmpPoint != -1) {
					System.out.println("Success:" + firstPattern);
					if (matchingPatterns(thePatterns, theBinding)) {
						// 成功
						return true;
					} else {
						// 失敗
						// choiceポイントを進める
						cPoint = tmpPoint;
						// 失敗したのでバインディングを戻す
						theBinding.clear();
						for (Iterator<String> i = orgBinding.keySet()
								.iterator(); i.hasNext();) {
							String key = i.next();
							String value = orgBinding.get(key);
							theBinding.put(key, value);
						}
					}
				} else {
					// 失敗したのでバインディングを戻す
					theBinding.clear();
					for (Iterator<String> i = orgBinding.keySet().iterator(); i
							.hasNext();) {
						String key = i.next();
						String value = orgBinding.get(key);
						theBinding.put(key, value);
					}
					return false;
				}
			}
			return false;
			/*
			 * if(matchingPatternOne(firstPattern,theBinding)){ return
			 * matchingPatterns(thePatterns,theBinding); } else { return false;
			 * }
			 */
		}
	}

	/**
	 * バックトラック
	 * ある推論が成功しなかった場合、一つ前の推論ステップに戻り、他の解を探す。
	 * @param thePattern
	 * @param theBinding
	 * @param cPoint
	 * @return
	 */
	private int matchingPatternOne(String thePattern,
			Hashtable theBinding, int cPoint) {
		if (cPoint < wm.size()) {
			// WME(Working Memory Elements) と Unify してみる．
			for (int i = cPoint; i < wm.size(); i++) {
				if ((new Unifier()).unify(thePattern, (String) wm.get(i),
						theBinding)) {
					System.out.println("Success WM");
					System.out.println((String) wm.get(i) + " <=> "
							+ thePattern);
					return i + 1;
				}
			}
		}
		if (cPoint < wm.size() + rules.size()) {
			// Ruleと Unify してみる．
			for (int i = cPoint; i < rules.size(); i++) {
				Rule aRule = rename((Rule) rules.get(i));
				// 元のバインディングを取っておく．
				HashMap<String, String> orgBinding = new HashMap<String, String>();
				for (Iterator<String> itr = theBinding.keySet().iterator(); itr
						.hasNext();) {
					String key = (String)itr.next();
					String value = (String)theBinding.get(key);
					orgBinding.put(key, value);
				}
				if ((new Unifier()).unify(thePattern,
						(String) aRule.getConsequent(), theBinding)) {
					System.out.println("Success RULE");
					System.out.println("Rule:" + aRule + " <=> " + thePattern);
					// さらにbackwardChaining
					Vector newPatterns = aRule.getAntecedents();
					if (matchingPatterns(newPatterns, theBinding)) {
						return wm.size() + i + 1;
					} else {
						// 失敗したら元に戻す．
						theBinding.clear();
						for (Iterator<String> itr = orgBinding.keySet()
								.iterator(); itr.hasNext();) {
							String key = itr.next();
							String value = orgBinding.get(key);
							theBinding.put(key, value);
						}
					}
				}
			}
		}
		return -1;
	}

	/**
	 * 与えられたルールの変数をリネームしたルールのコピーを返す．
	 *
	 * @param 変数をリネームしたいルール
	 * @return 変数がリネームされたルールのコピーを返す．
	 */
	int uniqueNum = 0;

	private Rule rename(Rule theRule) {
		Rule newRule = theRule.getRenamedRule(uniqueNum);
		uniqueNum = uniqueNum + 1;
		return newRule;
	}

	private String instantiate(String thePattern,
			Hashtable theBindings) {
		String result = new String();
		StringTokenizer st = new StringTokenizer(thePattern);
		for (int i = 0; i < st.countTokens();) {
			String tmp = st.nextToken();
			if (var(tmp)) {
				result = result + " " + (String) theBindings.get(tmp);
				System.out.println("tmp: " + tmp + ", result: " + result);
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
}

class FileManager {
	FileReader f;
	StreamTokenizer st;

	public Vector loadRules(String theFileName) {
		Vector rules = new Vector();
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
					}
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
		return rules;
	}

	public Vector loadWm(String theFileName) {
		Vector wm = new Vector();
		String line;
		try {
			int token;
			f = new FileReader(theFileName);
			st = new StreamTokenizer(f);
			st.eolIsSignificant(true);
			st.wordChars('\'', '\'');
			while ((token = st.nextToken()) != StreamTokenizer.TT_EOF) {
				line = new String();
				while (token != StreamTokenizer.TT_EOL) {
					line = line + st.sval + " ";
					token = st.nextToken();
				}
				wm.add(line.trim());
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return wm;
	}
}

/**
 * ルールを表すクラス．
 */
class Rule implements Serializable {
	String name;
	Vector antecedents;
	String consequent;

	Rule(String theName, Vector theAntecedents, String theConsequent) {
		this.name = theName;
		this.antecedents = theAntecedents;
		this.consequent = theConsequent;
	}

	public Rule getRenamedRule(int uniqueNum) {
		ArrayList<String> vars = new ArrayList<String>();
		for (int i = 0; i < antecedents.size(); i++) {
			String antecedent = (String) this.antecedents.get(i);
			vars = getVars(antecedent, vars);
		}
		vars = getVars(this.consequent, vars);
		HashMap<String, String> renamedVarsTable = makeRenamedVarsTable(vars,
				uniqueNum);

		Vector newAntecedents = new Vector();
		for (int i = 0; i < antecedents.size(); i++) {
			String newAntecedent = renameVars((String) antecedents.get(i),
					renamedVarsTable);
			newAntecedents.add(newAntecedent);
		}
		String newConsequent = renameVars(consequent, renamedVarsTable);

		Rule newRule = new Rule(name, newAntecedents, newConsequent);
		return newRule;
	}

	private ArrayList<String> getVars(String thePattern, ArrayList<String> vars) {
		StringTokenizer st = new StringTokenizer(thePattern);
		for (int i = 0; i < st.countTokens();) {
			String tmp = st.nextToken();
			if (var(tmp)) {
				vars.add(tmp);
			}
		}
		return vars;
	}

	private HashMap<String, String> makeRenamedVarsTable(
			ArrayList<String> vars, int uniqueNum) {
		HashMap<String, String> result = new HashMap<String, String>();
		for (int i = 0; i < vars.size(); i++) {
			String newVar = (String) vars.get(i) + uniqueNum;
			result.put((String) vars.get(i), newVar);
		}
		return result;
	}

	private String renameVars(String thePattern,
			HashMap<String, String> renamedVarsTable) {
		String result = new String();
		StringTokenizer st = new StringTokenizer(thePattern);
		for (int i = 0; i < st.countTokens();) {
			String tmp = st.nextToken();
			if (var(tmp)) {
				result = result + " " + renamedVarsTable.get(tmp);
			} else {
				result = result + " " + tmp;
			}
		}
		return result.trim();
	}

	private boolean var(String str) {
		// 先頭が ? なら変数
		return str.startsWith("?");
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return name + " " + antecedents.toString() + "->" + consequent;
	}

	public Vector getAntecedents() {
		return antecedents;
	}

	public String getConsequent() {
		return consequent;
	}
}