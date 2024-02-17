package uga.cs4370.mydbimpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uga.cs4370.mydb.Cell;
import uga.cs4370.mydb.Relation;
import uga.cs4370.mydb.RelationBuilder;
import uga.cs4370.mydb.Type;

public class Driver {
    public static void main(String[] args) {

        RAImpl ra = new RAImpl();

        // Advisor Relation
        Relation advisorRel = new RelationBuilder()
        .attributeNames(List.of("stu_id", "instr_id"))
        .attributeTypes(List.of(Type.INTEGER, Type.INTEGER))
        .build();

        advisorRel.loadData("./resources/advisor_export.csv");

        // Instructor Relation
        Relation instructorRel = new RelationBuilder()
        .attributeNames(List.of("instr_id", "name", "dept_name", "salary"))
        .attributeTypes(List.of(Type.INTEGER, Type.STRING, Type.STRING, Type.DOUBLE))
        .build();

        instructorRel.loadData("./resources/instructor_export.csv");

        // Student Relation
        Relation studentsRel = new RelationBuilder()
        .attributeNames(List.of("stu_id", "name", "dept_name", "tot_cred"))
        .attributeTypes(List.of(Type.INTEGER, Type.STRING, Type.STRING, Type.INTEGER))
        .build();

        studentsRel.loadData("./resources/student_export.csv");

        // Takes Relation
        Relation takesRel = new RelationBuilder()
        .attributeNames(List.of("stu_id", "course_id", "sec_id", "semester", "year", "point_grade"))
        .attributeTypes(List.of(Type.INTEGER, Type.INTEGER, Type.INTEGER, Type.STRING, Type.INTEGER, Type.STRING))
        .build();

        takesRel.loadData("./resources/takes_export.csv");



        Set<Integer> instructorIDs = new HashSet<>();

        for (int i = 0; i < instructorRel.getSize(); i++) {
            List<Cell> row = instructorRel.getRow(i);
            int id = row.get(0).getAsInt(); 
            instructorIDs.add(id);
        }

        Relation advisorsNotInstructors = new RelationBuilder()
            .attributeNames(advisorRel.getAttrs())
            .attributeTypes(advisorRel.getTypes())
            .build();

        for (int i = 0; i < advisorRel.getSize(); i++) {
            List<Cell> row = advisorRel.getRow(i);
            int advisorID = row.get(1).getAsInt();
            if (!instructorIDs.contains(advisorID)) {
                advisorsNotInstructors.insert(new ArrayList<>(row));
            }
        }

        System.out.println("Prints all Advisors who are not instructors (should be empty)");
        ra.print50(advisorsNotInstructors);



        Relation q2ProjectedHistory = ra.select(studentsRel, row -> row.get(studentsRel.getAttrIndex("dept_name")).equals(Cell.val("History")));
        Relation q2ProjectedMarketing = ra.select(studentsRel, row -> row.get(studentsRel.getAttrIndex("dept_name")).equals(Cell.val("Marketing")));
        Relation query2 = ra.union(q2ProjectedHistory, q2ProjectedMarketing);

        System.out.println("\n\nUnion of Students in the History and Marketing departments");
        ra.print50(query2);



        Relation q3ProjectedStu = ra.project(studentsRel, List.of("name", "dept_name"));
        Relation q3ProjectedIns = ra.project(instructorRel, List.of("name", "dept_name"));
        Relation query3 = ra.union(q3ProjectedStu, q3ProjectedIns);

        System.out.println("\n\nName, Dept_Name of Student U Instructor");
        ra.print50(query3);



        Relation q4ProjectedStu = ra.project(studentsRel, List.of("stu_id", "tot_cred"));
        Relation q4ProjectedIns = ra.project(instructorRel, List.of("instr_id", "salary"));
        Relation query4 = ra.cartesianProduct(q4ProjectedStu, q4ProjectedIns);

        System.out.println("\n\nCartesian Product of Instructors and Students when they're projected to not have Name and Dept_Name");
        ra.print50(query4);



        Relation studentCredsAbove60 = ra.select(studentsRel, row -> row.get(studentsRel.getAttrIndex("tot_cred")).getAsInt() > 60);
        Relation takesStuCredNaturalJoin = ra.join(studentCredsAbove60, takesRel);
        Relation query5 = ra.project(takesStuCredNaturalJoin, List.of("name", "semester", "course_id", "year", "point_grade"));

        System.out.println("\n\nName, Semester, Course_ID, Year, Grade for students whos Tot_Cred are greater than 60");
        ra.print50(query5);



    }

}
