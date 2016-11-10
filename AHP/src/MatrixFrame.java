
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MatrixFrame extends Frame implements ActionListener{
    int matrix_size;
    TreePanel tp;
    MatrixPanel mp;
    Button ok;
    Button cancel;

    MatrixFrame(TreePanel p){
	String title = new String();

	tp = p;
	setTitle(tp.itsme.name);

	matrix_size = tp.itsme.child_ids.size();
	mp = new MatrixPanel(p);

	Panel command = new Panel();
	command.setLayout(new GridLayout(1,2));
	ok = new Button("OK");
	ok.addActionListener(this);
	command.add(ok);
	cancel = new Button("CANCEL");
	cancel.addActionListener(this);
	command.add(cancel);

	setLayout(new BorderLayout());
	add("Center",mp);
	//add("South",command);

	//resize(matrix_size*100+80,matrix_size*30+200);
	pack();
    }

    public Dimension getPreferredSize(){
	return new Dimension((matrix_size+2)*mp.cell_width+80+10,
			     (matrix_size+1)*30+150);
    }

    public Dimension getMinimumSize(){
	return new Dimension((matrix_size+2)*mp.cell_width+80+10,
			     (matrix_size+1)*30+150);
    }

    public void actionPerformed(ActionEvent event){
	Object source = event.getSource();
	if(source == cancel){
	    setVisible(false);
	    dispose();
	}
    }
}

class MatrixPanel extends Panel implements MouseListener {
    int matrix_size;
    TreePanel tp;
    Image offscreen;
    Dimension offscreensize;
    Graphics offgraphics;
    double matrix[][];
    /*
      EXPLAIN for matrix -> this.matrix[y][x]
      matrix[0][0], matrix[0][1] , ... , matrix[0][n]
      matrix[1][0], matrix[1][1] , ... , matrix[1][n]
          :             :                     :
      matrix[n][0], matrix[n][1] , ... , matrix[n][n]
    */
    double weight[];
    double calc_result[];
    double rmax;
    MatrixCell mcell[][];
    /*
      (NOTE the diffrence between mcell and matrix)
      EXPLAIN for mcell -> mcell[x][y]
      mcell[0][0] , mcell[1][0] , ... , mcell[n][0]
      mcell[0][1] , mcell[1][1] , ... , mcell[n][1]
           :            :                   :
      mcell[0][n] , mcell[1][n] , ... , mcell[n][n]
    */
    MatrixCell current = null;
    MatrixCell cocurrent = null;
    MatrixCell result[];
    String names[];
    int cell_width = 0;
    // Random Inconsistency
    double ri[] = new double[16];
    // consistency index
    double ci;
    // consistency ratio
    double cr;

    MatrixPanel(TreePanel p){
	tp = p;

	ri[1]  = 0.00; ri[2]  = 0.00; ri[3]  = 0.58; ri[4]  = 0.90;
	ri[5]  = 1.12; ri[6]  = 1.24; ri[7]  = 1.32; ri[8]  = 1.41;
	ri[9]  = 1.45; ri[10] = 1.49; ri[11] = 1.51; ri[12] = 1.53;
	ri[13] = 1.56; ri[14] = 1.57; ri[15] = 1.59;

	matrix_size = tp.itsme.child_ids.size();
	matrix = new double[matrix_size][matrix_size];
	result = new MatrixCell[matrix_size];
	mcell = new MatrixCell[matrix_size][matrix_size];
	names = new String[matrix_size];
	for(int i=0; i < matrix_size ; i++){
	    for(int j=0; j < matrix_size ; j++){
		int c_id1 = Integer.parseInt(tp.itsme.child_ids.elementAt(i).toString());
		int c_id2 = Integer.parseInt(tp.itsme.child_ids.elementAt(j).toString());
		//String name1 = tp.node[c_id1].name;
		//String name2 = tp.node[c_id2].name;
		mcell[i][j] = new MatrixCell(c_id1,c_id2);
		//mcell[i][j].value = 1;
		mcell[i][j].value = tp.itsme.matrix[i][j];
	    }
	}
	Calculate();
	for(int i = 0 ; i < matrix_size ; i++){
	    int c_id = Integer.parseInt(tp.itsme.child_ids.elementAt(i).toString());
	    //String name = tp.node[c_id].name;
	    result[i] = new MatrixCell(c_id);
	    result[i].value = weight[i];
	}
	setFont(new Font("TimesRoman",Font.BOLD,12));
	FontMetrics fm = getFontMetrics(getFont());
	for(int k = 0 ; k < tp.itsme.child_ids.size() ; k++){
	    int c_id =
		Integer.parseInt(tp.itsme.child_ids.elementAt(k).toString());
	    names[k] = tp.node[c_id].name;
	    if ( cell_width < fm.stringWidth(names[k])){
		cell_width = fm.stringWidth(names[k]);
	    }
	}
	cell_width = cell_width + 10;
	addMouseListener(this);
    }

