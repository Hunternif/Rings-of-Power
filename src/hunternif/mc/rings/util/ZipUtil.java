package hunternif.mc.rings.util;

import hunternif.mc.rings.RingsOfPower;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ZipUtil {
	public static byte[] decompressByteArray(byte[] bytes, int offset) {
		ByteArrayOutputStream baos = null;
		Inflater iflr = new Inflater();
		iflr.setInput(bytes, offset, bytes.length - offset);
		baos = new ByteArrayOutputStream();
		byte[] tmp = new byte[4 * 1024];
		try {
			while (!iflr.finished()) {
				int size = iflr.inflate(tmp);
				baos.write(tmp, 0, size);
			}
		} catch (Exception ex) {
			RingsOfPower.logger.severe("Error unzipping bytes: " + ex.toString());
		} finally {
			try {
				if (baos != null)
					baos.close();
			} catch (Exception ex) {
				RingsOfPower.logger.severe("Error unzipping bytes: " + ex.toString());
			}
		}
		return baos.toByteArray();
	}
	
	public static byte[] compressByteArray(byte[] bytes) {
		ByteArrayOutputStream baos = null;
		Deflater dfl = new Deflater();
		dfl.setLevel(Deflater.BEST_COMPRESSION);
		dfl.setInput(bytes);
		dfl.finish();
		baos = new ByteArrayOutputStream();
		byte[] tmp = new byte[4 * 1024];
		try {
			while (!dfl.finished()) {
				int size = dfl.deflate(tmp);
				baos.write(tmp, 0, size);
			}
		} catch (Exception ex) {
			RingsOfPower.logger.severe("Error zipping bytes: " + ex.toString());
		} finally {
			try {
				if (baos != null)
					baos.close();
			} catch (Exception ex) {
				RingsOfPower.logger.severe("Error zipping bytes: " + ex.toString());
			}
		}
		return baos.toByteArray();
	}
}
