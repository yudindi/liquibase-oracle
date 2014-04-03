package liquibase.ext.ora.mview;

import liquibase.statement.AbstractSqlStatement;

public class GetMaterializedViewsStatement extends AbstractSqlStatement {
    private String catalogName;
    private String name;

    public GetMaterializedViewsStatement(String catalogName) {
        this.catalogName = catalogName;
    }

    public GetMaterializedViewsStatement(String catalogName, String name) {
        this(catalogName);
        this.name = name;
    }

    public String getCatalogName() {
        return catalogName;
    }

    public String getName() {
        return name;
    }
}
