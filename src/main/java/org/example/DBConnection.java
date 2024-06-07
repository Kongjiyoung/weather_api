package org.example;

import java.sql.*;

public class DBConnection {
    public static Connection getInstance() {
        String username = "root";
        String password = "ssar1234";
        String url = "jdbc:mysql://localhost:3306/weather";

        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("DB 연결 성공");

            // 데이터베이스 메타데이터를 사용하여 테이블 존재 여부 확인
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "weather", null);

            if (!tables.next()) { // 테이블이 존재하지 않는 경우
                System.out.println("테이블이 존재하지 않습니다. Excel 파일에서 데이터를 가져와 테이블에 삽입합니다.");
                UpdateDatabaseFromExcel.insert(conn); // UpdateDatabaseFromExcel 클래스의 insert 메서드 호출
            } else {
                System.out.println("테이블이 이미 존재합니다.");
            }
            return conn;

        } catch (SQLException e) {
            throw new RuntimeException("DB 연결 실패", e);
        }
    }
}