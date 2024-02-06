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
        Relation rel1 = new RelationBuilder()
                .attributeNames(List.of("Col01_Name", "Col02_Name", "Col03_Name"))
                .attributeTypes(List.of(Type.INTEGER, Type.STRING, Type.DOUBLE))
                .build();
        rel1.loadData("/path/to/exported/csv_file");
        rel1.print();
    }

}
