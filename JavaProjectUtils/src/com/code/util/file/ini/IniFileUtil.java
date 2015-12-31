package com.code.util.file.ini;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类名：读取配置类<br>
 * 
 * @author Phonnie
 * 
 */
public class IniFileUtil {

	/**
	 * 整个ini的引用
	 */
	private Map<String, List<String>> map = null;

	/**
	 * 读取
	 * 
	 * @param path
	 */
	public IniFileUtil(String path) {
		map = new HashMap<String, List<String>>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			read(reader);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("IO Exception:" + e);
		}

	}

	/**
	 * 读取
	 * 
	 * @param path
	 */
	public IniFileUtil(File file) {
		map = new HashMap<String, List<String>>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			read(reader);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("IO Exception:" + e);
		}

	}

	/**
	 * 读取文件
	 * 
	 * @param reader
	 * @throws IOException
	 */
	private void read(BufferedReader reader) throws IOException {
		String line = null;
		while ((line = reader.readLine()) != null) {
			parseLine(line);
		}
	}

	/**
	 * 转换
	 * 
	 * @param line
	 */
	private void parseLine(String line) {
		line = line.trim();
		// 此部分为注释
		if (line.matches("^\\#.*$")) {
			return;
		} else if (line.matches("^\\S+=.*$")) {
			// key ,value
			int i = line.indexOf("=");
			String key = line.substring(0, i).trim();
			String value = line.substring(i + 1).trim();
			addKeyValue(map, key, value);
		}
	}

	/**
	 * 增加新的Key和Value
	 * 
	 * @param map
	 * @param key
	 * @param value
	 */
	private void addKeyValue(Map<String, List<String>> map, String key, String value) {
		List<String> list = map.get(key);
		if(list == null){
			List<String> newList = new ArrayList<String>();
			newList.add(value);
			map.put(key, newList);
		} else {
			list.add(value);
		}
	}

	/**
	 * 获取配置文件指定子键的值
	 * 
	 * @param key
	 * @return
	 */
	public List<String> get(String key) {
		return map.get(key);
	}

	/**
	 * 获取这个配置文件的节点和值
	 * 
	 * @return
	 */
	public Map<String, List<String>> get() {
		return map;
	}

}