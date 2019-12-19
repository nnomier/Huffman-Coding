import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Compress {

	ArrayList<Integer> compressedBytes = new ArrayList<Integer>();
	HashMap<Character, String > codes = new HashMap<Character, String>();
	TreeNode root;
	boolean lastByte = false;
	int lastByteIndex = 0;
	
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
			System.out.println( str );
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
        	System.out.println("CURRENT INDEX IS " + currentIndex );
        	if( header.charAt(i) == '1')
        	{
        		//set the bit with index currentIndex to 1 in currentB
        		currentB = (currentB | (128 >> currentIndex) );
        		System.out.println("I is "+ i +" char is " + header.charAt(i) + " and we are in 1" + Integer.toBinaryString(currentB));
        		currentIndex++;
        		i++;

        		//we have an 8 bit character which could overflow our byte buffer
        		int tempChar = header.charAt(i);
        		if( currentIndex != 0)
        		{
        			tempChar = tempChar >> currentIndex; //example: if current index was 3, that means that buffer has 3 bits already used
            		currentB = currentB | tempChar; //therefore shift character by 3 bits to the left, five bits will be added to buffer
            		compressedBytes.add(currentB);  //or the buffer& shifter character
            		System.out.println("H" + Integer.toBinaryString(currentB));
            		currentB = 0; //add old buffer to arrayList and empty buffer
            		
            		tempChar = header.charAt(i);  //to get the rest of the bits
            		tempChar = tempChar << (8-currentIndex); //example: shift the five bits that we already added, out of the character (8-3) = 5
            		currentB = currentB | tempChar;  // or the buffer and character, current Index should  still be 3 but for the new buffer
        		}else
        		{//the buffer is empty and can take the whole character
        			currentB = currentB | tempChar;
            		compressedBytes.add(currentB);
            		System.out.println("H" + Integer.toBinaryString(currentB));
            		currentB = 0; //currentIndex is still zero
        		}
        		
        	}else if( header.charAt(i) == '0')
        	{
        		//do nothing just increment the index
        		currentIndex++;
        		System.out.println("I is "+ i +" char is " + header.charAt(i) + " and we are in 0" + Integer.toBinaryString(currentB));
        	}
        	
        	
        	if( currentIndex == 8) //if our buffer is full, add it to the array
        	{
        		compressedBytes.add(currentB);
        		System.out.println("H" + Integer.toBinaryString(currentB));
        		currentB = 0;
        		currentIndex = 0;
        	}
        	
        }
        
        if( currentIndex != 0)
        {
        	compressedBytes.add(currentB);
        	System.out.println("H" + Integer.toBinaryString(currentB));
        	lastByte = true;
        	lastByteIndex = currentIndex;
        }
	}
      
public void CompressFolders(File[] fileList)
{
	for(int i=0;i<fileList.length;i++)
	{
		 String filePath = fileList[i].getAbsolutePath();
		 int len = (int) fileList[i].length();
		 compressedBytes.add(len);
		 //need to add seperator here
		 createCompressedFile(filePath);
		
	}
}

public void createCompressedFile(String filePath) {
		
		FileInputStream in = null;
        try {
            in = new FileInputStream(filePath);
            int tempChar, currentIndex = 0, currentB = 0;
            while ((tempChar = in.read()) != -1 ) { //not sure of this condition though, what happens when in.read is called twice when 
            	//file is already completely read 
                
            	if( lastByte ) //check only the first time entering the loop
            	{
            		
            		currentIndex = this.lastByteIndex;
            		currentB = compressedBytes.get(compressedBytes.size()-1);
            		compressedBytes.remove(compressedBytes.size()-1);
            		lastByte = false;
            		System.out.println(" INSIDE LAST BYTE "+ Integer.toBinaryString(currentB));
            	}
            	
            	String characCode;
                characCode = codes.get( (char)tempChar);
                System.out.println("Character code is "+ characCode);
            	
            	
            	int len = characCode.length(); //loop on the character's code
            	
            	for(int i=0; i<len; i++)
            	{
            		if( characCode.charAt(i) == '1')
                	{
                		//set the bit with index currentIndex to 1 in currentB
                		currentB = currentB | (128>> currentIndex);
                		currentIndex++;
                	}else if( characCode.charAt(i) == '0')
                	{
                		//do nothing just increment the index
                		currentIndex++;
                	}
            		
            		if( currentIndex == 8 )
            		{
            			compressedBytes.add(currentB);
            			System.out.println("NEW ALERT " + Integer.toBinaryString(currentB));
                		currentB = 0;
                		currentIndex = 0;
            		}
            	}
            	
            
           }
            
        	if( currentIndex != 0 )
        	{
        		System.out.println("NEW ALERT " + Integer.toBinaryString(currentB));
        		compressedBytes.add(currentB);
        		compressedBytes.add(currentIndex); //number of valid bits in last byte
        	}else
        	{
        		compressedBytes.add(8); //number of valid bits is the whole byte
        	}
        	
            in.close();
        } catch (IOException e) {
			
			e.printStackTrace();
		} 
	}

	public void writeToFile(String filePath) {
		
	
        FileOutputStream out = null;

        try {
        	File outputFile = new File(filePath);
        	outputFile.createNewFile(); // if file already exists will do nothing 
            out = new FileOutputStream( outputFile, false);
            System.out.println("STARTING FILE PRINT");
            
            for( int character : compressedBytes)
            {
                out.write(character);
            	System.out.println( Integer.toBinaryString(character) + " BINARY BYTE "+ (char )character);
            }
            
            if (out != null) {
                out.close();
            }
        } catch(IOException e) {
        	e.printStackTrace();
        	System.out.println("HELLO THERE");
        }
	}
}