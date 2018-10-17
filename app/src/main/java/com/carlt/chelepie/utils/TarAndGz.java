package com.carlt.chelepie.utils;

import com.carlt.doride.utils.Log;

import org.apache.tools.tar.TarEntry;
import org.apache.tools.tar.TarInputStream;
import org.apache.tools.tar.TarOutputStream;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

/**
 * 通过 Apache Tool 进行JAVA tar || tar.gz
 * 
 * @author Jacky
 * 
 *         2010-11-30
 */
public class TarAndGz {

	/**
	 * 归档tar文件
	 * 
	 * @param file
	 *            归档的文件对象
	 * @param out
	 *            输出tar流
	 * @param dir
	 *            相对父目录名称
	 * @param boo
	 *            是否把空目录归档进去
	 */
	public static void tar(File file, TarOutputStream out, String dir,
                           boolean boo) throws IOException {

		if (file.isDirectory()) {// 是目录

			File[] listFile = file.listFiles();// 得出目录下所有的文件对象

			if (listFile.length == 0 && boo) {// 空目录归档

				out.putNextEntry(new TarEntry(dir + file.getName() + "/"));// 将实体放入输出Tar流中

				System.out.println("归档." + dir + file.getName() + "/");

				return;
			} else {

				for (File cfile : listFile) {

					tar(cfile, out, dir + file.getName() + "/", boo);// 递归归档
				}
			}

		} else if (file.isFile()) {// 是文件

			System.out.println("归档." + dir + file.getName() + "/");

			byte[] bt = new byte[2048 * 2];

			TarEntry ze = new TarEntry(dir + file.getName());// 构建tar实体
			// 设置压缩前的文件大小
			ze.setSize(file.length());

			// ze.setName(file.getName());//设置实体名称.使用默认名称

			out.putNextEntry(ze);// //将实体放入输出Tar流中

			FileInputStream fis = null;

			try {

				fis = new FileInputStream(file);

				int i = 0;

				while ((i = fis.read(bt)) != -1) {// 循环读出并写入输出Tar流中

					out.write(bt, 0, i);
				}

			} catch (IOException ex) {
				throw new IOException("写入归档文件出现异常");
			} finally {

				try {
					if (fis != null)
						fis.close();// 关闭输入流
					out.closeEntry();
				} catch (IOException ex) {

					throw new IOException("关闭输入流出现异常");
				}

			}
		}

	}

	/**
	 * 解压tar File
	 * 
	 * @param file
	 *            要解压的tar文件对象
	 * @param outputDir
	 *            要解压到某个指定的目录下
	 * @throws IOException
	 */
	public static void unTar(File file, String outputDir) throws IOException {
		Log.e("info", "file.tar_name="+file.getAbsolutePath());
		Log.e("info", "file.outputDir="+outputDir);

		TarInputStream tarIn = null;

		try {

			tarIn = new TarInputStream(new FileInputStream(file), 1024 * 2);

			createDirectory(outputDir, null);// 创建输出目录

			TarEntry entry = null;

			while ((entry = tarIn.getNextEntry()) != null) {

				if (entry.isDirectory()) {// 是目录

					createDirectory(outputDir, entry.getName());// 创建空目录

				} else {// 是文件

					File tmpFile = new File(outputDir + "/" + entry.getName());

					createDirectory(tmpFile.getParent() + "/", null);// 创建输出目录

					OutputStream out = null;

					try {

						out = new FileOutputStream(tmpFile);

						int length = 0;

						byte[] b = new byte[2048];

						while ((length = tarIn.read(b)) != -1) {
							out.write(b, 0, length);
						}

					} catch (IOException ex) {
						throw ex;
					} finally {

						if (out != null)
							out.close();
					}

				}
			}

		} catch (IOException ex) {
			throw new IOException("解压归档文件出现异常=="+ex);
		} finally {
			try {
				if (tarIn != null) {
					tarIn.close();
				}
			} catch (IOException ex) {
				throw new IOException("关闭tarFile出现异常=="+ex);
			}
		}

	}

