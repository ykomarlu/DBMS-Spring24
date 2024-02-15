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
        List<String> attrList = rel.getAttrs();
        List<Type> types = rel.getTypes();

        List<String> relAttrs = new ArrayList<>();
        List<Type> relTypes = new ArrayList<>();
        List<Integer> indexes = new ArrayList<>();

        for (int i = 0; i < attrs.size(); i++) {
            try {
                int index = rel.getAttrIndex(attrs.get(i));
                relAttrs.add(attrList.get(index));
                relTypes.add(types.get(index));
                indexes.add(index);
            } catch (IllegalArgumentException iae) {
                throw new IllegalArgumentException("Attribute \"" + attrs.get(i) + "\" not found in relation!");
            }
        }

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
    public Relation union(Relation rel1, Relation rel2) {
        
    }

    /**
     * Performs the set difference operaion on the relations rel1 and rel2.
     * 
     * @return The resulting relation after applying the set difference operation.
     * 
     * @throws IllegalArgumentException If rel1 and rel2 are not compatible.
     */
    public Relation diff(Relation rel1, Relation rel2) {
        
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
        
    }

    /**
     * Performs cartesian product on relations rel1 and rel2.
     * 
     * @return The resulting relation after applying cartisian product.
     * 
     * @throws IllegalArgumentException if rel1 and rel2 have common attibutes.
     */
    public Relation cartesianProduct(Relation rel1, Relation rel2) {
        
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

        Relation joinedRelation = new RelationBuilder()
            .attributeNames(finalRelAttrs)
            .attributeTypes(finalRelTypes)
            .build();

        for (int i = 0; i < rel1.getSize(); i++) {
            for (int j = 0; j < rel2.getSize(); j++) {
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
        
    }
}
