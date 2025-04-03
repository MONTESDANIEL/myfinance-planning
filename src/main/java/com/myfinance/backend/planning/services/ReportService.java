package com.myfinance.backend.planning.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.List;

import org.xhtmlrenderer.pdf.ITextRenderer;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.myfinance.backend.planning.entities.Movements.AppPdf;
import com.myfinance.backend.planning.entities.Movements.ResponseAppMovements;

@Service
public class ReportService {

    private final MovementClient movementClient;

    public ReportService(MovementClient movementClient) {
        this.movementClient = movementClient;
    }

    public byte[] generateMovementsReport(AppPdf appPdf) throws IOException {
        // Extract data from the AppPdf object
        int year = appPdf.getYear();
        int startMonth = appPdf.getStartMonth();
        int endMonth = appPdf.getEndMonth();
        String type = appPdf.getType();
        String token = appPdf.getToken();

        List<ResponseAppMovements> movements = movementClient.getMovements(token);

        List<ResponseAppMovements> filteredMovements = movements.stream()
                .filter(movement -> {
                    LocalDate date = movement.getDate();
                    boolean matchesYear = date.getYear() == year;
                    boolean matchesMonth = date.getMonthValue() >= startMonth && date.getMonthValue() <= endMonth;
                    boolean matchesType = type.equalsIgnoreCase("all")
                            || type.equalsIgnoreCase(movement.getMovementType());
                    return matchesYear && matchesMonth && matchesType;
                })
                .toList();

        // 3. Create the HTML content for the report
        String htmlContent = generateReportHtml(filteredMovements, year, startMonth, endMonth, type);

        // 4. Generate PDF using Flying Saucer with try-catch for exception handling
        try {
            return generatePdfFromHtml(htmlContent);
        } catch (IOException e) {
            System.err.println("Error generando PDF: " + e.getMessage());
            // You can also log the error or throw a custom exception here
            throw e; // Re-throw the exception to indicate the failure
        }
    }

    private static final String TEMPLATE_NAME = "pdf.html";

    public String generateReportHtml(List<ResponseAppMovements> movements, int year, int startMonth, int endMonth,
            String type) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("year", year);
        context.setVariable("startMonth", startMonth);
        context.setVariable("endMonth", endMonth);
        context.setVariable("type", type);
        context.setVariable("movements", movements);

        StringWriter writer = new StringWriter();
        templateEngine.process(TEMPLATE_NAME, context, writer);

        return writer.toString();
    }

    private byte[] generatePdfFromHtml(String htmlContent) throws IOException {

        try {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            renderer.createPDF(outputStream);

            return outputStream.toByteArray();
        } catch (Exception e) {
            return null;
        }

    }
}