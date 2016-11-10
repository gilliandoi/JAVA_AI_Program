/*
 * ショップマスタークラス
 */
class ShopMaster extends Thread {

	Counter counter;
	String name;
	boolean threadExitFlag=false;

	ShopMaster(Counter theCounter,String theName){
		this.counter=theCounter;
		this.name = theName;
	}

	/*
	 * runメソッドを宣言
	 * @see java.lang.Thread#run()
	 */
	public void run(){
		while(!threadExitFlag){
			try{
				// 在庫はなくなった
				if(!counter.putCoffee(name)){
					threadExitFlag = true;
				}
				// コーヒーをぼちぼち作る
				Thread.sleep((int)(3000*Math.random()));
			}catch(InterruptedException e){
				// 処理なし
			}
		}
	}
}
