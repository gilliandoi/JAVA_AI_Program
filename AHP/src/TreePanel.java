
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

class TreePanel extends Panel implements Runnable,MouseListener,MouseMotionListener {
    Color selectColor,defaultColor;
    Thread linner;
    int id = 2;
    Node node[] = new Node[50];
    Node itsme;
    Image offscreen;
    Dimension offscreensize;
    Graphics offgraphics;
    Vector alternative_ids = new Vector();
    PairwiseComparisonFrame pcframe;
    MatrixFrame mframe;
    boolean enabled = true;
    boolean show_weights = true;
    AHP ahp;

    // Constructer
    TreePanel(String problem, Vector alternative, AHP theAhp){
	ahp = theAhp;
	setSize(400,300);
	Dimension d = getSize();
	this.selectColor = Color.black;
	this.defaultColor = Color.gray;

	// Make a Alternaive Nodes (id > 1)
	int k = d.width / (alternative.size()+1);
	for(int i=0;i<alternative.size();i++){
	    Vector alt_child_ids = new Vector(); // size() = 0
	    //newNode((String)alternative.elementAt(i),id,1,
	    //alt_child_ids,50*i+100,250);
	    newNode((String)alternative.elementAt(i),id,1,
		    alt_child_ids,k*(i+1),d.height-50);
	    // NOTE: ALTERNATIVES DO NOT NEED TO KEEP PARENT IDS.
	    //       BUT, HERE, I GIVE 1 ALTERNAIVES' PARENT ID TENTATIVELY.
	    alternative_ids.addElement(new Integer(id));
	    id++;
	}
	// Make a Problem Node (id = 1)
	newNode(problem,1,0,alternative_ids,d.width/2,20);
	node[1].active = true;
	itsme = node[1];

	initNodeValues();

	addMouseListener(this);
	addMouseMotionListener(this);

	start();
    }

    public Dimension getPreferredSize(){
	return new Dimension(400,300);
    }

    public Dimension getMinimumSize(){
	return new Dimension(400,300);
    }

    void newNode(String name, int n_id, int parent_id, Vector child_ids,
                 int x, int y) {
    	Node n = new Node();
	n.name = name;
	n.id = n_id;
	n.parent_id = parent_id;
	n.child_ids = child_ids;
	n.x = x;
	n.y = y;
	n.active = false;
	//System.out.println(n_id);
	n.initMatrix();

	node[n_id] = n;
    }

    public void disabled(){
	enabled = false;
    }

    public void enabled(){
	enabled = true;
    }

    public void run(){
	while(linner != null){
	    calculateWeights();
	    repaint();
	    try{
		Thread.sleep(100);
	    }catch (InterruptedException e){
		break;
	    }
	}
    }

    public void update(Graphics g){
	paint(g);
	//System.out.println(itsme.name);
    }

    public void paint(Graphics g){
	Dimension d = getSize();
        if ((offscreen == null) || (d.width != offscreensize.width)
	    			|| (d.height != offscreensize.height)) {
            offscreen = createImage(d.width, d.height);
            offscreensize = d;
            offgraphics = offscreen.getGraphics();
            offgraphics.setFont(getFont());
        }
	offgraphics.setColor(Color.white);
	offgraphics.fillRect(0,0,d.width,d.height);

	offgraphics.setFont(new Font("Helvetica",Font.BOLD,18));
	paintLines(offgraphics);
	paintNodes(offgraphics);

	g.drawImage(offscreen,0,0,null);
    }

    public void paintNodes(Graphics g){
	for(int i = 1 ; i < id ; i++){
	    if ( node[i] != null && i != itsme.id){
		paintANode(i,g);
	    }
	}
	paintANode(itsme.id,g);
    }

