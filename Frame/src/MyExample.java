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
		fs.createInstanceFrame("人間", "佐藤");

		fs.writeSlotValue("田中", "性別", "男性");
		fs.writeSlotValue("田中", "親", "森");
		fs.writeSlotValue("田中", "叔父", "佐藤");
		fs.writeSlotValue("田中", "仕事", "医者");

		fs.writeSlotValue("森", "性別", "女性");
		fs.writeSlotValue("森", "兄", "佐藤");
		fs.writeSlotValue("森", "息子", "田中");
		// 森さんは病気があるので、動かせない
		fs.writeSlotValue("森", "特性1", "動かせない");

		fs.writeSlotValue("佐藤", "性別", "男性");
		fs.writeSlotValue("佐藤", "弟", "森");
		fs.writeSlotValue("佐藤", "甥", "田中");
		fs.writeSlotValue("佐藤", "仕事", "教師");

		// 田中
		System.out.println("\n田中：");
		System.out.println(fs.readSlotValue("田中", "性別", false));
		System.out.println(fs.readSlotValue("田中", "特性1", false));
		System.out.println(fs.readSlotValue("田中", "特性2", false));
		System.out.println(fs.readSlotValue("田中", "特性3", false));

		System.out.println("親：" + fs.readSlotValue("田中", "親", false));
		System.out.println("叔父：" + fs.readSlotValue("田中", "叔父", false));
		System.out.println("仕事：" + fs.readSlotValue("田中", "仕事", false));

		// 森
		System.out.println("\n森：");
		System.out.println(fs.readSlotValue("森", "性別", false));
		System.out.println(fs.readSlotValue("森", "特性1", false));
		System.out.println(fs.readSlotValue("森", "特性2", false));
		System.out.println(fs.readSlotValue("森", "特性3", false));
		System.out.println("兄：" + fs.readSlotValue("森", "兄", false));

		// 佐藤
		System.out.println("\n佐藤：");

		System.out.println(fs.readSlotValue("佐藤", "特性1", false));
		System.out.println(fs.readSlotValue("佐藤", "特性2", false));
		System.out.println(fs.readSlotValue("佐藤", "特性3", false));
		System.out.println(fs.readSlotValue("佐藤", "仕事", false));
		System.out.println("弟：" + fs.readSlotValue("佐藤", "弟", false));
		System.out.println("甥：" + fs.readSlotValue("佐藤", "甥", false));
	}

}