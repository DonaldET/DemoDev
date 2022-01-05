/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.dupcheck.impl;

import java.util.ArrayList;
import java.util.List;

import demo.don.dupcheck.domain.DataRow;
import demo.don.dupcheck.domain.DupDataReader;

/**
 * This is a test fixture that simulates reading and parsing a CSV file, and
 * creates a known data quality state to develop and test the checking
 * algorithms
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class StubDataReader implements DupDataReader {
	private String source;
	private long linesRead;
	private List<DataRow> data;
	private boolean list;

	public StubDataReader() {
		setSource(getClass().getSimpleName());
		setLinesRead(-1);
		final List<DataRow> empty = new ArrayList<DataRow>();
		setData(empty);
	}

	@Override
	public boolean loadData() {
		loadDummyData();

		return true;
	}

	private void loadDummyData() {
		List<String> rowData = new ArrayList<String>();
		rowData.add("Col1");
		rowData.add("Col2");
		rowData.add("Col3");
		DataRow rb = new DataRowBean(++linesRead, rowData);
		data.add(rb);

		rowData = new ArrayList<String>();
		rowData.add("aa");
		rowData.add("bb");
		rowData.add("c");
		rb = new DataRowBean(++linesRead, rowData);
		data.add(rb);

		rowData = new ArrayList<String>();
		rowData.add("p");
		rowData.add("q");
		rowData.add("r");
		rb = new DataRowBean(++linesRead, rowData);
		data.add(rb);

		rowData = new ArrayList<String>();
		rowData.add("");
		rowData.add("s");
		rowData.add("ttt");
		rb = new DataRowBean(++linesRead, rowData);
		data.add(rb);

		rowData = new ArrayList<String>();
		rowData.add("ww");
		rowData.add("ww");
		rowData.add("x");
		rb = new DataRowBean(++linesRead, rowData);
		data.add(rb);

		rowData = new ArrayList<String>();
		rowData.add("123");
		rowData.add("");
		rowData.add("123");
		rb = new DataRowBean(++linesRead, rowData);
		data.add(rb);

		linesRead++;
	}

	@Override
	public String getSource() {
		return source;
	}

	@Override
	public void setSource(final String source) {
		this.source = source;
	}

	@Override
	public long getLinesRead() {
		return linesRead;
	}

	private void setLinesRead(final long linesRead) {
		this.linesRead = linesRead;
	}

	@Override
	public List<DataRow> getData() {
		return data;
	}

	private void setData(final List<DataRow> data) {
		this.data = data;
	}

	@Override
	public boolean isList() {
		return list;
	}

	@Override
	public void setList(final boolean list) {
		this.list = list;
	}
}
