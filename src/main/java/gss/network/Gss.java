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
    private static NioSocketAcceptor acceptor;
    
    public static void startServer(GssConfig config) {
      
        final int messageExecutorThreadCount = config.getThreadsCount();
                
        acceptor = new NioSocketAcceptor();
        acceptor.setHandler(new GssIoHandler(config.getMoules()));
        
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new GssProtocolCodec()));
        acceptor.getFilterChain().addLast("threadPool", new ExecutorFilter(messageExecutorThreadCount, messageExecutorThreadCount)); 

        acceptor.setReuseAddress(true);        
        
        try {
            acceptor.bind(new InetSocketAddress(config.getLocalIp(), config.getLocalPort()));
            log("server started on " + config.getLocalIp() + ":" + config.getLocalPort() + " thread count: " + messageExecutorThreadCount);
        }
        catch (IOException ex) {
            ex.printStackTrace(System.out);
            System.exit(-1);
        }
        
    }

    public static void startClient(GssConfig config) {
        
        GssIoHandler handler = new GssIoHandler(config.getMoules());
        
        NioSocketConnector connector = new NioSocketConnector();        
        connector.setHandler(handler);
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new GssProtocolCodec()));
        
        ConnectFuture connFuture = connector.connect(new InetSocketAddress(config.getRemoteIp(), config.getRemotePort()));
        connFuture.awaitUninterruptibly();

    }        
    
    public static long getIoWrittenBytes() {
        
        IoServiceStatistics stats = acceptor.getStatistics();
        return stats.getWrittenBytes();
        
    }
    
    public static void enableDebugFunctionCalls(boolean enable) {
        debugFunctionCalls = enable;
    }
    
    public static boolean isDebugFunctionCallsEnabled() {
        return debugFunctionCalls;
    }
    
    public static void shutdown() {        
        acceptor.unbind();
        acceptor.dispose(true);        
    }
    
    private static void log(String message) {
        GssLogger.info(message);
    }
    
}
