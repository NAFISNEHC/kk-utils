/**
 * Copyright (C), 2015-2019, 知融科技服务有限公司
 * FileName: ExcelImportUtils
 * Author: allahbin
 * Date: 2018/7/10
 * 11:24 Description: Excel工具代码
 */
package com.kk.common.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * 〈一句话功能简述〉<br>
 * 〈Excel工具代码〉
 *
 * @author 56969
 * @create 2018/7/10
 * @since 1.0.0
 */
public class ExcelZrUtils {

	/**
	 * 是否是2003的excel，返回true是2003
	 * 
	 * @param filePath
	 *            文件地址
	 * @return
	 */
	public static boolean isExcel2003(String filePath) {
		return filePath.matches("^.+\\.(?i)(xls)$");
	}

	/**
	 * 是否是2007的excel，返回true是2007
	 * 
	 * @param filePath
	 *            文件地址
	 * @return
	 */
	public static boolean isExcel2007(String filePath) {
		return filePath.matches("^.+\\.(?i)(xlsx)$");
	}

	/**
	 * 验证EXCEL文件
	 *
	 * @param filePath
	 *            文件地址
	 * @return
	 */
	public static boolean validateExcel(String filePath) {
		if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
			return false;
		}
		return true;
	}

	/**
	 * 接收参数，并统一以字符串的形式返回
	 *
	 * @param cell
	 * @return
	 */
	public static String cellToString(Cell cell, String... type) {
		CellType cellType = cell.getCellTypeEnum();
		String cellValue = null;
		switch (cellType) {
		// switch case语句case后的枚举常量不带枚举类型
		// NUMERIC(0),
		// STRING(1),
		// FORMULA(2),
		// BLANK(3),
		// BOOLEAN(4),
		// ERROR(5);
		case STRING:
			cellValue = cell.getStringCellValue();
			System.out.println("该单元格类型为：STRING ,值为：" + cellValue);
			break;
		case NUMERIC:
			// 数字还是日期判断
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				Date date = cell.getDateCellValue();
				cellValue = AppDateUtil.buildDateToStr(date);
			} else {
				cellValue = cell.getNumericCellValue() + "";
				BigDecimal bd = new BigDecimal(cellValue);
				cellValue = bd.toPlainString();
			}
			System.out.println("该单元格类型为：NUMERIC ,值为：" + cellValue);
			break;
		case FORMULA:
			cellValue = cell.getCellFormula();
			System.out.println("该单元格类型为：FORMULA ,值为：" + cellValue);
			break;
		case ERROR:
			cellValue = cell.getErrorCellValue() + "";
			System.out.println("该单元格类型为：ERROR ,值为：" + cellValue);
			break;
		case BOOLEAN:
			cellValue = cell.getBooleanCellValue() + "";
			System.out.println("该单元格类型为：BOOLEAN ,值为：" + cellValue);
			break;
		case BLANK:
			cellValue = cell.getRichStringCellValue() + "";
			System.out.println("该单元格类型为：BLANK ,值为：" + cellValue);
			break;
		default:
			break;
		}
		return cellValue;
	}

	/**
	 * 获取数字类型的cell值
	 *
	 * @param cell
	 * @return
	 */
	private static Object getValueOfNumericCell(Cell cell) {
		Boolean isDate = DateUtil.isCellDateFormatted(cell);
		Double d = cell.getNumericCellValue();
		Object o = null;
		if (isDate) {
			o = DateFormat.getDateTimeInstance().format(cell.getDateCellValue());
		} else {
			o = getRealStringValueOfDouble(d);
		}
		return o;
	}

	/**
	 * 处理科学计数法与普通计数法的字符串显示，尽最大努力保持精度
	 * 
	 * @param d
	 * @return
	 */
	private static String getRealStringValueOfDouble(Double d) {
		String doubleStr = d.toString();
		boolean b = doubleStr.contains("E");
		int indexOfPoint = doubleStr.indexOf('.');
		if (b) {
			int indexOfE = doubleStr.indexOf('E');
			// 小数部分
			BigInteger xs = new BigInteger(doubleStr.substring(indexOfPoint + BigInteger.ONE.intValue(), indexOfE));
			// 指数
			int pow = Integer.valueOf(doubleStr.substring(indexOfE + BigInteger.ONE.intValue()));
			int xsLen = xs.toByteArray().length;
			int scale = xsLen - pow > 0 ? xsLen - pow : 0;
			doubleStr = String.format("%." + scale + "f", d);
		} else {
			/*
			 * java.util.regex.Pattern p = Pattern.compile(".0$");
			 * java.util.regex.Matcher m = p.matcher(doubleStr); if (m.find()) {
			 * doubleStr = doubleStr.replace(".0", ""); }
			 */
		}
		return doubleStr;
	}

    /**
     * 说明：城市融合Excel导入错误json对象
     * @author tangbin
     * @date 2018/8/25
     * @time 17:52
     * @param row 错误的行
     * @param eventid 错误的编码
     * @param info 错误信息
     * @param cell 所在单元格
     * @return com.alibaba.fastjson.JSONObject
     */
	public static JSONObject buildErrorObj(int row, String eventid, String info, int cell){
        JSONObject errObj = new JSONObject();
        errObj.put("row", row);
        errObj.put("eventid", eventid);
        errObj.put("info", info);
        errObj.put("cell", cell);
        return errObj;
	}

	/**
	 * 导出 excel
	 * @param list 导出的数据
	 * @param title 标题
	 * @param sheetName 工作薄名字
	 * @param pojoClass 导出对象的类型
	 * @param fileName 文件名称
	 * @param isCreateHeader 是否创建头
	 * @param response 输出
	 */
	public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, boolean isCreateHeader, HttpServletResponse response){
		ExportParams exportParams = new ExportParams(title, sheetName);
		exportParams.setCreateHeadRows(isCreateHeader);
		defaultExport(list, pojoClass, fileName, response, exportParams);

	}

	/**
	 *
	 * @param list 导出的数据
	 * @param title 标题
	 * @param sheetName 工作薄名字
	 * @param pojoClass 导出对象的类型
	 * @param fileName 文件名称
	 * @param response 输出
	 */
	public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass,String fileName, HttpServletResponse response){
		defaultExport(list, pojoClass, fileName, response, new ExportParams(title, sheetName));
	}
	public static void exportExcel(List<Map<String, Object>> list, String fileName, HttpServletResponse response){
		defaultExport(list, fileName, response);
	}

	private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName, HttpServletResponse response, ExportParams exportParams) {
		Workbook workbook = ExcelExportUtil.exportExcel(exportParams,pojoClass,list);
        if(workbook != null){
            downLoadExcel(fileName, response, workbook);
        }
	}

    /**
     * 下载Excel
     * @param fileName 下载的文件名称
     * @param response 输出
     * @param workbook 工作表
     */
	public static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) {
		try {
			response.setCharacterEncoding("UTF-8");
			// 设置导出的excel格式为xls
			response.setHeader("content-Type", "application/vnd.ms-excel");
			response.setHeader("Content-Disposition",
					"attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
			workbook.write(response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void defaultExport(List<Map<String, Object>> list, String fileName, HttpServletResponse response) {
		Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);
		if (workbook != null){
			downLoadExcel(fileName, response, workbook);
		}
	}

	public static <T> List<T> importExcel(String filePath,Integer titleRows,Integer headerRows, Class<T> pojoClass){
		if (StringUtils.isBlank(filePath)){
			return null;
		}
		ImportParams params = new ImportParams();
		params.setTitleRows(titleRows);
		params.setHeadRows(headerRows);
		List<T> list = null;
		try {
			list = ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
		}catch (NoSuchElementException e){
			//"模板不能为空"
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass){
		if (file == null){
			return null;
		}
		ImportParams params = new ImportParams();
		params.setTitleRows(titleRows);
		params.setHeadRows(headerRows);
		List<T> list = null;
		try {
			list = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
		}catch (NoSuchElementException e){
			//"excel文件不能为空"
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 导出Excel到本地
	 * @param list 数据
	 * @param title 标题
	 * @param sheetName 表明
	 * @param pojoClass 类
	 * @param fileName 文件名称
	 * @param path 要保存的地址
	 */
	public static boolean exportExcelToLocal(List<?> list, String title, String sheetName, Class<?> pojoClass,String fileName, String path){
		Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(title, sheetName),pojoClass,list);
		if(workbook != null){
			OutputStream outputStream = null;
			try{
				//去除敏感词
				path = path.replace(" ", "");
				//File file = new File(path);//可能会抛异常：NullPointerException
				File file = new File(path);//可能会抛异常：NullPointerException
				System.out.println(file.getParent());
				outputStream = new FileOutputStream(file);
				// 创建文件输出流，准备输出电子表格
				workbook.write(outputStream);
				return true;
			} catch (Exception e) {
				System.out.println("文件保存到本地失败" + e);
				return false;
			} finally {
				try {
					workbook.close();
				} catch (IOException e) {
					System.out.println("文件保存到本地失败-写入错误" + e);
				}
				try {
					if (outputStream != null) {
						outputStream.close();
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return false;
	}
}
