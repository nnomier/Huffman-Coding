import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter; 


public class fileReading {
	
	private  String filePath;
	
	public String getFilePath() {
		return filePath;
	}

	
	public  File getFile() throws IOException{
		JFileChooser chooser = new JFileChooser();

	    FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif", "txt");

	    chooser.setFileFilter(filter);

	    int returnVal = chooser.showOpenDialog(null);
	    
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	        System.out.println("You chose to open this file: " +
	                chooser.getSelectedFile().getAbsolutePath());
	        
	        this.filePath = chooser.getSelectedFile().getAbsolutePath();
	    }
	    
	    File f=new File(chooser.getSelectedFile().getAbsolutePath());     //Creation of File Descriptor for input file
	    return f;
	}

	public int[] readFromFileReturnArray(File f) throws IOException {
		int chars[] = new int[265]; 
	
	    FileReader fr=new FileReader(f);   //Creation of File Reader object
	    BufferedReader br=new BufferedReader(fr);  //Creation of BufferedReader object
	    int c = 0;     
	    while((c = br.read()) != -1)         //Read char by Char
	    {
	    	char character = (char) c;          //converting integer to char
	                 //Display the Character
	  		chars[character]= chars[character]+1;
	          		          
	    }
    
    
	return chars;
	}
	
		public static void main(String[] args) throws IOException {		
	
		}
}