package uga.cs4370.mydbimpl;

import java.util.ArrayList;
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
        Relation q1InsJoin = ra.join(q1Diff50kCS, advisorRel);
        Relation q1SmallStuID = ra.select(q1InsJoin, row -> row.get(q1InsJoin.getAttrIndex("stu_id")).getAsInt() < 20000);

        Relation query1 = ra.select(q1SmallStuID, row -> row.get(q1SmallStuID.getAttrIndex("name")).getAsString().contains("s"));

        System.out.println("\n\nPrints the difference between Instructors with a Salary greater than 50k and Instructors in Comp. Sci. That result is then natural joined with advisors. We then select all Stu_IDs smaller than 20k and names that contain the letter S.");
        ra.print50(query1);



        Relation q2ProjectedHistory = ra.select(studentsRel, row -> row.get(studentsRel.getAttrIndex("dept_name")).equals(Cell.val("Athletics")));
        Relation q2LessCredits = ra.select(q2ProjectedHistory, row -> row.get(q2ProjectedHistory.getAttrIndex("tot_cred")).getAsInt() < 22);
        Relation q2ProjectedMarketing = ra.select(studentsRel, row -> row.get(studentsRel.getAttrIndex("dept_name")).equals(Cell.val("Marketing")));
        Relation q2MoreCredits = ra.select(q2ProjectedMarketing, row -> row.get(q2ProjectedMarketing.getAttrIndex("tot_cred")).getAsInt() > 100);
        Relation unionOfProject = ra.union(q2LessCredits, q2MoreCredits);
        Relation q2projectedIns = ra.project(instructorRel, List.of("dept_name", "salary"));

        Relation query2 = ra.join(unionOfProject, q2projectedIns);

        System.out.println("\n\nThe Union of Students who are in Athletics with less than 22 credits and Students in Marketing with more than 100 credits. This is then natural joined with Instructors, which has been projected to only display Dept_Name and Salary");
        query2.print();



        Relation q3ProjectedStu = ra.project(studentsRel, List.of("name", "dept_name"));
        Relation q3ProjectedIns = ra.project(instructorRel, List.of("name", "dept_name"));
        Relation q3Union = ra.union(q3ProjectedStu, q3ProjectedIns);
        Relation q3Rename = ra.rename(q3Union, q3Union.getAttrs(), List.of("First Name", "Department"));

        Relation query3 = ra.select(q3Rename, row -> row.get(q3Rename.getAttrIndex("First Name")).getAsString().length() > 8);

        System.out.println("\n\nName, Dept_Name of Student U Instructor, which is renamed to be First Name and Department");
        query3.print();



        Relation q4ProjectedStu = ra.project(studentsRel, List.of("stu_id", "tot_cred"));
        Relation q4ProjectedIns = ra.project(instructorRel, List.of("instr_id", "salary"));
        Relation query4 = ra.cartesianProduct(q4ProjectedStu, q4ProjectedIns);

        System.out.println("\n\nCartesian Product of Instructors and Students when they're projected to not have Name and Dept_Name");
        ra.print50(query4);



        Relation geoStudent = ra.select(studentsRel,((row)->row.get(studentsRel.getAttrIndex("dept_name")).equals(Cell.val("Geology"))));
        Relation geoInstructor = ra.select(instructorRel,((row)->row.get(instructorRel.getAttrIndex("dept_name")).equals(Cell.val("Geology"))));
        Relation projectedStudent = ra.project(geoStudent, List.of("name"));
        Relation projectedInstructor = ra.project(geoInstructor, List.of("name"));

        Relation geoRelation = (ra.union(projectedStudent, projectedInstructor));
        geoRelation.print();


    }

}
