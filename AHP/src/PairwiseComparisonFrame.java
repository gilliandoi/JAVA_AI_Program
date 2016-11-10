
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;

public class PairwiseComparisonFrame extends Frame implements ActionListener {
    Panel comparisons = new Panel();
    ComparisonPanel cp[];
    TreePanel tp;
    int now_card;
    int pn;
    Button ok;
    Button cancel;
    Button next;
    Button previous;

    PairwiseComparisonFrame(TreePanel p){
	setTitle("Pairwise Comparison for " + p.itsme.name);
	setSize(400,400);
	tp = p;

	Panel command = new Panel();
	command.setLayout(new GridLayout(1,4));
	ok     = new Button("OK");
	ok.addActionListener(this);
	command.add(ok);
	cancel = new Button("CANCEL");
	cancel.addActionListener(this);
	command.add(cancel);
	next   = new Button("NEXT");
	next.addActionListener(this);
	command.add(next);
	previous = new Button("PREVIOUS");
	previous.addActionListener(this);
	command.add(previous);

	int n = p.itsme.child_ids.size();
	comparisons.setLayout(new CardLayout());

	pn = 0;
	for(int i = 0 ; i < n ; i++ ){
	    for(int j = i + 1 ; j < n ; j++){
		pn++;
	    }
	}

	///Panel test = new Panel();
	///test.setLayout(new GridLayout(1,pn));

	//cp = new ComparisonPanel[100];
	cp = new ComparisonPanel[pn];

/*
	for(int i = 0 ; i < n ; i++){
	    for(int j = 0 ; j < i ; j++){
		cp[k] = new ComparisonPanel(p,i,j);
		comparisons.add(cp[k]);
		cp[k++].start();
	    }
	}
*/
	int k = 0;
	for(int i = 0 ; i < n ; i++){
	    for(int j = i+1 ; j < n ; j++){
		cp[k] = new ComparisonPanel(p,i,j);
		///test.add(cp[k]);
		comparisons.add(""+k,cp[k]);
		k++;
	    }
	}
	for(int i = 0 ; i < k ; i++){
	    cp[i].start();
	}
	//comparisons.show();
	setLayout(new BorderLayout());
	///add("Center",test);
	add("Center",comparisons);
	add("South",command);

	// At First, Only thread No. 0 is active.
	now_card = 0;
	cp[now_card].active = true;
    }

    public Dimension getPreferredSize(){
	return new Dimension(400,400);
    }

    public Dimension getMinimumSize(){
	return new Dimension(400,300);
    }

    /* Go to the No.(arg) card */
    public void gotoCard(String arg){
	((CardLayout)comparisons.getLayout()).show(comparisons,arg);
	cp[now_card].active = false;
	now_card = Integer.parseInt(arg);
	cp[now_card].active = true;
    }

    public void actionPerformed(ActionEvent event){
	Object source = event.getSource();
	if(source == cancel){
	    tp.mframe.mp.storeMatrix();
	    tp.mframe.setVisible(false);
	    tp.mframe.dispose();
	    setVisible(false);
	    dispose();
	    tp.enabled();
	    tp.ahp.enableAllButton();
	} else if (source == ok){
	    tp.mframe.mp.storeMatrix();
	    tp.mframe.setVisible(false);
	    tp.mframe.dispose();
	    setVisible(false);
	    dispose();
	    tp.enabled();
	    tp.ahp.enableAllButton();
	} else if (source == next){
	    ((CardLayout)comparisons.getLayout()).next(comparisons);
	    cp[now_card].active = false;
	    now_card++;
	    if(now_card > pn - 1 ){
		now_card = 0;
	    }
	    cp[now_card].active = true;
	} else if (source == previous){
	    ((CardLayout)comparisons.getLayout()).previous(comparisons);
	    cp[now_card].active = false;
	    now_card--;
	    if(now_card < 0 ){
		now_card = pn - 1;
	    }
	    cp[now_card].active = true;
	}
    }
}

