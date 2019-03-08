package edu.sbcc.cs107;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import static org.apache.commons.io.FileUtils.*;
import java.util.ListIterator;
import java.util.List;

/**
 * @author Leonard Euler
 * CS 107: Disassembler Project
 *
 * This code implements working with a Hex file.
 * The hex file format is documented
 * at http://www.keil.com/support/docs/1584/
 */
public class HexFile {
	/**
	 * This is where you load the hex file.
	 * By making it an ArrayList you can easily traverse it in order.
	 */
	private ArrayList<String> hexFile = null; // full arry w all lines
	private ArrayList<String> hexFileTypeData = null; // ALL type: Data
	private ArrayList<Halfword> halfWords = null;
	private static int hwBytes = 2; // two bytes
	private static int wordBytes = 4; // four bytes
	private static int wordChars = 8; // eight hex chars in word

	/**
	 * Constructor that loads the .hex file.
	 * 
	 * @param hexFileName
	 * @throws FileNotFoundException
	 */
	public HexFile(String hexFileName) throws FileNotFoundException {
		try {
			File file = new File(hexFileName);
			String longText = FileUtils.readFileToString(file, "UTF-8");

			String[] arrayOfLines = longText.split("\\r?\\n");
			hexFile = new ArrayList<String>();

			for(String line : arrayOfLines){
				hexFile.add(line); // arrayList w/ strings of which are tbd to live
			}

			hexFileTypeData = makeHexFileTypeData(); // ArrayList<String> hexFileTypeData
			halfWords = makeHalfWords(); // ArrayList<Halfword> halfWords
		}
		catch (Exception IO){
			System.out.println(IO.getMessage());
		}
	}

	/**
	 * Pulls the length of the data bytes from an individual record.
	 * 
	 * This extracts the length of the data byte field from an individual 
	 * hex record.
	 * This is referred to as LL->Record Length in documentation.
	 * 
	 * @param Hex file record (one line).
	 * @return record length.
	 */
	public int getDataBytesOfRecord(String record) {
		String input = record;
		StringBuilder lengthHexStr = new StringBuilder("");
		int recordLengthBytes = 0;

		// ":[10]246200464C5549442050524F46494C4500464C33"
		for(int i = 1; i < 3; i++) {
			lengthHexStr.append(input.charAt(i));
		}
		recordLengthBytes = Integer.parseInt(lengthHexStr.toString(), 16);

		return recordLengthBytes;
	}
	
	/**
	 * Get the starting address of the data bytes.
	 * 
	 * Extracts the starting address for the data.
	 * This tells you where the data bytes
	 * start && are referred to as
	 * AAAA->Address in the doc.
	 * 
	 * @param Hex file record (one line).
	 * @return Starting address of where the data bytes go.
	 */
	public int getAddressOfRecord(String record) {
		String input = record;
		StringBuilder addressHexStr = new StringBuilder("");
		int address = 0;

		// ":10[2462]00464C5549442050524F46494C4500464C33"
		for(int x = 3; x < 7; x++){
			addressHexStr.append(input.charAt(x));
		}
		address = Integer.parseInt(addressHexStr.toString(), 16);

		return address;
	}
	
	/**
	 * Gets the record type.
	 * 
	 * The record type tells you what the record
	 * can do and determines what happens
	 * to the data in the data field.
	 * This is referred to as TT->Record Type in the
	 * documentation.
	 * 
	 * @param Hex file record (one line).
	 * @return Record type.
	 */
	public int getRecordType(String record) {

		String input = record;
		StringBuilder recordTypeHexStr = new StringBuilder("");
		int recordType = 0;

		// ":102462[00]464C5549442050524F46494C4500464C33"
		for(int r = 7; r < 9; r++){
			recordTypeHexStr.append(input.charAt(r));
		}
		recordType = Integer.parseInt(recordTypeHexStr.toString(), 16);

		return recordType;
	}

