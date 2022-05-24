package com.thana.hwapp;

import com.thana.hwapp.utils.Decoding;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.thana.hwapp.HomeworkApplication.HEIGHT;
import static com.thana.hwapp.HomeworkApplication.WIDTH;

public class CommonHelper {

    public static JTextField studentId;

    public static List<String> getStudents() {
        return Arrays.stream(Decoding.decodeStudentId().split(",")).collect(Collectors.toList());
    }

    public static boolean hasLogin() {
        return verify(studentId.getText());
    }

    public static boolean verify(String text) {
        return getStudents().contains(text);
    }

    public static void initButtons(JFrame frame) {
        JTextField field = new JTextField();
        field.setBounds(WIDTH / 2 - 50, HEIGHT / 2 + 21, 100, 30);
        field.setSize(100, 30);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setVisible(false);

        studentId = field;
        frame.add(field);
    }

    public static void tickField() {
        if (studentId.isVisible()) {
            studentId.setForeground(!verify(studentId.getText()) ? Color.decode("#EC4040") : Color.BLACK);
        }
    }
}
