package dao;

import model.BookTransaction;

public interface BookAskDao {
    void add(BookTransaction bookTransaction);

    BookTransaction get(Integer price);

    BookTransaction getBest();
}
