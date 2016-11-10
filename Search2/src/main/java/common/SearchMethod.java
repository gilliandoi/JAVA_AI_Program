package common;

import java.util.Vector;

import app.BusStationInfoList;
import app.BusStationItems;

/*
 * 探索方法
 * 1:幅優先探索
 * 2:深さ優先探索
 * 3:分岐限定法
 * 4:山登り法
 * 5:最良優先探索
 * 6:Aアルゴリズム
 *
 */
public class SearchMethod {

	BusStationItems start;
	BusStationItems goal;
	boolean success = false;

	/*
	 * 初期処理
	 */
	private void init(BusStationInfoList busStationInfoList) {
		start = busStationInfoList.findStart();
		goal = busStationInfoList.findGoal();
		System.out.println("start=" + start.getName() + ",goal=" + goal.getName());
	}

	/*
	 * 幅優先探索
	 */
	public Vector breadthFirst(BusStationInfoList busStationInfoList) {
		System.out.println("\n幅優先探索");
		init(busStationInfoList);

		// 選択した駅を順番でリストにセットする用
		BusStationInfoList selectedList = new BusStationInfoList();
		// OPEN
		Vector open = new Vector();

		// START Nodeを追加
		open.addElement(busStationInfoList.getStation(0));

		// CLOSE
		Vector closed = new Vector();

		int step = 0;

		for (;;) {
			if (open.size() == 0) {
				success = false;
				break;
			} else {
				// OPENの先頭を取り出しNodeとする
				BusStationItems parent = (BusStationItems) open.elementAt(0);
				open.removeElementAt(0);
				// Nodeはゴールかの判断
				if (parent == goal) {
					success = true;
					closed.addElement(parent);
					break;
				} else {
					// nodeを展開して子ノードをすべて求める
					BusStationInfoList childrenList = busStationInfoList.findChildren(parent);
					// node をclosedに入れる
					closed.addElement(parent);

					int j = 0;

					while (j < childrenList.size()) {
						BusStationItems nStation = childrenList.getStation(j);

						// 子駅nがopenにもclosedにも含めれていなければ
						if (!open.contains(nStation) && !closed.contains(nStation)) {
							// 親駅からn駅へのポインタを付ける
							nStation.setPointer(parent);
							if (nStation == goal) {
								open.insertElementAt(nStation, 0);
							} else {
								// 展開した子ノードをOPENの後ろへ追加
								open.addElement(nStation);
							}
						}
						j++;
					}
				}
			}
		}

		if (success) {
			return closed;
		} else {
			return null;
		}
	}

	/*
	 * 深さ優先探索
	 */
	public Vector depthFirst(BusStationInfoList busStationInfoList) {
		System.out.println("\n深さ優先探索");
		init(busStationInfoList);

		// 選択した駅を順番でリストにセットする用
		BusStationInfoList selectedList = new BusStationInfoList();
		// OPEN
		Vector open = new Vector();

		// START Nodeを追加
		open.addElement(busStationInfoList.getStation(0));

		// CLOSE
		Vector closed = new Vector();

		int step = 0;

		for (;;) {
			if (open.size() == 0) {
				success = false;
				break;
			} else {
				// OPENの先頭を取り出しNodeとする
				BusStationItems parent = (BusStationItems) open.elementAt(0);
				open.removeElementAt(0);
				// Nodeはゴールかの判断
				if (parent == goal) {
					success = true;
					closed.addElement(parent);
					break;
				} else {
					// nodeを展開して子ノードをすべて求める
					BusStationInfoList childrenList = busStationInfoList.findChildren(parent);
					// node をclosedに入れる
					closed.addElement(parent);

					int j = 0;
					while (j < childrenList.size()) {
						BusStationItems nStation = childrenList.getStation(j);

						// 子駅nがopenにもclosedにも含めれていなければ
						if (!open.contains(nStation) && !closed.contains(nStation)) {
							// 親駅からn駅へのポインタを付ける
							nStation.setPointer(parent);
							// 展開した子ノードをOPENの頭で追加
							open.insertElementAt(nStation, 0);
						}
						j++;
					}
				}
			}
		}

		if (success) {
			return closed;
		} else {
			return null;
		}
	}

