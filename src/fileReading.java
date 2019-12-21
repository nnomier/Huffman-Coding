import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter; 


public class fileReading {
	private  String filePath;
	private int chars[] = new int[265];
	private long originalFileLength;
	
	public int[] getChars() {
		return chars;
	}


	public void setChars(int[] chars) {
		this.chars = chars;
	}


	public String getFilePath() {
		return filePath;
	}

	public long getOriginalFileLength() {
		return originalFileLength;
	}
	
	public  File getFile() throws IOException{
		JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif", "txt");

	    chooser.setFileFilter(filter);

	    int returnVal = chooser.showOpenDialog(null);
	    
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	        System.out.println("You chose to open this file: " +
	                chooser.getSelectedFile().getAbsolutePath());
	        
	        this.filePath = chooser.getSelectedFile().getAbsolutePath();
	    }
	    
	    File f=new File(chooser.getSelectedFile().getAbsolutePath()); 
	    originalFileLength=f.length();
	    return f;
	}

	public File[] folderCompression(File f) throws IOException {
		File[] listOfFiles = f.listFiles();
			for(int i=0;i<listOfFiles.length;i++)
		{
				originalFileLength+=listOfFiles[i].length();
			 FileReader fr=new FileReader(listOfFiles[i]);   //Creation of File Reader object
			    BufferedReader br=new BufferedReader(fr);  //Creation of BufferedReader object
			    int c = 0;     
			    while((c = br.read()) != -1)         //Read char by Char
			    {
			    	char character = (char) c;          //converting integer to char
			                 //Display the Character
			  		chars[character]= chars[character]+1;
			          		          
			    }
		}
		chars[215] = 1;
		return listOfFiles;
	}
	
	public void readFromFile(File f) throws IOException {
	    long len = f.length();
	    FileReader fr=new FileReader(f);   //Creation of File Reader object
	    BufferedReader br=new BufferedReader(fr);  //Creation of BufferedReader object
	    int c = 0;     
	    while((c = br.read()) != -1)         //Read char by Char
	    {
	    	char character = (char) c;          //converting integer to char
	                 //Display the Character
	  		chars[character]= chars[character]+1;
	          		          
	    }
    
    
	}
	
	public void readBinaryFile(File f) {
		FileInputStream in = null;
		
		try {
			
			 int c = 0;     
			 in = new FileInputStream(f);
			    while((c = in.read()) != -1)         //Read byte by byte
			    {
			    	char character = (char) c;          //converting integer to char
			  		chars[character]++;
			          		          
			    }
			
		}catch( IOException e )
		{
			e.printStackTrace();
		}
		   
		 
		}
	
		public static void main(String[] args) throws IOException {		
	
		}
}