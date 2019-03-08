package edu.sbcc.cs107;

import static java.lang.System.*;

/**
 * @author Leonard Euler
 * CS 107: Disassembler Project
 *
 * This class is used to model a half-word of an object file.
 * Each half-word must have an address as well as a
 * data value that can be disassembled into mnemonics and optional operands.
 * 
 * Note
 * that the half-word is 16 bits but we are using a
 * Java int which is typically 32 bits.
 * Be sure to take that into
 * account when working with it.
 *
 */
public class Halfword {
	private int address;
	private int data;
	
	/**
	 * Constructor for a halfword.
	 * 
	 * @param address
	 * @param data
	 */
	public Halfword(int address, int data) {
		this.address = address;
		this.data = data;
	}
	
	/** 
	 * toString method.
	 * 
	 * The format for the halfword is a
	 * hex value 8 characters wide (address), a single space, and a hex
	 * value four characters wide (data).
	 *
	 *
	 * 
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder build = new StringBuilder("");

		// "0000FEED A5A5"
		build
				.append(makeAddressStr(Integer.toHexString(getAddress()).toUpperCase()))
				.append(" ")
				.append(makeDataStr(Integer.toHexString(getData()).toUpperCase()));


		//out.println(build.toString());
		return build.toString();
	}

	/**
	 * Get the address of the half-word.
	 * 
	 * @return
	 */
	public int getAddress() {
		return this.address;
	}
	
	/**
	 * Get the data of the half-word.
	 * 
	 * @return
	 */
	public int getData() {
		return this.data;
	}


   /**
	* fill in lead zeros
 	* 		need 8 [hex] chars = 32-bits
 	*
 	* @param incompleteAddress (hex str) not yet 8 char string
 	* @return eightCharStr (hex str) full 8 char w/s front 0's
 	*/
	public String makeAddressStr(String incompleteAddress){
		int numHexChars_32bits = 8;
		String eightCharStr = incompleteAddress;

		while(eightCharStr.length() < numHexChars_32bits){
			eightCharStr = "0" + eightCharStr;
		}

		return eightCharStr;
	}

	/**
	 * F(x) to fill in lead zeros
	 * 		need 4 [hex] chars = 16-bits
	 *
	 * @param incompleteData (hex str), not yet 4 char string
	 * @return fourCharStr (hex str) full 4 char w/s front 0's
	 */
	public String makeDataStr(String incompleteData){
		int numbHexChars_16bits = 4;
		String fourCharStr = incompleteData;

		while(fourCharStr.length() < numbHexChars_16bits){
			fourCharStr = "0" + fourCharStr;
		}

		return fourCharStr;
	}

}
