package gss.server.model;

public interface ServiceUpdateInterface {

    // default: we are going to update as soon as possible
    default int getUpdateIntervalInSeconds() {
        return 0;
    }

    public void updateService(long deltaTime);

}