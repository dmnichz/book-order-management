package db;

import java.util.Comparator;
import java.util.TreeMap;
import model.BookTransaction;

public class BidStorage {
    public static final TreeMap<Integer, BookTransaction> booksBid
            = new TreeMap<>(Comparator.reverseOrder());
}
