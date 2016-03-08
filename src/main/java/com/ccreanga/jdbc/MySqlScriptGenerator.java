package com.ccreanga.jdbc;

import com.ccreanga.jdbc.model.Column;
import com.ccreanga.jdbc.model.Table;

import java.io.File;
import java.sql.Types;
import java.util.List;

public class MySqlScriptGenerator implements ScriptGenerator{
    @Override
    public String generateLoadCommand(Table table, List<Column> columns, String folderName) {
        StringBuilder sb = new StringBuilder("LOAD DATA LOCAL INFILE '" + folderName + File.separator + table.getName() + ".txt'" + " INTO TABLE `" + table.getName() + "` (");

        boolean found = false;
        for (Column c : columns) {
            if (binaryType(c)) {
                sb.append("@");
                found = true;
            }
            sb.append(c.getName()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);

        sb.append(")");
        if (found) {
            sb.append(" SET ");

            for (Column c : columns) {
                if (binaryType(c)) {
                    sb.append(c.getName()).append("=UNHEX(@").append(c.getName()).append("),");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(";");

        return sb.toString();

    }

    private boolean binaryType(Column c) {
        return ((c.getType() == Types.BLOB) || (c.getType() == Types.LONGVARBINARY) || (c.getType() == Types.VARBINARY) || (c.getType() == Types.BINARY));
    }

}
