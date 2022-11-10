package dao;

import model.BookTransaction;

public interface BookBidDao {
    void add(BookTransaction bookTransaction);

    BookTransaction get(Integer price);

    BookTransaction getBest();
}
