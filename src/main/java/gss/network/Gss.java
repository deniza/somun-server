package gss.network;

import gss.GssLogger;
import java.io.IOException;
import java.net.InetSocketAddress;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoServiceStatistics;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

/**
 *
 * @author deniz
 */
public class Gss {
    
    private static boolean debugFunctionCalls = false;
    
    private static NioSocketAcceptor serverAcceptor;
    private static GssConnection clientConnection;
    
    public static void startServer(GssConfig config) {
      
        final int messageExecutorThreadCount = config.getThreadsCount();
                
        serverAcceptor = new NioSocketAcceptor();

        GssIoHandler handler = new GssIoHandler(config.getMoules());        
        serverAcceptor.setHandler(handler);
        
        serverAcceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new GssProtocolCodec(handler)));
        serverAcceptor.getFilterChain().addLast("threadPool", new ExecutorFilter(messageExecutorThreadCount, messageExecutorThreadCount)); 

        serverAcceptor.setReuseAddress(true);        
        
        try {
            serverAcceptor.bind(new InetSocketAddress(config.getLocalIp(), config.getLocalPort()));
            log("server started on " + config.getLocalIp() + ":" + config.getLocalPort() + " thread count: " + messageExecutorThreadCount);
        }
        catch (IOException ex) {
            ex.printStackTrace(System.out);
            System.exit(-1);
        }
        
    }

    public static GssConnection startClient(GssConfig config) {
        
        GssIoHandler handler = new GssIoHandler(config.getMoules());
        
        NioSocketConnector connector = new NioSocketConnector();        
        connector.setHandler(handler);
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new GssProtocolCodec(handler)));
        
        ConnectFuture connFuture = connector.connect(new InetSocketAddress(config.getRemoteIp(), config.getRemotePort()));
        connFuture.awaitUninterruptibly();

        clientConnection = handler.getFirstConnection();
        return clientConnection;

    }        
    
    public static long getServerIoWrittenBytes() {
        
        IoServiceStatistics stats = serverAcceptor.getStatistics();
        return stats.getWrittenBytes();
        
    }
    
    public static void enableDebugFunctionCalls(boolean enable) {
        debugFunctionCalls = enable;
    }
    
    public static boolean isDebugFunctionCallsEnabled() {
        return debugFunctionCalls;
    }
    
    public static void shutdownServer() {
        serverAcceptor.unbind();
        serverAcceptor.dispose(true);  
        
        log("server shutdown");
    }

    public static void shutdownClient() {
        clientConnection.closeConnection();
        log("client shutdown");
    }
    
    private static void log(String message) {
        GssLogger.info(message);
    }
    
}