class ComparisonPanel extends Panel implements Runnable,MouseListener,MouseMotionListener {
    String name1;
    String name2;
    String factor;
    Money money[] = new Money[16];
    Money itsme;
    MediaTracker money_tracker;
    Image offscreen;
    Dimension offscreensize;
    Graphics offgraphics;
    Thread foliage;
    TreePanel tp;
    int x = 0;
    int textwidth = 0;
    Image money_image;
    boolean active = false;
    int mcell_x,mcell_y;
    Dimension d;

    static String scale[] = {
	"EQUALLY",
	"Intermediate between EQUALLY and SLIGHTLY",
	"SLIGHTLY",
	"Intermediate between SLIGHTLY and STRONGLY",
	"STRONGLY",
	"Intermediate between STRONGLY and VERY STRONGLY",
	"VERY STRONGLY",
	"Intermediate between VERY STRONGLY and EXTREMELY",
	"EXTREMELY"
    };

    ComparisonPanel(TreePanel p,int i,int j){
	setSize(400,330);
	d = getSize();
	tp = p;

	mcell_y = i ;
	mcell_x = j ;

	initMoneys(i,j);
	/*
	for(int k = 0 ; k < 8 ; k++){
	    money[k] = new Money();
	    money[k].x = d.width / 4 - 30;
	    money[k].y = d.height - 50 - k * 10;
	}
	for(int k = 8 ; k < 16; k++){
	    money[k] = new Money();
	    money[k].x = d.width * 3 / 4 - 30;
	    money[k].y = d.height - 50 - (k-8) * 10;
	}
	*/
	//setLayout(new BorderLayout());
	int c_id1=Integer.parseInt(p.itsme.child_ids.elementAt(j).toString());
	int c_id2=Integer.parseInt(p.itsme.child_ids.elementAt(i).toString());
	name1 = p.node[c_id1].name;
	name2 = p.node[c_id2].name;
	factor = p.itsme.name;
	//add("North",new Label("Comparison:" + name1 + "<->" + name2));
	//System.out.println("Comparison:" + name1 + "<->" + name2);
	money_image = tp.ahp.money_image;
	money_tracker = tp.ahp.money_tracker;

	addMouseListener(this);
	addMouseMotionListener(this);
    }

    public void initMoneys(int x, int y){
	d = getSize();
	double n1 = tp.itsme.matrix[y][x];
	double n2 = tp.itsme.matrix[x][y];
	if( n1 < 1 ){
	    n1 = 16 - (n2+7);
	} else {
	    n1 = n1 + 7;
	}

	for(int k = 0 ; k < (int)n1 ; k++){
	    money[k] = new Money();
	    money[k].x = d.width / 4 - 30;
	    money[k].y = d.height - 50 - k * 10;
	}
	for(int k = (int)n1 ; k < 16; k++){
	    money[k] = new Money();
	    money[k].x = d.width * 3 / 4 - 30;
	    money[k].y = d.height - 50 - (k-8) * 10;
	}
    }


    public Dimension getPreferredSize(){
	return new Dimension(400,330);
    }

    public Dimension getMinimumSize(){
	return new Dimension(400,330);
    }

    public synchronized void mousePressed(MouseEvent e){
	int x = e.getX();
	int y = e.getY();
        int best = Integer.MAX_VALUE;
        for (int i = 0 ; i < 16 ; i++) {
	    if ( money[i] == null ){
		continue;
	    }
            Money n = money[i];
            int dist = (n.x - x) * (n.x - x) + (n.y - y) * (n.y - y);
            if (dist < best) {
                itsme = n;
                best = dist;
            }
        }
        itsme.x = x;
        itsme.y = y;
        repaint();
    }
    public synchronized void mouseClicked(MouseEvent e){}
    public synchronized void mouseEntered(MouseEvent e){}
    public synchronized void mouseExited(MouseEvent e){}
    public synchronized void mouseReleased(MouseEvent e){
        itsme.x = e.getX();
        itsme.y = e.getY();
        itsme = null;
        repaint();
    }


    public synchronized void mouseDragged(MouseEvent e){
        itsme.x = e.getX();
        itsme.y = e.getY();
        repaint();
    }
    public synchronized void mouseMoved(MouseEvent e){
    }

    public synchronized void update(Graphics g){
	paint(g);
    }

