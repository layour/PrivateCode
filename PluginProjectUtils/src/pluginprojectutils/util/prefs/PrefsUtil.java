package pluginprojectutils.util.prefs;

import java.util.prefs.BackingStoreException;
import java.util.prefs.NodeChangeEvent;
import java.util.prefs.NodeChangeListener;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

public class PrefsUtil {
	public static void main(String args[]) {
		String denominations[] = { "One", "Two", "Five", "Ten", "Twenty" };
		String pictures[] = { "Washington", "Jefferson", "Lincoln", "Hamilton", "Jackson" };

		NodeChangeListener nodeChangeListener = new NodeChangeListener() {
			public void childAdded(NodeChangeEvent event) {
				Preferences parent = event.getParent();
				Preferences child = event.getChild();
				System.out.println(parent.name() + " has a new child " + child.name());
			}

			public void childRemoved(NodeChangeEvent event) {
				Preferences parent = event.getParent();
				Preferences child = event.getChild();
				System.out.println(parent.name() + " lost a child " + child.name());
			}
		};

		PreferenceChangeListener preferenceChangeListener = new PreferenceChangeListener() {
			public void preferenceChange(PreferenceChangeEvent event) {
				String key = event.getKey();
				String value = event.getNewValue();
				Preferences node = event.getNode();
				System.out.println(node.name() + " now has a value of " + value + " for " + key);
			}
		};

		// 设定需要根目录
		Preferences prefs = Preferences.userRoot().node("/net/fubin/ibm");

		// 添加监听
		prefs.addNodeChangeListener(nodeChangeListener);
		prefs.addPreferenceChangeListener(preferenceChangeListener);

		//保存一个束的键名和相应的值
		for (int i = 0, n = denominations.length; i < n; i++) {
			prefs.put(denominations[i], pictures[i]);
		}

		// 显示所有的键名字及值
		try {
			String keys[] = prefs.keys();
			for (int i = 0, n = keys.length; i < n; i++) {
				System.out.println(keys[i] + ": " + prefs.get(keys[i], "Unknown"));
			}
		} catch (BackingStoreException e) {
			System.err.println("Unable to read backing store: " + e);
		}

		// 建立 子目录
		Preferences child = Preferences.userRoot().node("/net/fubin/ibm/foo");

		//保存为XML文件
//		try {
//			FileOutputStream fos = new FileOutputStream("prefs.xml");
//			prefs.exportNode(fos);
//		} catch (Exception e) {
//			System.err.println("Unable to export nodes: " + e);
//		}

		//删除新生成的接点
//		try {
//			prefs.removeNode();
//		} catch (BackingStoreException e) {
//			System.err.println("Unable to access backing store: " + e);
//		}

	}
}