import java.util.ArrayList;
import java.util.Iterator;

/**
 * AIDaemonProcs
 */
public class AIDemonProcs {

	/**
	 * 祖母を返すデモン
	 */
	public static class Grandma extends AIDemonProc {
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
							    //女なら祖母
								if(gparentframe.readSlotValue(inFrameSystem, "性別",false).equals("女")){
									relist.add(gparentname);
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

	/**
	 * 祖父を返すデモン
	 */
	public static class Grandpa extends AIDemonProc {
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
							    //男なら祖父
								if(gparentframe.readSlotValue(inFrameSystem, "性別",false).equals("男")){
									relist.add(gparentname);
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

	/**
	 * 兄を返すデモン
	 */
	public static class OldBrother extends AIDemonProc {
		public Object eval(AIFrameSystem inFrameSystem, AIFrame inFrame,
				String inSlotName, Iterator inSlotValues, Object inOpts) {

			// 条件を満たすものを入れるリスト
			ArrayList<String> relist = new ArrayList<String>();

			// 自分の年齢が登録されているかチェック
			if (inFrame.readSlotValue(inFrameSystem, "誕生日", false) == null) {
				// 誕生日が登録されていないことを返す
				relist.add("誕生日が登録されていないためわからない");
			} else {

				// 自分の年齢(data1)をint型でとってくる
				int data1 = Integer.parseInt((String) inFrame.readSlotValue(
						inFrameSystem, "誕生日", false));

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

						// 親の逆リンクから子供の名前(自分を親だと見て接続している名前)をとってくる
						// klistは兄弟の名前のリスト
						ArrayList<String> klist = parentframe
								.getLeankersSlotNames("親");


						// 親の子供の数(兄弟の数)だけループ
						for (int j = 0; j < klist.size(); j++) {

							// 兄弟の名前から兄弟のフレームをとってくる
							AIFrame frame = inFrameSystem.getFrame(klist.get(j));

							// 兄弟フレームの誕生日スロットの値が自分より若く、かつ、性別スロットの値が男なら兄
							if (Integer.parseInt((String) frame.readSlotValue(
									inFrameSystem, "誕生日", false)) < data1
									&& frame.readSlotValue(inFrameSystem, "性別",
											false).equals("男")) {

								// 親は基本二人なので二回目以降のループ時すでに前のループで登録されてないか調べる
								if (!relist.contains(klist.get(j))) {
									// 初めての名前なら兄としてリストに入れる
									relist.add(klist.get(j));
								}
							}
						}
					}
				}
			}
			 if(relist.size()==0)
				 relist.add("登録されていないため分からない");
			// relistを返す
			return AIFrame.makeEnum(new ArrayList<String>(relist));
		}
	}

	/**
	 * 姉を返すデモン
	 */
	public static class OldSister extends AIDemonProc {
		public Object eval(AIFrameSystem inFrameSystem, AIFrame inFrame,
				String inSlotName, Iterator inSlotValues, Object inOpts) {

			// 条件を満たすものを入れるリスト
			ArrayList<String> relist = new ArrayList<String>();

			// 自分の年齢が登録されているかチェック
			if (inFrame.readSlotValue(inFrameSystem, "誕生日", false) == null) {
				// 誕生日が登録されていないことを返す
				relist.add("誕生日が登録されていないためわからない");
			} else {

				// 自分の年齢(data1)をint型でとってくる
				int data1 = Integer.parseInt((String) inFrame.readSlotValue(
						inFrameSystem, "誕生日", false));

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

						// 親の逆リンクから子供の名前(自分を親だと見て接続している名前)をとってくる
						// klistは兄弟の名前のリスト
						ArrayList<String> klist = parentframe
								.getLeankersSlotNames("親");


						// 親の子供の数(兄弟の数)だけループ
						for (int j = 0; j < klist.size(); j++) {

							// 兄弟の名前から兄弟のフレームをとってくる
							AIFrame frame = inFrameSystem.getFrame(klist.get(j));

							// 兄弟フレームの誕生日スロットの値が自分より若く、かつ、性別スロットの値が男なら兄
							if (Integer.parseInt((String) frame.readSlotValue(
									inFrameSystem, "誕生日", false))  < data1
									&& frame.readSlotValue(inFrameSystem, "性別",
											false).equals("女")) {

								// 親は基本二人なので二回目以降のループ時すでに前のループで登録されてないか調べる
								if (!relist.contains(klist.get(j))) {
									// 初めての名前なら姉としてリストに入れる
									relist.add(klist.get(j));
								}
							}
						}
					}
				}
			}
			 if(relist.size()==0)
				 relist.add("登録されていないため分からない");
			// relistを返す
			return AIFrame.makeEnum(new ArrayList<String>(relist));
		}
	}

