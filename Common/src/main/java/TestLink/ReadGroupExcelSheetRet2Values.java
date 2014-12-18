package TestLink;
/**
 * This class is defined to enable GenerateTestNGXmlAndRun.readGroupNamesExcelSheet to return 2 objects
 * @author vidya.priyadarshini
 *
 */
public class ReadGroupExcelSheetRet2Values<U,V> {


		 /**
		     * The first element of this <code>readGroupNamesExcelSheet</code>
		     */
		    private U first;

		    /**
		     * The second element of this <code>readGroupNamesExcelSheet</code>
		     */
		    private V second;

		    /**
		     * Constructs a new <code>readGroupNamesExcelSheet</code> with the given values.
		     * 
		     * @param first  the first element
		     * @param second the second element
		     */
		    public ReadGroupExcelSheetRet2Values(U first, V second) {

		        this.first = first;
		        this.second = second;
		    }

		//getter for first and second
		    public U first() {
		        return first;
		    }

		    public V second() {
		        return second;
		    }
}

