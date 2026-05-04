package com.proje.config;

import com.proje.entity.Problem;
import com.proje.entity.ProblemStatus;
import com.proje.entity.Role;
import com.proje.entity.User;
import com.proje.repository.ProblemRepository;
import com.proje.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Configuration
public class DataInitializer {

    @Bean
    public ApplicationRunner seedUsers(UserRepository userRepository,
                                       ProblemRepository problemRepository,
                                       PasswordEncoder passwordEncoder) {
        return args -> {
            createUserIfMissing(userRepository, passwordEncoder, "admin", "admin123", Role.ADMIN);
            User demoUser = createUserIfMissing(userRepository, passwordEncoder, "user", "user123", Role.USER);

            if (problemRepository.count() == 0) {
                createProblem(problemRepository, demoUser,
                        "Parkta kirik bank",
                        "Mahalle parkindaki oturma banki kirilmis durumda. Cocuk oyun alaninin yaninda oldugu icin risk olusturuyor.",
                        "Cevre",
                        ProblemStatus.APPROVED,
                        "#2f80ed",
                        "#56ccf2",
                        "PARK");

                createProblem(problemRepository, demoUser,
                        "Ana yolda cukur",
                        "Otobus duragi yakinindaki yol uzerinde buyuk bir cukur var. Araclar ani manevra yapmak zorunda kaliyor.",
                        "Ulasim",
                        ProblemStatus.PENDING,
                        "#f2994a",
                        "#f2c94c",
                        "YOL");

                createProblem(problemRepository, demoUser,
                        "Tasmakta olan cop konteyneri",
                        "Sokak basindaki cop konteyneri uzun suredir bosaltilmadi. Kotu koku ve hijyen sorunu olustu.",
                        "Temizlik",
                        ProblemStatus.REJECTED,
                        "#27ae60",
                        "#6fcf97",
                        "COP");

                demoUser.setScore(10);
                userRepository.save(demoUser);
            }
        };
    }

    private User createUserIfMissing(UserRepository userRepository,
                                     PasswordEncoder passwordEncoder,
                                     String username,
                                     String rawPassword,
                                     Role role) {
        return userRepository.findByUsername(username).orElseGet(() -> {
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(rawPassword));
            user.setRole(role);
            user.setScore(0);
            return userRepository.save(user);
        });
    }

    private void createProblem(ProblemRepository problemRepository,
                               User owner,
                               String title,
                               String description,
                               String category,
                               ProblemStatus status,
                               String firstColor,
                               String secondColor,
                               String label) throws IOException {
        Problem problem = new Problem();
        problem.setOwner(owner);
        problem.setTitle(title);
        problem.setDescription(description);
        problem.setCategory(category);
        problem.setStatus(status);
        problem.setScoreAwarded(ProblemStatus.APPROVED.equals(status));
        problem.setImageContentType("image/png");
        problem.setImageData(createDemoImage(firstColor, secondColor, label));
        problemRepository.save(problem);
    }

    private byte[] createDemoImage(String firstColor, String secondColor, String label) throws IOException {
        int width = 900;
        int height = 520;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setPaint(new GradientPaint(0, 0, Color.decode(firstColor), width, height, Color.decode(secondColor)));
        graphics.fillRect(0, 0, width, height);

        graphics.setColor(new Color(255, 255, 255, 50));
        graphics.fillOval(-120, 250, 360, 360);
        graphics.fillOval(650, -120, 340, 340);

        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("SansSerif", Font.BOLD, 88));
        graphics.drawString(label, 80, 290);

        graphics.setFont(new Font("SansSerif", Font.PLAIN, 30));
        graphics.drawString("Ornek bildirim gorseli", 84, 345);
        graphics.dispose();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        return outputStream.toByteArray();
    }
}
