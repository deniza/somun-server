package gss.network;

import gss.GssLogger;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.StringTokenizer;
import org.apache.mina.core.buffer.IoBuffer;

public class GssMethod {

    public static final byte INT = 0;
    public static final byte BYTE = 1;
    public static final byte SHORT = 2;
    public static final byte LONG = 3;
    public static final byte FLOAT = 4;
    public static final byte DOUBLE = 5;
    public static final byte CHAR = 6;
    public static final byte STRING = 7;
    public static final byte ARRAY = 8;
    public static final byte OBJECT = 9;

    private Method method;
    private String methodName;
    private Object[] params;
    private Class<?>[] classTypes;
    private GssConnection connection;
    private GssInterface interfaceModule;
    
    public GssMethod(GssConnection connection) {
        this.connection = connection;
    }
    
    public GssMethod(String methodName, Object[] params) {
        
        this.methodName = methodName;
        this.params = params;
        
    }

    public GssMethod deserialize(DataInputStream in) {

        String methodcallstr = null;
        int paramLen;
        try {
            methodcallstr= in.readUTF();
            StringTokenizer stk = new StringTokenizer(methodcallstr, "_");
            String moduleName = stk.nextToken();

            methodName = stk.nextToken();
            interfaceModule = connection.getInterface(moduleName);
            if (interfaceModule == null) {
                logError("Error: interface module not found: " + moduleName);
            }

            paramLen = in.readByte();

            classTypes = new Class[paramLen + 1];
            classTypes[paramLen] = GssConnection.class;
            params = new Object[paramLen];

            for (int parameterIndex = 0; parameterIndex < paramLen; parameterIndex++) {
                try {
                    readParameter(parameterIndex, in);
                }
                catch (Exception ex) {
                    ex.printStackTrace(System.out);
                }
            }

            method = interfaceModule.getClass().getMethod(methodName, classTypes);
            return this;
        }
        catch (IOException ex) {
            logError("[GssException] IOException while deserializing method: "+methodcallstr);
            ex.printStackTrace(System.out);
        }
        catch (NoSuchMethodException ex) {
            logError("[GssException] NoSuchMethodException while deserializing method: "+methodcallstr);
            ex.printStackTrace(System.out);
        }
        catch (SecurityException ex) {
            logError("[GssException] SecurityException while deserializing method: "+methodcallstr);
            ex.printStackTrace(System.out);
        }
        return null;
    }

    private void readParameter(int parameterIndex, DataInputStream in) throws Exception {
        
        byte type = in.readByte();
        
        switch (type) {
            case INT:
                classTypes[parameterIndex] = Integer.TYPE;
                params[parameterIndex] = in.readInt();
                break;
            case BYTE:
                classTypes[parameterIndex] = Byte.TYPE;
                params[parameterIndex] = in.readByte();
                break;
            case SHORT:
                classTypes[parameterIndex] = Short.TYPE;
                params[parameterIndex] = in.readShort();
                break;
            case LONG:
                classTypes[parameterIndex] = Long.TYPE;
                params[parameterIndex] = in.readLong();
                break;
            case FLOAT:
                classTypes[parameterIndex] = Float.TYPE;
                params[parameterIndex] = in.readFloat();
                break;
            case DOUBLE:
                classTypes[parameterIndex] = Double.TYPE;
                params[parameterIndex] = in.readDouble();
                break;
            case CHAR:
                classTypes[parameterIndex] = Character.TYPE;
                params[parameterIndex] = in.readChar();
                break;
            case STRING:
                classTypes[parameterIndex] = String.class;
                params[parameterIndex] = readUTF8Raw(in);
                break;
            case ARRAY:
                params[parameterIndex] = readArray(in);
                classTypes[parameterIndex] = params[parameterIndex].getClass();
                break;
            default:
                throw new Exception("[GssException] undefined parameter type: " + type);
        }
        
    }
    
    // DataInputStream readUTF fonksiyonelligi NON BMP codepoints icin exception atiyor.
    // Yani emoji karakterleri duzgun bicimde okunamiyor. Bu karakterleri okuyabilmek icin
    // kendi donusumumuzu yapmamiz gerekti.
    private String readUTF8Raw(DataInputStream in) throws IOException {        
        short length = in.readShort();
        byte[] data = new byte[length];        
        in.read(data);        
        return new String(data, "UTF-8");
    }

