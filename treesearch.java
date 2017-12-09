import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.ParseException;

@SuppressWarnings("hiding")
//This is main tree class with root and leaf count and index count defined seperately although it is same when a degree value is specified//
//Key values can be double while Value is a generic definition which accepts string//
public class treesearch<Double extends Comparable<? super Double>, Value>
{
	//Root node which could be leaf node or IndexNode node but it is never a NULL value//
    private Mainnode root;
   
    //Maximum Number of keys in leaf node which can be different from index node//
    private final int M;
    
    //Maximum Number of keys in IndexNode node which can be different from leaf node and is N+1 for every node and 2 for root node//
    private final int N;

    //Definition of treesearch tree with degree n//
    public treesearch(int degree) {
	this(degree, degree);
    }

    //treesearch tree when M and N are different and indexed with different values//
    public treesearch(int leafcount, int indexcount) {
        M = leafcount;
        N = indexcount;
        root = new leaf();
    }

    //Insert function for treesearch tree//
    public void insert(Double key, Value value) {
    	Splitvalue splitvalue = root.insert(key, value);
        if (splitvalue != null) {
        	
	    //While the root is split into two parts, new root is created pointing to the split nodes//
            IndexNode newroot = new IndexNode();
            newroot.lengthofnode = 1;
            newroot.keys[0] = splitvalue.key;
            newroot.child[0] = splitvalue.leftnode;
            newroot.child[1] = splitvalue.rightnode;
            root = newroot;
        }
    }

    //Function to find all key pairs with a given key -- returns null if no key pair exists//
    public ArrayList<String> search(Double key) {
    	ArrayList<String> result = new ArrayList<String>();
        Mainnode node = root;
        
        //Traversing till we find leaf node//
        while (node instanceof treesearch.IndexNode) { 
	    @SuppressWarnings("unchecked")
		IndexNode inner = (IndexNode) node;
            int i = inner.getlocation(key);
            node = inner.child[i];
        }
        
        //NNow traversing through leaf nodes using next and previous commands for only given key value  as they are connected through doubly linked list//
        @SuppressWarnings("unchecked")
		leaf leaf = (leaf) node;        
        int idx = 0;
        int previdx = -1;
        
        //To traverse back of the list to find if the element  exists in the previous leaf nodes//
        leaf leafprev = leaf.prev;
        while(leafprev!=null && leafprev.getlocation(key)!=leafprev.lengthofnode)
        {
       	idx = leafprev.getlocation(key);
       	while(idx<leafprev.lengthofnode && leafprev.keys[idx].compareTo(key)==0)     
       	{
       	           
     		if(previdx == -1)
     		{
     		result.add("Value" + leafprev.values[idx]);
     		
     		}
     		else
     		{
     			result.add(", Value" + leafprev.values[idx]);
     			
     		}
     		previdx = idx;
     	       
     idx++;
       	}
       	leafprev = leafprev.prev;
        }
        
     
        //Traversing forward//
        while(leaf!=null && leaf.getlocation(key)!=leaf.lengthofnode)
        {
        	idx = leaf.getlocation(key);
        	 while(idx<leaf.lengthofnode && leaf.keys[idx].compareTo(key)==0)
             {
             	
             	//Adding values of key pairs that match given key to the splitvalue list//             
             
             		if(previdx == -1)
             		{
             		result.add("Value" + leaf.values[idx]);
             		
             		}
             		else
             		{
             			result.add(", Value" + leaf.values[idx]);
             			
             		}
             		previdx = idx;
             	       
             idx++;
             }
        	 leaf = leaf.next;
        	
        }
        
       
       
        return result;
    }
    public ArrayList<String> search(Double key1, Double key2)
    {
    	ArrayList<String> splitvalue = new ArrayList<String>();
    	 Mainnode node = root;
    	 int previdx = -1;
    	 
    	     //Traversing till we find leaf node//
         while (node instanceof treesearch.IndexNode) 
         {
 	    @SuppressWarnings("unchecked")
 		IndexNode inner = (IndexNode) node;
             int idx = inner.getlocation(key1);
             node = inner.child[idx];
         }

       //Now traversing through leaf nodes using next commands  as they are connected through doubly linked list//
         @SuppressWarnings("unchecked")
 		leaf leaf = (leaf) node;        
         leaf leaf2 =leaf;
         int idx = leaf2.getlocation(key1);
        int k = idx;
        leaf leafprev = leaf.prev;
        while(leafprev!=null && leafprev.getlocation(key1)!=leafprev.lengthofnode)
        {
        	int p = leafprev.getlocation(key1);
        	while(p < leafprev.lengthofnode && leafprev.keys[p].compareTo(key1)==0)
        	{
        		
        	  		 if(previdx == -1)
        			 {
        				 splitvalue.add("(" + leafprev.keys[p]+",Value"+leafprev.values[p]+")"); 
        			 }
        			 else
        			 {
        				 splitvalue.add(", (" + leafprev.keys[p]+",Value"+leafprev.values[p]+")");
        			 }       			 
        			 previdx = p;           		
        			
        				
        		p++;
      
        	}
        	        	       	
        	leafprev = leafprev.prev;
        }
        
         while(leaf2!=null)
         {      	 
         //Adding values of key pairs that are in the range of given key to the splitvalue list//
        	 while(k<leaf2.lengthofnode && leaf2.keys[k].compareTo(key2)<=0) {
        		 if(leaf2.keys[k].compareTo(key1)>=0)
        		 {
        			 if(previdx == -1)
        			 {
        				 splitvalue.add("(" + leaf2.keys[k]+",Value"+leaf2.values[k]+")"); 
        			 }
        			 else
        			 {
        				 splitvalue.add(", (" + leaf2.keys[k]+",Value"+leaf2.values[k]+")");
        			 }       			 
        			 previdx = k;
        		 }
        		 k++;
        	 }
        	 leaf2=leaf2.next;
        	 k=0;
         }                
         return splitvalue;
    }

