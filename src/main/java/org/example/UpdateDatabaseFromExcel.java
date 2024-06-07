package org.example;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateDatabaseFromExcel {

    public void excute(ArrayList<ArrayList<String>> list, Connection conn) throws IOException {

        PreparedStatement pstmt = null;
        String query = "INSERT INTO weather(si, gu, dong, x, y) values (?, ?, ?, ?, ?)";
        String createTable = """
                            CREATE TABLE weather (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            si VARCHAR(255),
                            gu VARCHAR(255),
                            dong VARCHAR(255),
                            x VARCHAR(255),
                            y VARCHAR(255))
                            """;

        System.out.println("총 라인 수 : " + list.size());

        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(createTable);
            pstmt = conn.prepareStatement(query);  // 쿼리 설정

            for (int i = 0; i < list.size(); i++) {  // 매개변수로 받아온 ArrayList 의 길이만큼 반복한다.
                ArrayList<String> row = list.get(i);
                if (row == null || row.size() < 5) continue;  // 행에 값이 없거나 충분한 열이 없으면 제외


                // 앞의 쿼리에서 물음표에 들어갈 항목들을 순서대로 기입
                pstmt.setString(1, row.get(0));
                pstmt.setString(2, row.get(1));
                pstmt.setString(3, row.get(2));
                pstmt.setString(4, row.get(3));
                pstmt.setString(5, row.get(4));

                // update query 실행
                pstmt.executeUpdate();

                if (i % 1000 == 0) {
                    System.out.println(i + "번 라인 쓰는 중...");
                }
            }

            System.out.println("insert를 완료했습니다.");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            if (conn != null) try { conn.close(); } catch (SQLException e) {}
        } // DB 연결에 사용한 객체와 Query수행을 위해 사용한 객체를 닫는다.
    }

    public ArrayList<ArrayList<String>> readFilter(String fileName) throws IOException {
        FileInputStream fis = new FileInputStream(fileName);

//        @SuppressWarnings("resource")
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        int rowindex = 0;
        int columnindex = 0;
        ArrayList<ArrayList<String>> filters = new ArrayList<ArrayList<String>>();

        int sheetCn = workbook.getNumberOfSheets();  // 시트 수
        for (int sheetnum = 0; sheetnum < sheetCn; sheetnum++) {  // 시트 수만큼 반복

            int sheetnum2 = sheetnum + 1;
            System.out.println("sheet = " + sheetnum2);

            XSSFSheet sheet = workbook.getSheetAt(sheetnum);  // 읽어올 시트 선택
            int rows = sheet.getPhysicalNumberOfRows();    // 행의 수
            XSSFRow row = null;

            for (rowindex = 1; rowindex < rows; rowindex++) {  // 행의 수만큼 반복

                row = sheet.getRow(rowindex);  // rowindex 에 해당하는 행을 읽는다
                ArrayList<String> filter = new ArrayList<String>();  // 한 행을 읽어서 저장할 변수 선언

                if (row != null) {
                    int cells = 5;  // 셀의 수 (수정)
                    cells = row.getPhysicalNumberOfCells();    // 열의 수
                    for (columnindex = 0; columnindex < cells; columnindex++) {  // 열의 수만큼 반복
                        XSSFCell cell_filter = row.getCell(columnindex);  // 셀값을 읽는다
                        String value = "";
                        // 셀이 빈값일경우를 위한 널체크
                        if (cell_filter == null) {
                            continue;
                        } else {
                            // 타입별로 내용 읽기
                            switch (cell_filter.getCellType()) {
                                case XSSFCell.CELL_TYPE_FORMULA:
                                    value = cell_filter.getCellFormula();
                                    break;
                                case XSSFCell.CELL_TYPE_NUMERIC:
                                    value = cell_filter.getNumericCellValue() + "";
                                    break;
                                case XSSFCell.CELL_TYPE_STRING:
                                    value = cell_filter.getStringCellValue() + "";
                                    break;
                                case XSSFCell.CELL_TYPE_BLANK:
                                    value = cell_filter.getBooleanCellValue() + "";
                                    break;
                                case XSSFCell.CELL_TYPE_ERROR:
                                    value = cell_filter.getErrorCellValue() + "";
                                    break;
                            }
                        }
                        filter.add(value);  // 읽은 셀들을 filter에 추가 (행)
                    }
                }
                filters.add(filter); // filter(행)을 filters(열)에 추가
            }
        }
        fis.close();  // 파일 읽기 종료
        return filters;  // 리스트 반환
    }

    public static void insert(Connection conn){
        UpdateDatabaseFromExcel eamCsvLoad = new UpdateDatabaseFromExcel();  // 객체 생성
        String location = "D:\\weather\\weatherdata.xlsx";  // 파일의 위치 및 이름
        ArrayList<ArrayList<String>> list = null;
        try {
            list = eamCsvLoad.readFilter(location);  // 파일에서 각 셀들을 읽어서 ArrayList에 저장
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("파일 update 시작");
        try {
            eamCsvLoad.excute(list, conn);  // list에 저장되어 있는 데이터들을 데이터베이스에 업로드하는 함수 시작
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
