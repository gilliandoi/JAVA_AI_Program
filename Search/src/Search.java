import java.util.Vector;

public class Search {

	Node node[];
	Node start;
	Node goal;

	Search() {
		// 状態空間作成
		makeStateSpace();
	}

	/*
	 * メインメソッド
	 */
	public static void main(String args[]) {

		if (args.length != 1) {
			// パラメータが未入力または入力不正の場合、ヘルプ表示
			printInfo();
		} else {
			int which = Integer.parseInt(args[0]);
			switch (which) {
			case 1:
				// 幅優先探索
				System.out.println("\n幅優先探索");
				(new Search()).breadthFirst();
				break;
			case 2:
				// 深さ優先探索
				System.out.println("\n深さ優先探索");
				(new Search()).depthFirst();
				break;
			case 3:
				// 分岐限定法
				System.out.println("\n分岐限定法");
				(new Search()).branchFirst();
				break;
			case 4:
				// 山登り法
				System.out.println("\n山登り法");
				(new Search()).hillClimbing();
				break;
			case 5:
				// 最良優先探索
				System.out.println("\n最良優先探索");
				(new Search()).bestFirst();
				break;
			case 6:
				// A*アルゴリズム
				System.out.println("\nA*アルゴリズム");
				(new Search()).aStar();
				break;
			default:
				// ヘルプ表示
				printInfo();
			}
		}
	}

	/*
	 * 状態空間の生成
	 */
	private void makeStateSpace() {
		node = new Node[10];

		// ヒューリスティック生成 h(n)
		node[0] = new Node("L.A.Airport", 0);
		start = node[0];

		node[1] = new Node("UCLA", 7);
		node[2] = new Node("Hollywood", 4);
		node[3] = new Node("Pasadena", 4);
		node[4] = new Node("San Diego", 2);
		node[5] = new Node("Downtown", 3);
		node[6] = new Node("Anaheim", 6);
		node[7] = new Node("Disneyland", 2);
		node[8] = new Node("Grand Canyou", 1);

		node[9] = new Node("Las Vegas", 0);
		goal = node[9];

		// コストを追加 g(n)
		// L.A.Airport
		node[0].addChild(node[2], 3);
		node[0].addChild(node[1], 1);

		// UCLA
		node[1].addChild(node[5], 6);
		node[1].addChild(node[2], 1);
		// Hollywood
		node[2].addChild(node[3], 3);
		node[2].addChild(node[6], 6);
		node[2].addChild(node[5], 6);

		// Pasadena
		node[3].addChild(node[7], 1);
		node[3].addChild(node[9], 7);
		// San Diego
		node[4].addChild(node[1], 1);
		// Downtown
		node[5].addChild(node[4], 7);
		node[5].addChild(node[3], 2);
		// Anaheim
		node[6].addChild(node[3], 2);
		node[6].addChild(node[8], 5);
		node[6].addChild(node[7], 4);
		// Disneyland
		node[7].addChild(node[9], 5);
		// Grand Canyou
		node[8].addChild(node[9], 1);
		node[8].addChild(node[7], 2);
	}

	/*
	 * 幅優先探索
	 */
	public void breadthFirst() {
		// OPEN
		Vector open = new Vector();

		// START Nodeを追加
		open.addElement(node[0]);

		// CLOSE
		Vector closed = new Vector();

		boolean success = false;
		int step = 0;

		for (;;) {
			if (open.size() == 0) {
				success = false;
				break;
			} else {
				// OPENの先頭を取り出しNodeとする
				Node node = (Node) open.elementAt(0);
				open.removeElementAt(0);
				// Nodeはゴールかの判断
				if (node == goal) {
					success = true;
					closed.addElement(node);
					break;
				} else {
					// nodeを展開して子ノードをすべて求める
					Vector children = node.getChildren();
					// node をclosedに入れる
					closed.addElement(node);

					// 子ノードmがopenにもclosedにも含めれていなければ
					for (int j = 0; j < children.size(); j++) {
						Node m = (Node) children.elementAt(j);
						if (!open.contains(m) && !closed.contains(m)) {
							// mからnodeへのポインタを付ける
							m.setPointer(node);
							if (m == goal) {
								open.insertElementAt(m, 0);
							} else {
								// 展開した子ノードをOPENの後ろへ追加
								open.addElement(m);
							}
						}
					}
				}
			}
		}

		// TODO
		if (success) {
			System.out.println("*** Solution ***");
			printSolution(closed);
		}
	}

