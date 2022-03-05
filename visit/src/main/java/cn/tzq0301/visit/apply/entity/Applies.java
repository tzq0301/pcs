package cn.tzq0301.visit.apply.entity;

import cn.tzq0301.util.DateUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * @author tzq0301
 * @version 1.0
 */
public final class Applies {
    private static final int NORMAL_SCORES_NUM = 80;

    private static final int SPECIAL_SCORES_NUM = 4;

    private static final int TOTAL_SCORES_NUM = NORMAL_SCORES_NUM + SPECIAL_SCORES_NUM;

    private static final int SCORE_FACTORY = 20;

    private static final int EACH_SCORE = 4;

    private static final int TOTAL_SCORE = EACH_SCORE * NORMAL_SCORES_NUM + EACH_SCORE * SCORE_FACTORY * SPECIAL_SCORES_NUM;

    private static final String SCALE_RESULT_FORMAT = "量表得分 %s / 量表总分 %s = %2.2f%%";

    public static Apply newApply(String userId, String phone, String email, ProblemEnum problemId, String problemDetail,
                                 String day, Integer from, String address, List<Integer> scores) {
        return newApply(userId, phone, email, problemId.getCode(), problemDetail, day, from, address, scores);
    }

    public static Apply newApply(String userId, String phone, String email, Integer problemId, String problemDetail,
                                 String day, Integer from, String address, List<Integer> scores) {
        int sumScore = getSumScore(scores);
        String scaleResult = String.format(SCALE_RESULT_FORMAT,
                sumScore, TOTAL_SCORE, (double) sumScore / TOTAL_SCORE * 100);
        int order = getOrder(sumScore);

        return new Apply(userId, phone, email, problemId, problemDetail, DateUtils.stringToLocalDate(day),
                from, address, scores, sumScore, scaleResult, order, null,
                ApplyStatusEnum.PENDING_REVIEW.getCode(), LocalDate.now());
    }

    private static int getSumScore(List<Integer> scores) {
        return scores.subList(0, NORMAL_SCORES_NUM).stream().mapToInt(Integer::intValue).sum()
                + scores.subList(NORMAL_SCORES_NUM, TOTAL_SCORES_NUM).stream()
                .mapToInt(Integer::intValue).map(it -> it * SCORE_FACTORY).sum();
    }

    private static int getOrder(int sumScore) {
        if (sumScore > TOTAL_SCORE * 9 / 10) {
            return 3;
        } else if (sumScore > TOTAL_SCORE * 7 / 10) {
            return 2;
        } else {
            return 1;
        }
    }

    private Applies() {
    }
}
