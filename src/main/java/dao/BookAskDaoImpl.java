package dao;

import db.AskStorage;
import model.BookTransaction;

public class BookAskDaoImpl implements BookAskDao {
    @Override
    public void add(BookTransaction bookTransaction) {
        AskStorage.booksAsk.put(bookTransaction.getPrice(), bookTransaction);
    }

    @Override
    public BookTransaction get(Integer price) {
        return AskStorage.booksAsk.get(price);
    }

    @Override
    public BookTransaction getBest() {
        int best = AskStorage.booksAsk.firstKey();
        return AskStorage.booksAsk.get(best);
    }
}
