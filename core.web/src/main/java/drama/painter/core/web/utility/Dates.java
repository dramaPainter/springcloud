package drama.painter.core.web.utility;

import drama.painter.core.web.validator.DateValidator;
import drama.painter.core.web.validator.EmptyValidator;
import drama.painter.core.web.validator.Validator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @author murphy
 */
public class Dates {
	static final Validator EMPTY = new EmptyValidator();
	static final Validator SCAN = new DateValidator();
	static final DateTimeFormatter MDF = DateTimeFormatter.ofPattern("yyyyMM");
	static final DateTimeFormatter SDF = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	static final DateTimeFormatter FDF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	static final DateTimeFormatter DDF = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	static final ZoneId CHINA = ZoneId.of("Asia/Shanghai");
	static final ZoneOffset OFFSET = ZoneOffset.ofHours(8);

	public static String toMonth(long time) {
		return MDF.format(Instant.ofEpochSecond(time).atZone(CHINA).toLocalDateTime());
	}

	public static String toDate(long time) {
		return SDF.format(Instant.ofEpochSecond(time).atZone(CHINA).toLocalDateTime());
	}

	public static String toDateTime(long time) {
		return FDF.format(Instant.ofEpochSecond(time).atZone(CHINA).toLocalDateTime());
	}

	public static String toDigitalTime(long time) {
		return DDF.format(Instant.ofEpochSecond(time).atZone(CHINA).toLocalDateTime());
	}

	public static long toLong(String datetime) {
		datetime = datetime.contains(":") ? datetime : datetime.trim() + " 00:00:00";
		return LocalDateTime.parse(datetime, FDF).toEpochSecond(OFFSET);
	}

	public static long addDate(String date, int days) {
		return toLong(date) + days * 3600 * 24;
	}

	public static String getNowDate() {
		return SDF.format(LocalDateTime.now(CHINA));
	}

	public static long getNowLong() {
		return LocalDateTime.now(CHINA).toEpochSecond(OFFSET);
	}

	public static String getNowDateTime() {
		return FDF.format(LocalDateTime.now());
	}

	public static String get5minDate(String date, int minute) {
		long time = LocalDateTime.parse(date, SDF).toEpochSecond(OFFSET);
		time = time / (minute * 60) * (minute * 60);
		return FDF.format(Instant.ofEpochMilli(time * 1000).atZone(CHINA).toLocalDateTime());
	}

	public static long getStartTime(String startTime) {
		return getStartTime(startTime, 0);
	}

	public static long getStartTime(String startTime, int days) {
		startTime = EMPTY.validate(startTime) ? "" : startTime.trim();
		return (SCAN.validate(startTime) ? toLong(startTime) : toLong(getNowDate())) - days * 24 * 3600;
	}

	public static long getEndTime(String endTime) {
		endTime = EMPTY.validate(endTime) ? "" : endTime.trim();
		return SCAN.validate(endTime) ? toLong(endTime) : System.currentTimeMillis() / 1000;
	}
}