	/**
	 * 息子を返すデモン
	 */
	public static class Son extends AIDemonProc {
		public Object eval(AIFrameSystem inFrameSystem, AIFrame inFrame,
				String inSlotName, Iterator inSlotValues, Object inOpts) {

			// 条件を満たすものを入れるリスト
			ArrayList<String> relist = new ArrayList<String>();

			// 親の逆リンクから子供の名前(自分を親だと見て接続している名前)をとってくる
			// klistは息子の名前のリスト
				ArrayList<String> klist = inFrame
						.getLeankersSlotNames("親");

			// 子供の数だけループ
				for (int j = 0; j < klist.size(); j++) {

					//子供の名前から子供のフレームをとってくる
					AIFrame frame = inFrameSystem.getFrame(klist.get(j));

					// 性別スロットの値が男なら息子
					if (frame.readSlotValue(inFrameSystem, "性別",
									false).equals("男")) {

						// 二回目以降のループ時すでに前のループで登録されてないか調べる
						if (!relist.contains(klist.get(j))) {
							// 初めての名前なら息子としてリストに入れる
							relist.add(klist.get(j));
						}
					}
				}
				 if(relist.size()==0)
					 relist.add("登録されていないため分からない");
			// relistを返す
			return AIFrame.makeEnum(new ArrayList<String>(relist));
		}
	}

	/**
	 * 娘を返すデモン
	 */
	public static class Daughter extends AIDemonProc {
		public Object eval(AIFrameSystem inFrameSystem, AIFrame inFrame,
				String inSlotName, Iterator inSlotValues, Object inOpts) {

			// 条件を満たすものを入れるリスト
			ArrayList<String> relist = new ArrayList<String>();

			// 親の逆リンクから子供の名前(自分を親だと見て接続している名前)をとってくる
			// klistは息子の名前のリスト
				ArrayList<String> klist = inFrame
						.getLeankersSlotNames("親");

			// 子供の数だけループ
				for (int j = 0; j < klist.size(); j++) {

					//子供の名前から子供のフレームをとってくる
					AIFrame frame = inFrameSystem.getFrame(klist.get(j));

					// 性別スロットの値が男なら息子
					if (frame.readSlotValue(inFrameSystem, "性別",
									false).equals("女")) {

						// 二回目以降のループ時すでに前のループで登録されてないか調べる
						if (!relist.contains(klist.get(j))) {
							// 初めての名前なら息子としてリストに入れる
							relist.add(klist.get(j));
						}
					}
				}
				 if(relist.size()==0)
					 relist.add("登録されていないため分からない");
			// relistを返す
			return AIFrame.makeEnum(new ArrayList<String>(relist));
		}
	}

