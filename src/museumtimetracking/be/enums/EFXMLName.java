/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museumtimetracking.be.enums;

public enum EFXMLName {

    MAIN_FOLER("/museumtimetracking/gui/views/root/"),
    /*
     * Guild
     */
    GUILD_OVERVIEW(MAIN_FOLER + "guild/GuildOverview.fxml"),
    GUILD_TABLE(MAIN_FOLER + "guild/guildComponents/GuildTableView.fxml"),
    ADD_NEW_GUILD(MAIN_FOLER + "guild/newGuild/NewGuildView.fxml"),
    EDIT_GUILD("guild/editGuild/EditGuildView.fxml"),
    /*
     * Manager
     */
    MANAGER_OVERVIEW(MAIN_FOLER + "guildManager/guildManagerOverview/GuildManagerOverview.fxml"),
    NEW_MANAGER(MAIN_FOLER + "guildManager/newGuildManager/NewGuildManagerView.fxml");

    private final String text;

    private EFXMLName(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