    public void storeMatrix(){
	for(int i = 0 ; i < matrix_size ; i++){
	    for(int j = 0 ; j < matrix_size ; j++){
		tp.itsme.matrix[i][j] = mcell[i][j].value;
	    }
	}
    }

    public void Calculate(){
	calc_result = new double[matrix_size+1];
	weight = new double[matrix_size];
	for(int i = 0 ; i < matrix_size ; i++){
	    for(int j = 0 ; j < matrix_size ; j++){
		matrix[i][j] = mcell[i][j].value;
	    }
	}
	calc_result = PowerMethod.power(matrix);
	for(int i = 0 ; i < matrix_size ; i++ ){
	    weight[i] = calc_result[i];
	}
	rmax = calc_result[matrix_size];
	if(matrix_size < 3){
	    ci = 0 ;
	    cr = 0 ;
	} else {
	    ci = (rmax - (double)matrix_size)/((double)matrix_size - 1);
	    cr = ci / ri[matrix_size];
	}

	for(int i = 0 ; i < tp.itsme.child_ids.size(); i++){
	    int c_id = Integer.parseInt(tp.itsme.child_ids.elementAt(i).toString());
	    tp.itsme.addChildValue(c_id,weight[i]);
	}
    }

    public void update(Graphics g){
	paint(g);
    }

    public Dimension getPreferredSize(){
	return new Dimension((matrix_size+2)*cell_width+80+10,
			     (matrix_size+1)*30+90);
    }

    public Dimension getMinimumSize(){
	return new Dimension((matrix_size+2)*cell_width+80+10,
			     (matrix_size+1)*30+90);
    }

    public void paint(Graphics g){
	Dimension d  = getSize();
        if ((offscreen == null) || (d.width != offscreensize.width)
	    			|| (d.height != offscreensize.height)) {
            offscreen = createImage(d.width, d.height);
            offscreensize = d;
            offgraphics = offscreen.getGraphics();
            offgraphics.setFont(getFont());
        }
	offgraphics.setColor(getBackground());
	offgraphics.fillRect(0,0,d.width,d.height);

	Calculate();
	paintMatrix(offgraphics);
	g.drawImage(offscreen,0,0,null);
    }

