package uga.cs4370.mydbimpl;

import java.util.Arrays;
import uga.cs4370.mydb.Relation;
import uga.cs4370.mydb.RelationBuilder;
import uga.cs4370.mydbimpl.RAImpl;


import java.util.Arrays;
import uga.cs4370.mydb.Relation;
import uga.cs4370.mydb.RelationBuilder;
import uga.cs4370.mydb.Type;
import uga.cs4370.mydb.Cell;

public class Driver {


    // Following is an example of how to use the relation class.
    // This creates a table with three columns with below mentioned
    // column names and data types.
    // After creating the table, data is loaded from a CSV file.
    // Path should be replaced with a correct file path for a compatible
    // CSV file.
    /**   Relation instructorRelation = new RelationBuilder()
     .attributeNames(List.of("ID", "Name", "dept_name","Salary"))
     .attributeTypes(List.of(Type.INTEGER, Type.STRING, Type.STRING,Type.DOUBLE))
     .build();
     // Load the instructor CSV file
     instructorRelation.loadData("/Users/zhuohongwu/4370_file/mysql-files/instructor_export.csv");
     // Print your myid and the instructor table
     System.out.println("zw93090");
     instructorRelation.print();

     // Assuming another table, repeat the process
     // Initialize the relation for the second table (replace with actual table details)
     Relation secondTableRelation = new RelationBuilder()
     .attributeNames(List.of("s_ID", "i_ID")) // Update with actual column names
     .attributeTypes(List.of(Type.INTEGER, Type.INTEGER))// Update with actual data types
     .build();
     // Load the CSV file for the second table
     secondTableRelation.loadData("/Users/zhuohongwu/4370_file/mysql-files/advisor_export.csv");
     // Print your myid and the second table
     System.out.println("zw93090");
     secondTableRelation.print();
     */
    public static void main(String[] args) {
        // Initialize the relation for the instructor table
   /* Relation instructorRelation = new RelationBuilder()
        .attributeNames(Arrays.asList("ID", "Name", "dept_name", "Salary"))
        .attributeTypes(Arrays.asList(Type.INTEGER, Type.STRING, Type.STRING, Type.DOUBLE))
        .build();
    // Load the instructor CSV file
    instructorRelation.loadData("/path/to/instructor_export.csv");
    // Print the original instructor table
    System.out.println("Original Instructor Table:");
    instructorRelation.print();

    // Rename attributes in the instructorRelation
    RAImpl ra = new RAImpl();
    Relation renamedInstructorRelation = ra.rename(instructorRelation, Arrays.asList("dept_name"), Arrays.asList("Department"));
    // Print the renamed instructor table
    System.out.println("Renamed Instructor Table:");
    renamedInstructorRelation.print();

    // Initialize the relation for the second table (advisor table)
    Relation secondTableRelation = new RelationBuilder()
        .attributeNames(Arrays.asList("s_ID", "i_ID"))
        .attributeTypes(Arrays.asList(Type.INTEGER, Type.INTEGER))
        .build();
    // Load the CSV file for the second table
    secondTableRelation.loadData("/path/to/advisor_export.csv");
    // Print the original second table
    System.out.println("Original Advisor Table:");
    secondTableRelation.print();

    // Rename attributes in the secondTableRelation
    Relation renamedSecondTableRelation = ra.rename(secondTableRelation, Arrays.asList("s_ID"), Arrays.asList("StudentID"));
    // Print the renamed second table
    System.out.println("Renamed Advisor Table:");
    renamedSecondTableRelation.print();

    */

        //the rename test

        /**
         RAImpl ra = new RAImpl();

         // Create a dummy relation with attributes "ID", "Name", "Dept", and "Salary"
         RelationBuilder builder = new RelationBuilder();
         builder.attributeNames(Arrays.asList("ID", "Name", "Dept", "Salary"));
         builder.attributeTypes(Arrays.asList(Type.INTEGER, Type.STRING, Type.STRING, Type.DOUBLE));
         Relation instructorRelation = builder.build();

         // Assuming the insert method and Cell.val static methods are correctly implemented
         // Insert dummy data into the instructorRelation
         // Example of inserting data: ID=1, Name="Alice", Dept="CS", Salary=50000.0
         instructorRelation.insert(Arrays.asList(Cell.val(1), Cell.val("Wu"), Cell.val("Bio"), Cell.val(6900.00)));
         instructorRelation.insert(Arrays.asList(Cell.val(2), Cell.val("Z"), Cell.val("Econ"), Cell.val(6900.00)));

         // Print the original relation for comparison
         System.out.println("Original Relation:");
         instructorRelation.print(); // Ensure your Relation class has a print method

         // Perform the rename operation to change "Dept" to "Department"
         Relation renamedRelation = ra.rename(instructorRelation, Arrays.asList("Dept"), Arrays.asList("Department"));

         // Print the renamed relation to verify the results
         System.out.println("Renamed Relation:");
         renamedRelation.print(); // Verify that "Dept" is now "Department"



         */


// test the diff methods 
        RAImpl ra = new RAImpl();

        // Create the first dummy relation (relationA) with some attributes and data
        RelationBuilder builderA = new RelationBuilder()
                .attributeNames(Arrays.asList("ID", "Name", "Dept", "Salary"))
                .attributeTypes(Arrays.asList(Type.INTEGER, Type.STRING, Type.STRING, Type.DOUBLE));
        Relation relationA = builderA.build();

        // Insert data into relationA
        relationA.insert(Arrays.asList(Cell.val(1), Cell.val("Asen"), Cell.val("CS"), Cell.val(1820000.0)));
        relationA.insert(Arrays.asList(Cell.val(2), Cell.val("Bsen"), Cell.val("Math"), Cell.val(525000.0)));
        relationA.insert(Arrays.asList(Cell.val(3), Cell.val("Csen"), Cell.val("Bio"), Cell.val(1230000.0)));

        // Create the second dummy relation (relationB) with the same schema but different data
        RelationBuilder builderB = new RelationBuilder()
                .attributeNames(Arrays.asList("ID1", "Name1", "Salary1"))
//                .attributeNames(Arrays.asList("ID1", "Name1", "Dept1", "Salary1"))
                .attributeTypes(Arrays.asList(Type.INTEGER, Type.STRING, Type.DOUBLE));
        Relation relationB = builderB.build();

        // Insert data into relationB
//        relationB.insert(Arrays.asList(Cell.val(1), Cell.val("Asen"), Cell.val("CS"), Cell.val(1820000.0)));
//        relationB.insert(Arrays.asList(Cell.val(2), Cell.val("Bsen"), Cell.val("Math"), Cell.val(525000.0)));
//        relationB.insert(Arrays.asList(Cell.val(3), Cell.val("Dsen"), Cell.val("CS"), Cell.val(622000.0)));
        relationB.insert(Arrays.asList(Cell.val(1), Cell.val("Asen"), Cell.val(1820000.0)));
        relationB.insert(Arrays.asList(Cell.val(2), Cell.val("Bsen"), Cell.val(525000.0)));
        relationB.insert(Arrays.asList(Cell.val(3), Cell.val("Dsen"), Cell.val(622000.0)));

        // Print the original relations for comparison
        System.out.println("Relation A:");
        relationA.print();
        System.out.println("Relation B:");
        relationB.print();

        // Perform the diff operation to get rows in A that are not in B
        Relation diffRelation = ra.union(relationA, relationB);

        // Print the result of the diff operation
        System.out.println("Result of A X B:");
        diffRelation.print();
    }

}