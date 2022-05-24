package gss.network;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class GssProtocolCodec implements ProtocolCodecFactory {

    private GssMessageEncoder encoder = new GssMessageEncoder();
    private GssMessageDecoder decoder = new GssMessageDecoder();
    
    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {        
        return decoder;
    }
    
}
