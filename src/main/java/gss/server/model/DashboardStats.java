package gss.server.model;

import java.util.HashMap;
import java.util.Map;

public class DashboardStats {

    int onlinePlayers;
    int activeGames;
    int ioRate;

    String serverName;
    String serverVersion;

    public DashboardStats() {
    }

    public DashboardStats(int onlinePlayers, int activeGames, int ioRate, String serverName, String serverVersion) {
        this.onlinePlayers = onlinePlayers;
        this.activeGames = activeGames;
        this.ioRate = ioRate;
        this.serverName = serverName;
        this.serverVersion = serverVersion;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("onlinePlayers", onlinePlayers);
        map.put("activeGames", activeGames);
        map.put("ioRate", ioRate);
        map.put("serverName", serverName);
        map.put("serverVersion", serverVersion);
        return map;
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    public void setOnlinePlayers(int onlinePlayers) {
        this.onlinePlayers = onlinePlayers;
    }

    public int getActiveGames() {
        return activeGames;
    }

    public void setActiveGames(int activeGames) {
        this.activeGames = activeGames;
    }

    public int getIoRate() {
        return ioRate;
    }

    public void setIoRate(int ioRate) {
        this.ioRate = ioRate;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

}
