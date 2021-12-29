```java
select * from A where id in(select id from B)
    
    
select a.* from A a where exists(select 1 from B b where a.id=b.id)
    
    
```

exists  优于in的情况



In 优于exists的情况



