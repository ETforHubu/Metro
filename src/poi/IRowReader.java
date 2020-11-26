package poi;

public interface IRowReader {  

    public void processLine(int sheetIndex, String sheetname, int rowIndex, String row, MySheet currSheet,int[] TitleSequence);

    public boolean isForcedInterrupt();
}
