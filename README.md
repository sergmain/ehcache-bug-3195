repo for https://github.com/ehcache/ehcache3/issues/3195

how to run

- create temp directory i.e. /temp-dir
- run com.example.sb35718.SimpleApplication with following java options:


-ea -Xms1g -Xmx1g -Dfile.encoding=UTF-8 -Dspring.profiles.active=dispatcher,h2 -Dmh.home=/temp-dir 
