import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Compress {

	HashMap<Character, String > codes = new HashMap<Character, String>();
	FileOutputStream out;
	File outputFile;
	TreeNode root;
	boolean folder=false;
	boolean lastByte = false;
	int lastIndex=0;
	int currentFileBuffer=0;
	int currentIndex= 0;
	int lastHeaderByte;
	
	
	public void closeWritter() {
		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public Compress(HashMap<Character, String > codes, TreeNode root, String compressedFilePath ) throws IOException {
		this.codes = codes;
		this.root = root;
	    outputFile = new File(compressedFilePath);
    	outputFile.createNewFile(); // if file already exists will do nothing 
	    out = new FileOutputStream( outputFile , false);
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
	
	public void createHeaderBytes( String header ) throws IOException
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
            		out.write(currentB);
            		currentB = 0; //add old buffer to arrayList and empty buffer
            		
            		tempChar = header.charAt(i);  //to get the rest of the bits
            		tempChar = tempChar << (8-currentIndex); //example: shift the five bits that we already added, out of the character (8-3) = 5
            		currentB = currentB | tempChar;  // or the buffer and character, current Index should  still be 3 but for the new buffer
        		}else
        		{//the buffer is empty and can take the whole character
        			currentB = currentB | tempChar;
            		//compressedBytes.add(currentB);
        			out.write(currentB);
            		currentB = 0; //currentIndex is still zero
        		}
        		
        	}else if( header.charAt(i) == '0')
        	{
        		//do nothing just increment the index
        		currentIndex++;
        	}
        	
        	
        	if( currentIndex == 8) //if our buffer is full, add it to the array
        	{
        		//compressedBytes.add(currentB);
        		out.write(currentB);
        		currentB = 0;
        		currentIndex = 0;
        	}
        	
        }
        
        if( currentIndex != 0)
        {
        	//compressedBytes.add(currentB);
        	lastHeaderByte = currentB;
        	lastByte = true;
        	lastIndex = currentIndex;
        }
	}
      
	
public long createCompressedFile(String filePath,boolean folder, boolean lastFile) {
	
	 for (Entry<Character, String> entry : codes.entrySet())  
         System.out.println("Key = " + entry.getKey() + 
                          ", Value = " + entry.getValue());

		
	 
	long fileLength=0;
		FileInputStream in = null;
        try {
            in = new FileInputStream(filePath);
            int tempChar; //shelt currentIndex=0 w 5aleto global
            while ((tempChar = in.read()) != -1 ) {
            	
                
            	if( lastByte ) //check only the first time entering the loop
            	{
            		
            		currentFileBuffer = lastHeaderByte;
            		currentIndex = lastIndex;
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
            			//compressedBytes.add(currentFileBuffer);
            			out.write(currentFileBuffer);
                		currentFileBuffer = 0;
                		currentIndex = 0;
            		}
            	}     
           }
            

            
        	if( folder && !lastFile )
            {    
            	 int specialChar = 215; 
            	 String characCode = codes.get((char)specialChar);
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
            			
            			out.write(currentFileBuffer);
                		currentFileBuffer = 0;
                		currentIndex = 0;
            		}
            	}
            
    			
    		}
        	
            
        	if(  lastFile || !folder)
        	{
        		if( currentIndex != 0 )
        		{
        			out.write(currentFileBuffer);
        			out.write(currentIndex);
        		}else
        		{
        			out.write(8);
        			
        		}
        	}
            
            
            fileLength+=outputFile.length();
        	
            in.close();
        } catch (IOException e) {
			
			e.printStackTrace();
		} 
        return fileLength;
		
	}


public long createCompressedFolder(File[] listOfFiles) {
	long len=0;
	folder=true;
	for(int j=0;j<listOfFiles.length;j++)
	{
		if( j == (listOfFiles.length - 1) )
		  len+=createCompressedFile(listOfFiles[j].getAbsolutePath(),folder, true);
		else
		 createCompressedFile(listOfFiles[j].getAbsolutePath(),folder, false);
	}
	
 return len;
}



}
