package liquibase.ext.ora.mview;

import liquibase.statement.AbstractSqlStatement;

public class GetMaterializedViewsStatement extends AbstractSqlStatement {
    private String schemaName;
    private String name;

    public GetMaterializedViewsStatement(String schemaName) {
        this.schemaName = schemaName;
    }

    public GetMaterializedViewsStatement(String schemaName, String name) {
    }

    public String getSchemaName() {
        return schemaName;
    }

    public String getName() {
        return name;
    }
}
