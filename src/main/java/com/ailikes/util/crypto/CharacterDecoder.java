package com.ailikes.util.crypto;

import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public abstract class CharacterDecoder {

    /** Return the number of bytes per atom of decoding */
    abstract protected int bytesPerAtom();

    /** Return the maximum number of bytes that can be encoded per line */
    abstract protected int bytesPerLine();

    /** decode the beginning of the buffer, by default this is a NOP. */
    protected void decodeBufferPrefix(PushbackInputStream aStream,
                                      OutputStream bStream)
            throws IOException {
    }

    /** decode the buffer suffix, again by default it is a NOP. */
    protected void decodeBufferSuffix(PushbackInputStream aStream,
                                      OutputStream bStream)
            throws IOException {
    }

    protected int decodeLinePrefix(PushbackInputStream aStream,
                                   OutputStream bStream)
            throws IOException {
        return (bytesPerLine());
    }

    protected void decodeLineSuffix(PushbackInputStream aStream,
                                    OutputStream bStream)
            throws IOException {
    }

    protected void decodeAtom(PushbackInputStream aStream,
                              OutputStream bStream,
                              int l)
            throws IOException {
        throw new CEStreamExhausted();
    }

    protected int readFully(InputStream in,
                            byte buffer[],
                            int offset,
                            int len)
            throws java.io.IOException {
        for (int i = 0; i < len; i++) {
            int q = in.read();
            if (q == -1)
                return ((i == 0) ? -1 : i);
            buffer[i + offset] = (byte) q;
        }
        return len;
    }

    public void decodeBuffer(InputStream aStream,
                             OutputStream bStream)
            throws IOException {
        int i;
        int totalBytes = 0;

        PushbackInputStream ps = new PushbackInputStream(aStream);
        decodeBufferPrefix(ps, bStream);
        while (true) {
            int length;

            try {
                length = decodeLinePrefix(ps, bStream);
                for (i = 0; (i + bytesPerAtom()) < length; i += bytesPerAtom()) {
                    decodeAtom(ps, bStream, bytesPerAtom());
                    totalBytes += bytesPerAtom();
                }
                if ((i + bytesPerAtom()) == length) {
                    decodeAtom(ps, bStream, bytesPerAtom());
                    totalBytes += bytesPerAtom();
                } else {
                    decodeAtom(ps, bStream, length - i);
                    totalBytes += (length - i);
                }
                decodeLineSuffix(ps, bStream);
            } catch (CEStreamExhausted e) {
                break;
            }
        }
        decodeBufferSuffix(ps, bStream);
    }

    public byte decodeBuffer(String inputString)[] throws IOException {
        byte inputBuffer[] = null;// new byte[inputString.length()];
        ByteArrayInputStream inStream;
        ByteArrayOutputStream outStream;
        inputBuffer = inputString.getBytes();
        // inputString.getBytes(0, inputString.length(), inputBuffer, 0);
        inStream = new ByteArrayInputStream(inputBuffer);
        outStream = new ByteArrayOutputStream();
        decodeBuffer(inStream, outStream);
        return (outStream.toByteArray());
    }

    public byte decodeBuffer(InputStream in)[] throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        decodeBuffer(in, outStream);
        return (outStream.toByteArray());
    }

    public ByteBuffer decodeBufferToByteBuffer(String inputString) throws IOException {
        return ByteBuffer.wrap(decodeBuffer(inputString));
    }

    public ByteBuffer decodeBufferToByteBuffer(InputStream in) throws IOException {
        return ByteBuffer.wrap(decodeBuffer(in));
    }
}
