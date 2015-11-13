package simpledb;
import java.util.*;
import java.io.*;

/**
 * The Join operator implements the relational join operation.
 */
public class Join extends AbstractDbIterator {

    private JoinPredicate _predicate;
    private DbIterator _outerRelation;
    private DbIterator _innerRelation;
    private Iterator<Tuple> _outerPage=null;
    private Iterator<Tuple> _innerPage=null;

    private Tuple _outerRecent=null;
    private Tuple _innerRecent=null;

    private int _joinType = 0;
    private int _numMatches =0;
    private int _numComp=0;
  
    public static final int SNL = 0;
    public static final int PNL = 1;    
    public static final int BNL = 2;    
    public static final int SMJ = 3;    
    public static final int HJ = 4;    
    /**
     * Constructor.  Accepts to children to join and the predicate
     * to join them on
     *
     * @param p The predicate to use to join the children
     * @param child1 Iterator for the left(outer) relation to join
     * @param child2 Iterator for the right(inner) relation to join
     */
    public Join(JoinPredicate p, DbIterator child1, DbIterator child2) {
        //IMPLEMENT THIS

        _predicate = p;
        _outerRelation = child1;
        _innerRelation = child2;
    }

    public void setJoinAlgorithm(int joinAlgo){
	   _joinType = joinAlgo;
    }
    /**
     * @see simpledb.TupleDesc#combine(TupleDesc, TupleDesc) for possible implementation logic.
     */
    public TupleDesc getTupleDesc() {
    	//IMPLEMENT THIS

        // create new tupledesc
        TupleDesc joinedTupleDesc = TupleDesc.combine(_outerRelation.getTupleDesc(), _innerRelation.getTupleDesc());
    	return joinedTupleDesc;
    }

    public void open()
        throws DbException, NoSuchElementException, TransactionAbortedException, IOException {
        //IMPLEMENT THIS

        _outerRelation.open();
        _innerRelation.open();
    }

    public void close() {
        //IMPLEMENT THIS

        _outerRelation.close();
        _innerRelation.close();
    }

    public void rewind() throws DbException, TransactionAbortedException, IOException {
        //IMPLEMENT THIS

        _outerRelation.rewind();
        _innerRelation.rewind();
    }

    /**
     * Returns the next tuple generated by the join, or null if there are no more tuples.
     * Logically, this is the next tuple in r1 cross r2 that satisfies the join
     * predicate.  There are many possible implementations; the simplest is a
     * nested loops join.
     * <p>
     * Note that the tuples returned from this particular implementation of
     * Join are simply the concatenation of joining tuples from the left and
     * right relation. Therefore, there will be two copies of the join attribute
     * in the results.  (Removing such duplicate columns can be done with an
     * additional projection operator if needed.)
     * <p>
     * For example, if one tuple is {1,2,3} and the other tuple is {1,5,6},
     * joined on equality of the first column, then this returns {1,2,3,1,5,6}.
     *
     * @return The next matching tuple.
     * @see JoinPredicate#filter
     */
    protected Tuple readNext() throws TransactionAbortedException, DbException {
	switch(_joinType){
	case SNL: return SNL_readNext();
	case PNL: return PNL_readNext();
	case BNL: return BNL_readNext();
	case SMJ: return SMJ_readNext();
	case HJ: return HJ_readNext();
	default: return SNL_readNext();
	}
    }

    protected Tuple SNL_readNext() throws TransactionAbortedException, DbException {
        //IMPLEMENT THIS
        try {
            // if first time enter
            if (_outerRecent == null) {
                _outerRecent = _outerRelation.next();
            }

            Tuple res = null;
            // find next join until touch the end
            while (res == null) {
                // if touch the end of inner loop
                if (!_innerRelation.hasNext()) {
                    // rewind the inner iterator
                    _innerRelation.rewind();
                    // step to the next outer tuple
                    if (!_outerRelation.hasNext()) {
                        // if touch the end of outer loop
                        return null;
                    }
                    _outerRecent = _outerRelation.next();
                }

                // now try to match
                Tuple innerTuple = _innerRelation.next();
                res = joinTuple(_outerRecent, innerTuple, getTupleDesc());
            }
            return res;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


    protected Tuple PNL_readNext() throws TransactionAbortedException, DbException {
	//IMPLEMENT THIS (EXTRA CREDIT ONLY)
	return null;
    }


    protected Tuple BNL_readNext() throws TransactionAbortedException, DbException {
	//no need to implement this
	return null;
    }


    protected Tuple SMJ_readNext() throws TransactionAbortedException, DbException {
	
	//IMPLEMENT THIS. YOU CAN ASSUME THE JOIN PREDICATE IS ALWAYS =
	return null;
    }

    protected Tuple HJ_readNext() throws TransactionAbortedException, DbException {
	//no need to implement this
	return null;
    }


    private Tuple joinTuple(Tuple outer, Tuple inner, TupleDesc tupledesc){
        //IMPLEMENT THIS

        // increment for comparison
        ++_numComp;
        // if match, return the combined tuple
        if (_predicate.filter(outer, inner)) {
            ++_numMatches;
            TupleDesc outerDesc = outer.getTupleDesc();
            TupleDesc innerDesc = inner.getTupleDesc();
            // create new tuple
            Tuple joinedTuple = new Tuple(tupledesc);
            // set the fields
            for (int i = 0; i < outerDesc.numFields(); ++i) {
                joinedTuple.setField(i, outer.getField(i));
            }
            for (int i = 0; i < innerDesc.numFields(); ++i) {
                joinedTuple.setField(i + outerDesc.numFields(), inner.getField(i));
            }
            return joinedTuple;
        }
        // return null if not match
        return null;
    }

    public int getNumMatches(){
	return _numMatches;
    }
    public int getNumComp(){
	return _numComp;
    }
}
