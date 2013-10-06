package com.lithouse.writer;

import java.util.List;

import com.lithouse.common.model.RecordToDevice;

public interface Writer {
	public void sendRecords ( List < RecordToDevice > records ) throws Exception;
}
