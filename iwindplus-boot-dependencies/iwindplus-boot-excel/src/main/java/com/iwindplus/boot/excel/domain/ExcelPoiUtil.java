/**
 * Copyright (c) iwindplus Technologies Co., Ltd.2011-2020, All rights reserved.
 */

package com.iwindplus.boot.excel.domain;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.hutool.core.util.IdUtil;
import com.iwindplus.boot.excel.domain.dto.ExcelExportAnnDTO;
import com.iwindplus.boot.excel.domain.dto.ExcelExportCustomDTO;
import com.iwindplus.boot.excel.domain.dto.ExcelImportFileDTO;
import com.iwindplus.boot.excel.domain.dto.ExcelImportStreamDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * poi excel工具类.
 *
 * @author zengdegui
 * @since 2018/9/1
 */
public class ExcelPoiUtil {
    /**
     * xls 类型.
     */
    public static final String XLS = "xls";

    /**
     * xlsx 类型.
     */
    public static final String XLSX = "xlsx";

    /**
     * excel导出（自定义导出格式）.
     *
     * @param entity 对象
     * @throws Exception 异常
     */
    public static void exportExcel(ExcelExportCustomDTO entity) throws Exception {
        List<Map<String, Object>> list = entity.getList();
        String title = entity.getTitle();
        String sheetName = entity.getSheetName();
        String excelType = entity.getExcelType();
        String fileName = entity.getFileName();
        HttpServletRequest request = entity.getRequest();
        HttpServletResponse response = entity.getResponse();

        ExportParams exportParams = getExcelType(title, sheetName, excelType);
        exportParams.setMaxNum(entity.getMaxNum());
        if (null != entity.getColorClass()) {
            exportParams.setStyle(entity.getColorClass());
        }
        // 封装自定义导出字段和标题
        List<ExcelExportEntity> entityList = entity.getEntityList();
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, entityList, list);
        if (workbook != null) {
            fileName = getFileName(fileName, excelType);
            downLoadExcel(fileName, workbook, request, response);
        }
    }

    /**
     * excel导出（基于注解导出格式）.
     *
     * @param entity 对象
     * @throws Exception
     */
    public static void exportExcel(ExcelExportAnnDTO entity) throws Exception {
        List<?> list = entity.getList();
        String title = entity.getTitle();
        String sheetName = entity.getSheetName();
        String excelType = entity.getExcelType();
        String fileName = entity.getFileName();
        Class<?> pojoClass = entity.getPojoClass();
        HttpServletRequest request = entity.getRequest();
        HttpServletResponse response = entity.getResponse();

        ExportParams exportParams = getExcelType(title, sheetName, excelType);
        exportParams.setMaxNum(entity.getMaxNum());
        if (null != entity.getColorClass()) {
            exportParams.setStyle(entity.getColorClass());
        }
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
        if (workbook != null) {
            fileName = getFileName(fileName, excelType);
            downLoadExcel(fileName, workbook, request, response);
        }
    }

    /**
     * 导入本地excel（文件上传方式） 字段类型.
     * Integer,Long,Double,Date,String,Boolean支持校验,支持Key-Value
     *
     * @param entity 对象
     * @return ExcelImportResult<T>
     * @throws Exception
     */
    public static <T> ExcelImportResult<T> importExcel(ExcelImportStreamDTO entity) throws Exception {
        InputStream inputStream = entity.getFile().getInputStream();
        Class<?> pojoClass = entity.getPojoClass();
        ImportParams params = new ImportParams();
        // 是否导入验证
        if (entity.getNeedVerify()) {
            params.setNeedVerify(true);
            params.setVerifyGroup(entity.getVerifyGroup());
            params.setVerifyHandler(entity.getVerifyHandler());
        }
        return ExcelImportUtil.importExcelMore(inputStream, pojoClass, params);
    }

    /**
     * 导入本地excel（加载本地文件方式） 字段类型.
     * Integer,Long,Double,Date,String,Boolean支持校验,支持Key-Value
     *
     * @param entity 对象
     * @return ExcelImportResult
     * @throws Exception
     */
    public static <T> ExcelImportResult<T> importExcel(ExcelImportFileDTO entity) throws Exception {
        File file = entity.getFile();
        Class<?> pojoClass = entity.getPojoClass();
        ImportParams params = new ImportParams();
        // 是否导入验证
        if (entity.getNeedVerify()) {
            params.setNeedVerify(true);
            params.setVerifyGroup(entity.getVerifyGroup());
            params.setVerifyHandler(entity.getVerifyHandler());
        }
        return ExcelImportUtil.importExcelMore(file, pojoClass, params);
    }

    /**
     * 设置导出参数设置.
     *
     * @param title     表格标题
     * @param sheetName 表格名称
     * @param excelType 文件类型（xls,xlsx）
     * @return ExportParams
     * @throws Exception
     */
    private static ExportParams getExcelType(String title, String sheetName, String excelType) throws Exception {
        ExportParams exportParams = null;
        if (StringUtils.endsWithIgnoreCase(XLS, excelType)) {
            exportParams = new ExportParams(title, sheetName, ExcelType.HSSF);
        } else {
            exportParams = new ExportParams(title, sheetName, ExcelType.XSSF);
        }
        return exportParams;
    }

    /**
     * 获取文件名.
     *
     * @param fileName  文件名称
     * @param excelType 导出表格文件类型
     * @return String
     */
    private static String getFileName(String fileName, String excelType) {
        if (StringUtils.isNotBlank(fileName)) {
            fileName = new StringBuilder().append(fileName.trim()).append(".").append(excelType).toString();
        } else {
            fileName = new StringBuilder().append(IdUtil.fastSimpleUUID()).append(".").append(excelType).toString();
        }
        return fileName;
    }

    /**
     * 下载excel文件.
     *
     * @param fileName 文件名
     * @param workbook 表格
     * @param request  请求
     * @param response 响应
     * @throws Exception
     */
    private static void downLoadExcel(String fileName, Workbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-Type", "application/vnd.ms-excel");
        // 针对IE或者以IE为内核的浏览器
        String userAgent = request.getHeader("user-agent").toLowerCase(LocaleContextHolder.getLocale());
        if (userAgent.toLowerCase(LocaleContextHolder.getLocale()).contains("msie") || StringUtils.contains(userAgent,
                "trident") || userAgent.toLowerCase(LocaleContextHolder.getLocale()).contains("like gecko") || userAgent
                .toLowerCase(LocaleContextHolder.getLocale())
                .contains("mozilla")) {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            fileName = new String(fileName.getBytes(Charset.defaultCharset()), "ISO8859-1");
        } else {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        }
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        ServletOutputStream os = response.getOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(os);
        workbook.write(bos);
        workbook.close();
        bos.close();
        os.close();
    }
}
