import com.opencsv.bean.CsvBindByName;

public class Commands {
    @CsvBindByName(column = "id")
    private int id;

    @CsvBindByName(column = "command")
    private String command;

    @CsvBindByName(column = "argument")
    private String argument;

    @CsvBindByName(column = "times")
    private int times;

    @CsvBindByName(column = "guild")
    private String guild;

    public Commands(){

    }

    Commands(int _id, String _cmd, String _arg, String _guild){
        id = _id;
        command = _cmd;
        argument = _arg;
        times = 0;
        guild = _guild;
    }

    public int getId(){return id;}

    public String getCommand(){return command;}

    public String getArgument(){return argument;}

    public int getTimes(){return times;}

    public String getGuild(){return guild;}

    public void increaseCount(){times++;}
}
