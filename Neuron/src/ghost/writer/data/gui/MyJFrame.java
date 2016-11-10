package ghost.writer.data.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ghost.writer.data.data.WordMap;
import ghost.writer.data.logic.BPFactory;
import ghost.writer.data.logic.WordDataFactory;
import ghost.writer.data.logic.WordDataMap;
import ghost.writer.data.util.Util;
/**
 * 手入力識別画面
 */
public class MyJFrame extends JFrame implements WindowListener {


	private static final long serialVersionUID = 1L;

	private MyJPanel panel;
	private JButton jbu_tell, jbu_learn, jbu_clean, jbu_other;

	public static JFrame context;

	/**
	 * DBファイル名
	 */
	private final String BPFileName = "DPData.ser";
	private final String WordFileName = "WordData.ser";

	/**
	 * 手入力識別画面
	 */
	public MyJFrame() {
		super("手入力識別画面");

		context = this;
		this.setLocation(200, 100);

		panel = new MyJPanel();
		this.add(panel);

		// ボタン設定
		JPanel func_panel = new JPanel();
		jbu_tell = new JButton("識別");
		jbu_tell.setEnabled(false);
		jbu_learn = new JButton("学習");
		jbu_learn.setEnabled(false);
		jbu_clean = new JButton("クリア");
		jbu_clean.setEnabled(false);
		jbu_other = new JButton("その他");
		jbu_other.setEnabled(false);
		func_panel.setLayout(new GridLayout(8, 1));
		addButtonHelper(func_panel, jbu_tell);
		addButtonHelper(func_panel, jbu_learn);
		addButtonHelper(func_panel, jbu_clean);
		addButtonHelper(func_panel, jbu_other);

		//ボタンのイベント設定
		//クリアボタン
		jbu_clean.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				panel.clearScreen();
			}
		});
		//その他ボタン
		jbu_other.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				panel.analysis();
			}
		});
		//学習ボタン
		jbu_learn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new LearnJFrame(context, panel.getMap()).setVisible(true);
			}
		});
		//識別ボタン
		jbu_tell.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String result[] = Util.getMatchString(panel.getMap());
				for (int i = 0; i < 5; ++i) {
					System.out.print(result[i] + "|");
				}
				System.out.println();
				new ExamJFrame(context, panel.getMap(), result[0])
						.setVisible(true);
			}
		});

		this.add(func_panel, BorderLayout.EAST);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.addWindowListener(this);

		new Thread(new Runnable() {

			@Override
			public void run() {
				long oldTime = System.currentTimeMillis();
				File file = new File(BPFileName);
				if (file.exists()) {
					try {
						BPFactory.initialization(file);
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					BPFactory.initialization(WordMap.unit_width
							* WordMap.unit_height, WordMap.unit_width
							* WordMap.unit_height + 16 * WordDataMap.MAX_LENTH,
							16 * WordDataMap.MAX_LENTH);
				}

				file = new File(WordFileName);
				if (file.exists()) {
					try {
						WordDataFactory.initialization(file);
					} catch (ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} else {
					WordDataFactory.initialization();
				}

				jbu_tell.setEnabled(true);
				jbu_learn.setEnabled(true);
				jbu_clean.setEnabled(true);
				jbu_other.setEnabled(true);

				JOptionPane.showMessageDialog(context, "BPニューラルネットワーク初期化完了"
						+ (System.currentTimeMillis() - oldTime) + "ms", "INFO",
						JOptionPane.NO_OPTION);
			}

		}).start();
	}

	/**
	 * ボタン追加
	 *
	 * @param base
	 * @param button
	 */
	private void addButtonHelper(JPanel base, JButton button) {
		JPanel temp_panel = new JPanel();
		base.add(temp_panel);
		temp_panel.add(button);
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * アプリ閉じる
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		new Thread(new Runnable() {

			@Override
			public void run() {

				// DBファイル作成
				File file = new File(BPFileName);
				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				// DBファイルへ保存
				try {
					BPFactory.save(file);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				file = new File(WordFileName);
				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				try {
					WordDataFactory.save(file);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.exit(0);
			}

		}).start();
		JOptionPane.showMessageDialog(context, "データ保存中...", "INFO",
				JOptionPane.NO_OPTION);
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}
