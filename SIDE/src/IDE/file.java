package IDE;








import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author rohan
 */
public class file {

	/**
	 * Prints each line of the file fileName.
	 *
	 * @param fileName is the path to the file or just the name if it is local
	 */
	public static void printFile(String fileName) {
		try { 
			File textFile = new File(fileName);
			Scanner sc = new Scanner(textFile);
			while (sc.hasNextLine()) {
				System.out.println(sc.nextLine());
			}
		} catch (Exception e) {

		}
	}

	/**
	 *
	 * @param fileName is the path to the file or just the name if it is local
	 * @return the number of lines in fileName
	 */
	public static int getLengthOfFile(String fileName) {
		int length = 0;
		try {
			File textFile = new File(fileName);
			Scanner sc = new Scanner(textFile);
			while (sc.hasNextLine()) {
				sc.nextLine();
				length++;
			}
		} catch (Exception e) {

		}
		return length;
	}

	/**
	 *
	 * @param fileName is the path to the file or just the name if it is local
	 * @return an array of Strings where each string is one line from the file
	 * fileName.
	 */
	public static String[] getWordsFromFile(String fileName) {

		int lengthOfFile = getLengthOfFile(fileName);
		
		String[] wordBank=new String[lengthOfFile];
		int i = 0;
		try {
			File textFile = new File(fileName);
			Scanner sc = new Scanner(textFile);
			for (i = 0; i < lengthOfFile; i++) {
			wordBank[i] = sc.nextLine();
			}
			return wordBank;
		} catch (Exception e) {
			System.err.println(e);
		}
		return null;
	}/**
	 *
	 * @param fileName is the path to the file or just the name if it is local
	 * @return an array of Strings where each string is one line from the file
	 * fileName.
	 */
	public static String[] getWordsFromFile(File textFile){
				int lengthOfFile=0;
		try {
			lengthOfFile = getLengthOfFile(textFile.getCanonicalPath());
		} catch (IOException ex) {
			Logger.getLogger(file.class.getName()).log(Level.SEVERE, null, ex);
		}
		String[] wordBank=new String[lengthOfFile];
		int i = 0;
		try {
			Scanner sc = new Scanner(textFile);
			for (i = 0; i < lengthOfFile; i++) {
			wordBank[i] = sc.nextLine();
			}
			return wordBank;
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
		
	}
		/**
	 *
	 * @param fileName is the path to the file or just the name if it is local
	 * @return an array of Strings where each string is one line from the file
	 * fileName.
	 */
	public static char[][] getCharGridFromFile(String fileName) {
				int lengthOfFile = getLengthOfFile(fileName);
		char[][] wordBank=new char[lengthOfFile][];
		int i = 0;
		try {
			File textFile = new File(fileName);
			Scanner sc = new Scanner(textFile);
			for (i = 0; i < lengthOfFile; i++) {
			wordBank[i] = sc.nextLine().toCharArray();
			}
			return wordBank;
		} catch (Exception e) {
			System.err.println(e);
			System.exit(55);
		}
		return null;
	}

	/**
	 *
	 * @param fileName is the path to the file or just the name if it is local
	 * @return a String from file
	 */
	public static String getStringFromFile(String fileName) {
		String wordBank;
		int i = 0;
		try { 
			File textFile = new File(fileName);
			Scanner sc = new Scanner(textFile);
			wordBank = sc.nextLine();
			return wordBank;
		} catch (Exception e) {

		}
		return null;
	}

	public static double getDoubleFromFile(String fileName) {
		String wordBank;
		int i = 0;
		try { 
			File textFile = new File(fileName);
			Scanner sc = new Scanner(textFile);
			wordBank = sc.nextLine();
			return Double.parseDouble(wordBank);
		} catch (Exception e) {

		}
		return .1;
	}

	public static double getIntFromFile(String fileName) {
		String wordBank;
		int i = 0;
		try { 
			File textFile = new File(fileName);
			Scanner sc = new Scanner(textFile);
			wordBank = sc.nextLine();
			return Integer.parseInt(wordBank);
		} catch (Exception e) {

		}
		return .1;
	}

	//Pre: fileName contains the name of a txt file in current directory (folder)
	//Post: lines of text are written to fileName

	public static void writeToFile(String fileName, String stuff) {

		BufferedWriter output = null;
		try {
			File aFile = new File(fileName);
			FileWriter myWriter = new FileWriter(aFile);
			output = new BufferedWriter(myWriter);
			output.write(stuff); 
			output.close(); 
		} catch (Exception e) {

		}
	}

	public static void writeIntToFile(String fileName, int stuff) {

		BufferedWriter output = null;
		try {
			File aFile = new File(fileName);
			FileWriter myWriter = new FileWriter(aFile);
			output = new BufferedWriter(myWriter);
			output.write(stuff + "");
			output.newLine(); 
			output.close(); 
		} catch (Exception e) {

		}
	}

	public static void writeDoubleToFile(String fileName, double stuff) {

		BufferedWriter output = null;
		try {
			File aFile = new File(fileName);
			FileWriter myWriter = new FileWriter(aFile);
			output = new BufferedWriter(myWriter);
			output.write(stuff + "");
			output.newLine(); 
			output.close(); 
		} catch (Exception e) {

		}
	}

	/**
	 *
	 * @param fileName
	 * @param intArray
	 */
	public static void writeIntArrayToFile(String fileName, int... intArray) {

		BufferedWriter output = null;
		try {
			File aFile = new File(fileName);
			FileWriter myWriter = new FileWriter(aFile);
			output = new BufferedWriter(myWriter);
			for (int i = 0; i < intArray.length; i++) {
				output.write(intArray[i] + "");
				output.newLine(); 
			}
			output.close(); 
		} catch (Exception e) {

		}

	}
	
	public static void writeStringArrayToFile(String fileName, String... intArray) {

		BufferedWriter output = null;
		try {
			File aFile = new File(fileName);
			FileWriter myWriter = new FileWriter(aFile);
			output = new BufferedWriter(myWriter);
			for (String s : intArray) {
output.write(s);
				output.newLine(); 
			}
			output.close(); 
		} catch (Exception e) {

		}

	}
	public static int[] getIntArrayFromFile(String fileName) {
		String[] ints = getWordsFromFile(fileName);
		int[] integers = new int[getLengthOfFile(fileName)];
		for (int i = 0; i < ints.length; i++) {
			integers[i] = Integer.parseInt(ints[i]);
		}
		return integers;
	}
}

