import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class SemanticNet {
	Vector links;
	Vector nodes;
	Hashtable nodesNameTable;

	SemanticNet() {
		links = new Vector();
		nodes = new Vector();
		nodesNameTable = new Hashtable();
	}

	public Vector doQuery(Vector theQueries) {
		System.out.println("----- Query -----");
		for (int i = 0; i < theQueries.size(); i++) {
			System.out.println(((Link) theQueries.elementAt(i)).toString());
		}

		Vector bindingsList = new Vector();
		for (int i = 0; i < theQueries.size(); i++) {
			Link theQuery = (Link) theQueries.elementAt(i);
			Vector bindings = queryLink(theQuery);
			if (bindings.size() != 0) {
				bindingsList.addElement(bindings);
			} else {
				return (new Vector());
			}
		}
		return join(bindingsList);
	}

	public Vector queryLink(Link theQuery) {
		Vector bindings = new Vector();

		for (int i = 0; i < links.size(); i++) {
			Link theLink = (Link) links.elementAt(i);
			Hashtable binding = new Hashtable();

			String theQueryString = theQuery.toString();
			String theLinksString = theLink.toString();
			if (new Matcher().matching(theQueryString, theLinksString, binding)) {
				bindings.addElement(binding);
			}
		}
		return bindings;
	}

	public Vector join(Vector theBindingsList) {
		int size = theBindingsList.size();
		Vector bindings1 = null;
		Vector bindings2 = null;
		switch (size) {
		case 0:
			// 失敗してる場合
			break;
		case 1:
			return (Vector) theBindingsList.elementAt(0);
		case 2:
			bindings1 = (Vector) theBindingsList.elementAt(0);
			bindings2 = (Vector) theBindingsList.elementAt(1);
			return joinBindings(bindings1, bindings2);
		default:
			bindings1 = (Vector) theBindingsList.elementAt(0);
			theBindingsList.removeElement(bindings1);
			bindings2 = joinBindings(bindings1, bindings2);
			return joinBindings(bindings1, bindings2);
		}
		// dummy return
		return (Vector) null;
	}

	public Vector joinBindings(Vector theBindings1, Vector theBindings2) {

		Vector resultBindings = new Vector();
		if (theBindings1 == null || theBindings2 == null) {
			return null;
		}
		for (int i = 0; i < theBindings1.size(); i++) {
			Hashtable theBinding1 = (Hashtable) theBindings1.elementAt(i);
			for (int j = 0; j < theBindings2.size(); j++) {
				Hashtable theBinding2 = (Hashtable) theBindings2.elementAt(j);
				Hashtable resultBinding = joinBinding(theBinding1, theBinding2);
				if (resultBinding.size() != 0) {
					resultBindings.addElement(resultBinding);
				}
			}
		}
		return resultBindings;
	}

	public Hashtable joinBinding(Hashtable theBinding1, Hashtable theBinding2) {
		Hashtable resultBinding = new Hashtable();

		// theBinding1のkeyとvalueをすべてコピー
		for (Enumeration e = theBinding1.keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			String value = (String) theBinding1.get(key);
			resultBinding.put(key, value);
		}

		// theBinding2のkeyとvalueをすべてコピー
		for (Enumeration e = theBinding2.keys(); e.hasMoreElements();) {
			String key = (String) e.nextElement();
			String value2 = (String) theBinding2.get(key);

			if (resultBinding.containsKey(key)) {
				String value1 = (String) resultBinding.get(key);

				if (!value2.equals(value1)) {
					resultBinding.clear();
					break;
				}
			}
			resultBinding.put(key, value2);
		}

		return resultBinding;
	}

	public void addLink(Link theLink) {
		Node tail = theLink.getTail();
		Node head = theLink.getHead();
		links.addElement(theLink);

		// 属性の継承
		if ("is-a".equals(theLink.getLabel())) {
			// headのすべてのリンクをis-aをたどってすべてのノードに継承
			Vector tmp = new Vector();
			tmp.addElement(tail);
			recursiveInheritance(head.getDepartFromMeLinks(), tmp);
		}

		Vector tmp = new Vector();
		tmp.addElement(theLink);
		recursiveInheritance(tmp, tail.getISATails());
		tail.addDepartFromMeLinks(theLink);
	}

	public void recursiveInheritance(Vector theInheritanceLinks, Vector theInheritanceNodes) {
		for (int i = 0; i < theInheritanceNodes.size(); i++) {
			Node theNode = (Node) theInheritanceNodes.elementAt(i);
			for (int j = 0; j < theInheritanceLinks.size(); j++) {
				Link theLink = (Link) theInheritanceLinks.elementAt(j);
				Link newLink = new Link(theLink.getLabel(), theNode.getName(), (theLink.getHead()).getName(), this);
				newLink.setInheritance(true);
				links.addElement(newLink);
				theNode.addDepartFromMeLinks(theLink);
			}

			// theNodeからis-aでたどれるノードにリンクを継承
			Vector isaTails = theNode.getISATails();
			if (isaTails.size() != 0) {
				recursiveInheritance(theInheritanceLinks, isaTails);
			}
		}
	}

	public void printLinks() {
		System.out.println("----- Links -----");
		for (int i = 0; i < links.size(); i++) {
			System.out.println(((Link) links.elementAt(i)).toString());
		}
	}

	public void printNodes() {
		System.out.println("----- Nodes -----");
		for (int i = 0; i < nodes.size(); i++) {
			System.out.println(((Node) nodes.elementAt(i)).name.toString());
		}
	}

	/**
	 * @return links
	 */
	public Vector getLinks() {
		return links;
	}

	/**
	 * @param links
	 *            セットする links
	 */
	public void setLinks(Vector links) {
		this.links = links;
	}

	/**
	 * @return nodes
	 */
	public Vector getNodes() {
		return nodes;
	}

	/**
	 * @param nodes
	 *            セットする nodes
	 */
	public void setNodes(Vector nodes) {
		this.nodes = nodes;
	}

	/**
	 * @return nodesNameTable
	 */
	public Hashtable getNodesNameTable() {
		return nodesNameTable;
	}

	/**
	 * @param nodesNameTable
	 *            セットする nodesNameTable
	 */
	public void setNodesNameTable(Hashtable nodesNameTable) {
		this.nodesNameTable = nodesNameTable;
	}

}
