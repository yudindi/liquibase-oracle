package liquibase.ext.ora.mview;

import liquibase.CatalogAndSchema;
import liquibase.database.Database;
import liquibase.database.core.OracleDatabase;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sql.UnparsedSql;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.core.AbstractSqlGenerator;

public class GetMaterializedViewDefinitionGeneratorOracle extends AbstractSqlGenerator<GetMaterializedViewDefinitionStatement> {
    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public ValidationErrors validate(GetMaterializedViewDefinitionStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors.checkRequiredField("viewName", statement.getName());
        return validationErrors;
    }

    @Override
    public boolean supports(GetMaterializedViewDefinitionStatement statement, Database database) {
        return database instanceof OracleDatabase;
    }

    @Override
    public Sql[] generateSql(GetMaterializedViewDefinitionStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        // will change null schema to default if needed
        CatalogAndSchema schema = database.correctSchema(new CatalogAndSchema(null, statement.getSchemaName()));

        return new Sql[]{
                new UnparsedSql("SELECT QUERY FROM ALL_MVIEWS WHERE OWNER='" + schema.getCatalogName() + "' AND MVIEW_NAME='" + statement.getName().toUpperCase() + "'")
        };
    }
}
