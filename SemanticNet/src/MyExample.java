import java.util.Vector;

public class MyExample {
	public static void main(String args[]){
		SemanticNet sn = new SemanticNet();

		sn.addLink(new Link("is-a","花子","帝京大学の学生",sn));
		sn.addLink(new Link("is","花子","女性",sn));
		sn.addLink(new Link("like","花子","犬",sn));
		sn.addLink(new Link("owns","花子","自転車",sn));
		sn.addLink(new Link("has","自転車","ブレーキ",sn));

		sn.addLink(new Link("is-a","太郎","帝京大学の学生",sn));
		sn.addLink(new Link("is","太郎","男性",sn));
		sn.addLink(new Link("like","太郎","サッカー",sn));
		sn.addLink(new Link("owns","太郎","フェラーリ",sn));
		sn.addLink(new Link("is-a","フェラーリ","車",sn));
		sn.addLink(new Link("has","車","エンジン",sn));
		sn.addLink(new Link("needs","車","ガソリン",sn));

		sn.addLink(new Link("is","帝京大学の学生","学生",sn));
		sn.addLink(new Link("like","学生","映画",sn));
		sn.addLink(new Link("like","学生","勉強",sn));
		sn.addLink(new Link("like","学生","ゲーム",sn));

		sn.printLinks();
		sn.printNodes();

		Vector query=new Vector();
		query.addElement(new Link("like","?y","犬"));
		query.addElement(new Link("owns","花子","?y"));
		query.addElement(new Link("is","?y","男性"));
		query.addElement(new Link("?y","太郎","サッカー"));
		query.addElement(new Link("has","?y","エンジン"));

		sn.doQuery(query);
	}
}
