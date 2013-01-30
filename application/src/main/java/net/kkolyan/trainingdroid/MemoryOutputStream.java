package net.kkolyan.trainingdroid;

import java.io.ByteArrayOutputStream;

/**
* @author nplekhanov
*/
public class MemoryOutputStream extends ByteArrayOutputStream {
    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        MemoryOutputStream other = (MemoryOutputStream) obj;
        if (count != other.count) {
            return false;
        }
        for (int i = 0; i < count; i ++) {
            if (buf[i] != other.buf[i]) {
                return false;
            }
        }
        return true;
    }
}
