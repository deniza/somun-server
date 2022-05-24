package gss.network;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 *
 * @author deniz
 */
public class GssMessageEncoder extends ProtocolEncoderAdapter {

    public void encode(IoSession is, Object message, ProtocolEncoderOutput out) throws Exception {

        GssMethod p = (GssMethod) message;

        IoBuffer buf = IoBuffer.allocate(256, false);                        
        buf.setAutoExpand(true);

        buf.putInt(0);//skip length

        int dataLength = p.serialize(buf);
        buf.putInt(0, dataLength);  // write length at the start of this buffer

        buf.flip();
        out.write(buf);
        buf.free();
    
    }

}
