
/*
 * カウンタークラス
 */
import java.util.Vector;

class Counter {
	Vector coffees;
	int coffeeMax = 4;
	int coffeeTotal = 0;
	int coffeeTotalMax = 30;

	Counter() {
		coffees = new Vector();
	}

	/*
	 * コーヒー取得（お客さん）
	 */
	public synchronized boolean getCoffee(String name) throws InterruptedException {
		// 一日コーヒーが売り切れ
		if (coffeeTotal > coffeeTotalMax-1 && coffees.size() == 0) {
			System.out.println("Sold Out!");
			return false;
		}

		// 誰か一人を起きこす瞬間に、他の誰かがコーヒーを
		// 持って行ってしまう可能性があるので
		while (coffees.size() == 0) {
			System.out.println(name + ":Can NOT drink");
			wait();
		}

		coffees.removeElementAt(0);

		System.out.println(name + ":Can drink!");
		System.out.println("Coffees:" + coffees.toString());

		if (coffees.size() == coffeeMax) {
			notifyAll();
		}
		return true;
	}

	/*
	 * コーヒー作る（マスター）
	 */
	public synchronized boolean putCoffee(String name) throws InterruptedException {
		// 一日コーヒーが売り切れ
		if (coffeeTotal > coffeeTotalMax-1) {
			System.out.println("No Stock!");
			return false;
		}

		coffees.addElement(new String("Coffee"));
		if (coffees.size() > coffeeMax) {
			System.out.println("AKAJI!");
			wait();
		}
		// 作ったコーヒー数量
		coffeeTotal++;
		// 在庫数を表示する
		System.out.println(name + ":Made a Coffee!【stock:" + Integer.toString(coffeeTotalMax - coffeeTotal) + "】)");
		System.out.println("Coffees:" + coffees.toString());

		if (coffees.size() == 1) {
			notify();
		}
		return true;
	}

}
