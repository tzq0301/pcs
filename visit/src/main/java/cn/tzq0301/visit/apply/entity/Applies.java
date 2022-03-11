package cn.tzq0301.visit.apply.entity;

import cn.tzq0301.util.DateUtils;
import cn.tzq0301.visit.apply.entity.applyrequest.ApplyRequest;
import cn.tzq0301.visit.apply.entity.getapplies.GetApplies;
import cn.tzq0301.visit.apply.entity.unfinished.UnfinishedApply;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    public static Apply newApply(String userId, String name, Integer sex, String birthday, String phone, String email, String identity,
                                 Integer problemId, String problemDetail, String day, Integer from, String address,
                                 List<Integer> scores, String visitorId) {
        int sumScore = getSumScore(scores);
        String scaleResult = String.format(SCALE_RESULT_FORMAT,
                sumScore, TOTAL_SCORE, (double) sumScore / TOTAL_SCORE * 100);
        int order = getOrder(sumScore);

        return new Apply(userId, name, sex, DateUtils.stringToLocalDate(birthday), phone, email, identity, problemId,
                problemDetail, DateUtils.stringToLocalDate(day), from, address, scores, sumScore, scaleResult,
                order, null, ApplyStatusEnum.PENDING_REVIEW.getCode(), visitorId, LocalDate.now());
    }

    public static Apply newApply(UserInfo userInfo, Integer problemId, String problemDetail, String day, Integer from, String address,
                                 List<Integer> scores, String visitorId) {
        return newApply(userInfo.getUserId(), userInfo.getName(), userInfo.getSex(), DateUtils.localDateToString(userInfo.getBirthday()),
                userInfo.getPhone(), userInfo.getEmail(), userInfo.getIdentity(), problemId, problemDetail,
                day, from, address, scores, visitorId);
    }

    public static Apply newApply(UserInfo userInfo, ApplyRequest request) {
        return newApply(userInfo.getUserId(), userInfo.getName(), userInfo.getSex(), DateUtils.localDateToString(userInfo.getBirthday()),
                request.getPhone(), request.getEmail(), userInfo.getIdentity(), request.getProblemId(), request.getProblemDetail(),
                request.getDay(), request.getFrom(), request.getAddress(), request.getScores().stream().map(Integer::parseInt).collect(Collectors.toList()), request.getVisitorId());
    }

    public static GetApplies toGetApplies(Apply apply) {
        return new GetApplies(apply.getId().toString(), apply.getStatus(),
                DateUtils.localDateToString(apply.getCreatedTime()), DateUtils.localDateToString(apply.getDay()),
                apply.getFrom(), apply.getAddress(), ProblemEnum.getName(apply.getProblemId()));
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
