package app;

import java.util.ArrayList;
import java.util.StringTokenizer;

/*
 * 駅情報リスト
 */
public class BusStationInfoList {

	private ArrayList<Object> list;

	public BusStationInfoList() {
		list = new ArrayList();
	}

	/*
	 * 駅情報追加（一駅に対して）
	 */
	public void addStation(BusStationItems item) {
		list.add(item);
	}

	/*
	 * 駅情報取得（INDEX）
	 */
	public BusStationItems getStation(Integer index) {
		int i = 0;

		while (i < list.size()) {
			if (i == index) {
				return (BusStationItems) list.get(i);
			}
			i++;
		}
		return null;
	}

	/*
	 * 駅情報取得（名前）
	 */
	public BusStationItems findStation(String theName) {
		int i = 0;

		while (i < list.size()) {
			BusStationItems station = (BusStationItems) list.get(i);
			if (theName.equals(station.getName())) {
				return station;
			}
			i++;
		}
		return null;
	}

	/*
	 * 子駅情報取得（親駅）
	 */
	public BusStationInfoList findChildren(BusStationItems parent) {
		BusStationInfoList childrenList = new BusStationInfoList();

		// 子駅文字列をSplit
		StringTokenizer token = new StringTokenizer(parent.getChildStationStr(), ":");

		while (token.hasMoreTokens()) {
			// 子駅名
			String childName = token.nextToken();
			// 子Node名を元に、駅情報を取得する
			BusStationItems childInfo = findStation(childName);
			childrenList.addStation(childInfo);
		}

		return childrenList;
	}

	/*
	 * 開始駅情報取得
	 */
	public BusStationItems findStart() {
		BusStationItems station = (BusStationItems) list.get(0);

		return station;
	}

	/*
	 * 目標駅情報取得
	 */
	public BusStationItems findGoal() {
		BusStationItems station = (BusStationItems) list.get(list.size()-1);

		return station;
	}

	/*
	 * 駅数量
	 */
	public Integer size() {
		return list.size();
	}
}
