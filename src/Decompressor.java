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
	private String filepath;
	public Decompressor(String filepath) throws IOException
	{
		this.filepath=filepath;
		reader = new BitReader(filepath);
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

	public long decompressFile(HashMap<String, Character> codes) {
		 FileOutputStream out = null;
		    String decompressedPath= filepath.substring(0,filepath.length()-15)+"_decompressed.txt";

		    long len=0;
		 for (Map.Entry< String, Character> entry : codes.entrySet())  
            System.out.println("Key = " + entry.getKey() + 
                             ", Value = " + entry.getValue()); 
	        try {
	        	
	        	File outputFile = new File(decompressedPath);
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
	            		//System.out.print(codes.get(temp.toString()));
            			out.write( codes.get(temp.toString()));
            			temp = new StringBuilder("");
            		}
	            		
	            }
	            	
	           len=outputFile.length();
	            
	            if (out != null) {
	            	out.flush();
	                out.close();
	            }
	        } catch(IOException e) {
	        	e.printStackTrace();
	        }
			return len;
	}
	
	public long decompressFolder(HashMap<String, Character> codes) {
		long len=0;
		int i = 1;
		int specialChar = 215;
		 FileOutputStream out = null;
		 String decompressedPath= filepath.substring(0,filepath.length()-15)+i+"_decompressed.txt";
		 for (Map.Entry< String, Character> entry : codes.entrySet())  
	            System.out.println("Key = " + entry.getKey() + 
	                             ", Value = " + entry.getValue()); 

	        try {
	        	
	        	File outputFile = new File(decompressedPath);
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
	            		if (codes.get(temp.toString()).equals((char)specialChar)) {
	            			len+=outputFile.length(); 
	            			i++;
	            			 if (out != null) {
	         	            	out.flush();
	         	                out.close();
	         	            }

	            			decompressedPath= filepath.substring(0,filepath.length()-15)+i+"_decompressed.txt";
	            			 outputFile = new File(decompressedPath);
	        	        	outputFile.createNewFile(); // if file already exists will do nothing 
	        	            out = new FileOutputStream( outputFile, false);
	        	            temp = new StringBuilder("");
	            		}
	            		else {
         			out.write( codes.get(temp.toString()));
         			temp = new StringBuilder("");
         			}
         		}
	            		
	            }
	            	//out.write(character);
	            len+=outputFile.length();  
	            
	            if (out != null) {
	            	out.flush();
	                out.close();
	            }
	        } catch(IOException e) {
	        	e.printStackTrace();
	        }
	        return len;
	}
}