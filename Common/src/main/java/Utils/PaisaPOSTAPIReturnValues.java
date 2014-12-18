package Utils;

/**
 * This class is defined to enable WebService.ExecutePaisaAuthPOSTWebServicesRet2Values to return 2 objects
 * @author vidya.priyadarshini
 *
 */
public class PaisaPOSTAPIReturnValues<U,V> {

		 /**
		     * The first element of this <code>PaisaPOSTAPIReturnValues</code>
		     */
		    private U first;

		    /**
		     * The second element of this <code>PaisaPOSTAPIReturnValues</code>
		     */
		    private V second;

		    /**
		     * Constructs a new <code>PaisaPOSTAPIReturnValues</code> with the given values.
		     * 
		     * @param first  the first element
		     * @param second the second element
		     */
		    public PaisaPOSTAPIReturnValues(U first, V second) {

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
