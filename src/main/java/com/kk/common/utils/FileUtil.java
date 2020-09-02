/**
 * Copyright (C), 2015-2019, 知融科技服务有限公司
 * FileName: FileUtile
 * Author: allahbin
 * Date: 2018/7/10 11:14
 * Description: 用于进行文件处理的工具类
 */
package com.kk.common.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 〈一句话功能简述〉<br>
 * 〈用于进行文件处理的工具类〉
 *
 * @author 56969
 * @create 2018/7/10
 * @since 1.0.0
 */

public class FileUtil {

    // 常用图片类型
    protected static final String imgReg = ".*(bmp|jpg|jpeg|png|tiff|gif|pcx|tga|exif|fpx|svg|psd|cdr|pcd|dxf|ufo|eps|ai|raw|wmf).*";

    protected static final String excelReg = ".*(xlsx|xls).*";

    protected static final String zipReg = ".*(zip).*";

    /**
     * io拷贝
     *
     * @param inFile  源文件
     * @param outFile 目标文件
     * @return
     * @throws Exception
     */
    public static long fileStraeamCopy(File inFile, String outFile) throws Exception {
        long begin = System.currentTimeMillis();
        File out = new File(outFile);
        FileInputStream fin = new FileInputStream(inFile);
        FileOutputStream fout = new FileOutputStream(out);
        // 2m内存
        int length = 2097152;
        byte[] buffer = new byte[length];
        ioCopy(fin, fout, buffer);
        long end = System.currentTimeMillis();
        long runtime = 0;
        if (end > begin) runtime = end - begin;
        return runtime;
    }

    /**
     * io拷贝
     *
     * @param inFile  源文件
     * @param outFile 目标文件
     * @return
     * @throws Exception
     */
    public static long fileStraeamCopy(String inFile, String outFile) throws Exception {
        long begin = System.currentTimeMillis();

        File in = new File(inFile);
        File out = new File(outFile);
        FileInputStream fin = new FileInputStream(in);
        FileOutputStream fout = new FileOutputStream(out);
        // 2m内存
        int length = 2097152;
        byte[] buffer = new byte[length];

        ioCopy(fin, fout, buffer);
        long end = System.currentTimeMillis();
        long runtime = 0;
        if (end > begin) runtime = end - begin;
        return runtime;
    }

    /**
     * IO 拷贝使用的
     *
     * @param fin    输入流
     * @param fout   输出流
     * @param buffer 字节
     * @throws IOException 异常
     */
    private static void ioCopy(FileInputStream fin, FileOutputStream fout, byte[] buffer) throws IOException {
        while (true) {
            int ins = fin.read(buffer);
            if (ins == -1) {
                fin.close();
                fout.flush();
                fout.close();
                break;

            } else fout.write(buffer, 0, ins);
        }
    }

