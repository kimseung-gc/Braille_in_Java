import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystemNotFoundException;

/**
 * A class where it utilizes the BitTree that I have created
 * that converts the inputs to desired output depending on
 * the function
 * 
 * @author Seunghyeon (Hyeon) Kim
 * @version 0.0.3
 */
public class BrailleASCIITables {
  /**
   * integer of hex (16)
   */
  final int HEX = 16;
  /**
   * input length of the braille input
   */
  final int BRAILLEINPLEN = 6;
  /**
   * input length of the ASCII input
   */
  final int ASCIIINPLEN = 8;

  // +--------------+------------------------------------------------
  // | Main Methods |
  // +--------------+
  /**
   * This function returns the whole string into braille (translating
   * each of the characters of the input string)
   * 
   * @param inpString
   * @return returns the Braille version of string
   * @throws FileSystemNotFoundException
   * @throws IOException
   */
  public String stringToBraille(String inpString) throws FileSystemNotFoundException, IOException {
    // create an empty string that will be returned
    String resultingBraille = "";
    for (char eachChar : inpString.toCharArray()) {
      // apply toBraille function to each of the characters in the input string
      resultingBraille += toBraille(eachChar);
    } // for
    // return the resultant braille structure
    return resultingBraille;
  } // stringToBraille (String)

  /**
   * returns the ASCII version of the input string version of the braille.
   * 
   * @param inpString
   * @throws FileSystemNotFoundException
   * @throws IOException
   */
  public String stringToASCII(String inpString)
      throws IllegalArgumentException, FileSystemNotFoundException, IOException {
    // When invalid input
    if (inpString.length() % BRAILLEINPLEN != 0) {
      // It means the input string is incomplete, so throw an error.
      throw new IllegalArgumentException("The input is an incomplete braille or invalid input");
    } // if
    // declare an empty string that will be returned
    String resultingASCII = "";
    // split every 6 indices of the string
    String[] temp = splitEveryN(inpString, BRAILLEINPLEN);
    for (String eachBraille : temp) {
      // apply toASCII function to each of the braille characters and add them
      resultingASCII += toASCII(eachBraille);
    } // for
    // return the ASCII
    return resultingASCII;
  } // stringToASCII(String)

  /**
   * returns the unicode version of the input string version of the braille.
   * 
   * @param inpString
   * @throws FileSystemNotFoundException
   * @throws IOException
   */
  public String stringToUniCode(String inpString) throws FileSystemNotFoundException, IOException {
    // When invalid input
    if (inpString.length() % BRAILLEINPLEN != 0) {
      // It means the input string is incomplete, so throw an error.
      throw new IllegalArgumentException("The input is an incomplete braille or invalid input");
    } // if
    // declare an empty string that will be returned
    String resultingASCII = "";
    // split every 6 indices of the string
    String[] temp = splitEveryN(inpString, BRAILLEINPLEN);
    for (String eachBraille : temp) {
      // convert each braille character to the desired unicode character, and
      // concatenate it with the unicode character
      resultingASCII += toUnicode(eachBraille);
    } // for
    // Return the unicode characters
    return resultingASCII;
  } // stringToUniCode(String)

  // +---------+-----------------------------------------------------
  // | Methods |
  // +---------+
  /**
   * The function returns the braille version of the given character
   * 
   * @param letter
   * @return The braille version of the ASCII character
   * @throws FileSystemNotFoundException
   * @throws IOException
   */
  public String toBraille(char letter) throws FileSystemNotFoundException, IOException, IllegalArgumentException {
    BitTree temp = new BitTree(ASCIIINPLEN);
    InputStream tempInp;
    try {
      tempInp = new FileInputStream("./ASCIIToBraille.txt");
    } catch (Exception e) {
      throw new FileSystemNotFoundException("The txt file for ASCII to Braille is not found.\n"
          + "Perhaps, you did not download ASCIIToBraille.txt or not in the same path?");
    } // try/catch
    temp.load(tempInp);
    int ASCIIChar = (int) letter;
    String ret;
    try {
      ret = temp.get(Integer.toBinaryString(ASCIIChar));
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Cannot find \'" + letter + "\'");
    } // try/catch
    return ret;
  } // toBraille(char)

  /**
   * The function returns the ASCII version of the input of 6 bits.
   * 
   * @param bits
   * @return The ASCII version of the Braille
   * @throws FileSystemNotFoundException
   * @throws IOException
   */
  public String toASCII(String bits) throws FileSystemNotFoundException, IOException {
    BitTree temp = new BitTree(BRAILLEINPLEN);
    InputStream tempInp;
    try {
      tempInp = new FileInputStream("./BrailleToASCII.txt");
    } catch (Exception e) {
      throw new FileSystemNotFoundException("The txt file for Braille to ASCII is not found.\n"
          + "Perhaps, you did not download BrailleToASCII.txt or not in the same path?");
    } // try/catch
    temp.load(tempInp);
    return temp.get(bits);
  } // toASCII(String)

  /**
   * The function takes a set of 6 bits as the input and returns the unicode
   * associated with
   * the set of bits (single set of 6 bits).
   * 
   * @param bits
   * @return the unicode version of the braille (bits)
   * @throws FileSystemNotFoundException
   * @throws IOException
   */
  public String toUnicode(String bits) throws FileSystemNotFoundException, IOException {
    BitTree temp = new BitTree(BRAILLEINPLEN);
    InputStream tempInp;
    try {
      tempInp = new FileInputStream("./BrailleToUnicode.txt");
    } catch (Exception e) {
      throw new FileSystemNotFoundException("The txt file for Braille to Unicode is not found.\n"
          + "Perhaps, you did not download BrailleToUnicode.txt or not in the same path?");
    } // try/catch
    temp.load(tempInp);
    String resultingCode = temp.get(bits);
    char ret = (char) Integer.parseInt(resultingCode, HEX);
    return "" + ret;
  } // toASCII(String)

  // +---------+-----------------------------------------------------
  // | Helpers |
  // +---------+

  /**
   * It returns the string array that splits the input string by length n.
   * 
   * @param inpString
   * @param n
   */
  private String[] splitEveryN(String inpString, int n) {
    String[] ret = new String[(int) (inpString.length() / n)];
    int i;
    for (i = n; i < inpString.length(); i += n) {
      ret[(int) ((i - n) / n)] = inpString.substring(i - n, i);
    } // for
    ret[(int) (inpString.length() / n) - 1] = inpString.substring(i - n, inpString.length());
    return ret;
  } // splitEveryN(String, int)
} // class BrailleASCIITables
