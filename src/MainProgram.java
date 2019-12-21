import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class MainProgram {
private static Scanner in;

public static void main(String[] args) throws IOException {
	fileReading fr = new fileReading();
	File f=fr.getFile();
	long readFileLength=fr.getOriginalFileLength();	//only used in case of file not folders
	long outputFileLength=0;
	double ratio=0;
	String filePath = fr.getFilePath();
	System.out.println("Choose \n [1] Text File Compression \n [2] File Decompression \n [3] Folder Compression \n [4] Folder Decompression  ");
    in = new Scanner(System.in);
    int choice = in.nextInt();
    String compressedFilePath= filePath.substring(0,filePath.length()-4)+"_compressed.txt";
    
	long startTime = System.currentTimeMillis();

	if(choice==1) {
		fr.readBinaryFile(f);
	    if(readFileLength==0) {
	    	System.out.println("File was empty, Compression denied");
	    	return;
	    }
	    int[] chars = fr.getChars();
		huffmanTree huffmanT = new huffmanTree( chars );
		HashMap<Character, String > codes;
		TreeNode root;
	   System.out.println("CHARACTEr CODES: ");
	   codes = huffmanT.buildTree();
	   System.out.println(codes);
	   System.out.println("------------------------------------------------------------------------");
	   root = huffmanT.getRoot();
	   Compress compress = new Compress(codes, root);    
	   String header = compress.preOrderTarversal(root, "");   
	   compress.createHeaderBytes(header);	   
	   compress.createCompressedFile(filePath,false,false);	 
	   outputFileLength=compress.writeToFile(compressedFilePath);
	   //compression file divided by original text file read
	   ratio = (double)outputFileLength/readFileLength;   
	}
	else if(choice==2)
	{
		huffmanTree huffmanT = new huffmanTree();
		Decompressor decompressor = new Decompressor(filePath);
		decompressor.root =  decompressor.reconstructHuffmanTree();
		huffmanT.FlipEncodingMap(decompressor.root, "");
		outputFileLength=decompressor.decompressFile(huffmanT.getFlippedEncodingMap());
		//compression file divided by decompressed file 
		ratio = (double)readFileLength/outputFileLength;
	}
	else if(choice==3)
	{
		
		File fileList[] = fr.folderCompression(f);
		readFileLength=fr.getOriginalFileLength();
		if(readFileLength==0)
		{    
	    	System.out.println("Folder was empty, Compression denied");
	    	return;
		}
		int chars[]=fr.getChars();
		huffmanTree huffmanT = new huffmanTree( chars );
		HashMap<Character, String > codes;
		TreeNode root;
		codes = huffmanT.buildTree();
		System.out.println(codes);
	    root = huffmanT.getRoot();
	   Compress compress = new Compress(codes, root);    
	   String header = compress.preOrderTarversal(root, "");   
	   compress.createHeaderBytes(header);	   
	   compress.createCompressedFolder(fileList);	 
	   
	   outputFileLength=compress.writeToFile(filePath+"_compressed.txt");
	   ratio = (double)outputFileLength/readFileLength;   


	}
	else if(choice==4)
	{
		huffmanTree huffmanT = new huffmanTree();
		Decompressor decompressor = new Decompressor(filePath);
		decompressor.root =  decompressor.reconstructHuffmanTree();
		huffmanT.FlipEncodingMap(decompressor.root, "");
		//outputfilelength here is the total length of the folder
		outputFileLength=decompressor.decompressFolder(huffmanT.getFlippedEncodingMap());
		ratio = (double)readFileLength/outputFileLength;

	}
	
   long stopTime = System.currentTimeMillis();
   long elapsedTime = stopTime - startTime;
   System.out.println("Size of Original file is: "+readFileLength);
   System.out.println("Size of Output file is: "+outputFileLength);
   System.out.println("Compression ratio is: "+ratio);

   if(ratio>=1)
   {
	   System.out.println("Compression was unsuccessful , compressed file size is the same or larger");
   }
   
   System.out.println("EXECUTION TIME : "+elapsedTime+" ms");
}
}