    public synchronized void paint(Graphics g){
        if ((offscreen == null) || (d.width != offscreensize.width)
	    			|| (d.height != offscreensize.height)) {
            offscreen = createImage(d.width, d.height);
            offscreensize = d;
            offgraphics = offscreen.getGraphics();
            offgraphics.setFont(getFont());
        }
	offgraphics.setColor(getBackground());
	offgraphics.fillRect(0,0,d.width,d.height);

	tp.mframe.mp.setcurrent(mcell_y,mcell_x);

	paintAlternatives(offgraphics);
	paintMoney(offgraphics);
	paintMessage(offgraphics);
	paintFactor(offgraphics);
	setx();
	g.drawImage(offscreen,0,0,this);
    }

    public synchronized void paintFactor(Graphics g){
	//Dimension d = size();

	FontMetrics fm = g.getFontMetrics();

	int textwidth = fm.stringWidth("Comparison with respect to");
	g.setColor(Color.black);
	g.drawString("Comparison with respect to",d.width/2-textwidth/2,80);

	textwidth = fm.stringWidth(factor);
	g.setColor(Color.red);
	g.drawString(factor,d.width/2-textwidth/2,100);
    }

    public synchronized void paintMessage(Graphics g){
	String message = new String();
	int left = 0;
	for(int i = 0 ; i < 16 ; i++){
	    if ( money[i].x < getSize().width/2 ){
		left++;
	    }
	}

	if ( left == 8 ){
	    message = name1 + " and " + name2 + " are " + scale[0]
		 + " important or prefered ";
	    tp.mframe.mp.current.value = 1;
	    tp.mframe.mp.cocurrent.value = 1;
	} else if ( left > 8 ) {
	    message = name1 + " is " + scale[left-8] +
		" more important or prefered than " + name2;
	    tp.mframe.mp.current.value = (double)left -8 +1;
	    tp.mframe.mp.cocurrent.value = 1/((double)left -8 +1);
	} else if ( left < 8 ) {
	    message = name2 + " is " + scale[8-left] +
		" more important or prefered than " + name1;
	    tp.mframe.mp.current.value = 1/(8 -(double)left  +1);
	    tp.mframe.mp.cocurrent.value = 8 -(double)left +1;
	}
	g.setColor(Color.black);
	g.drawString(message,x,30);
	FontMetrics fm = g.getFontMetrics();
	textwidth = fm.stringWidth(message + "");
    }

    public synchronized void setx(){
	x = x - 10;
	if ( x < -(textwidth)){
	    x = getSize().width;
	}
    }

    public synchronized void paintAlternatives(Graphics g){
        g.setColor(Color.white);
        g.fillOval(0            ,d.height-50-d.height/10,
                   d.width/2,d.height/5);
        g.fillOval(d.width/2    ,d.height-50-d.height/10,
                   d.width/2,d.height/5);
	g.setColor(Color.black);
	g.drawOval(0            ,d.height-50-d.height/10,
                   d.width/2,d.height/5);
        g.drawOval(d.width/2    ,d.height-50-d.height/10,
                   d.width/2,d.height/5);
        g.setColor(Color.red);
        g.setFont(new Font("TimesRoman",Font.BOLD,18));
	g.drawString(name1,30           ,d.height-50);
	g.drawString(name2,d.width/2+30 ,d.height-50);
    }

    public synchronized void paintMoney(Graphics g){
	for(int i = 0 ; i < 16; i++){
	    int x = money[i].x;
	    int y = money[i].y;
	    g.drawImage(money_image,x,y,this);
	}
    }

    public void start() {
	foliage = new Thread(this);
	foliage.start();
    }

    public void stop() {
	foliage.stop();
	foliage = null;
    }

    public void run(){
	Random rand = new Random();
	try {money_tracker.waitForID(1);} catch (InterruptedException e){};
	while(foliage != null){
	    //System.out.println("Hello");
	    for(int i = 0; i < 16 ;i++){
		if(money[i].y < d.height-50-d.height/10){
		    int xx = rand.nextInt()/1000000000;
		    money[i].y = money[i].y + 3 + xx;
		    money[i].x = money[i].x + xx;
		}
	    }
	    try{
		Thread.sleep(100);
	    }catch (InterruptedException e){
		break;
	    }
	    if(active == true){
		repaint();
	    }
	}
    }
}
