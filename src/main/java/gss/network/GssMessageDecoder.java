package gss.network;

import java.io.DataInputStream;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;


/**
 *
 * @author deniz
 */
public class GssMessageDecoder extends CumulativeProtocolDecoder {
        
    private GssIoHandler ioHandler;
    
    public GssMessageDecoder(GssIoHandler ioHandler) {
        this.ioHandler = ioHandler;
    }

    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {

        int streamStartPos = in.position();
        
        // packet header size (4 for length)        
        if (in.prefixedDataAvailable(4)) {
        
            DataInputStream din = new DataInputStream(in.asInputStream());                    

            int len = in.getInt();                    
            int rem = in.remaining();

            if (rem < len) {
                in.position(streamStartPos);
                din.close();
                return false;                        
            }

            GssConnection cb = ioHandler.getConnection(session);
            if (cb == null) {
                din.close();
                throw new Exception("GssConnection is NULL");
            }
            
            GssMethod mc = new GssMethod(cb);
            out.write(mc.deserialize(din));

            din.close();

            return true;
        }

        // not enough data to read. Wait for the next call.
        in.position(streamStartPos);
        return false;
    
    }

}
