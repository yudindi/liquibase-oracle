package liquibase.ext.ora.mview;

import liquibase.CatalogAndSchema;
import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;

public class GetMaterializedViewsGeneratorOracle extends AbstractSqlGenerator<GetMaterializedViewsStatement> {
    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public ValidationErrors validate(GetMaterializedViewsStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        return new ValidationErrors();
    }

    @Override
    public boolean supports(GetMaterializedViewsStatement statement, Database database) {
        return database instanceof OracleDatabase;
    }

    @Override
    public Sql[] generateSql(GetMaterializedViewsStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        // will change null schema to default if needed
        CatalogAndSchema schema = database.correctSchema(new CatalogAndSchema(null, statement.getSchemaName()));

        String sql = "SELECT MVIEW_NAME, OWNER FROM ALL_MVIEWS WHERE OWNER='" + schema.getSchemaName() + "'";
        if (statement.getName() != null) {
            sql += " AND MVIEW_NAME='" + statement.getName().toUpperCase() + "'";
        }
        return new Sql[]{
                new UnparsedSql(sql)
        };
    }
}
