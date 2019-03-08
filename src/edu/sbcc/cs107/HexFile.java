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
	private ArrayList<String> hexFileDataType = null; // hexFile minus everything not type: Data
	private ArrayList<Halfword> halfWords_List = null;

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
				hexFile.add(line);
			} // we have hexFile, an arryLst w/ strings of which are tbd to live

			makeDataTypeArrayListFromHexFile();
			makeHalfWordsFromDataList();
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
	 * Gets all the data
	 *
	 * Will assume each record length is 16 bytes
	 *
	 *
	 * @param Hex file record (one line).
	 * @return data_dec decimal format
	 */
	public int getDataDecimal(String record){
		String data_hexStr = record.substring(9, 41);
		int data_dec = Integer.parseInt(data_hexStr, 16);
		//////System.out.println("~+" + data_dec);

		return data_dec;
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
	 */
	public Halfword getNextHalfword() {
		ListIterator<Halfword> it = halfWords_List.listIterator();
		Halfword _hw = null;

		while(it.hasNext()){
			_hw = it.next();
			return _hw;
		}
		return _hw;
	}

	/**
	 * Our HexFile is an arrayList of strings of MANY TYPES
	 * We need new arrayList to have just type: DATA
	 *
	 */
	public void makeDataTypeArrayListFromHexFile(){
		hexFileDataType = new ArrayList<String>();

		for(int i = 0; i < hexFile.size(); i++){
				String lineOf = hexFile.get(i);
				String typeCheck = lineOf.substring(7,9);
				if(typeCheck.equals("00")){
					hexFileDataType.add(lineOf);
				}
		}
	}

	/**
	 * array list of halfwords
	 * "public Halfword(int address, int data) "
	 *
	 */
	public void makeHalfWordsFromDataList(){
		int address = 0;
		int data = 0; // 00000000 0400
		halfWords_List = new ArrayList<Halfword>();

		for(int x = 0; x < hexFileDataType.size(); x++){
			Halfword hw = new Halfword(
					getAddressOfRecord(hexFileDataType.get(x)),
					getDataDecimal(hexFileDataType.get(x)));
			halfWords_List.add(hw);
		}

	}
}