	/*
	 * 深さ優先探索
	 */
	public void depthFirst() {
		// OPEN
		Vector open = new Vector();

		// START Nodeを追加
		open.addElement(node[0]);

		// CLOSE
		Vector closed = new Vector();

		boolean success = false;
		int step = 0;

		for (;;) {
			if (open.size() == 0) {
				success = false;
				break;
			} else {
				// OPENの先頭を取り出しNodeとする
				Node node = (Node) open.elementAt(0);
				open.removeElementAt(0);
				// Nodeはゴールかの判断
				if (node == goal) {
					success = true;
					closed.addElement(node);
					break;
				} else {
					// nodeを展開して子ノードをすべて求める
					Vector children = node.getChildren();
					// node をclosedに入れる
					closed.addElement(node);

					// 子ノードmがopenにもclosedにも含めれていなければ
					for (int j = 0; j < children.size(); j++) {
						Node m = (Node) children.elementAt(j);
						if (!open.contains(m) && !closed.contains(m)) {
							// mからnodeへのポインタを付ける
							m.setPointer(node);
							// 展開した子ノードをOPENの先頭へ追加
							open.insertElementAt(m, 0);
						}
					}
				}
			}
		}

		if (success) {
			System.out.println("*** Solution ***");
			printSolution(closed);
		}
	}

	/*
	 * 山登り法 h(n) MIN
	 */
	public void hillClimbing() {
		// OPEN
		Vector open = new Vector();

		// START Nodeを追加
		open.addElement(node[0]);

		// CLOSE
		Vector closed = new Vector();

		boolean success = false;
		int step = 0;

		for (;;) {
			if (open.size() == 0) {
				success = false;
				break;
			} else {
				// OPENの先頭を取り出しNodeとする
				Node node = (Node) open.elementAt(0);
				open.removeElementAt(0);

				// nodeを展開して子ノードをすべて求める
				Vector children = node.getChildren();
				// node をclosedに入れる
				closed.addElement(node);

				// 子ノードmがopenにもclosedにも含めれていなければ
				Node min = (Node) children.elementAt(0);
				for (int j = 0; j < children.size(); j++) {
					Node m = (Node) children.elementAt(j);
					// Nodeはゴールかの判断
					if (open.contains(goal)) {
						success = true;
						closed.addElement(node);
						closed.addElement(goal);
						break;
					} else if (min.hValue > m.hValue) {
						min = m;
					}
				}
				open.addElement(min);

				printSolution(closed);
			}
		}

		if (success) {
			System.out.println("*** Solution ***");
			printSolution(closed);
		}
	}

	/*
	 * 分岐限定法 G(n)
	 */
	public void branchFirst() {
		// OPEN
		Vector open = new Vector();

		// START Nodeを追加
		open.addElement(node[0]);

		// CLOSE
		Vector closed = new Vector();

		boolean success = false;
		int step = 0;

		for (;;) {
			if (open.size() == 0) {
				success = false;
				break;
			} else {
				// OPENの先頭を取り出しNodeとする
				Node node = (Node) open.elementAt(0);
				open.removeElementAt(0);
				// TODO FIX
				// Nodeはゴールかの判断(OPENでゴールが存在している)
				if (node == goal) {
					success = true;
					closed.addElement(node);
					break;
				} else if (open.contains(goal)) {
					success = true;
					closed.addElement(node);
					closed.addElement(goal);
					break;
				} else {
					// nodeを展開して子ノードをすべて求める
					Vector children = node.getChildren();
					// node をclosedに入れる
					closed.addElement(node);

					// 子ノードmがopenにもclosedにも含めれていなければ
					for (int j = 0; j < children.size(); j++) {
						Node m = (Node) children.elementAt(j);
						if (!open.contains(m) && !closed.contains(m)) {
							// mからnodeへのポインタを付ける
							m.setPointer(node);

							// nodeまでの評価値とnode⇒mのコストを足したものをmの評価値とする
							int gmn = node.getgValue() + node.getCost(m);
							m.setgValue(gmn);
							open.addElement(m);
						}
						// 子ノードmがopenに含まれているならば
						if (open.contains(m)) {
							int gmn = node.getgValue() + node.getCost(m);
							if (gmn < m.getgValue()) {
								m.setgValue(gmn);
								m.setPointer(node);
							}
						}
					}
				}
			}
			open = sortUpperByGValue(open);
		}

		if (success) {
			System.out.println("*** Solution ***");
			printSolution(closed);
		}
	}

	/*
	 * 最良優先探索 h(n)
	 */
	public void bestFirst() {
		// OPEN
		Vector open = new Vector();

		// START Nodeを追加
		open.addElement(node[0]);

		// CLOSE
		Vector closed = new Vector();

		boolean success = false;
		int step = 0;

		for (;;) {
			if (open.size() == 0) {
				success = false;
				break;
			} else {
				// OPENの先頭を取り出しNodeとする
				Node node = (Node) open.elementAt(0);
				open.removeElementAt(0);
				// Nodeはゴールかの判断
				if (node == goal) {
					success = true;
					closed.addElement(node);
					break;
				} else {
					// nodeを展開して子ノードをすべて求める
					Vector children = node.getChildren();
					// node をclosedに入れる
					closed.addElement(node);

					// 子ノードmがopenにもclosedにも含めれていなければ
					for (int j = 0; j < children.size(); j++) {
						Node m = (Node) children.elementAt(j);
						if (!open.contains(m) && !closed.contains(m)) {
							// mからnodeへのポインタを付ける
							m.setPointer(node);
							// 展開した子ノードをOPENの先頭へ追加
							open.insertElementAt(m, 0);
						}
					}
				}
			}

			open = sortUpperByHValue(open);
		}

		if (success) {
			System.out.println("*** Solution ***");
			printSolution(closed);
		}
	}

