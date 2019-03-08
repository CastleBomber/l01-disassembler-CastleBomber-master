/**
**************************************************************
					CS107 Assembly

					Lab 01 - Disassembler

					Date:   [13 FEB 2019]
 	Authors: [Leonard Euler && Dr. Nevins]
 	Editor: [CastleBomber]
**************************************************************
*/

package edu.sbcc.cs107;
import org.omg.CORBA.INTERNAL;

import static java.lang.System.*;
import java.io.*;
import java.util.*;

/**
 * @author Leonard Euler
 * CS 107: Disassembler Project
 * 
 * This code implements the disassembler
 * as well as pulling apart the Hex file.
 * The hex file format
 * is documented at http://www.keil.com/support/docs/1584/
 */
public class Disassembler {

	final String ADCS = "0100000101"; //A7-187 T1
	final String ADDS = "0001110"; //A7-189 T1
	final String CMN = "0100001011"; //A7-227 T1
	final String LDRSB = "0101011"; //A7-286 T1
	final String MOVS_reg = "0000000000"; //A7-314 T2
	final String MOVS_imm = "00100"; //	A7-312 T1
	final String REV = "1011101000"; //A7-363 T1
	final int machineCodeTerminate_0xE7FE = 59390;

	/**
	 * Extracts the register operand from a halfword.
	 * 
	 * The register operand (e.g. r0) is used by many mnemonics
	 * && is embedded in the data halfword.
	 * It's position is specified by the LSB && MSB.
	 * This value is extracted && concatenated w/ "r"
	 * to give us the desired register.
	 * 
	 * @param hw Halfword that contains the machine code data.
	 * @param lsBitPosition Encoded register value (LSB)
	 * @param msBitPosition Encoded register value (MSB)
	 * @return Register field designation (e.g. r1)
	 *
	 * Notes:
	 *
	 * IN:
	 * 		hw = new Halfword(0xFFFF, 0xAAA6); //[address, data]
	 * 		d.getRegister(hw, 1, 3)
	 *
	 * OUT:
	 * 		"r3"
	 */
	public String getRegister(Halfword hw, int lsBitPosition, int msBitPosition) {
		int reg = 0;
		String data = order(order(Integer.toBinaryString(hw.getData()))
				.substring(lsBitPosition,msBitPosition+1));
		reg = Integer.parseInt(data, 2);

		return "r" + reg;
	}
	
	/**
	 * Extracts the immediate operand from a halfword.
	 * 
	 * Same as the getRegister f(x) but
	 * returns the embedded immediate value (e.g. #4).
	 *                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      
	 * @param hw Halfword that contains the machine code data.
	 * @param lsBitPosition Encoded immediate value (LSB)
	 * @param msBitPosition Encoded immediate value (MSB)
	 * @return Immediate field designation (e.g. #12)
	 *
	 * Notes:
	 *
	 * IN:
	 * 		//1010 1010 10(10 011)0
	 *
	 * 		hw = new Halfword(0xFFFF, 0xAAA6); //[address, data]
	 * 		d.getImmediate(hw, 1, 5)
	 *
	 * OUT:
	 * 		//0(110 01)01 0101 0101
	 *
	 * 		"#19"
	 */
	public String getImmediate(Halfword hw, int lsBitPosition, int msBitPosition) {
		int imm = 0;
		String data = order(order(Integer.toBinaryString(hw.getData()))
				.substring(lsBitPosition,msBitPosition+1));
		imm = Integer.parseInt(data, 2);

		return "#" + imm;
	}

	/**
	 * Returns a formatted string consisting of the
	 * Mnemonic and Operands for the given halfword.
	 * 
	 * The halfword is decoded into its corresponding
	 * mnemonic and any optional operands.
	 *
	 * The return value is a formatted string
	 * with an 8 character wide field
	 * for the mnemonic (left justified) a
	 * single space and then any operands.
	 * 
	 * @param hw Halfword that contains the machine code data.
	 * @return Formatted string containing the mnemonic and any operands.
	 */
	public String dissassembleToString(Halfword hw) {

		return "ADCS     r6, r0";
	}

	/**
	 * The MSB && LSB are starting from RIGHT
	 *
	 * We have bit postitions:
	 * 		[0 -> 15]
	 * We want:
	 * 		[15 -> 0]
	 *
	 * 	May need to "double order" w/n
	 * 		certain f(x)s
	 *
	 * @param unflipped out of order
	 * @return sb stringBuilder w/ reverse input
	 */
	public String order(String unflipped){
		StringBuilder sb = new StringBuilder();
		sb = sb.append(unflipped).reverse();
		return sb.toString();
	}
	
}
