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
        List<Type> types = rel.getTypes();
        List<String> attrList = rel.getAttrs();

        List<Type> relTypes = new ArrayList<>();
        List<String> relAttrs = new ArrayList<>();
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
        if (rel1.getAttrs().size() == rel2.getAttrs().size() && rel1.getTypes.hashCode() == rel2.getTypes())
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
     * Peforms natural join on relations rel1 and rel2.
     * 
     * @return The resulting relation after applying natural join.
     */
    public Relation join(Relation rel1, Relation rel2) {
        
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
