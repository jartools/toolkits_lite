package com.bowlong.text.filter;

import java.util.ArrayList;
import java.util.List;


/**
 * 专门给文字过滤使用的hashmap
 * @author Huayco
 */
public class NoConflictHashMapForChar {
	private Entry[] values;
	private boolean isEndText = false;
	private int length;

	public NoConflictHashMapForChar(int size){
		values = new Entry[size + 1];
		length = size;
	}

	public NoConflictHashMapForChar(){
		this(10);
	}
		
	public boolean put(char key, TrieFilter value){
		if(key == 0){
			isEndText = true;
			return true;
		}
		int newLength = length;
		List<Character> keys = new ArrayList<Character>();
		List<Integer> positions = new ArrayList<Integer>();
		for(int i = 0; i < values.length; i++){
			if(values[i] != null){
				keys.add(values[i].hash);
				positions.add(i);
			}
		}
		if(!keys.contains(key)){ // 新的key
			keys.add(key);
			if(positions.contains(key & newLength)){ // 冲突
				newLength++;// 加到不冲突为止
				positions.clear();
				for(int i = 0; i < keys.size(); i++){
					if(positions.contains(keys.get(i) & newLength)){
						newLength++;// 加到不冲突为止
						positions.clear();
						i = -1;
					}else{
						positions.add(keys.get(i) & newLength);
					}
				}
				Entry[] vs = new Entry[newLength + 1];
				for(Entry entry: values){
					if(entry != null)
						vs[entry.hash & newLength] = entry;
				}
				values = vs;
			}
		}
		length = newLength;
		Entry entry = new Entry();
		entry.hash = key;
		entry.v = value;
		if(values[entry.hash & newLength] != null){
			System.err.println("=====================================================\r\n********************************************");
			System.err.println("be carefull, hasmap error!....key=" + key +",old=" + (char)(values[entry.hash & newLength].hash));
			System.err.println("********************************************\r\n=====================================================");
		}
		values[entry.hash & newLength] =  entry;
		return true;
	}
	static int indexOf(char key, int length){
		return key & length;
	}
	public TrieFilter get(char key){
		if(key == 0)
			return null;
		Entry entry = values[indexOf(key, length)];
		if(entry != null && entry.hash == key){
			return entry.v;
		}
		return null;
	}

	public boolean containsKey(char key){
		if(key == 0)
			return isEndText;
		Entry v = values[indexOf(key, length)];
		return v != null && v.hash == key;
	}
	
	static class Entry{
		char hash;
		TrieFilter v;
	}
}