    //Definining Main class Node that includes leaf node and index node//
    abstract class Mainnode {
	protected int lengthofnode; 
	//Array of keys which is either double/float//
	protected Double[] keys;
	abstract public Splitvalue insert(Double key, Value value);
	abstract public int getlocation(Double key);
	
	
    }

    //Defining Leaf Node class where leaves are connected as doubly linked list//
    @SuppressWarnings("unchecked")
	class leaf extends Mainnode {
    	//Previous and next pointers initialized for doubly linked list//
    	leaf next = null;
    	leaf prev = null;
	final Value[] values = (Value[]) new Object[M-1];
	{ keys = (Double[]) new Comparable[M-1]; }

	//Function to get location in a leaf node with specified key//
	public int getlocation(Double keyk) {
	    for (int i = 0; i < lengthofnode; i++) {
		if (keys[i].compareTo(keyk) >= 0) {			
		    return i;
		}
	    }
	    return lengthofnode;
	}

	//Split insert function that returns split to main function till we reach root node where parent is null for root node//
	public Splitvalue insert(Double key, Value value) {
	    int i = getlocation(key);
	    //Leafnode is full, hence splitting//
	    if (this.lengthofnode == M-1) 
	    { 
		int mid = (M)/2;
		int siblinglength = this.lengthofnode - mid;
		leaf sibling = new leaf();
		
		//for doubly linked list insertion for leaf nodes//
		sibling.next=this.next;
		if(this.next!=null)
		{
		this.next.prev = sibling;
		}
		this.next=sibling;
		sibling.prev = this;
		sibling.lengthofnode = siblinglength;
		
		System.arraycopy(this.keys, mid, sibling.keys, 0, siblinglength);
		System.arraycopy(this.values, mid, sibling.values, 0, siblinglength);
		this.lengthofnode = mid;
		if (i < mid) {
		    // Insertion in left sibling
		    this.Insertwhennotfull(key, value, i);
		} else {
		    // Insertion in right sibling
		    sibling.Insertwhennotfull(key, value, i-mid);
		}
		// Return split splitvalue to parent to further call the split function verifying whether limit is reached and parent is null - Split is stoppped if parent is null//
		Splitvalue splitvalue = new Splitvalue(sibling.keys[0],this, sibling);
		return splitvalue;
	    } 
	    //Node is not full, hence calling normal insert function//
	    else 
	    {
		this.Insertwhennotfull(key, value, i);
		return null;
	    }
	}

	//Function to call when Node is not full//
	private void Insertwhennotfull(Double key, Value value, int idx) 
	{
		System.arraycopy(keys, idx, keys, idx+1, lengthofnode-idx);
		System.arraycopy(values, idx, values, idx+1, lengthofnode-idx);
		//Inserting key value pair in new array//
		keys[idx] = key;
		values[idx] = value;
		lengthofnode++;	    
	}	
    }

    //Defnining index node where each node in same level is not connected//
    @SuppressWarnings("unchecked")
	class IndexNode extends Mainnode {
	final Mainnode[] child = new treesearch.Mainnode[N];
	{ keys = (Double[]) new Comparable[N-1]; }

	//Function to get location in a index node with specified key//
	public int getlocation(Double keyi)
	{
		//This is linear search but binary search can be implemented for larger N//
	    for (int i = 0; i < lengthofnode; i++) 
	    {
		if (keys[i].compareTo(keyi) > 0) {
		    return i;
		}
	    }
	    return lengthofnode;
	}

