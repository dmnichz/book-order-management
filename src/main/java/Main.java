import dao.BookAskDao;
import dao.BookAskDaoImpl;
import dao.BookBidDao;
import dao.BookBidDaoImpl;
import java.util.List;
import service.ProcessService;
import service.ReaderService;
import service.WriterService;
import service.impl.ProcessServiceImpl;
import service.impl.ReaderServiceImpl;
import service.impl.WriterServiceImpl;

public class Main {
    private static final String INPUT_FILE_PATH = "input.txt";
    private static final String OUTPUT_FILE_PATH = "output.txt";

    public static void main(String[] args) {
        BookBidDao bookBidDao = new BookBidDaoImpl();
        BookAskDao bookAskDao = new BookAskDaoImpl();

        ReaderService readerService = new ReaderServiceImpl();
        List<String> stringsFromFile = readerService.readFromFile(INPUT_FILE_PATH);

        WriterService writerService = new WriterServiceImpl();
        ProcessService processService = new ProcessServiceImpl(bookAskDao, bookBidDao);
        writerService.writeToFile(OUTPUT_FILE_PATH,
                processService.getReport(stringsFromFile));
    }
}
