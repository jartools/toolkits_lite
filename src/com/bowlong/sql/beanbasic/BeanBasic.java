package com.bowlong.sql.beanbasic;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.bowlong.basic.ExToolkit;
import com.bowlong.sql.SqlEx;

/**
 * 添加bean 基础类
 * 
 * @author canyon/龚阳辉
 * @time 2019-02-18 17:24
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class BeanBasic extends ExToolkit implements RsTHandler<BeanBasic>, Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
	static final public String insFmt = "INSERT INTO `%s` (%s) VALUES (%s)";
	static final public String selFmt = "SELECT * FROM `%s` WHERE ";
	static final public String upFmt = "UPDATE `%s` SET %s WHERE %s";
	static final public String delFmt = "DELETE FROM `%s` WHERE %s";
	static private long mCursor = 0;

	static final public long getCursor() {
		return mCursor;
	}

	protected long mMKey = 0; // 数据库主键id标识
	protected long mCKey = 0; // 对象实例唯一标识(非全局唯一表示，而是new Class的Class的标识)
	private long mLTime = 0; // 最后操作时间
	private int mDBType = 0; // 当前数据类型

	public long getmMKey() {
		return mMKey;
	}

	public void setmMKey(long mMKey) {
		this.mMKey = mMKey;
	}

	public long getmCKey() {
		return mCKey;
	}

	public void setmCKey(long mCKey) {
		this.mCKey = mCKey;
	}

	public long getmLTime() {
		return mLTime;
	}

	public int getmDBType() {
		return mDBType;
	}

	public void setmDBType(int mDBType) {
		this.mDBType = mDBType;
	}

	public BeanBasic() {
		super();
		this.newCKey();
	}

	public BeanBasic(long mMKey) {
		this();
		this.mMKey = mMKey;
	}

	public BeanBasic(long mMKey, long mCKey) {
		super();
		this.mMKey = mMKey;
		this.mCKey = mCKey;
	}

	private Map<String, Object> toMap(ResultSet rs) throws SQLException {
		return SqlEx.toMap(rs);
	}

	@Override
	public BeanBasic handle(ResultSet rs) throws SQLException {
		return toEntity(rs);
	}

	public <T extends BeanBasic> T toEntity(ResultSet rs) throws SQLException {
		return _newEntity(toMap(rs));
	}

	/** 必须实现:new对象并调用toEntity函数 */
	protected <T extends BeanBasic> T _newEntity(Map map) {
		return null; // 例如 return new BeanBasic().toEntity(map);
	}

	/** map -> entity */
	public <T extends BeanBasic> T toEntity(Map map) {
		return null;
	}

	/** entity -> map,子类想外部访问，可以将修饰语改成public */
	protected Map<String, Object> toMap(Map<String, Object> map) {
		return map;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = newMapT();
		return toMap(map);
	}

	protected Map<String, Object> toMap4Client(Map<String, Object> map) {
		return toMap(map);
	}

	public Map<String, Object> toMap4Client() {
		Map<String, Object> map = newMapT();
		return toMap4Client(map);
	}

	protected Map<String, Object> toMap4Html(Map<String, Object> map) {
		return toMap(map);
	}

	public Map<String, Object> toMap4Html() {
		Map<String, Object> map = newMapT();
		return toMap4Html(map);
	}

	protected Map<String, Object> toMap4Json(Map<String, Object> map) {
		return toMap(map);
	}

	public Map<String, Object> toMap4Json() {
		Map<String, Object> map = newMapT();
		return toMap4Json(map);
	}

	public void newLastTime() {
		this.mLTime = now();
	}

	public void newCKey() {
		this.mCKey = (++mCursor);
	}

	public <T extends BeanBasic> T toSave() {
		T cObj = (T) Cache.borrowObject(this.getClass(),false);
		cObj.setmMKey(this.mMKey);
		cObj.setmCKey(this.mCKey);
		cObj.setmDBType(0);
		cObj.newLastTime();
		return cObj;
	}

	/** 清除部分 */
	public void clear() {
		this.mMKey = 0;
		this.mCKey = 0;
		this.mLTime = 0;
		this.mDBType = 0;
	}

	/** 清除全部 */
	public void clearAll() {
		clear();
	}
}