    /**
     * 创建文件
     *
     * @param destFileName
     * @return
     */
    public static boolean createFile(String destFileName) {
        File file = new File(destFileName);
        // 创建失败，目标文件已存在
        if (file.exists()) {
            return false;
        }
        if (destFileName.endsWith(File.separator)) {
            return false;
        }
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                return false;
            }
        }
        // 创建目标文件
        try {
            if (file.createNewFile()) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 说明：判断文件是否是图片
     *
     * @param fileType 文件的类型
     * @return 是否是图片
     * @author tangbin
     * @date 2018/12/24
     * @time 17:19
     */
    public static boolean isImage(String fileType) {
        return Pattern.matches(imgReg, fileType);
    }

    /**
     * 创建文件夹 用于创建保存临时文件的文件夹，获取其他内容的文件夹
     *
     * @param dirPath 文件夹的路径
     * @return 创建成功或者文件夹存在则会返回true，否则false
     */
    public static boolean createFolder(String dirPath) {
        File fileDir = new File(dirPath);
        // 判断是否存在相同文件夹
        if (fileDir.exists()) {
            // 判断是不是文件夹
            if (fileDir.isDirectory()) {
                return true;
            } else {
                return false;
            }
        } else {
            // 文件夹不存在
            fileDir.mkdirs(); // 创建文件夹
            return true;
        }
    }

    /**
     * 删除指定的文件夹，包括文件夹和文件夹里面的内容
     *
     * @param mergePath 要删除的文件路径
     */
    public static boolean delFolder(String mergePath) {
        File dir = new File(mergePath);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                try {
                    file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return dir.delete();
    }

    /**
     * 删除指定文件夹内的所有文件 或者直接删除整个文件夹，再重新创建一个，这样速度回更快，但是文件夹的日期等属性就变了
     *
     * @param path 文件夹所在的位置
     * @return 如果文件夹不存在，返回false 如果路径不是文件夹返回false
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File folder = new File(path);
        // 如果不存在，返回false
        if (!folder.exists()) {
            return flag;
        }
        // 如果路径不是文件夹返回false
        if (!folder.isDirectory()) {
            return flag;
        }
        String[] fileList = folder.list();
        File file = null;
        for (int i = 0; i < fileList.length; i++) {
            // 获取内部文件信息
            if (path.endsWith(File.separator)) {
                file = new File(path + fileList[i]);
            } else {
                file = new File(path + File.separator + fileList[i]);
            }
            // 判断是否是文件
            if (file.isFile()) {
                file.delete();
                flag = true;
            }
            // 如果是文件夹
            if (file.isDirectory()) {
                delAllFile(path + "/" + fileList[i]); // 删除文件
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 删除单个文件
     *
     * <p>说明：传递文件路径，然后删了它
     *
     * @param filePath
     * @return 单个文件删除成功返回true，否则返回false
     * @author tangbin
     * @data 2018年2月3日下午1:03:42
     */
    public static boolean delFile(String filePath) {
        File file = new File(filePath);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 删除文件或文件夹
     *
     * <p>说明：传入文件或者文件夹的路径，都能进行删除
     *
     * @param path
     * @return 删除成功返回true，否则返回false
     * @author tangbin
     * @data 2018年2月3日下午1:07:21
     */
    public static boolean delFileOrFolder(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.isFile()) {
                return delFile(path);
            } else {
                return delAllFile(path);
            }
        } else {
            return false;
        }
    }

    /**
     * 判断文件夹里面是不是有文件
     *
     * <p>说明：进行文件分块上传的时候需要用到
     *
     * @param folderPath 文件夹所在的位置
     * @return 有文件就是true，没有则是false
     * @author tangbin
     * @data 2018年2月3日下午12:41:25
     */
    public static boolean verifyFileInFolder(String folderPath) {
        // 验证文件夹的有效性
        Boolean folderFlag = createFolder(folderPath);
        if (folderFlag) {
            // 获得指定文件对象
            File folder = new File(folderPath);
            // 获得该文件夹内的所有文件
            File[] fileArray = folder.listFiles();
            if (fileArray.length > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 得到文件的MD5码
     *
     * <p>说明：传入文件的地址，返回MD5字符串
     *
     * @param filePath
     * @return 如果文件存在的话，返回文件的MD5，否则返回为空
     * @author tangbin
     * @data 2018年2月3日下午12:52:43
     */
    public static String verifyFileMD5(String filePath) {
        File file = new File(filePath);
        String fileMD5 = "";
        // 对文件进行验证
        if (file.exists()) {
            fileMD5 = DigestUtils.md5Hex(filePath);
        }
        return fileMD5;
    }

    /**
     * 获取文件夹内所有的文件信息 方法会返回一个文件的数组
     *
     * @param folderPath 目标文件夹的路径
     * @return fileArray 如果给的路径是文件夹，就会返回这个，否则返回null
     */
    public static File[] getFilesInFolder(String folderPath) {
        // 验证文件夹的有效性
        Boolean folderFlag = createFolder(folderPath);
        if (folderFlag) {
            // 获得指定文件对象
            File folder = new File(folderPath);
            // 获得该文件夹内的所有文件
            File[] fileArray = folder.listFiles();
            return fileArray;
        } else {
            return null;
        }
    }

    /**
     * 获取文件夹内所有的文件信息 方法会返回一个文件名组成的数组
     *
     * @param folderPath 目标文件夹的路径
     * @param flag       是否需要文件夹信息，true：要文件夹的名字，false：不要文件夹的名字
     */
    public static List<String> getFilesNameInFolder(String folderPath, Boolean flag) {
        // 获得指定文件对象
        File folder = new File(folderPath);
        // 获得该文件夹内的所有文件
        File[] fileArray = folder.listFiles();
        List<String> fileName = new ArrayList<String>();
        for (int i = 0; i < fileArray.length; i++) {
            File file = fileArray[i];
            // 如果是文件
            if (file.isDirectory() && flag) {
                file.getName();
            } else {
                fileName.add(file.getName());
            }
        }
        return fileName;
    }

    /**
     * 压缩文件---这个功能还没测试
     *
     * @param srcFileList
     * @param zipFile
     * @throws IOException
     */
    public static void zipFiles(List<File> srcFileList, File zipFile) throws IOException {
        byte[] buf = new byte[1024];
        // ZipOutputStream类：完成文件或文件夹的压缩
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFile));

        for (File aSrcFileList : srcFileList) {
            FileInputStream in = new FileInputStream(aSrcFileList);
            out.putNextEntry(new ZipEntry(aSrcFileList.getName()));
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.closeEntry();
            in.close();
        }
        out.close();
    }

    /**
     * 获取文件的文件名
     *
     * @param file 文件对象
     * @return String 文件名
     */
    public static String getFileNameWithoutSuffix(File file) {
        String file_name = file.getName();
        return file_name.substring(0, file_name.lastIndexOf("."));
    }

    /**
     * 说明：根据文件名称来判断文件类型
     *
     * @param filename 文件名称
     * @return boolean 成功还是失败
     * @author tangbin
     * @date 2018/8/27
     * @time 15:13
     */
    public static Boolean verFileType(String filename) {
        // 正则验证
        Pattern pattern = Pattern.compile("\\.(JPEG)|(jpeg)|(JPG)|(jpg)|(gif)|(GIF)|(HEIC)|(heic)|(BMP)|(bmp)|(PNG)|(png)");
        Matcher matcher = pattern.matcher(filename);
        return matcher.find();
    }

    /**
     * 将图片转成byte数组
     *
     * @param path 文件地址
     * @return byte数据流
     */
    public static byte[] imageTobyte(String path) {
        byte[] data = null;
        FileImageInputStream input = null;
        try {
            input = new FileImageInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return data;
    }

    /**
     * 缩放图片(压缩图片质量，改变图片尺寸)
     * 若原图宽度小于新宽度，则宽度不变！
     *
     * @param filepath 目标文件，一定要是完整的路径
     */
    public static String imageResize(String filepath, double scale) throws IOException {
        // 要压缩的那个文件的名称
        File originalFile = new File(filepath);
        // 截取前面的那部分字段，注意，使用***.min.jpeg这样的格式，excel导出的话，会报错
        String newFilePath = filepath.substring(0, filepath.lastIndexOf(".")) + "-mini." + filepath.substring(filepath.lastIndexOf(".") + 1);
        // 压缩后的图片
        File resizedImg = new File(newFilePath);
        // 如果要压缩的文件不存在，那就直接结束
        if (!originalFile.exists()) {
            return null;
        }
        if (resizedImg.exists()) {
            return newFilePath;
        }
        // 开始进行压缩
        scaleImage(filepath, newFilePath, scale, null);
        return newFilePath;
    }

    /***
     * 按指定的比例缩放图片
     *
     * @param sourceImagePath
     *      源地址
     * @param destinationPath
     *      改变大小后图片的地址
     * @param scale
     *      缩放比例，如1.2
     */
    public static void scaleImage(String sourceImagePath,
                                  String destinationPath, double scale, String format) {

        File file = new File(sourceImagePath);
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(file);
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

            width = parseDoubleToInt(width * scale);
            height = parseDoubleToInt(height * scale);

            Image image = bufferedImage.getScaledInstance(width, height,
                    Image.SCALE_SMOOTH);
            BufferedImage outputImage = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics graphics = outputImage.getGraphics();
            graphics.drawImage(image, 0, 0, null);
            graphics.dispose();
            // 避免不传递格式
            if (format == null) {
                format = "JPEG";
            }
            ImageIO.write(outputImage, format, new File(destinationPath));
        } catch (IOException e) {
            System.out.println("scaleImage方法压缩图片时出错了");
            e.printStackTrace();
        }

    }

    /***
     * 将图片缩放到指定的高度或者宽度
     * @param sourceImagePath 图片源地址
     * @param destinationPath 压缩完图片的地址
     * @param width 缩放后的宽度
     * @param height 缩放后的高度
     * @param auto 是否自动保持图片的原高宽比例
     * @param format 图图片格式 例如 jpg
     */
    public static void scaleImageWithParams(String sourceImagePath,
                                            String destinationPath, int width, int height, boolean auto, String format) {

        try {
            File file = new File(sourceImagePath);
            BufferedImage bufferedImage = null;
            bufferedImage = ImageIO.read(file);
            if (auto) {
                ArrayList<Integer> paramsArrayList = getAutoWidthAndHeight(bufferedImage, width, height);
                width = paramsArrayList.get(0);
                height = paramsArrayList.get(1);
                System.out.println("自动调整比例，width=" + width + " height=" + height);
            }

            Image image = bufferedImage.getScaledInstance(width, height,
                    Image.SCALE_DEFAULT);
            BufferedImage outputImage = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics graphics = outputImage.getGraphics();
            graphics.drawImage(image, 0, 0, null);
            graphics.dispose();
            ImageIO.write(outputImage, format, new File(destinationPath));
        } catch (Exception e) {
            System.out.println("scaleImageWithParams方法压缩图片时出错了");
            e.printStackTrace();
        }


    }

    /**
     * 将double类型的数据转换为int，四舍五入原则
     *
     * @param sourceDouble
     * @return
     */
    private static int parseDoubleToInt(double sourceDouble) {
        int result = 0;
        result = (int) sourceDouble;
        return result;
    }

    /***
     *
     * @param bufferedImage 要缩放的图片对象
     * @param width_scale 要缩放到的宽度
     * @param height_scale 要缩放到的高度
     * @return 一个集合，第一个元素为宽度，第二个元素为高度
     */
    private static ArrayList<Integer> getAutoWidthAndHeight(BufferedImage bufferedImage, int width_scale, int height_scale) {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        double scale_w = getDot2Decimal(width_scale, width);

        System.out.println("getAutoWidthAndHeight width=" + width + "scale_w=" + scale_w);
        double scale_h = getDot2Decimal(height_scale, height);
        if (scale_w < scale_h) {
            arrayList.add(parseDoubleToInt(scale_w * width));
            arrayList.add(parseDoubleToInt(scale_w * height));
        } else {
            arrayList.add(parseDoubleToInt(scale_h * width));
            arrayList.add(parseDoubleToInt(scale_h * height));
        }
        return arrayList;

    }


    /***
     * 返回两个数a/b的小数点后三位的表示
     * @param a
     * @param b
     * @return
     */
    public static double getDot2Decimal(int a, int b) {

        BigDecimal bigDecimal_1 = new BigDecimal(a);
        BigDecimal bigDecimal_2 = new BigDecimal(b);
        BigDecimal bigDecimal_result = bigDecimal_1.divide(bigDecimal_2, new MathContext(4));
        Double double1 = new Double(bigDecimal_result.toString());
        System.out.println("相除后的double为：" + double1);
        return double1;
    }

    /**
     * 说明：对文件的名字进行修改
     *
     * @param path    要操作的文件地址
     * @param newName 新的文件名称
     * @return 成功还是失败
     * @author tangbin
     * @date 2018/12/25
     * @time 11:46
     */
    public static boolean updateName(String path, String newName) {
        File oldFile = new File("d:/PMS");
        // 判断文件是否存在
        if (oldFile.exists()) {
            System.out.println("修改前文件名称是：" + oldFile.getName());
            String rootPath = oldFile.getParent();
            System.out.println("根路径是：" + rootPath);
            File newFile = new File(rootPath + File.separator + "PMSTmp");
            System.out.println("修改后文件名称是：" + newFile.getName());
            if (oldFile.renameTo(newFile)) {
                System.out.println("修改成功!");
                return true;
            } else {
                System.out.println("修改失败");
                return false;
            }
        }else {
            return false;
        }
    }

    /**
     * 说明：对文件进行复制操作-复制存在问题，请使用Apache封装好的复制
     * @author tangbin
     * @date 2018/12/25
     * @time 11:51
     * @param oldpath 旧文件的知
     * @param newpath 新文件的地址
     * @return 成功还是失败
     */
    @Deprecated
    public static boolean copyFile(String oldpath, String newpath) {
        InputStreamReader fr = null;
        OutputStreamWriter fw = null;
        try {
            fr = new InputStreamReader(new FileInputStream(oldpath),"UTF-8");//读
            fw = new OutputStreamWriter(new FileOutputStream(newpath), "UTF-8");//写
            char[] buf = new char[1024];//缓冲区
            int len;
            while ((len = fr.read(buf)) != -1) {
                fw.write(buf, 0, len);//读几个写几个
            }
            return true;
        } catch (IOException e) {
            return false;
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fw != null) {
                try {
                    fw.flush();
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 说明：对文件进行复制操作
     * @author tangbin
     * @date 2018/12/25
     * @time 11:51
     * @param oldpath 旧文件的知
     * @param newpath 新文件的地址
     * @return 成功还是失败
     */
    public static boolean apacheCopyFile(String oldpath, String newpath) {
        File oldFile = new File(oldpath);
        File newFile = new File(newpath);
        try {
            FileUtils.copyFile(oldFile, newFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 文件下载的响应操作
     * @param response 响应
     * @param file 要下载的文件
     * @param filename 文件的名称
     */
    public static void downResponse(HttpServletResponse response, File file, String filename) {
        response.setContentType("application/force-download");// 设置强制下载不打开
        response.addHeader("Content-Disposition", "attachment;fileName=" + filename);// 设置文件名

        byte[] buffer = new byte[1024];
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            OutputStream os = response.getOutputStream();
            int i = bis.read(buffer);
            while (i != -1) {
                os.write(buffer, 0, i);
                i = bis.read(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
