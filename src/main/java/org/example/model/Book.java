package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    public String title;
    public int price;
    public String publisher;
    public String authors;
    public int sale_price;
    public String isbn;

    public String printBookInfo() {
        return this.title + " | " +  this.price + " | "
                + this.publisher + " | " + this.authors + " | "
                + this.sale_price+ " | " + this.isbn;
    }

}

