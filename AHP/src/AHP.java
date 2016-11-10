import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

public class AHP extends Frame implements ActionListener,ItemListener{
    TreePanel tree;
    Panel command;
    Button create,remove,rename,comparison;
    Button load,save,quit;
    Checkbox show_check;
    MenuBar menuBar;
    Menu mFile,mEdit;
    MenuItem miLoad,miSave,miQuit;
    MenuItem miCreate,miRemove,miRename,miComparison;
    Image money_image;
    MediaTracker money_tracker;

    public AHP(){
	setTitle("Analytic Hierarchy Process");
	setSize(400,300);
	int i=0;
        String problem = "Choosing a Destination";
	Vector alternative = new Vector();
	alternative.addElement("Los Angeles");
	alternative.addElement("San Francisco");
	alternative.addElement("Las Vegas");

        tree = new TreePanel(problem,alternative,this);
	command = new Panel();
	command.setLayout(new GridLayout(2,4));
	create = new Button("CREATE");
	create.addActionListener(this);
	command.add(create);
	remove = new Button("REMOVE");
	remove.addActionListener(this);
	command.add(remove);
	rename = new Button("RENAME");
	rename.addActionListener(this);
	command.add(rename);
	comparison = new Button("COMPARISON");
	comparison.addActionListener(this);
	command.add(comparison);
	quit = new Button("QUIT");
	quit.addActionListener(this);
	command.add(quit);

	show_check = new Checkbox("SHOW");
	show_check.addItemListener(this);
	command.add(show_check);
	show_check.setState(true);

	load = new Button("LOAD");
	load.addActionListener(this);
	command.add(load);
	save = new Button("SAVE");
	save.addActionListener(this);
	command.add(save);

	setLayout(new BorderLayout());
	add("South",command);
	add("Center",tree);

	// MENU
	menuBar = new MenuBar();
	// File Menum
	mFile = new Menu("File");
	miLoad = new MenuItem("Load     ");
	miLoad.addActionListener(this);
	mFile.add(miLoad);
	miSave = new MenuItem("Save     ");
	miSave.addActionListener(this);
	mFile.add(miSave);
	miQuit = new MenuItem("Quit     ");
	miQuit.addActionListener(this);
	mFile.add(miQuit);
	menuBar.add(mFile);
	// Edit Menu
	mEdit = new Menu("Edit");
	miCreate = new MenuItem("Create   ");
	miCreate.addActionListener(this);
	mEdit.add(miCreate);
	miRemove = new MenuItem("Remove   ");
	miRemove.addActionListener(this);
	mEdit.add(miRemove);
	miRename = new MenuItem("Rename   ");
	miRename.addActionListener(this);
	mEdit.add(miRename);
	miComparison = new MenuItem("Comparison");
	miComparison.addActionListener(this);
	mEdit.add(miComparison);
	menuBar.add(mEdit);
	setMenuBar(menuBar);

	// IMAGES
	String sep = System.getProperty("file.separator");
	money_tracker = new MediaTracker(this);
	money_image = getToolkit().getImage("src/money.gif");
	money_tracker.addImage(money_image,1);
    }

    public void disableAllButton(){
	create.setEnabled(false);
	remove.setEnabled(false);
	comparison.setEnabled(false);
	rename.setEnabled(false);
    }

    public void enableAllButton(){
	create.setEnabled(true);
	remove.setEnabled(true);
	comparison.setEnabled(true);
	rename.setEnabled(true);
    }

    public void disableButton(String name){
	if( name.equals("COMPARISON")){
	    comparison.setEnabled(false);
	} else if( name.equals("RENAME")){
	    rename.setEnabled(false);
	}
    }

    public void enableButton(String name){
	if( name.equals("COMPARISON")){
	    comparison.setEnabled(true);
	} else if( name.equals("RENAME")){
	    rename.setEnabled(true);
	}
    }

    public void actionPerformed(ActionEvent event){
	Object source = event.getSource();
	if (source == create || source == miCreate){
	    tree.create();
	} else if (source == remove || source == miRemove){
	    tree.remove();
	} else if (source == rename || source == miRename){
	    tree.rename();
	    disableButton("RENAME");
	} else if (source == load || source == miLoad) {
	    tree.load();
	} else if (source == save || source == miSave) {
	    tree.save();
	} else if (source == quit || source == miQuit) {
	    System.exit(0);
	} else if (source == comparison || source == miComparison){
	    if(tree.itsme.child_ids.size() > 1){
		tree.pairwisecomparison();
		tree.disabled();
		disableAllButton();
	    }
	}
    }

    public void itemStateChanged(ItemEvent event){
	Object source = event.getSource();
	if(source == show_check){
	    tree.show_weights = ((Checkbox)source).getState();
	}
    }

    public static void main(String args[]){
	(new AHP()).show();
    }

}






