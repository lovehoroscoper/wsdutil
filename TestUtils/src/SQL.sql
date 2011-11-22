-select trunc(sysdate,'DD'), to_char(sysdate,'D')  ,trunc(sysdate,'DD')-to_char(sysdate,'D')+ 2 - 7 from dual;


select trunc( to_date('2011-11-08','yyyy-mm-dd'),'DD')-to_char( to_date('2011-11-08','yyyy-mm-dd'),'D')+ 2 - 7 from dual;

select trunc( to_date('2011-11-08','yyyy-mm-dd'),'DD')-to_char( to_date('2011-11-08','yyyy-mm-dd'),'D')+ 2 - 1 from dual;