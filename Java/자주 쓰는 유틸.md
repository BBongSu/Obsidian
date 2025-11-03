---
sticker: emoji//270f-fe0f
---

``` java title="PortalFileGenerator - Spring MyBatis CRUD 자동생성"

package com.bizpack;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class PortalFileGenerator {

    private static final String BASE_PACKAGE = "com.bizpack.portal";  // 공통 패키지명
    private static final String BASE_PATH = "C:\\bizpack_dev\\bizpack\\src\\main\\java\\" + BASE_PACKAGE.replace(".", "\\");
    private static final String CONTROLLER_PATH = "controller";
    private static final String SERVICE_PATH = "service";
    private static final String MAPPER_PATH = "mapper";
    private static final String XML_PATH = "C:\\bizpack_dev\\bizpack\\src\\main\\resources\\mapper\\portal";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("테이블명을 입력하세요 : ");
        String tableName = scanner.nextLine().trim();

        System.out.print("컬럼명을 쉼표(,)로 구분하여 입력하세요 : ");
        String columns = scanner.nextLine().trim();
        List<String> columnList = Arrays.asList(columns.split(","));
        String primaryColumn = columnList.get(0).trim();

        String className = convertToCamelCase(tableName, true);
        String packageName = extractPackageName(tableName);
        String packageName2Depth = extractPackageName2Depth(tableName);

        generateController(className, packageName, packageName2Depth);
        generateService(className, packageName);
        generateMapper(className, packageName);
        generateXmlFile(tableName, className, columnList, packageName, primaryColumn);
    }

    private static void generateController(String className, String packageName, String packageName2Depth) {
        String fileName = BASE_PATH + "\\" + CONTROLLER_PATH + "\\" + packageName + "\\" + className + "Controller.java";
        String content = "package " + BASE_PACKAGE + ".controller." + packageName + ";\n\n"
                + "import org.springframework.web.bind.annotation.*;\n"
                + "import org.springframework.beans.factory.annotation.Autowired;\n"
                + "import java.util.Map;\n"
                + "import java.util.List;\n"
                + "import " + BASE_PACKAGE + ".service." + packageName + "." + className + "Service;\n\n"
                + "@RestController\n"
                + "@RequestMapping(\"/portal/" + packageName + "/" + packageName2Depth + "\")\n"
                + "public class " + className + "Controller {\n\n"
                + "    @Autowired\n"
                + "    private final " + className + "Service " + decapitalize(className) + "Service;\n\n"
                + "    public " + className + "Controller(" + className + "Service " + decapitalize(className) + "Service) {\n"
                + "        this." + decapitalize(className) + "Service = " + decapitalize(className) + "Service;\n"
                + "    }\n\n"
                + generateControllerMethods(className)
                + "}\n";

        writeFile(fileName, content);
    }

    private static String generateControllerMethods(String className) {
        String[] methods = {"count", "selectList", "select", "insert", "update", "delete"};
        StringBuilder sb = new StringBuilder();
        for (String method : methods) {
            if (method.equals("selectList")) {
                sb.append("    @PostMapping(\"/").append(method).append(className).append("\")\n")
                  .append("    public List<Map<String, Object>> ").append(method).append(className).append("(@RequestBody Map<String, Object> map) {\n")
                  .append("        return ").append(decapitalize(className)).append("Service.").append(method).append(className).append("(map);\n")
                  .append("    }\n\n");
            } else if (method.equals("select")) {
                sb.append("    @PostMapping(\"/").append(method).append(className).append("\")\n")
                  .append("    public Map<String, Object> ").append(method).append(className).append("(@RequestBody Map<String, Object> map) {\n")
                  .append("        return ").append(decapitalize(className)).append("Service.").append(method).append(className).append("(map);\n")
                  .append("    }\n\n");
            } else {
                sb.append("    @PostMapping(\"/").append(method).append(className).append("\")\n")
                  .append("    public int ").append(method).append(className).append("(@RequestBody Map<String, Object> map) {\n")
                  .append("        return ").append(decapitalize(className)).append("Service.").append(method).append(className).append("(map);\n")
                  .append("    }\n\n");
            }
        }
        return sb.toString();
    }

    private static void generateService(String className, String packageName) {
        String fileName = BASE_PATH + "\\" + SERVICE_PATH + "\\" + packageName + "\\" + className + "Service.java";
        String content = "package " + BASE_PACKAGE + ".service." + packageName + ";\n\n"
                + "import org.springframework.beans.factory.annotation.Autowired;\n"
                + "import org.springframework.stereotype.Service;\n"
                + "import org.springframework.transaction.annotation.Transactional;\n"
                + "import java.util.Map;\n"
                + "import java.util.List;\n"
                + "import " + BASE_PACKAGE + ".mapper." + packageName + "." + className + "Mapper;\n\n"
                + "@Service\n"
                + "@Transactional\n"
                + "public class " + className + "Service {\n\n"
                + "    @Autowired\n"
                + "    private " + className + "Mapper " + decapitalize(className) + "Mapper;\n\n"
                + generateServiceMethods(className)
                + "}\n";

        writeFile(fileName, content);
    }

    private static String generateServiceMethods(String className) {
        String[] methods = {"count", "selectList", "select", "insert", "update", "delete"};
        StringBuilder sb = new StringBuilder();
        for (String method : methods) {
            if (method.equals("selectList")) {
                sb.append("    public List<Map<String, Object>> ").append(method).append(className).append("(Map<String, Object> map) {\n")
                  .append("        return null; // Implement\n")
                  .append("    }\n\n");
            } else if (method.equals("select")) {
                sb.append("    public Map<String, Object> ").append(method).append(className).append("(Map<String, Object> map) {\n")
                  .append("        return null; // Implement\n")
                  .append("    }\n\n");
            } else {
                sb.append("    public int ").append(method).append(className).append("(Map<String, Object> map) {\n")
                  .append("        return 0; // Implement\n")
                  .append("    }\n\n");
            }
        }
        return sb.toString();
    }

    private static void generateMapper(String className, String packageName) {
        String fileName = BASE_PATH + "\\" + MAPPER_PATH + "\\" + packageName + "\\" + className + "Mapper.java";
        String content = "package " + BASE_PACKAGE + ".mapper." + packageName + ";\n\n"
                + "import org.apache.ibatis.annotations.Mapper;\n"
                + "import java.util.Map;\n"
                + "import java.util.List;\n\n"
                + "@Mapper\n"
                + "public interface " + className + "Mapper {\n\n"
                + generateMapperMethods(className)
                + "}\n";

        writeFile(fileName, content);
    }

    private static String generateMapperMethods(String className) {
        String[] methods = {"count", "selectList", "select", "insert", "update", "delete"};
        StringBuilder sb = new StringBuilder();
        for (String method : methods) {
            if (method.equals("selectList")) {
                sb.append("    List<Map<String, Object>> ").append(method).append(className).append("(Map<String, Object> map);\n\n");
            } else if (method.equals("select")) {
                sb.append("    Map<String, Object> ").append(method).append(className).append("(Map<String, Object> map);\n\n");
            } else {
                sb.append("    int ").append(method).append(className).append("(Map<String, Object> map);\n\n");
            }
        }
        return sb.toString();
    }

    private static void generateXmlFile(String tableName, String className, List<String> columns, String packageName, String primaryColumn) {
        String fileName = XML_PATH + "\\" + packageName + "\\" + className + "-mapper.xml";
        String columnList = String.join(", ", columns);
        String content = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
                + "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n"
                + "<mapper namespace=\"" + BASE_PACKAGE + ".mapper." + packageName + "." + className + "Mapper\">\n\n"
                + generateXmlCrudStatements(tableName, className, columns, primaryColumn)
                + "</mapper>\n";

        writeFile(fileName, content);
    }

    private static String generateXmlCrudStatements(String tableName, String className, List<String> columns, String primaryColumn) {
        String columnList = String.join(", ", columns);
        StringBuilder sb = new StringBuilder();

        sb.append("    <select id=\"count").append(className).append("\" resultType=\"int\">\n")
          .append("        SELECT COUNT(*)\n")
          .append("        FROM ").append(tableName).append("\n")
          .append("    </select>\n\n");

        sb.append("    <select id=\"selectList").append(className).append("\" resultType=\"map\">\n")
          .append("        SELECT \n            ").append(columnList.replaceAll(", ", ",\n            ")).append("\n")
          .append("        FROM ").append(tableName).append("\n")
          .append("    </select>\n\n");

        sb.append("    <select id=\"select").append(className).append("\" parameterType=\"map\" resultType=\"map\">\n")
          .append("        SELECT \n            ").append(columnList.replaceAll(", ", ",\n            ")).append("\n")
          .append("        FROM ").append(tableName).append("\n")
          .append("        WHERE ").append(primaryColumn).append(" = #{").append(primaryColumn).append("}\n")
          .append("    </select>\n\n");

        sb.append("    <insert id=\"insert").append(className).append("\" parameterType=\"map\">\n")
          .append("        INSERT INTO ").append(tableName).append(" (\n            ").append(columnList.replaceAll(", ", ",\n            ")).append("\n")
          .append("        )\n")
          .append("        VALUES (\n            ").append(generateParameterList(columns).replaceAll(", ", ",\n            ")).append("\n")
          .append("        )\n")
          .append("    </insert>\n\n");

        sb.append("    <update id=\"update").append(className).append("\" parameterType=\"map\">\n")
          .append("        UPDATE ").append(tableName).append("\n")
          .append("        SET \n            ").append(generateUpdateSet(columns).replaceAll(", ", ",\n            ")).append("\n")
          .append("        WHERE ").append(primaryColumn).append(" = #{").append(primaryColumn).append("}\n")
          .append("    </update>\n\n");

        sb.append("    <delete id=\"delete").append(className).append("\" parameterType=\"map\">\n")
          .append("        DELETE FROM ").append(tableName).append("\n")
          .append("        WHERE ").append(primaryColumn).append(" = #{").append(primaryColumn).append("}\n")
          .append("    </delete>\n\n");

        return sb.toString();
    }

    private static String generateParameterList(List<String> columns) {
        StringBuilder sb = new StringBuilder();
        for (String column : columns) {
            sb.append("#{").append(column.trim()).append("}, ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    private static String generateUpdateSet(List<String> columns) {
        StringBuilder sb = new StringBuilder();
        for (String column : columns) {
            sb.append(column.trim()).append(" = #{").append(column.trim()).append("}, ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    private static String convertToCamelCase(String text, boolean capitalizeFirst) {
        StringBuilder result = new StringBuilder();
        boolean capitalize = capitalizeFirst;
        for (char c : text.toCharArray()) {
            if (c == '_') {
                capitalize = true;
            } else if (capitalize) {
                result.append(Character.toUpperCase(c));
                capitalize = false;
            } else {
                result.append(Character.toLowerCase(c));
            }
        }
        return result.toString();
    }

    private static String extractPackageName(String tableName) {
        String[] parts = tableName.split("_");
        return parts.length > 0 ? parts[1] : "default";
    }

    private static String extractPackageName2Depth(String tableName) {
        String[] parts = tableName.split("_");
        return parts.length > 0 ? parts[2] : "default";
    }

    private static String decapitalize(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return Character.toLowerCase(text.charAt(0)) + text.substring(1);
    }

    private static void writeFile(String fileName, String content) {
        try {
            File file = new File(fileName);
            File directory = file.getParentFile();
            if (directory != null && !directory.exists()) {
                directory.mkdirs();
            }
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(content);
                System.out.println(fileName + " 파일이 생성됐습니다.");
            }
        } catch (IOException e) {
            System.out.println(fileName + " 파일 생성 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}


```
``` java title="ExcelController - 엑셀 다운로드"
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bizpack.excel.SimpleExcelGenerator;
import com.bizpack.excel.service.ExcelService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/system/excel/")
public class ExcelController {

	@Autowired
	private ExcelService excelService;

	@GetMapping("select")
	public void selectListExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<LinkedHashMap<String, Object>> rtnList = null;
		// 여기에 엑셀 데이터 Service 생성
		rtnList = excelService.selectTestList();
		SimpleExcelGenerator excelFile = new SimpleExcelGenerator(rtnList);
		excelFile.write(response, response.getOutputStream());
	}
}
```
``` java title="SimpleExcelGenerator - 엑셀 다운로드"
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.bizpack.util.ExcelUtil;
import com.bizpack.util.ObjectUtil;
import com.bizpack.util.StringUtil;

import jakarta.servlet.http.HttpServletResponse;

public class SimpleExcelGenerator {

    private static final SpreadsheetVersion supplyExcelVersion = SpreadsheetVersion.EXCEL2007;
    private static final int rowIndex = 0;
    private static final int cellIndex = 0;

    private SXSSFWorkbook sxssWorkbook;
    private SXSSFSheet sxssSheet;
    private String[] cellData;

    public SimpleExcelGenerator(List<LinkedHashMap<String,Object>> data){
    	validateMaxRow(data);
        this.sxssWorkbook = new SXSSFWorkbook();
        if (!data.isEmpty()) {
            this.cellData = StringUtil.extractColumnNames(data.get(0));
        } else {
            this.cellData = new String[0];
        }
        renderExcel(data);
    }

    private void validateMaxRow(List<LinkedHashMap<String,Object>> data){
    	int maxRows = supplyExcelVersion.getMaxRows();
        if (data.size() > maxRows)
            throw new IllegalArgumentException(String.format("현재 Excel 버전은 %s 행 이상 데이터를 지원하지 않습니다.", maxRows));
    }

    private void renderExcel(List<LinkedHashMap<String,Object>> data){
        sxssSheet = sxssWorkbook.createSheet("example");
        renderHeaders(rowIndex, cellIndex);

        if (data.isEmpty())
        return;

        int rowIdx = rowIndex + 1;
		renderBody(data, rowIdx++);
    }

    private void renderHeaders(int rowIdx, int cellIdx){
    	SXSSFRow headerRow = sxssSheet.createRow(rowIdx++);

    	CellStyle greyCellStyle = sxssWorkbook.createCellStyle();
    	ExcelUtil.applyExcelCellStyle(greyCellStyle, new Color(231, 234, 236));

        for (int i = 0; i < cellData.length; i++) {
        	sxssSheet.setColumnWidth(i, (sxssSheet.getColumnWidth(i))+1024);
		}

        for (int i = 0; i < cellData.length; i++) {
        	SXSSFCell headerCell = headerRow.createCell(cellIdx++);
			headerCell.setCellValue(cellData[i]);
			headerCell.setCellStyle(greyCellStyle);
		}
    }

    private void renderBody(List<LinkedHashMap<String,Object>> data, int rowIdx){
    	CellStyle numberStyle = ExcelUtil.numberFormat(sxssWorkbook);
    	CellStyle dateStyle = ExcelUtil.dateFormat(sxssWorkbook);
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    	for (Map<String,Object> map : data) {
			SXSSFRow sxssDataRow = sxssSheet.createRow(rowIdx++);
			for (int i = 0; i < cellData.length; i++) {

				SXSSFCell sxssDataCell = sxssDataRow.createCell(i);

				Object value = map.get(cellData[i]);

				// 숫자인 경우, 날짜인 경우 분기처리
                if (value != null && ObjectUtil.isNumeric(value.toString())) {
                    sxssDataCell.setCellType(CellType.NUMERIC);
                    sxssDataCell.setCellValue(Double.parseDouble(value.toString()));
                    sxssDataCell.setCellStyle(numberStyle);
                } else if (value != null && ObjectUtil.isDate(value.toString())) {
                    try {
                        sxssDataCell.setCellType(CellType.NUMERIC);
                        sxssDataCell.setCellValue(dateFormat.parse(value.toString()));
                        sxssDataCell.setCellStyle(dateStyle);
                    } catch (ParseException e) {
                        sxssDataCell.setCellType(CellType.STRING);
                        sxssDataCell.setCellValue(value.toString());
                    }
                } else {
                    sxssDataCell.setCellType(CellType.STRING);
                    sxssDataCell.setCellValue(StringUtil.isNull(value, ""));
                }
			}
		}
    }

    public void write(HttpServletResponse response,OutputStream stream) throws IOException{
    	response.setHeader("Set-Cookie", "fileDownload=true; path=/");
		response.setHeader("Content-Disposition", "attachment;filename=example.xlsx");

    	sxssWorkbook.write(stream);
    	sxssWorkbook.close();
    	sxssWorkbook.dispose();
        stream.close();
    }
}
```
``` java title="ExcelUtil - 엑셀 다운로드"
import java.awt.Color;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

public class ExcelUtil {

	public static void applyExcelCellStyle(CellStyle cellStyle, Color color) {
		XSSFCellStyle xssfCellStyle = (XSSFCellStyle) cellStyle;
		xssfCellStyle.setFillForegroundColor(new XSSFColor(color, new DefaultIndexedColorMap()));

		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderBottom(BorderStyle.THIN);
	}

	public static CellStyle numberFormat(SXSSFWorkbook sxssWorkbook) {
		CellStyle cellStyle = sxssWorkbook.createCellStyle();
		DataFormat dataFormat = sxssWorkbook.createDataFormat();
    	cellStyle.setDataFormat(dataFormat.getFormat("#,##0"));

    	return cellStyle;
	}

	public static CellStyle dateFormat(SXSSFWorkbook sxssWorkbook) {
		CellStyle cellStyle = sxssWorkbook.createCellStyle();
		DataFormat dataFormat = sxssWorkbook.createDataFormat();
    	cellStyle.setDataFormat(dataFormat.getFormat("yyyy-MM-dd"));

    	return cellStyle;
	}
}
```
``` java title="ObjectUtil - 엑셀 다운로드"
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class ObjectUtil {

	/**
	 * Object Type 별 변수가 비어있는지 체크
	 * @param obj 검사할 Object
	 * @return true / false
	 */
	public static Boolean empty(Object obj){
		if (obj instanceof String) return obj == null || "".equals(obj.toString().trim());
		else if (obj instanceof List) return obj == null || ((List<?>)obj).isEmpty();
		else if (obj instanceof Map) return obj == null || ((Map<?,?>)obj).isEmpty();
		else if (obj instanceof Object[]) return obj == null || Array.getLength(obj) == 0;
		else return obj == null;
	}

	/**
	 * Object Type 별 변수가 비어있지 않는지 체크
	 * @param obj 검사할 Object
	 * @return true / false
	 */
	public static Boolean notEmpty(Object obj){
		return !empty(obj);
	}

	/**
	 * Object Type 별 변수가 숫자로 대체할 수 있는지 체크
	 * @param obj 검사할 Object
	 * @return true / false
	 */
    public static boolean isNumeric(Object value) {
        if (value instanceof Number) {
            return true;
        }
        if (value instanceof String) {
            try {
                Double.parseDouble((String) value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    /**
	 * Object Type 별 변수가 날짜로 대체할 수 있는지 체크
	 * @param obj 검사할 Object
	 * @return true / false
	 */
    public static boolean isDate(Object value) {
        if (value instanceof String) {
            try {
                new SimpleDateFormat("yyyy-MM-dd").parse((String) value);
                return true;
            } catch (ParseException e) {
                return false;
            }
        }
        return false;
    }
}
```
``` java title="StringUtil - 엑셀 다운로드"
package com.bizpack.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;

import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

public class StringUtil {

    /**
	 * Object를 String으로 변환, Object가 널값이 일 경우, defaultStr 반환
	 * @param obj 문자열로된 Object
	 * @param defaultStr obj 가 Null 값일경우 반환할 값
	 * @return Null값 : defaultStr, 그외 : obj
	 */
	public static String isNull(Object obj, String defaultStr){
		String result = defaultStr;
		if(obj != null && !"".equals(obj)){
			if(!"".equals(String.valueOf(obj))){
				result = String.valueOf(obj);
			}
		}
		return result.trim();
	}

	/**
	 * XSS 방어 용으로 " 을 &quot 로 변경한다.
	 * @param str "\" 이 포함된 문자
	 * @return "\" 이 "&amp;quot;" 으로 치환된 문자
	 */
	public static String quot(String str){
		String returnValue = "";
		if(!"".equals(str)){
			returnValue = str.replaceAll("\"", "&quot;");
		}
		return returnValue;
	}

	/**
	 * 정규식 검사 메소드(문자열이 지정된 패턴과 일치하는지 여부 검사)
	 * @param ptn 패턴
	 * @param str 검사할 문자열
	 * @return true | false
	 */
	public static boolean regEx(String ptn, String str){
	    java.util.regex.Pattern p = java.util.regex.Pattern.compile(ptn,java.util.regex.Pattern.CASE_INSENSITIVE | java.util.regex.Pattern.MULTILINE );
	    java.util.regex.Matcher m = p.matcher(str);
	    return m.find();
	}

	/**
	 * 정규식 검사 메소드(문자열중 패턴과 일치하는 부분을 대체 문자열로 치환)
	 * @param ptn 패턴
	 * @param str 검사할 문자열
	 * @param replaceStr 대체할 문자열
	 * @return 변경된 문자열
	 */
	public static String regEx(String ptn, String str, String replaceStr){
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ptn,java.util.regex.Pattern.CASE_INSENSITIVE | java.util.regex.Pattern.MULTILINE );
		java.util.regex.Matcher m = p.matcher(str);
		m.replaceAll(replaceStr);
		return m.replaceAll(replaceStr);
	}

	/**
	 * 영문 대, 소문자, 숫자가 조합된 랜덤한 문자열을 구한다.
	 * @param length 구하고자 하는 문자열 갯수
	 * @return 입력된 길이의 랜덤 문자열
	 */
	public static String getRandomString(int length){
		String[] arrString = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","x","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","X"};
		java.util.Random rnd = new java.util.Random();
		String returnValue = "";
		for(int i =0; i< length; i++){
		returnValue+= arrString[rnd.nextInt(arrString.length-1)];
		}
		return returnValue;
	}

	/**
	 * oracle의 DE_CODE 함수로 deCode("직업", "0,학생,1,회사원,2,주부,3,프리랜서","백수");
	 * @param compareStr 비교할 변수
	 * @param targetPattern 대상이 되는 값과 리턴값 패턴, 콤마로 구분되면 홀수는 비교변수, 짝수는 그에 해당하는 리턴값.
	 * @param otherwiseStr 같은 값이 없을때 가져올 값
	 * @return 패턴의 갯수가 쌍이아니면 NaN 리턴, compareStr 과 targetPattern 값이 같을때 지정한 리턴 값
	 */
	public static String deCode(String compareStr, String targetPattern, String otherwiseStr ){
		String retStr = "";
		String[] arrArgs = targetPattern.split(",");
		String tmpStr = "";
		if(arrArgs.length % 2 == 0){
			for(int i = 0; i < arrArgs.length; i+=2){
				tmpStr = arrArgs[i];
				if(compareStr.equals(tmpStr)){
					retStr = arrArgs[i +1];
					return retStr;
				}
			}
			retStr = otherwiseStr;
		}
		return retStr;
	}

	/**
	 * 10 아래의 숫자앞에 0 붙이기
	 * @param no 숫자
	 * @return 0이 붙은 숫자형 문자
	 */
	public static String getZeroPlus(int no){
		return (no < 10 )? "0" + String.valueOf(no) : String.valueOf(no);
	}

	/**
	 * (length - str.length) 만큼 앞에 0을 추가한다.
	 * @param str 숫자형 문자열
	 * @param length 0을 추가할 단위
	 * @return 0이 추가된 숫자형 문자열
	 */
	public static String getZeroPlus(String str, int length) {
		String temp = "";
		for (int i = str.length(); i < length; i++)
			temp += "0";
		temp += str;
		return temp;
	}

	/**
	 * String.replaceAll 메소드를 사용하는데, jstl에서 사용하기 위해 추가함.
	 * @param strSource replaceAll 을 사용할 문자열
	 * @param strPattern 변경 할 패턴
	 * @param strResult 변경할 문자
	 * @return 변경된 문자열
	 */
	public static String replaceAll(String strSource, String strPattern, String strResult){
		return strSource.replaceAll(strPattern, strResult);
	}

	/**
	 * 게시판 제목등 문자열 길이를 설정한 갯수에 맞게 잘라온다
	 * @param str 문자열
	 * @param len 짜를 갯수(영문,숫자는 1 한글등 유니코드는 2로 계산해서 보통 출력되는 갯수 *2 값으로 설정)
	 * @param trail 짜른뒤에 붙일 문자열 "..."
	 * @return 짜르고 "..." 처리된 문자열
	 */
	public static String cutString(String str, int len, String trail) {
		if (str==null) return "";
		String returnValue = str;
		int slen = 0;
		char c;
		for(int i=0; i < str.length();i++){
			c = str.charAt(i);
			if(c > 127) slen+=2;
			else slen++;
			if(slen > len){
				returnValue = str.substring(0, i) + trail;
				break;
			}
		}
		return returnValue;
	}

	/**
	 * GET 방식으로 변수를 넘길때 특수문자, 한글등을 변환해준다.
	 * @param str 변환할 변수 문자열
	 * @return 변환된 변수 문자열
	 */
	public static String URLEncoding(String str){
		try {
			str =  java.net.URLEncoder.encode(str,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			LogUtil.exlog(new Object().getClass(), e);
		}
		return str;
	}

	/**
	 * 문자열을 구분자로 분리하여 배열 리턴
	 * @param str Split기준이 되는 문자
	 * @param del 구분자
	 * @return 문자 배열
	 */
	public static String[] strSplit(String str, String del){
		if(!"".equals(str)){
			return new String[0];
		}
		return str.split(del);
	}

	/**
	 * String문자열을 자르고자 하는 문자(str)을 입력 후 얻어오고자 하는 위치(position)의 문자를 가져옴.
	 * @param str Split기준이 되는 문자
	 * @param position 자르고 난 후 가져올 문자의 위치
	 * @return 문자
	 */
	public static String strSplitGet(String str, String position){
		String returnValue = "";
		if(!"".equals(isNull(str, ""))){
			String[] arrStr = str.split(">");
			if(!isNumeric(position)) return null;
			returnValue = arrStr[Integer.parseInt(position)];
		}
		return returnValue;
	}

	/**
	 * 전화번호 구분 (-,~)
	 * String문자열을 자르고자 하는 문자(str)을 입력 후 얻어오고자 하는 위치(position)의 문자를 가져옴.
	 * @param str Split기준이 되는 문자
	 * @param position 자르고 난 후 가져올 문자의 위치
	 * @return 자른배열[position] 문자
	 */
	public static String strSplitGetPhone(String str, String position){
		String returnValue = "";
		if(!"".equals(isNull(str, ""))){
			String[] arrStr = str.split("-|~");
			returnValue = arrStr[Integer.parseInt(position)];
		}
		return returnValue;
	}

	public static String getAntiHtml(String contents, String type){
		if("".equals(isNull(contents, ""))) return "";
		String result = contents;
		result = result.replaceAll("\"", "&quot;");
		result = result.replaceAll("\\r", " ");
		result = result.replaceAll("&nbsp;", " ");
		result = result.replaceAll("\\n", " ");
		result = result.replaceAll("'", "&apos;");
		result = result.replaceAll("<[^>]+>", " ");
		return result;
	}

	public static String sqlInjetFilter(String contents){
		if(ObjectUtil.empty(contents)) return "";
		String result = contents;

		/*특수문자공백처리*/
		Pattern SpecialChars = Pattern.compile("[\"\\#()@;=*/+]");
		result = SpecialChars.matcher(result).replaceAll("");

		String regex = "(union|select|from|where)";

		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(result);


		if(matcher.find()){
		    result = "";
		}

		return result;
	}

	public static String stripXss(String contents){
		if("".equals(isNull(contents, ""))) return "";
		String result = contents;
		result = result.replaceAll("\0", "");//널문자

		result = result.replaceAll("<", "&lt;");
		result = result.replaceAll(">", "&gt;");

		result = result.replaceAll("\\(", "&#40;");
		result = result.replaceAll("\\)", "&#41;");

		result = result.replaceAll("\"", "&quot;");
		result = result.replaceAll("'", "&apos;");


		result = result.replaceAll("  ", "&nbsp;");//공백은   되게 함
		result = result.replaceAll("\\n", "<br />");//줄바꿈은 되게 함
		return result;
	}


	public static String stripXssScript(String contents){
		if("".equals(isNull(contents, ""))) return "";
		//String result = contents;
		String result = getRemoveScript(contents);

		result = result.replaceAll("<", "&lt;");
		result = result.replaceAll(">", "&gt;");

		result = result.replaceAll("\\(", "&#40;");
		result = result.replaceAll("\\)", "&#41;");

		result = result.replaceAll("\"", "&quot;");
		result = result.replaceAll("'", "&apos;");

		return result;
	}

	public static String getRemoveScript(String strContent){

		strContent = strContent.replaceAll("<script[(^\\w)|(^\\W)]*?</script>", "");
		strContent = strContent.replaceAll("onclick=[\"']?([^>\"']+)[\"']*", "");
		strContent = strContent.replaceAll("onmouseover=[\"']?([^>\"']+)[\"']*", "");
		strContent = strContent.replaceAll("onmouseout=[\"']?([^>\"']+)[\"']*", "");
		strContent = strContent.replaceAll("onkeydown=[\"']?([^>\"']+)[\"']*", "");
		strContent = strContent.replaceAll("onkeyup=[\"']?([^>\"']+)[\"']*", "");
		strContent = strContent.replaceAll("onblur=[\"']?([^>\"']+)[\"']*", "");
		strContent = strContent.replaceAll("onchange=[\"']?([^>\"']+)[\"']*", "");

		return strContent;
	}

	public static String decodeXss(String contents){
		if("".equals(isNull(contents, ""))) return "";
		String result = contents;

		result = result.replaceAll("&lt;","<");
		result = result.replaceAll("&gt;",">");

		result = result.replaceAll("&#40;","\\(");
		result = result.replaceAll("&#41;","\\)");

		result = result.replaceAll("&quot;","\"");
		result = result.replaceAll("&apos;","'");

		result = result.replaceAll("&nbsp;"," ");//공백은   되게 함

		return result;
	}


	public static String decodeHTMLTagFilter(String contents){
		if("".equals(isNull(contents, ""))) return "";
		String result = contents;

		result = result.replaceAll("&lt;","<");
		result = result.replaceAll("&gt;",">");
		result = result.replaceAll("&amp;","&");
		result = result.replaceAll("&quot;","\"");
		result = result.replaceAll("&apos;","'");

		return result;
	}

	/**
	 * 원본 문자열에서 검색 문자열의 존재 여부를 알아낸다.(대소문자무시)
	 * @param originString 원본문자열 ,
	 * @param str 검색문자열
	 * @return 해당 운영체제가 맞는지 여부
	 */
	public static boolean isContainStr(String str,String originString){
		return originString.toLowerCase().indexOf(str.toLowerCase()) != -1;
	}

	 /**
	 * 문자열 숫자인지 아닌지 판단
	 * @param s : 숫자형 문자열
	 * @return true|false
	 */
   public static boolean isNumeric(String s) {
       return s.matches("[-+]?\\d*\\.?\\d+");
   }

   /**
	 * 배열스트링(arr) 안에 받아온 String a가 포함 되어있는지 체크
	 * @param a : String
	 * @param arr : String배열
	 * @return String
	 */
   public static boolean strInArrChk(String arr,String a) {
   	if(ObjectUtil.notEmpty(arr)) {
   		for (int j2 = 0; j2 < arr.split(",").length; j2++) {
   			if(arr.split(",")[j2].trim().equals(a.trim())){
   				return true;
   			}
   		}
   	}
   	return false;
   }

   /**
    * 문자열을 지정된 지점에서부터 *로 치환(끝나는 지점이 지정되지 않으면 끝까지 치환)
    * @param str 치환할 문자열
    * @param start 치환할 시작 위치
	 * @param end  치환할 종료 위치 0 일시 끝까지
    * @return str 치환문
    * @throws IOException
    */
   public static String strConceal(String str, int start, int end) throws IOException{
   	if ("".equals(str)) {
   		return "(내용없음)";
   	}

   	StringBuilder builder = new StringBuilder(str);
   	if(0==end) end = str.length();
   	for (int i = start; i < end; i++) {
   		builder.setCharAt(i, '*');
		}
   	str = builder.toString();

   	return str;
   }

	/**
	 * 확인할 문자열이 prefix 로 시작되는지 확인
	 * @param str 확인할 문자열
	 * @param prefix 시작 prefix
	 * @return true / false
	 */
   public static boolean startWith(String str, String prefix){
   	return str.startsWith(prefix);
   }

	/**
	 * List에 있는 key 값을 가져와서 배열로 변환
	 * @param List<Map<String,Object>> 확인할 List
	 * @return firstRow.keySet().toArray(new String[0])
	 */
   public static String[] extractColumnNames(Map<String, Object> data) {

       Set<String> keySet = data.keySet();
       return keySet.toArray(new String[0]);
   }

   /**
    * 숫자에 천 단위 콤마를 추가하여 문자열로 반환하는 유틸리티 함수
    *
    * @param amount 포맷팅할 숫자
    * @return 천 단위 콤마가 추가된 문자열
    */
   public static String formatPrice(double amount) {
       NumberFormat formatter = NumberFormat.getInstance();
       return formatter.format(amount);
   }

   /**
    * 문자열의 첫 글자 대문자로 변환
    *
    * @param amount 포맷팅할 숫자
    * @return 문자열 반환
    */
   public static String strCapitalize(String inStr) {
	   	if(StringUtil.isNull(inStr, "").equals("")) return inStr;

	   	char firstLetter = Character.toUpperCase(inStr.charAt(0));
		return firstLetter + inStr.substring(1);
   }

   /**
    * HttpServletRequest to Map 변환
    *
    * @param HttpServletRequest request
    * @return map data
    */
   public static Map<String, Object> convertReqToMap(HttpServletRequest request) {

		Map<String, Object> hmap = new HashMap<String, Object>();
		String key;

		Enumeration<?> nEnum = request.getParameterNames();

		while (nEnum.hasMoreElements()) {
			key = (String) nEnum.nextElement();
			if (request.getParameterValues(key).length > 1) {
				hmap.put(key, request.getParameterValues(key));
			} else {
				hmap.put(key, request.getParameter(key));
			}

		}

		return hmap;
   }

   /**
	 * HTML 태그 제거, &amp;nbsp " " 치환 메소드
	 * @param contents 태그가 포함된 문자열
	 * @return 태그가 제거된 문자열
	 */
	public static String stripTag(String contents){
		if("".equals(StringUtil.isNull(contents, ""))) return "";
		contents = StringUtil.regEx("(?:<!.*?(?:--.*?--s*)*.*?>)|(?:<(?:[^>'\"]*|\".*?\"|'.*?')+>)", contents, "");
		return contents.replaceAll("&nbsp;"," ");
	}

	/**
	 * form select box selected 설정 TagLib에서도 사용 반드시 escapeXml="false" 추가
	 * @param val 원본 변수값
	 * @param compare1 비교 변수값
	 * @return selected = "selected" OR "";
	 */
	public static String selected(String val, String compare1){
		String returnValue = "";
		if(StringUtil.isNull(val, "").equals(StringUtil.isNull(compare1,"")) ){
			returnValue = "selected=\"selected\"";
		}
		return returnValue;
	}

	/**
	 * form radio, checkbox checked 설정  반드시 escapeXml="false" 추가
	 * @param val 원본 변수값
	 * @param compare1 비교 변수값
	 * @return checked="checked" OR ""
	 */

	public static String checked(String val, String compare1){
		String returnValue = "";
		if(StringUtil.isNull(val, "").equals(StringUtil.isNull(compare1,"")) ){
			returnValue = "checked=\"checked\"";
		}
		return returnValue;
	}

	/**
	 * @param val 원본 변수값
	 * @param compare1 비교 변수값
	 * @return readonly="readonly" OR ""
	 */
	public static String readOnly(String val, String compare1){
		String returnValue = "";
		if(StringUtil.isNull(val, "").equals(StringUtil.isNull(compare1,"")) ){
			returnValue = "disabled";
		}
		return returnValue;
	}

	/**
	 * request 에서 접속중인 클라이언트의 "IP"를 알아 낸다.
	 * @param request
	 * @return ip : ipv4 주소
	 */
	public static String getCurrentRequestIp() {
	   RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
	   if(attributes instanceof ServletRequestAttributes servletRequestAttributes) {
		   HttpServletRequest request = servletRequestAttributes.getRequest();
		   return getClientIp(request);
	   }
	   return null;
	}

	public static String getClientIp(HttpServletRequest request) {
	    String ip = request.getHeader("X-Forwarded-For");

	    if (ip == null) {
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if (ip == null) {
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if (ip == null) {
	        ip = request.getHeader("HTTP_CLIENT_IP");
	    }
	    if (ip == null) {
	        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	    }
	    if (ip == null) {
	        ip = request.getRemoteAddr();
	    }

	    return ip;
	}

	/**
	 * 문자열에서 숫자 부분만 추출
	 * @param str 숫자로 변환할 문자열
	 * @return result 숫자 반환
	 */

	public static String getNumberToString(String str) {

		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(str);

		String result = "";

		if(matcher.find()){
			result = matcher.group();
		}

		return result;
	}

	/**
	 * 배열로 받은 데이터를 ,로 연결
	 * @param strValues ,로 연결할 배열 데이터
	 * @return result ,로 연결한 값
	 */

	public static String getCommaToArrange(String[] strValues) {
		String str = "";
		if(strValues != null) {
			str = String.join(",", strValues);
		}
		return str;
	}

	/**
	 * 공백('',"")인 데이터를 null로 변경
	 * @param Map<String, Object> map
	 * @return result
	 */

	public static Map<String, Object> convertEmptyStringsToNull(Map<String, Object> map) {
		Map<String, Object> result = new HashMap<>();
	    for (Map.Entry<String, Object> entry : map.entrySet()) {
	        Object val = entry.getValue();
	        if (val instanceof String && ((String) val).trim().isEmpty()) {
	            result.put(entry.getKey(), null);
	        } else {
	            result.put(entry.getKey(), val);
	        }
	    }
	    return result;
	}
}
```
``` java title="DateUtil - 날짜 유틸"
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
	/**
	 * 오늘날짜를 패턴에 맞게 가져오기
	 * @param pattern yyyy-MM-dd : 2019-02-28 , yyyy-MM-dd HH:mm:ss.SSS : 2019-02-28 01:59:28.002
	 * @return 날짜
	 */
	public static String getDatePattern(String pattern){
		String rtnStr = null;
	   	try {
	   		java.text.SimpleDateFormat sdfCurrent = new java.text.SimpleDateFormat(pattern, Locale.KOREA);
	   	    java.sql.Timestamp ts = new java.sql.Timestamp(System.currentTimeMillis());
	   	    rtnStr = sdfCurrent.format(ts.getTime());
	   	} catch (IllegalArgumentException e) {
	   		LogUtil.exlog(new Object().getClass(), e);
	   	}
	   	return rtnStr;
	}

	/**
	 * 입력된 날짜를 패턴에 맞게 가져온다
	 * @param pattern (yyyy-MM-dd : 2019-02-28 , yyyy-MM-dd HH:mm:ss.SSS : 2019-02-28 01:59:28.002
	 * @return 날짜
	 */
	public static String printDatePattern(String date, String pattern){
		if(date == null) return "";
		String rtnStr = null;
		Date date1 = null;
		Calendar cal1 = Calendar.getInstance();
		java.text.SimpleDateFormat sdfCurrent = new java.text.SimpleDateFormat(pattern, Locale.KOREA);
		if(checkDate(date,pattern)){
			try {
				date1 = sdfCurrent.parse(date);
				rtnStr = sdfCurrent.format(date1);
			} catch (ParseException e) {
				LogUtil.exlog(new Object().getClass(), e);
			} catch (IllegalArgumentException e) {
				LogUtil.exlog(new Object().getClass(), e);
			}
		}

		if(date1 == null && checkDate2(date)){
			java.text.SimpleDateFormat sdfCurrent2 = new java.text.SimpleDateFormat("yyyy-M-d.HH.mm.ss.S", Locale.KOREA);
			try {
				date1 = sdfCurrent2.parse(date);
				rtnStr = sdfCurrent.format(date1);
			} catch (ParseException e) {
				LogUtil.exlog(new Object().getClass(), e);
			} catch (IllegalArgumentException e) {
				LogUtil.exlog(new Object().getClass(), e);
			}
		}
		if(date1 == null) return date;
		cal1.setTime(date1);
		return sdfCurrent.format(new java.sql.Timestamp(cal1.getTimeInMillis()));
	}

	/**
	 * 문자열이 패턴과 일치한 날짜인지 확인하는 메소드
	 * @param str 문자열
	 * @param pattern 날짜 패턴
	 * @return 날짜가 아니거나 패턴과 일치하지 않으면 false / 그외 true
	 */
	public static boolean checkDate(String str, String pattern){

		boolean dateValidity = true;
		SimpleDateFormat df = new SimpleDateFormat(pattern,Locale.KOREA); //20041102101244
		df.setLenient(true); // false 로 설정해야 엄밀한 해석을 함.
		try {
			Date dt = df.parse(str);
		}catch(ParseException pe){
			dateValidity = false;
		}

		return dateValidity;
	}

	/**
	 * 문자열이 날짜인지 확인하는 메소드
	 * @param str 문자열
	 * @return 날짜가 아니면 false / 그외 true
	 */
	public static boolean checkDate2(String str){
		boolean dateValidity = true;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-M-d.HH.mm.ss.S",Locale.KOREA); //20041102101244
		df.setLenient(false); // false 로 설정해야 엄밀한 해석을 함.
		try {
			Date dt = df.parse(str);
		}catch(ParseException pe){
			dateValidity = false;
		}catch(IllegalArgumentException ae){
			dateValidity = false;
		}
		return dateValidity;
	}

	/**
	 * date형식을 가진 String value의 패턴을 변경한다.
	 * @param stringDate date형식을 가진 String value(yyyy-MM-dd : 2019-02-28 , yyyy-MM-dd HH:mm:ss.SSS : 2019-02-28 01:59:28.002
	 * @param old_pattern 이전 패턴
	 * @param new_pattern 변경할 패턴
	 * @return stringDate
	 */
	public static String convertDatePattern(String stringDate, String old_pattern, String new_pattern){
		SimpleDateFormat formatter = new SimpleDateFormat(old_pattern,new Locale("en", "US"));
		try {
			Date time = formatter.parse(stringDate);
			return new SimpleDateFormat(new_pattern).format(time);
		} catch (ParseException e) {
			LogUtil.exlog(new Object().getClass(), e);
		}
		return stringDate;
	}

	/**
	 * 입력된 날짜(yyyy-MM-dd)형식이 유효한 날짜인지를 검사한다.
	 * @param dt yyyy-MM-dd 형식의 날짜형식 문자열
	 * @return 유효한 날짜 true, 아니면 false
	 */
	public static boolean isDate(String dt){
		try{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			format.setLenient(false);
			format.parse(dt);
		} catch (ParseException e) {
			return false;
		} catch (IllegalArgumentException e) {
			return false;
		}
       return true;
	}

	/**
	 * 입력된 날짜(yyyy-MM-dd)형식이 유효한 날짜인지를 검사한다.
	 * @param m : 월, d : 일, y : 연
	 * @return 유효한 날짜 true, 아니면 false
	 */
	public static boolean isDate(int m, int d, int y)
    {
        m -= 1;
        Calendar c = Calendar.getInstance();
        c.setLenient(false);
        try{
                c.set(y,m,d);
                java.util.Date dt = c.getTime();
        }catch (IllegalArgumentException e){
                return false;
        }
        return true;
    }

	/**
	 * 입력된 값과 현재 시간의 차이를   이전인지 현재인지 지났는지 검사
	 * 입력날짜기 기준(strDate - nowDate)
	 * @param strDate yyyy-MM-dd 형식으로 날짜만
	 * @return 1: 전, 0:오늘날짜, -1:지난날짜
	 */
	public static int dateDiff(String strDate){
		return dateDiff(strDate, "yyyy-MM-dd");
	}

	/**
	 * 입력된 값과 현재 시간의 차이를   이전인지 현재인지 지났는지 검사
	 * 입력날짜기 기준(strDate - nowDate)
	 * @param strDate 날짜형 문자열
	 * @param pattern 날짜 패턴
	 * @return 1: 전, 0:오늘날짜, -1:지난날짜
	 */
	public static int dateDiff(String strDate, String pattern){
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
//		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(pattern, Locale.KOREA);

		Date date1 = null;
		try {
			date1 = sdf.parse(strDate);
		} catch (ParseException e) {
			LogUtil.exlog(new Object().getClass(), e);
		}
		Date date2 = null;

		try {
			date2 = sdf.parse(getDatePattern(pattern));
		} catch (ParseException e) {
			LogUtil.exlog(new Object().getClass(), e);
		}

		cal1.setTime(date1);
		cal2.setTime(date2);
		int byInt = 60*60*24*1000;
		if(pattern.indexOf("HH") > -1) byInt = 60*60*1000;
		if(pattern.indexOf("mm") > -1) byInt = 60*1000;
		if(pattern.indexOf("ss") > -1) byInt = 1000;
		int diff = (int)((cal1.getTimeInMillis() - cal2.getTimeInMillis())/(long)(byInt));
		if(diff > 0){
			return 1;
		}else if(diff < 0){
			return -1;
		}else{
			return 0;
		}
	}
	/**
	 * 두 날짜의 차이를 설정한 type에 맞게 가져온다. 주의할것은 strDate1 - strDate2 이므로 양,음수를 잘 구분해야함
	 * @param strDate1 첫번째 날짜
	 * @param strDate2 두번째 날짜
	 * @param pattern 날짜 패턴 두 날짜가 패턴과 일치해야함 (yyyy-MM-dd HH:mm:ss) 형식
	 * @param type 리턴받는 두 날짜의 차이를 일(d), 시간(h), 분(m) 단위로 선택함
	 * @return
	 */
	public static int dateDiff(String strDate1, String strDate2, String pattern, String type){
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(pattern, Locale.KOREA);

		Date date1 = null;
		Date date2 = null;
		try {
			date1 = sdf.parse(strDate1);
			date2 = sdf.parse(strDate2);
		} catch (ParseException e) {
			LogUtil.exlog(new Object().getClass(), e);
		}

		cal1.setTime(date1);
		cal2.setTime(date2);
		long divide = 0;
		if("d".equals(type)){
			divide = 60*60*24*1000;
		}else if("h".equals(type)){
			divide = 60*60*1000;
		}else{
			divide = 60 * 1000;
		}
		return (int)((cal1.getTimeInMillis() - cal2.getTimeInMillis())/(divide));

	}

	/**
	 * 해당 날짜를 원하는 날수 만큼 증가, 감소 시킨다.
	 * @param strDate 기준 날짜, 날짜 형식으로 입력.
	 * @param intIncrease 증가 또는 감소 하고자 하는 수치
	 * @return 변경된 날짜 문자열
	 */
	public static String dateAdd(String strDate, int intIncrease){
		Calendar cal1 = Calendar.getInstance();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
		Date date1 = null;
		try {
			date1 = sdf.parse(strDate);
		} catch (ParseException e) {
			LogUtil.exlog(new Object().getClass(), e);
		}
		cal1.setTime(date1);
		long addTimeStamp = ((long)(60*60*24*1000) * intIncrease);
		cal1.setTimeInMillis(cal1.getTimeInMillis() + addTimeStamp);

		return sdf.format(new java.sql.Timestamp(cal1.getTimeInMillis()));
	}

	/**
	 * 해당 날짜를 패턴을 지정하여 원하는 날수 만큼 증가, 감소 시킨다.
	 * @param strDate 기준 날짜, 날짜 형식으로 입력.
	 * @param intIncrease 증가 또는 감소 하고자 하는 수치
	 * @param pattern (yyyy-MM-dd : 2019-02-28 , yyyy-MM-dd HH:mm:ss.SSS : 2019-02-28 01:59:28.002)
	 * @return 변경된 날짜 문자열
	 */
	public static String dateAdd(String strDate, int intIncrease, String pattern){
		Calendar cal1 = Calendar.getInstance();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(pattern, Locale.KOREA);
		Date date1 = null;
		try {
			date1 = sdf.parse(strDate);
		} catch (ParseException e) {
			LogUtil.exlog(new Object().getClass(), e);
		}
		cal1.setTime(date1);
		long addTimeStamp = ((long)(60*60*24*1000) * intIncrease);
		cal1.setTimeInMillis(cal1.getTimeInMillis() + addTimeStamp);

		return sdf.format(new java.sql.Timestamp(cal1.getTimeInMillis()));
	}

	/**
	 * 오늘을 기준으로 원하는 날수 만큼 증가, 감소 시킨다.
	 * @param intString 증가 또는 감소 하고자 하는 수치
	 * @param pattern 반환받고자 하는 날짜 패턴
	 * @return 변경된 날짜 문자열
	 */
	public static String todayAdd(String intString, String pattern){
		int intIncrease = 0;
		if(ObjectUtil.notEmpty(intIncrease)){
			intIncrease = Integer.parseInt(intString);
		}
		Calendar cal1 = Calendar.getInstance();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(pattern, Locale.KOREA);
		Date date1 = new Date();
		cal1.setTime(date1);
		long addTimeStamp = ((long)(60*60*24*1000) * intIncrease);
		cal1.setTimeInMillis(cal1.getTimeInMillis() + addTimeStamp);

		return sdf.format(new java.sql.Timestamp(cal1.getTimeInMillis()));
	}

	/**
	 * 특정 날짜를 기준으로 원하는 만큼 달을 증가, 감소 시킨다.
	 * @param date 변경할 날짜
	 * @param months 증가 또는 감소 하고자 하는 수치
	 * @return 변경된 날짜
	 */
    public static Date addMonth(Date date, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, months);
        return cal.getTime();
    }

	/**
	 * 요청하는 날짜 패턴에 맞춰 오늘 날짜 반환
	 * @param schOption 요청자
	 * @return HOUR : yyyy-MM-dd
	 * 		   DAY_M : yyyy-MM
	 * 		   MONTH : yyyy
	 * 		   YEAR : ""
	 */
	public static String getTodayforStat(String schOption) {
		String today="";
		if("HOUR".equals(schOption)){
			today = getDatePattern("yyyy-MM-dd");
		}else if("DAY_M".equals(schOption)){
			today = getDatePattern("yyyy-MM");
		}else if("MONTH".equals(schOption)){
			today = getDatePattern("yyyy");
		}else if("YEAR".equals(schOption)){
			today= "";
		}
		return today;
	}

	/**
	 * 요청하는 날짜 패턴에 맞춰 요청 날짜 반환
	 * @param Date 요청 날짜
	 * @param schOption 요청자
	 * @return HOUR : yyyy-MM-dd
	 * 		   DAY_W : yyyy-MM
	 * 		   DAY_M : yyyy-MM
	 * 		   MONTH : yyyy
	 * 		   YEAR : yyyy
	 */
	public static String getDateforStat(String Date, String schOption) {
		String today="";
		if("HOUR".equals(schOption)){
			today = printDatePattern(Date,"yyyy-MM-dd");
		}else if("DAY_W".equals(schOption)){
			today = printDatePattern(Date,"yyyy-MM");
		}else if("DAY_M".equals(schOption)){
			today = printDatePattern(Date,"yyyy-MM");
		}else if("MONTH".equals(schOption)){
			today = printDatePattern(Date,"yyyy");
		}else if("YEAR".equals(schOption)){
			today = printDatePattern(Date,"yyyy");
		}
		return today;
	}

    /**
     * 특정 날짜에 대하여 요일을 구함(일 ~ 토)
     * @param date
     * @param datePattern
     * @return
     * @throws IOException
     */
    public static String getDateDay(String date, String datePattern) throws IOException, ParseException {

        String[] week = {"일", "월", "화", "수", "목", "금", "토"};
        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern) ;
        Date nDate = dateFormat.parse(date) ;
        Calendar cal = Calendar.getInstance() ;
        cal.setTime(nDate);
        int dayNum = cal.get(Calendar.DAY_OF_WEEK) ;

        return week[dayNum+1] ;
    }
}
```