	/**
	 * おじを返すデモン
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

	/**
	 * おばを返すデモン
	 */
	public static class Aunt extends AIDemonProc {
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
							//自分の親でない、かつ、女なら叔母
								if(!klist.get(k).equals(olist.get(i)) && frame.readSlotValue(inFrameSystem, "性別",false).equals("女")){

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

	/**
	 * 弟を返すデモン
	 */
	public static class YoungBrother extends AIDemonProc {
		public Object eval(AIFrameSystem inFrameSystem, AIFrame inFrame,
				String inSlotName, Iterator inSlotValues, Object inOpts) {

			// 条件を満たすものを入れるリスト
			ArrayList<String> relist = new ArrayList<String>();

			// 自分の年齢が登録されているかチェック
			if (inFrame.readSlotValue(inFrameSystem, "誕生日", false) == null) {
				// 誕生日が登録されていないことを返す
				relist.add("誕生日が登録されていないためわからない");
			} else {

				// 自分の年齢(data1)をint型でとってくる
				int data1 = Integer.parseInt((String) inFrame.readSlotValue(
						inFrameSystem, "誕生日", false));

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

						// 親の逆リンクから子供の名前(自分を親だと見て接続している名前)をとってくる
						// klistは兄弟の名前のリスト
						ArrayList<String> klist = parentframe
								.getLeankersSlotNames("親");


						// 親の子供の数(兄弟の数)だけループ
						for (int j = 0; j < klist.size(); j++) {

							// 兄弟の名前から兄弟のフレームをとってくる
							AIFrame frame = inFrameSystem.getFrame(klist.get(j));

							// 兄弟フレームの誕生日スロットの値が自分より若く、かつ、性別スロットの値が男なら兄
							if (Integer.parseInt((String) frame.readSlotValue(
									inFrameSystem, "誕生日", false)) > data1
									&& frame.readSlotValue(inFrameSystem, "性別",
											false).equals("男")) {

								// 親は基本二人なので二回目以降のループ時すでに前のループで登録されてないか調べる
								if (!relist.contains(klist.get(j))) {
									// 初めての名前なら弟としてリストに入れる
									relist.add(klist.get(j));
								}
							}
						}
					}
				}
			}
			 if(relist.size()==0)
				 relist.add("登録されていないため分からない");
			// relistを返す
			return AIFrame.makeEnum(new ArrayList<String>(relist));
		}
	}

