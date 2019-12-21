import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;

 class TreeNode {   
    int value; 
    char c; 
    TreeNode left; 
    TreeNode right; 
    
    
    public boolean isLeafNode() {
    	if( this.left == null )
    		return true;
    	else
    		return false;
    }
} 
  
 class MyComparator implements Comparator<TreeNode> { 
    public int compare(TreeNode n1, TreeNode n2) 
    { 
        return n1.value - n2.value; 
    } 
} 

public class huffmanTree {
	private int[] arr;
	private PriorityQueue<TreeNode> heap;
	private HashMap<Character,String> encodingMap;
	private HashMap<String, Character> flippedEncodingMap;
	private TreeNode root;
	
	public huffmanTree(int[] arr)
	{
		this.arr=arr;
		heap   = new PriorityQueue<TreeNode>(arr.length, new MyComparator()); 
		encodingMap = new HashMap<>();
		flippedEncodingMap = new HashMap<>();
		root=null;
		
	}
	
	public huffmanTree()
	{
		flippedEncodingMap = new HashMap<>();
		root=null;
	}
	
	
	public HashMap<String, Character> getFlippedEncodingMap() {
		return flippedEncodingMap;
	}



	public void setFlippedEncodingMap(HashMap<String, Character> flippedEncodingMap) {
		this.flippedEncodingMap = flippedEncodingMap;
	}



	private void addLeafNodes()
	{
		int n = arr.length;
		
		 for (int i = 0; i < n; i++) { 
	            
	            if(arr[i]==0) continue;
	            TreeNode node = new TreeNode(); //changed the order only
	            
	            node.c = (char)i; 
	            node.value = arr[i]; 
	  
	            node.left = null; 
	            node.right = null; 
	  
	            heap.add(node); 
	        } 
	}
	
	public HashMap<Character,String> buildTree() {
		addLeafNodes();
		 
		if(heap.size()==1)
		{   TreeNode dummy = new TreeNode();
			dummy.c='-';
			dummy.value=0;
            TreeNode r = new TreeNode(); 
            r.c='-';
            r.left=heap.peek();
            r.right=dummy;
            root=r;
		}
        //At the end we will be left with just one node 
        while (heap.size() > 1) { 
        	//At each iteration we extract the minimum two nodes 
        	//add the two values and put it in a new node 
            TreeNode n1 = heap.peek(); 
            heap.poll(); 
  
            TreeNode n2 = heap.peek(); 
            heap.poll(); 
  
            TreeNode r = new TreeNode(); 

            r.value = n1.value + n2.value; 
            r.c = '-'; 
            r.left = n1; 
            r.right = n2; 
            root = r; 
            
            heap.add(r);
        } 
        
          buildEncodingMap(root,"");
          return this.encodingMap;
	}
	
	 public void buildEncodingMap(TreeNode root, String s) 
	    { 
	        if (root.left== null && root.right == null) { 
	        	encodingMap.put(root.c, s);
	            return; 
	        } 
	        if(root.left!=null)
	        buildEncodingMap(root.left,s+"0");
	        if(root.right!=null)
	        buildEncodingMap(root.right,s+"1"); 
	    } 
	 
	 public void FlipEncodingMap(TreeNode root, String s) 
	    { 
	        if (root.left== null && root.right == null) { 
	        	flippedEncodingMap.put(s, root.c);
	            return; 
	        } 

	        FlipEncodingMap(root.left,s+"0");
	        FlipEncodingMap(root.right,s+"1"); 
	    }
	  
	 public TreeNode getRoot() {
			return root;
		}
	 
	public static void main(String[] args) {
		int [] arr = new int [256];
		Random randomGenerator = new Random();
		HashMap<Character,String> map = new HashMap<>();
		for(int i=97;i<=127;i++)
		{
			int randomInt = randomGenerator.nextInt(50);
			arr[i] = randomInt;
		}
		
		huffmanTree h = new huffmanTree(arr);
		map = h.buildTree();
		System.out.println(Arrays.asList(map)); 
	}

	

	
}