	/**
	 * Returns next halfword data byte
	 * 
	 * This f(x) will extract next halfword from Hex file.
	 * By repeatedly calling this f(x)
	 * it will look like we are getting a series of halfwords.
	 *
	 * Behind the scenes we must parse the HEX file so that we are
	 * extracting the data from the data files
	 * as well as indicating the correct address.
	 * 
	 * This requires us to handle various record types.
	 * Some record types can effect the address only.
	 * These need to be processed && skipped.
	 *
	 * Only data from recordType 0 will result in something returned.
	 * When finished processing null is returned.
	 * 
	 * @return Next halfword.
	 *
	 * Notes:
	 *
	 * 		// hf.getNextHalfword().toString()
	 *
	 * IN:
	 * 		[HEX FILE: ] (max length: 42 chars + : )
	 *
	 *
	 * 		absolute vs offset (0000) for this one below, hmmm
	 * 		:02(0000)(04)(0000)(FA) _extnededLinearAddress (skip because of type)
	 * 		:10(0000)(00)[00040010-E5000000-E9000000-EB000000]23 _dataRecord		[*,~ - #,! - @,^ - +,&]
	 * 		:10(0010)(00)[ED000000-EF000000-F1000000-9EF3FFEF]94 				[%,{} - ...]
	 * 		:10(0020)(00)[00000000-00000000-00000000-F3000000]DD
	 * 		:10(0030)(00)[F5000000-00000000-F7000000-F9000000]DB
	 * 													...etc
	 *
	 *
	 * OUT:
	 * 		hw - Halfword(int address, int data)
	 *
	 * 	   Expected:
	 * 					-Address	 (16^0 * xBytes)-
	 * 					"00000000 0400"		*
	 * 					"00000002 1000"		~
	 * 					"00000004 00E5"		#
	 * 					"00000006 0000"		!
	 * 					"00000008 00E9"		@
	 * 					"0000000A 0000"		^
	 * 					"0000000C 00EB"		+
	 * 					"0000000E 0000"		&
	 *
	 * 					"00000010 00ED"		%
	 * 					"00000012 0000"		{}
	 */
	public Halfword getNextHalfword() {
		Halfword hw = null;
		ListIterator<Halfword> it = halfWords.listIterator();

		while(it.hasNext()){
			hw = it.next();
			return hw;
		}
		return hw;
	}

	/**
	 * Our HexFile is an arrayList of strings of MANY TYPES
	 * We need new arrayList to have just type: DATA
	 *
	 */
	public ArrayList<String> makeHexFileTypeData(){
		String typeData = "00";
		hexFileTypeData = new ArrayList<String>();

		for(int i = 0; i < hexFile.size(); i++){
				String lineOf = hexFile.get(i);
				String typeCheck = lineOf.substring(7,9);
				if(typeCheck.equals(typeData)){
					hexFileTypeData.add(lineOf);
					//System.out.println(lineOf);
				}
		}
		return hexFileTypeData;
	}

	/**
	 * Gets all the data, too much!!!!
	 *
	 *
	 * @param data_hexStr (one line).
	 * @return data_dec decimal format
	 */
	public int getBaseTenData(String data_hexStr){
		int data_dec = Integer.parseInt(data_hexStr, 16);

		return data_dec;
	}

	/**
	 * Make arrayList of halfWords from hexFileTypeData
	 *
	 */
	public ArrayList<Halfword> makeHalfWords() {
		int start = 0;
		String data = "";
		int offset = 0;
		int jump = 0; // halfWord jumps
		halfWords = new ArrayList<Halfword>();

		for(int row = 0; row < hexFileTypeData.size(); row++) {
			start = getAddressOfRecord(hexFileTypeData.get(row));
			offset = row * hwBytes;
			for(int col = 0; col < wordChars; col++) {
				jump = col * wordBytes;
				data = flip(hexFileTypeData.get(row).substring(9+jump, 13+jump));

				Halfword hw = new Halfword(start + offset, getBaseTenData(data));


				halfWords.add(hw);
			}
		}
		return halfWords;
	}

	/**
	 * MSB, LSB mixup, flip the bytes around
	 *
	 * @param unordered
	 * @return flipped
	 */
	public String flip(String unordered){
		String flipped = "";
		String beg = unordered.substring(0,2);
		String end = unordered.substring(2,4);

		flipped = end + beg;

		return flipped;
	}
}
