package com.bookstore.dev.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageWithElements<T> {

    private int number;
    private int size;
    private int totalPages;
    private long totalElements;
    private boolean first;
    private boolean last;
    private boolean next;
    private boolean previous;
    private List<T> elements;

    public PageWithElements(Page page, List<T> collection) {
        number = page.getNumber();
        size = page.getSize();
        totalPages = page.getTotalPages();
        totalElements = page.getTotalElements();
        first = page.isFirst();
        last = page.isLast();
        next = page.hasNext();
        previous = page.hasPrevious();
        elements = collection;
    }
}

