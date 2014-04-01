package liquibase.ext.ora.mview;

import liquibase.structure.core.Relation;
import liquibase.structure.core.Schema;

public class MaterializedView extends Relation {

    @Override
    public MaterializedView setName(String name) {
        return (MaterializedView) super.setName(name);
    }

    @Override
    public MaterializedView setSchema(Schema schema) {
        return (MaterializedView) super.setSchema(schema);
    }

    public String getDefinition() {
        return getAttribute("definition", String.class);
    }

    public void setDefinition(String definition) {
        this.setAttribute("definition", definition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MaterializedView that = (MaterializedView) o;

        return getName().equalsIgnoreCase(that.getName());

    }

    @Override
    public int hashCode() {
        return getName().toUpperCase().hashCode();
    }

    @Override
    public String toString() {
        String viewStr = getName() + " (";
        for (int i = 0; i < getColumns().size(); i++) {
            if (i > 0) {
                viewStr += "," + getColumns().get(i);
            } else {
                viewStr += getColumns().get(i);
            }
        }
        viewStr += ")";
        return viewStr;
    }

}
