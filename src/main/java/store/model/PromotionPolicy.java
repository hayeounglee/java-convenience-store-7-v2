package store.model;

import camp.nextstep.edu.missionutils.DateTimes;
import store.util.Reader;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PromotionPolicy {
    private int promotionBuyCount;
    private int promotionGetCount;
    private String startDate;
    private String endDate;

    public PromotionPolicy(String productPromotion) {
        promotionBuyCount = 0;
        promotionGetCount = 0;
        startDate = "9999-12-31";
        endDate = "9999-12-31";
        initPromotionInfo(productPromotion);
    }

    private void initPromotionInfo(String productPromotion) {
        Reader reader = new Reader();
        List<String> lines = reader.readLines("src/main/resources/promotions.md").stream().skip(1).toList();
        lines.forEach(line -> updatePolicy(line, productPromotion));
    }

    private void updatePolicy(String line, String productPromotion) {
        String[] promotionInfo = line.split(",");
        String promotionName = promotionInfo[0];
        if (promotionName.equals(productPromotion)) {
            promotionBuyCount = Integer.parseInt(promotionInfo[1]);
            promotionGetCount = Integer.parseInt(promotionInfo[2]);
            startDate = promotionInfo[3];
            endDate = promotionInfo[4];
        }
    }

    public boolean isValidPeriod() {
        LocalDateTime targetTime = DateTimes.now();

        String[] startInfo = startDate.split("-");
        String[] endInfo = endDate.split("-");

        LocalDate targetDate = LocalDate.of(targetTime.getYear(), targetTime.getMonth(), targetTime.getDayOfMonth());
        LocalDate startDate = LocalDate.of(Integer.parseInt(startInfo[0]), Integer.parseInt(startInfo[1]), Integer.parseInt(startInfo[2]));
        LocalDate endDate = LocalDate.of(Integer.parseInt(endInfo[0]), Integer.parseInt(endInfo[1]), Integer.parseInt(endInfo[2]));
        return !targetDate.isBefore(startDate) && !targetDate.isAfter(endDate);
    }

    public int getPromotionCount() {
        return promotionBuyCount + promotionGetCount;
    }

    public int getPromotionBuyCount() {
        return promotionBuyCount;
    }
}
