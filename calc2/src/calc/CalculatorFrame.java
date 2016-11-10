package calc;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

class CalculatorFrame extends Frame implements ActionListener, WindowListener {
	Label text;
	Button button[];
	Panel numberPanel;
	Button clear, plus, minus, sin, sqrt, equal;
	Panel commandPanel;
	String buffer;
	Double result;
	String operator;
	boolean append;

	/*
	 * 電卓初期化
	 */
	CalculatorFrame() {
		text = new Label(buffer, Label.RIGHT);
		button = new Button[10];
		for (int i = 0; i < 10; i++) {
			button[i] = new Button((new Integer(i)).toString());
			button[i].addActionListener(this);

		}

		clear = new Button("C");
		clear.addActionListener(this);
		plus = new Button("+");
		plus.addActionListener(this);
		minus = new Button("-");
		minus.addActionListener(this);
		equal = new Button("-");
		equal.addActionListener(this);
		sqrt = new Button("√");
		sqrt.addActionListener(this);
		sin = new Button("sin");
		sin.addActionListener(this);
		equal = new Button("=");
		equal.addActionListener(this);

		numberPanel = new Panel();
		numberPanel.setLayout(new GridLayout(4, 3));
		for (int i = 1; i < 10; i++) {
			numberPanel.add(button[i]);

		}
		numberPanel.add(button[0]);

		numberPanel.add(sin);
		numberPanel.add(sqrt);
		commandPanel = new Panel();
		commandPanel.setLayout(new GridLayout(4, 1));
		commandPanel.add(clear);

		commandPanel.setLayout(new GridLayout(4, 2));
		commandPanel.add(plus);

		commandPanel.setLayout(new GridLayout(4, 3));
		commandPanel.add(minus);

		commandPanel.setLayout(new GridLayout(4, 4));
		commandPanel.add(equal);

		setLayout(new BorderLayout());
		add("North", text);
		add("Center", numberPanel);
		add("East", commandPanel);
		pack();

		// window
		setSize(150, 200);
		// center
		setLocationRelativeTo(null);

		buffer = "0";
		showBuffer();

		addWindowListener(this);
	}

	/*
	 * イベントオブジェクト取得条件
	 *
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == clear) {
			// 「C」押す
			buffer = "0";
			showBuffer();
		} else {
			if (source == plus) {
				// 「+」
				operator = "plus";
				result = Double.parseDouble(buffer);

				append = false;
				text.setText("+");
			} else if (source == minus) {
				// 「-」
				operator = "minus";
				result = Double.parseDouble(buffer);

				append = false;
				text.setText("-");
			} else if (source == sin) {
				// 「sin」
				operator = "sin";
				buffer = event.getActionCommand();
				append = false;

				text.setText("sin");
			} else if (source == sqrt) {
				// 「√」
				operator = "sqrt";
				buffer = event.getActionCommand();
				append = false;

				text.setText("√");
			} else if (source == equal) {
				// 計算メッソドを呼び出す
				calculate();
				append = false;
			} else {
				// 0～9の場合
				if (append) {
					// 前回入力したのは数字の場合
					buffer = buffer + event.getActionCommand();
				} else {
					buffer = event.getActionCommand();
				}
				append = true;
				showBuffer();
			}

		}
	}

	/*
	 * 文字列の頭0を消す
	 */
	void showBuffer() {
		if ((int) Double.parseDouble(buffer) == Double.parseDouble(buffer)) {
			buffer = Integer.toString((int) Double.parseDouble(buffer));
		}
		text.setText(buffer);
	}

	/*
	 * 計算メッソド
	 */
	void calculate() {
		try {
			if (operator.equals("plus")) {
				result = result + Integer.parseInt(buffer);
				buffer = String.valueOf(result);
				showBuffer();
			} else if (operator.equals("minus")) {
				result = result - Integer.parseInt(buffer);
				buffer = String.valueOf(result);
				showBuffer();
			} else if (operator.equals("sin")) {
				result = (Double) Math.sin(Math.sin(Math.toRadians(Integer.parseInt(buffer))));
				buffer = String.valueOf(result);
				showBuffer();
			} else if (operator.equals("sqrt")) {
				result = (Double) Math.sqrt(Integer.parseInt(buffer));
				buffer = String.valueOf(result);
				showBuffer();
			} else if (operator.equals("clear")) {
				buffer = "0";
				showBuffer();
			} else {
				result = Double.parseDouble(buffer);
				showBuffer();
			}
		} catch (Exception e) {
			text.setText("Error！");
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		System.exit(0);

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}
}
