import java.util.Hashtable;
import java.util.Vector;

public class Link {
	String label;
	Node tail;
	Node head;
	boolean inheritance;

	// 登録用メッソド
	Link(String theLabel, String theTail, String theHead, SemanticNet sn) {
		label = theLabel;
		Hashtable nodesNameTable = sn.getNodesNameTable();
		Vector nodes = sn.getNodes();

		tail = (Node) nodesNameTable.get(theTail);
		if (tail == null) {
			tail = new Node(theTail);
			nodes.addElement(tail);
			nodesNameTable.put(theTail, tail);
		}

		head = (Node) nodesNameTable.get(theHead);
		if (head == null) {
			head = new Node(theHead);
			nodes.addElement(head);
			nodesNameTable.put(theHead, head);
		}
		inheritance = false;
	}

	// 登録用メッソド
	Link(String theLabel, String theTail, String theHead, Vector nodes, Hashtable nodesNameTable) {
		tail = (Node) nodesNameTable.get(theTail);
		if (tail == null) {
			tail = new Node(theTail);
			nodes.addElement(tail);
			nodesNameTable.put(theTail, tail);
		}

		head = (Node) nodesNameTable.get(theHead);
		if (head == null) {
			head = new Node(theHead);
			nodes.addElement(head);
			nodesNameTable.put(theHead, head);
		}
	}

	// 質問用メッソド
	Link(String theLabel, String theTail, String theHead) {
		label = theLabel;
		tail = new Node(theTail);
		head = new Node(theHead);
		inheritance = false;
	}

	public String toString() {
		String result = tail.getName() + " " + label + " " + head.getName();
		if (!inheritance) {
			return result;
		} else {
			return "(" + result + ")";
		}
	}

	/**
	 * @return label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            セットする label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return tail
	 */
	public Node getTail() {
		return tail;
	}

	/**
	 * @param tail
	 *            セットする tail
	 */
	public void setTail(Node tail) {
		this.tail = tail;
	}

	/**
	 * @return head
	 */
	public Node getHead() {
		return head;
	}

	/**
	 * @param head
	 *            セットする head
	 */
	public void setHead(Node head) {
		this.head = head;
	}

	/**
	 * @return inheritance
	 */
	public boolean isInheritance() {
		return inheritance;
	}

	/**
	 * @param inheritance
	 *            セットする inheritance
	 */
	public void setInheritance(boolean inheritance) {
		this.inheritance = inheritance;
	}

}
