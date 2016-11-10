import java.util.ArrayList;
import java.util.Iterator;

/**
 * AIDaemonProcs
 */
public class AIDemonProcs {

	/**
	 * 親、叔父を返すデモン
	 */
	public static class Uncle extends AIDemonProc {
		public Object eval(AIFrameSystem inFrameSystem, AIFrame inFrame,
				String inSlotName, Iterator inSlotValues, Object inOpts) {

			// 条件を満たすものを入れるリスト
			ArrayList<String> relist = new ArrayList<String>();

			//自分の親が登録されているかチェック
			if (!(inFrame.readSlotValue(inFrameSystem, "親", false) == null)){
				// 自分の親の名前をとってくる
				ArrayList<String> olist = inFrame.getmVals("親");

				// 親の数だけループ
				for (int i = 0; i < olist.size(); i++) {

					// 親の名前をとる
					String parentname = (String) olist.get(i);

					// 親の名前を元に親のフレームをとってくる
					AIFrame parentframe = inFrameSystem.getFrame(parentname);

					if (!(parentframe.readSlotValue(inFrameSystem, "親", false) == null)){
					//祖父母の名前をとってくる
					 ArrayList<String> glist = parentframe.getmVals("親");
						// 祖父母の数だけループ
						for (int j = 0; j < glist.size(); j++) {
							// 祖父母の名前をとる
							String gparentname = (String) glist.get(j);
							// 祖父母の名前を元に祖父母のフレームをとってくる
							AIFrame gparentframe = inFrameSystem.getFrame(gparentname);

							// 親の逆リンクから子供の名前(自分を親だと見て接続している名前)をとってくる
							// klistは兄弟の名前のリスト
							ArrayList<String> klist = gparentframe.getLeankersSlotNames("親");

							// 親の子供の数(兄弟の数)だけループ
							for (int k = 0; k < klist.size(); k++) {

								// 兄弟の名前から兄弟のフレームをとってくる
								AIFrame frame = inFrameSystem.getFrame(klist.get(k));
							//自分の親でない、かつ、男なら叔父
								if(!klist.get(k).equals(olist.get(i)) && frame.readSlotValue(inFrameSystem, "性別",false).equals("男")){

								// 親は基本二人なので二回目以降のループ時すでに前のループで登録されてないか調べる
								if (!relist.contains(klist.get(k)))
									// 初めての名前なら兄弟としてリストに入れる
									relist.add(klist.get(k));
								}
							}
						}
					}
				}
			}
			 if(relist.size()==0)
				 relist.add("登録されていないため分からない");
			return AIFrame.makeEnum(new ArrayList<String>(relist));
		}
	}

}
