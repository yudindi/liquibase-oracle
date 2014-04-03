package liquibase.ext.ora.mview;

import liquibase.statement.AbstractSqlStatement;

public class GetMaterializedViewDefinitionStatement extends AbstractSqlStatement {
    private String catalogName;
    private String name;

    public GetMaterializedViewDefinitionStatement(String catalogName, String name) {
        this.catalogName = catalogName;
        this.name = name;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public String getName() {
        return name;
    }
}
