package com.pfa.dailyapp.mailing;

import com.itextpdf.text.*;
import com.pfa.dailyapp.entities.Task;
import com.pfa.dailyapp.entities.User;
import com.pfa.dailyapp.enums.TaskPriority;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PdfReportService {

    @Value("${logo.path}")
    private String logoPath;

    public byte[] generateDailyReport(User user, List<Task> completedTasks, List<Task> inProgressTasks) throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH:mm");
        PdfWriter.getInstance(document, baos);
        document.open();

        Image logo = Image.getInstance(logoPath);
        logo.scaleToFit(150, 150);
        logo.setAlignment(Image.ALIGN_CENTER);
        document.add(logo);

        document.add(new Paragraph("\n"));

        Paragraph title = new Paragraph("Rapport des tâches du jour", new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD));
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Nom : " + user.getFirstname() + " " + user.getLastname()));
        document.add(new Paragraph("Date : " + LocalDate.now()));

        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Tâches en Cours :"));

        document.add(new Paragraph("\n"));

        PdfPTable inProgressTable = new PdfPTable(4);
        inProgressTable.addCell("Titre");
        inProgressTable.addCell("Description");
        inProgressTable.addCell("Priorité");
        inProgressTable.addCell("Date de Création");

        for (Task task : inProgressTasks) {
            inProgressTable.addCell(task.getTitle());
            inProgressTable.addCell(convertHtmlToText(task.getDescription()));
            inProgressTable.addCell(task.getPriority() == TaskPriority.HIGH ? "Urgente" : task.getPriority() == TaskPriority.MEDIUM ? "Moyenne" : "Normale");
            inProgressTable.addCell(task.getCreatedAt().format(formatter));
        }
        document.add(inProgressTable);

        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Tâches Complétées :"));

        document.add(new Paragraph("\n"));

        PdfPTable completedTable = new PdfPTable(3);
        completedTable.addCell("Titre");
        completedTable.addCell("Description");
        completedTable.addCell("Complétée en");

        for (Task task : completedTasks) {
            completedTable.addCell(task.getTitle());
            completedTable.addCell(convertHtmlToText(task.getDescription()));
            completedTable.addCell(task.getUpdatedAt().format(formatter));
        }
        document.add(completedTable);

        document.close();

        return baos.toByteArray();
    }

    private String convertHtmlToText(String html) {
        org.jsoup.nodes.Document document = Jsoup.parse(html);
        return document.text();
    }
}