	/*
	 * 分岐限定法 G(n) MIN
	 */
	public Vector branchFirst(BusStationInfoList busStationInfoList) {
		System.out.println("\n分岐限定法 G(n) MIN");

		init(busStationInfoList);

		// 選択した駅を順番でリストにセットする用
		BusStationInfoList selectedList = new BusStationInfoList();
		// OPEN
		Vector open = new Vector();

		// START Nodeを追加
		open.addElement(busStationInfoList.getStation(0));

		// CLOSE
		Vector closed = new Vector();

		int step = 0;

		for (;;) {
			if (open.size() == 0) {
				success = false;
				break;
			} else {
				// OPENの先頭を取り出しNodeとする
				BusStationItems parent = (BusStationItems) open.elementAt(0);
				open.removeElementAt(0);
				// Nodeはゴールかの判断
				if (parent == goal) {
					success = true;
					closed.addElement(parent);
					break;
				} else {
					// nodeを展開して子ノードをすべて求める
					BusStationInfoList childrenList = busStationInfoList.findChildren(parent);
					// node をclosedに入れる
					closed.addElement(parent);

					int j = 0;
					BusStationItems min = childrenList.getStation(0);

					while (j < childrenList.size()) {
						BusStationItems nStation = childrenList.getStation(j);
						// 親駅と子駅の座標を利用して、距離を計算して、G(n)にセット
						Double gValue = Math.sqrt(Math.pow(nStation.getX() - parent.getX(), 2)
								+ Math.pow(nStation.getY() - parent.getY(), 2));
						nStation.setGValue(gValue);

						if (min.getGValue() > nStation.getGValue()) {
							min = nStation;
						}
						j++;
					}

					open.addElement(min);
				}
			}

			open = sortUpperByGValue(open);
		}

		if (success) {
			return closed;
		} else {
			return null;
		}
	}

	/*
	 * 山登り法 h(n) MIN
	 */
	public Vector hillClimbing(BusStationInfoList busStationInfoList) {
		System.out.println("\n山登り法 h(n) MIN");

		init(busStationInfoList);

		// 選択した駅を順番でリストにセットする用
		BusStationInfoList selectedList = new BusStationInfoList();
		// OPEN
		Vector open = new Vector();

		// START Nodeを追加
		open.addElement(busStationInfoList.getStation(0));

		// CLOSE
		Vector closed = new Vector();

		int step = 0;

		for (;;) {
			if (open.size() == 0) {
				success = false;
				break;
			} else {
				// OPENの先頭を取り出しNodeとする
				BusStationItems parent = (BusStationItems) open.elementAt(0);
				open.removeElementAt(0);
				// Nodeはゴールかの判断
				if (parent == goal) {
					success = true;
					closed.addElement(parent);
					break;
				} else {
					// nodeを展開して子ノードをすべて求める
					BusStationInfoList childrenList = busStationInfoList.findChildren(parent);
					// node をclosedに入れる
					closed.addElement(parent);

					int j = 0;
					BusStationItems min = childrenList.getStation(0);

					while (j < childrenList.size()) {
						BusStationItems nStation = childrenList.getStation(j);

						if (min.getHValue() > nStation.getHValue()) {
							min = nStation;
						}
						j++;
					}

					open.addElement(min);
				}
			}

		}

		if (success) {
			return closed;
		} else {
			return null;
		}
	}

