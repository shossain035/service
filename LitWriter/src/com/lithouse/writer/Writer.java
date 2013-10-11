package com.lithouse.writer;

import java.util.List;

import com.lithouse.common.model.LatestRecordToDeviceItem;

public interface Writer {
	public void sendRecords ( List < LatestRecordToDeviceItem > records ) throws Exception;
}
