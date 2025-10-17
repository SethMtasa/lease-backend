//package prac.lease.service;
//
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//import prac.lease.model.File;
//
///**
// * Service to handle all email-related notifications.
// */
//@Service
//public class NotificationService {
//
//    private final JavaMailSender mailSender;
//
//    public NotificationService(JavaMailSender mailSender) {
//        this.mailSender = mailSender;
//    }
//
//    /**
//     * Sends a notification email to the assigned KAR.
//     *
//     * @param file The File entity containing details for the email.
//     */
//    public void sendKarAssignmentNotification(File file) {
//        // Build the email message
//        SimpleMailMessage message = new SimpleMailMessage();
//
//        // The recipient's email address comes from the KAR user entity
//        if (file.getKar() != null && file.getKar().getEmail() != null) {
//            message.setTo(file.getKar().getEmail());
//        } else {
//            // Log an error or handle the case where the KAR's email is missing
//            System.err.println("Could not send email notification. KAR email is missing for file: " + file.getFileName());
//            return;
//        }
//
//        // Set email subject and content
//        message.setSubject("New File Assigned: " + file.getFileName());
//        message.setText("Hello " + file.getKar().getFirstName() + ",\n\n"
//                + "A new file has been assigned to you for management.\n\n"
//                + "File Details:\n"
//                + "File Name: " + file.getFileName() + "\n"
//                + "File Type: " + file.getFileType() + "\n"
//                + "Region: " + file.getRegion().getName() + "\n"
//                + "Channel Partner: " + file.getChannelPartner().getName() + "\n"
//                + "Expiry Date: " + file.getExpiryDate() + "\n\n"
//                + "Please review the file and take appropriate action.\n\n"
//                + "Regards,\n"
//                + "File Management System");
//
//        // Send the email
//        try {
//            mailSender.send(message);
//            System.out.println("Notification email sent to KAR: " + file.getKar().getFirstName());
//        } catch (Exception e) {
//            System.err.println("Failed to send email: " + e.getMessage());
//        }
//    }
//}
