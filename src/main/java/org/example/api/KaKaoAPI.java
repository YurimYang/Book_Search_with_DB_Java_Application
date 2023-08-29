package org.example.api;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.example.model.Book;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.example.yurim.Statics.*;

public class KaKaoAPI {
    static List<Book> bookList = new ArrayList<>();

    /** rest_api_key 받아오는 함수 **/
    public static void getAPIKey() throws IOException {
        Properties pro = new Properties();
        FileInputStream fis = new FileInputStream(PROPERTIES);
        pro.load(new BufferedInputStream(fis));
        REST_API_KEY = pro.getProperty("REST_API");
    }

    /** 쿼리 파라미터 (Params)
     * 기본 10개 출력 : size = 10
     * 입력값 : query (String)
     */
    /** 응답 (Response)
     * 도서제목 : title
     * 가격 : price (Integer)
     * 출판사 : publisher
     * 저자 : authors[] (String[]) => String화해야함!!
     * 할인가격 : sale_price (Integer)
     * ISBN : isbn
     */

    /**Book의 기본정보 받아오는 함수**/
    public static List<Book> getBookInfo(String info) throws IOException {
        //1. Http Client 생성
        CloseableHttpClient client = HttpClients.custom().build();

        //2. Request 생성
        HttpUriRequest request = RequestBuilder.get()
                .setUri(GET_BOOKINFO)
                .setHeader("Authorization", REST_API_KEY)
                .addParameter("query",info)
                .addParameter("size",String.valueOf(10))
                .build();

        //3. Response 회수(IOException)
        CloseableHttpResponse response = client.execute(request);

        //4. Response string화
        BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String input;
        StringBuffer responseSB = new StringBuffer();
        while ((input = br.readLine()) != null) {
            responseSB.append(input);
        }
        br.close();
        client.close();


        //5. Response로부터 bookInfo 추출
        JSONObject resJsonObj = new JSONObject(responseSB.toString());
        JSONArray resJsonArr = resJsonObj.getJSONArray("documents");
        for(int i =0;i<10;i++){
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
        return bookList;
    }

    /**Book정보를 출력**/
    public static void printBookInfo(){
        System.out.println("  도서 제목  |  가격  |  출판사  |  작가  |  할인가격  | ISBN  ");
        for(Book book : bookList){
            System.out.println(book.printBookInfo());
        }
    }
}
