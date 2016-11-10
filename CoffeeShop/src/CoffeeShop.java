/*
 * 喫茶店クラス
 */
public class CoffeeShop {
	public static void main(String args[]) {
		// 各クラスをインスタンスを作成する。
		Counter counter = new Counter();
		// コーヒー作る
		ShopMaster master = new ShopMaster(counter,"master1");
		// コーヒー作る
		CoffeeDrinker user1 = new CoffeeDrinker(counter, "user1");
		CoffeeDrinker user2 = new CoffeeDrinker(counter, "user2");
		CoffeeDrinker user3 = new CoffeeDrinker(counter, "user3");

		// 複数スレッド開始
		master.start();
		user1.start();
		user2.start();
		user3.start();
	}
}
