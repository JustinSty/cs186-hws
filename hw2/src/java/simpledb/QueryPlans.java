package simpledb;

public class QueryPlans {

	public QueryPlans(){
	}

	//SELECT * FROM T1, T2 WHERE T1.column0 = T2.column0;
	public Operator queryOne(DbIterator t1, DbIterator t2) {
		// IMPLEMENT ME
		JoinPredicate joinpred = new JoinPredicate(0, Predicate.Op.EQUALS, 0);
		Join j = new Join(joinpred, t1, t2);
		return j;
	}

	//SELECT * FROM T1, T2 WHERE T1. column0 > 1 AND T1.column1 = T2.column1;
	public Operator queryTwo(DbIterator t1, DbIterator t2) {
		// IMPLEMENT ME
		IntField i1 = new IntField(1);
		Predicate filterpre = new Predicate(0, Predicate.Op.GREATER_THAN, i1);
		Filter f1 = new Filter(filterpre, t1);
		JoinPredicate joinpred = new JoinPredicate(1, Predicate.Op.EQUALS, 1);
		Join j = new Join(joinpred, f1, t2);
		return j;
	}

	//SELECT column0, MAX(column1) FROM T1 WHERE column2 > 1 GROUP BY column0;
	public Operator queryThree(DbIterator t1) {
		// IMPLEMENT ME
		IntField i1 = new IntField(1);
		Predicate filterpre = new Predicate(2, Predicate.Op.GREATER_THAN, i1);
		Filter f1 = new Filter(filterpre, t1);
		Aggregate a1 = new Aggregate(f1, 1, 0, Aggregator.Op.MAX);
		return a1;
	}

	// SELECT ​​* FROM T1, T2
	// WHERE T1.column0 < (SELECT COUNT(*​​) FROM T3)     f1
	// AND T2.column0 = (SELECT AVG(column0) FROM T3)   f2
	// AND T1.column1 >= T2. column1
	// ORDER BY T1.column0 DESC;
	public Operator queryFour(DbIterator t1, DbIterator t2, DbIterator t3) throws TransactionAbortedException, DbException {
		// IMPLEMENT ME
		DbIterator t4 = t3;
		Aggregate c3 = new Aggregate(t3, 0, -1, Aggregator.Op.COUNT);
		c3.open();
		IntField i1 = (IntField)c3.next().getField(0);
		c3.close();
		Predicate filterpre1 = new Predicate(0, Predicate.Op.LESS_THAN, i1);
		Filter f1 = new Filter(filterpre1, t1);

		Aggregate a3 = new Aggregate(t4, 0, -1, Aggregator.Op.AVG);
		a3.open();
		IntField i2 = (IntField)a3.next().getField(0);
		a3.close();
		Predicate filterpre2 = new Predicate(0, Predicate.Op.EQUALS, i2);
		Filter f2 = new Filter(filterpre2, t2);

		JoinPredicate joinpred = new JoinPredicate(1, Predicate.Op.GREATER_THAN_OR_EQ, 1);
		Join j = new Join(joinpred, f1, f2);
		return j;
	}


}