	/*
	 * A*アルゴリズム f(n)
	 */
	public void aStar() {
		// OPEN
		Vector open = new Vector();

		// START Nodeを追加
		open.addElement(node[0]);

		// CLOSE
		Vector closed = new Vector();

		boolean success = false;
		int step = 0;

		for (;;) {
			if (open.size() == 0) {
				success = false;
				break;
			} else {
				// OPENの先頭を取り出しNodeとする
				Node node = (Node) open.elementAt(0);
				open.removeElementAt(0);
				// TODO FIX
				// Nodeはゴールかの判断(OPENでゴールが存在している)
				if (node == goal) {
					success = true;
					closed.addElement(node);
					break;
				} else if (open.contains(goal)) {
					success = true;
					closed.addElement(node);
					closed.addElement(goal);
					break;
				} else {
					// nodeを展開して子ノードをすべて求める
					Vector children = node.getChildren();
					// node をclosedに入れる
					closed.addElement(node);

					// 子ノードmがopenにもclosedにも含めれていなければ
					for (int j = 0; j < children.size(); j++) {
						Node m = (Node) children.elementAt(j);
						int gmn = node.getgValue() + node.getCost(m);
						int fmn = gmn + node.gethValue();
						if (!open.contains(m) && !closed.contains(m)) {
							// mからnodeへのポインタを付ける
							m.setgValue(gmn);
							m.setfValue(fmn);
							m.setPointer(node);
							open.addElement(m);
						} else if (open.contains(m)) {
							if (gmn < m.getgValue()) {
								m.setgValue(gmn);
								m.setfValue(fmn);
								m.setPointer(node);
							}
						} else if (closed.contains(m)) {
							if (gmn < m.getgValue()) {
								m.setgValue(gmn);
								m.setfValue(fmn);
								m.setPointer(node);
								closed.removeElement(m);
								open.addElement(m);
							}
						}
					}
				}
			}
			open = sortUpperByFValue(open);
		}

		if (success) {
			System.out.println("*** Solution ***");
			printSolution(closed);
		}
	}

	/*
	 * 昇順(G)
	 */
	private Vector sortUpperByGValue(Vector open) {
		Vector ascOpen = new Vector();
		Node prevNode = null;
		for (int i = 0; i < open.size(); i++) {
			Node nowNode = (Node) open.elementAt(i);
			if (prevNode == null || nowNode.gValue < prevNode.gValue) {
				ascOpen.insertElementAt(nowNode, 0);
			} else {
				ascOpen.addElement(nowNode);
			}
			prevNode = (Node) open.elementAt(i);

		}
		return ascOpen;
	}

	/*
	 * 昇順(H)
	 */
	private Vector sortUpperByHValue(Vector open) {
		Vector ascOpen = new Vector();
		Node prevNode = null;
		for (int i = 0; i < open.size(); i++) {
			Node nowNode = (Node) open.elementAt(i);
			if (prevNode == null || nowNode.hValue < prevNode.hValue) {
				ascOpen.insertElementAt(nowNode, 0);
			} else {
				ascOpen.addElement(nowNode);
			}
			prevNode = (Node) open.elementAt(i);

		}
		return ascOpen;
	}

	/*
	 * 昇順(F)
	 */
	private Vector sortUpperByFValue(Vector open) {
		Vector ascOpen = new Vector();
		Node prevNode = null;
		for (int i = 0; i < open.size(); i++) {
			Node nowNode = (Node) open.elementAt(i);
			if (prevNode == null || nowNode.fValue < prevNode.fValue) {
				ascOpen.insertElementAt(nowNode, 0);
			} else {
				ascOpen.addElement(nowNode);
			}
			prevNode = (Node) open.elementAt(i);

		}
		return ascOpen;
	}

	/*
	 * 結果表示
	 */
	private void printSolution(Vector vectorInfo) {
		String ret = "";
		for (int i = 0; i < vectorInfo.size(); i++) {
			Node node = (Node) vectorInfo.elementAt(i);
			ret += node.name;
			if (i != vectorInfo.size() - 1) {
				ret += " ⇒ ";
			}
		}
		System.out.println(ret);
	}

	/*
	 * ヘルプ表示
	 */
	private static void printInfo() {
		System.out.println("USASE:");
		System.out.println("java Search [Number]");
		System.out.println("[Number]=1:幅優先探索");
		System.out.println("[Number]=2:深さ優先探索");
		System.out.println("[Number]=3:分岐限定法");
		System.out.println("[Number]=4:山登り法");
		System.out.println("[Number]=5:最良優先探索");
		System.out.println("[Number]=6:Aアルゴリズム");
	}
}
