package simpledb;

import java.util.*;

/**
 * The ChunkNestedLoopJoin operator implements the chunk nested loop join operation.
 */
public class ChunkNestedLoopJoin extends Operator {

    private static final long serialVersionUID = 1L;
    private JoinPredicate pred;
    private DbIterator child1, child2;
    private TupleDesc comboTD;
    private int chunkSize;
    private Chunk currentChunk;
    private Tuple[] chunk_tuples;
    private int cur_tuple;

    /**
     * Constructor. Accepts to children to join and the predicate to join them
     * on
     * 
     * @param p
     *            The predicate to use to join the children
     * @param child1
     *            Iterator for the left(outer) relation to join
     * @param child2
     *            Iterator for the right(inner) relation to join
     * @param chunkSize
     *            The chunk size used for chunk nested loop join
     */
    public ChunkNestedLoopJoin(JoinPredicate p, DbIterator child1, DbIterator child2, int chunkSize) {
        this.pred = p;
        this.child1 = child1;
        this.child2 = child2;
        this.chunkSize = chunkSize;
        comboTD = TupleDesc.merge(child1.getTupleDesc(), child2.getTupleDesc());
        currentChunk = new Chunk(chunkSize);
        this.cur_tuple = chunkSize;

    }

    public JoinPredicate getJoinPredicate() {
        return pred;
    }

    public TupleDesc getTupleDesc() {
        return comboTD;
    }

    /**
     * Opens the iterator.
     */
    public void open() throws DbException, NoSuchElementException,
            TransactionAbortedException {
        // IMPLEMENT ME
        child1.open();
        child2.open();
        super.open();
    }

    /**
     * Closes the iterator.
     */
    public void close() {
        // IMPLEMENT ME
        super.close();
        child2.close();
        child1.close();
    }

    public void rewind() throws DbException, TransactionAbortedException {
        child1.rewind();
        child2.rewind();
    }

    /**
     * Returns the current chunk. 
     */
    public Chunk getCurrentChunk() throws DbException, TransactionAbortedException {
        // IMPLEMENT ME
        return currentChunk;
    }
 
    /**
     * Updates the current chunk with the next set of Tuples and returns the chunk.
     */
    protected Chunk fetchNextChunk() throws DbException, TransactionAbortedException {
        // IMPLEMENT ME
        currentChunk.loadChunk(child1);
        return currentChunk;
    }

    /**
     * Returns the next tuple generated by the join, or null if there are no
     * more tuples. Logically, this is the next tuple in r1 cross r2 that
     * satisfies the join predicate. 
     * 
     * Note that the tuples returned from this particular implementation
     * are simply the concatenation of joining tuples from the left and right
     * relation. Therefore, if an equality predicate is used there will be two
     * copies of the join attribute in the results.
     * 
     * For example, if one tuple is {1,2,3} and the other tuple is {1,5,6},
     * joined on equality of the first column, then this returns {1,2,3,1,5,6}.
     * 
     * @return The next matching tuple.
     * @see JoinPredicate#filter
     */
    protected Tuple fetchNext() throws TransactionAbortedException, DbException {
        // IMPLEMENT ME
        if (cur_tuple == chunkSize) {
            currentChunk = fetchNextChunk();
            chunk_tuples = currentChunk.getChunkTuples();
            cur_tuple = 0;
            }

        while (cur_tuple < chunkSize && chunk_tuples != null) {
            Tuple t1 = chunk_tuples[cur_tuple];
            //System.out.print("t1:  ");
            //System.out.println(t1);
            while (child2.hasNext()) {
                Tuple t2 = child2.next();
                
                if (!pred.filter(t1, t2))
                    continue;

                int td1n = t1.getTupleDesc().numFields();
                int td2n = t2.getTupleDesc().numFields();

                // set fields in combined tuple
                Tuple t = new Tuple(comboTD);
                for (int i = 0; i < td1n; i++)
                    t.setField(i, t1.getField(i));
                for (int i = 0; i < td2n; i++)
                    t.setField(td1n + i, t2.getField(i));
                return t;
            }
            child2.rewind();
            cur_tuple++;

            if (cur_tuple == chunkSize) {
                currentChunk = fetchNextChunk();
                chunk_tuples = currentChunk.getChunkTuples();
                cur_tuple = 0;
            }
        }

        return null;
    }

    @Override
    public DbIterator[] getChildren() {
        return new DbIterator[] { this.child1, this.child2 };
    }

    @Override
    public void setChildren(DbIterator[] children) {
        this.child1 = children[0];
        this.child2 = children[1];
    }

}
