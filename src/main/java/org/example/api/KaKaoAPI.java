package org.example.api;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.example.model.Book;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.example.yurim.Statics.*;

public class KaKaoAPI {
    /**Book의 기본정보 받아오는 함수**/
    public static List<Book> getBookInfo(String info) {
        CloseableHttpClient client = HttpClients.custom().build();
        HttpUriRequest request = buildRequest(info);
        StringBuffer responseSB = getResponse(client,request);
        List<Book> bookList = parsingResponse(responseSB);

        return bookList;
    }

    /**Request 생성 함수**/
    public static HttpUriRequest buildRequest(String info){
        return  RequestBuilder.get()
                .setUri(GET_BOOKINFO)
                .setHeader("Authorization", REST_API_KEY)
                .addParameter("query",info)
                .addParameter("size",String.valueOf(10))
                .build();
    }

    /**Response 반환 함수**/
    public static StringBuffer getResponse(CloseableHttpClient client, HttpUriRequest request) {
        StringBuffer responseSB = null;
        try{
            CloseableHttpResponse response = client.execute(request);
            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String input;
            responseSB= new StringBuffer();
            while ((input = br.readLine()) != null) {
                responseSB.append(input);
            }
            br.close();
            client.close();
        } catch (IOException e){
            System.out.println("IOException 발생");
        }
        return responseSB;
    }

    /**Response 파싱 함수**/
    public static List<Book> parsingResponse(StringBuffer responseSB){
        List<Book> bookList = new ArrayList<>();
        try {
            JSONObject resJsonObj = new JSONObject(responseSB.toString());
            JSONArray resJsonArr = resJsonObj.getJSONArray("documents");
            for(int i = 0; i<10; i++){
                JSONObject subJsonObj = resJsonArr.getJSONObject(i);
                String title = subJsonObj.getString("title");
                int price = subJsonObj.getInt("price");
                String publisher = subJsonObj.getString("publisher");
                JSONArray authorsJson = subJsonObj.getJSONArray("authors");
                String authors = authorsJson.toString();
                int sale_price = subJsonObj.getInt("sale_price");
                String isbn = subJsonObj.getString("isbn");
                Book book = new Book(title,price,publisher,authors,sale_price,isbn);
                bookList.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookList;
    }

    /**Book 정보 출력 함수**/
    public static void printBookInfo(List<Book> bookList){
        System.out.println("  도서 제목  |  가격  |  출판사  |  작가  |  할인가격  | ISBN  ");
        for(Book book : bookList){
            System.out.println(book.printBookInfo());
        }
    }
}
