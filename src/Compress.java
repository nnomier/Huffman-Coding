import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Compress {

	ArrayList<Integer> compressedBytes = new ArrayList<Integer>();
	HashMap<Character, String > codes = new HashMap<Character, String>();
	TreeNode root;
	boolean folder=false;
	boolean lastByte = false;
	int lastByteIndex = 0;
	int currentFileBuffer=0;
	int currentIndex= 0;
	
	public Compress(HashMap<Character, String > codes, TreeNode root ) {
		this.codes = codes;
		this.root = root;
	}
	
	public String preOrderTarversal(  TreeNode root, String str )
	{
		if( root == null )
			return str.toString();
		if( !root.isLeafNode() )
		{
			str = str + '0';
			str = preOrderTarversal( root.left, str);
			str = preOrderTarversal( root.right, str);
			return str;
		} else
		{
			str = str + '1';
			str = str + root.c;
			return str;
		}
	}
	
	public void createHeaderBytes( String header )
	{
		int currentB = 0;
        int len = header.length(), currentIndex = 0;
        
        for(int i=0; i<len; i++) //loops on every character in header
        {
        	if( header.charAt(i) == '1')
        	{
        		//set the bit with index currentIndex to 1 in currentB
        		currentB = (currentB | (128 >> currentIndex) );
        		currentIndex++;
        		i++;

        		//we have an 8 bit character which could overflow our byte buffer
        		int tempChar = header.charAt(i);
        		if( currentIndex != 0)
        		{
        			tempChar = tempChar >> currentIndex; //example: if current index was 3, that means that buffer has 3 bits already used
            		currentB = currentB | tempChar; //therefore shift character by 3 bits to the left, five bits will be added to buffer
            		compressedBytes.add(currentB);  //or the buffer& shifter character
            		currentB = 0; //add old buffer to arrayList and empty buffer
            		
            		tempChar = header.charAt(i);  //to get the rest of the bits
            		tempChar = tempChar << (8-currentIndex); //example: shift the five bits that we already added, out of the character (8-3) = 5
            		currentB = currentB | tempChar;  // or the buffer and character, current Index should  still be 3 but for the new buffer
        		}else
        		{//the buffer is empty and can take the whole character
        			currentB = currentB | tempChar;
            		compressedBytes.add(currentB);
            		currentB = 0; //currentIndex is still zero
        		}
        		
        	}else if( header.charAt(i) == '0')
        	{
        		//do nothing just increment the index
        		currentIndex++;
        	}
        	
        	
        	if( currentIndex == 8) //if our buffer is full, add it to the array
        	{
        		compressedBytes.add(currentB);
        		currentB = 0;
        		currentIndex = 0;
        	}
        	
        }
        
        if( currentIndex != 0)
        {
        	compressedBytes.add(currentB);
        	lastByte = true;
        	lastByteIndex = currentIndex;
        }
	}
      
	
public void createCompressedFile(String filePath,boolean folder, boolean lastFile) {
		System.out.println("buffer"+currentFileBuffer);
		FileInputStream in = null;
        try {
            in = new FileInputStream(filePath);
            int tempChar; //shelt currentIndex=0 w 5aleto global
            while ((tempChar = in.read()) != -1 ) {
            	
                
            	if( lastByte ) //check only the first time entering the loop
            	{
            		currentIndex = this.lastByteIndex;
            		currentFileBuffer = compressedBytes.get(compressedBytes.size()-1);
            		compressedBytes.remove(compressedBytes.size()-1);
            		lastByte = false;
            	}
            	
            	String characCode;
                characCode = codes.get( (char)tempChar);
            	
            	
            	int len = characCode.length(); //loop on the character's code
            	
            	for(int i=0; i<len; i++)
            	{
            		if( characCode.charAt(i) == '1')
                	{
                		//set the bit with index currentIndex to 1 in currentB
                		currentFileBuffer = currentFileBuffer | (128>> currentIndex);
                		currentIndex++;
                	}else if( characCode.charAt(i) == '0')
                	{
                		//do nothing just increment the index
                		currentIndex++;
                	}
            		
            		if( currentIndex == 8 )
            		{
            			compressedBytes.add(currentFileBuffer);
                		currentFileBuffer = 0;
                		currentIndex = 0;
            		}
            	}     
           }
            

            
        	if( folder && !lastFile )
            {     System.out.println("its a folder and i finished writing one file");
            	 int specialChar = 215; 
            	 String characCode = codes.get((char)specialChar);
            	 System.out.println("special character is to be written with code is "+characCode);
                // loop to put the special character
                int len = characCode.length();
                for(int i=0; i<len; i++)
            	{
            		if( characCode.charAt(i) == '1')
                	{
                		//set the bit with index currentIndex to 1 in currentB
                		currentFileBuffer = currentFileBuffer | (128>> currentIndex);
                		currentIndex++;
                	}else if( characCode.charAt(i) == '0')
                	{
                		//do nothing just increment the index
                		currentIndex++;
                	}
            		
            		if( currentIndex == 8 )
            		{
            			compressedBytes.add(currentFileBuffer);
                		currentFileBuffer = 0;
                		currentIndex = 0;
            		}
            	}
            System.out.println("buffer here is"+currentFileBuffer);
    			
    		}
        	
            
        	if(  lastFile || !folder)
        	{
        		if( currentIndex != 0 )
        		{
        			compressedBytes.add(currentFileBuffer);
        			compressedBytes.add(currentIndex); //number of valid bits in last byte
        		}else
        		{
        			compressedBytes.add(8); //number of valid bits is the whole byte
        		}
        	}
            
            
            
        	
            in.close();
        } catch (IOException e) {
			
			e.printStackTrace();
		} 
		
	}

	public long  writeToFile(String filePath) {
		
		long len=0;
        FileOutputStream out = null;

        try {
        	File outputFile = new File(filePath);
        	outputFile.createNewFile(); // if file already exists will do nothing 
            out = new FileOutputStream( outputFile, false);
            
            for( int character : compressedBytes)
            {
                out.write(character);
            }

            if (out != null) {
                out.close();
            }
            len=outputFile.length();
        } catch(IOException e) {
        	e.printStackTrace();
        }
        return len;
	}

public void createCompressedFolder(File[] listOfFiles) {
//boolean folderLastByte = false;
	folder=true;
	for(int j=0;j<listOfFiles.length;j++)
	{
		if( j == (listOfFiles.length - 1) )
			createCompressedFile(listOfFiles[j].getAbsolutePath(),folder, true);
		else
		 createCompressedFile(listOfFiles[j].getAbsolutePath(),folder, false);
	}
	
 
}



}