	/**
	 * 解压tar.gz 文件
	 * 
	 * @param file
	 *            要解压的tar.gz文件对象
	 * @param outputDir
	 *            要解压到某个指定的目录下
	 * @throws IOException
	 */
	public static void unTarGz(File file, String outputDir) throws IOException {

		TarInputStream tarIn = null;

		try {

			tarIn = new TarInputStream(new GZIPInputStream(
					new BufferedInputStream(new FileInputStream(file))),
					1024 * 2);

			createDirectory(outputDir, null);// 创建输出目录

			TarEntry entry = null;

			while ((entry = tarIn.getNextEntry()) != null) {

				if (entry.isDirectory()) {// 是目录

					createDirectory(outputDir, entry.getName());// 创建空目录

				} else {// 是文件

					File tmpFile = new File(outputDir + "/" + entry.getName());

					createDirectory(tmpFile.getParent() + "/", null);// 创建输出目录

					OutputStream out = null;

					try {

						out = new FileOutputStream(tmpFile);

						int length = 0;

						byte[] b = new byte[2048];

						while ((length = tarIn.read(b)) != -1) {
							out.write(b, 0, length);
						}

					} catch (IOException ex) {
						throw ex;
					} finally {

						if (out != null)
							out.close();
					}

				}
			}

		} catch (IOException ex) {
			throw new IOException("解压归档文件出现异常");
		} finally {
			try {
				if (tarIn != null) {
					tarIn.close();
				}
			} catch (IOException ex) {
				throw new IOException("关闭tarFile出现异常");
			}
		}

	}

	/**
	 * 构建目录
	 * 
	 * @param outputDir
	 * @param subDir
	 */
	public static void createDirectory(String outputDir, String subDir) {

		File file = new File(outputDir);

		if (!(subDir == null || subDir.trim().equals(""))) {// 子目录不为空

			file = new File(outputDir + "/" + subDir);
		}

		if (!file.exists()) {

			file.mkdirs();
		}

	}

	/**
	 * 清理文件(目录或文件)
	 * 
	 * @param file
	 */
	public void deleteDirectory(File file) {

		if (file.isFile()) {

			file.delete();// 清理文件
		} else {

			File list[] = file.listFiles();

			if (list != null) {

				for (File f : list) {
					deleteDirectory(f);
				}
				file.delete();// 清理目录
			}

		}

	}

