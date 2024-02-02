package org.example.librarymanagement;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;

@Service
public class ExcelSqlHandler {

    static String JDBC_URL = "jdbc:postgresql://localhost:5432/libraryManagementTest";

    static String USERNAME = "postgres";

    static String PASSWORD = "admin";

    public static void readExcelFile(String filePath, String tableName, int sheetNumber) throws IOException {
        try (FileInputStream excelFile = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(excelFile);
             Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {

            Sheet sheet = workbook.getSheetAt(sheetNumber);

            Row headerRow = sheet.getRow(0);
            String[] columnNames = new String[headerRow.getLastCellNum()];
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                columnNames[i] = headerRow.getCell(i).getStringCellValue();
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    PreparedStatement statement = createPreparedStatement(connection, tableName, columnNames);
                    int columnIndex = 0;

                    for (Cell cell : row) {
                        setCellValue(statement, cell, ++columnIndex);
                    }

                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception properly in your application
        }
    }

    private static PreparedStatement createPreparedStatement(Connection connection, String tableName, String[] columnNames) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ");
        queryBuilder.append(tableName).append(" (");

        for (String columnName : columnNames) {
            queryBuilder.append(columnName).append(", ");
        }

        queryBuilder.setLength(queryBuilder.length() - 2); // Remove the last comma and space
        queryBuilder.append(") VALUES (");

        for (int i = 0; i < columnNames.length; i++) {
            queryBuilder.append("?, ");
        }

        queryBuilder.setLength(queryBuilder.length() - 2); // Remove the last comma and space
        queryBuilder.append(")");
        return connection.prepareStatement(queryBuilder.toString());
    }

    private static void setCellValue(PreparedStatement statement, Cell cell, int columnIndex) throws SQLException {
        switch (cell.getCellType()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    statement.setDate(columnIndex, new java.sql.Date(cell.getDateCellValue().getTime()));
                } else {
                    statement.setDouble(columnIndex, cell.getNumericCellValue());
                }
                break;
            case STRING:
                statement.setString(columnIndex, cell.getStringCellValue());
                break;
            case BOOLEAN:
                statement.setBoolean(columnIndex, cell.getBooleanCellValue());
                break;
            default:
                statement.setString(columnIndex, null);
                break;
        }
    }

    public static void dropTable(String tableName) {
        // SQL statement to drop the table
        String dropTableSQL = "DROP TABLE IF EXISTS " + tableName;

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {

            // Execute the drop table statement
            statement.executeUpdate(dropTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
