package com.parkit.parkingsystem.util;

import java.util.Date;

public class DateTimeImpl implements DateTime {
	@Override
	public Date getDate() {
		return new Date();
	}
}
