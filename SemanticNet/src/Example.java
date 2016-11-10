import java.util.Vector;

public class Example {
	public static void main(String args[]){
		SemanticNet sn = new SemanticNet();

		sn.addLink(new Link("is-a","baseball","sport",sn));
		sn.addLink(new Link("is-a","Taro","NIT-student",sn));
		sn.addLink(new Link("speciality","Taro","AI",sn));
		sn.addLink(new Link("is-a","Ferrari","car",sn));
		sn.addLink(new Link("has-a","car","engine",sn));
		sn.addLink(new Link("hobby","Taro","baseball",sn));
		sn.addLink(new Link("own","Taro","Ferrari",sn));
		sn.addLink(new Link("is-a","NIT-student","student",sn));
		sn.addLink(new Link("donot","student","study",sn));

		sn.printLinks();
		sn.printNodes();

		Vector query=new Vector();
		query.addElement(new Link("own","?y","Ferrari"));
		query.addElement(new Link("is-a","?y","student"));
		query.addElement(new Link("hobby","?y","baseball"));
		sn.doQuery(query);
	}
}