    private Object readArray(DataInputStream in) throws IOException {

        int len = in.readInt();

        byte arType = in.readByte();

        if (arType == INT) {
            int[] ar = new int[len];
            for (int i = 0; i < len; i++) {
                ar[i] = in.readInt();
            }
            return ar;
        } else if (arType == BYTE) {
            byte[] ar = new byte[len];
            for (int i = 0; i < len; i++) {
                ar[i] = in.readByte();
            }
            return ar;
        } else if (arType == SHORT) {
            short[] ar = new short[len];
            for (int i = 0; i < len; i++) {
                ar[i] = in.readShort();
            }
            return ar;
        } else if (arType == LONG) {
            long[] ar = new long[len];
            for (int i = 0; i < len; i++) {
                ar[i] = in.readLong();
            }
            return ar;
        } else if (arType == FLOAT) {
            float[] ar = new float[len];
            for (int i = 0; i < len; i++) {
                ar[i] = in.readFloat();
            }
            return ar;
        } else if (arType == DOUBLE) {
            double[] ar = new double[len];
            for (int i = 0; i < len; i++) {
                ar[i] = in.readDouble();
            }
            return ar;
        } else if (arType == CHAR) {
            char[] ar = new char[len];
            for (int i = 0; i < len; i++) {
                ar[i] = in.readChar();
            }
            return ar;
        } else if (arType == STRING) {
            String[] ar = new String[len];
            for (int i = 0; i < len; i++) {
                ar[i] = readUTF8Raw(in);
            }
            return ar;
        } else if (arType == OBJECT) {
            
            Object[] ar = new Object[len];
            
            for (int i = 0; i < len; i++) {
                byte type = in.readByte();
                
                switch (type) {
                    case INT: ar[i] = in.readInt(); break;
                    case BYTE: ar[i] = in.readByte(); break;
                    case SHORT: ar[i] = in.readShort(); break;
                    case LONG: ar[i] = in.readLong(); break;
                    case FLOAT: ar[i] = in.readFloat(); break;
                    case DOUBLE: ar[i] = in.readDouble(); break;
                    case CHAR: ar[i] = in.readChar(); break;
                    case STRING: ar[i] = readUTF8Raw(in); break;
                    case ARRAY: ar[i] = readArray(in); break;
                }
                
            }
            return ar;
        } else {
            logError("Error: not supported array type encountered");
            return null;
        }

    }

    public int serialize(IoBuffer out) {
        
        int packetLength;
        int startPosition = out.position();
        
        try {
            if (checkNull()) {
                                                                
                byte [] nameBytes = methodName.getBytes("UTF-8");
                                
                out.putShort((short)nameBytes.length).put(nameBytes);
                out.put((byte)params.length);
                
                for (Object o : params) {
                    writeParameter(o, out);
                }
                
            }
        }
        catch (IOException ex) {
            logError("[GssException] IOException while serialize buffer");
            ex.printStackTrace(System.out);
        }

        packetLength = out.position() - startPosition;
        return packetLength;
        
    }

    private boolean checkNull() {
        for (Object o : params) {
            if (o == null) {
                logError("ERROR: " + methodName + " contains NULL parameter");
                return false;
            }
        }
        return true;
    }

    private void writeParameter(Object o, IoBuffer out) throws IOException {
        
        if (o.getClass() == Integer.class) {
            out.put(INT);
            out.putInt((Integer)o);
        } else if (o.getClass() == Byte.class) {
            out.put(BYTE);
            out.put((Byte)o);
        } else if (o.getClass() == Short.class) {
            out.put(SHORT);
            out.putShort((Short)o);
        } else if (o.getClass() == Long.class) {
            out.put(LONG);
            out.putLong((Long)o);
        } else if (o.getClass() == Float.class) {
            out.put(FLOAT);
            out.putFloat((Float)o);
        } else if (o.getClass() == Double.class) {
            out.put(DOUBLE);
            out.putDouble((Double)o);
        } else if (o.getClass() == Character.class) {
            out.put(CHAR);
            out.putChar((Character)o);
        } else if (o.getClass() == String.class) {
            out.put(STRING);
            String data = (String) o;
            byte[] dataBytes = data.getBytes("UTF-8");
            out.putShort((short)dataBytes.length).put(dataBytes);
        } else if (o.getClass().isArray()) {
            out.put(ARRAY);
            writeArray(o, out);
        } else {
            logError("Error: unsupported type in writeParameter");
        }
    }

