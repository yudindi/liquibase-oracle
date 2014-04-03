package liquibase.ext.ora.mview;

import liquibase.database.Database;
import liquibase.exception.DatabaseException;
import liquibase.executor.Executor;
import liquibase.executor.ExecutorService;
import liquibase.snapshot.DatabaseSnapshot;
import liquibase.snapshot.InvalidExampleException;
import liquibase.snapshot.jvm.JdbcSnapshotGenerator;
import liquibase.structure.DatabaseObject;
import liquibase.structure.core.Schema;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class MaterializedViewSnapshotGenerator extends JdbcSnapshotGenerator {
    private static Pattern CREATE_MVIEW_AS_PATTERN = Pattern.compile(
            "^CREATE\\s+.*?MATERIALIZED\\s+.*?VIEW\\s+.*?AS\\s+", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    public MaterializedViewSnapshotGenerator() {
        super(MaterializedView.class, new Class[]{Schema.class});
    }

    @Override
    protected DatabaseObject snapshotObject(DatabaseObject example, DatabaseSnapshot snapshot)
            throws DatabaseException, InvalidExampleException {
        Database database = snapshot.getDatabase();
        Schema schema = example.getSchema();
        Executor executor = ExecutorService.getInstance().getExecutor(database);

        List<Map> results = executor.queryForList(
                new GetMaterializedViewsStatement(schema.getCatalogName(), example.getName()));
        if (results.size() > 0) {
            Map row = results.get(0);
            String rawViewName = (String) row.get("MVIEW_NAME");
            MaterializedView mview = new MaterializedView().setName(rawViewName);
            mview.setSchema(schema);
            mview.setDefinition(getMaterializedViewDefinition(database, schema, mview.getName()));
            return mview;
        }

        return null;
    }

    @Override
    protected void addTo(DatabaseObject foundObject, DatabaseSnapshot snapshot)
            throws DatabaseException, InvalidExampleException {
        if (!snapshot.getSnapshotControl().shouldInclude(MaterializedView.class)) {
            return;
        }

        if (foundObject instanceof Schema) {
            Schema schema = (Schema) foundObject;
            Database database = snapshot.getDatabase();
            Executor executor = ExecutorService.getInstance().getExecutor(database);
            List<Map> results = executor.queryForList(new GetMaterializedViewsStatement(schema.getCatalogName()));
            for (Map row : results) {
                String viewName = (String) row.get("MVIEW_NAME");
                schema.addDatabaseObject(new MaterializedView().setName(viewName).setSchema(schema));
            }
        }
    }

    private String getMaterializedViewDefinition(Database database, Schema schema, final String viewName)
            throws DatabaseException {
        Executor executor = ExecutorService.getInstance().getExecutor(database);
        String definition = executor.queryForObject(
                new GetMaterializedViewDefinitionStatement(schema.getCatalogName(), viewName), String.class);
        if (definition == null) {
            return null;
        }
        return CREATE_MVIEW_AS_PATTERN.matcher(definition).replaceFirst("");
    }
}
