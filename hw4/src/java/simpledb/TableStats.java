package simpledb;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TableStats represents statistics (e.g., histograms) about base tables in a
 * query. 
 */
public class TableStats {

    /**
     * These static variables and methods helps the database access TableStats
     * for any given table.
     */
    private static final ConcurrentHashMap<String, TableStats> statsMap = new ConcurrentHashMap<String, TableStats>();

    static final int IOCOSTPERPAGE = 1000;

    public static TableStats getTableStats(String tablename) {
        return statsMap.get(tablename);
    }

    public static void setTableStats(String tablename, TableStats stats) {
        statsMap.put(tablename, stats);
    }
    
    public static void setStatsMap(HashMap<String,TableStats> s) {
        try {
            java.lang.reflect.Field statsMapF = TableStats.class.getDeclaredField("statsMap");
            statsMapF.setAccessible(true);
            statsMapF.set(null, s);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public static Map<String, TableStats> getStatsMap() {
        return statsMap;
    }

    public static void computeStatistics() {
        Iterator<Integer> tableIt = Database.getCatalog().tableIdIterator();

        System.out.println("Computing table stats.");
        while (tableIt.hasNext()) {
            int tableid = tableIt.next();
            TableStats s = new TableStats(tableid, IOCOSTPERPAGE);
            setTableStats(Database.getCatalog().getTableName(tableid), s);
        }
        System.out.println("Done.");
    }

    static final int NUM_HIST_BINS = 100;
    private final int ioCostPerPage;
    private final TupleDesc tupleDesc;
    private int numPages, numTuples;

    private IntStatistics[] intStats;
    private StringHistogram[] strHists;

    // TODO: add any fields that you may need

    /**
     * Create a new TableStats object, that keeps track of statistics on each
     * column of a table
     * 
     * @param tableid
     *            The table over which to compute statistics
     * @param ioCostPerPage
     *            The cost per page of IO. This doesn't differentiate between
     *            sequential-scan IO and disk seeks.
     */
    public TableStats(int tableid, int ioCostPerPage) {
        // For this function, we use the DbFile for the table in question,
        // then scan through its tuples and calculate the values that you
        // to build the histograms.

        // TODO: Fill out the rest of the constructor.
        // Feel free to change anything already written, it's only a guideline

        this.ioCostPerPage = ioCostPerPage;
        DbFile file = Database.getCatalog().getDbFile(tableid);
        tupleDesc = file.getTupleDesc();
        numPages = ((HeapFile) file).numPages();
        numTuples = 0;

        int numFields = tupleDesc.numFields();

        // TODO: what goes here?
        int[] mins = new int[numFields];
        int[] maxs = new int[numFields];
        intStats = new IntStatistics[numFields];
        strHists = new StringHistogram[numFields];

        final DbFileIterator iter = file.iterator(null);
        try {
            iter.open();

            for (int i = 0; i < numFields; i++) {
                Type type = tupleDesc.getFieldType(i);
                if (type == Type.INT_TYPE) {
                    intStats[i] = new IntStatistics(NUM_HIST_BINS);
                }
                else{
                    strHists[i] = new StringHistogram(NUM_HIST_BINS);
                }
            }

            while (iter.hasNext()) {
                Tuple t = iter.next();
                numTuples++;
                // TODO: and here?
                for (int i = 0; i<numFields; i++){
                    Type type = tupleDesc.getFieldType(i);
                    Field field = t.getField(i);
                    if (type == Type.INT_TYPE){
                        maxs[i] = Math.max(maxs[i], ((IntField) field).getValue());
                        mins[i] = Math.min(mins[i], ((IntField) field).getValue());
                        intStats[i].addValue(((IntField) t.getField(i)).getValue());
                    } else if (type == Type.STRING_TYPE) {
                        strHists[i].addValue(((StringField) t.getField(i)).getValue());
                    }
                }
            }

        } catch (DbException e) {
            e.printStackTrace();
        } catch (TransactionAbortedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Estimates the cost of sequentially scanning the file, given that the cost
     * to read a page is ioCostPerPage. You can assume that there are no seeks
     * and that no pages are in the buffer pool.
     * 
     * Also, assume that your hard drive can only read entire pages at once, so
     * if the last page of the table only has one tuple on it, it's just as
     * expensive to read as a full page. (Most real hard drives can't
     * efficiently address regions smaller than a page at a time.)
     * 
     * @return The estimated cost of scanning the table.
     */
    public double estimateScanCost() {
        // TODO: some code goes here
        return ioCostPerPage * numPages;
    }

    /**
     * This method returns the number of tuples in the relation, given that a
     * predicate with selectivity selectivityFactor is applied.
     * 
     * @param selectivityFactor
     *            The selectivity of any predicates over the table
     * @return The estimated cardinality of the scan with the specified
     *         selectivityFactor
     */
    public int estimateTableCardinality(double selectivityFactor) {
        // TODO: some code goes here
        return (int) Math.ceil(numTuples * selectivityFactor);
    }

    /**
     * Estimate the selectivity of predicate <tt>field op constant</tt> on the
     * table.
     * 
     * @param field
     *            The field over which the predicate ranges
     * @param op
     *            The logical operation in the predicate
     * @param constant
     *            The value against which the field is compared
     * @return The estimated selectivity (fraction of tuples that satisfy) the
     *         predicate
     */
    public double estimateSelectivity(int field, Predicate.Op op, Field constant) {
        // TODO: some code goes here
        Type type = tupleDesc.getFieldType(field);

        if(type == Type.INT_TYPE) {
            //System.out.println("file"+((IntStatistics) intStats[field]).estimateSelectivity(op, ((IntField) constant).getValue()));
            return ((IntStatistics) intStats[field]).estimateSelectivity(op, ((IntField) constant).getValue());
        } 
        else if(type == Type.STRING_TYPE) {
            return ((StringHistogram) strHists[field]).estimateSelectivity(op, ((StringField) constant).getValue());
        } 
        else {
            return 0.0;
        }
    }

    /**
     * The average selectivity of the field under op.
     * @param field
     *        the index of the field
     * @param op
     *        the operator in the predicate
     * The semantic of the method is that, given the table, and then given a
     * tuple, of which we do not know the value of the field, return the
     * expected selectivity. You may estimate this value from the histograms.
     * */
    public double avgSelectivity(int field, Predicate.Op op) {
        // optional: implement for a more nuanced estimation, or for skillz
        return 1.0;
    }

}