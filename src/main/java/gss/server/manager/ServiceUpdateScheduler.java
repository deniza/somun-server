package gss.server.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import gss.server.model.ServiceUpdateInterface;
import gss.server.util.Time;

public class ServiceUpdateScheduler {

    private final HashMap<ServiceUpdateInterface, ServiceRecord> serviceRecords = new HashMap<>();
    private final ArrayList<ServiceUpdateInterface> services = new ArrayList<>();

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    
    private static volatile ServiceUpdateScheduler instance;

    private ServiceUpdateScheduler() {
        // Prevent form the reflection api.
        if (instance != null) {
            throw new RuntimeException("Use get() method to get the single instance of this class.");
        }
    }

    public static ServiceUpdateScheduler get() {
        // Double check locking pattern
        if (instance == null) {
            synchronized (ServiceUpdateScheduler.class) {
                if (instance == null) {
                    instance = new ServiceUpdateScheduler();
                }
            }
        }
        return instance;
    }

    public void start() {
        executorService.scheduleAtFixedRate(() -> updateAll(), 0, 1, TimeUnit.SECONDS);
    }

    public void shutdown() {
        executorService.shutdownNow();
    }

    public void register(ServiceUpdateInterface service) {

        ServiceRecord record = new ServiceRecord(service.getUpdateIntervalInSeconds(), Time.now());

        serviceRecords.put(service, record);
        services.add(service);

    }

    private synchronized void updateAll() {

        for (ServiceUpdateInterface service : services) {

            ServiceRecord record = serviceRecords.get(service);
            long now = Time.now();
            long deltaTime = now - record.lastUpdateTimestamp;

            if (deltaTime >= record.updateIntervalInSecs * 1000) {

                record.lastUpdateTimestamp = now;

                try {
                    service.updateService(deltaTime);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

    }

    private class ServiceRecord {
        public final int updateIntervalInSecs;
        public long lastUpdateTimestamp;
        public ServiceRecord(int updateIntervalInSecs, long lastUpdateTimestamp) {
            this.updateIntervalInSecs = updateIntervalInSecs;
            this.lastUpdateTimestamp = lastUpdateTimestamp;
        }
    }


}