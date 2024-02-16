package uga.cs4370.mydbimpl;

import uga.cs4370.mydb.RA;
import uga.cs4370.mydb.Relation;
import uga.cs4370.mydb.RelationBuilder;
import uga.cs4370.mydb.Type;
import uga.cs4370.mydb.Predicate;
import uga.cs4370.mydb.Cell;

import java.util.List;
import java.util.ArrayList;

public class RAImpl implements RA {
    
    /**
     * Performs the select operation on the relation rel
     * by applying the predicate p.
     * 
     * @return The resulting relation after applying the select operation.
     */
    public Relation select(Relation rel, Predicate p) {
        Relation selectedRelation = new RelationBuilder()
                .attributeNames(rel.getAttrs())
                .attributeTypes(rel.getTypes())
                .build();

            for (int i = 0; i < rel.getSize(); i++) {
                List<Cell> row = new ArrayList<>();
                if (p.check(rel.getRow(i))){ // if predicate is true, add the row to the relation
                    for (int j = 0; j < rel.getAttrs().size(); j++) {
                        row.add(rel.getRow(i).get(j));
                    }//for
                    selectedRelation.insert(row);
                }//if
            }//for



        return selectedRelation;
    }

    /**
     * Performs the project operation on the relation rel
     * given the attributes list attrs.
     * 
     * @return The resulting relation after applying the project operation.
     * 
     * @throws IllegalArgumentException If attributes in attrs are not 
     * present in rel.
     */
    public Relation project(Relation rel, List<String> attrs) {

        List<Type> types = rel.getTypes(); //int, String
        List<String> attrList = rel.getAttrs(); // prof_id, name

        List<Type> relTypes = new ArrayList<>(); // empty
        List<String> relAttrs = new ArrayList<>(); //ditto
        List<Integer> indexes = new ArrayList<>(); //ditto

        for (int i = 0; i < attrs.size(); i++) { //going through each column in the db attrs.size times
            //because
            //i <= 2
            try {
                int index = rel.getAttrIndex(attrs.get(i));
                relAttrs.add(attrList.get(index));
                relTypes.add(types.get(index));
                indexes.add(index);
            } catch (IllegalArgumentException iae) {
                throw new IllegalArgumentException("Attribute \"" + attrs.get(i) + "\" not found in relation!");
            }
        }

        //So in here we need to get the actual attributes and types of the

        Relation projectedRelation = new RelationBuilder()
            .attributeNames(relAttrs)
            .attributeTypes(relTypes)
            .build();

        for (int i = 0; i < rel.getSize(); i++) { 
            List<Cell> row = new ArrayList<>();
            
            for (int j = 0; j < indexes.size(); j++) {
                row.add(rel.getRow(i).get(indexes.get(j)));
            }

            projectedRelation.insert(row);
        }

        return projectedRelation;
    }

    /**
     * Performs the union operation on the relations rel1 and rel2.
     * 
     * @return The resulting relation after applying the union operation.
     * 
     * @throws IllegalArgumentException If rel1 and rel2 are not compatible.
     */
    public Relation union(Relation rel1, Relation rel2) throws IllegalArgumentException{
        if ((rel1.getAttrs().size() != rel2.getAttrs().size()) || (rel1.getTypes().hashCode() != rel2.getTypes().hashCode())) {
            throw new IllegalArgumentException("Arity or Attribute Type requirements not met");
        }

        //Union: This is where you have multiple relations and only return the relation instances where either rel1 or rel2
        //are true but get rid of

        Relation newRelation = new RelationBuilder()
                .attributeNames(rel1.getAttrs())
                .attributeTypes(rel1.getTypes())
                .build();

        List<Cell> row = new ArrayList<>();

        for (int i = 0; i < rel1.getSize(); i++) {
//          Build new relation with all the rows from table 1 then from table 2
            newRelation.insert(rel1.getRow(i));
        }

        for (int j = 0; j < rel2.getSize(); j++) {
            newRelation.insert(rel2.getRow(j));
        }

        return newRelation;
    }

    /**
     * Performs the set difference operaion on the relations rel1 and rel2.
     * 
     * @return The resulting relation after applying the set difference operation.
     * 
     * @throws IllegalArgumentException If rel1 and rel2 are not compatible.
     */
    public Relation diff(Relation rel1, Relation rel2) {

         if (!rel1.getAttrs().equals(rel2.getAttrs()) || !rel1.getTypes().equals(rel2.getTypes())) {
            throw new IllegalArgumentException("Relations are not compatible for difference operation.");
        }
    
        // Create a new relation with the same schema as the input relations
        RelationBuilder builder = new RelationBuilder()
                .attributeNames(rel1.getAttrs())
                .attributeTypes(rel1.getTypes());
        Relation resultRelation = builder.build();
    
        // Add rows from rel1 to the result relation only if they are not present in rel2
        for (int i = 0; i < rel1.getSize(); i++) {
            List<Cell> row = rel1.getRow(i);
            if (!containsRow(rel2, row)) {
                resultRelation.insert(new ArrayList<>(row));
            }
        }
    
        return resultRelation;
    }