    public void paintANode(int i,Graphics g){
	FontMetrics fm = g.getFontMetrics();
	int x = node[i].x;
	int y = node[i].y;
	int dw = 0;
	int dh = 5;
	if ( node[i].child_ids.size() == 0 ){
	    dw = (int)(node[i].value * 50);
	    dh = (int)(node[i].value * 50);
	}
	/*
	  int w = fm.stringWidth(node[i].name) + 5;
	  int h = fm.getHeight() + 4;
	  */
	int w = fm.stringWidth(node[i].name) + dw + 5;
	int h = fm.getHeight() + dh - 5;

	g.setColor(Color.white);
	g.drawRoundRect(x - w/2, y - h/2, w+1, h+1, 5, 5);
	g.fillRoundRect(x - w/2, y - h/2, w+1, h+1, 5, 5);
	if ( node[i].active == false ){
	    g.setColor(new Color(220,220,220));
	} else if ( node[i].active == true){
	    g.setColor(selectColor);
	    g.drawRoundRect(x - w/2 - 1, y - h/2 - 1, w+3, h+3, 6, 6);
	}
	g.drawRoundRect(x - w/2, y - h/2, w+1, h+1, 5, 5);

	//g.setColor(Color.black);
	if ( node[i].active == false ){
	    //g.setColor(Color.black);
	    g.setColor(defaultColor);
	} else if ( node[i].active == true){
	    g.setColor(selectColor);
	}
	g.drawString(node[i].name,x-(w-10)/2,y-h/4+fm.getAscent());

	if(node[i].child_ids.size()==0 && show_weights){
	    Double av = new Double(node[i].value);
	    String av_value = av.toString();
	    if(av_value.length()>6){
		av_value = av_value.substring(0,5);
	    }
	    g.drawString(av_value,x-w/4,y+h);
	}
    }

    public void paintLines(Graphics g){
	for(int i = 1 ; i < id ; i++){
	    if ( node[i] != null && i != itsme.id){
		paintLinesForANode(node[i],g);
	    }
	}
	paintLinesForANode(itsme,g);
    }

    public void paintLinesForANode(Node theNode,Graphics g){
	Vector child_ids = theNode.child_ids;
	if(theNode.child_ids.size() != 0){
	    for(int i=0;i<child_ids.size();i++){
		int c_id =
		    Integer.parseInt(child_ids.elementAt(i).toString());
		if ( theNode.active == false ){
		    g.setColor(defaultColor);
		} else if ( theNode.active == true){
		    g.setColor(selectColor);
		}
		g.drawLine(theNode.x,theNode.y,
			   node[c_id].x,node[c_id].y);

		if(show_weights){
		    String value = theNode.child_value.get(
			    new Integer(c_id)).toString();
		    if(value.length()>6){
			value = value.substring(0,5);
		    }
		    g.drawString(value,
				 (theNode.x + node[c_id].x) / 2,
				 (theNode.y + node[c_id].y) / 2);
		}
	    }
	}
    }


    public void paintLine(Graphics g,int n_id){
	//g.setColor(Color.black);
	Vector child_ids = node[n_id].child_ids;
	if(node[n_id].child_ids.size() != 0){
	    for(int i=0;i<child_ids.size();i++){
		int c_id =
		    Integer.parseInt(child_ids.elementAt(i).toString());
		if ( node[n_id].active == false ){
		    //g.setColor(Color.black);
		    g.setColor(defaultColor);
		} else if ( node[n_id].active == true){
		    g.setColor(selectColor);
		}
		g.drawLine(node[n_id].x,node[n_id].y,
			   node[c_id].x,node[c_id].y);
		if(show_weights){
		    String value =
			node[n_id].child_value.get(
			    new Integer(c_id)).toString();
		    if(value.length()>6){
			value = value.substring(0,5);
		    }
		    g.drawString(value,
				 (node[n_id].x + node[c_id].x) / 2,
				 (node[n_id].y + node[c_id].y) / 2);
		}
		paintLine(g,c_id);
	    }
	}
    }