	// /**
	// * 测试归档tar文件
	// */
	// public File testTar() {
	//
	// File srcFile = new File("d:/upload");// 要归档的文件对象
	//
	// File targetTarFile = new File("d:/upload.tar");// 归档后的文件名
	// TarOutputStream out = null;
	//
	// boolean boo = false;// 是否压缩成功
	//
	// try {
	// out = new TarOutputStream(new BufferedOutputStream(
	// new FileOutputStream(targetTarFile)));
	//
	// tar(srcFile, out, "", true);
	//
	// boo = true;
	//
	// // 归档成功
	//
	// return targetTarFile;
	//
	// } catch (IOException ex) {
	// throw new RuntimeException(ex);
	// } finally {
	//
	// try {
	// if (out != null)
	// out.close();
	// } catch (IOException ex) {
	// throw new RuntimeException("关闭Tar输出流出现异常", ex);
	// } finally {
	// // 清理操作
	// if (!boo && targetTarFile.exists())// 归档不成功,
	// targetTarFile.delete();
	//
	// }
	//
	// }
	// }
	//
	// /**
	// * 测试压缩归档tar.gz文件
	// */
	// public void testTarGz() {
	//
	// File tarFile = testTar();// 生成的tar文件
	//
	// File gzFile = new File(tarFile + ".gz");// 将要生成的压缩文件
	//
	// GZIPOutputStream out = null;
	//
	// InputStream in = null;
	//
	// boolean boo = false;// 是否成功
	//
	// try {
	//
	// in = new FileInputStream(tarFile);
	//
	// out = new GZIPOutputStream(new FileOutputStream(gzFile), 1024 * 2);
	//
	// byte b[] = new byte[1024 * 2];
	//
	// int length = 0;
	//
	// while ((length = in.read(b)) != -1) {
	//
	// out.write(b, 0, length);
	// }
	//
	// boo = true;
	//
	// } catch (Exception ex) {
	//
	// throw new RuntimeException("压缩归档文件失败", ex);
	// } finally {
	//
	// try {
	// if (out != null)
	// out.close();
	// if (in != null)
	// in.close();
	// } catch (IOException ex) {
	// throw new RuntimeException("关闭流出现异常", ex);
	// } finally {
	//
	// if (!boo) {// 清理操作
	//
	// tarFile.delete();
	//
	// if (gzFile.exists())
	// gzFile.delete();
	//
	// }
	//
	// }
	//
	// }
	//
	// }
	//
	// /**
	// * 测试解压归档tar文件
	// */
	// public void testUnTar(String targzFilePath, String outFilePath) {
	//
	// File srcTarFile = new File(targzFilePath);// 要解压缩的tar文件对象
	//
	// String destDir = outFilePath;// 把解压的文件放置到outFilePath目录下面
	//
	// boolean boo = false;// 是否压缩成功
	//
	// try {
	// unTar(srcTarFile, destDir);
	// boo = true;
	// } catch (IOException e) {
	// throw new RuntimeException(e);
	// } finally {
	// // 清理操作
	// if (!boo)
	// deleteDirectory(new File(destDir));// 目标文件夹 。清理
	// }
	//
	// }
	//
	// /**
	// * 测试解压归档tar文件
	// */
	// public void testUnTarGz(String targzFilePath, String outFilePath) {
	//
	// File srcTarGzFile = new File(targzFilePath);// 要解压缩的tar.gz文件对象
	//
	// String destDir = outFilePath;// 把解压的文件放置outFilePath的目录下面
	//
	// boolean boo = false;// 是否压缩成功
	//
	// try {
	// unTarGz(srcTarGzFile, destDir);
	// boo = true;
	// } catch (IOException e) {
	// throw new RuntimeException(e);
	// } finally {
	// // 清理操作
	// if (!boo)
	// deleteDirectory(new File(destDir));// 目标文件夹 。清理
	//
	// }
	//
	// }

	/**
	 * 车乐拍缩略图解压tar File
	 * 
	 * @param file
	 *            要解压的tar文件对象
	 * @param outputDir
	 *            要解压到某个指定的目录下
	 * @throws IOException
	 */
	public static void unThumbnail(File file, String outputDir)
			throws IOException {

		TarInputStream tarIn = null;

		try {

			tarIn = new TarInputStream(new FileInputStream(file), 1024 * 2);

			createDirectory(outputDir, null);// 创建输出目录

			TarEntry entry = null;

			while ((entry = tarIn.getNextEntry()) != null) {

				if (entry.isDirectory()) {// 是目录
					createDirectory(outputDir, entry.getName());// 创建空目录

				} else {// 是文件

					String name = entry.getName().substring(
							entry.getName().lastIndexOf("/") + 1);
					Log.e("info", "文件==" + name);
					File tmpFile = new File(outputDir + "/" + name);

					createDirectory(tmpFile.getParent() + "/", null);// 创建输出目录

					OutputStream out = null;

					try {

						out = new FileOutputStream(tmpFile);

						int length = 0;

						byte[] b = new byte[2048];

						while ((length = tarIn.read(b)) != -1) {
							out.write(b, 0, length);
						}

					} catch (IOException ex) {
						throw ex;
					} finally {

						if (out != null)
							out.close();
					}

				}
			}

		} catch (IOException ex) {
			throw new IOException("解压归档文件出现异常");
		} finally {
			try {
				if (tarIn != null) {
					tarIn.close();
				}
			} catch (IOException ex) {
				throw new IOException("关闭tarFile出现异常");
			}
		}

	}
}