	/*
	 * 最良優先探索 h(n)
	 */
	public Vector bestFirst(BusStationInfoList busStationInfoList) {
		System.out.println("\n最良優先探索 h(n)");
		init(busStationInfoList);

		// 選択した駅を順番でリストにセットする用
		BusStationInfoList selectedList = new BusStationInfoList();
		// OPEN
		Vector open = new Vector();

		// START Nodeを追加
		open.addElement(busStationInfoList.getStation(0));

		// CLOSE
		Vector closed = new Vector();

		int step = 0;

		for (;;) {
			if (open.size() == 0) {
				success = false;
				break;
			} else {
				// OPENの先頭を取り出しNodeとする
				BusStationItems parent = (BusStationItems) open.elementAt(0);
				open.removeElementAt(0);
				// Nodeはゴールかの判断
				if (parent == goal) {
					success = true;
					closed.addElement(parent);
					break;
				} else {
					// nodeを展開して子ノードをすべて求める
					BusStationInfoList childrenList = busStationInfoList.findChildren(parent);
					// node をclosedに入れる
					closed.addElement(parent);

					int j = 0;
					while (j < childrenList.size()) {
						BusStationItems nStation = childrenList.getStation(j);

						// 子駅nがopenにもclosedにも含めれていなければ
						if (!open.contains(nStation) && !closed.contains(nStation)) {
							// 親駅からn駅へのポインタを付ける
							nStation.setPointer(parent);
							// 展開した子ノードをOPENの頭で追加
							open.insertElementAt(nStation, 0);
						}
						j++;
					}
				}
			}

			open = sortUpperByHValue(open);
		}

		if (success) {
			return closed;
		} else {
			return null;
		}
	}

	/*
	 * A*アルゴリズム f(n) MIN
	 */
	public Vector aStar(BusStationInfoList busStationInfoList) {
		System.out.println("\nA*アルゴリズム f(n) MIN");

		init(busStationInfoList);

		// 選択した駅を順番でリストにセットする用
		BusStationInfoList selectedList = new BusStationInfoList();
		// OPEN
		Vector open = new Vector();

		// START Nodeを追加
		open.addElement(busStationInfoList.getStation(0));

		// CLOSE
		Vector closed = new Vector();

		int step = 0;

		for (;;) {
			if (open.size() == 0) {
				success = false;
				break;
			} else {
				// OPENの先頭を取り出しNodeとする
				BusStationItems parent = (BusStationItems) open.elementAt(0);
				open.removeElementAt(0);
				// Nodeはゴールかの判断
				if (parent == goal) {
					success = true;
					closed.addElement(parent);
					break;
				} else {
					// nodeを展開して子ノードをすべて求める
					BusStationInfoList childrenList = busStationInfoList.findChildren(parent);
					// node をclosedに入れる
					closed.addElement(parent);

					int j = 0;
					BusStationItems min = childrenList.getStation(0);

					while (j < childrenList.size()) {
						BusStationItems nStation = childrenList.getStation(j);
						// 親駅と子駅の座標を利用して、距離を計算して、G(n)にセット
						Double gValue = Math.sqrt(Math.pow(nStation.getX() - parent.getX(), 2)
								+ Math.pow(nStation.getY() - parent.getY(), 2));
						nStation.setGValue(gValue);

						Double fValue = gValue + nStation.getHValue();
						nStation.setFValue(fValue);

						if (min.getFValue() > nStation.getFValue()) {
							min = nStation;
						}
						j++;
					}

					open.addElement(min);
				}
			}

			open = sortUpperByFValue(open);
		}

		if (success) {
			return closed;
		} else {
			return null;
		}
	}

