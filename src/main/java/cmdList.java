import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class cmdList {
    private static final String fileName = "cmdlist.csv";

    private List<Commands> commandsList;

    cmdList()
    {
        try
        {
            Reader reader = Files.newBufferedReader(Paths.get(fileName));
            CsvToBean<Commands> parser = new CsvToBeanBuilder<Commands>(reader)
                    .withType(Commands.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            commandsList = parser.parse();

            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    void addCommandToList(Commands cmd){
        commandsList.add(cmd);
    }

    void count(int i){commandsList.get(i).increaseCount();}

    int getSize(){return commandsList.size();}

    String getCmdString(int i){
        Commands temp = commandsList.get(i);
        return temp.getCommand();
    }

    String getCmdArgument(int i){
        Commands temp = commandsList.get(i);
        return temp.getArgument();
    }

    String getCmdGuild(int i){
        Commands temp = commandsList.get(i);
        return temp.getGuild();
    }

    int getCmdId(int i){
        Commands temp = commandsList.get(i);
        return temp.getId();
    }
    void write()
    {
        try
        {
            Writer writer = Files.newBufferedWriter(Paths.get(fileName));
            StatefulBeanToCsv<Commands> beanToCsv = new StatefulBeanToCsvBuilder<Commands>(writer).build();
            beanToCsv.write(commandsList);
            writer.close();
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
            e.printStackTrace();
        }
    }

    List<Commands> getCommandsList()
    {
        return commandsList;
    }
}
