/**
**************************************************************
					CS107 Assembly

					Lab 01 - Disassembler

					Date:   [13 FEB 2019]
 	Authors: [CastleBomber && Leonard Euler && Dr. Nevins]
**************************************************************
*/

package edu.sbcc.cs107;

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
	 */
	public String getRegister(Halfword hw, int lsBitPosition, int msBitPosition) {
		// :)



		return "r3";
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
	 */
	public String getImmediate(Halfword hw, int lsBitPosition, int msBitPosition) {
		/* */
		return "Replace this";
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
		/* */
		return "Replace this";
	}
	
}