    public synchronized void mousePressed(MouseEvent e){
	int x = e.getX();
	int y = e.getY();
	if(enabled){
	    int best = Integer.MAX_VALUE;
	    for (int i = 1 ; i < id ; i++) {
		if ( node[i] == null ){
		    continue;
		}
		Node n = node[i];
		int dist = (n.x - x) * (n.x - x) + (n.y - y) * (n.y - y);
		if (dist < best) {
		    itsme = n;
		    best = dist;
		}
	    }
	    itsme.x = x;
	    itsme.y = y;
	    for(int i = 1 ; i < id ; i++){
		if ( node[i] == null){
		    continue;
		}
		node[i].active = false;
	    }
	    itsme.active = true;
	    repaint();
	}
    }

    public synchronized void calculateWeights(){
	// INITIALIZE
	for(int i = 1 ; i < id ; i++){
	    if(node[i] != null){
		if(node[i].child_ids.size() == 0){
		    node[i].value = 0;
		}
	    }
	}
	calcWeights(1,(double)1);
    }

    public void calcWeights(int n_id,double value){
	Vector child_ids = node[n_id].child_ids;
	if(node[n_id].child_ids.size() == 0){
	    node[n_id].value = value + node[n_id].value;
	} else {
	    for(int i=0;i<child_ids.size();i++){
		int c_id = Integer.parseInt(child_ids.elementAt(i).toString());
		double v = ((Double)node[n_id].child_value.get(new Integer(c_id))).doubleValue();
		v = v * value;
		calcWeights(c_id,v);
	    }
	}
    }

    public synchronized void mouseClicked(MouseEvent e){
	if(e.getClickCount() == 2){
	    if(itsme.child_ids.size() > 1){
		pairwisecomparison();
		disabled();
		ahp.disableAllButton();
	    }
	}
    }
    public synchronized void mouseEntered(MouseEvent e){}
    public synchronized void mouseExited(MouseEvent e){}
    public synchronized void mouseMoved(MouseEvent e){}
    public synchronized void mouseDragged(MouseEvent e){
	if(enabled){
	    itsme.x = e.getX();
	    itsme.y = e.getY();
	    repaint();
	}
    }

    public synchronized void mouseReleased(MouseEvent e){
	if(enabled){
	    itsme.x = e.getX();
	    itsme.y = e.getY();
	    //itsme = null;

	    repaint();
	}
    }

    public synchronized void rename(){
	String newtext = new String();

	InputFrame rename = new InputFrame("RENAME",this);
	rename.show();
    }

    public void setNewname(String newname){
	itsme.name = newname;
	repaint();
    }

    public synchronized void pairwisecomparison(){
	pcframe = new PairwiseComparisonFrame(this);
	mframe = new MatrixFrame(this);
	pcframe.show();
	mframe.show();
    }

    public boolean equalAlternativeIds(Vector childIds){
	for(int i = 0 ; i < childIds.size() ; i++){
	    int aId = ((Integer)alternative_ids.elementAt(i)).intValue();
	    int cId = ((Integer)childIds.elementAt(i)).intValue();
	    if(aId != cId){
		return false;
	    }
	}
	return true;
    }

    public synchronized void remove(){
	if( itsme.parent_id != 0 && itsme.child_ids.size() != 0){
	    Node parentNode;
	    parentNode = node[itsme.parent_id];

	    //System.out.println("Hi");
	    //if( itsme.child_ids == alternative_ids ){
	    if(equalAlternativeIds(itsme.child_ids)){
		if ( parentNode.child_ids.size() > 1){
		    parentNode.child_ids.removeElement(new Integer(itsme.id));
		} else if (parentNode.child_ids.size() == 1){
		    parentNode.child_ids = alternative_ids;
		}
	    //} else if ( itsme.child_ids != alternative_ids){
	    } else if (!equalAlternativeIds(itsme.child_ids)){
		for(int j=0; j < itsme.child_ids.size(); j++){
		    parentNode.child_ids.addElement(itsme.child_ids.elementAt(j));
		}
		parentNode.child_ids.removeElement(new Integer(itsme.id));

		for(int j=0; j < itsme.child_ids.size(); j++){
		    int c_id =
			Integer.parseInt(itsme.child_ids.elementAt(j).toString());
		    node[c_id].parent_id = itsme.parent_id;
		}
	    }
	    node[itsme.parent_id].initChildValue();
	    node[itsme.parent_id].initMatrix();

	    node[itsme.id] = null;
	    itsme = node[1];
	    itsme.active = true;
	    repaint();
	}
    }

