/**
 * Copyright (C), 2015-2019, 知融科技服务有限公司
 * FileName: ZipFileUtil
 * Author:   56969
 * Date:     2018/8/14 23:08
 * Description: 对压缩文件进行处理的工具
 */
package com.kk.common.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈对压缩文件进行处理的工具〉
 *
 * @author 56969
 * @create 2018/8/14
 * @since 1.0.0
 */
public class ZipFileUtil {

    private static byte[] _byte = new byte[1024];

    private static Logger logger = LogManager.getLogger(ZipFileUtil.class.getName());

    /**
     * 压缩文件或路径
     *
     * @param zip      压缩的目的地址
     * @param srcFiles 压缩的源文件
     */
    public static void zipFile(String zip, List<File> srcFiles) {
        try {
            if (zip.endsWith(".zip") || zip.endsWith(".ZIP")) {
                ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(new File(zip)));
                zipOut.setEncoding("GBK");
                for (File f : srcFiles) {
                    handlerFile(zip, zipOut, f, "");
                }
                zipOut.close();
            } else {
                System.out.println("target file[" + zip + "] is not .zip type file");
            }
        } catch (IOException e) {
        }
    }

    /**
     * @param zip     压缩的目的地址
     * @param zipOut 输出的地址
     * @param srcFile 被压缩的文件信息
     * @param path    在zip中的相对路径
     * @throws IOException
     */
    private static void handlerFile(String zip, ZipOutputStream zipOut, File srcFile, String path) throws IOException {
        System.out.println(" begin to compression file[" + srcFile.getName() + "]");
        if (!"".equals(path) && !path.endsWith(File.separator)) {
            path += File.separator;
        }
        if (!srcFile.getPath().equals(zip)) {
            if (srcFile.isDirectory()) {
                File[] files = srcFile.listFiles();
                if (files != null && files.length == 0) {
                    zipOut.putNextEntry(new ZipEntry(path + srcFile.getName() + File.separator));
                    zipOut.closeEntry();
                } else {
                    assert files != null;
                    for (File f : files) {
                        handlerFile(zip, zipOut, f, path + srcFile.getName());
                    }
                }
            } else {
                InputStream in = new FileInputStream(srcFile);
                zipOut.putNextEntry(new ZipEntry(path + srcFile.getName()));
                int len = 0;
                while ((len = in.read(_byte)) > 0) {
                    zipOut.write(_byte, 0, len);
                }
                in.close();
                zipOut.closeEntry();
            }
        }
    }

    /**
     * 解压文件到指定目录
     *
     * @param zipFilePath  目标文件
     * @param fileSavePath 解压目录
     * @author isDelete 是否删除目标文件
     */
    @SuppressWarnings("unchecked")
    public static void unZipFiles(String zipFilePath, String fileSavePath, boolean isDelete) throws Exception {
        File f = new File(zipFilePath);
        if ((!f.exists()) && (f.length() <= 0)) {
            throw new RuntimeException("要解压的文件不存在!");
        }
        //一定要加上编码，之前解压另外一个文件，没有加上编码导致不能解压
        ZipFile zipFile = new ZipFile(f, "gbk");
        String strPath, gbkPath, strtemp;
        strPath = fileSavePath;// 输出的绝对位置
        Enumeration<ZipEntry> e = zipFile.getEntries();
        while (e.hasMoreElements()) {
            ZipEntry zipEnt = e.nextElement();
            gbkPath = zipEnt.getName();
            strtemp = strPath + File.separator + gbkPath;
            if (zipEnt.isDirectory()) { //目录
                File dir = new File(strtemp);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            } else {
                // 读写文件
                InputStream is = zipFile.getInputStream(zipEnt);
                BufferedInputStream bis = new BufferedInputStream(is);
                // 建目录
                for (int i = 0; i < gbkPath.length(); i++) {
                    if (gbkPath.substring(i, i + 1).equalsIgnoreCase("/")) {
                        String temp = strPath + File.separator
                                + gbkPath.substring(0, i);
                        File subdir = new File(temp);
                        if (!subdir.exists()) {
                            subdir.mkdir();
                        }
                    }
                }
                FileOutputStream fos = new FileOutputStream(strtemp);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                int len;
                byte[] buff = new byte[1024];
                while ((len = bis.read(buff)) != -1) {
                    bos.write(buff, 0, len);
                }
                bos.close();
                fos.close();
            }
        }
        zipFile.close();
        /**
         * 文件不能删除的原因：
         * 1.看看是否被别的进程引用，手工删除试试(删除不了就是被别的进程占用)
         2.file是文件夹 并且不为空，有别的文件夹或文件，
         3.极有可能有可能自己前面没有关闭此文件的流(我遇到的情况)
         */
        if (isDelete) {
            boolean flag = new File(zipFilePath).delete();
            logger.debug("删除源文件结果: " + flag);
        }
        logger.debug("删除文件成功");
    }

    /**
     * @param url 压缩包的路径
     * @return
     * @author myl
     * @date 2018/8/24
     * @time 15:09
     */
    public static void delZipFile(String url) {
        File file = new File(url);
        // zip文件  判断 是否存在
        if (file.getName().endsWith(".zip")) {
            if (file.delete()) {
                logger.info("zip文件已经删除");
            } else {
                logger.info("zip文件删除失败");
            }
        }
    }

    /**
     * 解压缩ZIP文件，将ZIP文件里的内容解压到targetDIR目录下
     *
     * @param zipPath 待解压缩的ZIP文件名
     * @param descDir 目标目录
     * @param mkdir   是否建立专门的文件夹
     */
    public static List<File> upzipFile(String zipPath, String descDir, Boolean mkdir) {
        return upzipFile(new File(zipPath), descDir, mkdir);
    }

    /**
     * 解压缩ZIP文件，如果需要指定解压后的文件前缀的话，使用这个方法
     *
     * @param zipPath 目标压缩文件
     * @param descDir 解压到哪个文件夹去
     * @param mkdir   是否创建新的文件夹用来放解压后的文件
     * @param prefix  指定解压后的文件前缀
     * @return list 解压后的文件数组
     */
    public static List<File> upZipFile(String zipPath, String descDir, Boolean mkdir, String prefix) {
        File zipFile = new File(zipPath);
        List<File> list = new ArrayList<File>();
        try {
            ZipFile newZipFile = new ZipFile(zipFile, "GBK");
            if (mkdir) {
                // 建立压缩包文件夹
                descDir = descDir + FileUtil.getFileNameWithoutSuffix(zipFile) + "/";
            }
            for (Enumeration entries = newZipFile.getEntries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                // 无效的文件进行过滤操作
                if (entry.getSize() == 0) {
                    continue;
                }
                // 创建文件
                File file = new File(descDir + File.separator + prefix + "-" + entry.getName());
                resolveZipFiles(list, newZipFile, entry, file);
            }
            newZipFile.close();
            delZipFile(zipPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 对解压后的文件进行生成
     *
     * @param list       结果数组
     * @param newZipFile 压缩文件
     * @param entry      压缩文件内的文件对象
     * @param file       文件
     * @throws IOException 文件生成异常
     */
    private static void resolveZipFiles(List<File> list, ZipFile newZipFile, ZipEntry entry, File file) throws IOException {
        if (entry.isDirectory()) {
            file.mkdirs();
        } else {
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            InputStream in = newZipFile.getInputStream(entry);
            OutputStream out = new FileOutputStream(file);
            int len = 0;
            while ((len = in.read(_byte)) > 0) {
                out.write(_byte, 0, len);
            }
            in.close();
            out.flush();
            out.close();
            list.add(file);
        }
    }

    /**
     * 对.zip文件进行解压缩
     *
     * @param zipFile 解压缩文件
     * @param descDir 压缩的目标地址，如：D:\\测试 或 /mnt/d/测试
     * @param mkdir   是否建立专门的文件夹
     * @return List<File> 解压后的文件对象
     */
    @SuppressWarnings("rawtypes")
    public static List<File> upzipFile(File zipFile, String descDir, Boolean mkdir) {
        List<File> list = new ArrayList<File>();
        try {
            ZipFile newZipFile = new ZipFile(zipFile, "GBK");
            if (mkdir) {
                descDir = descDir + FileUtil.getFileNameWithoutSuffix(zipFile) + "/";
            }
            for (Enumeration entries = newZipFile.getEntries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                if (entry.getSize() == 0) {
                    continue;
                }
                File file = new File(descDir + File.separator + entry.getName());
                resolveZipFiles(list, newZipFile, entry, file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 对临时生成的文件夹和文件夹下的文件进行删除
     */
    public static void deletefile(String delpath) {
        try {
            File file = new File(delpath);
            if (!file.isDirectory()) {
                file.delete();
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                for (String s : filelist) {
                    File delfile = new File(delpath + File.separator + s);
                    if (!delfile.isDirectory()) {
                        delfile.delete();
                    } else {
                        delfile.isDirectory();
                        deletefile(delpath + File.separator + s);
                    }
                }
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
