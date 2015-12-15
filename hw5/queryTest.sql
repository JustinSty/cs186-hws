/* Test Rollback and Recovery Here */

set transaction read write;

INSERT INTO data (f1, f2)
VALUES (6, 60);
### flush; 
rollback;

/* update table and end transaction above */

select * from data;
select * from s;