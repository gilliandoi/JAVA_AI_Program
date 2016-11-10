package common;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import app.BusStationInfoList;
import app.BusStationItems;

/*
 * CSVファイル読み込む
 */
public class CSVFileRead {
	@SuppressWarnings("unchecked")
	/*
	 * 駅情報取得
	 */
	public BusStationInfoList getBusStationList() {
		try {

			System.out.println("Read bus station info from csv file.");

			// ファイルを読み込む
			FileReader fr = new FileReader("src/stub/resources/station.csv");
			BufferedReader br = new BufferedReader(fr);

			// 駅情報リスト
			BusStationInfoList busStationList = new BusStationInfoList();

			// 読み込んだファイルを１行ずつ処理する
			String line;
			StringTokenizer token;
			while ((line = br.readLine()) != null) {
				// 駅情報
				BusStationItems busStationItems = new BusStationItems();

				// 区切り文字","で分割する
				token = new StringTokenizer(line, ",");

				Integer i = 0;
				// 分割した文字列、数値を駅情報にセットする。
				while (token.hasMoreTokens()) {
					i++;

					switch (i) {
					case 1:
						busStationItems.setName(token.nextToken());
					case 2:
						busStationItems.setX(Integer.parseInt(token.nextToken()));
						busStationItems.setCx();
						busStationItems.setAx();
					case 3:
						busStationItems.setY(Integer.parseInt(token.nextToken()));
						busStationItems.setCy();
						busStationItems.setAy();
					case 4:
						busStationItems.setHValue(Integer.parseInt(token.nextToken()));
					case 5:
						busStationItems.setChildStationStr(token.nextToken());
					default:
						break;
					}

				}

				busStationList.addStation(busStationItems);

			}

			// 終了処理
			br.close();

			// 駅情報リストをリターンする
			return busStationList;

		} catch (IOException e) {
			// 例外発生時処理
			System.out.println("例外が発生しました。") ;
			e.printStackTrace();
		}

		return null;
	}
}
