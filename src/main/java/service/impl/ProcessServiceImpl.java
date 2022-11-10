package service.impl;

import dao.BookAskDao;
import dao.BookBidDao;
import java.util.List;
import model.BookTransaction;
import service.ProcessService;

public class ProcessServiceImpl implements ProcessService {
    private static final String PARSE_SEPARATOR = ",";
    private static final String STRINGS_SEPARATOR = System.lineSeparator();
    private static final String UPDATE_STRING_PREFIX = "u";
    private static final String QUERY_STRING_PREFIX = "q";
    private static final String ORDER_STRING_PREFIX = "o";
    private static final int OPERATION_TYPE_INDEX = 0;
    private static final String UPDATE_ASK_TYPE = "ask";
    private static final String UPDATE_BID_TYPE = "bid";
    private static final int PRICE_UPDATE_INDEX = 1;
    private static final int SIZE_UPDATE_INDEX = 2;
    private static final int UPDATE_TYPE_INDEX = 3;
    private static final String QUERY_BEST_ASK = "best_ask";
    private static final String QUERY_BEST_BID = "best_bid";
    private static final String QUERY_SIZE = "size";
    private static final int QUERY_TYPE_INDEX = 1;
    private static final int QUERY_PRICE_SIZE_INDEX = 2;
    private static final String QUERY_EMPTY_RESULT = "0,0";
    private static final String QUERY_EMPTY_RESULT_SIZE = "0";
    private static final String ORDER_TYPE_BUY = "buy";
    private static final String ORDER_TYPE_SELL = "sell";
    private static final int ORDER_TYPE_INDEX = 1;
    private static final int ORDER_SIZE_INDEX = 2;
    private final BookAskDao bookAskDao;
    private final BookBidDao bookBidDao;

    public ProcessServiceImpl(BookAskDao bookAskDao, BookBidDao bookBidDao) {
        this.bookAskDao = bookAskDao;
        this.bookBidDao = bookBidDao;
    }

    @Override
    public String getReport(List<String> strings) {
        if (strings == null) {
            throw new RuntimeException("Input list can not be null");
        }
        StringBuilder sb = new StringBuilder();
        for (String string : strings) {
            final String[] data = string.split(PARSE_SEPARATOR);
            if (data[OPERATION_TYPE_INDEX].equals(UPDATE_STRING_PREFIX)) {
                processUpdate(data);
            }
            if (data[OPERATION_TYPE_INDEX].equals(QUERY_STRING_PREFIX)) {
                sb.append(processQuery(data));
            }
            if (data[OPERATION_TYPE_INDEX].equals(ORDER_STRING_PREFIX)) {
                processOrder(data);
            }
        }
        return sb.toString();
    }

    private void processUpdate(String[] data) {
        final int price = Integer.parseInt(data[PRICE_UPDATE_INDEX]);
        final int size = Integer.parseInt(data[SIZE_UPDATE_INDEX]);
        BookTransaction bookTransaction = new BookTransaction();
        bookTransaction.setPrice(price);
        bookTransaction.setSize(size);
        if (data[UPDATE_TYPE_INDEX].equals(UPDATE_ASK_TYPE)) {
            bookAskDao.add(bookTransaction);
        }
        if (data[UPDATE_TYPE_INDEX].equals(UPDATE_BID_TYPE)) {
            bookBidDao.add(bookTransaction);
        }
    }

    private String processQuery(String[] data) {
        final String queryType = data[QUERY_TYPE_INDEX];
        StringBuilder sb = new StringBuilder();
        if (queryType.equals(QUERY_BEST_ASK)) {
            if (bookAskDao.getBest() == null) {
                sb.append(QUERY_EMPTY_RESULT);
            } else {
                sb.append(bookAskDao.getBest().getPrice())
                        .append(PARSE_SEPARATOR)
                        .append(bookAskDao.getBest().getSize());
            }
        }
        if (queryType.equals(QUERY_BEST_BID)) {
            if (bookBidDao.getBest() == null) {
                sb.append(QUERY_EMPTY_RESULT);
            } else {
                sb.append(bookBidDao.getBest().getPrice())
                        .append(PARSE_SEPARATOR)
                        .append(bookBidDao.getBest().getSize());
            }
        }
        if (queryType.equals(QUERY_SIZE)) {
            final int price = Integer.parseInt(data[QUERY_PRICE_SIZE_INDEX]);
            if (bookAskDao.get(price) == null
                    && bookBidDao.get(price) == null) {
                sb.append(QUERY_EMPTY_RESULT_SIZE);
            }
            if (bookAskDao.get(price) != null
                    && bookBidDao.get(price) == null) {
                sb.append(bookAskDao.get(price).getSize());
            }
            if (bookAskDao.get(price) == null
                    && bookBidDao.get(price) != null) {
                sb.append(bookBidDao.get(price).getSize());
            }
        }
        sb.append(STRINGS_SEPARATOR);
        return sb.toString();
    }

    private void processOrder(String[] data) {
        final int size = Integer.parseInt(data[ORDER_SIZE_INDEX]);
        BookTransaction bookTransaction = new BookTransaction();
        if (data[ORDER_TYPE_INDEX].equals(ORDER_TYPE_BUY)) {
            int newSize = bookAskDao.getBest().getSize() - size;
            if (newSize < 0) {
                newSize = 0;
            }
            bookTransaction.setPrice(bookAskDao.getBest().getPrice());
            bookTransaction.setSize(newSize);
            bookAskDao.add(bookTransaction);
        }
        if (data[ORDER_TYPE_INDEX].equals(ORDER_TYPE_SELL)) {
            int newSize = bookBidDao.getBest().getSize() - size;
            if (newSize < 0) {
                newSize = 0;
            }
            bookTransaction.setPrice(bookBidDao.getBest().getPrice());
            bookTransaction.setSize(newSize);
            bookBidDao.add(bookTransaction);
        }
    }
}
