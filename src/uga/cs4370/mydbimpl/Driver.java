package uga.cs4370.mydbimpl;

import java.util.List;

import uga.cs4370.mydb.Relation;
import uga.cs4370.mydb.RelationBuilder;
import uga.cs4370.mydb.Type;

public class Driver {
    
    public static void main(String[] args) {
        // Following is an example of how to use the relation class.
        // This creates a table with three columns with below mentioned
        // column names and data types.
        // After creating the table, data is loaded from a CSV file.
        // Path should be replaced with a correct file path for a compatible
        // CSV file.
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

        RAImpl ra = new RAImpl();

        Relation newProjection = ra.project(studentsRel, List.of("dept_name", "name"));
        newProjection.print();
    }

}
