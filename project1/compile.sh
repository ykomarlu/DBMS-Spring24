javac -d bin -cp bin src/uga/cs4370/mydb/*.java

javac -d bin -cp bin src/uga/cs4370/mydbimpl/RAImpl.java
javac -d bin -cp bin src/uga/cs4370/mydbimpl/Driver.java
java -cp bin uga.cs4370.mydbimpl.Driver