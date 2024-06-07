package org.example;

public class Response {
//    int resultCode	;
//    String resultMsg;
//    int numOfRows;
//    int pageNo;
//    int totalCount;
//    int dataType;
//    long baseDate;
//    long baseTime;
//    int nx;
//    int ny;
    int category;
    int obsrValue;

    @Override
    public String toString() {
        return "Response{" +
                "category=" + category +
                ", obsrValue=" + obsrValue +
                '}';
    }
}
