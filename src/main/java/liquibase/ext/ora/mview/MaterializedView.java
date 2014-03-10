package liquibase.ext.ora.mview;

import liquibase.structure.core.View;

public class MaterializedView extends View {

    @Override
    public MaterializedView setName(String name) {
        return (MaterializedView) super.setName(name);
    }

}
