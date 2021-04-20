import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

public class Kawagawa_Memes
        extends ListenerAdapter {
    private final cmdList cmds = new cmdList();
    private static JDA jda;

    Message commands;
    
    public static void main(String[] args) throws LoginException {
        String token = null;
        BufferedReader reader;
        try
        {
            reader = new BufferedReader(new FileReader("token.txt"));
            token = reader.readLine();
            reader.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        jda =  JDABuilder.createDefault(token).build();
        jda.addEventListener(new Kawagawa_Memes());
    }

    PlayerManager manager1 = PlayerManager.getInstance();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        AudioManager manager;
        System.out.println( event.getAuthor().getName() + ": " + event.getMessage().getContentRaw());
        String id = event.getChannel().getId();
        if (event.getAuthor().isBot()) return;

        for (int i = 0; i < cmds.getSize(); i++) {
            if (event.getMessage().getContentRaw().equals(cmds.getCmdString(i))) {
                if (event.getGuild().getId().equals(cmds.getCmdGuild(i))) {
                    cmds.count(i);
                    if (cmds.getCmdId(i) == 1) {
                        manager1.getGuildMusicManager(event.getGuild()).player.setVolume(100);
                        try {
                            manager = event.getGuild().getAudioManager();
                            manager.openAudioConnection(event.getMember().getVoiceState().getChannel());
                            manager1.loadAndPlay2(event.getTextChannel(), cmds.getCmdArgument(i));
                        } catch (Exception e) {
                            try {
                                event.getChannel().sendFile(new File(cmds.getCmdArgument(i))).complete();
                            } catch (Exception ee) {
                                event.getChannel().sendMessage(cmds.getCmdArgument(i)).complete();
                            }
                        }
                    } else if (cmds.getCmdId(i) == 2) {
                        event.getChannel().sendMessage(cmds.getCmdArgument(i)).complete();
                    }
                    cmds.write();
                }
            }
        }
        if (event.getMessage().getContentRaw().startsWith("$adds")) {
            String[] msgArray = event.getMessage().getContentRaw().split("[\\[\\]]+");
            boolean found = false;
            for (int i = 0; i < cmds.getCommandsList().size(); i++) {
                if (msgArray[1].equals(cmds.getCmdString(i)) && cmds.getCmdId(i) == 1 && event.getGuild().getId().equals(cmds.getCmdGuild(i))) {
                    found = true;
                }
            }
            if (found) {
                event.getChannel().sendMessage("This command already exists, please" +
                        " select another name to call the command!").complete();
            } else {
                Commands command = new Commands(1, msgArray[1], msgArray[2], event.getGuild().getId());
                cmds.addCommandToList(command);
                cmds.write();
                event.getChannel().sendMessage("Added new command\n" +
                        "Trigger: " + msgArray[1] + "\n" +
                        "Plays: " + msgArray[2]).complete();
            }
        }
        if (event.getMessage().getContentRaw().startsWith("$addm")) {
            String[] msgArray = event.getMessage().getContentRaw().split("[\\[\\]]+");
            boolean found = false;
            for (int i = 0; i < cmds.getCommandsList().size(); i++) {
                if (msgArray[1].equals(cmds.getCmdString(i)) && cmds.getCmdId(i) == 2 && event.getGuild().getId().equals(cmds.getCmdGuild(i))) {
                    found = true;
                }
            }
            if (found) {
                event.getChannel().sendMessage("This command already exists, please" +
                        " select another name to call the command!").complete();
            } else {
                Commands command = new Commands(2, msgArray[1], msgArray[2], event.getGuild().getId());
                cmds.addCommandToList(command);
                cmds.write();
                event.getChannel().sendMessage("Added new command\n" +
                        "Trigger: " + msgArray[1] + "\n" +
                        "Says: " + msgArray[2]).complete();
            }
        }
        if (event.getMessage().getContentRaw().startsWith("$delete ")) {
            String[] msgArray = event.getMessage().getContentRaw().split("[\\[\\]]+");
            boolean found = false;
            for (int i = 0; i < cmds.getCommandsList().size(); i++) {
                if (msgArray[1].equals(cmds.getCmdString(i)) && event.getGuild().getId().equals(cmds.getCmdGuild(i))) {
                    found = true;
                    cmds.getCommandsList().remove(i);
                    cmds.write();
                }
            }
            if (found) {
                event.getChannel().sendMessage("Command has been deleted!").complete();
            } else {
                event.getChannel().sendMessage("That command does not exist!").complete();
            }
        }

        if (event.getMessage().getContentRaw().startsWith("$commands")) {
            MessageEmbed temp = commandsList(true, false, false, event.getGuild().getId());
            commands = event.getChannel().sendMessage(temp).complete();
            commands.addReaction("U+2b05").complete();
            commands.addReaction("U+27a1").complete();
        }
        if (event.getMessage().getContentRaw().equals("$flip")) {
            int result;
            Random randomNum = new Random();
            result = randomNum.nextInt(2);
            if (result == 0) {
                event.getChannel().sendMessage("The flip landed heads!").complete();
            } else {
                event.getChannel().sendMessage("The flip landed tails!").complete();
            }
        }
        //this is just the reactions spam with KEKW on shitpost
        if (id.equals("700636659368067092")) {
            event.getMessage().addReaction(jda.getEmoteById("695797456524279891")).complete();
        }
        if (event.getAuthor().isBot()) return;
        if (event.getMessage().getContentRaw().startsWith("$play ")) {
            String[] msgArray = event.getMessage().getContentRaw().split(" ");
            StringBuilder token = new StringBuilder();
            for (int i = 0; i < msgArray.length; ++i) {
                if (i != 0) {
                    token.append(msgArray[i]);
                }
            }
            try {
                manager = event.getGuild().getAudioManager();
                manager.openAudioConnection(event.getMember().getVoiceState().getChannel());
                manager1.loadAndPlay(event.getTextChannel(), token.toString());
                manager1.getGuildMusicManager(event.getGuild()).player.setVolume(100);
            }
            catch (Exception ignored){

            }
        }
        if (event.getMessage().getContentRaw().startsWith("$skip")) {
            try{
                manager1.skipSong(event.getTextChannel());
            }
            catch (Exception ignored){

            }
        }
        if (event.getMessage().getContentRaw().equals("$leave")) {
            try {
                manager = event.getGuild().getAudioManager();
                manager.closeAudioConnection();
            } catch (Exception ignored) {

            }
        }
        if (event.getMessage().getContentRaw().equals("$avatar")) {
            try {
                event.getChannel().sendMessage(event.getAuthor().getEffectiveAvatarUrl() + "?size=1024").queue();
            }
            catch (Exception ignored){

            }
        }
        if (event.getMessage().getContentRaw().startsWith("$avatar ")) {
            try {
                String msgArray[] = event.getMessage().getContentRaw().split(" ");
                String user_id = msgArray[1].replaceAll("[^\\d]", "");
                Guild guild = event.getGuild();
                User user = guild.getJDA().getUserById(user_id);
                event.getChannel().sendMessage(user.getEffectiveAvatarUrl() + "?size=1024").queue();
            }
            catch (Exception ignored){

            }
        }
    }

    @Override
    public void onGenericMessageReaction(@NotNull GenericMessageReactionEvent event) {
        try {
            if (!event.getUser().isBot()) {
                if (event.getMessageId().equals(commands.getId())) {
                    if (event.getReactionEmote().getAsCodepoints().equals("U+2b05")) {
                        MessageEmbed temp = commandsList(false, false, true, event.getGuild().getId());
                        commands = commands.editMessage(temp).complete();
                    } else if (event.getReactionEmote().getAsCodepoints().equals("U+27a1")) {
                        MessageEmbed temp = commandsList(false, true, false, event.getGuild().getId());
                        commands = commands.editMessage(temp).complete();
                    }
                }
            }
        }
        catch (Exception ignored){

        }
    }

    int commandsPage = 0;
    MessageEmbed commandsList (boolean reset, boolean nextpage, boolean previouspage, String guild){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm aa");
        formatter.setTimeZone(TimeZone.getTimeZone("PST"));
        Date date = new Date();

        List<Commands> listCopy = new ArrayList<>();
        for(int i = 0; i < cmds.getCommandsList().size(); i++){
            if(cmds.getCmdGuild(i).equals(guild)){
                listCopy.add(cmds.getCommandsList().get(i));
            }
        }
        listCopy.sort(Comparator.comparing(Commands::getTimes));
        Collections.reverse(listCopy);

        if(reset) {
            commandsPage = 0;
        }
        if(nextpage) {
            if(commandsPage >= listCopy.size()/35){
                commandsPage = 0;
            }
            else commandsPage++;
        }
        if(previouspage) {
            if(commandsPage == 0){
                commandsPage = listCopy.size()/35;
            }
            else commandsPage--;
        }

        int startRank = commandsPage * 35;
        int endRank = startRank + 35;

        StringBuilder desc = new StringBuilder("```css\nRank " + String.format("%-10s", "Command\t") + "Uses\n```");
        StringBuilder descTwo = new StringBuilder("```fix\n");
        StringBuilder descThree = new StringBuilder("```\n");
        for (int i = startRank; i < endRank; ++i)
        {
            if (i < listCopy.size())
            {
                Commands local = listCopy.get(i);
                if (local != null)
                {
                    if (i > 4)
                    {
                        int maxLengthName = (local.getCommand().length() < 10)?local.getCommand().length():10;
                        descThree.append(i + 1)
                                .append(i > 8 ? ". " : ".  ")
                                .append(String.format("%-10s\t", local.getCommand().substring(0, maxLengthName)))
                                .append(String.format("%-4s\t", local.getTimes())+ "\n");
                    }
                    else
                    {
                        int maxLengthName = (local.getCommand().length() < 10)?local.getCommand().length():10;
                        descTwo.append(i + 1)
                                .append(".  ")
                                .append(String.format("%-10s\t", local.getCommand().substring(0, maxLengthName)))
                                .append(String.format("%-4s\t", local.getTimes())+ "\n");
                    }
                }
            }
        }
        descTwo.append("```");
        descThree.append("```");

        EmbedBuilder lbBuilder = new EmbedBuilder();
        lbBuilder.addField("Commands - Page " + (commandsPage == 0 ? 1 : commandsPage + 1), desc.toString(), false);

        if (commandsPage == 0)
            lbBuilder.addField("", descTwo.toString(), false);
        lbBuilder.addField("", descThree.toString(),false);

        lbBuilder.setColor(new Color(0, 153, 255));
        lbBuilder.setFooter("Today at " + formatter.format(date),
                "https://i.imgur.com/vI8bfTI.png");
        return lbBuilder.build();
    }
}
