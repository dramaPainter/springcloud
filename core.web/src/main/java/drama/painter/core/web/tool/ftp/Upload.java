package drama.painter.core.web.tool.ftp;

import drama.painter.core.web.misc.Result;
import drama.painter.core.web.utility.Dates;
import drama.painter.core.web.config.WebSecurity;
import drama.painter.core.web.utility.Randoms;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.threads.TaskThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author murphy
 */
@Slf4j
public class Upload {
	private static final int STEP = 2;
	private static final int ZERO = 0;
	private static ExecutorService POOL = null;

	@Autowired
	private FtpConfig.FtpConfigProperties ftpConfig;

	private void init(int size) {
		if (POOL == null) {
			synchronized (this) {
				if (POOL == null) {
					POOL = new ThreadPoolExecutor(size, size, ZERO, TimeUnit.MILLISECONDS, new LinkedBlockingQueue(size),
						new TaskThreadFactory("FtpUploadThread", false, Thread.NORM_PRIORITY));
				}
			}
		}
	}

	public Result upload(Object file, String filePath, long id) {
		WebSecurity.PageUserDetails userDetails = (WebSecurity.PageUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int userid = userDetails.getUser().getUserid();
		try {
			return new FileUploader(file, ftpConfig.getBasePath(), filePath, userid, id, ZERO).call();
		} catch (Exception e) {
			log.error("文件上传失败", e);
			return new Result(Result.CODE_FAIL, e.getMessage());
		}
	}

	public Result uploadList(List<?> files, String filePath, long id) {
		WebSecurity.PageUserDetails userDetails = (WebSecurity.PageUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int userid = userDetails.getUser().getUserid();
		int size = files.size();
		init(size);
		List<FutureTask<Result>> list = null;
		try {
			list = new ArrayList(size);
			if (files.get(0) instanceof String) {
				for (int i = 0; i < size; i += STEP) {
					FutureTask task = new FutureTask(new FileUploader(files.get(i + 1), ftpConfig.getBasePath(), filePath, userid, id, i / 2));
					POOL.submit(task);
					list.add(task);
				}
			} else {
				for (int i = 0; i < size; i++) {
					FutureTask task = new FutureTask(new FileUploader(files.get(i), ftpConfig.getBasePath(), filePath, userid, id, i));
					POOL.submit(task);
					list.add(task);
				}
			}

			List<String> url = new ArrayList(size);
			Result r = new Result(Result.CODE_SUCCESS, "文件上传成功");
			for (FutureTask<Result> o : list) {
				if (o.get().getCode() < ZERO) {
					r = o.get();
					break;
				}
				url.add(o.get().getData().toString());
			}

			r.setData(url);
			return r;
		} catch (Exception e) {
			log.error("文件上传出错：", e);
			return new Result(Result.CODE_FAIL, "上传出现错误：" + e.getMessage());
		} finally {
			if (list != null) {
				list.clear();
			}
		}
	}

	private static class FileUploader implements Callable<Result> {
		private static final Map<String, ArgFunction<Integer, Long, Integer, String>> MAP = new HashMap();
		private Object file;
		private long id;
		private int userid;
		private int i;
		private String basePath;
		private String filePath;

		static {
			MAP.put("{i}", (userid, id, i) -> String.valueOf(i + 1));
			MAP.put("{userid}", (userid, id, i) -> String.valueOf(userid));
			MAP.put("{userid%10}", (userid, id, i) -> String.valueOf(userid % 10));
			MAP.put("{userid%100}", (userid, id, i) -> String.valueOf(userid % 100));
			MAP.put("{id}", (userid, id, i) -> String.valueOf(id));
			MAP.put("{id%100}", (userid, id, i) -> String.valueOf(id % 100));
			MAP.put("{id%100}", (userid, id, i) -> String.valueOf(id % 100));
			MAP.put("{yyyyMM}", (userid, id, i) -> Dates.toDate(System.currentTimeMillis() / 1000).substring(0, 10));
			MAP.put("{yyyyMMdd}", (userid, id, i) -> Dates.toDate(System.currentTimeMillis() / 1000));
			MAP.put("{yyyyMMddHHmmss}", (userid, id, i) -> Dates.toDigitalTime(System.currentTimeMillis() / 1000));
			MAP.put("{uuid}", (userid, id, i) -> Randoms.getNonceString());
		}

		public FileUploader(Object file, String basePath, String filePath, int userid, long id, int i) {
			this.file = file;
			this.basePath = basePath;
			this.filePath = filePath;
			this.userid = userid;
			this.id = id;
			this.i = i;
		}

		@Override
		public Result call() throws IOException {
			FtpEnum.UPLOAD_NOT_EXIST.doAssert(file == null);
			MAP.entrySet().stream()
				.filter(x -> filePath.contains(x.getKey()))
				.forEach(y -> filePath = filePath.replace(y.getKey(), y.getValue().apply(userid, id, i)));

			InputStream stream;
			if (file instanceof MultipartFile) {
				stream = ((MultipartFile) file).getInputStream();
			} else {
				byte[] bytes = Base64.getDecoder().decode(file.toString());
				stream = new ByteArrayInputStream(bytes);
			}

			FtpPool.upload(stream, basePath, filePath);
			return new Result(Result.CODE_SUCCESS, "文件上传成功", filePath);
		}

		@FunctionalInterface
		private interface ArgFunction<T, U, V, R> {
			/**
			 * 多参数函数接口
			 * @param t
			 * @param u
			 * @param v
			 * @return
			 */
			R apply(T t, U u, V v);
		}
	}
}