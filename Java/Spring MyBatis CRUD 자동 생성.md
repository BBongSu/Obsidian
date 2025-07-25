---
sticker: emoji//270f-fe0f
---
``` java
/*

	포탈 DB 테이블명과 컬럼명들을 쉼표로 구분해서 입력하면 Controller, Service, Mapper, xml 파일 생성

	## 입력 예시

	테이블명을 입력하세요 : bpp_ast_fix
	컬럼명을 쉼표(,)로 구분하여 입력하세요 : pafSeq, pafClss, pafAstCd, pafPdtNm, pafModelNm, pafSerialNo, pafBuyDt, pafBuyPrc, pafBuyPlac, pafCompAddr, pafCompDetlAddr, pafBuyPrch, pafBuyDprt, pafMainPrch, pafMainDprt, pafSubPrch, pafSubDprt, pafWsteDt, pafWsteYn, pafRgstr, pafRegDt, pafModifr, pafModDt, pafIp, pafDelYn

	## 출력 예시

	BppPorTestController.java 파일이 생성됐습니다.
	BppPorTestService.java 파일이 생성됐습니다.
	BppPorTestMapper.java 파일이 생성됐습니다.
	BppPorTest-mapper.xml 파일이 생성됐습니다.

	## 공통 생성 코드 예시

	Controller Annotation : @RestController, @RequestMapping
	Controller 공통 RequestMapping : /portal/por/test
	Controller PostMapping Name : count[BppPorTest], selectList[BppPorTest], select[BppPorTest], insert[BppPorTest], update[BppPorTest], delete[BppPorTest]
	Controller 공통 매개변수 : @RequestBody Map<String, Object> map
	Controller package : com.bizpack.portal.controller.por

	Service Annotation : @Service, @Transactional
	Service package : com.bizpack.portal.service.por

	Mapper Annotation : @Mapper
	Mapper package : com.bizpack.portal.mapper.por

	BppPorBrd-mapper.xml : MyBatis이고 위에 작성한 함수들의 SELECT, INSERT, UPDATE. DELETE 기본 CRUD 세팅.
	BppPorBrd-mapper namespace : com.bizpack.portal.mapper.por.BppPorTestMapper


	## 참고

	- 1번째로 입력하는 값은 기본키 or Seq여야 함

*/

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