    /**
     * Renames the attributes in origAttr of relation rel to corresponding 
     * names in renamedAttr.
     * 
     * @return The resulting relation after renaming the attributes.
     * 
     * @throws IllegalArgumentException If attributes in origAttr are not present in 
     * rel or origAttr and renamedAttr do not have matching argument counts.
     */
    public Relation rename(Relation rel, List<String> origAttr, List<String> renamedAttr) {
         if (origAttr.size() != renamedAttr.size()) {
            throw new IllegalArgumentException("Original and new attribute lists must have the same length.");
        }

        List<Type> types = rel.getTypes();
        List<String> attrList = new ArrayList<>(rel.getAttrs()); 

        
        for (int i = 0; i < origAttr.size(); i++) {
            String originalName = origAttr.get(i);
            String newName = renamedAttr.get(i);

            int index = rel.getAttrIndex(originalName); 
            attrList.set(index, newName); 
        }

        
        Relation renamedRelation = new RelationBuilder()
                .attributeNames(attrList)
                .attributeTypes(types)
                .build();

       
        for (int i = 0; i < rel.getSize(); i++) {
            List<Cell> row = rel.getRow(i); 
            renamedRelation.insert(new ArrayList<>(row)); 
        }

        return renamedRelation;
    }

    /**
     * Performs cartesian product on relations rel1 and rel2.
     * 
     * @return The resulting relation after applying cartisian product.
     * 
     * @throws IllegalArgumentException if rel1 and rel2 have common attibutes.
     */

    public Relation cartesianProduct(Relation rel1, Relation rel2) throws IllegalArgumentException {

        for (String str : rel1.getAttrs()) {
            if (rel2.hasAttr(str)) throw new IllegalArgumentException("Duplicate Attribute");
        }
//do a double for loop using hasAttr to check to see if there are any common attributes

        List<String> rel1Attrs = rel1.getAttrs();
        List<Type> rel1Types = rel1.getTypes();
        List<String> rel2Attrs = rel2.getAttrs();
        List<Type> rel2Types = rel2.getTypes();

//        int index = rel.getAttrIndex(attrs.get(i));
//        relAttrs.add(attrList.get(index));
//        relTypes.add(types.get(index));
//        indexes.add(index);

        List<String> relAttrs = new ArrayList<>();
        List<Type> relTypes = new ArrayList<>();

        int i;

        for (int z = 0; z < rel1Attrs.size(); z++) {
            relAttrs.add(rel1Attrs.get(z));
            relTypes.add(rel1Types.get(z));
        }

        for (int z = 0; z < (rel2Attrs.size()); z++) {
            relAttrs.add(rel2Attrs.get(z));
            relTypes.add(rel2Types.get(z));
        }

        Relation newRelation = new RelationBuilder()
                .attributeNames(relAttrs)
                .attributeTypes(relTypes)
                .build();

        //Now a relation object with the combined names and types is created

        //Now I need to add the cells of each relation 2 table multiple times for each row of the relation 1 table.

        for (int p = 0; p < rel1.getSize(); p++) {
            List<Cell> row = new ArrayList<>();

//            go through each row and then add the cells from those rows from each table into the new tables rows

            row = (rel1.getRow(p));

            for (int a = 0; a < rel2.getSize(); a++) {

                List<Cell> row2 = new ArrayList<>();

                for (Cell cell : row) {
                    row2.add(cell);
                }

                for (int j = 0; j < rel2.getRow(a).size(); j++) {
                    row2.add(rel2.getRow(a).get(j));
//               We need to get a cell object in the param and then add it to a row
                }


                newRelation.insert(row2);
                row2 = null;
            }
        }

        return newRelation;

    }

    /**
     * Class to hold attrs and types in a list
     * 
     */
    public class Pair {
        public String attr;
        public Type type;
        public int[] indexes = new int[2];
        public boolean isCommon = false;

        Pair(String attr, Type type) {
            this.attr = attr;
            this.type = type;
        }

        Pair(String attr, Type type, int r1Index, int r2Index, boolean isCommon) {
            this.attr = attr;
            this.type = type;
            this.indexes[0] = r1Index;
            this.indexes[1] = r2Index;
            this.isCommon = true;
        }
     }

