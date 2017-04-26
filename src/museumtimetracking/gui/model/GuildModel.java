/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.gui.model;

import java.util.ArrayList;
import java.util.List;
import museumtimetracking.be.Guild;
import museumtimetracking.bll.GuildManager;

public class GuildModel {

    private final List<Guild> guildsFromDB;

    private final List<Guild> archivedGuilds;

    private final GuildManager guildManager;

    public GuildModel() {
        guildsFromDB = new ArrayList<>();
        archivedGuilds = new ArrayList<>();
        guildManager = new GuildManager();

//        guildsFromDB = guildManager.addGuild(guildToAdd);
    }

    public List<Guild> getArchivedGuilds() {
        return archivedGuilds;
    }

    /**
     * Adds the guild to DB.
     *
     * @param guild
     */
    public void addGuild(Guild guild) {
        guildManager.addGuild(guild);
    }
    
    public List<Guild> getAllGuilds(){
        return guildManager.getAllGuilds();
    }

}
