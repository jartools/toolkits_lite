package com.bowlong.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bowlong.util.MapEx;

@SuppressWarnings("rawtypes")
public class IdxBean {
	// fk_兵营队列_用户英雄1_idx [{TABLE_NAME=兵营队列, INDEX_QUALIFIER=,
	// COLUMN_NAME=用户英雄_id, PAGES=0, ORDINAL_POSITION=1,
	// INDEX_NAME=fk_兵营队列_用户英雄1_idx, FILTER_CONDITION=null, NON_UNIQUE=false,
	// TABLE_SCHEM=null, ASC_OR_DESC=A, TYPE=3, TABLE_CAT=hoc_design,
	// CARDINALITY=0}]
	public String TABLE_NAME;
	public String INDEX_QUALIFIER;
	public String COLUMN_NAME;
	public int PAGES;
	public int ORDINAL_POSITION;
	public String INDEX_NAME;
	public String FILTER_CONDITION;
	public boolean NON_UNIQUE;
	public String TABLE_SCHEM;
	public String ASC_OR_DESC;
	public int TYPE;
	public String TABLE_CAT;
	public int CARDINALITY;

	public static IdxBean parse(Map map) {
		IdxBean r2 = new IdxBean();
		r2.TABLE_NAME = MapEx.getString(map, "TABLE_NAME");
		r2.INDEX_QUALIFIER = MapEx.getString(map, "INDEX_QUALIFIER");
		r2.COLUMN_NAME = MapEx.getString(map, "COLUMN_NAME");
		r2.PAGES = MapEx.getInt(map, "PAGES");
		r2.ORDINAL_POSITION = MapEx.getInt(map, "ORDINAL_POSITION");
		r2.INDEX_NAME = MapEx.getString(map, "INDEX_NAME");
		r2.FILTER_CONDITION = MapEx.getString(map, "FILTER_CONDITION");
		r2.NON_UNIQUE = MapEx.getBoolean(map, "NON_UNIQUE");
		r2.TABLE_SCHEM = MapEx.getString(map, "TABLE_SCHEM");
		r2.ASC_OR_DESC = MapEx.getString(map, "ASC_OR_DESC");
		r2.TYPE = MapEx.getInt(map, "TYPE");
		r2.TABLE_CAT = MapEx.getString(map, "TABLE_CAT");
		r2.CARDINALITY = MapEx.getInt(map, "CARDINALITY");
		return r2;
	}
	
	public static List<IdxBean> parse(List<Map<String, Object>> list){
		List<IdxBean> r2 = new ArrayList<>();
		for (Map map : list) {
			r2.add(parse(map));
		}
		return r2;
	}
}