	/*
	 * カスタム g(n) MIN、h(n) MAX
	 */
	public Vector custom(BusStationInfoList busStationInfoList) {
		System.out.println("\nカスタム g(n) MIN、h(n) MAX");

		init(busStationInfoList);

		// 選択した駅を順番でリストにセットする用
		BusStationInfoList selectedList = new BusStationInfoList();
		// OPEN
		Vector open = new Vector();

		// START Nodeを追加
		open.addElement(busStationInfoList.getStation(0));

		// CLOSE
		Vector closed = new Vector();

		int step = 0;

		for (;;) {
			if (open.size() == 0) {
				success = false;
				break;
			} else {
				// OPENの先頭を取り出しNodeとする
				BusStationItems parent = (BusStationItems) open.elementAt(0);
				open.removeElementAt(0);
				// Nodeはゴールかの判断
				if (parent == goal) {
					success = true;
					closed.addElement(parent);
					break;
				} else {
					// nodeを展開して子ノードをすべて求める
					BusStationInfoList childrenList = busStationInfoList.findChildren(parent);
					// node をclosedに入れる
					closed.addElement(parent);

					int j = 0;
					BusStationItems max = childrenList.getStation(0);

					while (j < childrenList.size()) {
						BusStationItems nStation = childrenList.getStation(j);

						// 親駅と子駅の座標を利用して、距離を計算して、G(n)にセット
						Double gValue = Math.sqrt(Math.pow(nStation.getX() - parent.getX(), 2)
								+ Math.pow(nStation.getY() - parent.getY(), 2));
						nStation.setGValue(gValue);

						// H値 MAX,G MIN
						if(max.getHValue()- max.getGValue() < nStation.getHValue()-gValue){
							max = nStation;
						}
						j++;
					}

					open.addElement(max);
				}
			}

		}

		if (success) {
			return closed;
		} else {
			return null;
		}
	}

	/*
	 * コスト昇順ソート(G)
	 */
	private Vector sortUpperByGValue(Vector open) {
		Vector ascOpen = new Vector();
		BusStationItems prevNode = null;
		for (int i = 0; i < open.size(); i++) {
			BusStationItems nowNode = (BusStationItems) open.elementAt(i);
			if (prevNode == null || nowNode.getGValue() < prevNode.getGValue()) {
				ascOpen.insertElementAt(nowNode, 0);
			} else {
				ascOpen.addElement(nowNode);
			}
			prevNode = (BusStationItems) open.elementAt(i);

		}
		return ascOpen;
	}

	/*
	 * ヒューリスティック昇順(H)
	 */
	private Vector sortUpperByHValue(Vector open) {
		Vector ascOpen = new Vector();
		BusStationItems prevNode = null;
		for (int i = 0; i < open.size(); i++) {
			BusStationItems nowNode = (BusStationItems) open.elementAt(i);
			if (prevNode == null || nowNode.getHValue() < prevNode.getHValue()) {
				ascOpen.insertElementAt(nowNode, 0);
			} else {
				ascOpen.addElement(nowNode);
			}
			prevNode = (BusStationItems) open.elementAt(i);

		}
		return ascOpen;
	}

	/*
	 * ヒューリスティック降順(H)
	 */
	private Vector sortDownByHValue(Vector open) {
		Vector descOpen = new Vector();
		BusStationItems prevNode = null;
		for (int i = 0; i < open.size(); i++) {
			BusStationItems nowNode = (BusStationItems) open.elementAt(i);
			if (prevNode == null || nowNode.getHValue() > prevNode.getHValue()) {
				descOpen.insertElementAt(nowNode, 0);
			} else {
				descOpen.addElement(nowNode);
			}
			prevNode = (BusStationItems) open.elementAt(i);

		}
		return descOpen;
	}

	/*
	 * F関数昇順(F)
	 */
	private Vector sortUpperByFValue(Vector open) {
		Vector ascOpen = new Vector();
		BusStationItems prevNode = null;
		for (int i = 0; i < open.size(); i++) {
			BusStationItems nowNode = (BusStationItems) open.elementAt(i);
			if (prevNode == null || nowNode.getFValue() < prevNode.getFValue()) {
				ascOpen.insertElementAt(nowNode, 0);
			} else {
				ascOpen.addElement(nowNode);
			}
			prevNode = (BusStationItems) open.elementAt(i);

		}
		return ascOpen;
	}

}