    private void writeArray(Object o, IoBuffer out) throws IOException {

        int len = Array.getLength(o);
        out.putInt(len);

        Class<?> arType = o.getClass().getComponentType();

        if (arType == Integer.TYPE) {
            out.put(INT);
            int[] ar = (int[]) o;
            for (int data : ar) {
                out.putInt(data);
            }
        } else if (arType == Byte.TYPE) {
            out.put(BYTE);
            byte[] ar = (byte[]) o;
            for (byte data : ar) {
                out.put(data);
            }
        } else if (arType == Short.TYPE) {
            out.put(SHORT);
            short[] ar = (short[]) o;
            for (short data : ar) {
                out.putShort(data);
            }
        } else if (arType == Long.TYPE) {
            out.put(LONG);
            long[] ar = (long[]) o;
            for (long data : ar) {
                out.putLong(data);
            }
        } else if (arType == Float.TYPE) {
            out.put(FLOAT);
            float[] ar = (float[]) o;
            for (float data : ar) {
                out.putFloat(data);
            }
        } else if (arType == Double.TYPE) {
            out.put(DOUBLE);
            double[] ar = (double[]) o;
            for (double data : ar) {
                out.putDouble(data);
            }
        } else if (arType == Character.TYPE) {
            out.put(CHAR);
            char[] ar = (char[]) o;
            for (char data : ar) {
                out.putChar(data);
            }
        } else if (arType == String.class) {
            out.put(STRING);
            String[] ar = (String[]) o;
            for (String data : ar) {
                byte[] dataBytes = data.getBytes("UTF-8");
                out.putShort((short)dataBytes.length).put(dataBytes);
            }
            // arType.isArray()         => multidimensional arrays
            // arType == Object.class   => Object[] array = {3, "abc", 2.7 .. }
        } else if (arType == Object.class || arType.isArray()) {
            out.put(OBJECT);
            Object[] ar = (Object[]) o;
            for (Object ob : ar) {

                if (ob.getClass() == Integer.class) {
                    out.put(INT);
                    out.putInt((Integer)ob);
                } else if (ob.getClass() == Byte.class) {
                    out.put(BYTE);
                    out.put((Byte)ob);
                } else if (ob.getClass() == Short.class) {
                    out.put(SHORT);
                    out.putShort((Short)ob);
                } else if (ob.getClass() == Long.class) {
                    out.put(LONG);
                    out.putLong((Long)ob);
                } else if (ob.getClass() == Float.class) {
                    out.put(FLOAT);
                    out.putFloat((Float)ob);
                } else if (ob.getClass() == Double.class) {
                    out.put(DOUBLE);
                    out.putDouble((Double)ob);
                } else if (ob.getClass() == Character.class) {
                    out.put(CHAR);
                    out.putChar((Character)ob);
                } else if (ob.getClass() == String.class) {
                    out.put(STRING);
                    String data = (String) ob;
                    byte[] dataBytes = data.getBytes("UTF-8");
                    out.putShort((short)data.length()).put(dataBytes);
                } else if (ob.getClass().isArray()) {
                    out.put(ARRAY);
                    writeArray(ob, out);
                } else {
                    logError("Error: unsupported parameter type");
                }
            }
        }
        else {
            logError("Error: unsupported parameter type");
        }

    }

