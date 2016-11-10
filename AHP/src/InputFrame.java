
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputFrame extends Frame implements ActionListener{
    String newtext = new String();
    TextField inputtext = new TextField();
    TreePanel tp;
    Button ok;
    Button cancel;

    InputFrame(String title,TreePanel p){
	setTitle(title);

	setSize(200,100);

	Panel command = new Panel();
	command.setLayout(new GridLayout(1,2));
	ok = new Button("OK");
	ok.addActionListener(this);
	command.add(ok);
	cancel = new Button("CANCEL");
	cancel.addActionListener(this);
	command.add(cancel);

	inputtext.addActionListener(this);

	setLayout(new BorderLayout());
	add("Center",inputtext);
	add("South",command);

	pack();

	tp = p;
    }

    public void actionPerformed(ActionEvent event){
	Object source = event.getSource();
	if(source == ok || source == inputtext){
	    tp.setNewname(inputtext.getText());
	    setVisible(false);
	    dispose();
	    tp.ahp.enableButton("RENAME");
	}else if (source == cancel){
	    setVisible(false);
	    dispose();
	    tp.ahp.enableButton("RENAME");
	}
    }
}
