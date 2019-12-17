import java.io.*;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter; 


public class fileReading {
	
	static String filePath;
	
public static int[] readFromFileReturnArray() throws IOException {
	int chars[] = new int[265]; 
    
	JFileChooser chooser = new JFileChooser();
    FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif", "txt");
    chooser.setFileFilter(filter);
    int returnVal = chooser.showOpenDialog(null);
    
    if(returnVal == JFileChooser.APPROVE_OPTION) {
        System.out.println("You chose to open this file: " +
                chooser.getSelectedFile().getAbsolutePath());
        
        filePath = chooser.getSelectedFile().getAbsolutePath();
    }
    
    File f=new File(chooser.getSelectedFile().getAbsolutePath());     //Creation of File Descriptor for input file
    
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
			
			int chars[]=readFromFileReturnArray();  
			huffmanTree huffmanT = new huffmanTree( chars );
			HashMap<Character, String > codes;
			TreeNode root;
			
		    for(int i=0;i<256;i++) {
		    	if(chars[i]!=0) {
		        System.out.println(i+"<<<<");
			        
		        System.out.println(chars[i]); }
		        }
		    
		   codes = huffmanT.buildTree();
		   root = huffmanT.getRoot();
		   
		   Compress compress = new Compress(codes, root);
            
		   String header = compress.preOrderTarversal(root, "");
		   
		   System.out.println( "This is the header>"+header+"<" );
		   compress.createHeaderBytes(header);
		   
		   compress.createCompressedFile(filePath);
		   
		   compress.writeToFile();
			Decompressor decompressor = new Decompressor();

		  decompressor.root =  decompressor.reconstructHuffmanTree();
		   
		   huffmanT.FlipEncodingMap(decompressor.root, "");
		   
		   decompressor.decompressFile(huffmanT.getFlippedEncodingMap());
			
		}
}