	/**
	 * 妹を返すデモン
	 */
	public static class YoungSister extends AIDemonProc {
		public Object eval(AIFrameSystem inFrameSystem, AIFrame inFrame,
				String inSlotName, Iterator inSlotValues, Object inOpts) {

			// 条件を満たすものを入れるリスト
			ArrayList<String> relist = new ArrayList<String>();

			// 自分の年齢が登録されているかチェック
			if (inFrame.readSlotValue(inFrameSystem, "誕生日", false) == null) {
				// 誕生日が登録されていないことを返す
				relist.add("誕生日が登録されていないためわからない");
			} else {

				// 自分の年齢(data1)をint型でとってくる
				int data1 = Integer.parseInt((String) inFrame.readSlotValue(
						inFrameSystem, "誕生日", false));

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

						// 親の逆リンクから子供の名前(自分を親だと見て接続している名前)をとってくる
						// klistは兄弟の名前のリスト
						ArrayList<String> klist = parentframe
								.getLeankersSlotNames("親");


						// 親の子供の数(兄弟の数)だけループ
						for (int j = 0; j < klist.size(); j++) {

							// 兄弟の名前から兄弟のフレームをとってくる
							AIFrame frame = inFrameSystem.getFrame(klist.get(j));

							// 兄弟フレームの誕生日スロットの値が自分より若く、かつ、性別スロットの値が男なら兄
							if (Integer.parseInt((String) frame.readSlotValue(
									inFrameSystem, "誕生日", false)) > data1
									&& frame.readSlotValue(inFrameSystem, "性別",
											false).equals("女")) {

								// 親は基本二人なので二回目以降のループ時すでに前のループで登録されてないか調べる
								if (!relist.contains(klist.get(j))) {
									// 初めての名前なら妹としてリストに入れる
									relist.add(klist.get(j));
								}
							}
						}
					}
				}
			}
			 if(relist.size()==0)
				 relist.add("登録されていないため分からない");
			// relistを返す
			return AIFrame.makeEnum(new ArrayList<String>(relist));
		}

	}

	/**
	 * 従兄弟を返すデモン
	 */
	public static class Cousin extends AIDemonProc {
		public Object eval(AIFrameSystem inFrameSystem, AIFrame inFrame,
				String inSlotName, Iterator inSlotValues, Object inOpts) {

			//条件を満たすものを入れるリスト
			ArrayList<String> relist = new ArrayList<String>();

			//自分の親が登録されているかチェック
			//if (inFrame.readSlotValue(inFrameSystem, "親", false) == null) {
				//親が登録されていないことを返す
				//relist.add("登録されていないためわからない");}
			 if(!(inFrame.readSlotValue(inFrameSystem, "親", false) == null)) {

				//自分の親の名前をとってくる
				ArrayList<String> olist = inFrame.getmVals("親");

				//親の数だけループ
				for (int i = 0; i < olist.size(); i++) {

					//親の名前をとる
					String parentname = (String) olist.get(i);

					//親の名前を元に親のフレームをとってくる
					AIFrame parentframe = inFrameSystem.getFrame(parentname);

					//とってきた親の親が登録されているかチェック
//					if (!(parentframe.readSlotValue(inFrameSystem, "親", false) == null)) {
						//祖父母が登録されていないことを返す

	//					relist.add("登録されていないためわからない");
						if (!(parentframe.readSlotValue(inFrameSystem, "親", false) == null)) {

						//祖父母の名前をとってくる
						ArrayList<String> olist1 = parentframe.getmVals("親");

						//祖父母の数だけループ
						for (int j = 0; j < olist1.size(); j++) {

							//祖父母の名前をとる
							String parentname1 = (String) olist1.get(j);

							//祖父母の名前を元に祖父母のフレームをとってくる
							AIFrame parentframe1 = inFrameSystem.getFrame(parentname1);

							//祖父母の逆リンクから子供の名前をとってくる
							//klistは親の兄弟のリスト
							ArrayList<String> klist = parentframe1.getLeankersSlotNames("親");

							//祖父母の子供の数(親の兄弟の数)だけループ
							for (int k = 0; k < klist.size(); k++) {

								//祖父母の子供の名前をとってくる
								String name = (String) klist.get(k);

								//祖父母の子供で自分の親かどうかを判定
								if (!parentname.equals(name)){

									//親の兄弟の名前から親の兄弟のフレームをとってくる
									AIFrame frame = inFrameSystem.getFrame(klist.get(k));

									//親の兄弟の逆リンクから親の兄弟の子供の名前をとってくる
									//klist1は親の兄弟の子供(従兄弟)のリスト
									ArrayList<String> klist1 = frame.getLeankersSlotNames("親");

									//親の兄弟の子供の数(従兄弟の数)だけループ
									for (int l = 0; l < klist1.size(); l++) {
										//既に追加されていたら追加しない
										if (!relist.contains(klist1.get(l))) {
											//従兄弟の名前を従兄弟としてリストに入れる
											relist.add(klist1.get(l));
											AIFrame frame1 = inFrameSystem.getFrame(klist1.get(l));
										}
									}
								}
							}
						}
					}
				}
			}

			 if(relist.size()==0)
				 relist.add("登録されていないため分からない");
			//relistを返す
			return AIFrame.makeEnum(new ArrayList<String>(relist));
		}
	}
	/**
	 * 孫を返すデモン
	 */
	public static class Grandchi extends AIDemonProc {
		public Object eval(AIFrameSystem inFrameSystem, AIFrame inFrame,
				String inSlotName, Iterator inSlotValues, Object inOpts) {

			// 条件を満たすものを入れるリスト
			ArrayList<String> relist = new ArrayList<String>();

			// 親の逆リンクから子供の名前(自分を親だと見て接続している名前)をとってくる
			// klistは子供の名前のリスト
				ArrayList<String> klist = inFrame
						.getLeankersSlotNames("親");

			// 子供の数だけループ
				for (int i = 0; i < klist.size(); i++) {

					//子供の名前から子供のフレームをとってくる
					AIFrame childframe = inFrameSystem.getFrame(klist.get(i));
					//親が登録されているかチェック
					if (!(childframe.readSlotValue(inFrameSystem, "親", false) == null)){
						// 親の逆リンクから子供の名前(自分を親だと見て接続している名前)をとってくる
						// gclistは孫の名前のリスト
							ArrayList<String>  gclist = childframe
									.getLeankersSlotNames("親");
							// 孫の数だけループ
							for (int j = 0; j < gclist.size(); j++) {
								// 孫の名前をとる
//								String gchildname = (String) gclist.get(j);
								// 孫の名前を元に孫のフレームをとってくる
		//						AIFrame gchildframe = inFrameSystem.get_Frame(gchildname);
								// 二回目以降のループ時すでに前のループで登録されてないか調べる
								if (!relist.contains(gclist.get(j))) {
									// 初めての名前なら孫としてリストに入れる
									relist.add(gclist.get(j));
								}
							}
					}
				}
				if(relist.size()==0)
					relist.add("登録されていないため分からない");
				// relistを返す
				return AIFrame.makeEnum(new ArrayList<String>(relist));
		}
	}


	/**
	 * 甥を返すデモン
	 */
	public static class Nephew extends AIDemonProc {
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

						// 親の逆リンクから子供の名前(自分を親だと見て接続している名前)をとってくる
						// klistは兄弟の名前のリスト
						ArrayList<String> klist = parentframe.getLeankersSlotNames("親");


						// 親の子供の数(兄弟の数)だけループ
						for (int j = 0; j < klist.size(); j++) {

							// 兄弟の名前から兄弟のフレームをとってくる
							AIFrame frame = inFrameSystem.getFrame(klist.get(j));

							//対象が自分の場合は除く
							if(!(frame.equals(inFrame))){
							// 親の逆リンクから子供の名前(自分を親だと見て接続している名前)をとってくる
							// neplistは兄弟の子供の名前のリスト
							ArrayList<String> neplist = frame.getLeankersSlotNames("親");

								// 親の兄弟の子供の数だけループ
								for (int k = 0; k < neplist.size(); k++) {
									// 兄弟の子供の名前から兄弟の子供のフレームをとってくる
									AIFrame nepframe = inFrameSystem.getFrame(neplist.get(k));

									//性別スロットの値が男なら甥
									if (nepframe.readSlotValue(inFrameSystem, "性別",false).equals("男")){

										//前のループで登録されてないか調べる
										if (!relist.contains(neplist.get(k))) {
											// 初めての名前なら甥としてリストに入れる
											relist.add(neplist.get(k));
										}
									}
								}
							}
						}
					}
				}
				if(relist.size()==0)
					relist.add("登録されていないため分からない");
				// relistを返す
				return AIFrame.makeEnum(new ArrayList<String>(relist));
		}
	}


	/**
	 * 姪を返すデモン
	 */
	public static class Niece extends AIDemonProc {
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

						// 親の逆リンクから子供の名前(自分を親だと見て接続している名前)をとってくる
						// klistは兄弟の名前のリスト
						ArrayList<String> klist = parentframe.getLeankersSlotNames("親");


						// 親の子供の数(兄弟の数)だけループ
						for (int j = 0; j < klist.size(); j++) {

							// 兄弟の名前から兄弟のフレームをとってくる
							AIFrame frame = inFrameSystem.getFrame(klist.get(j));

							//対象が自分の場合は除く
							if(!(frame.equals(inFrame))){
							// 親の逆リンクから子供の名前(自分を親だと見て接続している名前)をとってくる
							// neplistは兄弟の子供の名前のリスト
							ArrayList<String> neplist = frame.getLeankersSlotNames("親");

								// 親の兄弟の子供の数だけループ
								for (int k = 0; k < neplist.size(); k++) {
									// 兄弟の子供の名前から兄弟の子供のフレームをとってくる
									AIFrame nepframe = inFrameSystem.getFrame(neplist.get(k));

									//性別スロットの値が女なら姪
									if (nepframe.readSlotValue(inFrameSystem, "性別",false).equals("女")){

										//前のループで登録されてないか調べる
										if (!relist.contains(neplist.get(k))) {
											// 初めての名前なら姪としてリストに入れる
											relist.add(neplist.get(k));
										}
									}
								}
							}
						}
					}
				}
				if(relist.size()==0)
					relist.add("登録されていないため分からない");
				// relistを返す
				return AIFrame.makeEnum(new ArrayList<String>(relist));
		}
	}


}
