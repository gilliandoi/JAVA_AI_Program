
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;

public class Node implements Cloneable,Serializable{
    int x,y;

    String name;
    int id;
    int parent_id;
    Vector child_ids;
    double matrix[][];

    // If this node is an ALTERNATIVE, this node has a value.
    double value = 0;

    // Selected or Disselected
    boolean active;

    Hashtable child_value;

    Node(){}

    public void initMatrix(){
	int matrix_size = child_ids.size();
	matrix = new double[matrix_size][matrix_size];
	for(int i =0 ; i < matrix_size ; i++){
	    for(int j =0 ; j < matrix_size ; j++){
		matrix[i][j] = 1.0;
	    }
	}
    }

    public void initChildValue(){
	child_value = new Hashtable();
	if(child_ids.size() != 0){
	    double value = 1.0/child_ids.size();
	    Double v = new Double(value);
	    for(int i = 0 ; i < child_ids.size() ; i++){
		Integer c_id = (Integer)child_ids.elementAt(i);
		child_value.put(c_id,v);
		//int c_id = Integer.parseInt(child_ids.elementAt(i).toString());
		//child_value.put(new Integer(c_id),v);
	    }
	}
    }

    public void addChildValue(int child_node_id, double value){
	Integer i = new Integer(child_node_id);
	Double v = new Double(value);
	child_value.put(i,v);
    }

    public void removeChildValue(int child_node_id){
	Integer i = new Integer(child_node_id);
	child_value.remove(i);
    }
}