    public void paintMatrix(Graphics g){
	Dimension d = getSize();
	Double n;
	String n_value;
	int n_length;

	g.setColor(Color.white);
	g.fillRect(29,29,
		   cell_width*(matrix_size+1)+1,
		   30*(matrix_size+1)+1);
	g.fillRect(cell_width*(matrix_size+1)+30+9,29,
		   cell_width+2,30*(matrix_size+1)+2);
	g.fillRect(29,30+30*(matrix_size+1)+10-1,
		   cell_width*2+2,30+2);
	g.fillRect(29+cell_width*2+10,30+30*(matrix_size+1)+10-1,
		   cell_width*2+2,30+2);

	g.setColor(Color.black);
	g.drawRect(29,29,
		   cell_width*(matrix_size+1)+1,
		   30*(matrix_size+1)+1);
	g.drawRect(cell_width*(matrix_size+1)+30+9,29,
		   cell_width+2,30*(matrix_size+1)+2);
	for(int i = 0 ; i < matrix_size+1 ; i++){
	    g.drawRect(i*cell_width+30,30,cell_width-1,29);
	    g.drawRect(30,i*30+30,cell_width-1,29);
	}

	FontMetrics fm = getFontMetrics(getFont());
	g.drawRect(29,30+30*(matrix_size+1)+10-1,
		   cell_width*2+2,30+2);
	g.drawRect(29+cell_width*2+10,30+30*(matrix_size+1)+10-1,
		   cell_width*2+2,30+2);
	for(int i = 0 ; i < 2 ; i++){
	    g.drawRect(30+cell_width*i,30+30*(matrix_size+1)+10,
		       cell_width,30);
	    g.drawRect(30+cell_width*2+10+cell_width*i,
		       30+30*(matrix_size+1)+10,
		       cell_width,30);
	}

	n_length = fm.stringWidth("C.I.");
	g.drawString("C.I.",
		     30+cell_width/2-n_length/2,
		     30+30*(matrix_size+1)+10+20);

	n = new Double(ci);
	n_value = n.toString();
	if(n_value.length()>6){
	    n_value = n_value.substring(0,5);
	}
	n_length = fm.stringWidth(n_value);
	g.drawString(n_value,
		     30+cell_width+cell_width/2-n_length/2,
		     30+30*(matrix_size+1)+10+20);
	n = null;

	n_length = fm.stringWidth("C.R.");
	g.drawString("C.R.",
		     30+cell_width*2+10+cell_width/2-n_length/2,
		     30+30*(matrix_size+1)+10+20);

	n = new Double(cr);
	n_value = n.toString();
	if(n_value.length()>6){
	    n_value = n_value.substring(0,5);
	}
	n_length = fm.stringWidth(n_value);
	g.drawString(n_value,
		     30+cell_width*3+10+cell_width/2-n_length/2,
		     30+30*(matrix_size+1)+10+20);
	n = null;


	for(int i = 0 ; i < matrix_size ; i++){
	    for(int j = 0 ; j< matrix_size ; j++){
		if(mcell[i][j].active == true){
		    g.setColor(Color.red);
		    g.drawRect((j+1)*cell_width+30,
			       (i+1)*30+30,
			       cell_width-1,29);
		    g.setColor(Color.black);
		} else {
		    g.drawRect((j+1)*cell_width+30,
			       (i+1)*30+30,
			       cell_width-1,29);
		}
		n = new Double(mcell[i][j].value);
		n_value = n.toString();
		if(n_value.length()>6){
		    n_value = n_value.substring(0,5);
		}
		n_length = fm.stringWidth(n_value);
		g.drawString(n_value,
			     (j+1)*cell_width+30+cell_width/2-n_length/2,
			     (i+1)*30+30+15);
		n = null;
	    }
	}

	// Weight Matrix
	n_length = fm.stringWidth("Weight");
	g.drawString("Weight",
		     (matrix_size+1)*cell_width+30+10+cell_width/2-n_length/2,
		     30+20);
	for(int i = 0 ; i < matrix_size+1 ; i++){
	    g.drawRect(cell_width*(matrix_size+1)+30+10,30+i*30,
		       cell_width,30);
	}
	for(int i = 0 ; i < matrix_size ; i++){
	    n = new Double(weight[i]);
	    n_value = n.toString();
	    if(n_value.length()>6){
		n_value = n_value.substring(0,5);
	    }
	    n_length = fm.stringWidth(n_value);
	    g.drawString(
		n_value,
		cell_width*(matrix_size+1)+30+10+cell_width/2-n_length/2,
		(i+1)*30+30+15);
	    n = null;
	}

	// Line Name
	for(int k = 0 ; k < matrix_size ; k++){
	    g.drawString(names[k],35,k*30+60+20);
	}

	// Column Name
	for(int k = 0 ; k < matrix_size ; k++){
	    g.drawString(names[k],k*cell_width+cell_width+35,50);
	}
    }

    public void setcurrent(int x, int y){
	current = mcell[y][x];
	cocurrent = mcell[x][y];

	for(int i = 0 ; i < matrix_size ; i++){
	    for(int j = 0 ; j < matrix_size ; j++){
		mcell[i][j].active = false;
	    }
	}
	current.active = true;
	cocurrent.active = true;
	repaint();
    }

    public synchronized void mouseClicked(MouseEvent e){}
    public synchronized void mouseEntered(MouseEvent e){}
    public synchronized void mouseExited(MouseEvent e){}
    public synchronized void mouseReleased(MouseEvent e){}
    public synchronized void mousePressed(MouseEvent e){
	int mouseX = e.getX();
	int mouseY = e.getY();
	int x,y;

	x = (mouseX - 30 - cell_width) / cell_width;
	y = (mouseY - 30 - 30) / 30;

	if( mouseX < 30+cell_width || mouseY < 30+30   || x == y ||
	    x >= matrix_size || y >= matrix_size ||
	    x < 0            || y < 0) {
	} else {
	    /* Get Upper Half */
	    if(y > x){
		int tmp;
		tmp = x;
		x = y;
		y = tmp;
	    }
	    setcurrent(x,y);

	    /* Go to the Card */
	    //System.out.println(row+","+col);
	    int now_card = 0;
	    for(int i = 0 ; i < y ; i++ ){
		for(int j = 1 + i ; j < matrix_size ; j++){
		    now_card++;
		}
	    }
	    now_card = now_card + (x-y);
	    now_card--;

	    Integer n = new Integer(now_card);
	    //System.out.println(n.toString());
	    tp.pcframe.gotoCard(n.toString());
	}
    }
}

class MatrixCell {
    double value;
    boolean active;
    int id1;
    int id2;

    MatrixCell(int id1, int id2){
	active = false;
	this.id1 = id1;
	this.id2 = id2;
    }

    //Constructer for results
    MatrixCell(int id){
	active = false;
	this.id1 = id;
    }
}


