package dao;

import db.BidStorage;
import model.BookTransaction;

public class BookBidDaoImpl implements BookBidDao {

    @Override
    public void add(BookTransaction bookTransaction) {
        BidStorage.booksBid.put(bookTransaction.getPrice(), bookTransaction);
    }

    @Override
    public BookTransaction get(Integer price) {
        return BidStorage.booksBid.get(price);
    }

    @Override
    public BookTransaction getBest() {
        int best = BidStorage.booksBid.firstKey();
        return BidStorage.booksBid.get(best);
    }
}
