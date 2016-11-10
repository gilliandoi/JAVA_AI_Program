import java.util.Hashtable;
import java.util.Vector;

/*
 * ノード
 */
public class Node {
	// 名
	String name;

	// 子ノード
	Vector children = new Vector();

	// 子コスト
	Hashtable childrenCosts = new Hashtable();

	// 解表示のためのポインタ
	Node pointer;
	// コスト
	int gValue;
	// ヒューリスティック
	int hValue;
	// 評価値
	int fValue;
	int cost;
	boolean hasGvalue = false;
	boolean hasFvalue = false;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vector getChildren() {

		return children;
	}

	public void setChildren(Vector children) {
		this.children = children;
	}

	public Hashtable getChildrenCosts() {
		return childrenCosts;
	}

	public void setChildrenCosts(Hashtable childrenCosts) {
		this.childrenCosts = childrenCosts;
	}

	public Node getPointer() {
		return pointer;
	}

	public void setPointer(Node pointer) {
		this.pointer = pointer;
	}

	public int getgValue() {
		return gValue;
	}

	public void setgValue(int gValue) {
		this.gValue = gValue;
	}

	public int gethValue() {
		return hValue;
	}

	public void sethValue(int hValue) {
		this.hValue = hValue;
	}

	public int getfValue() {
		return fValue;
	}

	public void setfValue(int fValue) {
		this.fValue = fValue;
	}

	public boolean isHasGvalue() {
		return hasGvalue;
	}

	public void setHasGvalue(boolean hasGvalue) {
		this.hasGvalue = hasGvalue;
	}

	public boolean isHasFvalue() {
		return hasFvalue;
	}

	public void setHasFvalue(boolean hasFvalue) {
		this.hasFvalue = hasFvalue;
	}

	public int getCost(Node node) {
		return Integer.parseInt(childrenCosts.get(node).toString());
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	/*
	 * TODO
	 */
	public Node(String theName, int theHValue) {
		name = theName;
		hValue = theHValue;
	}

	/*
	 * 子ノードを付ける
	 */
	public void addChild(Node theChild, int theCost) {
		children.addElement(theChild);
		childrenCosts.put(theChild, new Integer(theCost));
	}
}
