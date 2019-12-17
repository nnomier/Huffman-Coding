import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;

public class BitReader {

	int current;    // Current byte being returned, bit by bit
	int next;       // Next byte to be returned (could be a count)
	int afterNext;  // Byte two after the current byte
	int bitMask;    // Shows which bit to return
	  
	
	BufferedInputStream input;

	public BitReader(String pathName) throws IOException {
		input = new BufferedInputStream(new FileInputStream(pathName));
		
		current = input.read();
		if(current == -1)
			throw new EOFException("File did not have two bytes");
		
		next = input.read();
		if(next == -1) 
			throw new EOFException("File did not have two bytes");	
		
		afterNext = input.read();
		bitMask = 128;   // a 1 in leftmost bit position
	}
	
	public int readByte() throws IOException
	{
		int returnByte=current;
		current = next;
      	next = afterNext;
      	afterNext = input.read();
		return returnByte;		
	}
	
	
	public int readBit() throws IOException {
		int returnBit;   // Hold the bit to return
		
		if(afterNext == -1)  // Are we emptying the last byte?
			
			// When afterNext == -1, next is the count of bits remaining.
			
			if(next == 0)   // No more bits in the final byte to return
		  	return -1;    // No more bits to return
		  else {
		  	if((bitMask & current) == 0)
		  		returnBit = 0;
		  	else 
		  		returnBit = 1;
		  	
        next--;          // One fewer bit to return
        bitMask = bitMask >> 1;    // Shift to mask next bit
        return returnBit;
		  }
		
		else {
	  	if((bitMask & current) == 0)
	  		returnBit = 0;
	  	else 
	  		returnBit = 1;
	  	
      bitMask = bitMask >> 1;    // Shift to mask next bit
      
      if(bitMask == 0)  {        // Finished returning this byte?
      	bitMask = 128;           // Leftmost bit next
      	current = next;
      	next = afterNext;
      	afterNext = input.read();
      }
      return returnBit;
		}
	}
	
	public void close() throws IOException {
		input.close();
	}
}