import java.util.Hashtable;
import java.util.StringTokenizer;

public class Matcher {
	StringTokenizer st1;
	StringTokenizer st2;
	Hashtable vars;

	Matcher(){
		vars = new Hashtable();
	}

	public boolean matching(String string1,String string2){
		st1 = new StringTokenizer(string1);
		st2 = new StringTokenizer(string2);

		for(int i=0;i<st1.countTokens();){
			if(!tokenMatching(st1.nextToken(),st2.nextToken())){
				// トークンが一つでもマッチングに失敗したら失敗
				return false;
			}
		}

		System.out.println(vars.toString());
		return true;
	}

	boolean tokenMatching(String token1,String token2){
		//文字列が同じの場合、trueをリターンする
		if(token1.equals(token2)){
			return true;
		}

		// 一つが変数、一つが変数ではない場合
		if(var(token1) && !var(token2)){
			return varMatching(token1,token2);
		}

		// 同上
		if(!var(token1) && var(token2)){
			return varMatching(token2,token1);
		}

		// 両方が変数の場合、falseをリターンする
		return false;
	}

	boolean varMatching(String vartoken,String token){
		// 変数が束縛されるか判断
		if(vars.containsKey(vartoken)){
			// 定数が束縛した変数の値と同じの場合、trueをリターンする
			if(token.equals(vars.get(vartoken))){
				return true;
			}else{
				return false;
			}
		}else{
			// 変数を束縛する
			vars.put(vartoken, token);
		}
		return true;
	}

	boolean var(String str1){
		// 先頭が？なら変数
		return str1.startsWith("?");
	}


}
