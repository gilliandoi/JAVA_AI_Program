/*
 * お客さん_コーヒー飲むクラス
 */
class CoffeeDrinker extends Thread {
	Counter counter;
	String name;
	boolean exitFlag = false;

	CoffeeDrinker(Counter theCounter, String theName) {
		this.counter = theCounter;
		this.name = theName;
	}

	/*
	 * runメソッドを宣言
	 *
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		while (!exitFlag) {
			try {
				// コーヒー
				if (!counter.getCoffee(this.name)) {
					// 売り切れ
					exitFlag = true;
				} else {
					// コーヒーをぼちぼち飲む
					Thread.sleep((int) (10000 * Math.random()));
				}

			} catch (InterruptedException e) {
				// 処理なし
			}
		}
	}
}
