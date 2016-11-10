import java.util.Vector;

public class Node {
	String name;

	// 自分から出て行くリンク
	Vector<Link> departFromMeLinks;
	// 自分に入ってくるリンク
	Vector<Link> arriveAtMeLinks;

	// Node初期化
	Node(String theString) {
		name = theString;
		departFromMeLinks = new Vector<Link>();
		arriveAtMeLinks = new Vector<Link>();
	}

	public Vector getISATails() {
		Vector isaTails = new Vector();
		for (int i = 0; i < arriveAtMeLinks.size(); i++) {
			Link theLink = (Link) arriveAtMeLinks.elementAt(i);
			// "is-a"リンクの場合、子属性を取得する
			if ("is-a".equals(theLink.getLabel())) {
				isaTails.addElement(theLink.getTail());
			}
		}
		// "is-a"リンクの子属性をリターンする
		return isaTails;
	}

	public void addDepartFromMeLinks(Link theLink) {
		departFromMeLinks.addElement(theLink);
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            セットする name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return departFromMeLinks
	 */
	public Vector getDepartFromMeLinks() {
		return departFromMeLinks;
	}

	/**
	 * @param departFromMeLinks
	 *            セットする departFromMeLinks
	 */
	public void setDepartFromMeLinks(Vector departFromMeLinks) {
		this.departFromMeLinks = departFromMeLinks;
	}

	/**
	 * @return arriveAtMeLinks
	 */
	public Vector getArriveAtMeLinks() {
		return arriveAtMeLinks;
	}

	/**
	 * @param arriveAtMeLinks
	 *            セットする arriveAtMeLinks
	 */
	public void setArriveAtMeLinks(Vector arriveAtMeLinks) {
		this.arriveAtMeLinks = arriveAtMeLinks;
	}

}