	//Split of index node while number of elemnts reach maximum number//
	public Splitvalue insert(Double key, Value value) 
	{
		//Split when node is full//
	    if (this.lengthofnode == N-1) {
		int mid = (N)/2;
		int siblinglength = this.lengthofnode - mid;
		IndexNode siblingnode = new IndexNode();
		siblingnode.lengthofnode = siblinglength;
		System.arraycopy(this.keys, mid, siblingnode.keys, 0, siblinglength);
		System.arraycopy(this.child, mid, siblingnode.child, 0, siblinglength+1);
		this.lengthofnode = mid-1;

		// Returning split to parent to verify whether further split has to be done or not//
		Splitvalue splitvalue = new Splitvalue(this.keys[mid-1],this,siblingnode);

		//Insert in left or right sibling//
		if (key.compareTo((Double) splitvalue.key) < 0) {
		    this.Insertwhennotfull(key, value);
		} else {
			siblingnode.Insertwhennotfull(key, value);
		}
		return splitvalue;

	    } 
	    //If Node is not full, calling normal insert function//
	    else {
		this.Insertwhennotfull(key, value);
		return null;
	    }
	}

	private void Insertwhennotfull(Double key, Value value) {
		//Retrieves index for given key//
	    int i = getlocation(key);
	    Splitvalue splitvalue = child[i].insert(key, value);

	    if (splitvalue != null) {
		if (i == lengthofnode) {
		    // Insertion at the rightmost key
		    keys[i] = splitvalue.key;
		    child[i] = splitvalue.leftnode;
		    child[i+1] = splitvalue.rightnode;
		    lengthofnode++;
		} else {
		    // Insertion not at the rightmost key
		    //shift i>idx to the right
		    System.arraycopy(keys, i, keys, i+1, lengthofnode-i);
		    System.arraycopy(child, i, child, i+1, lengthofnode-i+1);

		    child[i] = splitvalue.leftnode;
		    child[i+1] = splitvalue.rightnode;
		    keys[i] = splitvalue.key;
		    lengthofnode++;
		}
	    } // else the current node is not affected
	}

	
	
    }

    //Defining split class that defines the key and left and right nodes//
    class Splitvalue {
	public final Double key;
	public final Mainnode leftnode;
	public final Mainnode rightnode;

	public Splitvalue(Double k, Mainnode left, Mainnode right) {
		leftnode = left;
	    rightnode = right;
	    key = k;	    
	}
    }
    
    //Main function to call the functions//
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws IOException {
    	
    	//Default System input if reader fails//
    	BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    	String path = args[0];
    	
    	//Function to read input file//
    	try {
            in = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        } catch (FileNotFoundException e) {
            System.err.println("Error: specified file not found (defaulting to standard input)");
        }
    	
    	//Output file to write//
    	BufferedWriter output = new BufferedWriter( new FileWriter(new File("output_file.txt")) ); 
 
    	//Reading degree value//
    	int degree = Integer.parseInt(in.readLine().trim());
    
    	
    	//Initializing the tree with given degree//
    	treesearch tree = new treesearch(degree);   
    	String s = null;
    	
    	//Traversing through the file till end to find other functions//
    	while((s = in.readLine())!=null)
    	{	
	  String[] k = null;
	  double key = 0.0;
	  double key1 = 0.0;
	  double key2 = 0.0;
	  DecimalFormat df = new DecimalFormat("#");
	  
	  //If Insert command is given ----- Insert function//
	  if(s.contains("Insert"))
	  {
		 s = s.replace("Insert","" );
		  s = s.replace("(","");
		  s= s.replace(")", "");
		  s=s.trim();
		  
		 k = s.split(",");
		 k[1]= k[1].replaceAll("Value", "");
		 try {
		    key = df.parse(k[0]).doubleValue();
		    
		 } catch (ParseException e) {
		    System.out.println(k[0] + " is not a valid number.");
		 }
		 tree.insert(key, k[1]);
		 
	  }
	  
	  //If Search Command is given//
	  else if(s.contains("Search"))
	  {
		  s = s.replace("Search","" );
		  s = s.replace("(","");
		  s= s.replace(")", "");
		  s=s.trim();
		  
		  //If search is within a given range --- Search in given range//
		  if(s.contains(","))
		  {  
			 k = s.split(","); 
			 try
			 {
				 key1 = df.parse(k[0]).doubleValue();
				 key2 = df.parse(k[1]).doubleValue();
			 }catch (ParseException e)
			 {
				 System.out.println(k[0] + " is not a valid number."); 
			 }
            ArrayList<String> splitvalue = tree.search(key1, key2);
            for(int i=0;i<splitvalue.size();i++)
            {
            	output.write(splitvalue.get(i));
            }
            if(splitvalue.size()==0)
				output.write("Null");
            output.write("\n");
		  }
		 
		  //If search is for a given key value -- Search for given key//
		  else
		  {
			  try
				 {
					 key1 = df.parse(s).doubleValue();
				 }catch (ParseException e)
				 {
					 System.out.println(s + " is not a valid number."); 
				 } 
			  ArrayList<String> splitvalue = tree.search(key1);
			  
			  
			for(int i=0;i<splitvalue.size();i++)
	            {
	            	output.write(splitvalue.get(i));
	            }
			if(splitvalue.size()==0)
				output.write("Null");
			output.write("\n");	
		  }	  
	  }	
  }
    output.close();
    }
}