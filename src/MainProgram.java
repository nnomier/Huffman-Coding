import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class MainProgram {
private static Scanner in;

public static void main(String[] args) throws IOException {
	fileReading fr = new fileReading();
	File f=fr.getFile();
	String filePath = fr.getFilePath();
	System.out.println("Choose \n [1] Compression \n [2] Decompression \n [3] Folder Compression ");
    in = new Scanner(System.in);
    int choice = in.nextInt();
    String compressedFilePath= filePath.substring(0,filePath.length()-4)+"_compressed.txt";
    
	long startTime = System.currentTimeMillis();

	if(choice==1) {
	    int chars[]=fr.readFromFileReturnArray(f);  
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
	   
	   compress.writeToFile(compressedFilePath);
	}
	else if(choice==2)
	{
		huffmanTree huffmanT = new huffmanTree();
		Decompressor decompressor = new Decompressor(filePath);
		decompressor.root =  decompressor.reconstructHuffmanTree();
		huffmanT.FlipEncodingMap(decompressor.root, "");
		decompressor.decompressFile(huffmanT.getFlippedEncodingMap());
	}
	else if(choice==3)
	{
		
		File fileList[] = fr.folderCompression(f);
		int chars[]=fr.getChars();
		   for(int i=0;i<256;i++) {
		    	if(chars[i]!=0) {
		        System.out.println(i+"<<<<");
			        
		        System.out.println(chars[i]); }
		        }
		huffmanTree huffmanT = new huffmanTree( chars );
		HashMap<Character, String > codes;
		TreeNode root;
		codes = huffmanT.buildTree();
		 root = huffmanT.getRoot();
		   Compress compress = new Compress(codes, root);    
		   String header = compress.preOrderTarversal(root, "");   
		   System.out.println( "This is the header>"+header+"<" );
		   compress.createHeaderBytes(header);	   
		   compress.CompressFolders(fileList);	 
		   
		   compress.writeToFile(filePath+"_compressed.txt");

	}
	
   long stopTime = System.currentTimeMillis();
   long elapsedTime = stopTime - startTime;
   System.out.println("EXECUTION TIME : "+elapsedTime+" ms");
}
}
