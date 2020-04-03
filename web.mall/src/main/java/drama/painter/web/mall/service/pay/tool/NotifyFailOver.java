package drama.painter.web.mall.service.pay.tool;

import drama.painter.core.web.misc.Result;
import drama.painter.core.web.tool.Json;
import drama.painter.core.web.utility.Encrypts;
import drama.painter.core.web.utility.Logs;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.apache.pulsar.shade.org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author murphy
 */
@RefreshScope
@Slf4j
@Component
public class NotifyFailOver {
	private static final String TEMPLATE_MESSAGE = "尝试第{}次连接[{}]{}";
	private static final String READ_TIMED_OUT = "Read timed out";
	private static final int MAX_CONNECTION_TIME = 10;
	private static boolean isThreadStarted = false;
	private static final Object LOCKER = new Object();
	private static ConcurrentLinkedQueue<Job> queue = new ConcurrentLinkedQueue<>();
	private String ipAddress;


	public NotifyFailOver(@Value("${api.server.dip:}") String ipAddress) {
		this.ipAddress = ipAddress;
	}

	private static byte[] getBytes(int num) {
		byte[] b = new byte[4];
		b[0] = (byte) ((num & 0xFF000000) >> 0x18);
		b[1] = (byte) ((num & 0x00FF0000) >> 0xF);
		b[2] = (byte) ((num & 0x0000FF00) >> 0x8);
		b[3] = (byte) (num & 0x000000FF);
		return reverseEndian(b, b.length);
	}

	private static byte[] reverseEndian(byte[] str, int len) {
		byte b;
		byte[] res = new byte[len];
		System.arraycopy(str, 0, res, 0, len);
		for (int i = 0; i < len; i++) {
			b = str[i];
			res[len - i - 1] = b;
		}
		return res;
	}

	private static void addJob(Job job) {
		queue.add(job);
	}

	protected String getIpAddress() {
		return ipAddress;
	}

	protected Result merge(long id, int dwMainId, int dwSubId, String json) {
		byte[] bytes = Encrypts.encryptByte(json);
		int dwDataSize = bytes.length + 1;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			os.write(getBytes(dwMainId));
			os.write(getBytes(dwSubId));
			os.write(getBytes(dwDataSize));
			os.write(bytes);
			os.write(0);
			bytes = os.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				os.close();
			} catch (IOException e) {
			} finally {
				os = null;
			}
		}

		String[] port = ipAddress.split(":");
		try {
			return sendClientInfo(port[0], Integer.parseInt(port[1]), bytes);
		} catch (IOException e) {
			log.error("Notify通知消息错：" + e);
			if (e.getMessage() == null || !e.getMessage().contains(READ_TIMED_OUT)) {
				if (!isThreadStarted) {
					synchronized (LOCKER) {
						if (!isThreadStarted) {
							log.info("启动DIP服务器调用接口");
							BasicThreadFactory factory = new BasicThreadFactory.Builder().namingPattern("NotifyThread").build();
							ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1, factory);
							service.scheduleAtFixedRate(new NotifyThread(this), 6000, 60, TimeUnit.SECONDS);
							isThreadStarted = true;
						}
					}
				}
				addJob(new Job(id, dwSubId, 1, bytes, json));
			}
			return new Result(-1, "通知消息错误：" + e.getMessage() + " 稍后会继续重试。");
		}
	}

	protected <T> Result<List<T>> sendClientInfo(String ip, int port, byte[] msg) throws IOException {
		Socket client = new Socket();
		OutputStream os = null;
		InputStream sr = null;
		byte[] values = new byte[4];
		byte[] data = null;
		client.setSoTimeout(50000);
		client.connect(new InetSocketAddress(ip, port), MAX_CONNECTION_TIME * 1000);

		try {
			os = client.getOutputStream();
			os.write(msg);
			os.flush();

			sr = client.getInputStream();
			int len = sr.read(values, 0, 4);
			if (len == -1) {
				throw new IOException("Read timed out 未能读取到服务器数据：服务器初始化已结束。");
			}
			values = reverseEndian(values, values.length);
			int length = ((values[0] & 0xFF) << 24) | ((values[1] & 0xFF) << 16) | ((values[2] & 0xFF) << 8) | (values[3] & 0xFF);
			if (length == 0) {
				sr.read(values, 0, 4);
				sr.read(values, 0, 4);
				sr.read(values, 0, 4);
				values = reverseEndian(values, values.length);
				length = ((values[0] & 0xFF) << 24) | ((values[1] & 0xFF) << 16) | ((values[2] & 0xFF) << 8) | (values[3] & 0xFF);
			}

			data = new byte[length];
			int off = 0;
			int readLength = 0;
			do {
				off = readLength + off;
				length = length - readLength;
				readLength = sr.read(data, off, length);
			} while (readLength != length);

			String decryped = Encrypts.decrypt(data);
			String text = URLDecoder.decode(decryped, "utf-8");
			if (Strings.isBlank(text)) {
				throw new IOException("服务器响应了空值。");
			}

			return Json.parseObject(text, Result.class);
		} catch (IOException e) {
			throw e;
		} finally {
			close(client, os, sr, values, data);
		}
	}

	private void close(Socket client, OutputStream os, InputStream sr, byte[] values, byte[] data) {
		try {
			if (sr != null) {
				sr.close();
			}
			if (os != null) {
				os.close();
			}
			if (!client.isClosed()) {
				client.close();
			}
			values = null;
			data = null;
			sr = null;
			os = null;
			client = null;
		} catch (IOException e) {
		}
	}

	@Data
	private static class Job<T> {
		private long id;
		private int dwSubId;
		private int times;
		private byte[] msg;
		private String json;

		public Job(long id, int dwSubId, int times, byte[] msg, String json) {
			this.id = id;
			this.dwSubId = dwSubId;
			this.times = times;
			this.msg = msg;
			this.json = json;
		}
	}

	private static class NotifyThread implements Runnable {
		private static String TIP = "Notify暂无可执行的作务，休息%d秒";
		private NotifyFailOver notifier;

		public NotifyThread(NotifyFailOver notifier) {
			this.notifier = notifier;
		}

		@Override
		public void run() {
			String[] ipPort = notifier.getIpAddress().split(":");
			String ip = ipPort[0];
			int port = Integer.parseInt(ipPort[1]);
			if (queue.size() > 0) {
				Iterator<Job> jobs = queue.iterator();
				while (jobs.hasNext()) {
					Job j = jobs.next();
					String serialize = Json.toJsonString(j);
					Result r;

					try {
						r = notifier.sendClientInfo(ip, port, j.getMsg());
					} catch (IOException e) {
						if (e.getMessage().contains(READ_TIMED_OUT)) {
							log.error(TEMPLATE_MESSAGE, j.getTimes(), "连接超时", serialize);
							continue;
						} else {
							j.setTimes(j.getTimes() + 1);
							jobs.remove();
							log.error(TEMPLATE_MESSAGE, j.getTimes(), e.getMessage(), serialize);
							continue;
						}
					}

					j.setTimes(j.getTimes() + 1);
					String msg = r.getCode() < 0 ? r.getMessage() : "成功";
					log.info(TEMPLATE_MESSAGE, j.getTimes(), msg, serialize);
					jobs.remove();
				}
			} else {
				log.debug(TIP, 10);
				Logs.sleep(10);
			}
		}
	}
}