    private int getTypeSize(Object o) throws UnsupportedEncodingException {
        Class<?> type = o.getClass();
        if (type == Integer.class) {
            return Integer.SIZE / 8;              // convert bits to bytes
        } else if (type == Byte.class) {
            return Byte.SIZE / 8;
        } else if (type == Short.class) {
            return Short.SIZE / 8;
        } else if (type == Long.class) {
            return Long.SIZE / 8;
        } else if (type == Float.class) {
            return Float.SIZE / 8;
        } else if (type == Double.class) {
            return Double.SIZE / 8;
        } else if (type == Character.class) {
            return Character.SIZE / 8;
        } else if (type == String.class) {
            return ((String) o).getBytes("UTF-8").length + 2;
        } else if (type.isArray()) {
            return getArraySize(o);
        } else {
            logError("Unsupported type for serialization: " + type);
            return 0;
        }
    }

    private int getArraySize(Object o) throws UnsupportedEncodingException {
        Class<?> type = o.getClass().getComponentType();
        int arlen = Array.getLength(o);

        if (type == Integer.TYPE) {
            return getTypeSize(arlen) + getTypeSize(INT) + arlen * Integer.SIZE / 8;              // convert bits to bytes
        } else if (type == Byte.TYPE) {
            return getTypeSize(arlen) + getTypeSize(BYTE) + arlen * Byte.SIZE / 8;
        } else if (type == Short.TYPE) {
            return getTypeSize(arlen) + getTypeSize(SHORT) + arlen * Short.SIZE / 8;
        } else if (type == Long.TYPE) {
            return getTypeSize(arlen) + getTypeSize(LONG) + arlen * Long.SIZE / 8;
        } else if (type == Float.TYPE) {
            return getTypeSize(arlen) + getTypeSize(FLOAT) + arlen * Float.SIZE / 8;
        } else if (type == Double.TYPE) {
            return getTypeSize(arlen) + getTypeSize(DOUBLE) + arlen * Double.SIZE / 8;
        } else if (type == Character.TYPE) {
            return getTypeSize(arlen) + getTypeSize(CHAR) + arlen * Character.SIZE / 8;
        } else if (type == String.class) {
            //return ((String)o).getBytes("UTF-8").length + 2;
            return getTypeSize(arlen) + getTypeSize(STRING) +getStrArrSize((String[]) o);
        } else if (type == Object.class || type.isArray()) {
            int len = 0;
            for (int i = 0; i < arlen; i++) {
                len += getTypeSize(BYTE);                // INT, BYT.. ARR
                len += getTypeSize(Array.get(o, i));
            }
            return getTypeSize(arlen) + getTypeSize(OBJECT)  + len;
        } else {
            logError("Error: unsupported array type: " + type);
            return 0;
        }
    }

    private int getStrArrSize(String[] arr) {
        int len = 0;
        try {
            for (String s : arr) {
                len += s.getBytes("UTF-8").length + 2;
            }
        }
        catch (UnsupportedEncodingException e) {
            logError("[GssException] UnsupportedEncodingException getStrArrSize");
            e.printStackTrace(System.out);
        }
        return len;
    }

    public void invoke() {

        if (Gss.isDebugFunctionCallsEnabled()) {
        
            StringBuilder logBuffer = new StringBuilder();
            logBuffer.append("GssMethod.invoke(): ").append(this.methodName).append(" [ ");

            for (Object p:params) {
                logBuffer.append(p).append(" ");
            }

            logBuffer.append("] on thread ").append(Thread.currentThread().getName());

            logInfo(logBuffer.toString());
            
        }
        
        if (connection.isConnected()) {
        
            try {
                Object[] params2 = Arrays.copyOf(params, params.length + 1);
                params2[params.length] = connection;
                method.invoke(interfaceModule, params2);
            }
            catch (IllegalAccessException ex) {
                logError("[GssException] IllegalAccessException while invoking method: "+method.getName());
                ex.printStackTrace(System.out);
            }
            catch (IllegalArgumentException ex) {
                logError("[GssException] IllegalArgumentException while invoking method: "+method.getName());
                ex.printStackTrace(System.out);
            }
            catch (InvocationTargetException ex) {
                logError("[GssException] InvocationTargetException while invoking method: "+method.getName());
                ex.printStackTrace(System.out);
            }
            
        }
        else {
            logError("GSS: cb not connected " + method.getName());
        }
    }
    
    private void logError(String message) {
        GssLogger.error(message);
    }

    private void logInfo(String message) {
        GssLogger.info(message);
    }
}
