package app;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import common.CSVFileRead;
import common.SearchMethod;

/*
 * 表示画面
 */
public class MyDrawPanel extends JPanel {

	Graphics wall;
	Graphics2D wall2;
	// Graphic生成
	static MyDrawPanel panel = new MyDrawPanel();
	// アローライン
	ArrowLinePanel arrowPanel;
	// ファイル読み込む
	CSVFileRead fileRead = new CSVFileRead();
	// 駅情報リスト
	BusStationInfoList busStationList;

	// 探索フラグ（1:幅優先探索 2:深さ優先探索 3:分岐限定法 4:山登り法 5:最良優先探索 6:Aアルゴリズム）
	static Integer searchFlg = 7;

	// 説明文字列
	String INFO = "1:幅優先探索,2:深さ優先探索,3:分岐限定法,4:山登り法,5:最良優先探索,6:Aアルゴリズム";

	// 表示メッセージ
	JLabel label;

	/*
	 * メイン
	 */
	public static void main(String[] args) {
		if (args.length == 1) {
			searchFlg = Integer.parseInt(args[0]);
		}

		init();
	}

	/*
	 * 初期設定
	 */
	public static void init() {
		// ウィンドウ生成
		JFrame frame = new JFrame();

		int win_X = (Toolkit.getDefaultToolkit().getScreenSize().width - 600) / 2;
		int win_Y = (Toolkit.getDefaultToolkit().getScreenSize().height - 400) / 2;

		// TODO 1920*1080
		win_X -= 200;
		win_Y -= 100;

		frame.setBounds(win_X, win_Y, 600, 400);
		frame.setTitle("状態空間探索");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		frame.getContentPane().add(panel);
	}

	/*
	 * ペイント(探索処理)
	 */
	public void paint(Graphics g) {
		wall = g;
		wall2 = (Graphics2D) wall;
		arrowPanel = new ArrowLinePanel();

		// 初期設定
		initPaint();

		// ファイルから駅情報取得
		busStationList = fileRead.getBusStationList();

		if (busStationList == null || busStationList.size() == 0) {
			// 取得駅情報が無い場合
			showMsg("入力ファイルで導入可能の駅情報がありません。CSVファイルを確認してください。");
			return;
		} else {
			// Pain生成
			drawStationMap();
		}

		// 探索
		if (searchFlg != null) {

			SearchMethod searchMethod = new SearchMethod();

			// 選択した駅を順番でリストにセットする用
			Vector selectedList = null;

			switch (searchFlg) {
			case 1:
				// 幅優先探索
				selectedList = searchMethod.breadthFirst(busStationList);
				showMsg("1:幅優先探索");
				break;
			case 2:
				// 深さ優先探索
				selectedList = searchMethod.depthFirst(busStationList);
				showMsg("2:深さ優先探索");
				break;
			case 3:
				// 分岐限定法
				selectedList = searchMethod.branchFirst(busStationList);
				showMsg("3:分岐限定法");
				break;
			case 4:
				// 山登り法
				selectedList = searchMethod.hillClimbing(busStationList);
				showMsg("4:山登り法");
				break;
			case 5:
				// 最良優先探索
				selectedList = searchMethod.bestFirst(busStationList);
				showMsg("5:最良優先探索");
				break;
			case 6:
				// A*アルゴリズム
				selectedList = searchMethod.aStar(busStationList);
				showMsg("6:A*アルゴリズム");
				break;
			case 7:
				// カスタム
				selectedList = searchMethod.custom(busStationList);
				showMsg("7:カスタム");
				break;
			default:
				showMsg(INFO);
			}

			// 選択した路線を表示
			showSelected(selectedList);

		} else {
			showMsg(INFO);

		}
	}

	/*
	 * ペイント初期設定
	 */
	public void initPaint() {
		Font font = new Font("Normal", 0, 19);
		wall.setFont(font);
	}

	/*
	 * 路線図作成
	 */
	public void drawStationMap() {

		// ルートで駅情報リストから取得した駅を出力する
		int i = 0;
		while (i < busStationList.size()) {
			BusStationItems busStationItems = busStationList.getStation(i);
			drawBusStation(busStationItems);
			i++;
		}
	}

	/*
	 * 駅生成
	 */
	public void drawBusStation(BusStationItems st) {
		String name = st.getName();
		Integer x = st.getX();
		Integer y = st.getY();

		Integer radius = 15;
		Integer cX = st.getCx();
		Integer cY = st.getCy();

		wall.drawString(name, cX, cY);
		wall.drawString(st.getHValue().toString(), cX, cY - 25);

		int i = 0;
		// ログ用文字列
		String childrenStr = "";

		// 親Node⇒子NodeのVector生成
		while (i < busStationList.findChildren(st).size()) {
			// 子Nodeを取得する
			BusStationItems childInfo = busStationList.findChildren(st).getStation(i);

			if (childInfo != null) {
				// 親Node⇒子NodeのVector線を表示
				arrowPanel.drawAL(st.getAx(), st.getAy(), childInfo.getX(), childInfo.getY() + 10, wall2);

				// ログ
				childrenStr += childInfo.getName();
				if (i != busStationList.findChildren(st).size() - 1) {
					childrenStr += ".";
				}
			}

			i++;
		}

		wall.drawOval(x, y, radius * 2, radius * 2);

		// ログ出力
		System.out.println(
				name + " : x=" + x + ",y=" + y + ",h(n)=" + st.getHValue().toString() + ",children=" + childrenStr);
	}

	/*
	 * 選択済路線表示
	 */
	public void showSelected(Vector selectedList) {
		if (selectedList != null) {

			String ret = "";
			int i = 0;
			while (i < selectedList.size()) {
				BusStationItems child = (BusStationItems) selectedList.elementAt(i);

				if (i > 0) {
					BusStationItems parent = (BusStationItems) selectedList.elementAt(i - 1);

					// 親Node⇒子NodeのVector線を表示
					wall2.setPaint(Color.red);
					wall2.setStroke(new BasicStroke(2.0f));
					arrowPanel.drawAL(parent.getAx(), parent.getAy(), child.getX(), child.getY() + 10, wall2);

				}

				// ログ
				ret += child.name;
				if (i != selectedList.size() - 1) {
					ret += " ⇒ ";
				}
				i++;
			}

			System.out.println(ret);

		}
	}

	/*
	 * メッセージ表示
	 */
	public void showMsg(String str) {
		Font font = new Font("Normal", 0, 14);
		wall.setFont(font);
		wall.setColor(Color.red);
		wall.drawString(str, 10, 350);

		font = new Font("Normal", 0, 19);
		wall.setFont(font);
	}
}
