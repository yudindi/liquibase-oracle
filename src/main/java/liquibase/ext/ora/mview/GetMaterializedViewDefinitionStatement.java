package liquibase.ext.ora.mview;

import liquibase.statement.AbstractSqlStatement;

public class GetMaterializedViewDefinitionStatement extends AbstractSqlStatement {
    private String schemaName;
    private String name;

    public GetMaterializedViewDefinitionStatement(String schemaName, String name) {
    }

    public String getSchemaName() {
        return schemaName;
    }

    public String getName() {
        return name;
    }
}
