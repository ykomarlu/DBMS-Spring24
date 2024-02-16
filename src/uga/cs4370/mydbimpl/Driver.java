package uga.cs4370.mydbimpl;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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

        Relation instructorRel = new RelationBuilder()
        .attributeNames(List.of("ID", "name", "dept_name", "salary"))
        .attributeTypes(List.of(Type.INTEGER, Type.STRING, Type.STRING, Type.DOUBLE))
        .build();

        instructorRel.loadData("./resources/instructor_export.csv");

        Relation studentsRel = new RelationBuilder()
        .attributeNames(List.of("ID", "name", "dept_name", "tot_cred"))
        .attributeTypes(List.of(Type.INTEGER, Type.STRING, Type.STRING, Type.INTEGER))
        .build();

        studentsRel.loadData("./resources/student_export.csv");

        Relation projectedRelationInstructor = ra.project(instructorRel, List.of("dept_name", "salary"));
        Relation projectedRelationStudent = ra.project(studentsRel, List.of("name", "dept_name", "tot_cred"));

        // ra.print50(projectedRelationInstructor);
        // ra.print50(projectedRelationStudent);

        Relation naturalJoinRelation = ra.join(projectedRelationInstructor, projectedRelationStudent);

        ra.print50(naturalJoinRelation);
    }

}