    public synchronized void create(){
	if (itsme.child_ids.size() != 0) { // THEY ARE ALTERNATIVES.
	    Vector childIdsOrg;
	    String name;
	    int x,y;

	    childIdsOrg = itsme.child_ids;
	    x = itsme.x;
	    y = (300 - itsme.y)/2 + itsme.y;
	    name = "unnamed";
 	    //if (itsme.child_ids != alternative_ids){
	    if(!equalAlternativeIds(itsme.child_ids)){
 		itsme.child_ids.addElement(new Integer(id));
 		newNode(name,id,itsme.id,alternative_ids,x,y);
	    } else if (equalAlternativeIds(itsme.child_ids)){
		//} else if (itsme.child_ids == alternative_ids){
 		Vector parent_child_ids = new Vector();
 		parent_child_ids.addElement(new Integer(id));
 		itsme.child_ids = parent_child_ids;
 		newNode(name,id,itsme.id,childIdsOrg,x,y);
 	    }
	    for(int i = 0 ; i < childIdsOrg.size() ; i++){
		int c_id =
		    ((Integer)childIdsOrg.elementAt(i)).intValue();
		node[c_id].parent_id = itsme.id;
	    }

	    node[id].initChildValue();
	    node[id].initMatrix();
	    node[itsme.id].initChildValue();
	    node[itsme.id].initMatrix();
	    id++;
	    // reinitNodeValues(itsme.id);
	    repaint();
    	}
    }

    public void initNodeValues(){
	for(int i = 1 ; i < id ; i++){
	    if(node[i] != null){
		node[i].initChildValue();
	    }
	}
    }

    public void reinitNodeValues(int n_id){
	Vector child_ids = node[n_id].child_ids;
	if(node[n_id].child_ids.size() != 0){
	    node[n_id].initChildValue();
	    for(int i=0;i<child_ids.size();i++){
		int c_id = Integer.parseInt(child_ids.elementAt(i).toString());
		reinitNodeValues(c_id);
	    }
	}
    }

    public void start() {
	linner = new Thread(this);
	linner.start();
    }

    public void stop() {
	linner.stop();
    }

    public void load(){
	Frame fdFrame = new Frame("FD");
	FileDialog fd =
	    new FileDialog(fdFrame,"Load a File",FileDialog.LOAD);
	fd.setDirectory(System.getProperty("user.dir"));
	fd.show();
	String dir = fd.getDirectory();
	String file = fd.getFile();

	try{
	    File f = new File(dir+file);
	    FileInputStream iStream = new FileInputStream(f);
	    ObjectInputStream dis = new ObjectInputStream(iStream);
	    node = (Node[])dis.readObject();
	    dis.close();
	    iStream.close();
	} catch(Exception e){
	    System.out.println(e);
	}

	for(int i = 0 ; i < 50 ; i++){
            if(node[i]!= null){
		if(node[i].active){
		    itsme = node[i];
		}
                id = i + 1;
            }
        }
    }

    public void save(){
	Frame fdFrame = new Frame("FD");
	FileDialog fd =
	    new FileDialog(fdFrame,"Save a File",FileDialog.SAVE);
	fd.setDirectory(System.getProperty("user.dir"));
	fd.show();
	String dir = fd.getDirectory();
	String file = fd.getFile();

	try{
	    File f = new File(dir+file);
	    FileOutputStream oStream = new FileOutputStream(f);
	    ObjectOutputStream dos = new ObjectOutputStream(oStream);
	    dos.writeObject(node);
	    dos.close();
	    oStream.close();
	} catch(Exception e){
	    System.out.println(e);
	}
    }


}
