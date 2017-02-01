package com.ccreanga.jdbc.oracle;

import com.ccreanga.IOExceptionRuntime;
import com.ccreanga.jdbc.ScriptGenerator;
import com.ccreanga.jdbc.model.Column;
import com.ccreanga.jdbc.model.Table;
import com.ccreanga.usecases.export.jdbc.oracle.OracleUtil;

import java.io.*;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OracleScriptGenerator implements ScriptGenerator {

    /**
     LOAD DATA
     INFILE '/home/cornel/orcl_export/TEST_TYPES_DATA_TABLE.ldr' "str '{EOL}'"
     INTO TABLE "TEST"."TEST_TYPES"
     FIELDS TERMINATED BY','
     OPTIONALLY ENCLOSED BY '"' AND '"'
     TRAILING NULLCOLS (
     "ID" ,
     "C_VARCHAR2" CHAR (100),
     "C_NVARCHAR2" CHAR (100),
     L_0 FILLER char,
     "C_CLOB" LOBFILE( L_0) TERMINATED BY EOF NULLIF L_0 = 'null',
     "C_NCLOB" ,
     "C_NUMBER" ,
     "C_DOUBLE" ,
     "C_FLOAT" ,
     "C_DATE" DATE "YYYY-MM-DD HH24:MI:SS" ,
     "C_TIMESTAMP" TIMESTAMP "YYYY-MM-DD HH24:MI:SS.FF" ,
     "C_TIMESTAMPTZ" TIMESTAMP WITH TIME ZONE "YYYY-MM-DD HH24:MI:SS.FF TZH:TZM" ,
     L_1 FILLER char,
     "C_BLOB" LOBFILE( L_1) TERMINATED BY EOF NULLIF L_1 = 'null')     */


    /**
     LOAD DATA
     INFILE 'sample.dat'
     INTO TABLE person_table
     FIELDS TERMINATED BY ','
     (name     CHAR(20),
     1  "RESUME"    LOBFILE( CONSTANT 'jqresume') CHAR(2000)
     TERMINATED BY "<endlob>\n")     */





    @Override
    public String generateLoadCommand(Table table, List<Column> columns, String folderName) {
        StringBuilder sb =
                new StringBuilder("LOAD DATA INFILE '" + folderName + File.separator + table.getName() + ".ldr' \"str '{EOL}'\"" +
                        " INTO TABLE \"" + table.getName() + "\" FIELDS TERMINATED BY ',' TRAILING NULLCOLS(\n");

        for (Column c : columns) {
            if (lob(c)){
                String lobFile = OracleUtil.generateLobFileName(table.getName(),c.getName());
                try {
                    BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(folderName+File.separator+lobFile));
                    LobFiles.put(lobFile,out);
                } catch (FileNotFoundException e) {
                    throw new IOExceptionRuntime(e);
                }
                sb.append("\""+c.getName()+"\"");
                sb.append("LOBFILE( CONSTANT '"+lobFile+"') TERMINATED BY \""+
                        OracleUtil.generateLobBoundary(table.getName()+"###"+c.getName())
                        +"\"");

            }else
            sb.append("\""+c.getName()+"\"");
            if ((c.getType()==Types.CHAR) || (c.getType()==Types.NCHAR))
                sb.append(" CHAR("+c.getSize()+")");
            else if (c.getType()==Types.DATE)
                sb.append(" DATE \"YYYY-MM-DD HH24:MI:SS\"");
            else if ((c.getType()==-101) || (c.getType()==Types.TIMESTAMP))
                sb.append(" TIMESTAMP \"YYYY-MM-DD HH24:MI:SS.FF\"");
            sb.append(",\n");

        }
        sb.setLength(sb.length()-2);

        sb.append(")");

        return sb.toString();

    }

    @Override
    public void end(Table table) {
        LobFiles.closeLobForTable(table.getName());
    }


    private boolean lob(Column c) {
        return (
                (c.getType() == Types.BLOB) ||
                        (c.getType() == Types.CLOB) ||
                        (c.getType() == Types.NCLOB) ||
                        (c.getType() == Types.LONGVARCHAR) ||
                        (c.getType() == Types.LONGNVARCHAR)
        );
    }

}
