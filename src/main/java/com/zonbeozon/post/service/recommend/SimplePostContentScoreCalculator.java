package com.zonbeozon.post.service.recommend;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class SimplePostContentScoreCalculator implements PostContentScoreCalculator {
    private static final double WEIGHT_LENGTH = 2.5;
    private static final double WEIGHT_TTR = 2.0;
    private static final double WEIGHT_STRUCTURE = 1.5;

    @Override
    public double calculate(String content) {
        if (content == null || content.isBlank()) {
            return 0.0;
        }

        double lengthScore = calculateLengthScore(content);
        double ttrScore = calculateTtrScore(content);
        double structureScore = calculateStructureScore(content);

        double finalScore = (lengthScore * WEIGHT_LENGTH) +
                (ttrScore * WEIGHT_TTR) +
                (structureScore * WEIGHT_STRUCTURE);

        return Math.max(0.0, finalScore); //최종 점수가 음수가 되지 않도록 보정
    }


    private double calculateLengthScore(String content) {
        int length = content.length();
        if (length < 300) return 1.0;
        if (length < 1000) return 3.0;
        if (length < 5000) return 5.0;
        return 2.0; // 너무 긴 글은 오히려 가독성이 떨어질 수 있다
    }

    //어휘 다양성 점수
    private double calculateTtrScore(String content) {
        String[] words = content.trim().split("\\s+");
        if (words.length < 50) return 0.0;

        Set<String> uniqueWords = new HashSet<>(Arrays.asList(words));
        double ttr = (double) uniqueWords.size() / words.length;

        return ttr * 10.0;
    }


    private double calculateStructureScore(String content) {
        double score = 0.0;

        //문단 수
        int lineBreaks = content.length() - content.replace("\n", "").length();
        if (lineBreaks > 10) score += 3.0;
        else if (lineBreaks > 3) score += 2.0;

        //평균 문장 길이
        String[] sentences = content.split("[.!?]+");
        if (sentences.length > 1) {
            double avgSentenceLength = (double) content.length() / sentences.length;
            if (avgSentenceLength > 120) {
                score -= 2.0; // 평균 120자가 넘는 긴 문장이면 감점
            } else if (avgSentenceLength < 80) {
                score += 1.0; // 가독성이 좋다고 판단하여 가점
            }
        }
        return score;
    }
}
