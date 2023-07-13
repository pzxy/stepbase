package com.base.core.mvc.web.httpmessage.excel;

import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.merge.AbstractMergeStrategy;

/**
 * @author start
 */
public class DefaultMergeStrategy extends AbstractMergeStrategy {
	
	private Map<Integer,ExportExcelHolder> headHolderMap;
	
	public DefaultMergeStrategy(Map<Integer,ExportExcelHolder> headHolderMap) {
		this.headHolderMap=headHolderMap;
	}
	
    @Override
    protected void merge(Sheet sheet, Cell cell, Head head, Integer relativeRowIndex) {
    	ExportExcelHolder holder=this.headHolderMap.get(cell.getColumnIndex());
    	cell.getRow().setHeight((short)(holder.getAnnCls().height()));
    	cell.getSheet().setColumnWidth(cell.getColumnIndex(), holder.getAnnCls().width());
		Workbook workbook = sheet.getWorkbook();
		CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
		cell.setCellStyle(cellStyle);
    }
    
    
    
}
