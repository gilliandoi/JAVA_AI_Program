/*
 Example.java

 */

public class Example {

	public static void main(String args[]) {
		System.out.println("Frame");

		// フレームシステムの初期化
		AIFrameSystem fs = new AIFrameSystem();

		// クラスフレーム human の生成
		fs.createClassFrame("human");
		// height スロットを設定
		fs.writeSlotValue("human", "height", new Integer(160));
		// height から weight を計算するための式 weight = 0.9*(height-100) を
		// when-requested demon として weight スロットに割り当てる
		fs.setWhenRequestedProc("human", "weight", new AIDemonProcReadTest());

		// インスタンスフレーム tora のﾌ生成
		fs.createInstanceFrame("human", "tora");

		// height と weight はデフォルト値
		System.out.println(fs.readSlotValue("tora", "height", false));
		System.out.println(fs.readSlotValue("tora", "weight", false));

		// weight はデフォルト値
		fs.writeSlotValue("tora", "height", new Integer(165));
		System.out.println(fs.readSlotValue("tora", "height", false));
		System.out.println(fs.readSlotValue("tora", "weight", false));

		// 再びデフォルト値を表示
		fs.writeSlotValue("tora", "weight", new Integer(50));
		System.out.println(fs.readSlotValue("tora", "height", true));
		System.out.println(fs.readSlotValue("tora", "weight", true));
	}

}