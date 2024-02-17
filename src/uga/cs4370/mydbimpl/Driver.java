package uga.cs4370.mydbimpl;

import java.util.List;

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


        
        Relation q1GreaterThan50k = ra.select(instructorRel, row -> row.get(instructorRel.getAttrIndex("salary")).getAsDouble() > 50000);
        Relation q1InCSDept = ra.select(instructorRel, row -> row.get(instructorRel.getAttrIndex("dept_name")).getAsString() == "Comp. Sci.");
        Relation q1Diff50kCS = ra.diff(q1GreaterThan50k, q1InCSDept);
        Relation query1 = ra.join(q1Diff50kCS, advisorRel);

        System.out.println("\n\nPrints the difference between Instructors with a Salary greater than 50k and Instructors in Comp. Sci. That result is then natural joined with advisors");
        ra.print50(query1);



        Relation q2ProjectedHistory = ra.select(studentsRel, row -> row.get(studentsRel.getAttrIndex("dept_name")).equals(Cell.val("History")));
        Relation q2ProjectedMarketing = ra.select(studentsRel, row -> row.get(studentsRel.getAttrIndex("dept_name")).equals(Cell.val("Marketing")));
        Relation unionOfProject = ra.union(q2ProjectedHistory, q2ProjectedMarketing);
        Relation selectedTakes = ra.project(takesRel, List.of("semester","year"));
        Relation query2 = ra.cartesianProduct(unionOfProject, selectedTakes);

        System.out.println("\n\nUnion of Students in the History and Marketing departments with Cartesian of Semester, Year from Takes");
        ra.print50(query2);



        Relation q3ProjectedStu = ra.project(studentsRel, List.of("name", "dept_name"));
        Relation q3ProjectedIns = ra.project(instructorRel, List.of("name", "dept_name"));
        Relation q3Union = ra.union(q3ProjectedStu, q3ProjectedIns);
        Relation query3 = ra.rename(q3Union, q3Union.getAttrs(), List.of("First Name", "Department"));

        System.out.println("\n\nName, Dept_Name of Student U Instructor, which is renamed to be First Name and Department");
        ra.print50(query3);



        Relation q4ProjectedStu = ra.project(studentsRel, List.of("stu_id", "tot_cred"));
        Relation q4ProjectedIns = ra.project(instructorRel, List.of("instr_id", "salary"));
        Relation q4StuSel = ra.select(q4ProjectedStu, row -> row.get(studentRelation.getAttrIndex("stu_id")).equals(Cell.val(1000)));
        Relation query4 = ra.cartesianProduct(q4ProjectedStu, q4ProjectedIns);

        System.out.println("\n\nCartesian Product of Instructors and Students with an id of 1000 when they're projected to not have Name and Dept_Name");
        ra.print50(query4);



        Relation studentCredsAbove60 = ra.select(studentsRel, row -> row.get(studentsRel.getAttrIndex("tot_cred")).getAsInt() > 60);
        Relation takesStuCredNaturalJoin = ra.join(studentCredsAbove60, takesRel);
        Relation query5 = ra.project(takesStuCredNaturalJoin, List.of("name", "semester", "course_id", "year", "point_grade"));

        System.out.println("\n\nName, Semester, Course_ID, Year, Grade for students whos Tot_Cred are greater than 60");
        ra.print50(query5);



    }

}
