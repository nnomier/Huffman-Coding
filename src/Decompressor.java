import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Decompressor {

	TreeNode root =null;
	static BitReader reader = null;
	
	
	public Decompressor() throws IOException
	{
		reader = new BitReader("compressedFile.txt");
	
	}
	
	public char createAsciiCharacter() throws IOException
	{
		int currentB = 0;
		for(int i=0; i<8; i++)
		{
			if( reader.readBit() == 1)
			{
				currentB = currentB | (128 >> i);
			}
		}
		
		return (char)currentB;
	}
	
	public  TreeNode reconstructHuffmanTree() throws IOException {
		 if (reader.readBit() == 1)
		    {
			 	TreeNode t = new TreeNode();
			 	t.c= createAsciiCharacter();
			 			//(char) ((char) reader.readByte() & 0xFF);
			 	System.out.println("TREE CHARACRER " + t.c + " " + Integer.toBinaryString(t.c));
			 	t.right=null;
			 	t.left=null;
			 	return t;
		    }
		    else
		    {
		        TreeNode leftChild = reconstructHuffmanTree();
		        TreeNode rightChild = reconstructHuffmanTree();
		    	TreeNode t = new TreeNode();
			 	t.c='-';
			 	t.right= rightChild;
			 	t.left= leftChild;
			 	return t;
		    }
	}

	public void decompressFile(HashMap<String, Character> codes) {
		 FileOutputStream out = null;


for (Map.Entry< String, Character> entry : codes.entrySet())  
            System.out.println("Key = " + entry.getKey() + 
                             ", Value = " + entry.getValue()); 
	        try {
	        	
	        	File outputFile = new File("DecompressedFile.txt");
	        	outputFile.createNewFile(); // if file already exists will do nothing 
	            out = new FileOutputStream( outputFile, false);
	            
	            StringBuilder temp = new StringBuilder("");
	            while(true)
	            {
	            	int currentBit =  reader.readBit();
	            	if( currentBit == -1)
	            		break;
	            	else if( currentBit == 0)
	            		temp.append('0');
	            	else
	            		temp.append('1');
	            	if( codes.containsKey(temp.toString()))
            		{
	            		System.out.print(codes.get(temp.toString()));
            			out.write( codes.get(temp.toString()));
            			temp = new StringBuilder("");
            		}
	            		
	            }
	            	//out.write(character);
	           
	            
	            if (out != null) {
	            	out.flush();
	                out.close();
	            }
	        } catch(IOException e) {
	        	e.printStackTrace();
	        	System.out.println("HELLO THERE");
	        }
	}
	
}