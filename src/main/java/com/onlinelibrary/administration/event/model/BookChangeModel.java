package com.onlinelibrary.administration.event.model;

import com.onlinelibrary.administration.entity.Book;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BookChangeModel {
    private String type;
    private String action;
    private Book book;
}
