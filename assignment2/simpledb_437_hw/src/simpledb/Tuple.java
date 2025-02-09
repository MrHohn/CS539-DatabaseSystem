package simpledb;

/**
 * Tuple maintains information about the contents of a tuple.
 * Tuples have a specified schema specified by a TupleDesc object and contain
 * Field objects with the data for each field.
 */
public class Tuple {
    private Field tupleFields[];
    private TupleDesc tupled;
    private RecordID tuplerid;

    /**
     * Create a new tuple with the specified schema (type).
     *
     * @param td the schema of this tuple. It must be a valid TupleDesc
     * instance with at least one field.
     */
    public Tuple(TupleDesc td) {
	this.tupled = td;
	this.tuplerid=null;
	tupleFields = new Field[td.numFields()];
	for (int i=0; i<tupled.numFields();i++)
	    tupleFields[i]=null;
    }        


    /**
     * @return The TupleDesc representing the schema of this tuple.
     */
    public TupleDesc getTupleDesc() {
         return this.tupled;
    }

    /**
     * @return The RecordID representing the location of this tuple on
     *   disk. May be null.
     */
    public RecordID getRecordID() {
        return this.tuplerid;
    }

    /**
     * Set the RecordID information for this tuple.
     * @param rid the new RecordID for this tuple.
     */
    public void setRecordID(RecordID rid) {
	this.tuplerid=rid;
    }

    /**
     * Change the value of the ith field of this tuple.
     *
     * @param i index of the field to change. It must be a valid index.
     * @param f new value for the field.
     */
    public void setField(int i, Field f) {
	this.tupleFields[i]=f;
    }

    /**
     * @return the value of the ith field, or null if it has not been set.
     *
     * @param i field index to return. Must be a valid index.
     */
    public Field getField(int i) {
	if (tupleFields[i]==null)
	    return null;
	else return tupleFields[i];
    }

    /**
     * Returns the contents of this Tuple as a string.
     * Note that to pass the system tests, the format needs to be as
     * follows:
     *
     * column1\tcolumn2\tcolumn3\t...\tcolumnN\n
     *
     * where \t is any whitespace, except newline, and \n is a newline
     */
    public String toString() {
        // some code goes here
        // throw new UnsupportedOperationException("Implement this");

        StringBuilder res = new StringBuilder();
        for (int i = 0; i < tupled.numFields(); ++i) {
            res.append(tupleFields[i].toString());
            res.append(" ");
        }
        return res.toString();
    }
}
