/*
 Example.java

 */

public class MyExample extends AIFrameSystem {

	public static void main(String args[]) {
		MyExample fs = new MyExample();

	}

	public MyExample() {
		System.out.println("Frame");

		// フレームシステムの初期化
		AIFrameSystem fs = new AIFrameSystem();

		// クラスフレーム 動物 の生成
		fs.createClassFrame("人間");
		// スロットを設定
		fs.writeSlotValue("人間", "特性1", "動く");
		fs.writeSlotValue("人間", "特性2", "食べる");
		fs.writeSlotValue("人間", "特性3", "呼吸する");
		// when-requested demon として スロットに割り当てる
		fs.setWhenRequestedProc("人間", "叔父", new AIDemonProcs.Uncle());

		// インスタンスフレーム のﾌ生成
		fs.createInstanceFrame("人間", "田中");
		fs.createInstanceFrame("人間", "森");
		fs.createInstanceFrame("人間", "清水");
		fs.createInstanceFrame("人間", "佐藤");

		fs.writeSlotValue("田中", "親", "森");
		fs.writeSlotValue("田中", "叔父", "佐藤");
		fs.writeSlotValue("森", "親", "清水");
		fs.writeSlotValue("清水", "親", "佐藤");

		// height と weight はデフォルト値
		System.out.println("\n田中の特性：");
		System.out.println(fs.readSlotValue("田中", "特性1", false));
		System.out.println(fs.readSlotValue("田中", "特性2", false));
		System.out.println(fs.readSlotValue("田中", "特性3", false));

		System.out.println("\n田中の親：");
		System.out.println(fs.readSlotValue("田中", "親", false));
		System.out.println("\n田中の叔父：");
		System.out.println(fs.readSlotValue("田中", "叔父", false));
		System.out.println("\n清水の親：");
		System.out.println(fs.readSlotValue("清水", "親", false));
		System.out.println("\n森の親：");
		System.out.println(fs.readSlotValue("森", "親", false));
	}

}