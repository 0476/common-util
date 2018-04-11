package com.ailikes.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @date 2014-07-24
 */
public final class ExceptionUtil {

	private ExceptionUtil() {
	}

	private static Logger logger = LoggerFactory.getLogger(ExceptionUtil.class);

	/**
	 * 将异常信息以字符串形式打印出来
	 * 
	 * @param e
	 *            Exception
	 * @return
	 * @date 2012-9-12
	 */
	public static String toString(Throwable e) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = null;
		try {
			ps = new PrintStream(baos, true, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			logger.warn(e1.getMessage(), e1);
		}
		StringBuilder result = new StringBuilder();
		try {
			if (e.getCause() != null) {
				// 如果有原因
				e.getCause().printStackTrace(ps);
			} else {
				// 如果没有原因
				e.printStackTrace(ps);
			}
			result.append(baos.toString("UTF-8"));
		} catch (Exception e2) {
			logger.warn(e2.getMessage(), e2);
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (Exception e2) {
				logger.warn(e2.getMessage(), e2);
			}
			try {
				if (baos != null) {
					baos.close();
				}
			} catch (Exception e2) {
				logger.warn(e2.getMessage(), e2);
			}
		}

		return result.toString();
	}
}
