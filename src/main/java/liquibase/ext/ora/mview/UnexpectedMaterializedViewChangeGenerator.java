package liquibase.ext.ora.mview;

import liquibase.change.Change;
import liquibase.change.core.DropViewChange;
import liquibase.database.Database;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.ChangeGeneratorChain;
import liquibase.diff.output.changelog.UnexpectedObjectChangeGenerator;
import liquibase.ext.ora.dropmaterializedview.DropMaterializedViewChange;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Column;
import liquibase.structure.core.Table;
import liquibase.structure.core.View;

public class UnexpectedMaterializedViewChangeGenerator implements UnexpectedObjectChangeGenerator {
    @Override
    public int getPriority(Class<? extends DatabaseObject> objectType, Database database) {
        if (MaterializedView.class.isAssignableFrom(objectType)) {
            return PRIORITY_DEFAULT;
        }
        return PRIORITY_NONE;
    }

    @Override
    public Class<? extends DatabaseObject>[] runAfterTypes() {
        return null;
    }

    @Override
    public Class<? extends DatabaseObject>[] runBeforeTypes() {
        return new Class[] {
                Table.class,
                Column.class
        };
    }

    @Override
    public Change[] fixUnexpected(DatabaseObject unexpectedObject, DiffOutputControl control, Database referenceDatabase, Database comparisonDatabase, ChangeGeneratorChain chain) {
        MaterializedView view = (MaterializedView) unexpectedObject;

        DropMaterializedViewChange change = new DropMaterializedViewChange();
        change.setViewName(view.getName());
        if (control.isIncludeSchema()) {
            change.setSchemaName(view.getSchema().getName());
        }

        for (Column column : view.getColumns()) {
            control.setAlreadyHandledUnexpected(column);
        };

        //TODO: remove a table with the same name from the chain
        //control.setAlreadyHandledUnexpected(table);

        return new Change[]{change};


    }
}
