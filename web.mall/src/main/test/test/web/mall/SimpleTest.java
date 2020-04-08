package test.web.mall;

import drama.painter.core.web.utility.Encrypts;
import drama.painter.core.web.utility.Strings;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public class SimpleTest {
	@Test
	public void test() {
		String te = Encrypts.md5("9fj023ifji02jp1oqj");
		try {
			byte[] arr = Files.readAllBytes(new File("/Users/murphy/Downloads/soft/elasticsearch/master/config/ca.crt").toPath());
			String text = Encrypts.encrypt(new String(arr));
			log.info(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}