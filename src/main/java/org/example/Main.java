package org.example;

import com.google.gson.Gson;
import org.apache.http.client.HttpClient;
import java.net.URI;
import java.net.http.HttpRequest;
import java.io.IOException;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static <HttpGet> void main(String[] args) throws IOException, InterruptedException {
        System.out.println("main 시작");
        Connection conn = DBConnection.getInstance();

        Scanner scanner = new Scanner(System.in);
        System.out.println("구를 입력하세요");

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        Integer x = null;
        Integer y = null;


        try {
            String query = "select DISTINCT gu FROM weather where si = '부산광역시'";
            pstmt = conn.prepareStatement(query);  // 쿼리 설정
            rs = pstmt.executeQuery();  // 쿼리 실행 및 결과 저장
            while (rs.next()) {
                String gu = rs.getString("gu");  // "gu" 열의 값을 가져옴
                System.out.println("구 이름: " + gu);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String gu = scanner.nextLine();

        System.out.println("동를 입력하세요");
        try {
            String query = "select DISTINCT dong FROM weather where si = '부산광역시' and gu = ?";
            pstmt = conn.prepareStatement(query);  // 쿼리 설정
            pstmt.setString(1, gu);

            rs = pstmt.executeQuery();  // 쿼리 실행 및 결과 저장
            while (rs.next()) {
                String dong = rs.getString("dong");  // "gu" 열의 값을 가져옴
                System.out.println("동 이름: " + dong);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String dong = scanner.nextLine();


        try {
            String query = "select x,y FROM weather where si = '부산광역시' and gu = ? and dong = ?";
            pstmt = conn.prepareStatement(query);  // 쿼리 설정
            pstmt.setString(1, gu);
            pstmt.setString(2, dong);

            rs = pstmt.executeQuery();  // 쿼리 실행 및 결과 저장
            while (rs.next()) {
                x = rs.getInt("x");  // "gu" 열의 값을 가져옴
                y = rs.getInt("y");  // "gu" 열의 값을 가져옴
                System.out.println("x : " + x + ", y : " + y);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String responseBody=MyHttp.get("https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst", "thHjlmkVi8Ps6wM9pRg%2FR7jmqig5Y2jUd4CwAn05g1BB18XjFBm9jsHw3pmRpwwAKeFyrzADXWyBnPs31bcXnQ%3D%3D", "20240607", "1200",  x,y);
        Gson gson = new Gson();
        Response yourObject = gson.fromJson(responseBody, Response.class);

        // 원하는 방식으로 객체 사용
        System.out.println(yourObject);



    }
}
