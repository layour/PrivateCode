package pluginprojectutils.snippet.download;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 文件传送客户端:获取远程文件
 */
public class Snippet001RemoteFileDownLoad {

	public Snippet001RemoteFileDownLoad() {

	}

	// 确定文件是否已经下载，但没有下载完成
	private boolean fileExist(String pathAndFile) {
		File file = new File(pathAndFile);
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

	// 确定已经下载了的文件大小
	private long fileSize(String pathAndFile) {
		File file = new File(pathAndFile);
		return file.length();
	}

	// 将下载完全的文件更名，去掉.tp名
	private void fileRename(String fName, String nName) {
		File file = new File(fName);
		file.renameTo(new File(nName));
		file.delete();
	}

	public static void main(String[] args) {
		DataOutputStream dos = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		String localFile = "d://x.x";// 文件保存的地方及文件名，具体情况可以改
		String localFile_bak = localFile + ".tp";// 未下载完文件加.tp扩展名，以便于区别
		Snippet001RemoteFileDownLoad gco = new Snippet001RemoteFileDownLoad();
		long fileSize = 0;
		long start = System.currentTimeMillis();
		int len = 0;
		byte[] bt = new byte[1024];
		RandomAccessFile raFile = null;
		try {
			URL url = new URL("http://uapma.yonyou.com/mobas/template/download/zip?id=4");
			HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
			urlc.setRequestProperty("Accept-Encoding", "identity");
			urlc.disconnect();// 先断开，下面再连接，否则下面会报已经连接的错误
			urlc = (HttpURLConnection) url.openConnection();
			// 确定文件是否存在
			if (gco.fileExist(localFile_bak)) {// 采用断点续传，这里的依据是看下载文件是否在本地有.tp有扩展名同名文件
				System.out.println("文件续传中...");
				fileSize = gco.fileSize(localFile_bak); // 取得文件在小，以便确定随机写入的位置
				System.out.println("fileSize:" + fileSize);
				// 设置断点续传的开始位置
				urlc.setRequestProperty("RANGE", "bytes=" + fileSize + "-");
				// 设置接受信息
				urlc.setRequestProperty("Accept", "image/gif,image/x-xbitmap,application/msword,*/*");
				raFile = new RandomAccessFile(localFile_bak, "rw");// 随机方位读取
				raFile.seek(fileSize);// 定位指针到fileSize位置
				bis = new BufferedInputStream(urlc.getInputStream());
				while ((len = bis.read(bt)) > 0) {// 循环获取文件
					raFile.write(bt, 0, len);
				}
				System.out.println("文件续传接收完毕！");
			} else {// 采用原始下载
				fos = new FileOutputStream(localFile_bak); // 没有下载完毕就将文件的扩展名命名.bak
				dos = new DataOutputStream(fos);
				bis = new BufferedInputStream(urlc.getInputStream());
				System.out.println("正在接收文件...");
				while ((len = bis.read(bt)) > 0) {// 循环获取文件
					dos.write(bt, 0, len);
				}
			}
			System.out.println("共用时：" + (System.currentTimeMillis() - start) / 1000);
			System.out.println("localFile_bak:" + gco.fileSize(localFile_bak));
			// 下载完毕后，将文件重命名
			gco.fileRename(localFile_bak, localFile);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
				if (dos != null) {
					dos.close();
				}
				if (fos != null) {
					fos.close();
				}
				if (raFile != null) {
					raFile.close();
				}
			} catch (IOException f) {
				f.printStackTrace();
			}
		}
	}
}