    /**
     * Function that returns the common attributes between the two lists.
     * 
     * @return A list of the common attributes.
     */
    public List<Pair> mergeRels(Relation rel1, Relation rel2) {

        List<String> rel1Attrs = rel1.getAttrs();
        List<Type> rel1Types = rel1.getTypes();
        List<String> rel2Attrs = rel2.getAttrs();
        List<Type> rel2Types = rel2.getTypes();

        List<Pair> pairs = new ArrayList<>();
        List<Pair> otherPairs = new ArrayList<>();

        for (int i = 0; i < rel1Attrs.size(); i++) {
            String attr = rel1Attrs.get(i);
            if (rel2Attrs.contains(attr)) {
                pairs.add(new Pair(attr, rel1Types.get(i), i, rel2.getAttrIndex(attr), true));
            } else {
                otherPairs.add(new Pair(attr, rel1Types.get(i)));
            }
        }

        for (int i = 0; i < rel2Attrs.size(); i++) {
            String attr = rel2Attrs.get(i);
            if (!rel1Attrs.contains(attr)) {
                otherPairs.add(new Pair(attr, rel2Types.get(i)));
            }
        }

        pairs.addAll(otherPairs);

        return pairs;
    }

    /**
     * Peforms natural join on relations rel1 and rel2.
     * 
     * @return The resulting relation after applying natural join.
     */
    public Relation join(Relation rel1, Relation rel2) {

        List<Pair> relationPairs = mergeRels(rel1, rel2);

        List<String> finalRelAttrs = new ArrayList<>();
        List<Type> finalRelTypes = new ArrayList<>();
        List<Integer> r1CommonIndexes = new ArrayList<>();
        List<Integer> r2CommonIndexes = new ArrayList<>();

        for (int i = 0; i < relationPairs.size(); i++) {
            finalRelAttrs.add(relationPairs.get(i).attr);
            finalRelTypes.add(relationPairs.get(i).type);
        }

        for (int i = 0; i < relationPairs.size(); i++) {
            if (relationPairs.get(i).isCommon) {
                r1CommonIndexes.add(relationPairs.get(i).indexes[0]);
                r2CommonIndexes.add(relationPairs.get(i).indexes[1]);
            } else {
                break;
            }
        }

        int rel1RowSize = rel1.getRow(0).size();
        int rel2RowSize = rel2.getRow(0).size();
        int rel2Size = rel2.getSize();

        Relation joinedRelation = new RelationBuilder()
            .attributeNames(finalRelAttrs)
            .attributeTypes(finalRelTypes)
            .build();

        for (int i = 0; i < rel1.getSize(); i++) {
            for (int j = 0; j < rel2Size; j++) {
                boolean skip = false;
                List<Cell> row = new ArrayList<>();

                for (int k = 0; k < r1CommonIndexes.size(); k++) {
                    if (rel1.getRow(i).get(r1CommonIndexes.get(k)).equals(rel2.getRow(j).get(r2CommonIndexes.get(k)))) {
                        row.add(rel1.getRow(i).get(r1CommonIndexes.get(k)));
                    } else {
                        skip = true;
                        break;
                    }
                }

                if (!skip) {
                    for (int k = 0; k < rel1RowSize; k++) {
                        if (!r1CommonIndexes.contains(k)) {
                            row.add(rel1.getRow(i).get(k));
                        }
                    }
                    for (int k = 0; k < rel2RowSize; k++) {
                        if (!r2CommonIndexes.contains(k)) {
                            row.add(rel2.getRow(j).get(k));
                        }
                    }
                    joinedRelation.insert(row);
                }
            }
        }

        return joinedRelation;
    }

    /**
     * Performs theta join on relations rel1 and rel2 with predicate p.
     * 
     * @return The resulting relation after applying theta join.
     * 
     * @throws IllegalArgumentException if rel1 and rel2 have common attibutes.
     */
    public Relation join(Relation rel1, Relation rel2, Predicate p) {
        for (int i = 0; i < rel1.getAttrs().size(); i++) {
            for (int k = 0; k < rel2.getAttrs().size(); k++) {
                if ((rel1.getAttrs().get(i)).equals(rel2.getAttrs().get(k))) {
                    throw new IllegalArgumentException();
                }//if
            }//for
        }//for

        List<String> attrList = rel1.getAttrs();
        attrList.addAll(rel2.getAttrs());
        List<Type> types = rel1.getTypes();
        types.addAll(rel2.getTypes());

        Relation joinedRelation = new RelationBuilder()
                .attributeNames(attrList)
                .attributeTypes(types)
                .build();

        Relation cartRel = cartesianProduct(rel1, rel2);

        for(int i = 0; i < cartRel.getSize(); i++) {
            List<Cell> row = new ArrayList<>();
            if (p.check(cartRel.getRow(i))){
                for (int j = 0; j < cartRel.getRow(i).size(); j++)
                {
                    row.add(cartRel.getRow(i).get(j));
                }
                joinedRelation.insert(row);
            }
        }
        return joinedRelation;
    }
}
