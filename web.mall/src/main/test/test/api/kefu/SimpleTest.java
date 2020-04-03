package test.api.kefu;

import drama.painter.core.web.utility.Dates;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Slf4j
public class SimpleTest {
	@Test
	public void test() {
		log.info(LocalDateTime.now(ZoneId.of("Asia/Shanghai")).toEpochSecond(ZoneOffset.ofHours(8)) + "");
		log.info(LocalDateTime.now(ZoneOffset.ofHours(8)).toEpochSecond(ZoneOffset.ofHours(8)) + "");
		log.info(LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(8)) + "");
		log.info(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + "");
		log.info(Dates.toDateTime(1583004579));
		log.info(Dates.toLong("2020-03-01 03:29:39") + "");
	}
}