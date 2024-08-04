package com.pfa.dailyapp.mailing;

import com.itextpdf.text.DocumentException;
import com.pfa.dailyapp.entities.Task;
import com.pfa.dailyapp.entities.User;
import com.pfa.dailyapp.enums.TaskStatus;
import com.pfa.dailyapp.repositories.TaskRepository;
import com.pfa.dailyapp.repositories.UserRepository;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class ScheduledTasks {
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private EmailSenderService emailSenderService;
    private PdfReportService pdfReportService;
    private TaskRepository taskRepository;
    private UserRepository userRepository;

    @Scheduled(cron = "0 0 21 * * ?")
    public void sendDailyReports() {
        log.info("Envoi des rapports");

        List<User> users = userRepository.findAll();
        for (User user : users) {
            List<Task> inProgressTasks = taskRepository.findAllByUser_UserIdAndStatus(user.getUserId(), TaskStatus.IN_PROGRESS);
            List<Task> completedTasks = taskRepository.findAllByUser_UserIdAndStatusAndUpdatedAtAfter(user.getUserId(), TaskStatus.DONE, LocalDate.now().atStartOfDay());

            try {
                byte[] pdfContent = pdfReportService.generateDailyReport(user, completedTasks, inProgressTasks);
                emailSenderService.sendDailyReportWithAttachment(user.getEmail(), "Rapport de Tâches Quotidien", "Veuillez trouver ci-joint le rapport des tâches.", pdfContent);
            } catch (DocumentException | IOException | MessagingException e) {
                e.printStackTrace();
                log.error(e.getMessage());
            }
        }

        log.info("Rapports envoyés");
    }
}