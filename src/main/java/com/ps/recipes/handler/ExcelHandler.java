package com.ps.recipes.handler;

import com.ps.recipes.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class ExcelHandler implements FileHandler {

    private final FileStorageService fileStorageService;
    private static final float ROW_HEIGHT = 20f;
    private static final float CELL_PADDING = 5f;
    private static final float FONT_SIZE = 10f;
    private static final float PAGE_MARGIN = 50f;
    private static final float MIN_COLUMN_WIDTH = 80f;

    @Override
    public String getSupportedFileType() {
        return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    }

    @Override
    public void handle(MultipartFile file) throws IOException {
        byte[] pdfBytes = convertExcelToPdf(file.getInputStream());
        fileStorageService.saveFile(pdfBytes, "pdf", "pdf");
    }

    private byte[] convertExcelToPdf(InputStream excelStream) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(excelStream);
             PDDocument document = new PDDocument()) {

            PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            PDType1Font boldFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

            for (Sheet sheet : workbook) {
                float[] columnWidths = calculateColumnWidths(sheet, font);
                processSheet(document, sheet, font, boldFont, columnWidths);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }

    private void processSheet(PDDocument document, Sheet sheet,
                              PDType1Font font, PDType1Font boldFont,
                              float[] columnWidths) throws IOException {
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        float currentY = page.getMediaBox().getHeight() - PAGE_MARGIN;
        boolean newPageCreated = false;

        // Draw header
        currentY = drawHeaderRow(sheet, contentStream, boldFont, columnWidths, currentY);

        // Draw rows
        DataFormatter dataFormatter = new DataFormatter();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header row

            // Check page space
            if (currentY < PAGE_MARGIN) {
                contentStream.close();
                page = new PDPage();
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                currentY = page.getMediaBox().getHeight() - PAGE_MARGIN;
                newPageCreated = true;
            }

            if (newPageCreated) {
                // Redraw header on new page
                currentY = drawHeaderRow(sheet, contentStream, boldFont, columnWidths, currentY);
                newPageCreated = false;
            }

            currentY = drawDataRow(contentStream, font, columnWidths, currentY, row, dataFormatter);
        }

        contentStream.close();
    }

    private float[] calculateColumnWidths(Sheet sheet, PDType1Font font) throws IOException {
        int maxColumns = sheet.getRow(0).getLastCellNum();
        float[] maxWidths = new float[maxColumns];

        for (Row row : sheet) {
            for (Cell cell : row) {
                int colIndex = cell.getColumnIndex();
                String content = new DataFormatter().formatCellValue(cell);
                float textWidth = font.getStringWidth(content) * FONT_SIZE / 1000f;
                float cellWidth = textWidth + 2 * CELL_PADDING;

                if (cellWidth > maxWidths[colIndex]) {
                    maxWidths[colIndex] = Math.max(cellWidth, MIN_COLUMN_WIDTH);
                }
            }
        }
        return maxWidths;
    }

    private float drawHeaderRow(Sheet sheet, PDPageContentStream contentStream,
                                PDType1Font font, float[] columnWidths, float startY) throws IOException {
        Row headerRow = sheet.getRow(0);
        float currentX = PAGE_MARGIN;

        // Header background
        contentStream.setNonStrokingColor(200/255f, 200/255f, 200/255f);
        contentStream.addRect(PAGE_MARGIN, startY - ROW_HEIGHT,
                getTotalWidth(columnWidths), ROW_HEIGHT);
        contentStream.fill();

        // Header text
        contentStream.setNonStrokingColor(0f, 0f, 0f);
        contentStream.setFont(font, FONT_SIZE);

        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            String text = cell != null ? new DataFormatter().formatCellValue(cell) : "";

            contentStream.beginText();
            contentStream.newLineAtOffset(currentX + CELL_PADDING,
                    startY - FONT_SIZE - CELL_PADDING);
            contentStream.showText(truncateText(text, columnWidths[i], font));
            contentStream.endText();

            currentX += columnWidths[i];
        }

        return startY - ROW_HEIGHT - 10f; // Add extra spacing
    }

    private float drawDataRow(PDPageContentStream contentStream, PDType1Font font,
                              float[] columnWidths, float startY,
                              Row row, DataFormatter dataFormatter) throws IOException {
        float currentX = PAGE_MARGIN;

        // Draw cell borders
        contentStream.setStrokingColor(200/255f, 200/255f, 200/255f);
        contentStream.setLineWidth(0.5f);
        contentStream.addRect(PAGE_MARGIN, startY - ROW_HEIGHT,
                getTotalWidth(columnWidths), ROW_HEIGHT);
        contentStream.stroke();

        // Draw cell content
        for (int i = 0; i < columnWidths.length; i++) {
            Cell cell = row.getCell(i);
            String text = cell != null ? dataFormatter.formatCellValue(cell) : "";

            contentStream.beginText();
            contentStream.setFont(font, FONT_SIZE);
            contentStream.newLineAtOffset(currentX + CELL_PADDING,
                    startY - FONT_SIZE - CELL_PADDING);
            contentStream.showText(truncateText(text, columnWidths[i], font));
            contentStream.endText();

            currentX += columnWidths[i];
        }

        return startY - ROW_HEIGHT;
    }

    private String truncateText(String text, float maxWidth, PDType1Font font) throws IOException {
        float availableWidth = maxWidth - 2 * CELL_PADDING;
        float textWidth = font.getStringWidth(text) * FONT_SIZE / 1000f;

        if (textWidth <= availableWidth) return text;

        // Truncate with ellipsis
        String ellipsis = "...";
        float ellipsisWidth = font.getStringWidth(ellipsis) * FONT_SIZE / 1000f;
        availableWidth -= ellipsisWidth;

        for (int i = text.length() - 1; i >= 0; i--) {
            String truncated = text.substring(0, i);
            float width = font.getStringWidth(truncated) * FONT_SIZE / 1000f;
            if (width <= availableWidth) {
                return truncated + ellipsis;
            }
        }
        return ellipsis;
    }

    private float getTotalWidth(float[] columnWidths) {
        float total = 0;
        for (float width : columnWidths) {
            total += width;
        }
        return total;
    }
}