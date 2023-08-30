package org.example.yurim;

import org.example.api.*;
import org.example.model.Book;
import org.example.model.BookDAO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("도서를 검색할 제목을 입력하세요: ");
        String info = br.readLine();
        List<Book> bookList = KaKaoAPI.getBookInfo(info);
        KaKaoAPI.printBookInfo(bookList);

        System.out.print("데이터베이스에 저장하시겠습니까? Y/N ");
        String ans = br.readLine();

        if (ans.equals("Y")){
            System.out.println();
            System.out.println("저장성공");
            System.out.println("[TABLE LIST]");
            System.out.println("  도서 제목  |  가격  |  출판사  |  작가  |  할인가격  | ISBN  ");
            BookDAO dao = new BookDAO();
            dao.bookRegister(bookList);
            List<Book> bookListDB = dao.bookSelect();
            if(bookListDB.size()!=0){
                for(Book book : bookListDB){
                    System.out.println(book.printBookInfo());
                }
            }
        } else {
            System.out.println("저장실패");
        }
    }
}
